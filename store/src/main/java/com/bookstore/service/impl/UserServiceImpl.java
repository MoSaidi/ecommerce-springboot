package com.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.domain.Book;
import com.bookstore.domain.Cartitem;
import com.bookstore.domain.Order;
import com.bookstore.domain.Shoppingcard;
import com.bookstore.domain.User;
import com.bookstore.domain.Usercreditcard;
import com.bookstore.domain.Usershipping;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.ShoppingCardRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.repository.UsercreditcardRepository;
import com.bookstore.repository.UsershippingRepository;
import com.bookstore.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UsershippingRepository usershippingrep;
	@Autowired
	private ShoppingCardRepository shrep;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	private UsercreditcardRepository usercreditcardRepository ;
	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}
	
	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public User findById(Long id){
		return userRepository.findOne(id);
	}
	
	@Override
	public User findByEmail (String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles){
		User localUser = userRepository.findByUsername(user.getUsername());
		if(localUser != null) {
			LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
		} else {
			for (UserRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			Shoppingcard shoppingcard=new Shoppingcard();
			user.setShoppingCart(shoppingcard);
			shoppingcard.setUser(user);
			shrep.save(shoppingcard);
			localUser = userRepository.save(user);
		}
		
		return localUser;
	}
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	@Override
	public void updateUserShipping(Usershipping userShipping, User user){
		userShipping.setUser(user);
		userShipping.setDefaul(true);
		for(Usershipping u:user.getUserShippingList()) {
			u.setDefaul(false);
			usershippingrep.save(u);
		}
		user.getUserShippingList().add(userShipping);
		save(user);
	}
	@Override
	public void setUserDefaultShipping(Long userShippingId, User user) {
		List<Usershipping> userShippingList = (List<Usershipping>) user.getUserShippingList();
		
		for (Usershipping userShipping : userShippingList) {
			if(userShipping.getId() == userShippingId) {
				userShipping.setDefaul(true);
				usershippingrep.save(userShipping);
			} else {
				userShipping.setDefaul(false);
				usershippingrep.save(userShipping);
			}
		}
		
	}
	@Override
	public void updateCreditCard(Usercreditcard userpayment, User user) {
        userpayment.setUser(user);
        userpayment.setDefaul(true);
        for(Usercreditcard u:user.getUsercrediscards()) {
        	u.setDefaul(false);
        	usercreditcardRepository.save(u);
        }
        user.getUsercrediscards().add(userpayment);
		save(user);
	}
	@Override
	public void setUserDefaultPayment(Long userPaymentId, User user) {
        List<Usercreditcard> usercreditcards = (List<Usercreditcard>) user.getUsercrediscards();
		
		for (Usercreditcard c:usercreditcards) {
			if(c.getId()==userPaymentId) {
				c.setDefaul(true);
				usercreditcardRepository.save(c);
			} else {
				c.setDefaul(false);
				usercreditcardRepository.save(c);
			}
		}
	}
	@Override
	public List<Book> listOfBooksBought(User user){
		List<Order> orders=(List<Order>) user.getOrderList();
		List<Book> inte=new ArrayList<Book>();
		for(Order o:orders) {
			for(Cartitem c:o.getCartItemList()) {
				if(!(inte.contains(c.getBook()))) {
					inte.add(c.getBook());
				}
			}
		}
		return inte;
	}
}
