package com.bookstore.service;

import com.bookstore.domain.Book;
import com.bookstore.domain.Note;
import com.bookstore.domain.User;

public interface NoteService {
	Note findnote(User u,Book b);
	void createnote(User u,Book b,float r);
}
