package com.bookstore.service.impl;

import org.springframework.stereotype.Service;

import com.bookstore.domain.Shipping_adresse;
import com.bookstore.domain.Usershipping;
import com.bookstore.service.ShippingAdresseService;

@Service
public class ShippingAdresseServiceImpl implements ShippingAdresseService{
	public Shipping_adresse setByUserShipping(Usershipping userShipping, Shipping_adresse shippingAddress) {
		shippingAddress.setShippingAddressCity(userShipping.getShippingAddressCity());
		shippingAddress.setShippingAddressCountry(userShipping.getShippingAddressCountry());
		shippingAddress.setShippingAddressState(userShipping.getShippingAddressState());
		shippingAddress.setShippingAddressStreet(userShipping.getShippingAddressStreet());
		shippingAddress.setShippingAddressZipcode(userShipping.getShippingAddressZipcode());
		shippingAddress.setShippingAddressName(userShipping.getShippingAddressName());
		return shippingAddress;
		
	}
}
