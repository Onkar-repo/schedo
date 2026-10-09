package info.ogkapps.schedo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import info.ogkapps.schedo.dto.CheckAvailabilityDTO;
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

		validationService.initiateRegistration(registerOwnerDTO, session);

		return "OTP sent on the given email.";
	}

	@PostMapping("/confirm")
	public String otpReceived(@RequestBody RegisterOwnerDTO registerOwnerDTO, HttpSession session) {

		if (!validationService.isValidInput(registerOwnerDTO))
			return "Invalid input data format.";

		if (validationService.isOtpValid(registerOwnerDTO, session)) {
			return ownerService.registerOwner(registerOwnerDTO);
		}

		return "Invalid or expired otp.";
	}

	@PostMapping("/check")
	public List<ScheduledTimeListDTO> getRunningList(@RequestBody CheckAvailabilityDTO checkAvailabilityDTO) {

		if (!validationService.isValidInput(checkAvailabilityDTO)) {
			List<ScheduledTimeListDTO> temp = new ArrayList<ScheduledTimeListDTO>(1);
			temp.add(new ScheduledTimeListDTO(0, 0, 0l, "", "Invalid input data format."));
			return temp;
		}
		return logsService.scheduledTimeList(checkAvailabilityDTO);
	}

	@PostMapping("/mark")
	public String markMeeting(@RequestBody ScheduleMeetingDTO scheduleMeetingDTO) {

		if (!validationService.isValidInput(scheduleMeetingDTO)) {
			return "Invalid input data format.";
		}
		return logsService.markMeetingIfValid(scheduleMeetingDTO);
	}

	@GetMapping("/reports")
	public List<Map<String, String>> getAllReports(@RequestParam String ownerEmail) {

		return null;
	}
}
