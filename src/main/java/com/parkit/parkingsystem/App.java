package com.parkit.parkingsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;


final class App {
/**
* logger : used to display a message in the console.
*/
    private static final Logger LOGGER = LogManager.getLogger("App");
    public static void main(String[] args) {
        LOGGER.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
