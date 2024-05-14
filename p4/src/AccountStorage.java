import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class AccountStorage implements AccountStorageInterface<Account> {
    List<Account> accounts = new ArrayList<>();
    Statement SQLStatement;
    Connection SQLConnection;

    public AccountStorage() {
        try {
            Class.forName("org.sqlite.JDBC");
            SQLConnection = DriverManager.getConnection("jdbc:sqlite:p3.sqlite");
            SQLStatement = SQLConnection.createStatement();
            SQLStatement.execute(
                    "CREATE TABLE IF NOT EXISTS accounts("
                        + "name TEXT, "
                        + "password TEXT, "
                        + "email TEXT)"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Account> load_from_db() throws SQLException {
        ArrayList<Account> result = new ArrayList<>();

        var resultSet = SQLStatement.executeQuery("SELECT * FROM accounts");
        while (resultSet.next()) {
            Account account = new Account();
            account.name = resultSet.getString("name");
            account.password = resultSet.getString("password");
            account.email = resultSet.getString("email");
            result.add(account);
        }

        return result;
    }

    public void save_to_db() throws SQLException {
        SQLStatement.execute("BEGIN TRANSACTION");
        SQLStatement.execute("DELETE FROM accounts");

            PreparedStatement statement = SQLConnection.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?)");
        for (Account account : accounts) {
            statement.setString(1, account.name);
            statement.setString(2, account.password);
            statement.setString(3, account.email);
            statement.executeUpdate();
        }

        SQLStatement.execute("END TRANSACTION");

    }

    public int validate(Account account) {
        String name = account.name;
        String password = account.password;
        String email = account.email;

        if (name.length() < 3)
            return 1;

        if (password.length() < 3)
            return 2;

        if (email.length() < 3)
            return 3;

        return 0;
    }

    /**
     * Возвращает аккаунт по имени
     *
     * @param  name Имя аккаунта
     * @return     Объект Optional<account>
     */
    @Override
    public Optional<Account> get(String name) {
        Account foundAccount = null;

        for (Account account : accounts) {
            if (name.equals(account.name)) {
                foundAccount = account;
                break;
            }
        }

        return Optional.ofNullable(foundAccount);
    }

    /**
     * Получить список зарегистрированных аккаунтов
     *
     * @return     список аккаунтов
     */
    @Override
    public List<Account> getAll() {
        return accounts;
    }

    /**
     * Возвращает количество зарегистрированных аккаунтов
     *
     * @return количество аккаунтов
     */
    @Override
    public int count() {
        return accounts.size();
    }

    /**
     * Загрузить сохраненные аккаунты из json файла
     *
     */
    @Override
    public void load() {
        try {
            accounts = load_from_db();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            save_to_db();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удалить аккаунт
     *
     * @param t объект Account
     */
    @Override
    public void delete(Account t) {
        accounts.remove(t);
    }

    /**
     * Обновить данные аккаунта
     *
     * @param  old   аккаунт, данные которого будут обновлены
     * @param  new_t новые данные
     */
    @Override
    public void update(Account old, Account new_t) {
        accounts.set(accounts.indexOf(old), new_t);
    }

    /**
     * Добавляет аккаунт в список accounts
     *
     * @param  t объект Account
     */
    @Override
    public void insert(Account t) {
        accounts.add(t);
    }
}