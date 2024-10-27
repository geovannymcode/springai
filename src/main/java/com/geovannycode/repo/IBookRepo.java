package com.geovannycode.repo;

import com.geovannycode.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBookRepo extends JpaRepository<Book, Integer> {
    List<Book> findByNameLike(String name);
}
