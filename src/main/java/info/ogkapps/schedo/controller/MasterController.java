package info.ogkapps.schedo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import info.ogkapps.schedo.dto.CheckAvailabilityDTO;
import info.ogkapps.schedo.dto.LoadOwnersDTO;
import info.ogkapps.schedo.dto.RegisterOwnerDTO;
import info.ogkapps.schedo.dto.ScheduleMeetingDTO;
import info.ogkapps.schedo.dto.ScheduledTimeListDTO;
import info.ogkapps.schedo.service.LogsService;
import info.ogkapps.schedo.service.OwnerService;
import info.ogkapps.schedo.service.ValidationService;
import jakarta.servlet.http.HttpSession;

@RestController
public class MasterController {

	private final ValidationService validationService;
	private final OwnerService ownerService;
	private final LogsService logsService;

	@Value("${thisapp.sessiontimeout:60}")
	private int sessionTimeoutSeconds;

	public MasterController(ValidationService validationService, OwnerService ownerService, LogsService logsService) {
		super();
		this.validationService = validationService;
		this.ownerService = ownerService;
		this.logsService = logsService;
	}

	@PostMapping("/register")
	public String registerOwnerRequest(@RequestBody RegisterOwnerDTO registerOwnerDTO, HttpSession session) {

		if (!validationService.isValidInput(registerOwnerDTO))
			return "Invalid input data format.";

		if (validationService.isOwnerRegistrationOnGoing(registerOwnerDTO, session))
			return registerOwnerDTO.ownerEmail() + " is being used by someone else, can not proceed.";

		if (validationService.ownerExists(registerOwnerDTO.ownerEmail()))
			return "Owner exists for given email " + registerOwnerDTO.ownerEmail();

		validationService.initiateRegistration(registerOwnerDTO, session);

		return "OTP sent on the given email.";
	}

	@PostMapping("/confirm")
	public String otpReceived(@RequestBody RegisterOwnerDTO registerOwnerDTO, HttpSession session) {

		if (!validationService.isValidInput(registerOwnerDTO))
			return "Invalid input data format.";

		if (validationService.isOtpValid(registerOwnerDTO, session)) {
			session.invalidate();
			return ownerService.registerOwner(registerOwnerDTO);
		}

		return "Invalid or expired otp.";
	}

	@PostMapping("/check")
	public List<ScheduledTimeListDTO> getRunningList(@RequestBody CheckAvailabilityDTO checkAvailabilityDTO) {

		if (!validationService.isValidInput(checkAvailabilityDTO)) {
			List<ScheduledTimeListDTO> temp = new ArrayList<ScheduledTimeListDTO>(1);
			temp.add(new ScheduledTimeListDTO(0, 0, checkAvailabilityDTO.targetDate(),
					checkAvailabilityDTO.ownerEmail(), "Invalid input data format.", ""));
			return temp;
		}
		return logsService.scheduledTimeList(checkAvailabilityDTO);
	}

	@GetMapping("/load")
	public List<LoadOwnersDTO> getOwners() {

		return ownerService.returnAllOwners();
	}

	@PostMapping("/mark")
	public String markMeeting(@RequestBody ScheduleMeetingDTO scheduleMeetingDTO) {

		if (!validationService.isValidInput(scheduleMeetingDTO)) {
			return "Invalid input data format.";
		}
		return logsService.markMeetingIfValid(scheduleMeetingDTO);
	}

	@GetMapping("/view")
	public String otpForLogsByOwner(@RequestParam("ownerEmail") String ownerEmail, HttpSession session) {
		if (ownerEmail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
			String otp = String.valueOf(new Random().nextInt(1000, 10000));
			String msg = logsService.sendOtp(ownerEmail, otp);
			if (msg.equals("sent")) {
				session.setAttribute(ownerEmail, otp);
				session.setMaxInactiveInterval(sessionTimeoutSeconds);
				return "Sent.";
			}
			if (msg.equals("ona")) {
				return "Owner email does not exists.";
			}
		}
		return "Invalid owner email id format.";
	}

	@GetMapping("/logs")
	public List<ScheduledTimeListDTO> logsByOwner(@RequestParam("ownerEmail") String ownerEmail, @RequestParam("crossCode") String otp,
			HttpSession session) {

		if (ownerEmail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
			if (session.getAttribute(ownerEmail).equals(otp)) {
				session.invalidate();
				return logsService.returnAllLogs(ownerEmail);
			}
			List<ScheduledTimeListDTO> temp = new ArrayList<ScheduledTimeListDTO>(1);
			temp.add(new ScheduledTimeListDTO(0, 0, 0l, ownerEmail, "Mismatched or expired OTP.", ""));
			return temp;

		} else {
			List<ScheduledTimeListDTO> temp = new ArrayList<ScheduledTimeListDTO>(1);
			temp.add(new ScheduledTimeListDTO(0, 0, 0l, ownerEmail, "Invalid input data format.", ""));
			return temp;
		}
	}

	@GetMapping("/reports")
	public List<Map<String, String>> getAllReports(@RequestParam String ownerEmail) {

		return null;
	}
}