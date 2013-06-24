package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class FuelDeliveryJump implements Serializable{

	Boolean Gasoline_Pertol=false;
	Boolean Diesel=false;
	Boolean CNG=false;
	
	Boolean ApplyVehicleOnly=false;
	
	static int TotalAttributesCount=4;
	
	
	
	public FuelDeliveryJump(){}
	
	public String getFuelDileveryParameter()
	{
		String data="";
		if(Gasoline_Pertol)data+="Gasoline/Petrol";
		if(Diesel)data +=",Diesel";
		if(CNG)data +=",CNG";
		if(data.charAt(0)==',')
		{
			data=data.substring(1);
		}
		return data;
	}
	
	public FuelDeliveryJump(Parcel in)
	{
		boolean[] arr=new boolean[TotalAttributesCount];
		in.readBooleanArray(arr);
		fetchDatafromArray(arr);
	}
	public void fetchDatafromArray(boolean[] arr)
	{
		Gasoline_Pertol=arr[0];
		Diesel=arr[1];
		CNG=arr[2];
		ApplyVehicleOnly=arr[3];
		
	}
	
	public JSONObject getJSON()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("Gasoline_Pertol", Gasoline_Pertol);}catch(Exception k){}
		try{obj.put("Diesel", Diesel);}catch(Exception k){}
		try{obj.put("CNG", CNG);}catch(Exception k){}
		try{obj.put("ApplyVehicleOnly", ApplyVehicleOnly);}catch(Exception k){}
//		
//		String data="{"+
//				"\"Gasoline_Pertol\":\""+Gasoline_Pertol+"\","+
//				"\"Diesel\":\""+Diesel+"\","+
//				"\"CNG\":\""+CNG+"\","+
//				"\"ApplyVehicleOnly\":\""+ApplyVehicleOnly+"\""
//					+"}";
		return obj;
	}
	
	public void setJSON(JSONObject json)
	{
		try{Gasoline_Pertol=Boolean.parseBoolean(json.getString("Gasoline_Pertol"));}catch(Exception g){}
		try{Diesel=Boolean.parseBoolean(json.getString("Diesel"));}catch(Exception g){}
		try{CNG=Boolean.parseBoolean(json.getString("CNG"));}catch(Exception g){}
		try{ApplyVehicleOnly=Boolean.parseBoolean(json.getString("ApplyVehicleOnly"));}catch(Exception g){}
	}
	
	public Boolean getGasoline_Pertol(){return Gasoline_Pertol;}
	public Boolean getDiesel(){return Diesel;}
	public Boolean getCNG(){return CNG;}
	public Boolean getApplyVehicleOnly(){return ApplyVehicleOnly;}
	
	public void setGasoline_Pertol(Boolean val){ Gasoline_Pertol=val;}
	public void setDiesel(Boolean val){ Diesel=val;}
	public void setCNG(Boolean val){ CNG=val;}
	public void setApplyVehicleOnly(Boolean val){ApplyVehicleOnly=val;}
	
	public boolean[] getAttributeArray()
	{
		return new boolean[]{Gasoline_Pertol,Diesel,CNG,ApplyVehicleOnly};
	}
	
}
