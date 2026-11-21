/*
Student Management System

Create a Java application using JDBC with the following operations:
1. Add a new student.
2. Display all students.
3. Search student by ID.
4. Update student marks.
5. Delete student by ID.
6. Display students who scored more than 75 marks. */

// import java.beans.Statement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// import com.mysql.cj.xdevapi.Statement;

public class Nov_20_2026 {
public static void main(String[] args) {
    try {
        // DriverManager.forClass("jdbc:mysql://localhost:3306/college");
       Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/college", "root", "1234");
       Statement st=con.createStatement();
       CallableStatement cs=con.prepareCall("{call inserts(?,?,?)}");
       cs.setInt(0, 1);
       cs.setString(1, "Pomendra");
       cs.setInt(2, 70);
       Boolean b=cs.execute();
       System.out.println("Insert "+b);
        
       cs=con.prepareCall("{call displayAll()}");
       ResultSet b1=cs.executeQuery();
       while (b1.next()) {
        System.out.println("display All");
        System.out.println("id : "+b1.getInt(0)+"name: "+b1.getString(1)+" marks: "+b1.getInt(2));
       }
    //    System.out.println("Insert "+b);

       cs=con.prepareCall("{call searchById(?)}");
       cs.setInt(0, 1);
       b1=cs.executeQuery();
       while (b1.next()) {
        System.out.println("search by");
        System.out.println("id : "+b1.getInt(0)+"name: "+b1.getString(1)+" marks: "+b1.getInt(2));
       }
       
        cs=con.prepareCall("{call updates(?,?)}");
       cs.setInt(0, 1);
       cs.setInt(1, 40);
       boolean b3=cs.execute();
       System.out.println("update");
       
       cs=con.prepareCall("{call deletes(?)}");
       cs.setInt(0, 1);
       boolean b4=cs.execute();
       System.out.println("delete "+ b4);

       cs=con.prepareCall("{call moreThan(?)}");
       cs.setInt(0, 75);
        ResultSet r=cs.executeQuery();
        while (r.next()) {
        System.out.println("search by");
        System.out.println("id : "+r.getInt(0)+"name: "+r.getString(1)+" marks: "+r.getInt(2));
       }
       System.out.println("update");
        
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        System.out.println(e.getErrorCode());
        System.out.println(e.getSQLState());
    }
}    
}
