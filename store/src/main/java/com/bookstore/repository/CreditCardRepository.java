package com.bookstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.CreditCard;

public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {

}
