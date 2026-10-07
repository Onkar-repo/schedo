package info.ogkapps.schedo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import info.ogkapps.schedo.dto.RegisterOwnerDTO;
import info.ogkapps.schedo.entity.Log;
import info.ogkapps.schedo.entity.Owner;
import info.ogkapps.schedo.repository.LogsRepository;
import info.ogkapps.schedo.repository.OwnersRepository;

@Service
public class OwnerService {

	private final OwnersRepository ownersRepository;
	private final LogsRepository logsRepository;
	
	public OwnerService(OwnersRepository ownersRepository, LogsRepository logsRepository) {
		super();
		this.ownersRepository = ownersRepository;
		this.logsRepository = logsRepository;
	}

	public String registerOwner(RegisterOwnerDTO registerOwnerDTO) {
		Long ts = System.currentTimeMillis();	
		ownersRepository.save(new Owner(registerOwnerDTO.ownerName(), registerOwnerDTO.ownerEmail(), registerOwnerDTO.ownerCatagory(), ts, ts));

		return "registered";
	}
	
	public List<Log> getOwnerLogs(String ownerEmail){
		
		return logsRepository.findByOwner_OwnerEmail(ownerEmail);
	}
}
