package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Provider implements Serializable{

	String FirstName=null;
	String LastName=null;
	String CellNumber=null;
	String EmailAddress=null;
	String Pasword=null;
	String GeneralServiceDuty=null;
	String WePayID=null;
	String EmergencyNumber=null;
	String creditcard_no=null;
	String creditcard_enddate=null;
	String threeDigitCode=null;
	
	Boolean CommercialFleetInsurance=false;
	Boolean ProfessionallyPermittedFleet=false;
	Boolean CommerciallyLicensedDriver=false;
	Boolean NATACertifiedDrivers=false;
	
	
	
	int FleetVehicleCount=0;
	
    public String getJsonString()
    {
    	JSONObject obj=new JSONObject();
    	try{obj.put("FirstName", FirstName);}catch(Exception h){}
    	try{obj.put("LastName", LastName);}catch(Exception h){}
    	try{obj.put("CellNumber", CellNumber);}catch(Exception h){}
    	try{obj.put("EmailAddress", EmailAddress);}catch(Exception h){}
    	try{obj.put("Pasword", Pasword);}catch(Exception h){}
    	try{obj.put("GeneralServiceDuty", GeneralServiceDuty);}catch(Exception h){}
    	try{obj.put("WePayID", WePayID);}catch(Exception h){}
    	try{obj.put("EmergencyNumber", EmergencyNumber);}catch(Exception h){}
    	try{obj.put("creditcard_no", creditcard_no);}catch(Exception h){}
    	try{obj.put("creditcard_enddate", creditcard_enddate);}catch(Exception h){}
    	try{obj.put("threeDigitCode", threeDigitCode);}catch(Exception h){}
    	
    	try{obj.put("CommercialFleetInsurance", CommercialFleetInsurance);}catch(Exception h){}
    	try{obj.put("ProfessionallyPermittedFleet", ProfessionallyPermittedFleet);}catch(Exception h){}
    	try{obj.put("CommerciallyLicensedDriver", CommerciallyLicensedDriver);}catch(Exception h){}
    	try{obj.put("NATACertifiedDrivers", NATACertifiedDrivers);}catch(Exception h){}
    	
    	try{obj.put("FleetVehicleCount", FleetVehicleCount);}catch(Exception h){}
    	
    	return obj.toString();
    	
    	
    }
	public void parseJSON(JSONObject obj)
	{
		try{FirstName=obj.getString("FirstName");}catch(Exception k){}
		try{LastName=obj.getString("LastName");}catch(Exception k){}
		try{CellNumber=obj.getString("CellNumber");}catch(Exception k){}
		try{EmailAddress=obj.getString("EmailAddress");}catch(Exception k){}
		try{Pasword=obj.getString("Pasword");}catch(Exception k){}
		try{GeneralServiceDuty=obj.getString("GeneralServiceDuty");}catch(Exception k){}
		try{WePayID=obj.getString("WePayID");}catch(Exception k){}
		try{EmergencyNumber=obj.getString("EmergencyNumber");}catch(Exception k){}
		try{creditcard_no=obj.getString("creditcard_no");}catch(Exception k){}
		try{creditcard_enddate=obj.getString("creditcard_enddate");}catch(Exception k){}
		try{threeDigitCode=obj.getString("threeDigitCode");}catch(Exception k){}
		
		
		try{CommercialFleetInsurance=obj.getBoolean("CommercialFleetInsurance");}catch(Exception k){}
		try{ProfessionallyPermittedFleet=obj.getBoolean("ProfessionallyPermittedFleet");}catch(Exception k){}
		try{CommerciallyLicensedDriver=obj.getBoolean("CommerciallyLicensedDriver");}catch(Exception k){}
		try{NATACertifiedDrivers=obj.getBoolean("NATACertifiedDrivers");}catch(Exception k){}
		
		try{FleetVehicleCount=obj.getInt("FleetVehicleCount");}catch(Exception k){}
		
	}
	
	public Provider(){}
	public Provider(Parcel in){
		String[] arr=new String[7];
		in.readStringArray(arr);
		boolean[] arr2=new boolean[4];
		in.readBooleanArray(arr2);
		
		readStringAttributes(arr);
		readBooleanAttributes(arr2);
		FleetVehicleCount=in.readInt();
	}
	
	public void readStringAttributes(String[] arr)
	{
		 FirstName=arr[0];
		 LastName=arr[1];
		 CellNumber=arr[2];
		 EmailAddress=arr[3];
		 Pasword=arr[4];
		 GeneralServiceDuty=arr[5];
		 WePayID=arr[6];
	}
	
	public void readBooleanAttributes(boolean[] arr)
	{
		CommercialFleetInsurance=arr[0];
		ProfessionallyPermittedFleet=arr[1];
		CommerciallyLicensedDriver=arr[2];
		NATACertifiedDrivers=arr[3];
	}
	
	public String getFirstName(){return FirstName;}
	public String getLastName(){return LastName;}
	public String getCellNumber(){return CellNumber;}
	public String getEmailAddress(){return EmailAddress;}
	public String getPasword(){return Pasword;}
	public String getGeneralServiceDuty(){return GeneralServiceDuty;}
	public String getEmergencyNumber(){return EmergencyNumber;}
	
	public Boolean getCommercialFleetInsurance(){return CommercialFleetInsurance;}
	public Boolean getProfessionallyPermittedFleet(){return ProfessionallyPermittedFleet;}
	public Boolean getCommerciallyLicensedDriver(){return CommerciallyLicensedDriver;}
	public Boolean getNATACertifiedDrivers(){return NATACertifiedDrivers;}
	
	public String getWePayID(){return WePayID;}
	
	public int getFleetVehicleCount(){return FleetVehicleCount;}
	
	public void setFirstName(String val){ FirstName=val;}
	public void setLastName(String val){ LastName=val;}
	public void setCellNumber(String val){ CellNumber=val;}
	public void setEmailAddress(String val){ EmailAddress=val;}
	public void setPasword(String val){ Pasword=val;}
	public void setGeneralServiceDuty(String val){ GeneralServiceDuty=val;}
	public void setEmergencyNumber(String val){ EmergencyNumber=val;}
	
	public void setCommercialFleetInsurance(Boolean val){ CommercialFleetInsurance=val;}
	public void setProfessionallyPermittedFleet(Boolean val){ ProfessionallyPermittedFleet=val;}
	public void setCommerciallyLicensedDriver(Boolean val){ CommerciallyLicensedDriver=val;}
	public void setNATACertifiedDrivers(Boolean val){ NATACertifiedDrivers=val;}
	
	public void setWePayID(String val){ WePayID=val;}
	
	public void setFleetVehicleCount(int val){ FleetVehicleCount=val;}
	
	public String getcreditcard_no(){return creditcard_no;}
	public String getcreditcard_enddate(){return creditcard_enddate;}
	public String getthreeDigitCode(){return threeDigitCode;}
	
	public void setcreditcard_no(String val){creditcard_no=val;}
	public void setcreditcard_enddate(String val){creditcard_enddate=val;}
	public void setthreeDigitCode(String val){threeDigitCode=val;}
	
	public String[] getStringAttributes()
	{
		return new String[]{
				 FirstName,
				 LastName,
				 CellNumber,
				 EmailAddress,
				 Pasword,
				 GeneralServiceDuty,
				 WePayID
		};
	}
	public boolean[] getBooleanAttributes(){
		return new boolean[]{
				
				 CommercialFleetInsurance,
				 ProfessionallyPermittedFleet,
				 CommerciallyLicensedDriver,
				 NATACertifiedDrivers
		};
	}
	
	
	
}
