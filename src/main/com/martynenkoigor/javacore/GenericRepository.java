package com.martynenkoigor.javacore;

import java.util.List;

public interface GenericRepository<T, ID> {
    public T create(String name);
    public T read(ID id);
    public T update(T value);
    public void deleteById(ID id);
    public List<T> getAll();
}
