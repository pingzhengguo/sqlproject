package com.pzg.code.sqlproject.controller;

import com.github.pagehelper.PageInfo;
import com.pzg.code.sqlproject.exception.ServiceException;
import com.pzg.code.sqlproject.service.ApiService;
import com.pzg.code.sqlproject.service.ISqlService;
import com.pzg.code.sqlproject.service.SqlQueryService;
import com.pzg.code.sqlproject.utils.HyDataPageInfo;
import com.pzg.code.sqlproject.utils.RequestResult;
import com.pzg.code.sqlproject.utils.ResultBuilder;
import com.pzg.code.sqlproject.vo.SQLExportVO;
import com.pzg.code.sqlproject.vo.Sql;
import com.pzg.code.sqlproject.vo.SqlQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName :  SqlQueryController
 * @Author : PZG
 * @Date : 2019-10-30   下午 02:18
 * @Description :
 */
@Slf4j
@RestController
@RequestMapping("/sql")
@Api(tags = "sql服务")
public class SqlQueryController {
    private static Logger logger = LoggerFactory.getLogger(SqlQueryController.class);

    @Autowired
    private SqlQueryService sqlQueryService;
    @Autowired
    private ISqlService sqlService;

    @Autowired
    private ApiService apiService;

    /**
     * @param sqlQueryVO
     * @return
     * @Title: selectSql
     * @Description: TODO(分页获取所有的sql服务)
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页获取所有的数据", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBuilder<HyDataPageInfo<Sql>> selectSql(SqlQueryVO sqlQueryVO) {
        log.info("parameter is :" + sqlQueryVO);
        if (sqlQueryVO == null) {
            throw new ServiceException("传入的参数sqlQueryVO有误");
        }
        PageInfo<Sql> pageInfo = sqlService.findByParam(sqlQueryVO);
        pageInfo.setSize(pageInfo.getPageSize());//解决框架的问题，返回的pagesize是固定的一页10条，而不是一条实际的返回条数
        if (pageInfo == null) {
            return ResultBuilder.fail(null);
        }
        List<Sql> sqlList = pageInfo.getList();
        for (Sql sql : sqlList) {
            RequestResult tableDataCount = apiService.getTableDataCount(sql.getSlId(), "admin_from_inner");
            if (tableDataCount.isSuccess()) {
                Map<String, Object> result = (Map<String, Object>) tableDataCount.getResult();
                if (result != null && result.size() > 0) {
                    String countStr = "" + result.get("count");
                    if (countStr != null) {
                        long count = Long.parseLong(countStr);
                        sql.setSMount(count);
                    }
                }
            }
        }
        HyDataPageInfo<Sql> hyDataPageInfo = HyDataPageInfo.of(pageInfo);
        return ResultBuilder.success(hyDataPageInfo);
    }

    /**
     * 描述：SQL服务查询数据导出
     *
     * @param req
     * @param resp
     * @param vo
     * @return void
     * @throws
     */
    @PostMapping("/export")
    public void export(HttpServletRequest req,
                       HttpServletResponse resp, SQLExportVO vo) {
        sqlService.exportExcel(req, resp, vo);
    }

    /**
     * 根据用户sql语句查询
     */
    @RequestMapping(value = "/query", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public RequestResult sqlQuery(@RequestBody Map<String, Object> params) {
        return sqlQueryService.sqlQueryByStoreListConnection(params);
    }

    /**
     * 根据表名查询字段信息
     */
    @RequestMapping(value = "/{storageId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public RequestResult getFields(@PathVariable("storageId") String tableName) {
        return sqlQueryService.getFieldsByTableName(tableName);
    }
}
