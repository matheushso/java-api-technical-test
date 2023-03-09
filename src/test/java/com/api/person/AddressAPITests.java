package com.api.person;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.api.person.domain.model.Address;
import com.api.person.domain.model.Person;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressAPITests {
	
	@LocalServerPort
	private Integer port;
	
	SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
	
	private String name = "Person 1";
	private Date dateBirth = creteDate("01/01/2000");
	private String addressName = "Street Address";
	private String zipCode = "11111111";
	private int number = 123;
	private String city = "Address City";
	private boolean isPrincipalAddress = false;
	
	private Address address = Address.builder()
			.address(addressName)
			.zipCode(zipCode)
			.number(number)
			.city(city)
			.isPrincipalAddress(isPrincipalAddress)
			.person(Person.builder().id(1L).build())
			.build();
	
	@Test
	public void shouldSucceedWhenConsultAddresses() {
		createAddress();
		
		given()
			.basePath("/api/v1/addresses")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("[0].address", is(addressName))
			.body("[0].zipCode", is(zipCode))
			.body("[0].number", is(number))
			.body("[0].city", is(city))
			.body("[0].principalAddress", is(isPrincipalAddress))
			.body("[0].person.name", is(name))
			.body("[0].person.dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldSucceedWhenConsultAddressPerPersonId() {
		given()
			.pathParam("id", 1)
			.basePath("/api/v1/addresses/{id}")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("[0].address", is(addressName))
			.body("[0].zipCode", is(zipCode))
			.body("[0].number", is(number))
			.body("[0].city", is(city))
			.body("[0].principalAddress", is(isPrincipalAddress))
			.body("[0].person.name", is(name))
			.body("[0].person.dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldSucceedWhenConsultAddressWithPersonIdNotExist() {
		given()
			.pathParam("id", 9)
			.basePath("/api/v1/addresses/{id}")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void shouldSucceedWhenCreatedAddress() {
		ValidatableResponse addressCreated = createAddress();
		
		addressCreated
			.body("address", is(addressName))
			.body("zipCode", is(zipCode))
			.body("number", is(number))
			.body("city", is(city))
			.body("principalAddress", is(isPrincipalAddress))
			.body("person.name", is(name))
			.body("person.dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldSucceedWhenUpdatedAddress() {
		createAddress();
		
		given()
			.pathParam("id", 1)
			.basePath("/api/v1/addresses/{id}")
			.port(port)
			.body(address)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("address", is(addressName))
			.body("zipCode", is(zipCode))
			.body("number", is(number))
			.body("city", is(city))
			.body("principalAddress", is(isPrincipalAddress))
			.body("person.name", is(name))
			.body("person.dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldFailedWhenUpdatedAddressWithIdNotExist() {
		given()
			.pathParam("id", 9)
			.basePath("/api/v1/address/{id}")
			.port(port)
			.body(address)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put()
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	private ValidatableResponse createAddress() {
		Person person = Person.builder()
				.name(name)
				.dateBirth(dateBirth)
				.build();
		
		given()
			.basePath("/api/v1/persons")
			.port(port)
			.body(person)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
		
		return given()
			.basePath("/api/v1/addresses")
			.port(port)
			.body(address)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	private Date creteDate(String date) {
		try {
			return formatDate.parse(date);
		} catch (ParseException e) {
			System.out.println("Date in invalid format");
		}
		
		return null;
	}
}
