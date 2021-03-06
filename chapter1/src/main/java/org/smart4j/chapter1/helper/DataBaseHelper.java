package org.smart4j.chapter1.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.util.CollectionUtil;
import org.smart4j.chapter1.util.PropsUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DataBaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseHelper.class);
//    private static final String DRIVER;
//    private static final String URL;
//    private static final String USERNAME;
//    private static final String PASSWORD;

//    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
//    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;

    //使用dbcp实现数据库链接池
    static {
        CONNECTION_HOLDER =new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        Properties conf = null;
        try {
            conf = PropsUtil.loadProps("config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String password = conf.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);

    }


    //静态代码块，封装数据库的相关操作
//    static {
//        Properties conf = null;
//        try {
//            conf = PropsUtil.loadProps("config.properties");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        DRIVER =conf.getProperty("jdbc.driver");
//        URL =conf.getProperty("jdbc.url");
//        USERNAME =conf.getProperty("jdbc.username");
//        PASSWORD =conf.getProperty("jdbc.password");
//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            LOGGER.error("can not load driver",e);
//        }
//    }

    /*
    获取数据库链接。为了确保一个线程只有一个connection,我们使用ThreadLocal来存放本地线程变量，也就是说，
    将当前线程中的connection放入ThreadLocal存放起来，这些connection一定不会出现线程安全问题，可以将
    ThreadLocal理解为一个隔离线程的容器
     */
    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        try {
            conn= DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            LOGGER.error("get connection fail",e);
            throw new RuntimeException(e);
        }finally {
            CONNECTION_HOLDER.set(conn);
        }
        return conn;
    }

    //关闭数据库链接
//    public static void closeConnection(){
//        Connection connection = CONNECTION_HOLDER.get();
//        if (connection!=null){
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                LOGGER.error("close connection fail",e);
//                throw new RuntimeException(e);
//            }finally {
//                CONNECTION_HOLDER.remove();
//            }
//        }
//    }

    //查询实体列表
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList;

        try {
            Connection connection = getConnection();
            entityList = QUERY_RUNNER.query(connection,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    //查询实体
    public static <T> T queryEntity(Class<T> entityClass,String sql,Object... params){
        T entity;
        try {
            Connection connection = getConnection();
            entity = QUERY_RUNNER.query(connection,sql,new BeanHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity failure",e);
            throw new RuntimeException(e);
        }

        return entity;
    }

    //执行查询语句
    public static List<Map<String,Object>> executeQuery(String sql, Object... params){
        List<Map<String,Object>> result;

        try {
            Connection connection = getConnection();
            result = QUERY_RUNNER.query(connection,sql,new MapListHandler(),params);
        }catch (SQLException e){
            LOGGER.error("execute query failure",e);
            throw new RuntimeException();
        }
        return result;
    }

    //执行更新语句（insert,update,delete）
    public static int executeUpdate(String sql,Object... params) {

        int rows = 0;
        try {
            Connection connection  = getConnection();
            rows = QUERY_RUNNER.update(connection,sql,params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure",e);
            throw new RuntimeException();
        }
        return rows;
    }

    //插入实体
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){

        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }
        String sql = "insert into "+getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append(",");
            values.append("?,");
        }
        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(values.lastIndexOf(","),values.length(),")");
        sql += columns+"VALUES"+values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql,params) == 1;
    }


    //更新实体
    public static <T> boolean updateEntity(Class<T> entityClass,Integer id,Map<String,Object> fieldMap){

        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }
        String sql = "UPDATE "+getTableName(entityClass)+" SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append("=?,");
        }
        sql += columns.substring(0,columns.lastIndexOf(","))+" WHERE id =?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return executeUpdate(sql,params) == 1;

    }

    //删除实体
    public static <T> boolean deleteEmpty(Class<T> entityClass,Integer id){

        String sql = "delete from "+getTableName(entityClass)+" where id = ?";
        return executeUpdate(sql,id) == 1;
    }

    public static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

    //执行sql文件
    public static void executeSqlFile(String filePath){

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String sql ;
            while ((sql=bufferedReader.readLine())!=null){
                executeUpdate(sql);
            }
        }catch (Exception e){
            LOGGER.error("execute sql file failure",e);
            throw new RuntimeException();
        }

    }

}
