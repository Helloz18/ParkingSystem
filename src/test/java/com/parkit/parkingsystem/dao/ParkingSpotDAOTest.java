package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

class ParkingSpotDAOTest {
	

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    
    
    ParkingSpot parkingSpot;
       
    private static ParkingSpotDAO parkingSpotDAO;
    
    @BeforeEach
    private void setUp() throws Exception{

        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
      //base de test : indiquer qu'une voiture (1) est garée, et indiquer qu'une moto (4) est garée
        String requestCar = "update parking set AVAILABLE = '0' where PARKING_NUMBER = 1";
        String requestBike = "update parking set AVAILABLE = '0' where PARKING_NUMBER = 4";
        Connection con = dataBaseTestConfig.getConnection();
        PreparedStatement psCar = con.prepareStatement(requestCar);
        PreparedStatement psBike = con.prepareStatement(requestBike);
        psCar.execute();
        psBike.execute();
        
       }

    @AfterAll
    private static void setUpPerTest() throws Exception {
    	//on vide la base de test après que les tests aient été effectués.
        dataBasePrepareService.clearDataBaseEntries();
    }



	@Test
	void testNextParkingSpotIsFoundForAcar() {
    	int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        
    	assertEquals(2, result );
		
		}
	
	@Test
	void testNextParkingSpotIsFoundForAbike() {
    	int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        
    	assertEquals(5, result );
		
		}
	
	@Test
	void testUpdateParkingSpotForAcar() {
        parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
		
        parkingSpotDAO.updateParking(parkingSpot);
        
        boolean result = parkingSpot.isAvailable();
		
		assertFalse(result);
	}
	
	@Test
	void testUpdateParkingSpotForAbike() {
		parkingSpot = new ParkingSpot(4, ParkingType.BIKE,false);
		
		parkingSpotDAO.updateParking(parkingSpot);
		
		boolean result = parkingSpot.isAvailable();
		
		assertFalse(result);
	}
}
