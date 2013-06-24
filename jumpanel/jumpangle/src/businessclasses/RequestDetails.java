package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

public class RequestDetails implements Serializable {

	String request_id=null;
	String motorist_id=null;
	String master_id=null;
	String angel_id=null;
	String senting_time=null;
	String accept_decline_time=null;
	String message=null;
	String service=null;
	String reponsetime=null;
	String arrivaltime=null;
	String amount=null;
	String request_authorize=null;
	String push=null;
	String request_status=null;
	String cancelledby=null;
	
	public String getrequest_id(){return request_id;}
	public String getmotorist_id(){return motorist_id;}
	public String getmaster_id(){return master_id;}
	public String getangel_id(){return angel_id;}
	public String getsenting_time(){return senting_time;}
	public String getaccept_decline_time(){return accept_decline_time;}
	public String getmessage(){return message;}
	public String getservice(){return service;}
	public String getreponsetime(){return reponsetime;}
	public String getarrivaltime(){return arrivaltime;}
	public String getamount(){return amount;}
	public String getrequest_authorize(){return request_authorize;}
	public String getpush(){return push;}
	public String getrequest_status(){return request_status;}
	public String getcancelledby(){return cancelledby;}
	
	public void setrequest_id(String val){request_id=val;}
	public void setmotorist_id(String val){motorist_id=val;}
	public void setmaster_id(String val){master_id=val;}
	public void setangel_id(String val){angel_id=val;}
	public void setsenting_time(String val){senting_time=val;}
	public void setaccept_decline_time(String val){accept_decline_time=val;}
	public void setmessage(String val){message=val;}
	public void setservice(String val){service=val;}
	public void setreponsetime(String val){reponsetime=val;}
	public void setarrivaltime(String val){arrivaltime=val;}
	public void setamount(String val){amount=val;}
	public void setrequest_authorize(String val){request_authorize=val;}
	public void setpush(String val){push=val;}
	public void setrequest_status(String val){request_status=val;}
	public void setcancelledby(String val){cancelledby=val;}
	
	public String getJsonString()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("request_id", request_id);}catch(Exception d){}
		try{obj.put("motorist_id", motorist_id);}catch(Exception d){}
		try{obj.put("master_id", master_id);}catch(Exception d){}
		try{obj.put("angel_id", angel_id);}catch(Exception d){}
		try{obj.put("senting_time", senting_time);}catch(Exception d){}
		try{obj.put("accept_decline_time", accept_decline_time);}catch(Exception d){}
		try{obj.put("message", message);}catch(Exception d){}
		try{obj.put("service", service);}catch(Exception d){}
		try{obj.put("reponsetime", reponsetime);}catch(Exception d){}
		try{obj.put("arrivaltime", arrivaltime);}catch(Exception d){}
		try{obj.put("amount", amount);}catch(Exception d){}
		try{obj.put("request_authorize", request_authorize);}catch(Exception d){}
		try{obj.put("push", push);}catch(Exception d){}
		try{obj.put("request_status", request_status);}catch(Exception d){}
		try{obj.put("cancelledby", cancelledby);}catch(Exception d){}
		
		return obj.toString();
		
	}
	
	public void parseJSON(JSONObject obj)
	{
		
		try{request_id=obj.getString("request_id");}catch(Exception ko){}
		try{motorist_id=obj.getString("motorist_id");}catch(Exception ko){}
		try{master_id=obj.getString("master_id");}catch(Exception ko){}
		try{angel_id=obj.getString("angel_id");}catch(Exception ko){}
		try{senting_time=obj.getString("senting_time");}catch(Exception ko){}
		try{accept_decline_time=obj.getString("accept_decline_time");}catch(Exception ko){}
		try{message=obj.getString("message");}catch(Exception ko){}
		try{service=obj.getString("service");}catch(Exception ko){}
		try{reponsetime=obj.getString("reponsetime");}catch(Exception ko){}
		try{arrivaltime=obj.getString("arrivaltime");}catch(Exception ko){}
		try{amount=obj.getString("amount");}catch(Exception ko){}
		try{request_authorize=obj.getString("request_authorize");}catch(Exception ko){}
		try{push=obj.getString("push");}catch(Exception ko){}
		try{request_status=obj.getString("request_status");}catch(Exception ko){}
		try{cancelledby=obj.getString("cancelledby");}catch(Exception ko){}
		
		
	}
	
	
}
