package thundrware.com.bistromobile.data;

import java.util.Collection;
import java.util.List;


public interface Repository<T> {
    void add(T item);
    void addRange(Collection<T> items);
    void update(T itemToUpdate, int id);
    void delete(int id);
    T get(int id);
    List<T> get();
}
