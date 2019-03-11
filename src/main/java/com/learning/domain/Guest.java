package com.learning.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "GUEST")
public class Guest extends AbstractIdentified{

    @Column(name = "GUEST_NAME")
    private String guestName;

    @Column(name = "GUEST_SURNAME")
    private String guestSurname;

    @ManyToOne
    @JoinColumn(name = "ROOMS")
    private Rooms rooms;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(guestName, guest.guestName) &&
                Objects.equals(guestSurname, guest.guestSurname) &&
                Objects.equals(rooms, guest.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestName, guestSurname, rooms);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestName='" + guestName + '\'' +
                ", guestSurname='" + guestSurname + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
