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
@Table(name = "notes")
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
 private Long idnote;
private float rating;
 @ManyToOne
 @JoinColumn(name="book_id")
 private Book book;
 @ManyToOne
 @JoinColumn(name="user_id")
 private User user;

public Long getIdnote() {
	return idnote;
}
public void setIdnote(Long idnote) {
	this.idnote = idnote;
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
public float getRating() {
	return rating;
}
public void setRating(float rating) {
	this.rating = rating;
}

}
