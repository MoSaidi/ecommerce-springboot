package com.bookstore.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Shoppingcard {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private BigDecimal GrandTotal;

	@OneToMany(mappedBy="shoppingCart", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Cartitem> cartItemList;
	@OneToOne(cascade = CascadeType.ALL)
	private User user;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getGrandTotal() {
		return GrandTotal;
	}
	public void setGrandTotal(BigDecimal grandTotal) {
		GrandTotal = grandTotal;
	}
	public List<Cartitem> getCartItemList() {
		return cartItemList;
	}
	public void setCartItemList(List<Cartitem> cartItemList) {
		this.cartItemList = cartItemList;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
