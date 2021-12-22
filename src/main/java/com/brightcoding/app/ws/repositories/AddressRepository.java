package com.brightcoding.app.ws.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightcoding.app.ws.entities.AddressEntity;
import com.brightcoding.app.ws.entities.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long>{

	List<AddressEntity> findByUser(UserEntity currentUser);
	
	//
	//@Query(value = "DELETE FROM addresses WHERE address_id = :addressId", nativeQuery=true)
	//@Query(value = "SELECT * FROM addresses WHERE address_id = :addressId", nativeQuery=true)@Param("addressId")
	AddressEntity findByAddressId(String addressId);
	
	@Modifying
	@Query(value = "DELETE FROM addresses WHERE address_id = :addressId", nativeQuery=true)
	AddressEntity deleteAddres(@Param("addressId") String addressId);
}
