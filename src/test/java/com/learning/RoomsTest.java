package com.learning;

import com.learning.domain.RoomTypes;
import org.junit.Assert;
import org.junit.Test;


public class RoomsTest {

    @Test
    public void start() {
        RoomTypes roomTypes = RoomTypes.STANDART;
        Assert.assertEquals(RoomTypes.valueOf("STANDART"), roomTypes);

    }
}


