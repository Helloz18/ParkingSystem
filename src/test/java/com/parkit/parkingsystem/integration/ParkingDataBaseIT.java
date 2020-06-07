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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static void setUp() throws Exception{
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
   	 	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");   	 
        }

    @AfterAll
    private static void tearDown(){
    	dataBasePrepareService.clearDataBaseEntries();        
    }

    @Test
    public void testParkingACar() throws Exception{
    	when(inputReaderUtil.readSelection()).thenReturn(1);
   	 	
    	parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        
        int parkingSuivant = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        boolean parkingSpotForAbcdef = parkingSpotDAO.updateParking(ticketDAO.getTicket("ABCDEF").getParkingSpot());               
        int ticketID = ticketDAO.getTicket("ABCDEF").getId();
        
        assertEquals(2,parkingSuivant);
        assertTrue(parkingSpotForAbcdef);
        assertNotNull(ticketID);       
   }
    
	
	  @Test 
	  public void testParkingLotExit() throws Exception{ 
		
		  //TODO: check that the fare generated and out time are populated correctly in the database
	   	  ParkingService  parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);			
		  
		  Ticket ticket = new Ticket();
		  ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), ParkingType.CAR, false);		  
		  ticket.setParkingSpot(parkingSpot);
		  parkingSpotDAO.updateParking(parkingSpot);		
		  ticket.setVehicleRegNumber("ABCDEF");			 
		  Date inTime = new Date();
		  inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		  ticket.setInTime(inTime);
		  ticket.setPrice(0.0);	 	  	  
		  ticketDAO.saveTicket(ticket);
		  
		  parkingService.processExitingVehicle();		  
		  //une fois processExitingVehicle() appelé, impossible de récupérer les valeurs de bdd
		  
		  System.out.println(ticketDAO.getTicket("ABCDEF").getPrice());
		  System.out.println(ticketDAO.getTicket("ABCDEF"));
		       
		  assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());	   
	  }
	 
}
