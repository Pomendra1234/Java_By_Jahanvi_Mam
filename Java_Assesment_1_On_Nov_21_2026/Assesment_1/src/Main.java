
/*
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    private static void account(Connection con, int id, String name, int balance) throws SQLException {
        CallableStatement cs = con.prepareCall("{call account(?,?,?)}");
        cs.setInt(1, id);
        cs.setString(2, name);
        cs.setInt(3, balance);
        System.out.println(cs.execute());

    }

    private static int retrieveAccountMoney(Connection con, int id) throws SQLException {
        CallableStatement cs = con.prepareCall("{call retrieveAccount(?)}");
        cs.setInt(1, id);
        ResultSet rs = cs.executeQuery();
        int ib = 0;
        while (rs.next()) {
            ib = rs.getInt(3);
            System.out.println(ib);
        }
        return ib;
    }

    private static void balanceUpdate(CallableStatement cs, Connection con, int id, int change) throws SQLException {
        cs = con.prepareCall("{call balance_Update(?,?)}");
        cs.setInt(1, id);
        cs.setInt(2, change);
        System.out.println(cs.executeQuery());
    }

    private static void transfer(Connection con, int i) throws SQLException {
        int ib = retrieveAccountMoney(con, 1);
        if (ib < i) {
            con.rollback();
            throw new Insufficient_Balance(String.format("your account balance is less than %d.", i - ib));

        } else {
            CallableStatement cs = null;
            balanceUpdate(cs, con, 1, ib - i);
            int money = retrieveAccountMoney(con, 2);
            balanceUpdate(cs, con, 2, money + i);
            cs = con.prepareCall("{call transactionsA(?,?,?,?)}");
            cs.setInt(1, ib);
            cs.setInt(2, ib - i);
            cs.setInt(3, 0);
            cs.setInt(4, i);
            System.out.println(cs.execute());
            cs = con.prepareCall("{call transactionsB(?,?,?,?)}");
            cs.setInt(1, money);
            cs.setInt(2, money + i);
            cs.setInt(3, i);
            cs.setInt(4, 0);
            System.out.println(cs.execute());
        }

    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/college";
        String username = "root";
        String password = "1234";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(false);
            try {
                account(con, 1, "A", 50000);
                account(con, 2, "B", 20000);
                transfer(con, 10000);
            } catch (SQLException e) {
                con.rollback();
                System.out.println(e.getMessage());
            }
            con.commit();
            con.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

 */
// What happens if the application crashes after deducting
//  * money from Account A but before crediting Account B? Explain how database
//  * transactions protect consistency.

// ANSWER --->
// Nothing bad happens, because the debit and credit run inside ONE transaction and commit() is
// never reached. When the application crashes, its database connection drops, and the database
// (InnoDB) automatically rolls back every uncommitted change. So A's deduction is undone and the
// money is not lost. (The application cannot roll back by itself after a crash; the DATABASE does it.)
// Transactions follow ACID:
//  - Atomicity: debit, credit and history insert happen all together or not at all.
//  - Consistency: the total money (A + B = 70000) stays the same before and after the transfer.
//  - Isolation: other users never see the half-done state (A debited, B not yet credited).
//  - Durability: once commit() succeeds, the change survives even a server crash.

// ------------------------------------------------------------------------------------------------

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    private static void account(Connection con, int id, String name, int balance) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call account(?,?,?)}")) {
            cs.setInt(1, id);
            cs.setString(2, name);
            cs.setInt(3, balance);
            cs.execute();
            System.out.println("Account " + name + " ready with balance " + balance);
        }
    }

    private static int retrieveAccountMoney(Connection con, int id) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call retrieveAccount(?)}")) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(3);
                }
            }
        }
        throw new SQLException("Account " + id + " not found");
    }

    private static void balanceUpdate(Connection con, int id, int change) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call balance_Update(?,?)}")) {
            cs.setInt(1, id);
            cs.setInt(2, change);
            cs.execute();
        }
    }

    private static void transfer(Connection con, int i) throws SQLException {
        if (i <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than 0.");
        }

        int ib = retrieveAccountMoney(con, 1);
        if (ib < i) {
            throw new Insufficient_Balance(String.format("Insufficient balance. You are short by %d.", i - ib));
        }

        balanceUpdate(con, 1, ib - i); // debit A
        int money = retrieveAccountMoney(con, 2);
        balanceUpdate(con, 2, money + i); // credit B

        try (CallableStatement cs = con.prepareCall("{call transactionsA(?,?,?,?)}")) {
            cs.setInt(1, ib);
            cs.setInt(2, ib - i);
            cs.setInt(3, 0);
            cs.setInt(4, i);
            cs.execute();
        }
        try (CallableStatement cs = con.prepareCall("{call transactionsB(?,?,?,?)}")) {
            cs.setInt(1, money);
            cs.setInt(2, money + i);
            cs.setInt(3, i);
            cs.setInt(4, 0);
            cs.execute();
        }
        System.out.println("A: " + ib + " -> " + (ib - i) + " | B: " + money + " -> " + (money + i));
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/college";
        String username = "root";
        String password = "1234";

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            boolean oldAutoCommit = con.getAutoCommit(); // purani setting yaad rakho
            con.setAutoCommit(false);
            try {
                account(con, 1, "A", 50000);
                account(con, 2, "B", 20000);
                transfer(con, 10000);

                con.commit(); // commit sirf tab, jab sab kuch succeed ho
                System.out.println("Transfer successful. Transaction committed.");
            } catch (SQLException | RuntimeException e) {
                con.rollback(); // koi bhi step fail -> sab undo
                System.out.println("Transfer failed. Transaction rolled back. Reason: " + e.getMessage());
            } finally {
                con.setAutoCommit(oldAutoCommit); // auto-commit restore
            }
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

}

/*
 * Galtiyan (kahan aur kya)
 * 
 * 1. balanceUpdate mein executeQuery() galat hai. Update procedure koi result
 * set nahi deta, to executeQuery() exception phenk dega. Wahan execute()
 * chahiye. Saath mein System.out.println(cs.executeQuery()) object ka address
 * print karta hai, kaam ka nahi.
 * 
 * 2. Insufficient balance wala flow toot jata hai. Insufficient_Balance ek
 * RuntimeException hai, aur main mein sirf SQLException catch hota hai. To
 * exception uncaught nikal jaata hai, con.close() kabhi chalta hi nahi, aur
 * program stack trace ke saath marta hai.
 * 
 * 3. con.commit() catch ke bahar hai. Rollback ke baad bhi commit chal jata
 * hai. Requirement hai ki commit sirf tab ho jab saare operations succeed hon,
 * to commit() try ke andar, transfer ke baad aana chahiye.
 * 
 * 4. Auto-commit restore nahi kiya. Requirement mein likha hai
 * "Restore the connection's auto-commit setting". Ab finally mein restore hota
 * hai.
 * 
 * 5. Resources leak hote hain. CallableStatement aur ResultSet kahin close nahi
 * ho rahe, aur exception aane par Connection bhi close nahi hota. Ab
 * try-with-resources use kiya hai.
 * 
 * 6. balanceUpdate(CallableStatement cs, ...) ka cs parameter bekar hai. Andar
 * turant overwrite ho jata hai, aur transfer mein ek hi cs variable ko
 * baar-baar naya statement dene se purane statements close nahi hote. Parameter
 * hata diya.
 * 
 * 7. retrieveAccountMoney account na milne par chupchap 0 return karta hai.
 * Isse "insufficient balance" ka galat message aata hai. Ab account na mile to
 * exception aata hai.
 * 
 * 8. Error message galat hai. "balance is less than %d", i - ib kehta hai
 * balance us amount se kam hai, jabki wo asal mein kami (shortfall) hai.
 * Message theek kar diya.
 * 
 * 9. Race condition ka dhyan. Balance padhna aur update karna ke beech koi
 * dusra transfer aa sakta hai. Isliye retrieveAccount procedure mein SELECT ...
 * FOR UPDATE lagaya hai, jo commit tak row lock rakhta hai.
 * 
 * 10. Interview answer thoda galat hai. Tune likha
 * "my application rollback the transaction". Lekin agar application crash ho
 * gayi, to wo rollback kar hi nahi sakti. Rollback database karta hai. Answer
 * neeche theek kar diya hai.
 * 
 * 11. Amount validation missing hai. amount <= 0 pe transfer nahi hona chahiye.
 * Ab check hai.
 * 
 * Jo maine nahi chheda: Account.java (wo kahin use nahi hoti, par tune kaha tha
 * jo sahi hai usse mat chhed) aur Insufficient_Balance.java.
 */

/*
 * Crash / rollback khud test kar
 * 
 * transfer mein balanceUpdate(con, 2, money + i); se pehle ye line daal:
 * 
 * java
 * if (true) throw new SQLException("Simulated crash after debiting A");
 * 
 * Run kar aur MySQL mein SELECT * FROM account; chala. A ka balance 40000 nahi
 * hoga, 50000 hi rahega, aur transaction_history mein koi row nahi hogi. Ye
 * dikhata hai ki A ka debit bhi undo ho gaya. Test ke baad wo line hata dena.
 * 
 * Interview mein short mein bolna
 * 
 * "Debit, credit aur history insert ek hi transaction mein hain. commit() sirf tab hota hai jab teeno succeed hon. Agar A se paise kat chuke hain aur B ko credit karne se pehle application crash ho jaye, to commit kabhi nahi hua. Connection toot-ta hai aur database uncommitted changes ko automatically rollback kar deta hai, to A ke paise wapas aa jaate hain. Isse atomicity milti hai, aur consistency bhi: A + B ka total 70000 hi rehta hai. Isolation ki wajah se beech ki half-done state koi aur dekh nahi sakta."
 */