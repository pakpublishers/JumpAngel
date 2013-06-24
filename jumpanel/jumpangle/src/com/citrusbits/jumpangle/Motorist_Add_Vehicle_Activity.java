package com.citrusbits.jumpangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

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
import businessclasses.PushApplication;
import businessclasses.Vehicle_Info;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Motorist_Add_Vehicle_Activity extends Activity{

	Button AddVehicleCancelButton=null;
//	Button AddVehicleSaveandAddAnotherButton=null;
	Button AddVehicleSaveButton=null;
	EditText AddVehicleVehicleName=null;
	Spinner AddVehicleVehicleType=null;
	Spinner AddVehicleVehicleYear=null;
	EditText AddVehicleVehicleMake=null;
	EditText AddVehicleVechicleModel=null;
	Spinner AddVehicleVehicleFuel=null;
	EditText AddVehicleVehicleColor=null;
	EditText AddVehicleVehicleLicencePlate=null;
	Spinner AddVehicleMotoristPlateState=null;
	
	CurrentLogedInUser cu=new CurrentLogedInUser();
	
	Motorist MotoristForRegistration=null;
	ProgressDialog UpdateUserProgressDialog=null;
	public void showDialog(String Message)
	{
		UpdateUserProgressDialog=new ProgressDialog(Motorist_Add_Vehicle_Activity.this);
		UpdateUserProgressDialog.setTitle(Message);
		UpdateUserProgressDialog.setMessage("Please wait");
		UpdateUserProgressDialog.setCancelable(false);
		UpdateUserProgressDialog.show();
	}
	
	
	Vehicle_Info vi=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motorist_add_vehicle_view);
		
		Bundle PassedMotorist= getIntent().getExtras();
		if(PassedMotorist!=null &&  PassedMotorist.containsKey("vehicleforEdit"))
		vi=(Vehicle_Info)PassedMotorist.get("vehicleforEdit");
		
		try {
			cu.parseJSON(new JSONObject(getThisSharedPreferences().getString("currentLogedUser", "")));
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initiateControls();
		initiateListners();
		
		String[] arr=getResources().getStringArray(R.array.Year);
		
		AddVehicleVehicleYear.setSelection(arr.length-1);
		
		if(vi!=null)
		{
			getIntent().getExtras().clear();
			setattributes();
		}
		
	}

private void setattributes() {
	
	showDialog("Loading Vehicle informations");
	
			final Handler h =new Handler()
			{
				@Override
				public void handleMessage(Message m)
				{
					switch (m.what) {
					case 1:
					{
						try {
							JSONObject obj=new JSONObject(m.obj.toString());
							vi.Assign=obj.getString("assign");
							vi.setModel(obj.getString("model"));
							vi.setColor(obj.getString("color"));
							vi.setFuel_Type(obj.getString("fuelType"));
							vi.setvehicleName(obj.getString("vehicleName"));
							vi.setModel(obj.getString("modelYear"));
							vi.setVehicle_Type(obj.getString("vehicleType"));
							vi.setvehicle_Id(obj.getString("vehicle_Id"));
							vi.userid=(obj.getString("user_id"));
							//vi.(obj.getString("type"));
							vi.setMake(obj.getString("make"));
							vi.setLicensePlate(obj.getString("licensePlate"));
							vi.setPlateState(obj.getString("licensePlate_state"));

							AddVehicleVechicleModel.setText(vi.getModel());
							AddVehicleVehicleColor.setText(vi.getColor());
							AddVehicleVehicleLicencePlate.setText(vi.getLicensePlate());
							AddVehicleVehicleMake.setText(vi.getMake());
							AddVehicleVehicleName.setText(vi.getvehicleName());
							
							ArrayList<String> typearr=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.vehicletype)));
							ArrayList<String> yeararr=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Year)));
							ArrayList<String> fueltypearr=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.fueltype)));
							ArrayList<String> statearr=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.state)));
//							
							int one=statearr.indexOf(vi.getPlateState().toString());
							int two=fueltypearr.indexOf(vi.getFuel_Type().toString());
							int three=typearr.indexOf(vi.getVehicle_Type().toString());
							int four=yeararr.indexOf(vi.getYear().toString());
							
							AddVehicleMotoristPlateState.setSelection(one);
							AddVehicleVehicleFuel.setSelection(two);
							AddVehicleVehicleType.setSelection(three);
							AddVehicleVehicleYear.setSelection(four);
							
							UpdateUserProgressDialog.cancel();
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
					case -1:
					{
						UpdateUserProgressDialog.cancel();
						Toast.makeText(Motorist_Add_Vehicle_Activity.this, m.obj.toString(), Toast.LENGTH_SHORT).show();
						break;
					}
					default:
						break;
					}
				}
			};
			
			new Thread()
			{
				@Override
				public void run()
				{
					
					JSONObject Response=getSingleVehicle("getSingleVehicle", vi.getvehicle_Id(), "0");
					Log.e("Signle vehicle Response",Response.toString());
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
					}catch(Exception k){}
				}
			}.start();
	
	
		
	}

public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}

	private void initiateListners() {
	
		AddVehicleSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(vi==null)
				{
				
					
					if(AddVehicleVehicleName.getText().toString().trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Vehicle Name", Toast.LENGTH_LONG).show();
						return;
					}
					if(AddVehicleVehicleMake.getText().toString().trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Vehicle Make", Toast.LENGTH_LONG).show();
						return;
					}
					if(AddVehicleVechicleModel.getText().toString().trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Vehicle Model", Toast.LENGTH_LONG).show();
						return;
					}
					if(AddVehicleVehicleColor.getText().toString().trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Vehicle Color", Toast.LENGTH_LONG).show();
						return;
					}
					if(AddVehicleVehicleLicencePlate.getText().toString().trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Vehicle License Plate", Toast.LENGTH_LONG).show();
						return;
					}
					
				final Vehicle_Info v=new Vehicle_Info();
				showDialog("Saving Vehicle");
				v.setvehicleName(AddVehicleVehicleName.getText().toString());
				v.setVehicle_Type(AddVehicleVehicleType.getSelectedItem().toString());
				v.setYear(AddVehicleVehicleYear.getSelectedItem().toString());
				v.setMake(AddVehicleVehicleMake.getText().toString());
				v.setModel(AddVehicleVechicleModel.getText().toString());
				v.setFuel_Type(AddVehicleVehicleFuel.getSelectedItem().toString());
				v.setColor(AddVehicleVehicleColor.getText().toString());
				v.setLicensePlate(AddVehicleVehicleLicencePlate.getText().toString());
				v.setPlateState(AddVehicleMotoristPlateState.getSelectedItem().toString());
				
				
				final Handler h =new Handler()
				{
					@Override
					public void handleMessage(Message m)
					{
						switch (m.what) {
						case 1:
						{
							
							SharedPreferences sp=getThisSharedPreferences();
							SharedPreferences.Editor edtr=sp.edit();
							edtr.putString("currentLogedUser", cu.getJSONString());
							edtr.commit();
							
							UpdateUserProgressDialog.cancel();
							Toast.makeText(Motorist_Add_Vehicle_Activity.this, m.obj.toString(), Toast.LENGTH_SHORT).show();
							if(m.obj.toString().toLowerCase().contains("successful"))
							{
								Intent i=new Intent(Motorist_Add_Vehicle_Activity.this, MainActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								
								startActivity(i);
							}
							
							break;
						}
						case -1:
						{
							UpdateUserProgressDialog.cancel();
							Toast.makeText(Motorist_Add_Vehicle_Activity.this, m.obj.toString(), Toast.LENGTH_SHORT).show();
							break;
						}
						default:
							break;
						}
					}
				};
				
				new Thread()
				{
					@Override
					public void run()
					{
						
						JSONObject Response=addVehicle("addVehicle", cu.getuser_id(), v.getvehicleName(), v.getVehicle_Type(), v.getYear(), v.getMake(), v.getModel(), v.getFuel_Type(), v.getColor(), v.getLicensePlate(), v.getPlateState(),"0");
						JSONObject Response2=null;
						if(cu.getUserCatagory().toLowerCase().contains("provider"))
						{
							Response2=getMyVehicle("getMyVehicle", cu.getuser_id(), cu.getmaster_id(), "1");
						}
						else
						{
							Response2=getMyVehicle("getMyVehicle", cu.getuser_id(), cu.getmaster_id(), "0");
						}
						Log.e("delete Response",Response.toString());
						try
						{
							
							JSONArray arr=Response2.getJSONArray("data");
							Boolean MakeCurrentVehicle=false;
							if(arr.length()==1)
							{
								MakeCurrentVehicle=true;
							}
							
							if(Response.getString("error").contains("false"))
							{
								JSONObject obj=Response.getJSONObject("data");
								
								Vehicle_Info vi=new Vehicle_Info();
								vi.setModel(obj.getString("model"));
								vi.setColor(obj.getString("color"));
								vi.setFuel_Type(obj.getString("fuelType"));
								vi.setvehicleName(obj.getString("vehicleName"));
								vi.setModel(obj.getString("modelYear"));
								vi.setVehicle_Type(obj.getString("vehicleType"));
								vi.setvehicle_Id(obj.getString("vehicle_Id"));
								//vi.setFuel_Type(Response.getString("user_id"));
								//vi.set(Response.getString("type"));
								vi.setMake(obj.getString("make"));
								vi.setLicensePlate(obj.getString("licensePlate"));
								vi.setPlateState(obj.getString("licensePlate_state"));
								
								
								if(MakeCurrentVehicle)
								{
									cu.setVehicleComplete(vi);
								}
								
								Message m=new Message();
								m.what=1;
								m.obj=Response.getString("message");
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
				else
				{
					showDialog("Updating Vehicle");
					final Handler h =new Handler()
					{
						@Override
						public void handleMessage(Message m)
						{
							switch (m.what) {
							case 1:
							{
								UpdateUserProgressDialog.cancel();
								Toast.makeText(Motorist_Add_Vehicle_Activity.this, m.obj.toString(), Toast.LENGTH_SHORT).show();
								if(m.obj.toString().toLowerCase().contains("successful"))
								{
									Intent i=new Intent(Motorist_Add_Vehicle_Activity.this, MainActivity.class);
									i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									
									startActivity(i);
								}
								
								break;
							}
							case -1:
							{
								UpdateUserProgressDialog.cancel();
								Toast.makeText(Motorist_Add_Vehicle_Activity.this, m.obj.toString(), Toast.LENGTH_SHORT).show();
								break;
							}
							default:
								break;
							}
						}
					};
					
					new Thread()
					{
						@Override
						public void run()
						{
							
							vi.setvehicleName(AddVehicleVehicleName.getText().toString());
							vi.setVehicle_Type(AddVehicleVehicleType.getSelectedItem().toString());
							vi.setYear(AddVehicleVehicleYear.getSelectedItem().toString());
							vi.setMake(AddVehicleVehicleMake.getText().toString());
							vi.setModel(AddVehicleVechicleModel.getText().toString());
							vi.setFuel_Type(AddVehicleVehicleFuel.getSelectedItem().toString());
							vi.setColor(AddVehicleVehicleColor.getText().toString());
							vi.setLicensePlate(AddVehicleVehicleLicencePlate.getText().toString());
							vi.setPlateState(AddVehicleMotoristPlateState.getSelectedItem().toString());
							
							JSONObject Response=editSingleVehicle("editSingleVehicle",vi.getvehicle_Id(), vi.getvehicleName(), vi.getVehicle_Type(), vi.getYear(), vi.getMake(), vi.getModel(), vi.getFuel_Type(), vi.getColor(), vi.getLicensePlate(), vi.getPlateState());
							Log.e("delete Response",Response.toString());
							try
							{
								if(Response.getString("error").contains("false"))
								{
									Message m=new Message();
									m.what=1;
									m.obj=Response.getString("message");
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
//				JSONObject Response = saveMotorist(MotoristForRegistration,v);
//				try {
//					
//					if(Response!=null && Response.getString("error").equals("false"))
//					{
//						Toast.makeText(Motorist_Add_Vehicle_Activity.this, Response.getString("message"), Toast.LENGTH_LONG).show();
//						JSONObject ResponseData=Response.getJSONObject("data");
//						CurrentLogedInUser cu=new CurrentLogedInUser();
//						cu.setuser_id(ResponseData.getString("user_id"));
//						cu.setvehicle_id(ResponseData.getString("vehicle_Id"));
//						cu.settoken(ResponseData.getString("token"));
//						
//						Motorist m=new Motorist();
//						m.setFirstName(ResponseData.getString("f_name"));
//						m.setLastName(ResponseData.getString("l_name"));
//						m.setCellNumber(ResponseData.getString("phone"));
//						m.setEMailAddress(ResponseData.getString("email"));
//						m.setIEmergencyN(ResponseData.getString("emergency_no"));
//						m.setWePayID(ResponseData.getString("paypal_username"));
//						m.setcreaditcard_no(ResponseData.getString("creditcard_no"));
//						m.setExpiryDate(ResponseData.getString("creditcard_enddate"));
//						m.setThreeDigitSecurityCode(ResponseData.getString("threeDigitCode"));
//						// replacing type with userCatagory
//						cu.setUserDetails("motorist", (Object)m);
//						
//						Vehicle_Info vi=new Vehicle_Info();
//						vi.setvehicleName(ResponseData.getString("vehicleName"));
//						vi.setVehicle_Type(ResponseData.getString("vehicleType"));
//						vi.setYear(ResponseData.getString("modelYear"));
//						vi.setMake(ResponseData.getString("make"));
//						vi.setModel(ResponseData.getString("model"));
//						vi.setFuel_Type(ResponseData.getString("fuelType"));
//						vi.setColor(ResponseData.getString("color"));
//						vi.setLicensePlate(ResponseData.getString("licensePlate"));
//						try{
//						vi.setPlateState(ResponseData.getString("status"));}catch (Exception e) {}
//						cu.setVehicleComplete(vi);
//						
//						Intent MainActivityintent=new Intent(Motorist_Add_Vehicle_Activity.this, MainActivity.class);
//						MainActivityintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						
//						//MainActivityintent.putExtra("ResponseUser", cu);
//						//Bundle b=new Bundle();
//						//b.putParcelable("motorist", m);
//						//b.putParcelable("vehicle", vi);
//						
//					//	MainActivityintent.putExtra("data", b);
//						//MainActivityintent.putExtra("data", cu);
//						//ArrayList<CurrentLogedInUser> arr=new ArrayList<CurrentLogedInUser>();
//						//arr.add(cu);
//						
//						Bundle b=new Bundle();
//						b.putSerializable("currentuser", cu);
//						MainActivityintent.putExtra("cu", b);
//						
//					//	MainActivityintent.putExtra("BackFromMororistRegistration", "");
//						startActivity(MainActivityintent);
//						
//					}
			
					
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}
		});
		AddVehicleCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(Motorist_Add_Vehicle_Activity.this, Registration_Choice_Activity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}

			
		});
	}

	private JSONObject getSingleVehicle(String methodName,String vehicle_Id,String type
			) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", methodName));
	        arr.add(new BasicNameValuePair("vehicle_Id", vehicle_Id));
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
	
	private JSONObject addVehicle(String methodName,String user_id,String vehicleName,String vehicleType,
			String modelYear,String make,String model,String fuelType,String color,String licensePlate,String licensePlate_state,String type
			) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", methodName));
	        arr.add(new BasicNameValuePair("user_id", user_id));
	        arr.add(new BasicNameValuePair("vehicleName", vehicleName));
	        arr.add(new BasicNameValuePair("vehicleType", vehicleType));
	        arr.add(new BasicNameValuePair("modelYear", modelYear));
	        arr.add(new BasicNameValuePair("make", make));
	        arr.add(new BasicNameValuePair("model", model));
	        arr.add(new BasicNameValuePair("fuelType", fuelType));
	        arr.add(new BasicNameValuePair("color", color));
	        arr.add(new BasicNameValuePair("licensePlate", licensePlate));
	        arr.add(new BasicNameValuePair("licensePlate_state", licensePlate_state));
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
	
	private JSONObject editSingleVehicle(String methodName,String vehicle_Id,String vehicleName,String vehicleType,
			String modelYear,String make,String model,String fuelType,String color,String licensePlate,String licensePlate_state
			) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", methodName));
	        arr.add(new BasicNameValuePair("vehicle_Id", vehicle_Id));
	        arr.add(new BasicNameValuePair("vehicleName", vehicleName));
	        arr.add(new BasicNameValuePair("vehicleType", vehicleType));
	        arr.add(new BasicNameValuePair("modelYear", modelYear));
	        arr.add(new BasicNameValuePair("make", make));
	        arr.add(new BasicNameValuePair("model", model));
	        arr.add(new BasicNameValuePair("fuelType", fuelType));
	        arr.add(new BasicNameValuePair("color", color));
	        arr.add(new BasicNameValuePair("licensePlate", licensePlate));
	        arr.add(new BasicNameValuePair("licensePlate_state", licensePlate_state));
	        //arr.add(new BasicNameValuePair("type", type));
	        
	        Log.e("Parameters",arr.toString());
	        
	        
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
	
	
	private JSONObject saveMotorist(Motorist motoristForRegistration,
			Vehicle_Info v) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost("http://sfbaytechnology.com/uploads/mayar/jumpAngel/index.php?");
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", "addUser"));
	        arr.add(new BasicNameValuePair("f_name", motoristForRegistration.getFirstName()));
	        arr.add(new BasicNameValuePair("l_name", motoristForRegistration.getLastName()));
	        arr.add(new BasicNameValuePair("phone", motoristForRegistration.getCellNumber()));
	        arr.add(new BasicNameValuePair("email", motoristForRegistration.getEMailAddress()));
	        arr.add(new BasicNameValuePair("password", motoristForRegistration.getPasword()));
	        arr.add(new BasicNameValuePair("emergency_no", motoristForRegistration.getIEmergencyN()));
	        arr.add(new BasicNameValuePair("lat", CurrentLocationLocator.ThisLat));
	        arr.add(new BasicNameValuePair("long", CurrentLocationLocator.ThisLong));
	        arr.add(new BasicNameValuePair("type", "0"));
	        arr.add(new BasicNameValuePair("paypal_username", motoristForRegistration.getWePayID()));
	        arr.add(new BasicNameValuePair("creditcard_no", "1234"));
	        arr.add(new BasicNameValuePair("creditcard_enddate", motoristForRegistration.getExpiryDate()));
	        arr.add(new BasicNameValuePair("threeDigitCode", motoristForRegistration.getThreeDigitSecurityCode()));
	        arr.add(new BasicNameValuePair("token", PushApplication.APID));//android_id));
	        arr.add(new BasicNameValuePair("vehicleName", v.getvehicleName()));
	        arr.add(new BasicNameValuePair("vehicleType", v.getVehicle_Type()));
	        arr.add(new BasicNameValuePair("modelYear", v.getModel()));
	        arr.add(new BasicNameValuePair("make", v.getMake()));
	        arr.add(new BasicNameValuePair("model", v.getModel()));
	        arr.add(new BasicNameValuePair("fuelType", v.getFuel_Type()));
	        arr.add(new BasicNameValuePair("color", v.getColor()));
	        arr.add(new BasicNameValuePair("licensePlate", v.getLicensePlate()));
	        
	        
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
		 AddVehicleCancelButton=(Button)findViewById(R.id.AddVehicleCancelButton);
//		 AddVehicleSaveandAddAnotherButton=(Button)findViewById(R.id.AddVehicleSaveandAddAnotherButton);
		 AddVehicleSaveButton=(Button)findViewById(R.id.AddVehicleSaveButton);
		 AddVehicleVehicleName=(EditText)findViewById(R.id.AddVehicleVehicleName);
		 AddVehicleVehicleType=(Spinner)findViewById(R.id.AddVehicleVehicleType);
		 AddVehicleVehicleYear=(Spinner)findViewById(R.id.AddVehicleVehicleYear);
		 AddVehicleVehicleMake=(EditText)findViewById(R.id.AddVehicleVehicleMake);
		 AddVehicleVechicleModel=(EditText)findViewById(R.id.AddVehicleVechicleModel);       
		 AddVehicleVehicleFuel=(Spinner)findViewById(R.id.AddVehicleVehicleFuel);
		 AddVehicleVehicleColor=(EditText)findViewById(R.id.AddVehicleVehicleColor);
		 AddVehicleVehicleLicencePlate=(EditText)findViewById(R.id.AddVehicleVehicleLicencePlate);
		 AddVehicleMotoristPlateState=(Spinner)findViewById(R.id.AddVehicleMotoristPlateState);
		
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
	
}
