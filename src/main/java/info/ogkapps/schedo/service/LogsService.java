package info.ogkapps.schedo.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
		
		for (Log log2 : allLogs) {
			System.out.println(log2.toString());
		}
		
		long startNum = checkAvailabilityDTO.targetDate(), endNum = (startNum + 86400000l);
		List<Log> filteredLogs = allLogs.stream()
				.filter(log -> log.getLogVisitorTime() >= startNum && log.getLogVisitorTime() <= endNum).toList();
		List<ScheduledTimeListDTO> finalList = new LinkedList<>();
		int sr = 0;
		for (Log l : filteredLogs) {
			finalList.add(sr,
					new ScheduledTimeListDTO(++sr, l.getLogId(), l.getLogVisitorTime(), l.getLogVisitorEmail(), "ok"));
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

	private long startMomentOfTheDay(long millis) {
		LocalDateTime temp = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
		LocalDateTime customDateTime = LocalDateTime.of(temp.getYear(), temp.getMonthValue(), temp.getDayOfMonth(), 0,
				0, 0);
		long newMillis = customDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		return newMillis;
	}

	public String markMeetingIfValid(ScheduleMeetingDTO scheduleMeetingDTO) {
		Long targetTime = scheduleMeetingDTO.logVisitorTime();
		long beforeTarget;
		long afterTarget;
		Owner tOwner = ownersRepository.findByOwnerEmail(scheduleMeetingDTO.ownerEmail());
		if (tOwner != null) {
			beforeTarget = targetTime - tOwner.getOwnerTimeSpan();
			afterTarget = targetTime + tOwner.getOwnerTimeSpan();
		} else {
			return "Failed";
		}

		long shiftStart, shiftEnd, breakStart, breakEnd;
		long baseTime = startMomentOfTheDay(targetTime);
		shiftStart = tOwner.getOwnerStartTime() + baseTime;
		shiftEnd = tOwner.getOwnerEndTime() + baseTime;
		breakStart = tOwner.getOwnerStartBreak() + baseTime;
		breakEnd = tOwner.getOwnerEndBreak() + baseTime;

		List<Log> filteredLogs = logsRepository.findByLogVisitorTimeBetween(convertTo(targetTime, false),
				convertTo(targetTime, true));

		List<Log> tempList = filteredLogs.stream()
				.filter(log -> log.getLogVisitorTime() > beforeTarget && log.getLogVisitorTime() < afterTarget)
				.toList();

		boolean invalidTime =  targetTime < shiftStart || targetTime > shiftEnd || (targetTime > breakStart && targetTime < breakEnd);

		if (!tempList.isEmpty() || invalidTime) {
			return "Time not available. Check Availability and retry.";
		}
		long timeNow = System.currentTimeMillis();
		logsRepository.save(new Log(null, tOwner, scheduleMeetingDTO.logVisitorName(),
				scheduleMeetingDTO.logVisitorEmail(), scheduleMeetingDTO.logVisitorTime(), timeNow));

		return "marked";
	}
}
