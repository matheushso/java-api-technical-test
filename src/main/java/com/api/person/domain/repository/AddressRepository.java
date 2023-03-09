package com.api.person.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.person.domain.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findAllByPersonId(Long personId);
}
