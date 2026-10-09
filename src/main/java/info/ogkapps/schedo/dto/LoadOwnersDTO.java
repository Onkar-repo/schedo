package info.ogkapps.schedo.dto;

public record LoadOwnersDTO(Integer ownerId, String ownerName, String ownerEmail, String ownerCatagory,
		Long ownerTimeSpan, Long ownerStartTime, Long ownerEndTime, Long ownerStartBreak, Long ownerEndBreak,
		Long ownerCreatedAt, Long ownerUpdatedAt) {

}