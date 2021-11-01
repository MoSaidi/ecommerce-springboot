package com.bookstore.service.impl;

import org.springframework.stereotype.Service;

import com.bookstore.domain.CreditCard;
import com.bookstore.domain.Usercreditcard;
import com.bookstore.service.CreditCardService;

@Service
public class CreditCardServiceImpl implements CreditCardService{
public CreditCard setByUserPayment(CreditCard creditcard,Usercreditcard usercreditcard) {
	creditcard.setCardName(usercreditcard.getCardName());
	creditcard.setCardNumber(usercreditcard.getCardNumber());
	creditcard.setType(usercreditcard.getType());
	return creditcard;
}
}
