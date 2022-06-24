package _06数据库连接池;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws PropertyVetoException, SQLException {
//        ComboPooledDataSource cpds = new ComboPooledDataSource();
//        cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" ); //loads the jdbc driver
//        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true" );
//        cpds.setUser("root");
//        cpds.setPassword("123456");
//
//        // the settings below are optional -- c3p0 can work with defaults
//        cpds.setInitialPoolSize(10);
//        cpds.setMinPoolSize(5);
//        cpds.setAcquireIncrement(5);
//        cpds.setMaxPoolSize(20);
//
//        Connection connection = cpds.getConnection();
//        System.out.println(connection);

        //销毁数据库连接池
        //DataSources.destroy(cpds);

//        ComboPooledDataSource cpds1 = new ComboPooledDataSource("intergalactoApp");
//        Connection connection1 = cpds1.getConnection();
//        System.out.println(connection1);

//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true");
//        dataSource.setUsername("root");
//        dataSource.setPassword("123456");
//        Connection connection2 = dataSource.getConnection();
//        System.out.println(connection2);

//        InputStream is = Main.class.getClassLoader().getResourceAsStream("_06数据库连接池/dbcp.properties");
//        Properties properties = new Properties();
//        try {
//            properties.load(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        DataSource dataSource1 = null;
//        try {
//            dataSource1 = BasicDataSourceFactory.createDataSource(properties);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        Connection connection3 = dataSource1.getConnection();
//        System.out.println(connection3);

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true");
        System.out.println(druidDataSource.getConnection());

        InputStream is = Main.class.getClassLoader().getResourceAsStream("_06数据库连接池/druid.properties");
        Properties properties4 = new Properties();
        try {
            properties4.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DruidDataSource dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties4);
            Connection connection4 = dataSource.getConnection();
            System.out.println(connection4);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
