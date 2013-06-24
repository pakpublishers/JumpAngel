package com.citrusbits.jumpangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import businessclasses.CurrentLogedInUser;
import businessclasses.EventHistoryAdapter;
import businessclasses.EventHistoryDetails;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class EventHistory_Activity extends Activity{

	ListView EventHistoryListView=null;
	CurrentLogedInUser cu=new CurrentLogedInUser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventhistory);
		
		try {
			cu.parseJSON(new JSONObject(getThisSharedPreferences().getString("currentLogedUser", "")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initiateControls();
		initiateListners();
		
	}

	ProgressDialog UpdateUserProgressDialog=null;
	public void showDialog()
	{
		UpdateUserProgressDialog=new ProgressDialog(EventHistory_Activity.this);
		UpdateUserProgressDialog.setTitle("Updateing record");
		UpdateUserProgressDialog.setMessage("Please wait");
		UpdateUserProgressDialog.setCancelable(false);
		UpdateUserProgressDialog.show();
	}
public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	private void initiateListners() {
		
		showDialog();
		final Handler h =new Handler()
		{
			@Override
			public void handleMessage(Message m)
			{
				switch (m.what) {
				case 1:
				{
					UpdateUserProgressDialog.cancel();
					
					
						ArrayList<EventHistoryDetails> obj=(ArrayList<EventHistoryDetails>)m.obj;
						EventHistoryAdapter adp=new EventHistoryAdapter(getBaseContext(), android.R.layout.simple_list_item_1, obj, EventHistory_Activity.this);
						EventHistoryListView.setAdapter(adp);
						adp.notifyDataSetChanged();
						UpdateUserProgressDialog.cancel();
					
					
					break;
				}
				case -1:
				{
					UpdateUserProgressDialog.cancel();
					Toast.makeText(EventHistory_Activity.this, m.obj.toString(), Toast.LENGTH_SHORT).show();
					break;
				}
				default:
					break;
				}
				UpdateUserProgressDialog.cancel();
			}
		};
		
		new Thread()
		{
			@Override
			public void run()
			{
				String masterid=cu.getmaster_id();
				
				JSONObject Response=transactionHistory("transactionHistory", cu.getuser_id());
				Log.e("assign Response",Response.toString());
				try
				{
					if(Response.getString("error").contains("false"))
					{
						Message m=new Message();
						m.what=1;
						JSONArray arr=Response.getJSONArray("data");
						
						ArrayList<EventHistoryDetails> obj=new ArrayList<EventHistoryDetails>();
						for(int i=0; i<arr.length(); i++)
						{
							JSONObject jobj=arr.getJSONObject(i);
							EventHistoryDetails e=new EventHistoryDetails();
							
							try{e.amount=jobj.getString("amount");}catch(Exception k){}
							try{e.angel_dispute=jobj.getString("angel_dispute");}catch(Exception k){}
							try{e.angel_name=jobj.getString("angel_name");}catch(Exception k){}
							try{e.angel_reason=jobj.getString("angel_reason");}catch(Exception k){}
							try{e.angel_user_id=jobj.getString("angel_user_id");}catch(Exception k){}
							try{e.arrivaltime=jobj.getString("arrivaltime");}catch(Exception k){}
							try{e.color=jobj.getString("color");}catch(Exception k){}
							try{e.completedORcancelled_time=jobj.getString("completedORcancelled_time");}catch(Exception k){}
							try{e.fuelType=jobj.getString("fuelType");}catch(Exception k){}
							try{e.licensePlate=jobj.getString("licensePlate");}catch(Exception k){}
							try{e.make=jobj.getString("make");}catch(Exception k){}
							try{e.message=jobj.getString("message");}catch(Exception k){}
							try{e.model=jobj.getString("model");}catch(Exception k){}
							try{e.modelYear=jobj.getString("modelYear");}catch(Exception k){}
							try{e.motorist_name=jobj.getString("motorist_name");}catch(Exception k){}
							try{e.motorist_reason=jobj.getString("motorist_reason");}catch(Exception k){}
							try{e.motorist_user_id=jobj.getString("motorist_user_id");}catch(Exception k){}
							try{e.rating=jobj.getString("rating");}catch(Exception k){}
							try{e.reponsetime=jobj.getString("reponsetime");}catch(Exception k){}
							try{e.senting_time=jobj.getString("senting_time");}catch(Exception k){}
							try{e.service=jobj.getString("service");}catch(Exception k){}
							try{e.transaction_id=jobj.getString("transaction_id");}catch(Exception k){}
							try{e.vehicleName=jobj.getString("vehicleName");}catch(Exception k){}
							try{e.vehicleType=jobj.getString("vehicleType");}catch(Exception k){}
							
							try{e.service=getResources().getStringArray(R.array.Services_Array)[Integer.parseInt(e.service)];}catch(Exception k){}
							
							obj.add(e);
							
						}
						
						m.obj=obj;
						h.sendMessage(m);
					}
					else
					{
						Message m=new Message();
						m.what=-1;
						m.obj=Response.getString("message");
						h.sendMessage(m);
					}
				}catch(Exception k){}
			}
		}.start();
		
	}

	private void initiateControls() {
		EventHistoryListView=(ListView)findViewById(R.id.EventHistoryListView);
	}
	private JSONObject transactionHistory(String methodName,String user_id) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", methodName));
	        
	        arr.add(new BasicNameValuePair("user_id", user_id));
	        
	        
	        
	        
	        httpGet.setEntity(new UrlEncodedFormEntity(arr));
	        StringBuilder builder=new StringBuilder();
		        try {
		        	
		            HttpResponse response = client.execute(httpGet);
		            StatusLine statusLine = response.getStatusLine();
		            int statusCode = statusLine.getStatusCode();
		            if (statusCode == 200) {
		                HttpEntity entity = response.getEntity();
		                
		               // String data=EntityUtils.toString(entity);
		                InputStream content = entity.getContent();
		                BufferedReader reader = new BufferedReader(
		                        new InputStreamReader(content));
		                String line;
		                while ((line = reader.readLine()) != null) {
		                    builder.append(line);
		                }
		            } else {
	
		            }
		        } catch (ClientProtocolException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        Log.e("regresponse",""+builder.toString());
		        return new JSONObject(builder.toString());
		        
	        }catch (Exception e) {
	        	int k=0;
			}
	        return null;
		
	}
	
}
