package mealplanner;

import java.util.ArrayList;

public interface genericDAO<T> {

    ArrayList<T> fetchByList(String Query);

    ArrayList<T> fetchAll();

    T findById(int id);

    T findByName(String recordName);

    void insert(T t);

    void update(T t);

    void deleteById(int id);
}

