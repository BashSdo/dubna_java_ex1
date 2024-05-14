import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.nio.file.Files;
import java.nio.file.Paths;

class AccountStorage implements AccountStorageInterface<Account> {
    List<Account> accounts = new ArrayList<>();

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
        Gson gson = new Gson();
        JsonReader reader;
        try {
            reader = new JsonReader(new FileReader("./accounts.json"));
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                System.out.println("Database File not found!");
            }

            e.printStackTrace();
            System.exit(2);
            return;
        }

        Account[] _accounts = gson.fromJson(reader, Account[].class);
        accounts = new ArrayList<>(Arrays.asList(_accounts));
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        String json = gson.toJson(accounts.toArray(), Account[].class);
        
        try {
            Files.write(Paths.get("./accounts.json"), json.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(3);
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