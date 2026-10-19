package info.ogkapps.schedo.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import info.ogkapps.schedo.dto.HandlerResponseDTO;

@RestControllerAdvice
public class GlobalHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<HandlerResponseDTO> handle(RuntimeException exception){
		HandlerResponseDTO hrDto = new HandlerResponseDTO(exception.getMessage());
		return new ResponseEntity<>(hrDto, HttpStatus.UNPROCESSABLE_CONTENT);
	}
}
