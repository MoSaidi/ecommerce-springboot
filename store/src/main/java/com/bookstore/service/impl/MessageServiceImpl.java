package com.bookstore.service.impl;

import java.util.Date;
import java.util.List;

import javax.mail.MessageAware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Book;
import com.bookstore.domain.Message;
import com.bookstore.domain.Note;
import com.bookstore.domain.User;
import com.bookstore.repository.MessageRepository;
import com.bookstore.repository.NoteRepository;
import com.bookstore.service.MessageService;
@Service
public class MessageServiceImpl implements MessageService{
	@Autowired
	private MessageRepository messageRepository;
	public Message findnote(User u,Book b) {
		return messageRepository.findByBookAndUser(b, u);
	}
	public List<Message> findbybook(Book b){
		return messageRepository.findByBook(b);
	}
	public void createmessage(User u,Book b,String r) {
		Message n=messageRepository.findByBookAndUser(b, u);
		if(n==null) {
			Message na=new Message();
			na.setBook(b);
			na.setUser(u);
			na.setMessage(r);
			Date d=new Date();
			na.setDatemessage(d);
			messageRepository.save(na);
		}
		else {
			Date d=new Date();
			n.setDatemessage(d);
			n.setMessage(r);
			messageRepository.save(n);
		}
	}
}
