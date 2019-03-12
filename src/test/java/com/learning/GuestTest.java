package com.learning;

import com.learning.domain.Guest;
import com.learning.domain.RoomStatus;
import com.learning.domain.RoomTypes;
import com.learning.domain.Rooms;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;


public class GuestTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeClass
    public static void start() {
        entityManagerFactory = Persistence.createEntityManagerFactory("mysql");
    }

    @Before
    public void before() {
        entityManager = entityManagerFactory.createEntityManager();
        Rooms room = new Rooms();
        room.setRoomTypes(RoomTypes.STANDART);
        room.setRoomStatus(RoomStatus.FREE);
        List<Guest> guests = new ArrayList<Guest>();
        guests.add(getGuest("Ivan", "Ivanov"));
        guests.add(getGuest("Semen", "Semenov"));
        guests.add(getGuest("Petr", "Petrov"));
        room.setGuest(guests);
        entityManager.getTransaction().begin();
        entityManager.persist(room);
    }

    @After
    public void after() {
        entityManager.close();
    }

    @Test
    public void getAllGuests() {
        List<Guest> guest1 = entityManager.createNativeQuery("select * from GUEST").getResultList();
        Assert.assertEquals(3, guest1.size());
    }


    @Test
    public void getGuest() {
        List<Guest> guest1 = entityManager.createNamedQuery("guestFind", Guest.class)
                .setParameter("guestName", "Petr")
                .setParameter("guestSurname", "Petrov")
                .getResultList();

        Assert.assertEquals(1, guest1.size());
    }

    @Test
    public void getGuestNotHave() {
        List<Guest> guest1 = entityManager.createNamedQuery("guestFind", Guest.class)
                .setParameter("guestName", "Roman")
                .setParameter("guestSurname", "Romanov")
                .getResultList();

        Assert.assertTrue(guest1.isEmpty());
    }

    @Test
    public void updateGuestData() {
        Guest guest2 = entityManager.createNamedQuery("guestFind", Guest.class)
                .setParameter("guestName", "Ivan")
                .setParameter("guestSurname", "Ivanov")
                .getSingleResult();

        guest2.setGuestSurname("Pupkin");
        entityManager.merge(guest2);

        Assert.assertEquals("Pupkin", guest2.getGuestSurname());
    }

    @Test
    public void addGuest() {
        List<Guest> guest1 = entityManager.createNativeQuery("select * from GUEST").getResultList();
        Assert.assertEquals(3, guest1.size());
        Rooms room = new Rooms();
        room.setRoomTypes(RoomTypes.LUXE);
        room.setRoomStatus(RoomStatus.RESERVED);
        guest1.add(getGuest("Potap", "Potapov"));
        room.setGuest(guest1);
        Assert.assertEquals(4, guest1.size());
    }

    @Test
    public void deleteGuest() {
        List<Guest> guests = entityManager.createNativeQuery("select * from GUEST").getResultList();
        Assert.assertEquals(3, guests.size());
        guests.remove(0);
        Assert.assertEquals(2, guests.size());
    }


    @AfterClass
    public static void finish() {
        entityManagerFactory.close();
    }

    private static Guest getGuest(String name, String surname) {
        Guest guest = new Guest();
        guest.setGuestName(name);
        guest.setGuestSurname(surname);
        return guest;
    }
}
