package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


@ExtendWith(MockitoExtension.class)
class TicketDAOTest {

    
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    
    
    Ticket ticket;
    @Mock
    ParkingSpot parkingSpot;
    
    private static TicketDAO dao;
    
    
    @BeforeEach
    private void setUp() throws Exception{
        dao = new TicketDAO();
        dao.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        // insertion d'une voiture ABCDEF en base qui est entrée et sortie du parking
        String request = "insert into ticket (PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME )values (1, 'ABCDEF' , 1.0,'2020-05-24 09:29:43','2020-05-24 10:29:43')";
        String request2 = "update parking set AVAILABLE = '0' where PARKING_NUMBER > 1 && PARKING_NUMBER < 4";
        Connection con = dataBaseTestConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(request);
        PreparedStatement ps2 = con.prepareStatement(request2);
        ps.execute();
        ps2.execute();
    }

    @AfterAll
    private static void setUpPerTest() throws Exception {
    	//on vide la base de test après que les tests aient été effectués.
        dataBasePrepareService.clearDataBaseEntries();
    }

    
  
	@Test
	void thisVehicleIsRecurrent() {  
		// creating a new ticket for the car with ABCDEF as RegNumber   
        Ticket ticket2 = new Ticket();   
        ticket2.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot2 = new ParkingSpot(2, ParkingType.CAR,false);
        ticket2.setParkingSpot(parkingSpot2);
        Date inTime2 = new Date ();
        ticket2.setInTime(inTime2);
        
        //Act
        dao.saveTicket(ticket2);

		//Assert True car la voiture ABCDEF a déjà un ticket enregistré
		assertTrue(ticket2.isReductionForRecurrentClient());
	}
	
	@Test
	void thisVehicleIsNotRecurrent() {
		
		Ticket ticket3 = new Ticket();
		ticket3.setVehicleRegNumber("GHIJKL");
		ParkingSpot parkingSpot = new ParkingSpot(3, ParkingType.CAR,false);
	    ticket3.setParkingSpot(parkingSpot);
	    Date inTime3 = new Date ();
	    ticket3.setInTime(inTime3);
	  
	       
        dao.saveTicket(ticket3);
        
		//Assert False car la voiture GHIJKL n'est jamais entré dans ce parking avant ce test
		assertFalse(ticket3.isReductionForRecurrentClient());
	 
	}	
	
	@Test
	void savingAticket() throws ClassNotFoundException, SQLException {
		//on créé un ticket avec les infos demandées en entrée
		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(3, ParkingType.CAR,false);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("MNOPQR");
		Date inTime = new Date();
		ticket.setInTime(inTime);
		
		dao.saveTicket(ticket);
		
		String requestTicketId = "select id from ticket where VEHICLE_REG_NUMBER = 'MNOPQR'";
        Connection con = dataBaseTestConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(requestTicketId); 
        ResultSet rs = ps.executeQuery();
        rs.next();
		int ticketId = rs.getInt("id");
		
		assertEquals(2,ticketId);
	}
	
	@Test
	void gettingAticket() throws ClassNotFoundException, SQLException {

		String requestTicketId = "select id from ticket where VEHICLE_REG_NUMBER = 'MNOPQR'";
        Connection con = dataBaseTestConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(requestTicketId); 
        ResultSet rs = ps.executeQuery();
        rs.next();
		int ticketId = rs.getInt("id");
		
		dao.getTicket("MNOPQR");
		
		assertEquals(2, ticketId);
	}
	
	@Test
	void updatingAticket() throws ClassNotFoundException, SQLException {
		String updatingTicket ="update ticket set price = '1.0', OUT_TIME = '2021-05-24 10:29:43' where id = 2";
		Connection con = dataBaseTestConfig.getConnection();
		PreparedStatement ps = con.prepareStatement(updatingTicket);
		ps.execute();
		
		dao.updateTicket(dao.getTicket("MNOPQR"));		
		
		assertEquals(1.0, dao.getTicket("MNOPQR").getPrice());
	}
	
}
