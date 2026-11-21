/*
 * Q11. JDBC – Bank Transfer with Transaction Management (20 Marks)
 * Create a money-transfer application using JDBC and MySQL. Initial account
 * balances are:
 * Interview challenge: Transfer ₹10,000 from Account A to Account B. Your
 * application must deduct the amount from A, credit B, and insert a
 * transaction-history record. If any step fails, roll back all changes. Commit
 * only when every operation succeeds. Also verify that the sender has
 * sufficient balance.
 * Account Initial Balance
 * A ₹50,000
 * B ₹20,000
 * Implementation requirements:
 * • Disable auto-commit using setAutoCommit(false).
 * • Perform debit, credit, and transaction-history insertion in one
 * transaction.
 * • Call commit() only after all operations succeed.
 * • Call rollback() if any operation fails.
 * • Restore the connection's auto-commit setting when appropriate.
 * Interview challenge: What happens if the application crashes after deducting
 * money from Account A but before crediting Account B? Explain how database
 * transactions protect consistency.
 * 
 */

package Q12;

/**
 * Account
 */
public class Account {
    private String account_Holder_Name;
    private int initial_Balance;

    public String getAccountHolderName() {
    return account_Holder_Name;
    }

    public int getInitialBalance() {
    return initial_Balance;
    }

}
