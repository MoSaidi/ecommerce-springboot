package com.bookstore.service;

import com.bookstore.domain.Usershipping;

public interface UserShippingService {
    Usershipping findById(Long id);
	
	void removeById(Long id);
}
