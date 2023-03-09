package com.api.person.api.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.person.domain.model.Person;
import com.api.person.domain.repository.PersonRepository;
import com.api.person.domain.service.PersonService;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PersonService personService;

	@GetMapping
	private List<Person> findAll(@RequestParam(required = false) String name) {
		if (name != null) {
			return personService.findByName(name);
		}

		return personRepository.findAll();
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable Long id) {
		try {
			Person person = personService.findById(id);

			return ResponseEntity.ok(person);

		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	private Person create(@RequestBody Person person) {
		return personRepository.save(person);
	}

	@PutMapping("/{id}")
	private ResponseEntity<?> update(@PathVariable Long id, @RequestBody Person person) {
		try {
			Person personUpdated = personService.update(id, person);
			return ResponseEntity.ok(personUpdated);

		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
