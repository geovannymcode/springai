package com.geovannycode.repo;

import com.geovannycode.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorRepo extends JpaRepository<Author, Integer> {
}
