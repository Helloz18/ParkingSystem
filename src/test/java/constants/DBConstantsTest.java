package constants;

public class DBConstantsTest {

	public static final String GET_INTIME_DBTEST = "SELECT IN_TIME FROM `ticket` where VEHICLE_REG_NUMBER=\"ABCDEF\"";
	public static final String GET_OUTTIME_DBTEST = "SELECT OUT_TIME FROM `ticket` where VEHICLE_REG_NUMBER=\"ABCDEF\"";
	public static final String GET_ID_TICKET_DBTEST = "SELECT ID FROM `ticket` where VEHICLE_REG_NUMBER=\"ABCDEF\"";
	public static final String Get_AVAILABLE = "SELECT AVAILABLE FROM `parking` WHERE PARKING_NUMBER = ( SELECT PARKING_NUMBER from ticket WHERE VEHICLE_REG_NUMBER=\"ABCDEF\")";
	
}
