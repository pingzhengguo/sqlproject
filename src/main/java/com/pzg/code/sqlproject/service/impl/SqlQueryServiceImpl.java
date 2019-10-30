package com.pzg.code.sqlproject.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pzg.code.sqlproject.mapper.DataAccessMapper;
import com.pzg.code.sqlproject.driverUrlProperties.MySqlDriverUrlProperties;
import com.pzg.code.sqlproject.driverUrlProperties.OracleDriverUrlProperties;
import com.pzg.code.sqlproject.driverUrlProperties.PhoenixDriverUrlProperties;
import com.pzg.code.sqlproject.operator.BaseDBOperator;
import com.pzg.code.sqlproject.operator.MySqlOperator;
import com.pzg.code.sqlproject.operator.OracleOperator;
import com.pzg.code.sqlproject.operator.PhoenixOperator;
import com.pzg.code.sqlproject.service.SqlQueryService;
import com.pzg.code.sqlproject.utils.RequestResult;
import com.pzg.code.sqlproject.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SqlQueryServiceImpl implements SqlQueryService {

    @Autowired
    private DataAccessMapper dataAccessMapper;

//    @Autowired
//    private DataSharingLogService dataSharingLogService;

    @Autowired
    private ApiServiceImpl apiService;

    @Value("${oracle.instance.type}")
    private String oracleType;

    /**
     * 根据连接信息动态查询数据库
     * @return
     */
    @Override
    public RequestResult sqlQueryByStoreListConnection(Map<String, Object> params) {
//        jdbc:oracle:thin:@192.168.1.170:1521:ICATI
//        jdbc:phoenix:192.168.1.74:2181:/hbase-unsecure
//        jdbc:mysql://localhost:3306/test

//        返回前端结果集
        Map<String,Object> allMap = new HashMap();
//        数据结果集
        List<Map<String, Object>> data = new ArrayList<>();
//        数据总量
        int totalCount = 0;
//        错误信息
        String errorMsg = "";
//        获取前台参数
        int startPage = (int)params.getOrDefault("startPage" ,0);
        int endPage = (int)params.getOrDefault("endPage" ,0);
        int pageNum = (int)params.getOrDefault("pageNum" ,0);
        int pageSize = (int)params.getOrDefault("pageSize" ,0);
        String sql = (String) params.get("sql");
        String storageId = (String) params.get("storageId");
        String userName = (String) params.get("userName");
        Map<String,String> storePropertis = dataAccessMapper.findStorePropertis(storageId);
        String tablename = storePropertis.get("TABLENAME");

//        定义出数据库操作类
        BaseDBOperator operator = null;
        RequestResult requestResult = analysisConn(operator, storePropertis, errorMsg, allMap);
        if(requestResult.getStatusCode() == 0){
            return requestResult;
        }
        operator = (BaseDBOperator)requestResult.getResult();
        Map<String, Integer> pageInfo = new HashMap<>();
        try {
            if(operator != null) {
//            计算分页信息
                int[] arr = operator.pageCount(pageNum, startPage, endPage, pageSize);

//            根据分页查询数据
                data = operator.queryForListMapPage(sql, arr);

//            查询数据总量

                pageInfo.put("currentPage", pageNum);
                pageInfo.put("pageSize", pageSize);
                String condition = "";
                if(sql.contains("where") || sql.contains("WHERE")) {
                    condition = sql.substring(sql.indexOf("where"));
                }
                Map<String, Object> count = operator.getCount(tablename, condition);
                totalCount =  Integer.valueOf(count.get("totalCount") + "");
                pageInfo.put("totalCount", totalCount);

            }
        } catch (BadSqlGrammarException e){
            errorMsg = e.getCause().getMessage();
            e.getSql();

            return new RequestResult(false, errorMsg, allMap);
        }
        allMap.put("data",data);
        allMap.put("pageInfo",pageInfo);
        if(userName != null) {
//            addLog(storageId, tablename, totalCount + "", "", userName);
        }
        return new RequestResult(true, errorMsg, allMap);
    }

    @Override
    public RequestResult getFieldsByTableName(String storageId) {
        BaseDBOperator operator = null;
        Map<String,String> storePropertis = dataAccessMapper.findStorePropertis(storageId);
        String tablename = storePropertis.get("TABLENAME");
        String errorMsg = "";
        Map<String,Object> allMap = new HashMap<>();
        RequestResult requestResult = analysisConn(operator, storePropertis, errorMsg, allMap);
        if(requestResult.getStatusCode() == 0){
            return requestResult;
        }
        operator = (BaseDBOperator)requestResult.getResult();
        try {
            if(operator != null) {
                List<DBTableColumn> data = operator.getAllColumns(tablename);
                allMap.put("data",data);
            }
        } catch (BadSqlGrammarException e){
            errorMsg = e.getCause().getMessage();
            return new RequestResult(false, errorMsg, allMap);
        }
        return new RequestResult(true, errorMsg, allMap);
    }

    public RequestResult analysisConn(BaseDBOperator operator, Map<String,String> storePropertis, String errorMsg, Map<String,Object> allMap){
        List<Map> contentList = JSONObject.parseArray(storePropertis.get("CONTENT").toString(), Map.class);

//        取出用户名和密码
        String username = null;
        String password = null;

        for(Map<String,String> content: contentList) {
            if (content.get("name").equals("Database User")) {
                username = content.get("value");
            }
            if (content.get("name").equals("Password")) {
                password = content.get("value");
            }
        }
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            errorMsg = "数据库用户名和密码不能为空";
//            buildAllMap(allMap, errorMsg, data, totalCount);
            return new RequestResult(false, errorMsg, allMap);
        }

//        数据源类型标志
        int flag = 0;

//        构建数据源操作类
        for(Map<String,String> content: contentList){

            if(content.get("name").equals("Database Connection URL")){
                String connInfo = content.get("value");
                if(StringUtils.isBlank(connInfo)){
                    errorMsg = "数据库连接信息不能为空";
//                    buildAllMap(allMap, errorMsg, data, totalCount);
                    return new RequestResult(false, errorMsg,0, allMap);
                }
                if(connInfo.contains("phoenix")) {
                    String[] split = connInfo.split(":");
                    String ip = split[2];
                    String port = split[3];
                    String db = split[4];
                    flag = 1;
                    operator = buildDB(ip, port, db, flag, username, password);
                }else if(connInfo.contains("oracle")){
                    String ip = "";
                    String port = "";
                    String db = "";
                    if(!connInfo.contains("/")){
                        String[] split = connInfo.split(":");
                        ip = split[3].replace("@","");
                        port = split[4];
                        db =  split[5];
                    }else{
                        String[] split = connInfo.split(":");
                        ip = split[3].replace("@//", "");
                        ip = split[3].replace("@", "");
                        String[] portAndDb = split[4].split("/");
                        port = portAndDb[0];
                        db = portAndDb[1];
                    }
                    flag = 2;
                    operator = buildDB(ip, port, db, flag, username, password);
                }else if(connInfo.contains("mysql")){
                    String[] split = connInfo.split(":");
                    String ip = split[2].replace("//","");
                    String port = split[3].split("/")[0];
                    String db =  split[3].split("/")[1];
                    flag = 3;
                    operator = buildDB(ip, port, db, flag, username, password);
                }

            }
        }
        if(operator == null){
            errorMsg = "不是支持的数据库类型";
            return new RequestResult(false, errorMsg,0, allMap);
        }
        return new RequestResult(true, errorMsg, 1, operator);
    }

    public BaseDBOperator buildDB(String ip, String port, String db, int flag, String username, String password){
        DataSourceProperties sd = new DataSourceProperties();
        sd.setHost(ip);
        sd.setPort(port);
        sd.setUsername(username);
        sd.setPassword(password);
        sd.setDatabaseName(db);

        if(flag == 1){
            sd.setDbType(DBType.PHOENIX);
            PhoenixDriverUrlProperties pdu = new PhoenixDriverUrlProperties(sd);
            sd.setDriverUrlProperties(pdu);
            PhoenixOperator operator = new PhoenixOperator(sd);
            return operator;
        }else if(flag == 2){
            sd.setDbType(DBType.ORACLE);
            if(oracleType.equals("sid")) {
                sd.setInstanceType(OracleInstanceType.SID);
            }else{
                sd.setInstanceType(OracleInstanceType.SERVICE_NAME);
            }
            OracleDriverUrlProperties odu = new OracleDriverUrlProperties(sd);
            sd.setDriverUrlProperties(odu);
            OracleOperator operator = new OracleOperator(sd);
            return operator;

        }else if(flag == 3){
            sd.setDbType(DBType.MYSQL);
            MySqlDriverUrlProperties pdu = new MySqlDriverUrlProperties(sd);
            sd.setDriverUrlProperties(pdu);
            MySqlOperator operator = new MySqlOperator(sd);
            return operator;
        }
        return null;
    }


    /**
     * 日志添加
     * @return
     */
//    public boolean addLog(String storageId, String tableName,String size,String account,String userName){
//
//        DataSharingLog log = new DataSharingLog();
//        //api查询
//        log.setDataUsageType(DataUsageTypeEnum.SQL.getCode());
//        log.setOpDesc(DataUsageTypeEnum.SQL.getName());
//        log.setOpTime(new Date());
//        log.setOpUser(userName);
//        try {
//            log.setOpIp(InetAddress.getLocalHost().getHostAddress());
//        } catch (UnknownHostException e) {
//            log.setOpIp("127.0.0.1");
//        }
//        log.setRlId(apiService.getRlid(tableName));
//        log.setSlId(storageId);
//        log.setVisitDataSize(size);
//        return dataSharingLogService.saveLog(log);
//    }


    public static void main(String[] args) {
        String a = "select * from tt where a = '1' and b = 4";
        String where = a.substring(a.indexOf("where"));
        System.out.println(where);


//        String a = "jdbc:oracle:thin:@//192.168.2.1:1521/XE";
//        String[] split = a.split(":");
//        String ip = split[3].replace("@//", "");
//        String[] ab = split[4].split("/");
//        String s = ab[0];
//        String s1 = ab[1];
//        System.out.println();

    }

}
