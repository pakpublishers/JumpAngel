package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle_Info implements Serializable{

	String vehicle_Id=null;
	String vehicleName=null;
	String Battery_Jumps=null;
	String Fuel_Delivery=null;
	String Tow_Boom=null;
	String Tow_FlatBed=null;
	String Tow_Sling=null;
	String Tow_WheelLift=null;
	String Tow_Winch=null;
	String Tire_Changes=null;
	String MobileTireRepair=null;
	String Diagnostics=null;
	String Vehicle_Unlocks=null;
	
	String Vehicle_Type=null;
	String Year=null;
	String Make=null;
	String Model=null;
	String Fuel_Type=null;
	String Color=null;
	String LicensePlate=null;
	String PlateState=null;
	public String Driving=null;
	public String Services=null;
	public String Assign=null;
	public String userid=null;
	public String name=null;
	
	
	static int AttributeCount=21;
	
	public Vehicle_Info(){}
	public Vehicle_Info(Parcel in)
	{
		String[] data=new String[AttributeCount];
		in.readStringArray(data);
		 vehicle_Id=data[0];
		 vehicleName=data[1];
		 Battery_Jumps=data[2];
		 Fuel_Delivery=data[3];
		 Tow_Boom=data[4];
		 Tow_FlatBed=data[5];
		 Tow_Sling=data[6];
		 Tow_WheelLift=data[7];
		 Tow_Winch=data[8];
		 Tire_Changes=data[9];
		 MobileTireRepair=data[10];
		 Diagnostics=data[11];
		 Vehicle_Unlocks=data[12];
		
		 Vehicle_Type=data[13];
		 Year=data[14];
		 Make=data[15];
		 Model=data[16];
		 Fuel_Type=data[17];
		 Color=data[18];
		 LicensePlate=data[19];
		 PlateState=data[20];
		
	}
	
	public Boolean isnull()
	{
		if(vehicle_Id==null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getvehicle_Id(){return vehicle_Id;};
	public String getvehicleName(){return vehicleName;}
	public String getBattery_Jumps(){return Battery_Jumps;}
	public  String getFuel_Delivery(){return Fuel_Delivery;}
	public String getTow_Boom(){return Tow_Boom;}
	public String getTow_FlatBed(){return Tow_FlatBed;}
	public String getTow_Sling(){return Tow_Sling;}
	public String getTow_WheelLift(){return Tow_WheelLift;}
	public String getTow_Winch(){return Tow_Winch;}
	public String getTire_Changes(){return Tire_Changes;}
	public String getMobileTireRepair(){return MobileTireRepair;}
	public String getDiagnostics(){return Diagnostics;}
	public String getVehicle_Unlocks(){return Vehicle_Unlocks;}
	
	public String getVehicle_Type(){return Vehicle_Type;}
	public String getYear(){return Year;}
	public String getMake(){return Make;}
	public String getModel(){return Model;}
	public String getFuel_Type(){return Fuel_Type;}
	public String getColor(){return Color;}
	public String getLicensePlate(){return LicensePlate;}
	public String getPlateState(){return PlateState;}
	
	public void setvehicle_Id(String val){ vehicle_Id=val;};
	public void setvehicleName(String val){ vehicleName=val;}
	public void setBattery_Jumps(String val){ Battery_Jumps=val;}
	public  void setFuel_Delivery(String val){ Fuel_Delivery=val;}
	public void setTow_Boom(String val){ Tow_Boom=val;}
	public void setTow_FlatBed(String val){ Tow_FlatBed=val;}
	public void setTow_Sling(String val){ Tow_Sling=val;}
	public void setTow_WheelLift(String val){ Tow_WheelLift=val;}
	public void setTow_Winch(String val){ Tow_Winch=val;}
	public void setTire_Changes(String val){ Tire_Changes=val;}
	public void setMobileTireRepair(String val){ MobileTireRepair=val;}
	public void setDiagnostics(String val){ Diagnostics=val;}
	public void setVehicle_Unlocks(String val){ Vehicle_Unlocks=val;}
	
	public void setVehicle_Type(String val){Vehicle_Type=val;}
	public void setYear(String val){ Year=val;}
	public void setMake(String val){ Make=val;}
	public void setModel(String val){ Model=val;}
	public void setFuel_Type(String val){ Fuel_Type=val;}
	public void setColor(String val){ Color=val;}
	public void setLicensePlate(String val){ LicensePlate=val;}
	public void setPlateState(String val){ PlateState=val;}
	
	public void parseObjectFromJSON(JSONObject json)
	{
		 try{vehicle_Id=json.getString("vehicle_Id");}catch(Exception e){}
		 try{vehicleName=json.getString("vehicleName");}catch(Exception e){}
		 try{Battery_Jumps=json.getString("Battery_Jumps");}catch(Exception e){}
		 try{Fuel_Delivery=json.getString("Fuel_Delivery");}catch(Exception e){}
		 try{Tow_Boom=json.getString("Tow_Boom");}catch(Exception e){}
		 try{Tow_FlatBed=json.getString("Tow_FlatBed");}catch(Exception e){}
		 try{Tow_Sling=json.getString("Tow_Sling");}catch(Exception e){}
		 try{Tow_WheelLift=json.getString("Tow_WheelLift");}catch(Exception e){}
		 try{Tow_Winch=json.getString("Tow_Winch");}catch(Exception e){}
		 try{Tire_Changes=json.getString("Tire_Changes");}catch(Exception e){}
		 try{ MobileTireRepair=json.getString("MobileTireRepair");}catch(Exception e){}
		 try{Diagnostics=json.getString("Diagnostics");}catch(Exception e){}
		 try{Vehicle_Unlocks=json.getString("Vehicle_Unlocks");}catch(Exception e){}
		
		 try{Vehicle_Type=json.getString("Vehicle_Type");}catch(Exception e){}
		 try{Year=json.getString("Year");}catch(Exception e){}
		 try{Make=json.getString("Make");}catch(Exception e){}
		 try{Model=json.getString("Model");}catch(Exception e){}
		 try{Fuel_Type=json.getString("Fuel_Type");}catch(Exception e){}
		 try{Color=json.getString("Color");}catch(Exception e){}
		 try{LicensePlate=json.getString("LicensePlate");}catch(Exception e){}
		 try{PlateState=json.getString("PlateState");}catch(Exception e){}
	}
	
	public JSONObject getJSONString()
	{
		JSONObject obj=new JSONObject();
		
		 try{obj.put("vehicle_Id",vehicle_Id);}catch(Exception k){}
		 try{obj.put("vehicleName",vehicleName);}catch(Exception k){}
		 try{obj.put("Battery_Jumps",Battery_Jumps);}catch(Exception k){}
		 try{obj.put("Fuel_Delivery",Fuel_Delivery);}catch(Exception k){}
		 try{obj.put("Tow_Boom",Tow_Boom);}catch(Exception k){}
		 try{obj.put("Tow_FlatBed",Tow_FlatBed);}catch(Exception k){}
		 try{obj.put("Tow_Sling",Tow_Sling);}catch(Exception k){}
		 try{obj.put("Tow_WheelLift",Tow_WheelLift);}catch(Exception k){}
		 try{obj.put("Tow_Winch",Tow_Winch);}catch(Exception k){}
		 try{obj.put("Tire_Changes",Tire_Changes);}catch(Exception k){}
		 try{obj.put("MobileTireRepair",MobileTireRepair);}catch(Exception k){}
		 try{obj.put("Diagnostics",Diagnostics);}catch(Exception k){}
		 try{obj.put("Vehicle_Unlocks",Vehicle_Unlocks);}catch(Exception k){}
		
		 try{obj.put("Vehicle_Type",Vehicle_Type);}catch(Exception k){}
		 try{obj.put("Year",Year);}catch(Exception k){}
		 try{obj.put("Make",Make);}catch(Exception k){}
		 try{obj.put("Model",Model);}catch(Exception k){}
		 try{obj.put("Fuel_Type",Fuel_Type);}catch(Exception k){}
		 try{obj.put("Color",Color);}catch(Exception k){}
		 try{obj.put("LicensePlate",LicensePlate);}catch(Exception k){}
		 try{obj.put("PlateState",PlateState);}catch(Exception k){}
		
		
//		String data="{"+
//		"\"vehicle_Id\":"+"\""+vehicle_Id+"\","+
//		 "\"vehicleName\":"+"\""+vehicleName+"\","+
//		 "\"Battery_Jumps\":"+"\""+Battery_Jumps+"\","+
//		 "\"Fuel_Delivery\":"+"\""+Fuel_Delivery+"\","+
//		 "\"Tow_Boom\":"+"\""+Tow_Boom+"\","+
//		 "\"Tow_FlatBed\":"+"\""+Tow_FlatBed+"\","+
//		 "\"Tow_Sling\":"+"\""+Tow_Sling+"\","+
//		 "\"Tow_WheelLift\":"+"\""+Tow_WheelLift+"\","+
//		 "\"Tow_Winch\":"+"\""+Tow_Winch+"\","+
//		 "\"Tire_Changes\":"+"\""+Tire_Changes+"\","+
//		 "\"MobileTireRepair\":"+"\""+MobileTireRepair+"\","+
//		 "\"Diagnostics\":"+"\""+Diagnostics+"\","+
//		 "\"Vehicle_Unlocks\":"+"\""+Vehicle_Unlocks+"\","+
//		
//		 "\"Vehicle_Type\":"+"\""+Vehicle_Type+"\","+
//		 "\"Year\":"+"\""+Year+"\","+
//		 "\"Make\":"+"\""+Make+"\","+
//		 "\"Model\":"+"\""+Model+"\","+
//		 "\"Fuel_Type\":"+"\""+Fuel_Type+"\","+
//		 "\"Color\":"+"\""+Color+"\","+
//		 "\"LicensePlate\":"+"\""+LicensePlate+"\","+
//		 "\"PlateState\":"+"\""+PlateState+"\""
//		 +"}";
		return obj;
	}
	
	
	
	public String[] getVehicleInArray()
	{
		return new String[]{
				
				 vehicle_Id,
				 vehicleName,
				 Battery_Jumps,
				 Fuel_Delivery,
				 Tow_Boom,
				 Tow_FlatBed,
				 Tow_Sling,
				 Tow_WheelLift,
				 Tow_Winch,
				 Tire_Changes,
				 MobileTireRepair,
				 Diagnostics,
				 Vehicle_Unlocks,
				
				 Vehicle_Type,
				 Year,
				 Make,
				 Model,
				 Fuel_Type,
				 Color,
				 LicensePlate,
				 PlateState
				};
	}
	
}
