package com.api.person.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.person.domain.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	List<Person> findByNameLike(String Name);

}
