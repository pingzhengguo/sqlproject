package com.pzg.code.sqlproject.dynamic;


import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 描述：动态创建oracle数据连接及各操作
 *
 */
public class PhoenixDataDynamic extends DataDynamic
{
    private  String url = "jdbc:oracle:thin:@192.168.1.170:1521:ICATI";
    private  String user = "cs_hai";
    private  String password = "ca_hai";

    public PhoenixDataDynamic(String url, String user, String password){
        this.url=url;
        this.user = user;
        this.password = password;
    }
    // 连接数据库的方法
    @Override
    public void getConnection() throws Exception
    {
            // 初始化驱动包
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            // 根据数据库连接字符，名称，密码给conn赋值
//            conn = DriverManager.getConnection(url, user, password);
        Properties props = new Properties();
        props.setProperty("phoenix.query.timeoutMs", "12000");
        props.setProperty("hbase.rpc.timeout", "12000");
        props.setProperty("hbase.client.scanner.timeout.period", "12000");
        props.setProperty("hbase.client.retries.number","3");
        props.put("user", user);
        props.put("password", password);
        conn = DriverManager.getConnection(url,props);
    }

    /**
     * 查询表数据
     * @param tableName
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public List<Map> selectTableData(String tableName,Integer pageNum,Integer pageSize) throws  Exception{
        int startPage = (pageNum-1) * pageSize;
        int maxPage = startPage + pageSize;
        // 查询数据的sql语句
//        String sql = "select * from " + tableName;
        String sql = "select * from " + tableName + " limit " + maxPage + " offset " + startPage;
//        String sql = "select * from " + tableName + " limit " + maxPage;
        System.out.println(sql);
        return  executeSql(sql);
    }
    /**
     * 查询表基本信息
     * @param tableName
     * @return
     */
    @Override
    public List<Map> selectTableInfo(String tableName) throws  Exception{

        String sql = "select COLUMN_NAME columnname,SqlTypeName(DATA_TYPE)  as datatype,REMARKS as comments from SYSTEM.CATALOG where table_name = '"+ tableName +"' and COLUMN_NAME is not null " ;
        System.out.println(sql);
        return  executeSql(sql);
    }

    /**
     * 查询表数据量
     * @param tableName
     * @return
     * @throws Exception
     */
    @Override
    public List<Map> selectTableDataCount(String tableName) throws  Exception{

        String sql = "select count(*) count from " + tableName;
        System.out.println(sql);
        return  executeSql(sql);
    }

    // 测试能否与oracle数据库连接成功
    public static void main(String[] args) throws Exception
    {
          String url = "jdbc:phoenix:192.168.1.74:2181:/hbase-unsecure";
          String user = "";
          String password = "";
        PhoenixDataDynamic basedao = new PhoenixDataDynamic(url, user, password);
        basedao.getConnection();
        if (conn == null)
        {
            System.out.println("与phoenix数据库连接失败！");
        }
        else
        {
            System.out.println("与phoenix数据库连接成功！");
        }
        List list = basedao.selectTableData("T_EXCEL",2,3);
        System.out.println(list.size());
    }
}
