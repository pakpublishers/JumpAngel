package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TireService implements Serializable{

	BattryJump TireChanges=null;
	BattryJump MobileTireRepair=null;
	
	Boolean ApplyVehicleOnly=false;
	
	int totalAttributesCount=2;
	
	
	public TireService(){}
	
	public void setApplyVehicleOnly(Boolean val){ApplyVehicleOnly=val;}
	public Boolean getApplyVehicleOnly(){return ApplyVehicleOnly;}
	
	public BattryJump getTireChanges(){return TireChanges;}
	public BattryJump getMobileTireRepair(){return MobileTireRepair;}
	
	public void setTireChanges(BattryJump val){TireChanges=val;}
	public void setMobileTireRepair(BattryJump val){MobileTireRepair=val;}
	
	public TireService(Parcel in)
	{
		Parcelable[] arr=new Parcelable[totalAttributesCount];
		arr= in.readParcelableArray(BattryJump.class.getClassLoader());
		readAttributesFromArray(arr);
		
	}
	public void readAttributesFromArray(Parcelable[] arr)
	{
		TireChanges=(BattryJump)arr[0];
		MobileTireRepair=(BattryJump)arr[1];
	}
	
	public JSONObject getJSON()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("TireChanges", TireChanges.getJSONString());}catch(Exception k){}
		try{obj.put("MobileTireRepair", MobileTireRepair.getJSONString());}catch(Exception k){}
		
//		String data="{"+
//				"\"TireChanges\":"+""+((TireChanges!=null)?TireChanges.getJSONString():"")+","+
//				"\"MobileTireRepair\":"+""+((MobileTireRepair!=null)?MobileTireRepair.getJSONString():"")+""+
//				"}";
		return obj;
	}
	
	public void parseJSON(JSONObject json)
	{
		try{TireChanges.parseObjectFromJSON(json.getJSONObject("TireChanges"));}catch(Exception k){}
		try{MobileTireRepair.parseObjectFromJSON(json.getJSONObject("MobileTireRepair"));}catch(Exception k){}
	}
	
	public BattryJump[] getArray()
	{
		return new BattryJump[]{TireChanges,MobileTireRepair};
	}
	
	

}
