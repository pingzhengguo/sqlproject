package com.pzg.code.sqlproject.dynamic;

import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

/**
 * 描述：动态创建mysql数据连接及各操作
 *
 */
public class MysqlDataDynamic extends DataDynamic
{
    private  String url = "jdbc:mysql://10.0.92.88:3306/person";
    private  String user = "root";
    private  String password = "root";

    public MysqlDataDynamic(String url, String user, String password){
        this.url=url;
        this.user = user;
        this.password = password;
    }
    // 连接数据库的方法
    @Override
    public void getConnection()  throws Exception
    {
            // 初始化驱动包
            Class.forName("com.mysql.jdbc.Driver");
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
        String sql = "select * from " + tableName + " limit " + startPage + "," + pageSize;
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

        String sql = "select table_name as tablename,column_name as columnname,column_type as datatype,column_comment as comments from information_schema.columns where table_name = '" + tableName + "'";
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
          String url = "jdbc:mysql://localhost:3306/test";
          String user = "root";
          String password = "root";
        MysqlDataDynamic basedao = new MysqlDataDynamic(url, user, password);
        basedao.getConnection();
        if (conn == null)
        {
            System.out.println("与mysql数据库连接失败！");
        }
        else
        {
            System.out.println("与mysql数据库连接成功！");
        }
        List list = basedao.selectTableInfo("t_user");
        System.out.println(list.size());
    }
}
