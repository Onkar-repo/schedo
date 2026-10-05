package info.ogkapps.schedo.controller;

import java.util.List;
import java.util.Map;
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

	public String registerOwnerRequest(@RequestBody RegisterOwnerDTO registerOwnerDTO, HttpSession session) {

		if (validationService.isOwnerRegistrationOnGoing(registerOwnerDTO, session)) {

			return null;
		}
		validationService.initiateRegistration(registerOwnerDTO, session);

		return null;
	}

	public String otpReceived(@RequestBody RegisterOwnerDTO registerOwnerDTO, HttpSession session) {

		if (validationService.isOtpValid(registerOwnerDTO, session)) {
			return ownerService.registerOwner(registerOwnerDTO);
		}

		return null;
	}

	public List<ScheduledTimeListDTO> getRunningList(@RequestBody CheckAvailabilityDTO checkAvailabilityDTO) {

		return logsService.scheduledTimeList(checkAvailabilityDTO);
	}

	public String markMeeting(@RequestBody ScheduleMeetingDTO scheduleMeetingDTO) {

		return logsService.markMeetingIfValid(scheduleMeetingDTO);
	}

	public List<Map<String, String>> getAllReports(@RequestParam String ownerEmail) {

		return null;
	}
}
