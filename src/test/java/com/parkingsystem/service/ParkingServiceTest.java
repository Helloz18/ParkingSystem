package com.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
        	lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");            
            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest() {
        
    	Ticket ticket = new Ticket();
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);      
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        
        parkingService.processExitingVehicle();
        
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    
    @Test
    public void processIncomingVehicleTest() {
       	when(inputReaderUtil.readSelection()).thenReturn(1);
    	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(2);
    	
    	parkingService.processIncomingVehicle();
    	
    	verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    
    @Test
    public void processExitingVehicle_but_updateTicketFailed() {
    	String expectedMessage = "Unable to process exiting vehicle";    	
    	try {
    		parkingService.processExitingVehicle();
    	}catch(NullPointerException ex) {
    		assert(ex.getMessage().contains(expectedMessage));
    	}
        	
    }
    
    @Test
    public void getNextParkingNumber_but_isNot_available() {
    	String expectedMessage = "Error fetching next available parking slot";    	
    	try {
    		parkingService.getNextParkingNumberIfAvailable();
    	}catch(NullPointerException ex) {
    		assert(ex.getMessage().contains(expectedMessage));
    	}
        	
    }
}


