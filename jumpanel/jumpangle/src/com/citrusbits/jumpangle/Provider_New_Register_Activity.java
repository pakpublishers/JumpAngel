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
import org.json.JSONObject;
import org.w3c.dom.Text;

import businessclasses.CurrentLogedInUser;
import businessclasses.Motorist;
import businessclasses.Provider;
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
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.Settings.Secure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Provider_New_Register_Activity extends Activity {

	Button RegisterCancelButton=null;
	//Spinner MotoristIMNSpiner=null;
	Button RegisterSaveButton=null;
	
	EditText FirstName=null;
	EditText LastName=null;
	//EditText CellNumber=null;
	EditText EmailAddress=null;
	EditText Pasword=null;
	
	
	
	//EditText WePayTV=null;
	//EditText ExpiryDate=null;
	//EditText ThreeDigitSecurityCode=null;
	ProgressDialog UpdateUserProgressDialog=null;
	public void showDialog(String Msg)
	{
		UpdateUserProgressDialog=new ProgressDialog(Provider_New_Register_Activity.this);
		UpdateUserProgressDialog.setTitle(Msg);
		UpdateUserProgressDialog.setMessage("Please wait");
		UpdateUserProgressDialog.setCancelable(false);
		UpdateUserProgressDialog.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motoristregistrationview);
		initiateControls();
		initiateListners();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initiateListners() {
		
		RegisterCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		RegisterSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(FirstName.getText().toString().trim().equals(""))
				{
					Toast.makeText(getBaseContext(), "Please provide First Name", Toast.LENGTH_LONG).show();
					return;
				}if(LastName.getText().toString().trim().equals(""))
				{
					Toast.makeText(getBaseContext(), "Please provide Last Name", Toast.LENGTH_LONG).show();
					return;
				}
				if(EmailAddress.getText().toString().trim().equals(""))
				{
					Toast.makeText(getBaseContext(), "Please provide Email Address", Toast.LENGTH_LONG).show();
					return;
				}
				if(Pasword.getText().toString().trim().equals(""))
				{
					Toast.makeText(getBaseContext(), "Please provide Pasword", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(!Login_Activity.isValidEmail(EmailAddress.getText().toString()))
				{
					Toast.makeText(getBaseContext(), "Please provide valid Email Address", Toast.LENGTH_LONG).show();
					return;
				}
				
				final Motorist m=new Motorist();
				m.setFirstName(FirstName.getText().toString());
				m.setLastName(LastName.getText().toString());
				//m.setCellNumber(CellNumber.getText().toString());
				m.setEMailAddress(EmailAddress.getText().toString());
				m.setPasword(Pasword.getText().toString());
				//m.setIEmergencyN(MotoristIMNSpiner.getItemAtPosition(MotoristIMNSpiner.getSelectedItemPosition()).toString());
				//m.setWePayID(WePayTV.getText().toString());
				//m.setThreeDigitSecurityCode(ThreeDigitSecurityCode.getText().toString());
				
				//Intent AddVehicle=new Intent(Motorist_Register_Activity.this,Motorist_Add_Vehicle_Activity.class);
				
				showDialog("Registering User.");
				
				final Handler h=new Handler()
				{
					@Override 
					public void handleMessage(Message m)
					{
						switch (m.what) {
						case 1:
						{
							UpdateUserProgressDialog.cancel();
							Intent MainActivityintent=new Intent(Provider_New_Register_Activity.this, MainActivity.class);
							MainActivityintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Bundle b=new Bundle();
							b.putSerializable("currentuser", (CurrentLogedInUser)m.obj);
							MainActivityintent.putExtra("cu", b);
							
							SharedPreferences sp=getThisSharedPreferences();
							SharedPreferences.Editor edtr=sp.edit();
							
							CurrentLogedInUser cu=(CurrentLogedInUser)m.obj;
							
							String custring=cu.getJSONString();
							
							edtr.putString("currentLogedUser", custring);
							edtr.commit();
							
							
							startActivity(MainActivityintent);
							
//							Toast.makeText(getBaseContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
//							finish();
							
							break;
						}
						case 0:
						{
							UpdateUserProgressDialog.cancel();
							Toast.makeText(getBaseContext(), m.obj.toString(), Toast.LENGTH_SHORT).show();
							break;
						}
						case -1:
						{
							UpdateUserProgressDialog.cancel();
							Toast.makeText(getBaseContext(), "Connection Problem", Toast.LENGTH_SHORT).show();
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
						JSONObject Response= saveMotorist(m, null);
						try
						{
							if(Response!=null && Response.getString("error").equals("false"))
							{
								JSONObject ResponseData=Response.getJSONObject("data");
								CurrentLogedInUser cu=new CurrentLogedInUser();
								cu.setuser_id(ResponseData.getString("user_id"));
								//cu.setvehicle_id(ResponseData.getString("vehicle_Id"));
								cu.settoken(ResponseData.getString("token"));
								cu.setmaster_id("0");
								Provider m=new Provider();
								m.setFirstName(ResponseData.getString("f_name"));
								m.setLastName(ResponseData.getString("l_name"));
								//m.setCellNumber(ResponseData.getString("phone"));
								m.setEmailAddress(ResponseData.getString("email"));
								m.setPasword(ResponseData.getString("password"));
								//m.(ResponseData.getString("long"));
								//m.setLat(ResponseData.getString("lat"));
								//m("0");
								//m.setIEmergencyN(ResponseData.getString("emergency_no"));
								//m.setWePayID(ResponseData.getString("paypal_username"));
								//m.setcreaditcard_no(ResponseData.getString("creditcard_no"));
								//m.setExpiryDate(ResponseData.getString("creditcard_enddate"));
								//m.setThreeDigitSecurityCode(ResponseData.getString("threeDigitCode"));
								// replacing type with userCatagory
								cu.setUserDetails("provider", (Object)m);
								
								Message ma=new Message();
								ma.what=1;
								ma.obj=cu;
								h.sendMessage(ma);
								
								
							}
							else
							{
								String msg=Response.getString("message");
								Message ma=new Message();
								ma.obj=msg;
								ma.what=0;
								h.sendMessage(ma);
								
							}
						}
						catch(Exception d)
						{
							h.sendEmptyMessage(-1);
						}
					}
					
				}.start();
				
				//Bundle b=new Bundle();
				//b.putSerializable("Motoristp", m);
				//AddVehicle.putExtra("Motorist", b);
				
				//startActivityForResult(AddVehicle, 0);
				
				//Log.e("MyObject",""+m.toString());
			}
		});
		
	}

	private void initiateControls() {
		RegisterCancelButton=(Button)findViewById(R.id.RegisterCancelButton);
		//MotoristIMNSpiner=(Spinner)findViewById(R.id.MotoristIMNSpiner);
		FirstName= (EditText)findViewById(R.id.Emailaddress);
		LastName= (EditText)findViewById(R.id.LoginPasword);
		//CellNumber= (EditText)findViewById(R.id.CellNumber);
		EmailAddress= (EditText)findViewById(R.id.EmailAddress);
		Pasword= (EditText)findViewById(R.id.Pasword);
		//WePayTV= (EditText)findViewById(R.id.WePayTV);
		//ExpiryDate= (EditText)findViewById(R.id.ExpiryDate);
		//ThreeDigitSecurityCode= (EditText)findViewById(R.id.ThreeDigitSecurityCode);
		setInternationEmergencyNumbers();
		RegisterSaveButton=(Button)findViewById(R.id.RegisterSaveButton);
	}
	public void setInternationEmergencyNumbers()
	{
		//ArrayAdapter<CharSequence> adp=ArrayAdapter.createFromResource(this, R.array.Imergency_Numbers, android.R.layout.simple_spinner_item);
		//adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//MotoristIMNSpiner.setAdapter(adp);
		///adp.notifyDataSetChanged();
		
		
	}
	private JSONObject saveMotorist(Motorist motoristForRegistration,
			Vehicle_Info v) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", "addUser"));
	        arr.add(new BasicNameValuePair("f_name", motoristForRegistration.getFirstName()));
	        arr.add(new BasicNameValuePair("l_name", motoristForRegistration.getLastName()));
	        //arr.add(new BasicNameValuePair("phone", motoristForRegistration.getCellNumber()));
	        arr.add(new BasicNameValuePair("email", motoristForRegistration.getEMailAddress()));
	        arr.add(new BasicNameValuePair("password", motoristForRegistration.getPasword()));
	        //arr.add(new BasicNameValuePair("emergency_no", motoristForRegistration.getIEmergencyN()));
	        arr.add(new BasicNameValuePair("lat", "0"));//CurrentLocationLocator.ThisLat));
	        arr.add(new BasicNameValuePair("long", "0"));//CurrentLocationLocator.ThisLong));
	        arr.add(new BasicNameValuePair("type", "1"));
	        //arr.add(new BasicNameValuePair("paypal_username", motoristForRegistration.getWePayID()));
	        //arr.add(new BasicNameValuePair("creditcard_no", "1234"));
	        //arr.add(new BasicNameValuePair("creditcard_enddate", motoristForRegistration.getExpiryDate()));
	        //arr.add(new BasicNameValuePair("threeDigitCode", motoristForRegistration.getThreeDigitSecurityCode()));
	        
	        String token=(PushApplication.APID!=null)?PushApplication.APID:"";
	        if(token.trim().equals(""))
	        {
	        	token="aa24dfsdfsdfsdf-sdfsdfsf-sdfsdf099sdfsdf-s";
	        }
	        
	        arr.add(new BasicNameValuePair("token", token));//android_id));
	        //arr.add(new BasicNameValuePair("vehicleName", v.getvehicleName()));
	        //arr.add(new BasicNameValuePair("vehicleType", v.getVehicle_Type()));
	        //arr.add(new BasicNameValuePair("modelYear", v.getModel()));
	        //arr.add(new BasicNameValuePair("make", v.getMake()));
	        //arr.add(new BasicNameValuePair("model", v.getModel()));
	        //arr.add(new BasicNameValuePair("fuelType", v.getFuel_Type()));
	        //arr.add(new BasicNameValuePair("color", v.getColor()));
	        //arr.add(new BasicNameValuePair("licensePlate", v.getLicensePlate()));
	        Log.e("Parameters are",arr.toString());
	        
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
		        Log.e("regresponse of registration",""+builder.toString());
		        return new JSONObject(builder.toString());
		        
	        }catch (Exception e) {
	        	int k=0;
			}
	        return null;
		
	}
	
	public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
	
}
