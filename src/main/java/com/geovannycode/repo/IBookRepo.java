package com.geovannycode.repo;

import com.geovannycode.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepo extends JpaRepository<Book, Integer> {

}
