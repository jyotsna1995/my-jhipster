package com.sats.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Flight.
 */
@Entity
@Table(name = "flight")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flno")
    private String flno;

    @Column(name = "orgn")
    private String orgn;

    @Column(name = "dest")
    private String dest;

    @Column(name = "flda")
    private ZonedDateTime flda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlno() {
        return flno;
    }

    public Flight flno(String flno) {
        this.flno = flno;
        return this;
    }

    public void setFlno(String flno) {
        this.flno = flno;
    }

    public String getOrgn() {
        return orgn;
    }

    public Flight orgn(String orgn) {
        this.orgn = orgn;
        return this;
    }

    public void setOrgn(String orgn) {
        this.orgn = orgn;
    }

    public String getDest() {
        return dest;
    }

    public Flight dest(String dest) {
        this.dest = dest;
        return this;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public ZonedDateTime getFlda() {
        return flda;
    }

    public Flight flda(ZonedDateTime flda) {
        this.flda = flda;
        return this;
    }

    public void setFlda(ZonedDateTime flda) {
        this.flda = flda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Flight flight = (Flight) o;
        if (flight.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), flight.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Flight{" +
            "id=" + getId() +
            ", flno='" + getFlno() + "'" +
            ", orgn='" + getOrgn() + "'" +
            ", dest='" + getDest() + "'" +
            ", flda='" + getFlda() + "'" +
            "}";
    }
}
