package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket implements Cloneable {
	private int id;
	private ParkingSpot parkingSpot;
	private String vehicleRegNumber;
	private double price;
	private Date inTime;
	private Date outTime;
	private boolean reductionForRecurrentClient;

	public boolean isReductionForRecurrentClient() {
		return reductionForRecurrentClient;
	}

	public void setReductionForRecurrentClient(
			boolean reductionForRecurrentClient) {
		this.reductionForRecurrentClient = reductionForRecurrentClient;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getInTime() {
		return (Date) inTime.clone();
	}

	public void setInTime(Date inTime) {
		this.inTime = (Date) inTime.clone();
	}

	public Date getOutTime() {
		if (outTime == null) {
			return null;
		} else {
			return (Date) outTime.clone();
			}
	}

	public void setOutTime(Date outTime) {
		if (outTime == null) {
			this.outTime = null;
		} else {
			this.outTime = (Date) outTime.clone();
			}
	}
}
