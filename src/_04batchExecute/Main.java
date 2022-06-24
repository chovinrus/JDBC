package _04batchExecute;

import _00DAO.DAO;
import _01connectionUtil.Conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {

    public static void noBatchInsert(){
        Connection conn = null;
        DAO dao = new DAO();
        PreparedStatement pst = null;
        try {
            conn = Conn.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into testbatch (value) values (?)";
            pst = conn.prepareStatement(sql);
            for (int i = 0; i < 10000; i++) {
                pst.setObject(1, i);
                pst.execute();
            }
            conn.commit();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            if (pst != null){
                try {
                    pst.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Conn.close(conn);
        }
    }

    public static void batchInsert(){
        Connection conn = null;
        DAO dao = new DAO();
        PreparedStatement pst = null;
        try {
            conn = Conn.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into testbatch (value) values (?)";
            pst = conn.prepareStatement(sql);
            for (int i = 0; i < 10000; i++) {
                pst.setObject(1, i);
                pst.addBatch();
                if (i%100 == 0 || i == 9999){
                    pst.executeBatch();
                    pst.clearBatch();
                }
            }
            conn.commit();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            if (pst != null){
                try {
                    pst.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Conn.close(conn);
        }
    }

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        noBatchInsert();
        long t2 = System.currentTimeMillis();
        long t3 = System.currentTimeMillis();
        batchInsert();
        long t4 = System.currentTimeMillis();
        System.out.println((t2 - t1));//54521 6312
        System.out.println((t4 - t3));//1125 313
    }
}
