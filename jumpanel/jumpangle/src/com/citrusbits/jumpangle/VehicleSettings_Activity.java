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
import businessclasses.Motorist;
import businessclasses.Provider;
import businessclasses.PushApplication;
import businessclasses.Vehicle_Info;
import businessclasses.Vehivle_Service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class VehicleSettings_Activity extends Activity {
	Button AddButton=null;
	ListView VehicleMainListView=null;
	
	
	CurrentLogedInUser cu=new CurrentLogedInUser();
	
	ProgressDialog UpdateUserProgressDialog=null;
	Boolean isProvider=true;
	
	public void showDialog(String Message)
	{
		if(UpdateUserProgressDialog==null)
		{
			UpdateUserProgressDialog=new ProgressDialog(VehicleSettings_Activity.this);
		}
		
		
		UpdateUserProgressDialog.setTitle(Message);
		UpdateUserProgressDialog.setMessage("Please wait");
		UpdateUserProgressDialog.setCancelable(false);
		UpdateUserProgressDialog.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changevehicle);
		
		
		try {
			cu.parseJSON(new JSONObject(getThisSharedPreferences().getString("currentLogedUser", "")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cu.getUserCatagory().toLowerCase().contains("provider"))
		{
			isProvider=true;
		}
		else
		{
			isProvider=false;
		}
		
		initiateControls();
		initiateListners();
		loadVehiclelist();
		
	}
	public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	public void setVehicle(Vehicle_Info v)
	{
		cu.setVehicleComplete(v);
		
		SharedPreferences sp=getThisSharedPreferences();
		SharedPreferences.Editor edtr=sp.edit();
		edtr.putString("currentLogedUser", cu.getJSONString());
		edtr.commit();
		
	}
	
	public void loadVehiclelist() {
		
		Provider p=null;
		Motorist m=null;
		showDialog("Loading vehicles");
		if(isProvider)
		{
			p=(Provider)cu.getUserObject();
		}
		else
		{
			m=(Motorist)cu.getUserObject();
		}
		
		final Handler h=new Handler()
		{
			@Override
			public void handleMessage(Message m)
			{
				switch (m.what) {
				case 1:
				{
					String Response =m.obj.toString();
					try
					{
						JSONArray arr=new JSONArray(Response);
						ArrayList<Vehicle_Info> myarrayList =new ArrayList<Vehicle_Info>();
						for(int i=0; i<arr.length(); i++)
						{
							JSONObject obj=arr.getJSONObject(i);
							Vehicle_Info v=new Vehicle_Info();
							v.setvehicleName(obj.getString("vehicleName"));
							v.setVehicle_Type(obj.getString("vehicleType"));
							v.setvehicle_Id(obj.getString("vehicle_Id"));
							v.userid=(obj.getString("user_id"));
							v.name=(obj.getString("name"));
							v.setYear(obj.getString("modelYear"));
							v.setModel(obj.getString("model"));
							v.setMake(obj.getString("make"));
							v.setFuel_Type(obj.getString("fuelType"));
							v.Assign=(obj.getString("assign"));
							if(isProvider)
							{
								v.Services=(obj.getString("Battery_Jumps").contains("off"))?"":"Battery Jumps";
								v.Services+=(obj.getString("Diagnostics").contains("off"))?"":",Fuel Delivery";
								v.Services+=(obj.getString("Fuel_Delivery").contains("off"))?"":",Diagnostics";
								v.Services+=(obj.getString("MobileTireRepair").contains("off"))?"":",Mobile Tire Repair";
								v.Services+=(obj.getString("Tire_Changes").contains("off"))?"":",Tire Changes";
								v.Services+=(obj.getString("Tow_Boom").contains("off"))?"":",Tow Boom";
								v.Services+=(obj.getString("Tow_FlatBed").contains("off"))?"":",Tow FlatBed";
								v.Services+=(obj.getString("Tow_Sling").contains("off"))?"":",Tow Sling";
								v.Services+=(obj.getString("Tow_WheelLift").contains("off"))?"":",Tow WheelLift";
								v.Services+=(obj.getString("Tow_Winch").contains("off"))?"":",Tow Winch";
								v.Services+=(obj.getString("Vehicle_Unlocks").contains("off"))?"":",Vehicle Unlocks";
							}
							myarrayList.add(v);
							
						}
						
						
						
							VehicleAdapter adp=new VehicleAdapter(VehicleSettings_Activity.this, android.R.layout.simple_list_item_1,myarrayList,VehicleSettings_Activity.this,isProvider);
							VehicleMainListView.setAdapter(adp);
							adp.notifyDataSetChanged();
						
					}
					catch (Exception e) {
					}
					
					
					break;
				}
				case -1:
				{
					//Toast.makeText(getBaseContext(),m.obj.toString() , Toast.LENGTH_SHORT).show();
					VehicleAdapter adp=new VehicleAdapter(VehicleSettings_Activity.this, android.R.layout.simple_list_item_1,new ArrayList<Vehicle_Info>(),VehicleSettings_Activity.this,isProvider);
					VehicleMainListView.setAdapter(adp);
					adp.notifyDataSetChanged();
					break;
				}

			
				case 0:
				{
					Toast.makeText(getBaseContext(),"Connection Problem." , Toast.LENGTH_SHORT).show();
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
				JSONObject Response=null;
				if(isProvider)
				{
				   Response=getMyVehicle("getMyVehicle", cu.getuser_id(), cu.getmaster_id(), "1");
				}
				else
				{
					Response=getMyVehicle("getMyVehicle", cu.getuser_id(), cu.getmaster_id(), "0");
				}
				try
				{
					if(Response.getString("error").contains("false"))
					{
						Message m=new Message();
						m.what=1;
						m.obj=Response.getString("data");
						h.sendMessage(m);
					}
					else
					{
						Message m=new Message();
						m.what=-1;
						m.obj=Response.getString("message");
						h.sendMessage(m);
						
					}
				}
				catch(Exception d)
				{
					h.sendEmptyMessage(0);
				}
				
				Log.e("Response",Response.toString());
			}
		}.start();
		
	}

	private void initiateListners() {
		// TODO Auto-generated method stub
		AddButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isProvider)
				{
					Intent i=new Intent(VehicleSettings_Activity.this, Provider_Add_Vehicle_Activity.class);
					startActivity(i);
				}
				else
				{
					Intent i=new Intent(VehicleSettings_Activity.this, Motorist_Add_Vehicle_Activity.class);
					startActivity(i);
				}
			}
		});
	}
	
	private JSONObject getMyVehicle(String methodName,String user_id,String master_id,String type) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", methodName));
	        arr.add(new BasicNameValuePair("user_id", user_id));
	        arr.add(new BasicNameValuePair("master_id", master_id));
	        arr.add(new BasicNameValuePair("type", type));
	        
	        
	        
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
	
	
	private void initiateControls() {
		
		AddButton=(Button)findViewById(R.id.AddButton);
		VehicleMainListView=(ListView)findViewById(R.id.VehicleMainListView);
	}

	
	
}
