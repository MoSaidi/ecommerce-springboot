package com.bookstore.service;

import com.bookstore.domain.CreditCard;
import com.bookstore.domain.Usercreditcard;

public interface CreditCardService {
	CreditCard setByUserPayment(CreditCard creditcard,Usercreditcard usercreditcard);
}
