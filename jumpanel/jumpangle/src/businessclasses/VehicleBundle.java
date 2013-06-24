package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class VehicleBundle implements Serializable {

	Vehicle_Info _Vehivle_Infor=null;
	Vehivle_Service _Vehicle_Service=null;
	
	public VehicleBundle(){
		
		_Vehicle_Service=new Vehivle_Service();
		_Vehivle_Infor=new Vehicle_Info();
	}
	public VehicleBundle(Parcel in)
	{
		_Vehivle_Infor= in.readParcelable(Vehicle_Info.class.getClassLoader());
		_Vehicle_Service=in.readParcelable(Vehivle_Service.class.getClassLoader());
	}
	
	public void set_Vehivle_Infor(Vehicle_Info val){_Vehivle_Infor=val;}
	public void set_Vehicle_Service(Vehivle_Service val){_Vehicle_Service=val;}
	
	public Vehicle_Info get_Vehivle_Infor(){return _Vehivle_Infor;}
	public Vehivle_Service get_Vehicle_Service(){return _Vehicle_Service;}
	
	public JSONObject getJSON()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("_Vehivle_Infor", _Vehivle_Infor.getJSONString());}catch(Exception k){}
		try{obj.put("_Vehicle_Service", _Vehicle_Service.getJSON());}catch(Exception k){}
		
//		String data="{"+
//			"\"_Vehivle_Infor\":"+""+((_Vehivle_Infor!=null)?_Vehivle_Infor.getJSONString():"")+","+
//			"\"_Vehicle_Service\":"+""+((_Vehicle_Service!=null)?_Vehicle_Service.getJSON():"")+""+
//		"}";
		return obj;
	}
	public void parseJSON(JSONObject json)
	{
		try{_Vehivle_Infor.parseObjectFromJSON(json.getJSONObject("_Vehivle_Infor"));}catch(Exception k){}
		try{_Vehicle_Service.parseJSON(json.getJSONObject("_Vehicle_Service"));}catch(Exception k){}
	}
	
}
