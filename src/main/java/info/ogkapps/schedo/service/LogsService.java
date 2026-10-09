package info.ogkapps.schedo.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import info.ogkapps.schedo.dto.CheckAvailabilityDTO;
import info.ogkapps.schedo.dto.ScheduleMeetingDTO;
import info.ogkapps.schedo.dto.ScheduledTimeListDTO;
import info.ogkapps.schedo.entity.Log;
import info.ogkapps.schedo.entity.Owner;
import info.ogkapps.schedo.repository.LogsRepository;
import info.ogkapps.schedo.repository.OwnersRepository;

@Service
public class LogsService {

	LogsRepository logsRepository;
	OwnersRepository ownersRepository;

	public LogsService(LogsRepository logsRepository, OwnersRepository ownersRepository) {
		super();
		this.logsRepository = logsRepository;
		this.ownersRepository = ownersRepository;
	}

	public List<ScheduledTimeListDTO> scheduledTimeList(CheckAvailabilityDTO checkAvailabilityDTO) {
		List<Log> allLogs = logsRepository.findByOwner_OwnerEmail(checkAvailabilityDTO.ownerEmail());
		long startNum = checkAvailabilityDTO.targetDate(), endNum = (startNum + 86400000l);
		List<Log> filteredLogs = allLogs.stream()
				.filter(log -> log.getLogVisitorTime() >= startNum && log.getLogVisitorTime() <= endNum).toList();
		List<ScheduledTimeListDTO> finalList = new LinkedList<>();
		int sr = 0;
		for (Log l : filteredLogs) {
			finalList.add(sr,
					new ScheduledTimeListDTO(null, l.getLogId(), l.getLogVisitorTime(), l.getLogVisitorEmail(), "ok"));
		}
		return finalList;
	}

	private long convertTo(long t, boolean end) {
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDate date = Instant.ofEpochMilli(t).atZone(zoneId).toLocalDate();
		long startOfDayMillis = date.atStartOfDay(zoneId).toInstant().toEpochMilli();
		long endOfDayMillis = date.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1;
		return end ? endOfDayMillis : startOfDayMillis;
	}

	public String markMeetingIfValid(ScheduleMeetingDTO scheduleMeetingDTO) {
		Long targetTime = scheduleMeetingDTO.logVisitorTime();
		long beforeTarget;
		long afterTarget;
		Owner tOwner = ownersRepository.findByOwnerEmail(scheduleMeetingDTO.ownerEmail());
		if (tOwner != null) {
			beforeTarget = targetTime - tOwner.getOwnerTimeSpan();
			;
			afterTarget = targetTime + tOwner.getOwnerTimeSpan();
			;
		} else {
			return "Failed";
		}

		List<Log> filteredLogs = logsRepository.findByLogVisitorTimeBetween(convertTo(targetTime, false),
				convertTo(targetTime, true));

		List<Log> tempList = filteredLogs.stream()
				.filter(log -> beforeTarget > log.getLogVisitorTime() && afterTarget < log.getLogVisitorTime())
				.toList();

		if (!tempList.isEmpty()) {
			return "Time not available. Check Availability and retry.";
		}
		long timeNow = System.currentTimeMillis();
		logsRepository.save(new Log(null, tOwner, scheduleMeetingDTO.logVisitorName(),
				scheduleMeetingDTO.logVisitorEmail(), scheduleMeetingDTO.logVisitorTime(), timeNow));

		return "marked";
	}
}
