package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Extra User Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Extra User Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "ex_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_key")
    private String userKey;

    @OneToOne
    @JoinColumn(unique = true)
    private User relatedUser;

    @ManyToOne
    private Company company;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "ex_user_locations",
               joinColumns = @JoinColumn(name = "ex_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "locations_id", referencedColumnName = "id"))
    private Set<Location> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserKey() {
        return userKey;
    }

    public ExUser userKey(String userKey) {
        this.userKey = userKey;
        return this;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public User getRelatedUser() {
        return relatedUser;
    }

    public ExUser relatedUser(User user) {
        this.relatedUser = user;
        return this;
    }

    public void setRelatedUser(User user) {
        this.relatedUser = user;
    }

    public Company getCompany() {
        return company;
    }

    public ExUser company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public ExUser locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public ExUser addLocations(Location location) {
        this.locations.add(location);
        location.getUsers().add(this);
        return this;
    }

    public ExUser removeLocations(Location location) {
        this.locations.remove(location);
        location.getUsers().remove(this);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExUser)) {
            return false;
        }
        return id != null && id.equals(((ExUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ExUser{" +
            "id=" + getId() +
            ", userKey='" + getUserKey() + "'" +
            "}";
    }
}
