package info.ogkapps.schedo.service;

import org.springframework.stereotype.Service;

import info.ogkapps.schedo.dto.RegisterOwnerDTO;
import jakarta.servlet.http.HttpSession;

@Service
public class ValidationService {

	public boolean isOwnerRegistrationOnGoing(RegisterOwnerDTO registerOwnerDTO, HttpSession session) {
		
		return false;
	}
	
	public boolean initiateRegistration(RegisterOwnerDTO registerOwnerDTO, HttpSession session) {
		
		return false;
	}
	
	public boolean isOtpValid(RegisterOwnerDTO registerOwnerDTO, HttpSession session) {
		
		return false;
	}
}
