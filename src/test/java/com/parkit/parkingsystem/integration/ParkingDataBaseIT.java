package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import constants.DBConstantsTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig;
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingService parkingService;

	@BeforeAll
	private static void setUp() throws Exception {
		dataBaseTestConfig = new DataBaseTestConfig();
		dataBasePrepareService = new DataBasePrepareService();
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);

		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability

		int parkingSuivant = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		boolean parkingSpotForAbcdef = parkingSpotDAO.updateParking(ticketDAO.getTicket("ABCDEF").getParkingSpot());
		int ticketID = ticketDAO.getTicket("ABCDEF").getId();

		assertEquals(2, parkingSuivant);
		assertTrue(parkingSpotForAbcdef);
		assertNotNull(ticketID);
	}

	@Test
	public void testParkingLotExit() throws Exception {

		// TODO: check that the fare generated and out time are populated correctly in
		// the database
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), ParkingType.CAR,
				false);
		ticket.setParkingSpot(parkingSpot);
		parkingSpotDAO.updateParking(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);
		ticket.setPrice(0.0);
		ticketDAO.saveTicket(ticket);

		parkingService.processExitingVehicle();

		DecimalFormat df = new DecimalFormat("###0.00");

		assertEquals("1,50", df.format(ticketDAO.getTicket("ABCDEF").getPrice()));
		assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());
	}

	@Test
	void thisVehicleIsRecurrent() throws Exception {

		// the car enters and exit the parking
		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), ParkingType.CAR,
				false);
		ticket.setParkingSpot(parkingSpot);
		parkingSpotDAO.updateParking(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);
		ticket.setPrice(0.0);
		ticketDAO.saveTicket(ticket);
		parkingService.processExitingVehicle();
		// When : The car enters again and has a new ticket
		Ticket ticket2 = new Ticket();
		ParkingSpot parkingSpot2 = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),
				ParkingType.CAR, false);
		ticket2.setParkingSpot(parkingSpot2);
		parkingSpotDAO.updateParking(parkingSpot2);
		ticket2.setVehicleRegNumber("ABCDEF");
		Date inTime2 = new Date();
		ticket2.setInTime(inTime2);
		ticket2.setPrice(0.0);

		Connection con = dataBaseTestConfig.getConnection();
		PreparedStatement recurrent = con.prepareStatement(DBConstantsTest.RECURRENT);
		recurrent.setString(1, ticket2.getVehicleRegNumber());
		ResultSet rsRecurrent = recurrent.executeQuery();
		rsRecurrent.next();
		int enteredPreviously = rsRecurrent.getInt("count");

		ticketDAO.saveTicket(ticket2);

		// Assert True : the car has more than one ticket in the database
		assertTrue(enteredPreviously >= 1);
		assertTrue(ticket2.isReductionForRecurrentClient());
	}

	@Test
	void thisVehicleIsNotRecurrent() {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		Ticket ticket = ticketDAO.getTicket("ABCDEF");

		assertFalse(ticket.isReductionForRecurrentClient());

	}

	@Test
	void thisVehicle_has_no_previous_ticket() throws Exception {

		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), ParkingType.CAR,
				false);
		ticket.setParkingSpot(parkingSpot);
		parkingSpotDAO.updateParking(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		Date inTime = new Date();
		ticket.setInTime(inTime);
		ticket.setPrice(0.0);

		Connection con = dataBaseTestConfig.getConnection();
		PreparedStatement recurrent = con.prepareStatement(DBConstantsTest.RECURRENT);
		recurrent.setString(1, ticket.getVehicleRegNumber());
		ResultSet rsRecurrent = recurrent.executeQuery();
		rsRecurrent.next();
		int enteredPreviously = rsRecurrent.getInt("count");

		ticketDAO.saveTicket(ticket);

		assertEquals(0, enteredPreviously);
	}

}
