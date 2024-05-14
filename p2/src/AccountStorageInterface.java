import java.util.List;
import java.util.Optional;

public interface AccountStorageInterface<T> {
    Optional<T> get(String name);

    List<T> getAll();

    void load();

    void save();

    void delete(T t);

    void update(T old, T new_t);

    void insert(T t);

    int count();
}
