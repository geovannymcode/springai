package com.geovannycode.service.impl;

import com.geovannycode.model.Book;
import com.geovannycode.repo.IBookRepo;
import com.geovannycode.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {

    private final IBookRepo repo;

    @Override
    public Book save(Book book) throws Exception {
        return repo.save(book);
    }

    @Override
    public List<Book> saveAll(List<Book> list) throws Exception {
        return repo.saveAll(list);
    }

    @Override
    public Book update(Book book, Integer integer) throws Exception {
        return repo.save(book);
    }

    @Override
    public List<Book> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Book findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new Book());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }
}
