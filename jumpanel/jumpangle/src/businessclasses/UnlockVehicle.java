package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class UnlockVehicle implements Serializable {

	Boolean ApplytoVahicleOnly=false;
	static int TotalAttributeCount=1;
	
	public UnlockVehicle(){}
	
	public UnlockVehicle(Parcel in)
	{
		boolean[] arr=new boolean[TotalAttributeCount];
		in.readBooleanArray(arr);
		readAttributesFromArray(arr);
	}
	
	public void readAttributesFromArray(boolean[] arr)
	{
		ApplytoVahicleOnly=arr[0];
	}
	
	public JSONObject getJSON()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("ApplytoVahicleOnly", ApplytoVahicleOnly);}catch(Exception k){}
		
//		String data="{"+
//				"\"ApplytoVahicleOnly\":"+"\""+ApplytoVahicleOnly+"\""+
//				"}";
		return obj;
	}
	
	public void parseJSON(JSONObject json)
	{
		
		try{ApplytoVahicleOnly=Boolean.parseBoolean(json.getString("ApplytoVahicleOnly"));}catch(Exception k){}
	}
	
	public Boolean getApplytoVahicleOnly(){return ApplytoVahicleOnly;}
	public void setApplytoVahicleOnly(Boolean val){ApplytoVahicleOnly=val;}
	
	 
	public boolean[] getAttributeArray()
	{
		return new boolean[]{ApplytoVahicleOnly};
	}
}
