package it.discovery.exceptions;



import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {
	

}
