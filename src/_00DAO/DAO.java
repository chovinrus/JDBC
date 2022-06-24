package _00DAO;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.ArrayList;

public class DAO<T> {
    public void update(Connection conn, String sql, Object... args) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                pst.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    pst.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public ArrayList<T> query(Class<T> clazz, Connection conn, String sql, Object... args) {
        ArrayList<T> ts = null;
        PreparedStatement pst = null;
        try {
            //预编译
            pst = conn.prepareStatement(sql);
            //对占位符赋值
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
            //获取查询语句的结果
            ResultSet res = pst.executeQuery();

            //准备返回值
            ts = new ArrayList<>();
            // 这里的next表示结果集指针下移后是否还有
            while (res.next()) {
                ResultSetMetaData metaData = res.getMetaData();
                int columnCount = metaData.getColumnCount();
                Object[] beanArgs = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    Object object = res.getObject(i + 1);
                    beanArgs[i] = object;
                }
                //当我们不是用变长参数数组来创建Bean的时候用反射去按照反射去获得属性时，
                //这时候我们要注意Bean类的属性与db表中列名的一致性
                //需要为列名其别名，且pst的方法应该用getColumnLabel
                T t = null;
                try {
                    Constructor constructor = clazz.getDeclaredConstructor(Object[].class);
                    try {
                        t = (T) constructor.newInstance((Object) beanArgs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                ts.add(t);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return ts;
    }
}