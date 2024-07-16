package net.bonn2.endislandreset.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class LoginLogger {

    @Comment("This file contains a list of all uuids that have joined the server since the last end reset.\n" +
            "You should NOT have to ever manually modify this file and doing so anyways may lead to unintended behavior.")
    public List<UUID> loggedIn = new ArrayList<>();
}
