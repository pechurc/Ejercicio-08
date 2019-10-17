package com.eiv.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, I> {
    
    Optional<T> findById(I id);
    
    List<T> findAll();
    
    void save(T t);
    
    void delete(I id);
}
