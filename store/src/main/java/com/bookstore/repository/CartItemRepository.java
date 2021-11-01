package com.bookstore.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.Cartitem;
import com.bookstore.domain.Order;
import com.bookstore.domain.Shoppingcard;
@Transactional
public interface CartItemRepository extends CrudRepository<Cartitem, Long> {
    List<Cartitem> findByShoppingCart(Shoppingcard shoppingcart);
	
	List<Cartitem> findByOrder(Order order);
}
