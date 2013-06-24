package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle_Diagnosis implements Serializable{

	Boolean GasolineVehicles=false;
	Boolean DesielVehicles=false;
	Boolean CNGVehicles=false;
	Boolean HybridVehicles=false;
	
	Boolean ApplyVehicleOnly=false;
	
	static int TotalAttributesCount=5;
	
	
	
	public Vehicle_Diagnosis(){}
	
	public Vehicle_Diagnosis(Parcel in)
	{
		boolean[] arr=new boolean[TotalAttributesCount];
		in.readBooleanArray(arr);
		fetchDatafromArray(arr);
	}
	public void fetchDatafromArray(boolean[] arr)
	{
		GasolineVehicles=arr[0];
		DesielVehicles=arr[1];
		CNGVehicles=arr[2];
		HybridVehicles=arr[3];
		ApplyVehicleOnly=arr[4];
		
	}
	public String getDiagnosisParameter()
	{
		String data="";
		if(GasolineVehicles)data+="Gasoline Vehicles";
		if(DesielVehicles)data +=",Diesel Vehicles";
		if(CNGVehicles)data +=",CNG Vehicles";
		if(HybridVehicles)data +=",Hybrid Vehicles";
		if(data.charAt(0)==',')
		{
			data=data.substring(1);
		}
		return data;
	}
	
	public JSONObject getJSON()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("GasolineVehicles", GasolineVehicles);}catch(Exception k){}
		try{obj.put("DesielVehicles", DesielVehicles);}catch(Exception k){}
		try{obj.put("CNGVehicles", CNGVehicles);}catch(Exception k){}
		try{obj.put("HybridVehicles", HybridVehicles);}catch(Exception k){}
		try{obj.put("ApplyVehicleOnly", ApplyVehicleOnly);}catch(Exception k){}
//		
//		String data="{"+
//				"\"GasolineVehicles\":"+"\""+GasolineVehicles+"\","+
//				"\"DesielVehicles\":"+"\""+DesielVehicles+"\","+
//				"\"CNGVehicles\":"+"\""+CNGVehicles+"\","+
//				"\"HybridVehicles\":"+"\""+HybridVehicles+"\","+
//				"\"ApplyVehicleOnly\":"+"\""+ApplyVehicleOnly+"\""
//				+"}";
		return obj;
	}
	public void parseJSON(JSONObject json)
	{
		try{GasolineVehicles=json.getBoolean("GasolineVehicles");}catch(Exception k){}
		try{DesielVehicles=json.getBoolean("DesielVehicles");}catch(Exception k){}
		try{CNGVehicles=json.getBoolean("CNGVehicles");}catch(Exception k){}
		try{HybridVehicles=json.getBoolean("HybridVehicles");}catch(Exception k){}
		try{ApplyVehicleOnly=json.getBoolean("ApplyVehicleOnly");}catch(Exception k){}
	}
	
	public Boolean getGasolineVehicles(){return GasolineVehicles;}
	public Boolean getDesielVehicles(){return DesielVehicles;}
	public Boolean getCNGVehicles(){return CNGVehicles;}
	public Boolean getApplyVehicleOnly(){return ApplyVehicleOnly;}
	
	public Boolean HybridVehicles(){return HybridVehicles;}
	
	
	public void setGasolineVehicles(Boolean val){ GasolineVehicles=val;}
	public void setDesielVehicles(Boolean val){ DesielVehicles=val;}
	public void setCNGVehicles(Boolean val){ CNGVehicles=val;}
	public void setHybridVehicles(Boolean val){HybridVehicles=val;}
	public void setApplyVehicleOnly(Boolean val){ApplyVehicleOnly=val;}
	
	public boolean[] getAttributeArray()
	{
		return new boolean[]{GasolineVehicles,DesielVehicles,CNGVehicles,HybridVehicles,ApplyVehicleOnly};
	}
	
}
