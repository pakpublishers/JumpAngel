package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Motorist implements Serializable {

	String FirstName=null;
	String LastName=null;
	String CellNumber=null;
	String EMailAddress=null;
	String Pasword=null;
	String IEmergencyN=null;
	String WePayID=null;
	String ExpiryDate=null;
	String ThreeDigitSecurityCode=null;
	String creaditcard_no=null;
	String master_id=null;
	String Lat=null;
	String Long=null;
	
	
	Boolean CommercialFleetInsurance=false;
	Boolean ProfessionallyPermittedFleet=false;
	Boolean CommerciallyLicensedDriver=false;
	Boolean NATACertifiedDrivers=false;
	
	static int TotalParceableObjects=10;
	
	public String getJSON()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("FirstName", FirstName);}catch(Exception k){}
		try{obj.put("LastName", LastName);}catch(Exception k){}
		try{obj.put("CellNumber", CellNumber);}catch(Exception k){}
		try{obj.put("EMailAddress", EMailAddress);}catch(Exception k){}
		try{obj.put("Pasword", Pasword);}catch(Exception k){}
		try{obj.put("IEmergencyN", IEmergencyN);}catch(Exception k){}
		try{obj.put("WePayID", WePayID);}catch(Exception k){}
		try{obj.put("ThreeDigitSecurityCode", ThreeDigitSecurityCode);}catch(Exception k){}
		try{obj.put("creaditcard_no", creaditcard_no);}catch(Exception k){}
		try{obj.put("ExpiryDate", ExpiryDate);}catch(Exception k){}
		
		try{obj.put("master_id", master_id);}catch(Exception k){}
		try{obj.put("Lat", Lat);}catch(Exception k){}
		try{obj.put("Long", Long);}catch(Exception k){}
		
		try{obj.put("CommercialFleetInsurance", CommercialFleetInsurance);}catch(Exception k){}
		try{obj.put("ProfessionallyPermittedFleet", ProfessionallyPermittedFleet);}catch(Exception k){}
		try{obj.put("CommerciallyLicensedDriver", CommerciallyLicensedDriver);}catch(Exception k){}
		try{obj.put("NATACertifiedDrivers", NATACertifiedDrivers);}catch(Exception k){}
		
		try{obj.put("TotalParceableObjects", TotalParceableObjects);}catch(Exception k){}
		return obj.toString();
	}
	
	public void parsJSONOBject(JSONObject obj)
	{
		try{FirstName=obj.getString("FirstName");}catch(Exception g){}
		try{LastName=obj.getString("LastName");}catch(Exception g){}
		try{CellNumber=obj.getString("CellNumber");}catch(Exception g){}
		try{EMailAddress=obj.getString("EMailAddress");}catch(Exception g){}
		try{Pasword=obj.getString("Pasword");}catch(Exception g){}
		try{IEmergencyN=obj.getString("IEmergencyN");}catch(Exception g){}
		try{WePayID=obj.getString("WePayID");}catch(Exception g){}
		try{ThreeDigitSecurityCode=obj.getString("ThreeDigitSecurityCode");}catch(Exception g){}
		try{creaditcard_no=obj.getString("creaditcard_no");}catch(Exception g){}
		try{ExpiryDate=obj.getString("ExpiryDate");}catch(Exception g){}
		
		try{master_id=obj.getString("master_id");}catch(Exception g){}
		try{Lat=obj.getString("Lat");}catch(Exception g){}
		try{Long=obj.getString("Long");}catch(Exception g){}
		
		try{CommercialFleetInsurance=obj.getBoolean("CommercialFleetInsurance");}catch(Exception g){}
		try{ProfessionallyPermittedFleet=obj.getBoolean("ProfessionallyPermittedFleet");}catch(Exception g){}
		try{CommerciallyLicensedDriver=obj.getBoolean("CommerciallyLicensedDriver");}catch(Exception g){}
		try{NATACertifiedDrivers=obj.getBoolean("NATACertifiedDrivers");}catch(Exception g){}
		
		try{TotalParceableObjects=obj.getInt("TotalParceableObjects");}catch(Exception g){}
	}
	
	
	public void setLat(String lat){Lat=lat;}
	public String getLat(){return Lat;}
	public void setLong(String Lng){Long=Lng;}
	public String getLong(){return Long;}
	
	public Boolean getCommercialFleetInsurance(){return CommercialFleetInsurance;}
	public Boolean getProfessionallyPermittedFleet(){return ProfessionallyPermittedFleet;}
	public Boolean getCommerciallyLicensedDriver(){return CommerciallyLicensedDriver;}
	public Boolean getNATACertifiedDrivers(){return NATACertifiedDrivers;}
	
	public void setCommercialFleetInsurance(Boolean val){ CommercialFleetInsurance=val;}
	public void setProfessionallyPermittedFleet(Boolean val){ ProfessionallyPermittedFleet=val;}
	public void setCommerciallyLicensedDriver(Boolean val){ CommerciallyLicensedDriver=val;}
	public void setNATACertifiedDrivers(Boolean val){ NATACertifiedDrivers=val;}
	
	public void readMotoristFromArray(String[] data)
	{
		FirstName=data[0];
		LastName=data[1];
		CellNumber=data[2];
		EMailAddress=data[3];
		Pasword=data[4];
		IEmergencyN=data[5];
		WePayID=data[6];
		ExpiryDate=data[7];
		ThreeDigitSecurityCode=data[8];
		creaditcard_no=data[9];
	}
	public void setmaster_id(String val){master_id=val;}
	public String getmaster_id(){return master_id;}
	
	public static  String ConvertBoolean(Boolean b)
	{
		return ((b)?"1":"0");
	}
	public static Boolean ConvertIntoBoolean(String v)
	{
		return ((v.equals("1"))?true:false);
	}
	
	public static int getMotoristAttributesCount()
	{
		return TotalParceableObjects;
	}
	public Motorist(){}
	
	
	public String getFirstName(){return FirstName;};
	public String getLastName(){return LastName;};
	public String getCellNumber(){return CellNumber;};
	public String getEMailAddress(){return EMailAddress;};
	public String getPasword(){return Pasword;};
	public String getIEmergencyN(){return IEmergencyN;};
	public String getWePayID(){return WePayID;};
	public String getExpiryDate(){return ExpiryDate;};
	public String getThreeDigitSecurityCode(){return ThreeDigitSecurityCode;};
	public String getcreaditcard_no(){return creaditcard_no;}
	
	public void setFirstName(String val){ FirstName=val;};
	public void setLastName(String val){ LastName=val;};
	public void setCellNumber(String val){ CellNumber=val;};
	public void setEMailAddress(String val){ EMailAddress=val;};
	public void setPasword(String val){ Pasword=val;};
	public void setIEmergencyN(String val){ IEmergencyN=val;};
	public void setWePayID(String val){ WePayID=val;};
	public void setExpiryDate(String val){ ExpiryDate=val;};
	public void setThreeDigitSecurityCode(String val){ ThreeDigitSecurityCode=val;}
	public void setcreaditcard_no(String val){creaditcard_no=val;}
	
	public String[] getMotoristInArray(){
		
		String[] arr= new String[]{
				FirstName,
				LastName,
				CellNumber,
				EMailAddress,
				Pasword,
				IEmergencyN,
				WePayID,
				ExpiryDate,
				ThreeDigitSecurityCode,
				creaditcard_no
				
		};
		return arr;
	}
	
}
