package com.api.person.domain.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.person.domain.model.Person;
import com.api.person.domain.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	public Person findById(Long id) {
		return personRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("No Person with ID %d was found.", id)));
	}

	public List<Person> findByName(String name) {
		return personRepository.findByNameLike("%" + name + "%");
	}

	public Person update(Long id, Person personUpdated) {
		Person person = personRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("No Person with ID %d was found.", id)));

		BeanUtils.copyProperties(personUpdated, person, "id");

		return personRepository.save(person);
	}
}
