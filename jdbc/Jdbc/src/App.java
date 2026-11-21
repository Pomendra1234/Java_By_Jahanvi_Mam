//Date-Nov-19-2026,Nov-18-2026,Nov-17-2026 
import java.sql.*;
public class App {
    public static void main(String[] args) throws Exception {
       String username ="root";
       String password = "1234";
       String url="jdbc:mysql://localhost:3306/college";
       try {
        Connection con=DriverManager.getConnection(url, username, password);
        Statement st=con.createStatement();
        PreparedStatement ps2=con.prepareStatement("update student set name=? where id=1;");
        Statement ps=con.createStatement();
        con.setAutoCommit(false);
        // ps.setString(1, "Cse");
        ps2.setString(1, "Snake");
        ps2.executeUpdate();
        con.commit();
        con.rollback();
        ResultSet rs=ps.executeQuery("Selec * from student where branch='cse';");
        CallableStatement cs=con.prepareCall("{call college_Secrets(?)}");
        cs.setInt(1, 2);
        ResultSet rs1=cs.executeQuery();
        // ResultSet rs=st.executeQuery("select * from student where branch='cse';");
        // ResultSet rs=ps.executeQuery();
        // int as=ps.executeUpdate();
        while(rs1.next()){
            System.out.println("id: "+rs1.getInt("id")+" name: "+rs1.getString("Name")+" Branch:"+rs1.getString("branch")); 
        }
        while(rs.next()){
            System.out.println("id: "+rs.getInt("id")+" name: "+rs.getString("Name")+" Branch:"+rs.getString("branch"));
        }
        con.close();
        
       } catch (SQLException e) {
        System.out.println(e.getErrorCode());
        System.out.println(e.getMessage());
        System.out.println(e.getSQLState());
        
       }
       


    }
}
