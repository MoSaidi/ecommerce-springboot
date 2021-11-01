package com.bookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Usershipping;
import com.bookstore.repository.UsershippingRepository;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
@Service
public class UserShippingServiceImpl implements UserShippingService{
@Autowired
private UsershippingRepository UsershippingRepository;
public Usershipping findById(Long id) {
	return UsershippingRepository.findOne(id);
}

public void removeById(Long id) {
	UsershippingRepository.delete(id);
}
}
