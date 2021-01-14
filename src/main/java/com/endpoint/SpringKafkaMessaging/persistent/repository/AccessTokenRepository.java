package com.endpoint.SpringKafkaMessaging.persistent.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.endpoint.SpringKafkaMessaging.persistent.model.AccessToken;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, Long> {

	AccessToken findByUserId(Long userId);

	void deleteByUserId(Long userId);

}
