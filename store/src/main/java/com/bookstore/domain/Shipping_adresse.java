package com.bookstore.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Shipping_adresse {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String ShippingAddressName;
	private String ShippingAddressStreet;
	private String ShippingAddressCity;
	private String ShippingAddressState;
	private String ShippingAddressCountry;
	private String ShippingAddressZipcode;
	@OneToOne
	private Order order;
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
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public String getShippingAddressStreet() {
		return ShippingAddressStreet;
	}
	public void setShippingAddressStreet(String shippingAddressStreet) {
		ShippingAddressStreet = shippingAddressStreet;
	}
	
}
