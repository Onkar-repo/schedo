package info.ogkapps.schedo.service;

import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import info.ogkapps.schedo.dto.CheckAvailabilityDTO;
import info.ogkapps.schedo.dto.RegisterOwnerDTO;
import info.ogkapps.schedo.dto.ScheduleMeetingDTO;
import jakarta.servlet.http.HttpSession;

@Service
public class ValidationService {

	private JavaMailSender mailSender;
	

	public ValidationService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}

	public void sendSimpleEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("onkarkulak@gmail.com");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	public boolean isValidInput(RegisterOwnerDTO registerOwnerDTO) {

		String on = registerOwnerDTO.ownerName();
		String oe = registerOwnerDTO.ownerEmail();
		String oc = registerOwnerDTO.ownerCatagory();

		return !on.equals("") && !oe.equals("") && !oc.equals("") && oe.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	}

	public boolean isValidInput(CheckAvailabilityDTO checkAvailabilityDTO) {
		String oe = checkAvailabilityDTO.ownerEmail();
		Long td = checkAvailabilityDTO.targetDate();
		long ct = System.currentTimeMillis();

		return !oe.equals("") && oe.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") && td > ct;

	}

	public boolean isValidInput(ScheduleMeetingDTO scheduleMeetingDTO) {
		String oe = scheduleMeetingDTO.ownerEmail();
		String lvn = scheduleMeetingDTO.logVisitorName();
		String lve = scheduleMeetingDTO.logVisitorEmail();
		Long lvt = scheduleMeetingDTO.logVisitorTime();
		long ct = System.currentTimeMillis();

		System.out.println(oe);
		System.out.println(lvn);
		System.out.println(lve);
		System.out.println(lvt);
		
		return !oe.equals("") && !lvn.equals("") && !lve.equals("") && oe.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
				&& lve.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") && lvt > ct;
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
		sendSimpleEmail(registerOwnerDTO.ownerEmail(), "Schedo - Validate Email Id",
				"OTP: " + otp + " (Expires in 1 minute)");
	}

	public boolean isOtpValid(RegisterOwnerDTO registerOwnerDTO, HttpSession session) {

		Object obj = session.getAttribute(registerOwnerDTO.ownerEmail());
		if(obj!=null)
		return obj.equals(registerOwnerDTO.crossCode());
		else 
			return false;
	}
}
