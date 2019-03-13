package com.learning.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "GUEST")
@NamedQueries(
        @NamedQuery(name = "guestFind",
                query = "select g from Guest g where guestName=:guestName and " +
                        "guestSurname=:guestSurname "))

public class Guest extends AbstractIdentified {

    @Column(name = "GUEST_NAME")
    private String guestName;

    @Column(name = "GUEST_SURNAME")
    private String guestSurname;

    @ManyToOne
    private Rooms rooms;

    @Version
    @Column(name = "VERSION")
    private int version;

    public Guest() {
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestSurname() {
        return guestSurname;
    }

    public void setGuestSurname(String guestSurname) {
        this.guestSurname = guestSurname;
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return version == guest.version &&
                Objects.equals(guestName, guest.guestName) &&
                Objects.equals(guestSurname, guest.guestSurname) &&
                Objects.equals(rooms, guest.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestName, guestSurname, rooms, version);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestName='" + guestName + '\'' +
                ", guestSurname='" + guestSurname + '\'' +
                ", rooms=" + rooms +
                ", version=" + version +
                '}';
    }
}
