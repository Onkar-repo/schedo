package info.ogkapps.schedo.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import info.ogkapps.schedo.dto.CheckAvailabilityDTO;
import info.ogkapps.schedo.dto.RegisterOwnerDTO;
import info.ogkapps.schedo.dto.ScheduleMeetingDTO;
import info.ogkapps.schedo.repository.OwnersRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class ValidationService {

	private OwnersRepository ownersRepository;
	private EmailService emailService;
	
	@Value("${thisapp.sessiontimeout:60}")
	private int sessionTimeoutSeconds;

	public ValidationService(OwnersRepository ownersRepository, EmailService emailService) {
		super();
		this.ownersRepository = ownersRepository;
		this.emailService = emailService;
	}
	
	public boolean ownerExists(String ownerEmail) {
		if (ownerEmail == null ) 
		throw new RuntimeException("ownerEmail can not be null.");
		
		return ownersRepository.existsByOwnerEmail(ownerEmail);
	}

	public boolean isValidInput(RegisterOwnerDTO registerOwnerDTO) {

		String on = registerOwnerDTO.ownerName();
		String oe = registerOwnerDTO.ownerEmail();
		String oc = registerOwnerDTO.ownerCatagory();
		if (on == null || oe == null || oc == null) 
			throw new RuntimeException("Any of these - [ownerName, ownerEmail, ownerCatagory, crossCode] can not be null.");
		return !on.equals("") && !oe.equals("") && !oc.equals("") && oe.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	}

	
	private long startMomentOfTheDay(long millis) {
		LocalDateTime temp = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
		LocalDateTime customDateTime = LocalDateTime.of(temp.getYear(), temp.getMonthValue(), temp.getDayOfMonth(), 0,
				0, 0);
		long newMillis = customDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		return newMillis;
	}
	
	
	public boolean isValidInput(CheckAvailabilityDTO checkAvailabilityDTO) {
		String oe = checkAvailabilityDTO.ownerEmail();
		Long td = checkAvailabilityDTO.targetDate();
		
		if (oe == null || td == null) 
			throw new RuntimeException("Any of these - [ownerEmail, targetDate] can not be null.");

		
		long ct = startMomentOfTheDay(System.currentTimeMillis());
		System.out.println(oe + ":" + td + ":");
System.out.println(td + " td\n" + ct + " ct\n" + (td>ct?1:0));
		return !oe.equals("") && oe.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") && td >= ct;

	}

	public boolean isValidInput(ScheduleMeetingDTO scheduleMeetingDTO) {
		String oe = scheduleMeetingDTO.ownerEmail();
		String lvn = scheduleMeetingDTO.logVisitorName();
		String lve = scheduleMeetingDTO.logVisitorEmail();
		Long lvt = scheduleMeetingDTO.logVisitorTime();
		long ct = System.currentTimeMillis();
		if (lvn == null || oe == null || lve == null || lvt ==null) 
			throw new RuntimeException("Any of these - [ownerEmail, logVisitorName, logVisitorEmail, logVisitorTime] can not be null.");

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
		session.setMaxInactiveInterval(sessionTimeoutSeconds);
		emailService.sendSimpleEmail(registerOwnerDTO.ownerEmail(), "Schedo",
				"OTP: " + otp + " (Expires in "+ sessionTimeoutSeconds + "seconds)");
	}

	public boolean isOtpValid(RegisterOwnerDTO registerOwnerDTO, HttpSession session) {

		Object obj = session.getAttribute(registerOwnerDTO.ownerEmail());
		if(obj!=null)
		return obj.equals(registerOwnerDTO.crossCode());
		else 
			return false;
	}
}
