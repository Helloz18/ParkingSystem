package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
	void aNumberIsEntered() {
		int input = 2;
		
		int result = inputReaderUtil.readSelection();
		
		assertEquals(input , result);
	}
}
