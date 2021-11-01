package com.bookstore.service;

import java.util.List;

import com.bookstore.domain.Book;
import com.bookstore.domain.Message;
import com.bookstore.domain.User;

public interface MessageService {
	void createmessage(User u,Book b,String r) ;
	 Message findnote(User u,Book b);
	 List<Message> findbybook(Book b);
}
