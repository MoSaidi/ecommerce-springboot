package com.bookstore.service;

import java.util.List;
import java.util.Set;

import com.bookstore.domain.Book;
import com.bookstore.domain.User;
import com.bookstore.domain.Usercreditcard;
import com.bookstore.domain.Usershipping;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail (String email);
	
	User findById(Long id);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);
	
	void updateUserShipping(Usershipping userShipping, User user);
	
	void setUserDefaultShipping(Long userShippingId, User user);
	
	void updateCreditCard(Usercreditcard userpayment, User user);
	
	void setUserDefaultPayment(Long userPaymentId, User user);
	
	List<Book> listOfBooksBought(User user);
}
