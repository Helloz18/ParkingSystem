package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.Test;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.Ticket;

class TicketDAOTest {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	
	@Test
	void thisVehicleIsRecurrent() {
		//Arrange
		Ticket ticket = new Ticket();
		ticket.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);  
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (120 * 60 * 1000));
        ticket.setInTime(inTime);
        Date outTime = new Date();
        outTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000));
        ticket.setOutTime(outTime);
        TicketDAO dao = new TicketDAO();
        dao.getTicket(ticket.getVehicleRegNumber());
		
                
        Ticket ticket2 = new Ticket();   
        ticket2.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot2 = new ParkingSpot(1, ParkingType.CAR,false);
        ticket2.setParkingSpot(parkingSpot2);
        Date inTime2 = new Date ();
        inTime2.setTime(System.currentTimeMillis() - (20 * 60 * 1000));
        ticket2.setInTime(inTime2);
        
        //Act
        dao.saveTicket(ticket2);

		//Assert
		assertTrue(ticket2.isReductionForRecurrentClient());
	}
	
	//@Test
	//void getNumberTicketFromExitingVehicle() {
		 
	//}

}
