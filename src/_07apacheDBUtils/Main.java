package _07apacheDBUtils;

import _00DAO.Customer;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Handler;

public class Main {

    static Connection conn;

    static {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("_07apacheDBUtils/druid.properties");
        Properties properties4 = new Properties();
        try {
            properties4.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DruidDataSource dataSource = null;
        try {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties4);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            conn = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) {
        QueryRunner queryRunner = new QueryRunner();


        int res;
        try {
            System.out.println(
                    res = queryRunner.update(
                        conn,
                        "insert into user_table values (?,?,?)",
                        "EE", "123456", 9000
                    )
            );// 1
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        Customer customer = null;
        List<Customer> customers = null;
        Map<String, Object> map = null;
        List<Map<String, Object>> mapList = null;
        Date birth = null;
        //ResultSetHandler可以自己去实现，就是根据结果集进行抽取封装得到返回对象
        //返回一个或一组对象
        BeanHandler<Customer> customerBeanHandler = new BeanHandler<>(Customer.class);
        BeanListHandler<Customer> customerBeanListHandler = new BeanListHandler<Customer>(Customer.class);
        //DQL语句执行结果没有封装对象，直接以Map或其数组的形式返回
        MapHandler mapHandler = new MapHandler();
        MapListHandler mapListHandler = new MapListHandler();
        //单行函数或DQL返回的结果只有一个字段,或者是个函数结果
        //implementation that converts one ResultSet column into an Object
        ScalarHandler scalarHandler = new ScalarHandler("birth");
        try {
            customer = queryRunner.query(conn, "select * from customers where id = ?", customerBeanHandler, 1);
            customers = queryRunner.query(conn, "select * from customers where id > ?", customerBeanListHandler, 1);
            map = queryRunner.query(conn, "select * from customers where id = ?", mapHandler, 1);
            mapList = queryRunner.query(conn, "select * from customers where id > ?", mapListHandler, 1);
            birth = (Date) queryRunner.query(conn, "select * from customers where id = ?", scalarHandler, 1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(customer);
        customers.forEach(System.out::println);
        System.out.println(map);
        mapList.forEach(System.out::println);
        System.out.println(birth);

        //这个巨强，参数可以是连接，可以是StateMent，也可以是结果集
        //用来省略try-catch和非空判断的
        DbUtils.closeQuietly(conn);
    }
}
