package com.adminportal.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.adminportal.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	@Query(value = "select * from user  where username = :a and id IN (SELECT user_id from user_role where role_id = 0)", nativeQuery = true)
	User findByUsername(@Param("a")String username);
}
