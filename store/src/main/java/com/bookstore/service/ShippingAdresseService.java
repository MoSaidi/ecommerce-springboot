package com.bookstore.service;

import com.bookstore.domain.Shipping_adresse;
import com.bookstore.domain.Usercreditcard;
import com.bookstore.domain.Usershipping;

public interface ShippingAdresseService {
	Shipping_adresse setByUserShipping(Usershipping userShipping, Shipping_adresse shippingAddress);
}
