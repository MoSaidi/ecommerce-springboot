package com.bookstore.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.Book;
import com.bookstore.domain.Note;
import com.bookstore.domain.User;

public interface NoteRepository extends CrudRepository<Note, Long>{
	Note findByUserAndBook(User u,Book b);
}
