package com.bookstore.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class Usershipping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String ShippingAddressName;
	private String ShippingAddressStreet;
	private String ShippingAddressCity;
	private String ShippingAddressState;
	private String ShippingAddressCountry;
	private String ShippingAddressZipcode;
	private boolean defaul;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getShippingAddressName() {
		return ShippingAddressName;
	}
	public void setShippingAddressName(String shippingAddressName) {
		ShippingAddressName = shippingAddressName;
	}
	public String getShippingAddressCity() {
		return ShippingAddressCity;
	}
	public void setShippingAddressCity(String shippingAddressCity) {
		ShippingAddressCity = shippingAddressCity;
	}
	public String getShippingAddressState() {
		return ShippingAddressState;
	}
	public void setShippingAddressState(String shippingAddressState) {
		ShippingAddressState = shippingAddressState;
	}
	public String getShippingAddressCountry() {
		return ShippingAddressCountry;
	}
	public void setShippingAddressCountry(String shippingAddressCountry) {
		ShippingAddressCountry = shippingAddressCountry;
	}
	public String getShippingAddressZipcode() {
		return ShippingAddressZipcode;
	}
	public void setShippingAddressZipcode(String shippingAddressZipcode) {
		ShippingAddressZipcode = shippingAddressZipcode;
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
	public String getShippingAddressStreet() {
		return ShippingAddressStreet;
	}
	public void setShippingAddressStreet(String shippingAddressStreet) {
		ShippingAddressStreet = shippingAddressStreet;
	}
	
}
