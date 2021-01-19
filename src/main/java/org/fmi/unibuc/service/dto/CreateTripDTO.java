package org.fmi.unibuc.service.dto;

public class CreateTripDTO {

    private String title;
    private String description;
    private Long[] participantsAppUserId;
    private Long createdBy;

    public CreateTripDTO() {
    }

    public CreateTripDTO(String title, String description, Long[] participantsAppUserId, Long createdBy) {
        this.title = title;
        this.description = description;
        this.participantsAppUserId = participantsAppUserId;
        this.createdBy = createdBy;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long[] getParticipantsAppUserId() {
        return participantsAppUserId;
    }

    public void setParticipantsAppUserId(Long[] participantsAppUserId) {
        this.participantsAppUserId = participantsAppUserId;
    }
}
