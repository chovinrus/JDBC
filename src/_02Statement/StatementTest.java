package _02Statement;
import _01connectionUtil.Conn;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

public class StatementTest {
    // 使用Statement实现对数据表的查询操作
    public static <T> ArrayList<T>  get(String sql, Class<T> clazz) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Conn connection1 = new Conn();
        Connection connection = connection1.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println(columnCount);
        ArrayList<T> ts = new ArrayList<>();
        T t = null;
        while (resultSet.next()){
            t = clazz.newInstance();
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i+1);
                String attr = (String) resultSet.getObject(i+1);
                Field declaredField = clazz.getDeclaredField(columnName);
                declaredField.setAccessible(true);
                declaredField.set(t,attr);
            }
            ts.add(t);
        }
        return ts;
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
//        String userName = "AA";
//        String password = "123456";

        //SQL注入
        String userName = "'or 1 = 1 or'";
        String password = "";
        System.out.println(
                get( "SELECT user,password FROM user_table WHERE USER = '"+userName+"'" +
                " AND PASSWORD = '" + password +"'", User.class)
        );
    }
}
