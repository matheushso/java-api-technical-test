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

import com.api.person.domain.model.Person;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonAPITests {
	
	@LocalServerPort
	private Integer port;
	
	SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
	
	private String name = "Person 1";
	private Date dateBirth = creteDate("01/01/2000");
	
	Person person = Person.builder()
			.name(name)
			.dateBirth(dateBirth)
			.build();
	
	@Test
	public void shouldSucceedWhenConsultPersons() {
		createPerson();
		
		given()
			.basePath("/api/v1/persons")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("[0].name", is(name))
			.body("[0].dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldSucceedWhenConsultPersonsPerName() {
		given()
			.basePath("/api/v1/persons")
			.port(port)
			.param("name", "Per")
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("[0].name", is(name))
			.body("[0].dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldSucceedWhenConsultPersonPerId() {
		createPerson();
		
		given()
			.pathParam("id", 1)
			.basePath("/api/v1/persons/{id}")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", is(name))
			.body("dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldFailedWhenConsultPersonWithIdNotExist() {
		given()
			.pathParam("id", 9)
			.basePath("/api/v1/persons/{id}")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void shouldSucceedWhenCreatedPerson() {
		ValidatableResponse personCreated = createPerson();
		
		personCreated
			.body("name", is(name))
			.body("dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldSucceedWhenUpdatedPerson() {
		given()
			.pathParam("id", 1)
			.basePath("/api/v1/persons/{id}")
			.port(port)
			.body(person)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", is(name))
			.body("dateBirth", is(formatDate.format(dateBirth)));
	}
	
	@Test
	public void shouldFailedWhenUpdatedPersonWithIdNotExist() {
		given()
			.pathParam("id", 9)
			.basePath("/api/v1/persons/{id}")
			.port(port)
			.body(person)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put()
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	private ValidatableResponse createPerson() {
		return given()
			.basePath("/api/v1/persons")
			.port(port)
			.body(person)
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
