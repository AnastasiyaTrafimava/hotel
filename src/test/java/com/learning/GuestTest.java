package com.learning;

import com.learning.domain.Guest;
import com.learning.domain.RoomStatus;
import com.learning.domain.RoomTypes;
import com.learning.domain.Rooms;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;


public class GuestTest {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();


    @Before
    public void start() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Guest> guests = new ArrayList<Guest>();
        guests.add(getGuest("Ivan", "Ivanov", new Rooms()));
        guests.add(getGuest("Petr", "Petrov", new Rooms()));
        entityManager.getTransaction().begin();
        guests.forEach(guest -> entityManager.persist(guest));
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static Guest getGuest(String name, String surname, Rooms rooms) {
        Guest guest = new Guest();
        guest.setGuestName(name);
        guest.setGuestSurname(surname);
        guest.setRooms(getRooms(rooms.getRoomStatus(), rooms.getRoomTypes()));
        return guest;
    }

    private static Rooms getRooms(RoomStatus roomStatus, RoomTypes roomTypes){
        Rooms room = new Rooms();
        room.setRoomStatus(roomStatus);
        room.setRoomTypes(roomTypes);
        return room;
    }

    @After
    public void end() {
        entityManagerFactory.close();
    }

    @Test
    public void type() {
        RoomTypes roomTypes = RoomTypes.STANDART;
        Assert.assertEquals("STANDART", roomTypes.toString());

    }

    @Test
    public void findFreeRooms() {
        List<Rooms> rooms = entityManager.createNamedQuery("select * from Rooms where RoomStatus = 0", Rooms.class)
                .getResultList();
        Assert.assertEquals(2, rooms.size());
    }
}
