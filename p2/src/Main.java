import java.util.Scanner;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public static AccountStorage accounts = new AccountStorage();
    public static Scanner scanner = new Scanner(System.in);

    static String ReadString(String message) {
        System.out.print(message + "> ");
        return scanner.next();
    }

    static void register() {
        Account account = new Account();
        account.name = ReadString("Name");
        account.password = ReadString("Password");
        account.email = ReadString("Email");

        if (accounts.get(account.name).isPresent()) {
            System.out.println("User with this name already exists! Try again");
            register();
            return;
        }

        accounts.insert(account);
        accounts.save();

        System.out.println("Registration was successful!");
        actions();
    }

    static void loggedIn(Account account) {
        System.out.println("Hello, " + account.name + "!\nYour Email: " + account.email + "\n");
        actions();
    }

    static void login() {
        var name = ReadString("Name");
        var password = ReadString("Password");

        Optional<Account> account = accounts.get(name);

        if (account.isPresent() && account.get().password.equals(password)) {
            loggedIn(account.get());
        }

        System.out.println("Login failed: incorrect name or password!");
        actions();
    }

    static void actions() {
        for (int i = 0; i < actions.length; i++) {
            System.out.println((i+1) + " - " + actions[i]);
        }

        System.out.println("Accounts number: " + accounts.count() + "\n");

        System.out.print("Select: ");

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

        if (!Files.exists(Paths.get("./accounts.json"))) {
            accounts.save();
        }else{
            accounts.load();
        }

        actions();

        scanner.close();
    }

}