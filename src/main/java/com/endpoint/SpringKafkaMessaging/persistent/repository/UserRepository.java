package com.endpoint.SpringKafkaMessaging.persistent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.endpoint.SpringKafkaMessaging.persistent.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findAll();

	User findByUserId(Long userId);
	
	User findByMobile(String mobile);
	
	User findByFname(String fname);
	
	User findByLname(String lname);

	@Query("SELECT u FROM User u, AccessToken t WHERE u.userId=t.userId AND t.token = :#{#token}")
	User findByToken(@Param("token") String token);

	void deleteById(Long userId);

}
