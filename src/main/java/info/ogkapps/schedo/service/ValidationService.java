package info.ogkapps.schedo.service;

import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import info.ogkapps.schedo.dto.RegisterOwnerDTO;
import jakarta.servlet.http.HttpSession;

@Service
public class ValidationService {

	private JavaMailSender mailSender;
	
	public void sendSimpleEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("onkarkulak@gmail.com");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}
	
	public boolean isValidInput(RegisterOwnerDTO registerOwnerDTO) {
		
	String on =	registerOwnerDTO.ownerName();
	String oe =	registerOwnerDTO.ownerEmail();
	String oc =	registerOwnerDTO.ownerCatagory();
	 	
		return !on.equals("") && oe.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") && !oc.equals("");
	}
	
	public boolean isOwnerRegistrationOnGoing(RegisterOwnerDTO registerOwnerDTO, HttpSession session) {
		if (session.getAttribute(registerOwnerDTO.ownerEmail()) == null)
			return false;
		return true;
	}
	
	public void initiateRegistration(RegisterOwnerDTO registerOwnerDTO, HttpSession session) {
		String otp = String.valueOf(new Random().nextInt(1000, 10000));
		session.setAttribute(registerOwnerDTO.ownerEmail(), otp);
		session.setMaxInactiveInterval(60);
		sendSimpleEmail(registerOwnerDTO.ownerEmail(), "Schedo - Validate Email Id", "OTP: " + otp + " (Expires in 1 minute)");
	}
	
	public boolean isOtpValid(RegisterOwnerDTO registerOwnerDTO, HttpSession session) {

		return session.getAttribute(registerOwnerDTO.ownerEmail()).equals(registerOwnerDTO.crossCode());
	}
}
