package businessclasses;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Vehivle_Service implements Serializable{

	BattryJump battryJump=new BattryJump();
	FuelDeliveryJump fuelDelivery=new FuelDeliveryJump();
	TowService TowServbice=new TowService();
	UnlockVehicle unlockVehicle=new UnlockVehicle();
	TireService tireService=new TireService();
	Vehicle_Diagnosis vehicleDiagnosis=new Vehicle_Diagnosis();
	
	public Boolean battryJumpStatus=false;
	public Boolean FuelDeliveryStatus=false;
	public Boolean TowServiceStatus=false;
	public Boolean UnlocakVehicleStatus=false;
	public Boolean TireServiceStatus=false;
	public Boolean VehicleDiagnosisStatus=false;
	
	int TotalAttributesCount=6;
	
	public Vehivle_Service(){}
	
	public Vehivle_Service(Parcel in){

		battryJump=(BattryJump) in.readParcelable(BattryJump.class.getClassLoader());
		fuelDelivery=(FuelDeliveryJump) in.readParcelable(FuelDeliveryJump.class.getClassLoader());
		TowServbice=(TowService) in.readParcelable(TowService.class.getClassLoader());
		unlockVehicle=(UnlockVehicle) in.readParcelable(UnlockVehicle.class.getClassLoader());
		tireService=(TireService) in.readParcelable(TireService.class.getClassLoader());
		vehicleDiagnosis=(Vehicle_Diagnosis) in.readParcelable(Vehicle_Diagnosis.class.getClassLoader());
	}
	
	public JSONObject getJSON()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("battryJump", battryJump.getJSONString());}catch(Exception k){}
		try{obj.put("fuelDelivery", fuelDelivery.getJSON());}catch(Exception k){}
		try{obj.put("TowServbice", TowServbice.getJSON());}catch(Exception k){}
		try{obj.put("unlockVehicle", unlockVehicle.getJSON());}catch(Exception k){}
		try{obj.put("tireService", tireService.getJSON());}catch(Exception k){}
		try{obj.put("vehicleDiagnosis", vehicleDiagnosis.getJSON());}catch(Exception k){}
		
//		String data="{"+
//				"\"battryJump\":"+""+((battryJump!=null)?battryJump.getJSONString():"\"null\"")+","+
//				"\"fuelDelivery\":"+""+((fuelDelivery!=null)?fuelDelivery.getJSON():"")+","+
//				"\"TowServbice\":"+""+((TowServbice!=null)?TowServbice.getJSON():"\"null\"")+","+
//				"\"unlockVehicle\":"+""+((unlockVehicle!=null)?unlockVehicle.getJSON():"\"null\"")+","+
//				"\"tireService\":"+""+((tireService!=null)?tireService.getJSON():"\"null\"")+","+
//				"\"vehicleDiagnosis\":"+""+((vehicleDiagnosis!=null)?vehicleDiagnosis.getJSON():"\"null\"")+""+
//				"}";
		return obj;
	}
	public void parseJSON(JSONObject json)
	{
	try{	battryJump.parseObjectFromJSON(json.getJSONObject("battryJump"));}catch(Exception k){}
	try{	fuelDelivery.setJSON(json.getJSONObject("fuelDelivery"));}catch(Exception k){}
	try{TowServbice.parseJSON(json.getJSONObject("TowServbice"));}catch(Exception k){}
	try{unlockVehicle.parseJSON(json.getJSONObject("unlockVehicle"));}catch(Exception k){}
	try{tireService.parseJSON(json.getJSONObject("tireService"));}catch(Exception k){}
	try{vehicleDiagnosis.parseJSON(json.getJSONObject("vehicleDiagnosis"));}catch(Exception k){}
	}
	
	
	public void setbattryJump(BattryJump val){battryJump =val;}
	public void setfuelDelivery(FuelDeliveryJump val){fuelDelivery=val;}
	public void setTowServbice(TowService val){TowServbice=val;}
	public void setunlockVehicle(UnlockVehicle val){unlockVehicle=val;}
	public void settireService(TireService val){tireService =val;}
	public void setvehicleDiagnosis(Vehicle_Diagnosis val){vehicleDiagnosis =val;}
	
	public BattryJump getbattryJump(){return battryJump;}
	public FuelDeliveryJump getfuelDelivery(){return fuelDelivery;}
	public TowService getTowServbice(){return TowServbice;}
	public UnlockVehicle getunlockVehicle(){return unlockVehicle;}
	public TireService gettireService(){return tireService;}
	public Vehicle_Diagnosis getvehicleDiagnosis(){return vehicleDiagnosis;}
	
	
}
