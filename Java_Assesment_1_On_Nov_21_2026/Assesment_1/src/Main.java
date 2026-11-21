
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
