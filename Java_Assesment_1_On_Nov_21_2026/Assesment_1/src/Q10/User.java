/*
Q10. JDBC – Prevent Duplicate User Registration (15 Marks)
Create a user-registration application using Java and JDBC.
 The application must accept a user's name, email, and password, then:
16.	Check whether the email address already exists.
17.	Prevent registration when the email is already registered.
18.	Insert the new user when the email is available.
19.	Use PreparedStatement for database operations.
20.	Handle SQL exceptions and validation errors appropriately.
21.	Enforce email uniqueness at the database level as well.
Interview challenge: Two users attempt to register with the same email at nearly the same time. How will you 
prevent duplicate registration even if both requests pass the initial existence check?
 Explain the role of a UNIQUE constraint and exception handling.
*/

package Q10;

public class User {
    private String username;
    private String email;
    private String password;

    public User(String username,String email,String password){
        this.username=username;
        this.email=email;
        this.password=password;
    }

    public String getUsername(){
        return username;
    }
     public String getEmail(){
        return email;
    }
     public String getPassword(){
        return password;
    }

}
