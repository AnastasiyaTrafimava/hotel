package com.learning;

import com.learning.domain.Guest;
import com.learning.domain.RoomStatus;
import com.learning.domain.RoomTypes;
import com.learning.domain.Rooms;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
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
        guests.add(getGuest("Ivan", "Ivanov", 1));
        guests.add(getGuest("Semen", "Semenov", 1));
        guests.add(getGuest("Petr", "Petrov", 1));
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
        guest1.add(getGuest("Potap", "Potapov", 1));
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

    @Test
    public void lockingGuest() {
        Guest guest1 = entityManager.createNamedQuery("guestFind", Guest.class)
                .setParameter("guestName", "Ivan")
                .setParameter("guestSurname", "Ivanov")
                .getSingleResult();

        Assert.assertEquals(1, guest1.getVersion());
        entityManager.lock(guest1, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        Assert.assertEquals(2, guest1.getVersion());
    }

    @AfterClass
    public static void finish() {
        entityManagerFactory.close();
    }

    private static Guest getGuest(String name, String surname, int version) {
        Guest guest = new Guest();
        guest.setGuestName(name);
        guest.setGuestSurname(surname);
        guest.setVersion(version);
        return guest;
    }
}
