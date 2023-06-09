package com.example.controlmoneys;

import junit.framework.TestCase;

public class UserTest extends TestCase {

    public void testGetUserEmail() {
        User user = new User("000", "angi@mail.ru", "abc");
        assertEquals("angi@mail.ru",user.getUserEmail());
    }


    public void testGetUserId() {
        User user = new User("000", "angi@mail.ru", "abc");
        assertEquals("000",user.getUserId());
    }

    public void testGetUserPassword() {
        User user = new User("000", "angi@mail.ru", "abc");
        assertEquals("abc",user.getUserPassword());
    }
}