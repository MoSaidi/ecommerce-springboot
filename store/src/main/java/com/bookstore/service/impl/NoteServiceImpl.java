package com.bookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Book;
import com.bookstore.domain.Note;
import com.bookstore.domain.User;
import com.bookstore.repository.NoteRepository;
import com.bookstore.service.NoteService;
@Service
public class NoteServiceImpl implements NoteService{
@Autowired
private NoteRepository noterepository;
public Note findnote(User u,Book b) {
	return noterepository.findByUserAndBook(u, b);
}
public void createnote(User u,Book b,float r) {
	Note n=noterepository.findByUserAndBook(u, b);
	if(n==null) {
		Note na=new Note();
		na.setBook(b);
		na.setUser(u);
		na.setRating(r);
		noterepository.save(na);
	}
	else {
		n.setRating(r);
		noterepository.save(n);
	}
}
}
