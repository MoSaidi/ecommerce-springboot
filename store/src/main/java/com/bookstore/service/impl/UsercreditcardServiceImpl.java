package com.bookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Usercreditcard;
import com.bookstore.repository.UsercreditcardRepository;
import com.bookstore.service.UsercreditcardService;

@Service
public class UsercreditcardServiceImpl implements UsercreditcardService{
@Autowired
private UsercreditcardRepository  usercreditcardRepository;
public Usercreditcard findById(Long id) {
	return usercreditcardRepository.findOne(id);
}
public void removeById(Long id) {
	usercreditcardRepository.delete(id);
}
}
