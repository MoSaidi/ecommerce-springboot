package com.bookstore.service;

import javax.transaction.Transactional;

import com.bookstore.domain.Usercreditcard;

@Transactional
public interface UsercreditcardService {
	Usercreditcard findById(Long id);
	void removeById(Long id);
}
