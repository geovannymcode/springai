package com.geovannycode.service.impl;

import com.geovannycode.model.Author;
import com.geovannycode.repo.IAuthorRepo;
import com.geovannycode.service.IAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements IAuthorService {

    private final IAuthorRepo repo;

    @Override
    public Author save(Author book) throws Exception {
        return repo.save(book);
    }

    @Override
    public List<Author> saveAll(List<Author> list) throws Exception {
        return repo.saveAll(list);
    }

    @Override
    public Author update(Author book, Integer integer) throws Exception {
        return repo.save(book);
    }

    @Override
    public List<Author> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Author findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new Author());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }
}
