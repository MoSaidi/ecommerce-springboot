package com.bookstore.service;

import com.bookstore.domain.Shoppingcard;

public interface ShoppingCartService {
	Shoppingcard updateShoppingCart(Shoppingcard shoppingCart);
	void clearShoppingCart(Shoppingcard shoppingCart);
}
