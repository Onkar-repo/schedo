package info.ogkapps.schedo.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import info.ogkapps.schedo.dto.LoadOwnersDTO;
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
		
		if (ownersRepository.existsByOwnerEmail(registerOwnerDTO.ownerEmail())) {
			return "Owner already exists by " + registerOwnerDTO.ownerEmail();
		}
		Long ts = System.currentTimeMillis();
		ownersRepository.save(new Owner(registerOwnerDTO.ownerName(), registerOwnerDTO.ownerEmail(),
				registerOwnerDTO.ownerCatagory(), ts, ts));

		return "registered";
	}

	public List<Log> getOwnerLogs(String ownerEmail) {

		return logsRepository.findByOwner_OwnerEmail(ownerEmail);
	}

	public List<LoadOwnersDTO> returnAllOwners() {
		List<Owner> all = ownersRepository.findAll();
		List<LoadOwnersDTO> list = new LinkedList<>();
		System.out.println(all);
		for (Owner owner : all) {
	
			System.out.println(owner);
			list.add(new LoadOwnersDTO(owner.getOwnerId(), owner.getOwnerName(), owner.getOwnerEmail(),
					owner.getOwnerCatagory(), owner.getOwnerTimeSpan()/60000l, owner.getOwnerStartTime()/3600000l,
					owner.getOwnerEndTime()/3600000l, owner.getOwnerStartBreak()/3600000l, owner.getOwnerEndBreak()/3600000l,
					owner.getOwnerCreatedAt(), owner.getOwnerUpdatedAt()));
		}
		return list;
	}
}
