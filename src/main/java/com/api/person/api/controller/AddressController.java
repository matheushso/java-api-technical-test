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
import org.springframework.web.bind.annotation.RestController;

import com.api.person.domain.model.Address;
import com.api.person.domain.repository.AddressRepository;
import com.api.person.domain.service.AddressService;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AddressService addressService;

	@GetMapping
	private List<Address> findAll() {
		return addressRepository.findAll();
	}

	@GetMapping("/{personId}")
	private ResponseEntity<?> findByPersonId(@PathVariable Long personId) {
		try {
			List<Address> address = addressService.findByPersonId(personId);

			return ResponseEntity.ok(address);

		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping
	private ResponseEntity<?> create(@RequestBody Address address) {
		try {
			Address addressCreated = addressService.save(address);

			return ResponseEntity.status(HttpStatus.CREATED).body(addressCreated);

		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	private ResponseEntity<?> updatePrincipalAddress(@PathVariable Long id,
			@RequestParam(required = false) boolean isPrincipalAddress) {
		try {
			Address address = addressService.update(id, isPrincipalAddress);

			return ResponseEntity.ok(address);

		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
