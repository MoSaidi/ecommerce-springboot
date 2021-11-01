package com.bookstore.service;

import com.bookstore.domain.CreditCard;
import com.bookstore.domain.Order;
import com.bookstore.domain.Shipping_adresse;
import com.bookstore.domain.Shoppingcard;
import com.bookstore.domain.User;

public interface OrderService {
	Order createOrder(Shoppingcard shoppingCart,Shipping_adresse shippingAddress,CreditCard payment,String shippingMethod,User user);
	Order findOne(Long id);
}
