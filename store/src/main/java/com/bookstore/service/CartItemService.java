package com.bookstore.service;

import java.util.List;

import com.bookstore.domain.Book;
import com.bookstore.domain.Cartitem;
import com.bookstore.domain.Order;
import com.bookstore.domain.Shoppingcard;
import com.bookstore.domain.User;

public interface CartItemService {
	Cartitem addBookToCartItem(Book book, User user, int qty);
	List<Cartitem> findByShoppingCart(Shoppingcard shoppingcard);
	Cartitem updateCartItem(Cartitem cartItem);
	Cartitem findById(Long id);
	void removeCartItem(Cartitem cartItem);
	Cartitem save(Cartitem cartItem);
	List<Cartitem> findByOrder(Order order);
}
