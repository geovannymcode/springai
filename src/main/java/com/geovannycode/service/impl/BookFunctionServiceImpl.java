package com.geovannycode.service.impl;

import com.geovannycode.model.Book;
import com.geovannycode.repo.IBookRepo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class BookFunctionServiceImpl implements Function<BookFunctionServiceImpl.Request, BookFunctionServiceImpl.Response> {

    private final IBookRepo repo;

    public record Request(String bookName){}
    public record Response(List<Book> books){}

    public Response apply(Request request) {
        List<Book> books = repo.findByNameLike("%" + request.bookName + "%");
        return new Response(books);
    }
}
