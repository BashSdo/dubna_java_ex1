import java.util.Scanner;
import java.util.Arrays;

class Account {
    String name;
    String password;
    String email;
}

class Main {
    public static String welcomeString = "Hello world!";
    public static String[] actions = {
            "Log in",
            "Register",
            "Exit"
    };
    public static Account[] accounts = {};
    public static Scanner scanner = new Scanner(System.in);

    static void register() {
        Account account = new Account();

        System.out.print("Name:");
        account.name = scanner.next();

        System.out.print("Password:");
        account.password = scanner.next();

        System.out.print("Email:");
        account.email = scanner.next();

        System.out.println("Registration was successful!");

        accounts = Arrays.copyOf(accounts, accounts.length + 1);
        accounts[accounts.length - 1] = account;

        actions();
    }

    static void loggedIn(Account account) {
        System.out.println("Hello, " + account.name + "!" + "\n" + "Your email: " + account.email + "\n");
        actions();
    }

    static void login() {
        String name;
        String password;

        System.out.print("Name:");
        name = scanner.next();

        System.out.print("Password:");
        password = scanner.next();

        for (int i = 0; i < accounts.length; i++) {
            if (name.equals(accounts[i].name) && password.equals(accounts[i].password)) {
                System.out.println("\nLogin successful!");
                loggedIn(accounts[i]);
                actions();
                return;
            }
        }

        System.out.println("Login failed: incorrect name or password!");

        actions();
    }

    static void actions() {
        for (int i = 0; i < actions.length; i++) {
            System.out.println((i + 1) + " - " + actions[i]);
        }

        System.out.println("Accounts number: " + accounts.length + "\nSelect: ");

        int action = scanner.nextInt();
        switch (action) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                // Finish the program
                break;
            default:
                System.out.println("Unknown action");
                actions();
                break;
        }
    }

    public static void main(String[] args) {
        System.out.println(welcomeString);
        System.out.println();

        actions();

        scanner.close();
    }

}