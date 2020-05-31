package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.service.InteractiveShell;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;


class InputReaderUtilTest {

	InputReaderUtil inputReaderUtil;

	/*
	 * @Test public void userShouldInputNumberBetween1And3() { //Arrange
	 * InputReaderUtil input = new InputReaderUtil();
	 * 
	 * Scanner scan = new Scanner(System.in); int entree =
	 * Integer.parseInt(scan.nextLine()); entree = 1;
	 * 
	 * //Act int result = input.readSelection();
	 * 
	 * //Assert assertEquals(entree, result); }
	 */
	
	@Test
	void aNonExistingNumberIsEntered() {
		
		//String answerGiven = "^[a-z]{0,1000}$";
		
		InteractiveShell.loadInterface();
		 when(inputReaderUtil.readSelection()).thenReturn(4);
	        
		
		
		
	}
}
