package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputReaderUtil {
/**
 * Scanner : allow a input from the console.
 */
private static Scanner scan = new Scanner(System.in, "UTF-8");
/**
 * Logger : display a message in the console.
 */
private static final Logger LOGGER = LogManager.getLogger("InputReaderUtil");

/**
 * this method is used to ask the client his vehicle type.
 * @return int that set the vehicle type
 */
public int readSelection() {
	try {
		int input = Integer.parseInt(scan.nextLine());
		return input;
	} catch (Exception e) {
		LOGGER.error("Error while reading "
				+ "user input from Shell", e);
		System.out.println("Error reading input. "
				+ "Please enter valid number "
				+ "for proceeding further");
		return -1;
	}
}
/**
 * @return a String with the registration number from the vehicle
 * 		   entered by the client thanks to a scanner
 * @throws Exception
 */
	public String readVehicleRegistrationNumber() throws Exception {
		try {
			String vehicleRegNumber = scan.nextLine();
			if (vehicleRegNumber == null
			|| vehicleRegNumber.trim().length() == 0) {
				throw new IllegalArgumentException(
						"Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			LOGGER.error("Error while reading "
					+ "user input from Shell",
			e);
			System.out.println("Error reading input. "
					+ "Please enter a valid string "
					+ "for vehicle registration number");
			throw e;
		}
	}

}
