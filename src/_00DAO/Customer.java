package _00DAO;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

//public class Customer {
//    private int ID;
//    private String name;
//    private String email;
//    private Date birth;
//    private byte[] photo;
//
//    public Customer(Object... beanArgs) {
//        this.ID = (int) beanArgs[0];
//        this.name = (String) beanArgs[1];
//        this.email = (String) beanArgs[2];
//        this.birth = (Date) beanArgs[3];
//        this.photo = (byte[]) beanArgs[4];
//    }
//
//    @Override
//    public String toString() {
//        FileOutputStream fos = null;
//        ByteArrayInputStream is = null;
//        try {
//            System.out.println(this.photo.length);
//            is = new ByteArrayInputStream(this.photo);
//            fos = new FileOutputStream("query.png");
//            byte[] buf = new byte[1024];
//            int len = 0;
//            while ((len = is.read(buf)) != -1) {
//                fos.write(buf, 0, len);
//            }
//        } catch (FileNotFoundException throwables) {
//            throwables.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//                if (is != null) {
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return "Customer{" +
//                "ID=" + ID +
//                ", name='" + name + '\'' +
//                ", email='" + email + '\'' +
//                ", birth='" + birth + '\'' +
//                '}';
//    }
//}


public class Customer {

    private int id;
    private String name;
    private String email;
    private Date birth;
    public Customer() {
        super();
    }
    public Customer(int id, String name, String email, Date birth) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getBirth() {
        return birth;
    }
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", birth=" + birth + "]";
    }
}
