package com.bookstore.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Book;
import com.bookstore.domain.Cartitem;
import com.bookstore.domain.CreditCard;
import com.bookstore.domain.Order;
import com.bookstore.repository.OrderRepository;
import com.bookstore.domain.Shipping_adresse;
import com.bookstore.domain.Shoppingcard;
import com.bookstore.domain.User;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CartItemService cartItemService;
	public Order findOne(Long id) {
		return orderRepository.findOne(id);
	}
	public synchronized Order createOrder(Shoppingcard shoppingCart,Shipping_adresse shippingAddress,CreditCard payment,String shippingMethod,User user) {
		Order order=new Order();
		order.setShippingAddress(shippingAddress);
		order.setOrderStatus("created");
		order.setShippingMethod(shippingMethod);
		order.setCreditcard(payment);
		List<Cartitem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		for(Cartitem c:cartItemList) {
			Book b=c.getBook();
			c.setOrder(order);
			b.setInStockNumber(b.getInStockNumber() - c.getQty());
			b.setPiecesolde(b.getPiecesolde() + c.getQty());
		}
		order.setCartItemList(cartItemList);
		Calendar c=Calendar.getInstance();
		order.setOrderDate(c.getTime());
		if(shippingMethod.equals("groundShipping")) {
		Date d=new Date();
		Calendar ca = Calendar.getInstance(); 
		ca.setTime(d); 
		c.add(Calendar.DATE, 5);
		d = c.getTime();
		order.setShippingDate(d);
		}
		if(shippingMethod.equals("premiumShipping")) {
			Date d=new Date();
			Calendar ca = Calendar.getInstance(); 
			ca.setTime(d); 
			c.add(Calendar.DATE, 3);
			d = c.getTime();
			order.setShippingDate(d);
			}
		order.setOrderTotal(shoppingCart.getGrandTotal());
		order.setUser(user);
		order.setCartItemList(cartItemList);
		shippingAddress.setOrder(order);
		payment.setOrder(order);
		orderRepository.save(order);
		return order;
	}
}
