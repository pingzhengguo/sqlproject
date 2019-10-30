package com.pzg.code.sqlproject.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pzg.code.sqlproject.dynamic.DataDynamic;
import com.pzg.code.sqlproject.dynamic.MysqlDataDynamic;
import com.pzg.code.sqlproject.dynamic.OracleDataDynamic;
import com.pzg.code.sqlproject.dynamic.PhoenixDataDynamic;
import com.pzg.code.sqlproject.mapper.ApiOracleDao;
import com.pzg.code.sqlproject.mapper.StoreListMapper;
import com.pzg.code.sqlproject.service.ApiService;
import com.pzg.code.sqlproject.utils.RequestResult;
import com.pzg.code.sqlproject.vo.StContentBean;
import com.pzg.code.sqlproject.vo.StoreListQueryVO;
import com.pzg.code.sqlproject.vo.StoreListVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述：api相关查询处理信息
 */
@Service
@Slf4j
public class ApiServiceImpl implements ApiService {
    @Autowired
    private StoreListMapper storeListMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    ApiOracleDao apiOracleDao;

    private final String HBASE = "hbase";
    private final String ORACLE = "oracle";
    private final String MYSQL = "mysql";
    String tableName = "";
    String url="";
    String user="";
    String password="";

    /**
     * 获得表信息
     * @param slId
     * @return
     */
    @Override
    public RequestResult getTableInfo(String slId, String account) {
        StoreListQueryVO query = new StoreListQueryVO();
        query.setSlId(slId);
        List<StoreListVO> storeListVOList = storeListMapper.findByParam(query);

        if (CollectionUtils.isEmpty(storeListVOList)) {
            return new RequestResult(false, "没有查到数据");
        }
        StoreListVO storeListVO = storeListVOList.get(0);
        String stContent = storeListVO.getSlcContent();

        List<StContentBean> listContent = new ArrayList<StContentBean>();
        //把String转换为json
        JSONArray jsonArray = JSONArray.fromObject(stContent);
//        listContent = JSONArray.toList(jsonArray,StContentBean.class);//这里的t是Class<T>
        listContent = JSONArray.toList(jsonArray,new StContentBean(),new JsonConfig());

        log.info(storeListVO.getSlcContent());
        //找到具体表（后期改造为自动配置ip和端口及加载驱动）
        tableName = storeListVO.getSlTableName();
        if ("".equals(tableName)){
            return new RequestResult(false, "没有查到数据");
        }

        putParam(listContent);
//        addLog(tableName,"",account);
//        DBIdentifier.setProjectCode(url);
        DataDynamic daoSource = null;
        //如果是phoenix调用phoenix的dao层数据查询
        if (HBASE.equals(storeListVO.getStName())){
//            List<Map> list = apiPhoenixDao.selectTableData(tableName);
            daoSource = new PhoenixDataDynamic(url, user,password);
        }
        //如果是oracle调用oracle的dao层数据查询
        else if (ORACLE.equals(storeListVO.getStName())){
            daoSource = new OracleDataDynamic(url,user,password);
        }
        else if (MYSQL.equals(storeListVO.getStName())){
            daoSource = new MysqlDataDynamic(url,user,password);
        }else{
            return new RequestResult(false, null);
        }
        try{
            daoSource.getConnection();
            List<Map> list =  daoSource.selectTableInfo(tableName);
            return new RequestResult(true, list);
        }catch (Exception e){
            e.printStackTrace();
            return new RequestResult(false, "获取数据库连接查询失败");
        }
    }

    /**
     * 获得表数据
     * @param slId
     * @return
     */
    @Override
    public RequestResult getTableDatas(String slId,Integer pageNum,Integer pageSize,String account) {
        StoreListQueryVO query = new StoreListQueryVO();
        query.setSlId(slId);
        List<StoreListVO> storeListVOList = storeListMapper.findByParam(query);

        if (CollectionUtils.isEmpty(storeListVOList)) {
            return new RequestResult(false, "没有查到数据");
        }
        StoreListVO storeListVO = storeListVOList.get(0);
        String stContent = storeListVO.getSlcContent();

        List<StContentBean> listContent = new ArrayList<StContentBean>();
        //把String转换为json
        JSONArray jsonArray = JSONArray.fromObject(stContent);
        listContent = JSONArray.toList(jsonArray,new StContentBean(),new JsonConfig());

        log.info(storeListVO.getSlcContent());
        //找到具体表（后期改造为自动配置ip和端口及加载驱动）
        tableName = storeListVO.getSlTableName();
        putParam(listContent);
        int listSize = 0;
        DataDynamic daoSource = null;
        //如果是phoenix调用phoenix的dao层数据查询
        if (HBASE.equals(storeListVO.getStName())){
            daoSource = new PhoenixDataDynamic(url, user,password);
        }
        //如果是oracle调用oracle的dao层数据查询
        else if (ORACLE.equals(storeListVO.getStName())){
            //找到具体表（后期改造为自动配置ip和端口及加载驱动）
            daoSource = new OracleDataDynamic(url,user,password);
        }
        else if (MYSQL.equals(storeListVO.getStName())){
            daoSource = new MysqlDataDynamic(url,user,password);
        }else{
            return new RequestResult(false, null);
        }
//        addLog(tableName,String.valueOf(listSize),account,slId);
        try {
            daoSource.getConnection();
            PageHelper.startPage(pageNum,pageSize);
            List<Map> list = daoSource.selectTableData(tableName,pageNum,pageSize);
            listSize = list.size();
            return new RequestResult(true, new PageInfo<>(list));
        }catch (Exception e){
            log.error("数据库执行错误：",e);
            return new RequestResult(false, "获取数据库连接查询失败");
        }
    }
    /**
     * 获得表数据量
     * @param slId
     * @return
     */
    @Override
    public RequestResult getTableDataCount(String slId,String account) {
        StoreListQueryVO query = new StoreListQueryVO();
        query.setSlId(slId);
        List<StoreListVO> storeListVOList = storeListMapper.findByParam(query);

        if (CollectionUtils.isEmpty(storeListVOList)) {
            return new RequestResult(false, "没有查到数据");
        }
        StoreListVO storeListVO = storeListVOList.get(0);
        String stContent = storeListVO.getSlcContent();

        List<StContentBean> listContent = new ArrayList<StContentBean>();
        //把String转换为json
        JSONArray jsonArray = JSONArray.fromObject(stContent);
        listContent = JSONArray.toList(jsonArray,new StContentBean(),new JsonConfig());

        log.info(storeListVO.getSlcContent());
        //找到具体表（后期改造为自动配置ip和端口及加载驱动）
        tableName = storeListVO.getSlTableName();

        putParam(listContent);
        DataDynamic daoSource = null;
        //如果是phoenix调用phoenix的dao层数据查询
        if (HBASE.equals(storeListVO.getStName())){
            daoSource = new PhoenixDataDynamic(url, user,password);
        }
        //如果是oracle调用oracle的dao层数据查询
        else if (ORACLE.equals(storeListVO.getStName())){

            //找到具体表（后期改造为自动配置ip和端口及加载驱动）
            daoSource = new OracleDataDynamic(url,user,password);
        }else if (ORACLE.equals(storeListVO.getStName())){
            daoSource = new MysqlDataDynamic(url,user,password);
        }else {
            return new RequestResult(false, null);
        }
        try{
            if(!"admin_from_inner".equals(account)){
//                addLog(tableName,"",account,slId);
            }
            daoSource.getConnection();
            Map list = daoSource.selectTableDataCount(tableName).get(0);
            return new RequestResult(true, list);
        }catch (Exception e){
            log.error("数据库执行错误：",e);
            return new RequestResult(false, "获取数据库连接查询失败");
        }
    }

    /**
     * 日志添加
     * @param tableName
     * @param size
     * @return
     */
//    public boolean addLog(String tableName,String size,String account,String slId){
//        String ip = IpUtil.getIpAddr(request);
//        log.info("ip地址："+ ip);
//        DataSharingLog log = new DataSharingLog();
//        //api查询
//        log.setDataUsageType(DataUsageTypeEnum.API.getCode());
//        log.setOpDesc(DataUsageTypeEnum.API.getName());
//        log.setOpTime(new Date());
//        log.setOpUser(account);
//        log.setOpIp(ip);
//        log.setRlId(getRlid(tableName));
//        log.setVisitDataSize(size);
//        log.setSlId(slId);
//        return dataSharingLogService.saveLog(log);
//    }

    /**
     * 根据表名查询资源表id
     * @param tableName 表名
     * @return
     */
    public String getRlid(String tableName){
        Map map = apiOracleDao.selectRlidByTablename(tableName);
        if(!map.isEmpty()){
            tableName = (String) map.get("RL_ID");
        }
        return tableName;
    }

    public void putParam(List<StContentBean> listContent){
        for (StContentBean bean : listContent){

            if ("Database Connection URL".equals(bean.getName())){
                url=bean.getValue();
            }
            if ("Database User".equals(bean.getName())){
                user=bean.getValue();
            }
            if ("Password".equals(bean.getName())){
                password=bean.getValue();
            }
        }
    }

}
