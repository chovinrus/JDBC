package _01connectionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 建立连接
 */
public class Conn {
    /**
     * 这是最规范的写法，好处
     * 1，实现了代码和数据的分离，做到了解耦
     * 2，配置到web项目时不用对配置文件打包，在配置文件中修改配置信息可以减少打包
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        //步骤1读取配置文件获取配置
        InputStream resourceAsStream = Conn.class.getClassLoader()
                .getResourceAsStream("_00properties/jdbc.properties");
//        File file = new File("src/_00properties/jdbc.properties");
//        FileInputStream resourceAsStream = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");
        //步骤2注册驱动
        DriverManager.registerDriver(
                (Driver) Class.forName(driver).newInstance()
        );
        //步骤3获取连接
        Connection connection = DriverManager.getConnection(url, properties);
        return connection;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        File file = new File("src/_00properties/z.txt");
        FileInputStream resourceAsStream = new FileInputStream(file);
//        System.out.println(getConnection());
    }
}
