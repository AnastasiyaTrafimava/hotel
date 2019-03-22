package com.learning.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ROOMS")
public class Rooms extends AbstractIdentified{

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ROOM_STATUS")
    private RoomStatus roomStatus;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ROOM_TYPES")
    private RoomTypes roomTypes;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "rooms", cascade = CascadeType.ALL)
    private List<Guest> guest = new ArrayList<>();

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public RoomTypes getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(RoomTypes roomTypes) {
        this.roomTypes = roomTypes;
    }

    public List<Guest> getGuest() {
        return guest;
    }

    public void setGuest(List<Guest> guest) {
        this.guest = guest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rooms rooms = (Rooms) o;
        return roomStatus == rooms.roomStatus &&
                roomTypes == rooms.roomTypes &&
                Objects.equals(guest, rooms.guest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomStatus, roomTypes, guest);
    }

    @Override
    public String toString() {
        return "Rooms{" +
                "roomStatus=" + roomStatus +
                ", roomTypes=" + roomTypes +
                ", guest=" + guest +
                '}';
    }
}
