package com.martynenkoigor.javacore;

public interface GenericRepository<T, ID> {
    public T create(String name);
    public T read(ID id);
    public T update(T value);
    public T delete(ID id);
}
