package com.bookstore.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class Usercreditcard {
	   @Id
	   @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    private String type;
	    private String cardName;
		private String cardNumber;
	    private boolean defaul;
	    @ManyToOne
		@JoinColumn(name="user_id")
	    private User user;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
		public String getCardName() {
			return cardName;
		}
		public void setCardName(String cardName) {
			this.cardName = cardName;
		}
		public String getCardNumber() {
			return cardNumber;
		}
		public void setCardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
		}
		public boolean isDefaul() {
			return defaul;
		}
		public void setDefaul(boolean defaul) {
			this.defaul = defaul;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
	    
}
