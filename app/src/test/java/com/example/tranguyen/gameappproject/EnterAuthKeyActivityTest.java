package com.example.tranguyen.gameappproject;

import org.junit.Test;

import static org.junit.Assert.*;

public class EnterAuthKeyActivityTest {

    @Test
    public void getHuntIdFromAuthKey() {

        EnterAuthKeyActivity activity = new EnterAuthKeyActivity();

        Hunt hunt = activity.getHunt();
        assertEquals(hunt.getId(), 1);



    }

}