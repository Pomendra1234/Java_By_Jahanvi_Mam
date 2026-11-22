package Q10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





// ---------------------------------------------------------
/*
QUESTION1->Two users attempt to register with the same email at nearly the same time. How will you 
prevent duplicate registration even if both requests pass the initial existence check?
 Explain the role of a UNIQUE constraint and exception handling.

 ANSWER-> We can handle this by using StringBuffer in User class variables which allows only one thread at a time.so if
 two users attempt to register with the same email at nearly the same time then they cannot break the 
 synchronization of my application. 

 An also If they pass initial checks then there is also another Unique constraint applied in a email column
 of the register table of user database.Unique constraint tells us that this field contains unique values
 and it also takes null values .If any duplicate value admin try to put in this field it automatically 
 restricts the entry of the record.
 */

public class Main {

    private static void inputValidation(String patterns,String input) throws ValidationError{
        Pattern p=Pattern.compile(patterns);
        Matcher m=p.matcher(input);
        if(!m.find()){
        throw new ValidationError(String.format("your input is wrong %s .", input));
        }
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the username");
        StringBuffer username=new StringBuffer(sc.nextLine());
        System.out.println("Enter the email");
        StringBuffer email=new StringBuffer(sc.next());
        sc.nextLine();
        System.out.println("Enter the password");
        StringBuffer password=new StringBuffer(sc.next());


        try {
            String EMAILPATTERN="^\\b[a-zA-Z]{4,}[0-9]*[@]{1}(gmail|yahoo|zohomail)\\.(com|in)\\b$";
        inputValidation(EMAILPATTERN,email.toString());
        String USERNAMEPATTERN="^\\b[a-z\\WA-Z0-9]{5,}\\b$";
        inputValidation(USERNAMEPATTERN, username.toString());
        String PASSWORDPATTERN="^\\b[a-zA-Z0-9]{5,}\\b$";
        inputValidation(PASSWORDPATTERN,password.toString());
        System.out.println("Great !");
        } catch (ValidationError e) {
            System.out.println(e.getMessage());
            return;
        }
        User users=new User(username.toString(),email.toString(),password.toString());
        String url="jdbc:mysql://localhost:3306/user";
        String user="root";
        String pass="1234";

        try{
            Connection con=DriverManager.getConnection(url, user, pass);
            try{
            con.setAutoCommit(false);
            PreparedStatement ps=con.prepareStatement("insert into register values(?,?,?);");
            ps.setString(1, users.getUsername());
            ps.setString(2, users.getEmail());
            ps.setString(3, users.getPassword());
            System.out.println(String.format("%d rows is affected", ps.executeUpdate()));
            }catch(SQLException e){
            con.rollback();
            System.out.println("Message1:"+e.getMessage());
            }
            con.commit();
            con.close();
        }catch(SQLException e){
            System.out.println("Message2:"+e.getMessage());
        }
        
    }
}
