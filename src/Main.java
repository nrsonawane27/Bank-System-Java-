import java.util.Scanner;
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Sign out account");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.next();
                    System.out.print("Enter password: ");
                    String password = scanner.next();
                    if (bank.registerUser(username, password)) {
                        System.out.println("User registered successfully!");
                    } else {
                        System.out.println("Registration failed!");
                    }
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    username = scanner.next();
                    System.out.print("Enter password: ");
                    password = scanner.next();
                    User user = bank.loginUser(username, password);
                    if (user != null) {
                        System.out.println("Login successful!");

                        boolean flag = true;
                        while(flag == true) {
                            System.out.println("1. Create Account");
                            System.out.println("2. Deposit");
                            System.out.println("3. Withdraw");
                            System.out.println("4. Check Balance");
                            System.out.println("5. Delete account");
                            System.out.println("6. Logout");
                            System.out.print("Enter choice: ");
                            choice = scanner.nextInt();

                            switch (choice) {
                                case 1 :
                                    if (bank.createAccount(password)) {
                                        System.out.println("Account created successfully!");
                                    } else {
                                        System.out.println("Account creation failed!");
                                    }
                                    break;

                                case 2 :
                                    System.out.print("Enter account password: ");
                                    String pass = scanner.next();
                                    if(password.equals(pass)) {
                                        System.out.print("Enter amount to deposit: ");
                                        double amount = scanner.nextDouble();
                                        if (bank.deposit(password, amount)) {
                                            System.out.println("Deposit successful!");
                                        } else {
                                            System.out.println("Deposit failed!.....Create account first.");
                                        }
                                    } else {
                                        System.out.println("Enter correct password");
                                    }
                                    break;

                                case 3 :
                                    System.out.print("Enter account password: ");
                                    pass = scanner.next();
                                    if(password.equals(pass)) {
                                        System.out.print("Enter amount to withdraw: ");
                                        double amount = scanner.nextDouble();
                                        if (bank.withdraw(password, amount)) {
                                            System.out.println("Withdrawal successful!");
                                        } else {
                                            System.out.println("Withdrawal failed!.....Create account first.");
                                        }
                                    } else {
                                        System.out.println("Enter correct password");
                                    }
                                    break;

                                case 4 :
                                    System.out.print("Enter account password: ");
                                    pass = scanner.next();
                                    if(password.equals(pass)) {
                                        bank.checkBalance(password);
                                    }else {
                                        System.out.println("Enter correct password");
                                    }
                                    break;

                                case  5 :
                                    System.out.print("Enter account password: ");
                                    pass = scanner.next();
                                    if(password.equals(pass)) {
                                        bank.deleteAccount(password);
                                        System.out.println("Account deleted successfully");
                                    }else {
                                        System.out.println("Enter correct password");
                                    }
                                    break;

                                case 6 :
                                    flag = false;
                                    System.out.println("logout successfully");
                            }
                        }
                    } else {
                        System.out.println("Login failed!");
                    }
                    break;

                case 3 :
                    System.out.print("Enter username: ");
                    username = scanner.next();
                    System.out.print("Enter password: ");
                    password = scanner.next();
                    if (bank.signOut(username, password)) {
                        System.out.println("User sign out successfully!");
                    } else {
                        System.out.println("Enter correct password");
                    }
                    break;
                case 4 :
                    System.out.println("Thank you !!!");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}