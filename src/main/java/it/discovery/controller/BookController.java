package it.discovery.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.discovery.exceptions.BookNotFoundException;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;

@RestController
@RequestMapping("/book")
public class BookController {
	@Autowired
	private BookRepository bookRepository;
	

	@RequestMapping(path="/{id}", method=RequestMethod.GET)
	@Secured("ADMIN")
	public ResponseEntity<Book> getBook(@PathVariable("id") String id) {
		// test in Postman : /id/100
		Book book = bookRepository.findById(Integer.parseInt(id));
		if(Integer.parseInt(id) <= 0) {
			return new ResponseEntity<>(org.springframework.http.HttpStatus.BAD_REQUEST); // 1
		}
		if(book == null) {
			throw new BookNotFoundException(); // 2
		} else {
			return new ResponseEntity<Book>(book, org.springframework.http.HttpStatus.OK); // 3
		}
	}
	
	/*@RequestMapping(method=RequestMethod.GET)
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}*/
	
	@RequestMapping
	public List<Resource<Book>> getBooks() {
		List<Book> books = bookRepository.findAll();
		
		List<Resource<Book>> resources = new ArrayList<>();
		for(Book book: books) {
			Resource<Book> resource = new Resource<Book> (book);
			resource.add(linkTo(methodOn(BookController.class).
					getBook(String.valueOf(book.getId()))).withSelfRel());
			
			resources.add(resource);
			
		}
		
		return resources;
	}

	
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Integer createBook(@RequestBody Book book) {
		bookRepository.save(book);
		// better return book id
		return book.getId();
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.PUT)
	public void updateBook(@PathVariable("id") String id, 
			@RequestBody Book book) {
		//book.setId(Integer.parseInt(id));
		bookRepository.save(book);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public void deleteBook(@PathVariable("id") String id) {
		bookRepository.delete(Integer.parseInt(id));
	}
	
}
