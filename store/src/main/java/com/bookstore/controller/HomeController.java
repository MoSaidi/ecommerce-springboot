package com.bookstore.controller;

import java.io.File;
import java.io.FileInputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;
import com.bookstore.domain.Book;
import com.bookstore.domain.Cartitem;
import com.bookstore.domain.Message;
import com.bookstore.domain.Order;
import com.bookstore.domain.User;
import com.bookstore.domain.Usercreditcard;
import com.bookstore.domain.Usershipping;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.MessageService;
import com.bookstore.service.NoteService;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.service.UsercreditcardService;
import com.bookstore.service.impl.UserSecurityService;

@Controller
public class HomeController {
	@Autowired
	private BookService bookService;
	@Autowired
	private RoleRepository rolerepository;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private UserService userservice;
	@Autowired
	UserRepository userrep;
	@Autowired
	private MailConstructor mailConstructor;
	@Autowired
	private UserSecurityService userSecurityService;
	@Autowired
	private UserShippingService usershippinservice;
	@Autowired
	private UsercreditcardService usercreditcardService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private NoteService noteService;
	@Autowired
	private MessageService  messageService ;
 @RequestMapping(value = "/",method = RequestMethod.GET)
 public String index() {
	 return "index";
 }
 @RequestMapping("/bookshelf")
public String bookshelf(Model model, Principal principal) {
	 if(principal != null) {
			String username = principal.getName();
			User user = userservice.findByUsername(username);
			model.addAttribute("user", user);
		}
	    List<Book> bookList=bookService.findAll();
		model.addAttribute("bookList", bookList);
		model.addAttribute("activeAll",true);
		
		return "bookshelf";
 }
 @RequestMapping(value = "/faq",method = RequestMethod.GET)
 public String faq() {
	 return "faq";
 }
 @RequestMapping(value = "/hours",method = RequestMethod.GET)
 public String hours() {
	 return "hours";
 }
 @RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
 @RequestMapping("/bookDetail")
 public String bookDetail(@RequestParam("id")Long id,Model model,Principal principal) {
	 if(principal != null) {
			String username = principal.getName();
			User user = userservice.findByUsername(username);
			model.addAttribute("user", user);
		}
	 Book book = bookService.findOne(id);
	   if(book.getMessages().size()==0) {
		   model.addAttribute("nomessage", true);
	   }
	   else {
		   model.addAttribute("withmessage", true);
		   model.addAttribute("messages",messageService.findbybook(book));
	   }
		model.addAttribute("book", book);
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		return "bookDetail";
 }
 @RequestMapping(value = "/newUser",method = RequestMethod.POST)
 public String newUserPost(HttpServletRequest req,@ModelAttribute("email") String userEmail,@ModelAttribute("username") String username,Model model) throws Exception {
	 model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);
		if(userrep.findByUsername(username)!=null) {
            model.addAttribute("usernameExists", true);
			return "myAccount";
		}
		if(userrep.findByEmail(userEmail)!=null) {
            model.addAttribute("emailExists", true);
			return "myAccount";
		}
		User u=new User();
		u.setEmail(userEmail);
		u.setUsername(username);
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		u.setPassword(encryptedPassword);
		Role role=null;
		role=(Role) rolerepository.findByname("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(u, role));
		userservice.createUser(u, userRoles);
		String token = UUID.randomUUID().toString();
		userservice.createPasswordResetTokenForUser(u, token);
		String appUrl = "http://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath();
		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, req.getLocale(), token, u, password);
        mailSender.send(email);
		model.addAttribute("emailSent", "true");
		return "myAccount";
 }
 @RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userservice.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("classActiveEdit", true);
		model.addAttribute("orderList", user.getOrderList());
		return "myProfile";
	}
 @RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
	public String updateUserInfo(
			@ModelAttribute("user") User user,
			@ModelAttribute("newPassword") String newPassword,
			Model model
			) throws Exception {
		User currentUser = userservice.findById(user.getId());
		
		if(currentUser == null) {
			throw new Exception ("User not found");
		}
		if(user.getUsername().isEmpty()) {
			model.addAttribute("classActiveEdit", true);
			model.addAttribute("usernameNull",true);
			model.addAttribute("orderList", currentUser.getOrderList())	;
			return "myProfile";
		}
		/*check email already exists*/
		if (userservice.findByEmail(user.getEmail())!=null) {
			if(userservice.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("emailExists", true);
				model.addAttribute("classActiveEdit", true);
				model.addAttribute("orderList", currentUser.getOrderList())	;
				return "myProfile";
			}
		}
		
		/*check username already exists*/
		if (userservice.findByUsername(user.getUsername())!=null) {
			if(userservice.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernameExists", true);
				model.addAttribute("classActiveEdit", true);
				model.addAttribute("orderList", currentUser.getOrderList())	;
				return "myProfile";
			}
		}
		
//		update password
		if((newPassword == null || newPassword.isEmpty() || newPassword.equals("")) && (!user.getUsername().isEmpty())) {
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if(passwordEncoder.matches(user.getPassword(), dbPassword)){
				currentUser.setFirstName(user.getFirstName());
				currentUser.setLastName(user.getLastName());
				userservice.save(currentUser);
				model.addAttribute("updateSuccess", true);
				model.addAttribute("user", currentUser);
				model.addAttribute("classActiveEdit", true);
				model.addAttribute("orderList", user.getOrderList());
				UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
						userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				return "myProfile";
			} else {
				model.addAttribute("incorrectPassword", true);
				model.addAttribute("classActiveEdit", true);
				model.addAttribute("orderList", currentUser.getOrderList())	;
				return "myProfile";
			}
			
		}
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("") && !user.getUsername().isEmpty()){
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if(passwordEncoder.matches(user.getPassword(), dbPassword)){
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);
				model.addAttribute("classActiveEdit", true);
				model.addAttribute("orderList", currentUser.getOrderList())	;
				return "myProfile";
			}
		}	
		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setUsername(user.getUsername());
		currentUser.setEmail(user.getEmail());
		userservice.save(currentUser);
		model.addAttribute("updateSuccess", true);
		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);
		model.addAttribute("orderList", currentUser.getOrderList());
		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "myProfile";
	}
 @RequestMapping(value = "/forgetPassword",method = RequestMethod.POST)
 public String forgetPassword(
			HttpServletRequest request,
			@ModelAttribute("email") String email,
			Model model
			) {
	 model.addAttribute("classActiveForgetPassword", true);
	 User user = userservice.findByEmail(email);		
		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}
String password = SecurityUtility.randomPassword();	
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);	
		userservice.save(user);	
		String token = UUID.randomUUID().toString();
		userservice.createPasswordResetTokenForUser(user, token);	
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);	
		mailSender.send(newEmail);	
		model.addAttribute("forgetPasswordEmailSent", "true");
		return "myAccount";
 }
 @RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(
			Model model, Principal principal, HttpServletRequest request
			) {
		User user = userservice.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}
 @RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(
			Model model, Principal principal
			){
		User user = userservice.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("orderList", user.getOrderList());
		Usershipping userShipping = new Usershipping();
		model.addAttribute("userShipping", userShipping);
		return "myProfile";
	}
 @RequestMapping(value = "/addNewShippingAddress",method = RequestMethod.POST)
 public String addNewShippingAddress(@ModelAttribute("userShipping") Usershipping usershipping,Principal principal,Model model) {
	 User user = userservice.findByUsername(principal.getName());
	 userservice.updateUserShipping(usershipping, user);
	 model.addAttribute("user", user);
	 model.addAttribute("userShippingList", user.getUserShippingList());
	 model.addAttribute("listOfShippingAddresses", true);
	 model.addAttribute("orderList", user.getOrderList());
	 model.addAttribute("classActiveShipping", true);
	 return "myProfile";
 }
 @RequestMapping(value="/updateUserShipping")
 public String updateUserShipping(@ModelAttribute("id")Long iden,Model model,Principal principal) {
	 User user = userservice.findByUsername(principal.getName());
		Usershipping userShipping = usershippinservice.findById(iden);
		if(user.getId()!=userShipping.getUser().getId()) {
			return "badRequestPage";
		}
		else {
            model.addAttribute("user", user);
			model.addAttribute("userShipping", userShipping);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("orderList", user.getOrderList());
			return "myProfile";
		}
 }
 @RequestMapping(value = "/setDefaultShippingAddress",method = RequestMethod.POST)
 public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId")Long ident,Principal principal,Model model) {
	 User user = userservice.findByUsername(principal.getName());
	 userservice.setUserDefaultShipping(ident, user);
	 model.addAttribute("user", user);
	 model.addAttribute("classActiveShipping", true);
	 model.addAttribute("listOfShippingAddresses", true);
	 model.addAttribute("userShippingList", user.getUserShippingList());
	 model.addAttribute("orderList", user.getOrderList());
	 return "myProfile";
 }
 @RequestMapping(value = "/removeUserShipping")
 public String removeUserShipping(@ModelAttribute("id")Long iden,Model model,Principal principal) {
	 User user = userservice.findByUsername(principal.getName());
		Usershipping userShipping = usershippinservice.findById(iden);
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			usershippinservice.removeById(iden);
			model.addAttribute("user", user);
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);	
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			return "myProfile";
		}
 }
 @RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(Model model, Principal principal) {
	    User user = userservice.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUsercrediscards());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("classActiveBilling",true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("orderList", user.getOrderList());
		return "myProfile";
 }
 @RequestMapping("/addNewCreditCard")
 public String addNewCreditCard(Model model,Principal principal) {
	 User user = userservice.findByUsername(principal.getName());
	 model.addAttribute("user", user);
	 model.addAttribute("classActiveBilling",true);
	 model.addAttribute("addNewCreditCard",true);
	 model.addAttribute("userPaymentList", user.getUsercrediscards());
	 model.addAttribute("userShippingList", user.getUserShippingList());
	 model.addAttribute("orderList", user.getOrderList());
	 Usercreditcard usercreditcard=new Usercreditcard();
	 System.out.println("a");
	 model.addAttribute("userPayment", usercreditcard);
	 return "myProfile";
	 
 }
 @RequestMapping(value="/addNewCreditCard", method=RequestMethod.POST)
 public String addNewCreditCard(@ModelAttribute("userPayment") Usercreditcard userPayment,Model model,Principal principal) {
	 User user = userservice.findByUsername(principal.getName());
	 userservice.updateCreditCard(userPayment, user);
	 model.addAttribute("user", user);
	 model.addAttribute("classActiveBilling",true);
	 model.addAttribute("listOfCreditCards",true);
	 model.addAttribute("userShippingList", user.getUserShippingList());
	 model.addAttribute("userPaymentList", user.getUsercrediscards());
	 model.addAttribute("orderList", user.getOrderList());
	 return "myProfile";
 }
@RequestMapping("/updateCreditCard")
public String updateCreditCard(@ModelAttribute("id") Long creditCardId, Principal principal, Model model) {
	User user = userservice.findByUsername(principal.getName());
	Usercreditcard usercreditcard=usercreditcardService.findById(creditCardId);
	if(user.getId() != usercreditcard.getUser().getId()) {
		return "badRequestPage";
	}
	else {
		model.addAttribute("user", user);
		model.addAttribute("userPayment", usercreditcard);
		 model.addAttribute("classActiveBilling",true);
		 model.addAttribute("addNewCreditCard",true);
		 model.addAttribute("userShippingList", user.getUserShippingList());
		 model.addAttribute("userPaymentList", user.getUsercrediscards());
		 model.addAttribute("orderList", user.getOrderList());
		 return "myProfile";
	}
}
@RequestMapping("/removeCreditCard")
public String removeCreditCard(@ModelAttribute("id") Long creditCardId, Principal principal, Model model) {
	User user = userservice.findByUsername(principal.getName());
	Usercreditcard usercreditcard=usercreditcardService.findById(creditCardId);
	if(user.getId() != usercreditcard.getUser().getId()) {
		return "badRequestPage";
	}
	else {
		model.addAttribute("user", user);
		usercreditcardService.removeById(creditCardId);
		 model.addAttribute("classActiveBilling",true);
		 model.addAttribute("listOfCreditCards",true);
		 model.addAttribute("userShippingList", user.getUserShippingList());
		 model.addAttribute("userPaymentList", user.getUsercrediscards());
		 model.addAttribute("orderList", user.getOrderList());
		 return "myProfile";
	}
}
@RequestMapping(value = "/setDefaultPayment",method=RequestMethod.POST)
public String setDefaultPayment(@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model) {
	User user = userservice.findByUsername(principal.getName());
	userservice.setUserDefaultPayment(defaultPaymentId, user);
	model.addAttribute("user", user);
	model.addAttribute("classActiveBilling",true);
	model.addAttribute("listOfCreditCards",true);
	 model.addAttribute("userShippingList", user.getUserShippingList());
	 model.addAttribute("userPaymentList", user.getUsercrediscards());
	 model.addAttribute("orderList", user.getOrderList());
	 return "myProfile";
}
@RequestMapping("/orderDetail")
public String orderDetail(@ModelAttribute("id")Long orderid,Principal principal, Model model) {
	User user = userservice.findByUsername(principal.getName());
	Order order = orderService.findOne(orderid);
	if(order.getUser().getId()!=user.getId()) {
		return "badRequestPage";
	} else {
		List<Cartitem> cartItemList = cartItemService.findByOrder(order);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("userPaymentList", user.getUsercrediscards());
		model.addAttribute("orderList", user.getOrderList());
		model.addAttribute("displayOrderDetail", true);
		model.addAttribute("classActiveOrders", true);
		 return "myProfile";
	}
}
@RequestMapping(value = "/getphoto",produces = MediaType.IMAGE_JPEG_VALUE)
@ResponseBody
public byte[] getphoto(Long id) throws Exception {
	File f=new File("D:/TC/bookstore/"+id);
	return IOUtils.toByteArray(new FileInputStream(f));	
}
@RequestMapping("/listOfBooksBought")
public String listOfBooksBought(Model model,Principal principal) {
	User user=userservice.findByUsername(principal.getName());
	List<Book> books=userservice.listOfBooksBought(user);
	model.addAttribute("listOfBooksBought",true);
	model.addAttribute("classActiveBooks",true);
	model.addAttribute("user",user);
	model.addAttribute("orderList", user.getOrderList());
	model.addAttribute("books",books);
	List<Integer> qtyList = Arrays.asList(1,2,3,4,5);
	model.addAttribute("qtyList",qtyList);
	return "myProfile";
}
@RequestMapping(value = "/sendfeedback",method = RequestMethod.POST)
public String sendfeedback(@ModelAttribute("qty")int qt,Principal principal,Model model,@ModelAttribute("feedback")String feedback,@ModelAttribute("ident")Long idbook){
	User user=userservice.findByUsername(principal.getName());
	Book book=bookService.findOne(idbook);
	List<Book> books=userservice.listOfBooksBought(user);
	Boolean a=false;
	Boolean b=false;
	if(qt!=0) {
		noteService.createnote(user, book, qt);
		a=true;
	}
	if(!feedback.isEmpty() && feedback!=null || !feedback.equals("")) {
		messageService.createmessage(user, book, feedback);
		b=true;
	}
	if(a || b) {
		model.addAttribute("sent",true);
	}
	model.addAttribute("classActiveBooks",true);
	model.addAttribute("listOfBooksBought", true);
	model.addAttribute("user",user);
	model.addAttribute("orderList", user.getOrderList());
	model.addAttribute("books",books);
	List<Integer> qtyList = Arrays.asList(1,2,3,4,5);
	model.addAttribute("qtyList",qtyList);
	return "myProfile";
}
}
