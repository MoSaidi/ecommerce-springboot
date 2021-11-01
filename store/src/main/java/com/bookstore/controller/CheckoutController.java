package com.bookstore.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.Cartitem;
import com.bookstore.domain.CreditCard;
import com.bookstore.domain.Order;
import com.bookstore.domain.Shipping_adresse;
import com.bookstore.domain.Shoppingcard;
import com.bookstore.domain.User;
import com.bookstore.domain.Usercreditcard;
import com.bookstore.domain.Usershipping;
import com.bookstore.repository.UsershippingRepository;
import com.bookstore.service.CartItemService;
import com.bookstore.service.CreditCardService;
import com.bookstore.service.OrderService;
import com.bookstore.service.ShippingAdresseService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.service.UsercreditcardService;
import com.bookstore.utility.MailConstructor;

@Controller
public class CheckoutController {
	private Shipping_adresse shipping_adresse =new Shipping_adresse();
	private CreditCard creditCard=new CreditCard();
	@Autowired
	private UserService userService;
	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private MailConstructor mailConstructor;
	@Autowired
	private UserShippingService userShippingService; 
	@Autowired
	private ShippingAdresseService shippingAdresseService;
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private UsercreditcardService usercreditcardService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private JavaMailSender mailSender;
	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long ident,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Principal principal,Model model) {
		User user = userService.findByUsername(principal.getName());
		if (ident != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}
		List<Cartitem> carditemsCartitems=null;
		carditemsCartitems=cartItemService.findByShoppingCart(user.getShoppingCart());
		if (carditemsCartitems.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppingCart/cart";
		}
		for (Cartitem cartItem : carditemsCartitems) {
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}
		List<Usershipping> userShippingList=user.getUserShippingList();
		List<Usercreditcard> usercreditcard=user.getUsercrediscards();
		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}
		if(usercreditcard.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		}else {
			model.addAttribute("emptyPaymentList", false);
		}
		for (Usershipping userShipping : userShippingList) {
			if (userShipping.isDefaul()) {
				 shippingAdresseService.setByUserShipping(userShipping,shipping_adresse);
			}
		}
		for(Usercreditcard u : usercreditcard) {
			if(u.isDefaul()) {
				creditCardService.setByUserPayment(creditCard,u);
			}
		}
		model.addAttribute("cartItemList", carditemsCartitems);
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("userPaymentList",user.getUsercrediscards());
		model.addAttribute("shippingAddress", shipping_adresse);
		model.addAttribute("payment",creditCard);
		model.addAttribute("shoppingCart",user.getShoppingCart());
		model.addAttribute("classActiveShipping", true);
		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}
		return "checkout";
	}
	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId, Principal principal,Model model) {
		User user = userService.findByUsername(principal.getName());
		Usershipping userShipping = userShippingService.findById(userShippingId);
		if (userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} 
		List<Cartitem> carditemsCartitems=cartItemService.findByShoppingCart(user.getShoppingCart());
		if (carditemsCartitems.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}
		for (Cartitem cartItem : carditemsCartitems) {
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}
			shippingAdresseService.setByUserShipping(userShipping, shipping_adresse);
			List<Usershipping> userShippingList=user.getUserShippingList();
			List<Usercreditcard> usercreditcard=user.getUsercrediscards();
			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}
			if(usercreditcard.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			}else {
				model.addAttribute("emptyPaymentList", false);
			}
			for(Usercreditcard u : usercreditcard) {
				if(u.isDefaul()) {
					creditCardService.setByUserPayment(creditCard,u);
				}
			}
			model.addAttribute("cartItemList", carditemsCartitems);
			model.addAttribute("payment",creditCard);
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("userPaymentList",user.getUsercrediscards());
			model.addAttribute("shippingAddress", shipping_adresse);
			model.addAttribute("shoppingCart",user.getShoppingCart());
			model.addAttribute("classActiveShipping", true);
		return "checkout";
	}
	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId, Principal principal,Model model) {
		User user = userService.findByUsername(principal.getName());
		Usercreditcard u=usercreditcardService.findById(userPaymentId);
		if (u.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} 
		List<Cartitem> carditemsCartitems=cartItemService.findByShoppingCart(user.getShoppingCart());
		if (carditemsCartitems.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}
		for (Cartitem cartItem : carditemsCartitems) {
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}
		creditCardService.setByUserPayment(creditCard, u);
		List<Usershipping> userShippingList=user.getUserShippingList();
		List<Usercreditcard> usercreditcard=user.getUsercrediscards();
		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}
		if(usercreditcard.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		}else {
			model.addAttribute("emptyPaymentList", false);
		}
		for (Usershipping userShipping : userShippingList) {
			if (userShipping.isDefaul()) {
				 shippingAdresseService.setByUserShipping(userShipping,shipping_adresse);
			}
		}
		model.addAttribute("cartItemList", carditemsCartitems);
		model.addAttribute("payment",creditCard);
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("userPaymentList",user.getUsercrediscards());
		model.addAttribute("shippingAddress", shipping_adresse);
		model.addAttribute("shoppingCart",user.getShoppingCart());
		model.addAttribute("classActivePayment", true);
		return "checkout";
	}
	@RequestMapping(value = "/checkout",method = RequestMethod.POST)
	public String validercheckout(@ModelAttribute("shippingAddress") Shipping_adresse shippingAddress,@ModelAttribute("payment") CreditCard payment ,Principal principal,Model model,@ModelAttribute("shippingMethod") String shippingMethod) {
		Shoppingcard shoppingCart=userService.findByUsername(principal.getName()).getShoppingCart();
		List<Cartitem> cartItemList=cartItemService.findByShoppingCart(shoppingCart);
		if(shippingAddress.getShippingAddressCity().isEmpty() || shippingAddress.getShippingAddressCountry().isEmpty() || shippingAddress.getShippingAddressName().isEmpty() || shippingAddress.getShippingAddressState().isEmpty() || shippingAddress.getShippingAddressZipcode().isEmpty() || shippingAddress.getShippingAddressStreet().isEmpty() || payment.getCardName().isEmpty() || payment.getCardNumber().isEmpty() || payment.getType().isEmpty()) {
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";
		}
		User user = userService.findByUsername(principal.getName());
		Order order=orderService.createOrder(shoppingCart, shippingAddress, payment, shippingMethod, user);
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
		shoppingCartService.clearShoppingCart(shoppingCart);
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}
		
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		model.addAttribute("cartItemList",cartItemList);
		return "orderSubmittedPage";
	}
}
