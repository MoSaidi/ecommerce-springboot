package com.bookstore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.Book;
import com.bookstore.domain.Cartitem;
import com.bookstore.domain.Shoppingcard;
import com.bookstore.domain.User;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;
	@Autowired
	private CartItemService cartitemservice;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@RequestMapping("/addItem")
	public String additem(@ModelAttribute("book")Book book,@ModelAttribute("qty") String qty,Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		book = bookService.findOne(book.getId());
		if(Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail?id="+book.getId();
		}
		Cartitem cartItem = cartitemservice.addBookToCartItem(book, user, Integer.parseInt(qty));
        model.addAttribute("addBookSuccess", true);
		return "forward:/bookDetail?id="+book.getId();
	}
	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		Shoppingcard shoppingCart = user.getShoppingCart();
		List<Cartitem> cartItemList = cartitemservice.findByShoppingCart(shoppingCart);
		if(cartItemList.size()==0) {
			shoppingCartService.updateShoppingCart(shoppingCart);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", shoppingCart);
			model.addAttribute("emptyCart", true);
			return "shoppingCart";
		}
		shoppingCartService.updateShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		return "shoppingCart";
	}
	@RequestMapping(value="/updateCartItem")
	public String updateShoppingCart(@ModelAttribute("id") Long cartItemId,@ModelAttribute("qty") int qty) {
		Cartitem catt=cartitemservice.findById(cartItemId);
		catt.setQty(qty);
		cartitemservice.updateCartItem(catt);
		return "forward:/shoppingCart/cart";
	}
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartitemservice.removeCartItem(cartitemservice.findById(id));
		return "forward:/shoppingCart/cart";
	}
}
