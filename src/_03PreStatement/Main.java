package _03PreStatement;

import _00DAO.Customer;
import _00DAO.DAO;
import _01connectionUtil.Conn;

import java.sql.Connection;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws NoSuchMethodException {
        Connection conn = null;
        DAO dao = new DAO();
        try {
            conn = Conn.getConnection();
//            String sql = "insert into customers (name,email,birth,photo) values (?,?,?,?)";
//            File file = new File("\\src\\_03PreStatement\\0.png");
//            FileInputStream fis = new FileInputStream(file);
//            dao.update(conn, sql, new Object[]{"朱建民", "23303258@qq.com",
//                    new Date(new SimpleDateFormat("yy-mm-dd").parse("2022-5-23").getTime()),fis});
//            String sql1 = "update customers set photo = ? where name = ?";
//            dao.update(conn, sql1, new Object[]{fis, "汪峰"});
            String sql2 = "select * from customers where id = ?";
            ArrayList<Customer> query = dao.query(Customer.class,conn, sql2, new Object[]{26});
            System.out.println(query);
            for (Object o :query) {
                System.out.println(o);
            }

        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            Conn.close(conn);
        }
    }
}