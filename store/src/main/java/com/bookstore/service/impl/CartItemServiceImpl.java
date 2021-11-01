package com.bookstore.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Book;
import com.bookstore.domain.Cartitem;
import com.bookstore.domain.Order;
import com.bookstore.domain.Shoppingcard;
import com.bookstore.domain.User;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.service.CartItemService;
@Service
public class CartItemServiceImpl implements CartItemService{
	@Autowired
	private CartItemRepository cartItemRepository;
	public Cartitem addBookToCartItem(Book book, User user, int qty) {
		List<Cartitem> cartitems=cartItemRepository.findByShoppingCart(user.getShoppingCart());
		for(Cartitem cartitem:cartitems) {
			if(book.getId()==cartitem.getBook().getId()) {
				cartitem.setQty(cartitem.getQty()+qty);
				cartitem.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartitem);
				return cartitem;
			}
		}
		Cartitem cart=new Cartitem();
		cart.setBook(book);
		cart.setQty(qty);
		cart.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
		cart.setShoppingCart(user.getShoppingCart());
		cart =cartItemRepository.save(cart);
		return cart;
	}
	public List<Cartitem> findByShoppingCart(Shoppingcard shoppingcard) {
		return cartItemRepository.findByShoppingCart(shoppingcard);
	}
	public Cartitem updateCartItem(Cartitem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(cartItem.getBook().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));	
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);	
		cartItemRepository.save(cartItem);	
		return cartItem;
	}
	public Cartitem findById(Long id) {
		return cartItemRepository.findOne(id);
	}
	public void removeCartItem(Cartitem cartItem) {
		cartItemRepository.delete(cartItem);
	}
	public Cartitem save(Cartitem cartItem) {
		return cartItemRepository.save(cartItem);
	}
	public List<Cartitem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}
}
