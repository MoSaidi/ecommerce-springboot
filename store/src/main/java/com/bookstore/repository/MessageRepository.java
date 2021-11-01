package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.Book;
import com.bookstore.domain.Message;
import com.bookstore.domain.Note;
import com.bookstore.domain.User;

public interface MessageRepository extends CrudRepository<Message,Long> {
	 Message findByBookAndUser(Book b,User u);
	 List<Message> findByBook(Book b);
}
