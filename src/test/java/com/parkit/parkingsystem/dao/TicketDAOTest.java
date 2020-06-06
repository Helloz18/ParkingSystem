package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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
    
    private static TicketDAO dao;
    
    
    @BeforeEach
    private void setUp() throws Exception{
        dao = new TicketDAO();
        dao.dataBaseConfig = dataBaseTestConfig;
        
        when(dataBaseTestConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        Ticket ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        ticket.setInTime(inTime);
        ticket.setPrice(1.5);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setReductionForRecurrentClient(true);
        
        //dataBasePrepareService = new DataBasePrepareService();
        // insertion d'une voiture ABCDEF en base qui est entrée et sortie du parking
//        String request = "insert into ticket (PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME )values (1, 'ABCDEF' , 1.0,'2020-05-24 09:29:43','2020-05-24 10:29:43')";
//        String request2 = "update parking set AVAILABLE = '0' where PARKING_NUMBER > 1 && PARKING_NUMBER < 4";
//        Connection con = dataBaseTestConfig.getConnection();
//        PreparedStatement ps = con.prepareStatement(request);
//        PreparedStatement ps2 = con.prepareStatement(request2);
//        ps.execute();
//        ps2.execute();
    }

//    @AfterAll
//    private static void setUpPerTest() throws Exception {
//    	//on vide la base de test après que les tests aient été effectués.
//        dataBasePrepareService.clearDataBaseEntries();
//    }

    
  
	@Test
	void thisVehicleIsRecurrent() throws Exception {  
		// creating a new ticket and the vehicle is already in the database  
        Ticket ticket2 = new Ticket();
        ticket2.setVehicleRegNumber("ABCDEF");
        //Act
        dao.saveTicket(ticket2);

		//Assert True 
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
	
//	@Test
//	void updatingAticket() throws ClassNotFoundException, SQLException {
////		String updatingTicket ="update ticket set price = '1.0', OUT_TIME = '2021-05-24 10:29:43' where id = 2";
////		Connection con = dataBaseTestConfig.getConnection();
////		PreparedStatement ps = con.prepareStatement(updatingTicket);
////		ps.execute();
//		Ticket ticket = new Ticket();
//		ticket.setVehicleRegNumber("MNOPQR");
//		ticket.setPrice(1.0);
//		when(preparedStatement.executeQuery()).thenReturn(resultSet);
//		when(resultSet.)
//		
//		dao.updateTicket(ticket);
//	
//		assertEquals();
//	}
	
}
