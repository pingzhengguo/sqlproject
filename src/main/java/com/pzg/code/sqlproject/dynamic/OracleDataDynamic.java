package com.pzg.code.sqlproject.dynamic;

import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

/**
 * 描述：动态创建oracle数据连接及各操作
 *
 */
public class OracleDataDynamic extends DataDynamic
{
    private  String url = "jdbc:oracle:thin:@192.168.1.170:1521:ICATI";
    private  String user = "cs_haiyun";
    private  String password = "ca_haiyun";

    public OracleDataDynamic(String url, String user, String password){
        this.url=url;
        this.user = user;
        this.password = password;
    }
    // 连接数据库的方法
    @Override
    public void getConnection()  throws Exception
    {
            // 初始化驱动包
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // 根据数据库连接字符，名称，密码给conn赋值
            conn = DriverManager.getConnection(url, user, password);
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
        // 查询数据的sql语句
//        String sql = "select * from " + tableName ;
        int startPage = (pageNum-1) * pageSize;
        int maxPage = startPage + pageSize;
        String sql = "select * from (select t.*, rownum num from " + tableName + " t where rownum<=" + maxPage + ") where num>"+startPage;
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

        String sql = "select t.TABLE_NAME  tablename,t.COLUMN_NAME columnname, t.DATA_TYPE datatype,t.DATA_LENGTH datalength,t1.COMMENTS FROM user_tab_cols t,user_col_comments t1 where t.table_name='" + tableName + "' and t.COLUMN_NAME=t1.COLUMN_NAME and t.table_name=t1.TABLE_NAME";
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
          String url = "jdbc:oracle:thin:@192.168.1.170:1521:ICATI";
          String user = "cs_haiyun";
          String password = "ca_haiyun";
        OracleDataDynamic basedao = new OracleDataDynamic(url, user, password);
        basedao.getConnection();
        if (conn == null)
        {
            System.out.println("与oracle数据库连接失败！");
        }
        else
        {
            System.out.println("与oracle数据库连接成功！");
        }
        List list = basedao.selectTableInfo("STORE_LIST");
        System.out.println(list.size());
    }
}
