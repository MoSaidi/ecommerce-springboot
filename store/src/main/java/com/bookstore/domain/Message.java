package com.bookstore.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
 private Long idmessage;
 private Date datemessage;
 @Column(columnDefinition="text")
 private String message;
 @ManyToOne
 @JoinColumn(name="book_id")
 private Book book;
 @ManyToOne
 @JoinColumn(name="user_id")
 private User user;
public Long getIdmessage() {
	return idmessage;
}
public void setIdmessage(Long idmessage) {
	this.idmessage = idmessage;
}
public Date getDatemessage() {
	return datemessage;
}
public void setDatemessage(Date datemessage) {
	this.datemessage = datemessage;
}
public Book getBook() {
	return book;
}
public void setBook(Book book) {
	this.book = book;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
 
}
