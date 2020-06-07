package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;


@ExtendWith(MockitoExtension.class)
class TicketDAOTest {

	@Mock
    private static DataBaseTestConfig dataBaseTestConfig;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    
    @Mock
    ParkingService parkingService;
    @Mock
    private static InputReaderUtil inputReaderUtil;
    
    Ticket ticket;
    ParkingSpot parkingSpot;
    
    private static TicketDAO ticketDAO;
    
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    
    @BeforeEach
    private void setUp() throws Exception{
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        when(dataBaseTestConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(preparedStatement);      

        //on créé un ticket pour une voiture ABCDEF entrée et sortie.
        ticket = new Ticket();
        ticket.setId(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), ParkingType.CAR, true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        ticket.setInTime(inTime);
        ticket.setPrice(1.50);
        ticket.setReductionForRecurrentClient(true);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
    }


    
  
	@Test
	void thisVehicleIsRecurrent() throws Exception {  
		// creating a new ticket and the vehicle is already in the database  
		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);		  
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");			 
		Date inTime = new Date();
		ticket.setInTime(inTime);
		ticket.setPrice(0.0);	 
		ticket.setReductionForRecurrentClient(true);
        
		//Assert True 
		assertTrue(ticket.isReductionForRecurrentClient());
	}
	
	@Test
	void thisVehicleIsNotRecurrent() {
		
		Ticket ticket3 = new Ticket();
		ticket3.setVehicleRegNumber("GHIJKL");
		ParkingSpot parkingSpot = new ParkingSpot(3, ParkingType.CAR,false);
	    ticket3.setParkingSpot(parkingSpot);
	    Date inTime3 = new Date ();
	    ticket3.setInTime(inTime3);
	    ticket3.setReductionForRecurrentClient(false);
	        
		assertFalse(ticket3.isReductionForRecurrentClient());
	 
	}	
	
	@Test
	void savingAticket() {	
        assertTrue(ticketDAO.saveTicket(ticket));	
	}
	
	@Test
	void gettingAticket() throws SQLException {	
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		assertEquals(1, ticketDAO.getTicket("ABCDEF").getId());
	}
	
	@Test
	void updatingAticket() {
		assertTrue(ticketDAO.updateTicket(ticket));
	}	
	
}
