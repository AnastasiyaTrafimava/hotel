package com.learning;

import com.learning.domain.Guest;
import com.learning.domain.RoomStatus;
import com.learning.domain.RoomTypes;
import com.learning.domain.Rooms;
import org.junit.*;

import javax.persistence.*;
import java.util.*;

public class GuestTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    //private List<Rooms> data = new ArrayList<>();

    @BeforeClass
    public static void start() {
        entityManagerFactory = Persistence.createEntityManagerFactory("mysql");
    }

    @Before
    public void before() {
        System.out.println("Start before");
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
        entityManager.getTransaction().commit();
        //data.add(room);
        System.out.println("End before");
    }

    @After
    public void after() {
        System.out.println("Start after");
        entityManager.getTransaction().begin();
        //data.forEach(room -> entityManager.remove(room));

        List<Rooms> roomsFromDb = entityManager.createNativeQuery("select * from ROOMS", Rooms.class).getResultList();

        for (Rooms room : roomsFromDb) {
            entityManager.remove(room);
        }

        entityManager.getTransaction().commit();

        Assert.assertTrue(entityManager.createNativeQuery("select * from ROOMS").getResultList().isEmpty());
        Assert.assertTrue(entityManager.createNativeQuery("select * from GUEST").getResultList().isEmpty());
        System.out.println("End after");
    }

    @Test
    public void testGetAllGuests() {
        List<Guest> guest1 = entityManager.createNativeQuery("select * from GUEST").getResultList();
        Assert.assertEquals(3, guest1.size());
    }


    @Test
    public void testGetGuest() {
        List<Guest> guest1 = entityManager.createNamedQuery("guestFind", Guest.class)
                .setParameter("guestName", "Petr")
                .setParameter("guestSurname", "Petrov")
                .getResultList();

        Assert.assertEquals(1, guest1.size());
    }

    @Test
    public void testGetGuestNotHave() {
        List<Guest> guest1 = entityManager.createNamedQuery("guestFind", Guest.class)
                .setParameter("guestName", "Roman")
                .setParameter("guestSurname", "Romanov")
                .getResultList();

        Assert.assertTrue(guest1.isEmpty());
    }

    @Test
    public void testUpdateGuestData() {
        Guest guest2 = entityManager.createNamedQuery("guestFind", Guest.class)
                .setParameter("guestName", "Ivan")
                .setParameter("guestSurname", "Ivanov")
                .getSingleResult();

        guest2.setGuestSurname("Pupkin");
        entityManager.merge(guest2);

        Assert.assertEquals("Pupkin", guest2.getGuestSurname());
    }

    @Test
    public void testAddGuest() {
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
    public void testDeleteGuest() {
        List<Guest> guests = entityManager.createNativeQuery("select * from GUEST").getResultList();
        Assert.assertEquals(3, guests.size());
        guests.remove(0);
        Assert.assertEquals(2, guests.size());
    }

    @Test
    public void testLockingGuest() {
        Guest guest1 = entityManager.createNamedQuery("guestFind", Guest.class)
                .setParameter("guestName", "Ivan")
                .setParameter("guestSurname", "Ivanov")
                .getSingleResult();

        Assert.assertEquals(1, guest1.getVersion());
        entityManager.getTransaction().begin();
        entityManager.lock(guest1, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        entityManager.getTransaction().commit();
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
