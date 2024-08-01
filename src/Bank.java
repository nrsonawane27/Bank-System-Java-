import java.sql.*;
import java.util.Scanner;

public class Bank {

    public boolean registerUser(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("password invalid.");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean signOut(String username, String userPassword) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, userPassword);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Delete account first");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User loginUser(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setPassword(resultSet.getString("password"));
                user.setUsername(resultSet.getString("username"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createAccount(String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Step 1: Check if an account already exists with the given user password
            String checkSql = "SELECT COUNT(*) FROM accounts WHERE user_pass = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, password);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) != 0) {
                // Account already exists
                System.out.println("Account already exists for user password " + password);
                return false;
            }

//            // Step 2: Insert a new account
//            System.out.println("account creating");
//            String sql = "INSERT INTO accounts (user_pass) VALUES (?)";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, password);
//            int rows = statement.executeUpdate();

            /////////////////////////////////////////////////

            // Collect user details before inserting
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter your email: ");
            String email = sc.next();

            if (!email.contains("@gmail.com")) {
                System.out.println("Enter a valid email address....");
                return false;
            }

            String phoneNo;
            phoneNo = null;
            boolean flag = false;
//            String checkQuery = "SELECT COUNT(*) FROM details WHERE phone_no = ?";
//            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
//            checkStmt.setString(1, phoneNo);
//            ResultSet rs = checkStmt.executeQuery();
//
//            if (rs.next() && rs.getInt(1) > 0) {
//                System.out.println("Phone number already exists.");
//                return false;
//            }

            while (flag == false) {
                System.out.println("Enter your phone number: ");
                phoneNo = sc.next();

                if (phoneNo.length() == 10) {
                    flag = true;
                    String checkQuery = "SELECT COUNT(*) FROM details WHERE phone_no = ?";
                    PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                    checkStmt.setString(1, phoneNo);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Phone number already exists.");
                        return false;
                    }
//                    return true;
                } else {
                    System.out.println("Enter valid phone number....");
                    flag = false;
//                    return false;
                }
            }

            // Start transaction
            connection.setAutoCommit(false);

            try {
                // Step 2: Insert a new account
                String sql = "INSERT INTO accounts (user_pass) VALUES (?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, password);
                int rows = statement.executeUpdate();

                // Step 3: Insert details
                String detailSql = "INSERT INTO details (pass, email, phone_no) VALUES (?, ?, ?)";
                PreparedStatement detailStatement = connection.prepareStatement(detailSql);
                detailStatement.setString(1, password);
                detailStatement.setString(2, email);
                detailStatement.setString(3, phoneNo);
                detailStatement.executeUpdate();

                // Commit transaction
                connection.commit();
                return rows > 0;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
            ////////////////////////////////////////////
//            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteAccount(String userPassword) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM accounts WHERE user_pass = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userPassword);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deposit(String password, double amount) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE accounts SET balance = balance + ? WHERE user_pass = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, amount);
            statement.setString(2, password);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean withdraw(String password, double amount) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT balance FROM accounts WHERE user_pass = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                if (balance >= amount) {
                    sql = "UPDATE accounts SET balance = balance - ? WHERE user_pass = ?";
                    statement = connection.prepareStatement(sql);
                    statement.setDouble(1, amount);
                    statement.setString(2, password);
                    int rows = statement.executeUpdate();
                    return rows > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double checkBalance(String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT balance FROM accounts WHERE user_pass = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Balance : " +resultSet.getDouble("balance"));
                return resultSet.getDouble("balance");
            } else {
                System.out.println("Account not found.....Create account first.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
