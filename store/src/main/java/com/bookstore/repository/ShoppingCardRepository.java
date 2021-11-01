package com.bookstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.Shoppingcard;

public interface ShoppingCardRepository extends CrudRepository<Shoppingcard, Long> {

}
