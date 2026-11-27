package Q10;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Main {

    private static void inputValidation(String fieldName, String patterns, String input) throws ValidationError {
        if (input == null || !Pattern.matches(patterns, input)) {
            throw new ValidationError("Invalid " + fieldName + ". Please check the format.");
        }
    }

    // SHA-256
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the username");
        String username = sc.nextLine().trim();
        System.out.println("Enter the email");
        String email = sc.nextLine().trim().toLowerCase();
        System.out.println("Enter the password");
        String password = sc.nextLine().trim();
        sc.close();

        try {
            String EMAILPATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            String USERNAMEPATTERN = "^[A-Za-z0-9 _.-]{5,30}$";
            String PASSWORDPATTERN = "^\\S{6,}$";

            inputValidation("email", EMAILPATTERN, email);
            inputValidation("username", USERNAMEPATTERN, username);
            inputValidation("password", PASSWORDPATTERN, password);
            System.out.println("Great !");
        } catch (ValidationError e) {
            System.out.println(e.getMessage());
            return;
        }

        String hashedPassword;
        try {
            hashedPassword = hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Password hashing failed: " + e.getMessage());
            return;
        }

        User users = new User(username, email, hashedPassword);

        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String pass = "1234";

        
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement check = con.prepareStatement("select 1 from register where email = ?")) {
                    check.setString(1, users.getEmail());
                    try (ResultSet rs = check.executeQuery()) {
                        if (rs.next()) {
                            System.out.println("Email already registered. Registration not allowed.");
                            con.rollback();
                            return;
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO register (username, email, password) VALUES (?, ?, ?)")) {
                    ps.setString(1, users.getUsername());
                    ps.setString(2, users.getEmail());
                    ps.setString(3, users.getPassword());
                    int rows = ps.executeUpdate();
                    con.commit(); 
                    System.out.println(String.format("%d row affected. Registration successful.", rows));
                }

            } catch (SQLException e) {
                con.rollback();
                if (e.getErrorCode() == 1062) {
                    System.out.println("Email already registered (caught by UNIQUE constraint).");
                } else {
                    System.out.println("Database error: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}

/*
 * Galtiyan (kahan aur kya)
 * 
 * 1. Requirement 1 aur 2 poora missing hai. Email exist karta hai ya nahi, iska
 * koi SELECT check hai hi nahi. Seedha insert kar diya. Question mein
 * "pehle check karo, exist kare to registration roko" likha hai.
 * 
 * 2. Requirement 6 (UNIQUE constraint) sirf comment mein hai. Table ka SQL code
 * mein kahin nahi hai. Neeche table ka SQL de diya hai, wo MySQL mein chalana
 * padega.
 * 
 * 3. Interview answer galat hai (sabse badi galti).
 * 
 * StringBuffer se kuch thread-safe nahi hota. username, email, password har
 * request ke local variables hain. Do users alag requests, alag threads, ya
 * alag JVMs se aate hain, unke beech ye share hi nahi hota.
 * Synchronization se ye problem solve nahi hoti, kyunki application kai servers
 * par chal sakti hai. Sirf database hi aisa common point hai jo duplicate rok
 * sakta hai.
 * "UNIQUE null values leta hai" sahi hai, par email column NOT NULL bhi hona
 * chahiye.
 * Sahi jawab: dono requests existence check pass kar lein, to bhi pehla INSERT
 * succeed hoga aur dusre ko DB UNIQUE constraint violation (MySQL error code
 * 1062) dega. Hum us exception ko catch karke user ko
 * "email already registered" bolte hain.
 * 
 * 3b. Exception handling mein duplicate alag se handle nahi hota.
 * SQLIntegrityConstraintViolationException (error 1062) ko alag pakadna
 * chahiye, warna user ko raw SQL message dikhta hai.
 * 
 * 4. Transaction galat hai. rollback() ke baad bhi con.commit() chal jata hai
 * (wo catch ke bahar hai). Commit sirf success par hona chahiye.
 * 
 * 5. Resources close nahi ho rahe. PreparedStatement kabhi close nahi hota, aur
 * Connection exception aane par close nahi hota. try-with-resources use kar.
 * 
 * 6. Password plain text mein store ho raha hai. Ye serious security issue hai.
 * Maine SHA-256 hash lagaya hai. Real project mein BCrypt use hota hai, par
 * uske liye external library chahiye.
 * 
 * 7. Validation error mein password print ho jata hai
 * ("your input is wrong %s"). Password kabhi print nahi karna. Ab error mein
 * sirf field ka naam aata hai.
 * 
 * 8. Regex ki galtiyan:
 * 
 * Email regex john.doe@gmail.com ya ab@gmail.com ko reject karta hai (4+
 * letters ke baad sirf digits allow, dot nahi) aur sirf gmail/yahoo/zohomail
 * allow karta hai.
 * Username regex mein \W kuch bhi special character allow kar deta hai, aur \b
 * ki wajah se kuch valid input reject ho jate hain.
 * find() ki jagah matches() use karna chahiye, aur {1}, \b bekar hain.
 * 
 * 9. Input lene ka tareeka kamzor hai. sc.next() space par ruk jata hai, aur
 * next() ke baad nextLine() ka jugaad kar rakha hai. Teeno ke liye
 * nextLine().trim() use kar. StringBuffer ki zaroorat hi nahi, String kaafi
 * hai.
 * 
 * 10. insert into register values(?,?,?) mein column names nahi hain. Table ka
 * structure badla to ye toot jayega. Column names likhna chahiye.
 * 
 * 11. Email case-insensitive hona chahiye. A@gmail.com aur a@gmail.com same
 * hone chahiye, isliye toLowerCase() kar diya.
 */