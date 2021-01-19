package org.fmi.unibuc.service.dto;

public class ExtendedUserDTO {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;

    private Long appUserId;

    public ExtendedUserDTO() {
    }

    public ExtendedUserDTO(Long id, String login, String firstName, String lastName, Long appUserId) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.appUserId = appUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }
}
