package com.pzg.code.sqlproject.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelBatchExportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pzg.code.sqlproject.exception.ServiceException;
import com.pzg.code.sqlproject.mapper.DataAccessMapper;
import com.pzg.code.sqlproject.mapper.SqlMapper;
import com.pzg.code.sqlproject.service.ISqlService;
import com.pzg.code.sqlproject.service.SqlQueryService;
import com.pzg.code.sqlproject.utils.RequestResult;
import com.pzg.code.sqlproject.utils.excel.ExportPO;
import com.pzg.code.sqlproject.utils.excel.FileUtils;
import com.pzg.code.sqlproject.utils.excel.MyExcelStyle;
import com.pzg.code.sqlproject.vo.SQLExportVO;
import com.pzg.code.sqlproject.vo.Sql;
import com.pzg.code.sqlproject.vo.SqlQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**描述：sql服务
 * @since
 */
@Service
@Slf4j
public class SqlServiceImpl implements ISqlService {

    @Autowired
    private SqlMapper sqlMapper;

    @Autowired
    private DataAccessMapper dataAccessMapper;

    @Autowired
    private SqlQueryService sqlQueryService;

    @Autowired
    private AsyncTaskExecutor defaultThreadPool;


    /**
    * Title: findByParam
    *Description: 条件分页查询sql服务
    * @param sqlQueryVO
    * @return 
    */
    @Override
    public PageInfo<Sql> findByParam(SqlQueryVO sqlQueryVO) {
        PageHelper.startPage(sqlQueryVO.getPageNum(), sqlQueryVO.getPageSize());
        List<Sql> slqList = sqlMapper.findByParam(sqlQueryVO);
        return PageInfo.of(slqList);
    }


    /**
    * Title: exportExcel
    *Description: sql服务到处到excel中
    * @param req
    * @param resp
    * @param vo 
    */
    @Override
    public void exportExcel(HttpServletRequest req, HttpServletResponse resp, SQLExportVO vo) {
        File dir = null;

        try {
            //生成文件夹
            String dirName = FileUtils.genUniqueName();
            dir = new File(System.getProperty("user.dir") + File.separator + "excel-temp" + File.separator + dirName);
            try {
                FileUtils.forceMkdir(dir);
            } catch (IOException e) {
                log.error("SqlServiceImpl.exportExcel() has error!{}", e);
            }

            //生成统一基础文件名
            Map storePropertis = dataAccessMapper.findStorePropertis(vo.getStorageId());
            String tableName = MapUtils.getString(storePropertis, "TABLENAME", "unknown");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String excelNamePrefix = tableName + "_" + LocalDateTime.now().format(dateTimeFormatter) + "_";

            long starTime = System.currentTimeMillis();
            exportByParallel(vo, dir, excelNamePrefix, tableName + ".zip", req, resp);
            long endTime = System.currentTimeMillis();

            log.info("excel导出总耗时：{}秒！",(endTime-starTime)*1.0D/1000);


        } catch (Exception e) {
            log.error("SqlServiceImpl.exportExcel() has error!{}",e);
            //这里统一处理成空的文件
            FileUtils.exportEmptyExcel("unknown.xlsx","SQL查询数据",req,resp);

        }finally{
            //文件夹清理工作
            if(dir !=null && dir.exists()){
                FileUtils.deleteQuietly(dir);
            }
        }
    }

    /**
     * 描述：检验导出数据
     * 详设编号：
     *
     * @param requestResult
     * @return boolean
     * @throws
     */
    private boolean checkResult(RequestResult requestResult){

        if(requestResult==null){
            return false;
        }else{
            if(!requestResult.isSuccess()){
                return false;
            }else {
                Map map =(Map)requestResult.getResult();
                if(map == null){
                    return false;
                }else{
                    List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.getObject(map, "data", null);
                    if(list == null || list.isEmpty()){
                       return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 描述：单个导出文件
     *
     * @param exportParams
     * @param entities
     * @param list
     * @param tempDir
     * @param excelNamePrefix
     * @param index
     * @return void
     * @throws
     */
    private void singleExport(ExportParams exportParams,
                              List<ExcelExportEntity> entities,
                              List<Map<String, Object>> list,
                              String tempDir,
                              String excelNamePrefix,
                              int index){
        ExcelBatchExportService batchService = ExcelBatchExportService.getExcelBatchExportService(exportParams, entities);
        Workbook wb = batchService.appendData(list);
        ExcelExportUtil.closeExportBigExcel();

        try (FileOutputStream fos = new FileOutputStream(tempDir + File.separator + excelNamePrefix + index + ".xlsx")) {
            wb.write(fos);
            fos.flush();
        } catch (Exception e) {
            log.error("SqlServiceImpl.singleExport() has error!{}", e);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    log.error("SqlServiceImpl.singleExport() has error!{}", e);
                }
            }
        }
    }


    /**
     * 描述：多线程分批导出EXCEL
     *
     * @param params
     * @param tempDir
     * @param excelNamePrefix
     * @param zipName
     * @param req
     * @param resp
     * @return void
     * @throws
     */
    private void exportByParallel(SQLExportVO params,
                                  File tempDir,
                                  String excelNamePrefix,
                                  String zipName,
                                  HttpServletRequest req,
                                  HttpServletResponse resp) throws Exception {

        String tempDirPath = tempDir.getAbsolutePath();
        Integer startPageNum = params.getStartPage();
        Integer endPageNum = params.getEndPage();
        int pageSize = params.getPageSize();
        int split = params.getSplit();
        String storeId = params.getStorageId();
        String sql = params.getSql();

        if (startPageNum != null && endPageNum != null) {
            Map<String, Object> queryMap = Maps.newHashMap();
            queryMap.put("startPage", startPageNum);
            queryMap.put("endPage", endPageNum);
            queryMap.put("pageSize", pageSize);
            queryMap.put("sql", sql);
            queryMap.put("storageId", storeId);
            long startTime = System.currentTimeMillis();
            //这里可能比较耗时
            RequestResult requestResult = sqlQueryService.sqlQueryByStoreListConnection(queryMap);
            long endTime = System.currentTimeMillis();
            log.info("多线程，SQL查询耗时:{}秒！",(endTime-startTime)*1.0D/1000);
            boolean checkResult = checkResult(requestResult);
            if(!checkResult){
                throw new ServiceException("没有数据可以导出！");
            }

            long startTime1 = System.currentTimeMillis();
            List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.getObject((Map) requestResult.getResult(), "data", null);
            int len = list.size();
            int subsection = (len - 1) / split + 1;

            //获取所有的字段信息，组织excel参数
            Map<String, Object> first = list.get(0);
            List<ExcelExportEntity> entities = Lists.newArrayListWithExpectedSize(first.size());
            for (Map.Entry<String, Object> entry : first.entrySet()) {
                entities.add(new ExcelExportEntity(entry.getKey(), entry.getKey()));
            }
            ExportParams exportParams = new ExportParams(null, "SQL查询结果", ExcelType.XSSF);
            exportParams.setStyle(MyExcelStyle.class);

            //分段导出到各个文件
            if (subsection > 1) {
                final ConcurrentLinkedQueue<ExportPO<Map<String,Object>>> queue = new ConcurrentLinkedQueue<>();
                for(int index =1;index<=subsection;index++){
                    int from = (index-1)*split;
                    int to = from + split > len ? len : (from + split);
                    queue.add(new ExportPO<>(index,list.subList(from,to)));
                }

                int threads = subsection < 5 ? subsection : 5;
                CountDownLatch countDownLatch = new CountDownLatch(threads);
                for(int i = 1;i<=threads;i++){
                    defaultThreadPool.execute(()->{
                        while(true){
                            ExportPO<Map<String,Object>> exportPO = queue.poll();
                            if(exportPO==null){
                                countDownLatch.countDown();
                                break;
                            }else{
                                //导出到excel
                                singleExport(exportParams,entities,exportPO.getDataList(),tempDirPath,excelNamePrefix,exportPO.getIndex());
                            }
                        }
                    });
                }
                countDownLatch.await(60, TimeUnit.SECONDS);
            }else{
                singleExport(exportParams, entities, list, tempDirPath, excelNamePrefix, 1);
            }

            list.clear();
            //将文件打包
            try (OutputStream os = resp.getOutputStream()) {
                //判断文件夹文件数
                File[] files = tempDir.listFiles((pathname) -> pathname.isFile() && pathname.getName().endsWith(".xlsx"));
                if (files != null) {
                    if (files.length == 1) {
                        FileUtils.setExcelDownloadResponseHeader(req, resp, files[0].getName());
                        FileUtils.writeExcelToOutputStream(files[0], os);
                        FileUtils.deleteQuietly(tempDir);
                    } else {
                        FileUtils.setZipDownloadResponseHeader(req, resp, zipName);
                        //已经自带删除临时文件夹功能
                        FileUtils.writeZipToOutputStream(tempDir, os);
                    }
                }

                long endTime1 = System.currentTimeMillis();
                log.info("多线程导出Excel耗时：{}秒！",(endTime1-startTime1)*1.0D/1000);

            } catch (IOException e) {
                log.error("SqlServiceImpl.exportByParallel() has error!{}", e);
                throw e;
            }

        }
    }

}
