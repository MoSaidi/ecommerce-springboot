package com.bookstore.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Cartitem;
import com.bookstore.domain.Shoppingcard;
import com.bookstore.repository.ShoppingCardRepository;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCardRepository shoppingCardRepository;
	
	public Shoppingcard updateShoppingCart(Shoppingcard shoppingCart) {
		BigDecimal cartTotal = new BigDecimal(0);
		List<Cartitem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		for(Cartitem cartitem:cartItemList) {
			if(cartitem.getBook().getInStockNumber() > 0) {
				cartItemService.updateCartItem(cartitem);
				cartTotal = cartTotal.add(cartitem.getSubtotal());
			}
		}
		shoppingCart.setGrandTotal(cartTotal);
		shoppingCardRepository.save(shoppingCart);
		return shoppingCart;
	}
	public void clearShoppingCart(Shoppingcard shoppingCart) {
		List<Cartitem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		for (Cartitem cartItem : cartItemList) {
			cartItem.setShoppingCart(null);
			cartItemService.save(cartItem);
		}
		shoppingCart.setGrandTotal(new BigDecimal(0));
		shoppingCardRepository.save(shoppingCart);
	}
}
