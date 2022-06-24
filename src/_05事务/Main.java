package _05事务;

import _00DAO.DAO;
import _00DAO.DAOwithTransaction;
import _01connectionUtil.Conn;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

public class Main {

    static Connection conn;

    public static void getConnection() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        conn = Conn.getConnection();
        conn.setAutoCommit(false);
        //修改完就保持这个隔离级别，若只是用几次，用完记得把隔离级别改回来
//        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

    public static void closeConnection() throws SQLException {
        //这里将连接的自动提交改为true的原因是适应数据库连接池使用的规范，避免别的代码拿到连接的自动提交不是默认
        conn.setAutoCommit(true);
        conn.close();
    }

    public static void balanceTest(int money, String sender, String receiver) {
        try {
            DAO dao = new DAO();
            PreparedStatement pst = null;
            String sql0 = "update user_table set balance = balance + ? where user = ?";
            dao.update(conn, sql0, new Object[]{-money, sender});
            int a = 3 / 0;//模拟执行过程中的其他异常或错误
            String sql1 = "update user_table set balance = balance + ? where user = ?";
            dao.update(conn, sql1, new Object[]{100, receiver});
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void balanceTestWithTransaction0(int money, String sender, String receiver) {
        Savepoint transection0 = null;
        try {
            transection0 = conn.setSavepoint("transection0");
            DAOwithTransaction<Object> dao = new DAOwithTransaction<>();
            DAOwithTransaction<Integer> dao1 = new DAOwithTransaction<>();
            PreparedStatement pst = null;
            String sql0 = "update user_table set balance = balance + ? where user = ?";
            dao.update(conn, sql0, new Object[]{-money, sender});

//            new Thread() {
//                public void run() {
//                    balanceTestWithTransaction1("AA", "BB");
//                }
//            }.start();
//            //转账后AA的账户余额为900元，BB的账户余额为1000元
//            //java.lang.ArithmeticException: / by zero
//            //	at _05事务.Main.balanceTestWithTransaction0(Main.java:59)
//            //	at _05事务.Main.main(Main.java:117)

            //为脏读创造时间
            Thread.sleep(10000);
            int a = 3 / 0;//模拟执行过程中的其他异常或错误
            String sql1 = "update user_table set balance = balance + ? where user = ?";
            dao.update(conn, sql1, new Object[]{100, receiver});
            conn.commit();
            String sql2 = "select balance from user_table where user = ?";
            ArrayList<Integer> senderBalance = dao1.query(Integer.class, conn, sql2, new Object[]{sender});
            ArrayList<Integer> recerverBalance = dao1.query(Integer.class, conn, sql2, new Object[]{receiver});
            String display0 = String.format("转账成功！\n%s向%s转账%d元，", sender, receiver, money);
            String diaplay1 = String.format("转账后%s的账户余额为%d元，%s的账户余额为%d元", sender, senderBalance.get(0), receiver, recerverBalance.get(0));
            System.out.println(display0 + diaplay1);
            //转账成功！
            //AA向BB转账100元，转账后AA的账户余额为900元，BB的账户余额为1100元
        } catch (Exception exception) {
            // 不要忘记在处理异常中回滚，否则在断开数据库连接的时候仍然会提交事务
            try {
                conn.rollback(transection0);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            exception.printStackTrace();
        }
    }

    @Test
    public void balanceTestWithTransaction1() {
        try {
            getConnection();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        Savepoint transection1 = null;
        String sender = "AA";
        String receiver = "BB";
        try {
            transection1 = conn.setSavepoint("transection1");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //保证执行时事务1已经转账转出成功，但没能收账成功
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DAOwithTransaction<Integer> dao1 = new DAOwithTransaction<>();
        String sql2 = "select balance from user_table where user = ?";
        ArrayList<Integer> senderBalance = dao1.query(Integer.class, conn, sql2, new Object[]{sender});
        ArrayList<Integer> recerverBalance = dao1.query(Integer.class, conn, sql2, new Object[]{receiver});
        String diaplay1 = String.format("转账后%s的账户余额为%d元，%s的账户余额为%d元", sender, senderBalance.get(0), receiver, recerverBalance.get(0));
        System.out.println(diaplay1);
        try {
            conn.rollback(transection1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //  这里将数据库连接的逻辑分离开来
        try {
            getConnection();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

//        balanceTest(100, "AA", "BB");
        balanceTestWithTransaction0(100, "AA", "BB");


        // 这里拿出来放到外面就是防止SQL逻辑有部分没执行完就被关闭，
        // 一旦关闭就自动提交事务了
        try {
            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}