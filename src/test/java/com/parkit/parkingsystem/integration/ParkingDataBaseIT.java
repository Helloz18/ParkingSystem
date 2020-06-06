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

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingService parkingService;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        //dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test@Disabled
    public void testParkingACar(){
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        
        int parkingSuivant = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        boolean parkingSpotForAbcdef = parkingSpotDAO.updateParking(ticketDAO.getTicket("ABCDEF").getParkingSpot());               
        int ticketID = ticketDAO.getTicket("ABCDEF").getId();
        
        assertEquals(2,parkingSuivant);
        assertTrue(parkingSpotForAbcdef);
        assertEquals(1,ticketID);
        
//        try (Connection con = dataBaseTestConfig.getConnection();
//	        PreparedStatement ticketSavedInDB = con.prepareStatement(DBConstantsTest.GET_ID_TICKET_DBTEST);
//	        PreparedStatement parkingUpdated = con.prepareStatement(DBConstantsTest.Get_AVAILABLE);
//	        ResultSet rsTicketSavedInDB = ticketSavedInDB.executeQuery();
//	        ResultSet rsParkingUpdated = parkingUpdated.executeQuery();
//	        )
//        	{
//              	rsTicketSavedInDB.next();
//            	rsParkingUpdated.next();
//	        int ticketNumberSaved = rsTicketSavedInDB.getInt(1);
//	        System.out.println("The ticket saved in DB is saved with the number " + ticketNumberSaved);
//	        boolean parkingIsAvailable = rsParkingUpdated.getBoolean(1);
//	        System.out.println("The spot where the car is parked is set as " + parkingIsAvailable);
//	        
//
//	        assertEquals(1, ticketNumberSaved);
//	        assertFalse(parkingIsAvailable);
//            
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
       
        
   }
    
	
	  @Test 
	  public void testParkingLotExit() throws Exception{ 
		  //parkingService.processIncomingVehicle();
		  //TODO: check that the fare generated and out time are populated correctly in the database
	       	     
		  ParkingSpot parkingSpot = new ParkingSpot( parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), ParkingType.CAR, false);
		 
		  Ticket ticket = new Ticket();
		  ticket.setVehicleRegNumber("ABCDEF");
		 // Date inTime = new Date();
		  ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		  
		  ticket.setParkingSpot(parkingSpot);
		  ticket.setPrice(0.0);
		  
		  ticketDAO.saveTicket(ticket);
		 
//		       Ticket ticket = ticketDAO.getTicket("ABCDEF");
//		       ticket.setPrice(1.5);
//		       Date outTime = new Date();
//		       outTime.setTime(System.currentTimeMillis() + 60 * 60 * 1000);
//		       ticket.setOutTime(outTime);
//	        
//		       System.out.println("prix" + ticket.getPrice());
//		       System.out.println("heure sortie" + ticket.getOutTime());

		  ParkingService  parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
			 
		       parkingService.processExitingVehicle();
		       
		       
		       assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());// pourquoi null ?

	     
	        
		  
		  
//		  Date outTime = new Date(); 
//		  outTime.setTime( System.currentTimeMillis() + ( 60 * 60 * 1000) );
//	  
//	  Connection con = null; try { con = dataBaseTestConfig.getConnection();
//	  PreparedStatement whenIn = con.prepareStatement(DBConstantsTest.GET_INTIME_DBTEST); 
//	  PreparedStatement whenOut = con.prepareStatement(DBConstantsTest.GET_OUTTIME_DBTEST); 
//	  ResultSet rsWhenIn = whenIn.executeQuery(); 
//	  ResultSet rsWhenOut = whenOut.executeQuery(); 
//	  rsWhenIn.next(); 
//	  rsWhenOut.next();
//	  
//	  Date entry = rsWhenIn.getTime(1);
//	  System.out.println("The car entered the parking at " + entry); 
//	  Date exit = rsWhenOut.getTime(1); System.out.println("The car exited the parking at " + exit);
//	  
//	  dataBaseTestConfig.closeResultSet(rsWhenIn);
//	  dataBaseTestConfig.closePreparedStatement(whenIn);
//	  dataBaseTestConfig.closeResultSet(rsWhenOut);
//	  dataBaseTestConfig.closePreparedStatement(whenOut);
//	  
//	  } catch (ClassNotFoundException e) { e.printStackTrace(); } catch
//	  (SQLException e) { e.printStackTrace(); }
//	  
	  
	   
	  }
	 
}
