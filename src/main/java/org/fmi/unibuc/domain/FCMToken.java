package org.fmi.unibuc.domain;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A FCMToken.
 */
@Entity
@Table(name = "fcmtoken")
public class FCMToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "app_user_id")
    private Long appUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public FCMToken token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public FCMToken appUserId(Long appUserId) {
        this.appUserId = appUserId;
        return this;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FCMToken)) {
            return false;
        }
        return id != null && id.equals(((FCMToken) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FCMToken{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            ", appUserId=" + getAppUserId() +
            "}";
    }
}
