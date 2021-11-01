package com.bookstore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.Book;
import com.bookstore.domain.User;
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;


@Controller
public class SearchController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;

	@RequestMapping("/searchByCategory")
	public String searchByCategory(
			@RequestParam("category") String category,
			Model model, Principal principal
			){
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		String classActiveCategory = "active"+category;
		classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
		classActiveCategory = classActiveCategory.replaceAll("&", "");
		model.addAttribute(classActiveCategory, true);
		
		List<Book> bookList = bookService.findByCategory(category);
		
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		return "bookshelf";
	}
	@RequestMapping("/searchByRating")
	public String searchByRating(Model model, Principal principal) {
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		List<Book> books=bookService.findAll();
		if (books.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		Book[] t=new Book[books.size()];
		books.toArray(t);
		int max;
		for(int ar=0;ar<books.size()-1;ar++) {
			max=ar;
			for(int j=ar+1;j<books.size();j++) {
				if(t[j].calculernote() > t[max].calculernote()) {
					max=j;
				}
			}
			if(max!=ar) {
				Book book;
				book=t[ar];
				t[ar]=t[max];
				t[max]=book;
			}
		}
		List<Book> booklist = Arrays.asList(t);
		model.addAttribute("bookList", booklist);
		model.addAttribute("activeRated",true);
		return "bookshelf";
	}
	@RequestMapping("/searchBypiecesolde")
	public String searchBypiecesolde(Principal principal,Model model) {
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		List<Book> bookList = bookService.findByOrderByPieceSolde();
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		model.addAttribute("activeBought",true);
		return "bookshelf";
	}
	@RequestMapping("/searchBook")
	public String searchBook(
			@ModelAttribute("keyword") String keyword,
			Principal principal, Model model
			) {
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		List<Book> bookList = bookService.blurrySearch(keyword);
		
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
}
