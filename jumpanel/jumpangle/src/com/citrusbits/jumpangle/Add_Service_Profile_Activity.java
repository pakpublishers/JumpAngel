package com.citrusbits.jumpangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
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
import org.json.JSONException;
import org.json.JSONObject;

import businessclasses.BattryJump;
import businessclasses.CurrentLogedInUser;
import businessclasses.FuelDeliveryJump;
import businessclasses.Provider;
import businessclasses.PushApplication;
import businessclasses.TireService;
import businessclasses.TowService;
import businessclasses.UnlockVehicle;
import businessclasses.VehicleBundle;
import businessclasses.Vehicle_Diagnosis;
import businessclasses.Vehicle_Info;
import businessclasses.Vehivle_Service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Add_Service_Profile_Activity extends Activity {

	Button ServiceCancelButton=null;
	Button AddNextVehicleButton=null;
	
	ToggleButton BattryJumpToggle=null;
	ToggleButton FuelDeliveryToggle=null;
	ToggleButton TowToggle=null;
	ToggleButton VehicleUnlocksToggle=null;
	ToggleButton TireServiceToggle=null;
	ToggleButton DiagnosticsToggle=null;
	TextView VehivleTitle=null;
	
	//BattryJump BJ=new BattryJump();
	//FuelDeliveryJump FD=new FuelDeliveryJump();
	//TowService TOW=new TowService();
	//UnlockVehicle UV=new UnlockVehicle();
	//TireService TS=new TireService();
	//Vehicle_Diagnosis VD=new Vehicle_Diagnosis();
	//Vehivle_Service ProviderVehicleService=new Vehivle_Service();
	VehicleBundle VB=new VehicleBundle();
	Provider ProviderUser=null;
	
	ProgressDialog UpdateUserProgressDialog=null;
	
	public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	Handler ServiceHandler=new Handler()
	{
		@Override
		public void handleMessage(Message m)
		{
			switch (m.what) {
			case 1:
			{
				SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(Add_Service_Profile_Activity.this);//getSharedPreferences("com.citrusbits.jumpangle", Context.MODE_PRIVATE);
				SharedPreferences.Editor myediEditor=sp.edit();
				myediEditor.clear();
				myediEditor.commit();
				UpdateUserProgressDialog.cancel();
				
				CurrentLogedInUser cu= (CurrentLogedInUser)m.obj;
				Intent i=new Intent(Add_Service_Profile_Activity.this,MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				Bundle b=new Bundle();
				b.putSerializable("currentuser", cu);
				i.putExtra("cu", b);
				setResult(RESULT_OK);
				startActivity(i);
				break;
			}
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(UpdateUserProgressDialog!=null)
		{
			UpdateUserProgressDialog.cancel();
			UpdateUserProgressDialog.dismiss();
		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_service_profile_view);
		if(getIntent().hasExtra("VehicleInfo") && getIntent().hasExtra("Provider"))
		{
			ProviderUser=(Provider)getIntent().getSerializableExtra("Provider");
			VB.set_Vehivle_Infor((Vehicle_Info)getIntent().getSerializableExtra("VehicleInfo"));
			int k=0;
		}
	
		initiateControls();
		initiateListners();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			if(data.getStringExtra("act").equals("BJ"))
			{
				VB.get_Vehicle_Service().setbattryJump((BattryJump) data.getSerializableExtra("BattryJump"));
				//Log.e("lightduty is ",""+BJ.getLightDuty().toString());
			}
			else if(data.getStringExtra("act").equals("FD"))
			{
				VB.get_Vehicle_Service().setfuelDelivery((FuelDeliveryJump) data.getSerializableExtra("fueldelivery"));
				//Log.e("gasoline is ",""+FD.getGasoline_Pertol().toString());
			}
			else if(data.getStringExtra("act").equals("TOW"))
			{
				VB.get_Vehicle_Service().setTowServbice((TowService) data.getSerializableExtra("tow"));
			//	Log.e("gasoline is ",""+TOW.getBoom().toString());
			}
			else if(data.getStringExtra("act").equals("UV"))
			{
				VB.get_Vehicle_Service().setunlockVehicle((UnlockVehicle) data.getSerializableExtra("unlockvehicle"));
				//Log.e("gasoline is ",""+UV.getApplytoVahicleOnly().toString());
			}
			else if(data.getStringExtra("act").equals("TS"))
			{
				VB.get_Vehicle_Service().settireService((TireService) data.getSerializableExtra("TireService"));
				//Log.e("gasoline is ",""+UV.getApplytoVahicleOnly().toString());
			}
			else if(data.getStringExtra("act").equals("VD"))
			{
				VB.get_Vehicle_Service().setvehicleDiagnosis((Vehicle_Diagnosis) data.getSerializableExtra("vehicle_diagnosis"));
				//Log.e("datya is ",VD.getGasolineVehicles().toString());
			}
		}
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
	        Log.e("Vehicel parameters",arr.toString());
	        
	        
	        
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

	private void initiateListners() {
		ServiceCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				UpdateUserProgressDialog.cancel();
				finish();
				
			}
		});
		AddNextVehicleButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//if(Provider_Register_Activity.CurrentFleetVehicle==Provider_Register_Activity.TotalFleetVehicles)
				//{
					//String s=VB.toString();
					//ServiceHandler.sendEmptyMessage(1);
					
				final CurrentLogedInUser cu=new CurrentLogedInUser();
				try {
					cu.parseJSON(new JSONObject(getThisSharedPreferences().getString("currentLogedUser", "")));
				} catch (JSONException e) {}
				
				try
				{
					showDialog();
				}catch(Exception l){}
//					new Thread()
//					{
//						
//						@Override
//						public void run()
//						{
							//SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(Add_Service_Profile_Activity.this);//getSharedPreferences("com.citrusbits.jumpangle", Context.MODE_PRIVATE);
							//Log.e("Total Fleet Vehicles",Provider_Register_Activity.TotalFleetVehicles+"");
							
							//VehicleBundle[] VBArray=new VehicleBundle[Provider_Register_Activity.TotalFleetVehicles];
							
//							for(int i=0; i<Provider_Register_Activity.TotalFleetVehicles-1;i++)
//							{
//								String data=sp.getString("VehicleBundle"+(i+1), "");
//								VehicleBundle vb=new VehicleBundle();
//								try{
//									JSONObject json=new JSONObject(data);
//									vb.parseJSON(json);
//								}catch(Exception k){}
//								Log.e("VehicleBundle "+(i+1),data);
//								VBArray[i]=vb;
//							}
							//VBArray[VBArray.length-1]=VB;
							
//							CurrentLogedInUser cu= updateProvider(VBArray[0].get_Vehivle_Infor());
							
							final Handler hndlr =new Handler()
							{
								@Override
								public void handleMessage(Message m)
								{
									switch (m.what) {
									case 1:
									{
										JSONObject vObj=null;
										try {
											vObj=new JSONObject(m.obj.toString());
											Toast.makeText(Add_Service_Profile_Activity.this,vObj.getString("message") , Toast.LENGTH_SHORT).show();
											
										} catch (Exception e) {
											// TODO: handle exception
										}
										
										Vehicle_Info vi=new Vehicle_Info();
										try{vi.setModel(vObj.getString("model"));}catch(Exception d){}
										try{vi.setColor(vObj.getString("color"));}catch(Exception d){}
										try{vi.setFuel_Type(vObj.getString("fuelType"));}catch(Exception d){}
										try{vi.setvehicleName(vObj.getString("vehicleName"));}catch(Exception d){}
										try{vi.setModel(vObj.getString("modelYear"));}catch(Exception d){}
										try{vi.setVehicle_Type(vObj.getString("vehicleType"));}catch(Exception d){}
										try{vi.setvehicle_Id(vObj.getString("vehicle_Id"));}catch(Exception d){}
										//vi.setFuel_Type(Response.getString("user_id"));
										//vi.set(Response.getString("type"));
										try{vi.setMake(vObj.getString("make"));}catch(Exception d){}
										try{vi.setLicensePlate(vObj.getString("licensePlate"));}catch(Exception d){}
										try{vi.setPlateState(vObj.getString("licensePlate_state"));}catch(Exception d){}
										try{
											if(vObj.getString("user_id").equals(cu.getuser_id()))
											{
												cu.setVehicleComplete(vi);
											}
										}catch(Exception d){}
//										
//										SharedPreferences sp=getThisSharedPreferences();
//										SharedPreferences.Editor edtr=sp.edit();
//										edtr.putString("currentLogedUser", cu.getJSONString());
//										edtr.commit();
										
										//
										
										try {
											JSONObject obj=new JSONObject(m.obj.toString());
											cu.setvehicle_id(obj.getString("vehicle_Id"));
											
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
											//_Act.loadVehiclelist();
											
											final Handler hdl=new Handler()
											{
												@Override
												public void handleMessage(Message m)
												{
													Toast.makeText(getBaseContext(), m.obj.toString(), Toast.LENGTH_SHORT).show();
													UpdateUserProgressDialog.cancel();
													Intent i=new Intent(Add_Service_Profile_Activity.this, MainActivity.class);
													i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivity(i);
												}
											};
										
											new Thread()
											{
												@Override
												public void run()
												{
													String masterid=cu.getmaster_id();
													
													JSONObject Response=addService(cu.getuser_id(), cu.getvehicle_id(), VB.get_Vehicle_Service());
													Log.e("delete Response",Response.toString());
													try
													{
														if(Response.getString("error").contains("false"))
														{
															Message m=new Message();
															m.what=1;
															m.obj=Response.getString("message");
															hdl.sendMessage(m);
														}
														else
														{
															Message m=new Message();
															m.what=-1;
															m.obj=Response.getString("message");
															hdl.sendMessage(m);
														}
													}catch(Exception k){}
												}
											}.start();
											
											
										
										
										break;
									}
									case -1:
									{
										UpdateUserProgressDialog.cancel();
										Toast.makeText(Add_Service_Profile_Activity.this, m.obj.toString(), Toast.LENGTH_SHORT).show();
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
									String masterid=cu.getmaster_id();
									
									JSONObject Response=addVehicle("addVehicle", cu.getuser_id(), VB.get_Vehivle_Infor().getvehicleName(), VB.get_Vehivle_Infor().getVehicle_Type(), VB.get_Vehivle_Infor().getYear(), VB.get_Vehivle_Infor().getMake(), VB.get_Vehivle_Infor().getModel(), VB.get_Vehivle_Infor().getFuel_Type(), VB.get_Vehivle_Infor().getColor(), VB.get_Vehivle_Infor().getLicensePlate(), VB.get_Vehivle_Infor().getPlateState(), "1");
									Log.e("delete Response",Response.toString());
									try
									{
										if(Response.getString("error").contains("false"))
										{
											Message m=new Message();
											m.what=1;
											m.obj=Response.getString("data");
											hndlr.sendMessage(m);
										}
										else
										{
											Message m=new Message();
											m.what=-1;
											m.obj=Response.getString("message");
											hndlr.sendMessage(m);
										}
									}catch(Exception k){}
								}
							}.start();
							
							
							
//							if(Provider_Register_Activity.TotalFleetVehicles>1)
//							{
//								for(int i=1; i<VBArray.length;i++)
//								{
//									Log.e("Adding Vehicle Number "+i,VBArray[i].get_Vehivle_Infor().getvehicleName());
//									Vehicle_Info vi= addVehicle(cu.getuser_id(), VBArray[i].get_Vehivle_Infor());
//									addService(cu.getuser_id(), vi.getvehicle_Id(), VBArray[i].get_Vehicle_Service());
//									Log.e("Successfully Added Vehicle with service","");
//								}
//							}
							
//							Log.e("Vehicle Bundle Last",VB.getJSON().toString());
//							
//							Message m=new Message();
//							m.what=1;
//							m.obj=cu;
//							ServiceHandler.sendMessage(m);
							
							
//						}
//					}.start();
				//	
					
//				}
//				else
//				{
//					// Making backup
//					SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(Add_Service_Profile_Activity.this);//getSharedPreferences("com.citrusbits.jumpangle", Context.MODE_PRIVATE);
//					SharedPreferences.Editor myediEditor=sp.edit();
//					String data=VB.getJSON().toString();
//					
//					myediEditor.putString("VehicleBundle"+Provider_Register_Activity.CurrentFleetVehicle, data);
//					myediEditor.commit();
//					Provider_Register_Activity.CurrentFleetVehicle +=1;
//					
//					int k=0;
//					finish();
//					Intent i=new Intent();
//					//i.putExtra("vehicleservice", ProviderVehicleService);
//					setResult(RESULT_OK, i);
//				}
				
				
				
				
			}
		});
		BattryJumpToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent i=new Intent(Add_Service_Profile_Activity.this, Battry_Jump_Service_Activity.class);
					i.putExtra("act", "BJ");
					i.putExtra("title", "Battry Jump Service\r\n Vehicle Name");
					startActivityForResult(i, 0);
				}
				VB.get_Vehicle_Service().battryJumpStatus=isChecked;
			}
		});
		FuelDeliveryToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent i=new Intent(Add_Service_Profile_Activity.this, Fuel_Delivery_Service_Activity.class);
					startActivityForResult(i, 0);
				}
				VB.get_Vehicle_Service().FuelDeliveryStatus=isChecked;
			}
		});
		TowToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent i=new Intent(Add_Service_Profile_Activity.this, Tow_Service_Activity.class);
					startActivityForResult(i, 0);
				}
				VB.get_Vehicle_Service().TowServiceStatus=isChecked;
			}
		});
		DiagnosticsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent i=new Intent(Add_Service_Profile_Activity.this, Vehicle_Diagnosis_Details_Activity.class);
					i.putExtra("title", "Vehivle Diagnosis\r\nVihicle Name");
					startActivityForResult(i, 0);
				}
				VB.get_Vehicle_Service().VehicleDiagnosisStatus=isChecked;
			}
		});
		VehicleUnlocksToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent i=new Intent(Add_Service_Profile_Activity.this, Vehicle_Unlock_Service.class);
					startActivityForResult(i, 0);
				}
				VB.get_Vehicle_Service().UnlocakVehicleStatus=isChecked;
			}
			
		});
		
		TireServiceToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent i=new Intent(Add_Service_Profile_Activity.this, Tire_Service_Activity.class);
					i.putExtra("title", "Tire Service Detail\r\n Vehicle 1");
					startActivityForResult(i, 0);
				}
				VB.get_Vehicle_Service().TireServiceStatus=isChecked;
				
			}
		});
		
	}

	private void initiateControls() {
		ServiceCancelButton=(Button)findViewById(R.id.ServiceCancelButton);
		AddNextVehicleButton=(Button)findViewById(R.id.AddNextVehicleButton);
		BattryJumpToggle=(ToggleButton)findViewById(R.id.BattryJumpToggle);
		FuelDeliveryToggle=(ToggleButton)findViewById(R.id.FuelDeliveryToggle);
		TowToggle=(ToggleButton)findViewById(R.id.TowToggle);
		VehicleUnlocksToggle=(ToggleButton)findViewById(R.id.VehicleUnlocksToggle);
		TireServiceToggle=(ToggleButton)findViewById(R.id.TireServiceToggle);
		DiagnosticsToggle=(ToggleButton)findViewById(R.id.DiagnosticsToggle);
		VehivleTitle=(TextView)findViewById(R.id.VehivleTitle);
		VehivleTitle.setText("Vehicle Services\r\n"+VB.get_Vehivle_Infor().getvehicleName());
	}
	public CurrentLogedInUser updateProvider(Vehicle_Info v)
	{
		CurrentLogedInUser cu=null;

		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", "addUser"));
	        arr.add(new BasicNameValuePair("f_name", ProviderUser.getFirstName()));
	        arr.add(new BasicNameValuePair("l_name", ProviderUser.getLastName()));
	        arr.add(new BasicNameValuePair("phone", ProviderUser.getCellNumber()));
	        arr.add(new BasicNameValuePair("email", ProviderUser.getEmailAddress()));
	        arr.add(new BasicNameValuePair("password", ProviderUser.getPasword()));
	        
	        arr.add(new BasicNameValuePair("lat", CurrentLocationLocator.ThisLat));
	        arr.add(new BasicNameValuePair("long", CurrentLocationLocator.ThisLong));
	        arr.add(new BasicNameValuePair("fleetDetail", Provider_Register_Activity.TotalFleetVehicles+","+ProviderUser.getGeneralServiceDuty()));
	        arr.add(new BasicNameValuePair("commercialFleetInsurance", ConvertBoolean(ProviderUser.getCommercialFleetInsurance())));
	        arr.add(new BasicNameValuePair("professionallyPermittedFleet", ConvertBoolean(ProviderUser.getProfessionallyPermittedFleet())));
	        arr.add(new BasicNameValuePair("commerciallyLicensedDriver", ConvertBoolean(ProviderUser.getCommerciallyLicensedDriver())));
	        arr.add(new BasicNameValuePair("NATACertifiedDriver", ConvertBoolean(ProviderUser.getNATACertifiedDrivers())));
	        arr.add(new BasicNameValuePair("type", "1"));// chect it
	        arr.add(new BasicNameValuePair("paypal_username", ProviderUser.getWePayID()));
	        arr.add(new BasicNameValuePair("token", PushApplication.APID));//Secure.getString(getContentResolver(), Secure.ANDROID_ID)+""));
	        arr.add(new BasicNameValuePair("vehicleName", v.getvehicleName()));
	        arr.add(new BasicNameValuePair("vehicleType", v.getVehicle_Type()));
	        arr.add(new BasicNameValuePair("modelYear", v.getModel()));
	        arr.add(new BasicNameValuePair("make", v.getMake()));
	        arr.add(new BasicNameValuePair("model", v.getModel()));
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
		        JSONObject json= new JSONObject(builder.toString());
		        if(Boolean.parseBoolean(json.getString("error"))==false)
		        {
		        	JSONObject data= json.getJSONObject("data");
		        
		        	cu=new CurrentLogedInUser();
			        cu.setuser_id(data.getString("user_id"));
			        cu.settoken(data.getString("token"));
			        cu.setvehicle_id(data.getString("vehicle_Id"));
			        
			        Provider p=new Provider();
			        p.setFirstName(data.getString("f_name"));
			        p.setLastName(data.getString("l_name"));
			        p.setCellNumber(data.getString("phone"));
			        p.setEmailAddress(data.getString("email"));
			        String fleetDetail=data.getString("fleetDetail");
			        p.setFleetVehicleCount(Integer.parseInt(fleetDetail.substring(0, fleetDetail.indexOf(","))));
			        p.setGeneralServiceDuty(fleetDetail.substring(fleetDetail.indexOf(",")+1));
			        p.setCommercialFleetInsurance(ConvertIntoBoolean(data.getString("commercialFleetInsurance")));
			        p.setProfessionallyPermittedFleet(ConvertIntoBoolean(data.getString("professionallyPermittedFleet")));
			        p.setCommercialFleetInsurance(ConvertIntoBoolean(data.getString("commerciallyLicensedDriver")));
			        p.setNATACertifiedDrivers(ConvertIntoBoolean(data.getString("NATACertifiedDriver")));
			        p.setWePayID(data.getString("paypal_username"));
			        
			        Vehicle_Info vi=new Vehicle_Info();
			        vi.setvehicleName(data.getString("vehicleName"));
			        vi.setVehicle_Type(data.getString("vehicleType"));
			        vi.setModel(data.getString("modelYear"));
			        vi.setMake(data.getString("make"));
			        vi.setModel(data.getString("model"));
			        vi.setColor(data.getString("color"));
			        vi.setLicensePlate(data.getString("licensePlate"));
			        vi.setPlateState(data.getString("licensePlate_state"));
			        cu.setVehicleComplete(vi);
			        cu.setUserDetails("provider", (Object)p);
		        
		        }
		        else
		        {
		        	Log.e("Error From Server : ",json.getString("error"));
		        }
		        
		        
	        }catch(Exception k){
	        	
	        	Log.e("Error",""+k.toString());
	        }
	        return cu;
	}
	public JSONObject addService(String user_id,String vehicle_id,Vehivle_Service service)
	{
		CurrentLogedInUser cu=new CurrentLogedInUser();

		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", "addService"));
	        arr.add(new BasicNameValuePair("vehicle_Id", vehicle_id));
	        arr.add(new BasicNameValuePair("user_id", user_id));
	        
	        arr.add(new BasicNameValuePair("Battery_Jumps", (service.battryJumpStatus)?service.getbattryJump().getBattryJumpasParameter():"off"));
	        arr.add(new BasicNameValuePair("Fuel_Delivery", (service.FuelDeliveryStatus)?service.getfuelDelivery().getFuelDileveryParameter():"off"));
	        arr.add(new BasicNameValuePair("Tow_Boom", (service.TowServiceStatus)?service.getTowServbice().getBoom().getBattryJumpasParameter():"off"));
	        arr.add(new BasicNameValuePair("Tow_FlatBed",(service.TowServiceStatus)? service.getTowServbice().getFlat_Bed().getBattryJumpasParameter():"off"));
	        
	        arr.add(new BasicNameValuePair("Tow_Sling", (service.TowServiceStatus)?service.getTowServbice().getSling().getBattryJumpasParameter():"off"));
	        arr.add(new BasicNameValuePair("Tow_WheelLift", (service.TowServiceStatus)?service.getTowServbice().getWheelLift().getBattryJumpasParameter():"off"));
	        arr.add(new BasicNameValuePair("Tow_Winch", (service.TowServiceStatus)?service.getTowServbice().getWinch().getBattryJumpasParameter():"off"));
	        arr.add(new BasicNameValuePair("Tire_Changes",(service.TireServiceStatus)? service.gettireService().getTireChanges().getBattryJumpasParameter():"off"));
	        arr.add(new BasicNameValuePair("MobileTireRepair",(service.TireServiceStatus)? service.gettireService().getMobileTireRepair().getBattryJumpasParameter():"off"));
	        arr.add(new BasicNameValuePair("Diagnostics",(service.VehicleDiagnosisStatus)? service.getvehicleDiagnosis().getDiagnosisParameter():"off"));
	        arr.add(new BasicNameValuePair("Vehicle_Unlocks", (service.UnlocakVehicleStatus)?"":"off"));
	        arr.add(new BasicNameValuePair("Battery_Jumps_status", ((service.battryJumpStatus)?((service.getbattryJump().getApplyVehicleOnly())?"1":"0"):"0")));
	        arr.add(new BasicNameValuePair("Fuel_Delivery_status", ((service.FuelDeliveryStatus)?((service.getfuelDelivery().getApplyVehicleOnly())?"1":"0"):"0")));
	        arr.add(new BasicNameValuePair("Tow_status", ((service.TowServiceStatus)?((service.getTowServbice().getApplyVehicleOnly())?"1":"0"):"0")));
	        arr.add(new BasicNameValuePair("Tire_Services_status", ((service.TireServiceStatus)?((service.gettireService().getApplyVehicleOnly())?"1":"0"):"0")));
	        arr.add(new BasicNameValuePair("Diagnostics_status", ((service.VehicleDiagnosisStatus)?((service.getvehicleDiagnosis().getApplyVehicleOnly())?"1":"0"):"0")));
	        arr.add(new BasicNameValuePair("Vehicle_Unlocks_status", ((service.UnlocakVehicleStatus)?((service.getunlockVehicle().getApplytoVahicleOnly())?"1":"0"):"0")));
	        
	        Log.e("Service Parameters",arr.toString());
	        
	        
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
		        JSONObject json= new JSONObject(builder.toString());
		       
		        if(Boolean.parseBoolean(json.getString("error"))==false)
		        {
		        	Log.e("Successfull Added Vehicle Services fo Vehicle",vehicle_id);
		        }
		        else
		        {
		        	Log.e("Operation Failed for Vehicle ",vehicle_id);
		        }
		        return json;
		        
	        }catch(Exception k){
	        	
	        	Log.e("Error ",k.toString());
	        }
	        return null;
	}
	public Vehicle_Info addVehicle(String user_id,Vehicle_Info v)
	{
		Vehicle_Info vi=null;

		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost("http://sfbaytechnology.com/uploads/mayar/jumpAngel/index.php?");
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", "addVehicle"));
	        arr.add(new BasicNameValuePair("user_id", user_id));
	        arr.add(new BasicNameValuePair("vehicleName", v.getvehicleName()));
	        
	        arr.add(new BasicNameValuePair("vehicleType", v.getVehicle_Type()));
	        arr.add(new BasicNameValuePair("modelYear", v.getYear()));
	        arr.add(new BasicNameValuePair("make", v.getMake()));
	        arr.add(new BasicNameValuePair("model",v.getModel()));
	        arr.add(new BasicNameValuePair("fuelType", v.getFuel_Type()));
	        arr.add(new BasicNameValuePair("color", v.getColor()));
	        arr.add(new BasicNameValuePair("licensePlate", v.getLicensePlate()));
	        arr.add(new BasicNameValuePair("licensePlate_state", v.getPlateState()));
	        arr.add(new BasicNameValuePair("type", "1"));
	        
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
		        JSONObject json= new JSONObject(builder.toString());
		       
		        if(Boolean.parseBoolean(json.getString("error"))==false)
		        {
		        	Log.e("Added Vehicle ",v.getvehicleName()+" for user "+user_id);
		        	// no need to get data from user
		        	JSONObject data=json.getJSONObject("data");
		        	vi=new Vehicle_Info();
		        	vi.setvehicle_Id(data.getString("vehicle_Id"));
		        	vi.setvehicleName(data.getString("vehicleName"));
		        	vi.setVehicle_Type(data.getString("vehicleType"));
		        	vi.setYear(data.getString("modelYear"));
		        	vi.setMake(data.getString("make"));
		        	vi.setModel(data.getString("model"));
		        	vi.setColor(data.getString("color"));
		        	vi.setLicensePlate(data.getString("licensePlate"));
		        	
		        }
		        else
		        {
		        	Log.e("Operation Failed for Vehicle ",v.getvehicleName());
		        }
		        
		        
	        }catch(Exception k){
	        	
	        	Log.e("Error ",k.toString());
	        }
	        return vi;
	        
	}
	
	
	        
	public  String ConvertBoolean(Boolean b)
	{
		return ((b)?"1":"0");
	}
	public Boolean ConvertIntoBoolean(String v)
	{
		return ((v.equals("1"))?true:false);
	}
	public void showDialog()
	{
		if(UpdateUserProgressDialog==null)
			UpdateUserProgressDialog=new ProgressDialog(Add_Service_Profile_Activity.this);
		
		UpdateUserProgressDialog.setTitle("Updateing record");
		UpdateUserProgressDialog.setMessage("Please wait");
		UpdateUserProgressDialog.setCancelable(false);
		UpdateUserProgressDialog.show();
	}

}
