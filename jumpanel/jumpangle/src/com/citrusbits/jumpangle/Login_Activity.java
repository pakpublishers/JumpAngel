package com.citrusbits.jumpangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import businessclasses.CurrentLogedInUser;
import businessclasses.Motorist;
import businessclasses.Provider;
import businessclasses.PushApplication;
import businessclasses.Vehicle_Info;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login_Activity extends Activity {

	Button CancelButton=null;
	EditText Emailaddress=null;
	EditText LoginPasword=null;
	Button LoginUserButton=null;
	TextView ForgotPasoword=null;
	ProgressDialog UpdateUserProgressDialog=null;
	
	CurrentLocationLocator currentLocaitonLocator=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginview);
		
		currentLocaitonLocator=new CurrentLocationLocator(null, getBaseContext(),true);
		initiateControls();
		initiateListners();
	}
	
	public void showDialog(String Msg)
	{
		UpdateUserProgressDialog=new ProgressDialog(Login_Activity.this);
		UpdateUserProgressDialog.setTitle(Msg);
		UpdateUserProgressDialog.setMessage("Please wait");
		UpdateUserProgressDialog.setCancelable(false);
		UpdateUserProgressDialog.show();
	}
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	private void initiateListners() {
		
ForgotPasoword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder ab=new AlertDialog.Builder(Login_Activity.this);
				ab.setTitle("Forgot Password?");
				ab.setMessage("Please enter your email.");
				final EditText ed=new EditText(getBaseContext());
				ed.setTextColor(Color.BLACK);
				ed.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
				ab.setPositiveButton("Send", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
						if(!Login_Activity.isValidEmail(ed.getText().toString()))
						{
							Toast.makeText(getBaseContext(), "Please provide valid Email Address", Toast.LENGTH_LONG).show();
							return;
						}
						
						
						showDialog("Loading Details");
						final Handler h=new Handler()
						{
							@Override
							public void handleMessage(Message m)
							{
								//Toast.makeText(getBaseContext(), "Amount has been increased", Toast.LENGTH_SHORT).show();
								//NumberPickerDialog.cancel();
								
								switch (m.what) {
								case -1:
								{
									Toast.makeText(Login_Activity.this, "Conneciton Problem", Toast.LENGTH_SHORT).show();
									
									break;
								}
								case 1:
								{
									Toast.makeText(Login_Activity.this, m.obj.toString(), Toast.LENGTH_SHORT).show();
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
								JSONObject obj=  forgotPass(ed.getText().toString());
								try {
									if(obj.getBoolean("error"))
									{
										
										h.sendEmptyMessage(-1);
									}
									else
									{
										Message m=new Message();
										m.what=1;
										m.obj=obj.getString("message");
										h.sendMessage(m);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}.start();
						
						
						 
					}
				});
				ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				ab.setView(ed);
				AlertDialog ad=ab.create();
				ad.show();
			}
		});
		
		CancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();	
				
			}
		});
		LoginUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String Email=Emailaddress.getText().toString();
				final String Pasword=LoginPasword.getText().toString();
				
				
				
				
				if(Emailaddress.getText().toString().trim().equals(""))
				{
					Toast.makeText(getBaseContext(), "Please provide Email Address", Toast.LENGTH_LONG).show();
					return;
				}
				if(LoginPasword.getText().toString().trim().equals(""))
				{
					Toast.makeText(getBaseContext(), "Please provide Pasword", Toast.LENGTH_LONG).show();
					return;
				}
				Boolean VE=isValidEmail(Email);
				if(!VE)
				{
					Toast.makeText(getBaseContext(), "Please provide Valid Email Address", Toast.LENGTH_LONG).show();
					return;
				}
				showDialog("Verifing user.");
					final Handler h=new Handler()
					{
						@Override
						public void handleMessage(Message m)
						{
							switch (m.what) {
							case 1:
							{
								CurrentLogedInUser cu=(CurrentLogedInUser)m.obj;
								Intent i=new Intent(Login_Activity.this, MainActivity.class);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								Bundle b=new Bundle();
								b.putSerializable("currentuser", cu);
								i.putExtra("cu", b);
								UpdateUserProgressDialog.cancel();
								startActivity(i);
								break;
							}
							case -1:
							{
								UpdateUserProgressDialog.cancel();
								Toast.makeText(getBaseContext(), "Login Failed: "+m.obj.toString(), Toast.LENGTH_LONG).show();
								UpdateUserProgressDialog.dismiss();
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
							CurrentLogedInUser cu=null;
							
							try {
								cu=loginUser(Email,Pasword,h);
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(cu!=null)
							{
								Message m=new Message();
								m.obj=cu;
								m.what=1;
								h.sendMessage(m);
							}
							
						}
					}.start();
				
			}

			
		});
		
	}

	private void initiateControls() {
		ForgotPasoword=(TextView)findViewById(R.id.ForgotPasoword);
		CancelButton=(Button)findViewById(R.id.CancelButton);
		Emailaddress=(EditText)findViewById(R.id.Emailaddress);
		LoginPasword=(EditText)findViewById(R.id.LoginPasword);
		LoginUserButton=(Button)findViewById(R.id.LoginUserButton);
	}
	
	private CurrentLogedInUser loginUser(String email, String pasword,Handler h) throws JSONException {
		
		CurrentLogedInUser cu=null;
		
		
		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);
		
		HttpClient client=new DefaultHttpClient(httpParameters);
		
		HttpPost post=new HttpPost(getResources().getString(R.string.url));
		ArrayList<BasicNameValuePair> arr=new ArrayList<BasicNameValuePair>();
		arr.add(new BasicNameValuePair("methodName", "signIn1"));
		arr.add(new BasicNameValuePair("email", email));
		arr.add(new BasicNameValuePair("password", pasword));
		arr.add(new BasicNameValuePair("master_id", "0"));
		arr.add(new BasicNameValuePair("lat", currentLocaitonLocator.ThisLat));
		arr.add(new BasicNameValuePair("long", currentLocaitonLocator.ThisLong));
		
		String token=(PushApplication.APID!=null)?PushApplication.APID:"";
        if(token.trim().equals(""))
        {
        	token="aa24dfsdfsdfsdf-sdfsdfsf-sdfsdf099sdfsdf-s";
        }
		
		arr.add(new BasicNameValuePair("token", token));
		//Secure.getString(getContentResolver(), Secure.ANDROID_ID)));
		Log.e("Sign in Parameters",""+arr);
		
		
		try {
			post.setEntity(new UrlEncodedFormEntity(arr));
			
			StringBuilder builder=new StringBuilder();
	        try {
	        	
	            HttpResponse response = client.execute(post);
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
	        Log.e(" login regresponse",""+builder.toString());
	        JSONObject json= new JSONObject(builder.toString());
	        if(Boolean.parseBoolean(json.getString("error"))==false)
	        {
	        	JSONObject data= json.getJSONObject("data");
	        
	        	Log.e("Login Response",""+data);
	        	
	        	cu=new CurrentLogedInUser();
		        cu.setuser_id(data.getString("user_id"));
		        cu.settoken(data.getString("token"));
//		        cu.setvehicle_id(data.getString("vehicle_Id"));
		        Log.e("This Device Toke is",Secure.getString(getContentResolver(), Secure.ANDROID_ID)+"");
		        cu.setLat(data.getString("lat"));
		        cu.setLong(data.getString("long"));
		       try{ cu.setfb_id(data.getString("fb_id"));}catch(Exception j){}
		        cu.setmaster_id(data.getString("master_id"));
		        
		        String type=data.getString("type");
		        
		        Vehicle_Info vi=new Vehicle_Info();
		        
		        vi.setvehicle_Id(data.getString("vehicle_Id"));
		        
		        
		        if(!vi.getvehicle_Id().equals("null"))
		        {
		        	
		        	vi.setvehicleName(data.getString("vehicleName"));
		        	vi.setVehicle_Type(data.getString("vehicleType"));
		        	vi.setColor(data.getString("color"));
		        	vi.setYear(data.getString("modelYear"));
		        	vi.setMake(data.getString("make"));
		        	vi.setModel(data.getString("model"));
		        	vi.setFuel_Type(data.getString("fuelType"));
		        	vi.setLicensePlate(data.getString("licensePlate"));
		        	vi.setPlateState(data.getString("licensePlate_state"));
		        	vi.Assign=data.getString("assign");
		        	
		        }
		        else
		        {
		        	vi=null;
		        }
		        cu.setVehicleComplete(vi);
		        
		        if(type.equals("1"))
		        {
		        
			        Provider p=new Provider();
			        try{p.setFirstName(data.getString("f_name"));}catch(Exception k){}
			        try{p.setLastName(data.getString("l_name"));}catch(Exception k){}
			        try{p.setCellNumber(data.getString("phone"));}catch(Exception k){}
			        try{p.setEmailAddress(data.getString("email"));}catch(Exception k){}
			        try{p.setPasword(data.getString("password"));}catch(Exception k){}
			        try{p.setEmergencyNumber(data.getString("emergency_no"));}catch(Exception k){}
			        String fleetDetail="";
			        try{fleetDetail=data.getString("fleetDetail");}catch(Exception k){}
			        try{p.setFleetVehicleCount(Integer.parseInt(fleetDetail.substring(0, fleetDetail.indexOf(","))));}catch(Exception k){}
			        try{p.setGeneralServiceDuty(fleetDetail.substring(fleetDetail.indexOf(",")+1));}catch(Exception k){}
			        try{p.setCommercialFleetInsurance(ConvertIntoBoolean(data.getString("commercialFleetInsurance")));}catch(Exception k){}
			        try{p.setProfessionallyPermittedFleet(ConvertIntoBoolean(data.getString("professionallyPermittedFleet")));}catch(Exception k){}
			        try{p.setCommercialFleetInsurance(ConvertIntoBoolean(data.getString("commerciallyLicensedDriver")));}catch(Exception k){}
			        try{p.setNATACertifiedDrivers(ConvertIntoBoolean(data.getString("NATACertifiedDriver")));}catch(Exception k){}
			        try{p.setWePayID(data.getString("paypal_username"));}catch(Exception k){}
			        try{p.setcreditcard_no(data.getString("creditcard_no"));}catch(Exception k){}
			        try{p.setcreditcard_enddate(data.getString("creditcard_enddate"));}catch(Exception k){}
			        try{p.setthreeDigitCode(data.getString("threeDigitCode"));}catch(Exception k){}
			        
			        cu.setUserDetails("provider", (Object)p);
		        }
		        else
		        {
		        	Motorist m=new Motorist();
		        	try{m.setCellNumber(data.getString("phone"));}catch(Exception k){}
		        	try{m.setExpiryDate(data.getString("creditcard_enddate"));}catch(Exception k){}
		        	try{m.setFirstName(data.getString("f_name"));}catch(Exception k){}
		        	try{m.setIEmergencyN(data.getString("emergency_no"));}catch(Exception k){}
		        	try{m.setPasword(data.getString("password"));}catch(Exception k){}
		        	try{m.setCommerciallyLicensedDriver(Motorist.ConvertIntoBoolean(data.getString("commerciallyLicensedDriver")));}catch(Exception k){}
		        	try{m.setNATACertifiedDrivers(Motorist.ConvertIntoBoolean(data.getString("NATACertifiedDriver")));}catch(Exception k){}
		        	try{m.setProfessionallyPermittedFleet(Motorist.ConvertIntoBoolean(data.getString("professionallyPermittedFleet")));}catch(Exception k){}
		        	try{m.setThreeDigitSecurityCode(data.getString("threeDigitCode"));}catch(Exception k){}
		        	try{m.setLastName(data.getString("l_name"));}catch(Exception k){}
		        	try{m.setWePayID(data.getString("paypal_username"));}catch(Exception k){}
		        	try{m.setEMailAddress(data.getString("email"));}catch(Exception k){}
		        	try{m.setmaster_id(data.getString("master_id"));}catch(Exception k){}
		        	try{m.setcreaditcard_no(data.getString("creditcard_no"));}catch(Exception k){}
		        	try{m.setExpiryDate(data.getString("creditcard_enddate"));}catch(Exception k){}
		        	try{cu.setUserDetails("motorist", (Object)m);}catch(Exception k){}
		        	
		        }	        
	        }
	        else
	        {
	        	Message m=new Message();
	        	m.what=-1;
	        	m.obj=json.getString("message");
	        	h.sendMessage(m);
	        	Log.e("Error From Server : ",json.getString("error"));
	        }
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cu;
		
	}
	public  String ConvertBoolean(Boolean b)
	{
		return ((b)?"1":"0");
	}
	public Boolean ConvertIntoBoolean(String v)
	{
		return ((v.equals("1"))?true:false);
	}
	
	public JSONObject forgotPass(String email)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        
        arr.add(new BasicNameValuePair("methodName", "forgotPass"));
        arr.add(new BasicNameValuePair("email", email));
       
        Log.e("getRefundRequest Parameters",""+arr);
        
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
	            }
	            else 
	            {

	            }
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        Log.e("getRefundRequest response",""+builder.toString());
	        JSONObject json= new JSONObject(builder.toString());
	        return json;
	       
	    }catch(Exception k){
        	
        	Log.e("Error",""+k.toString());
        	return null;
        }
    }
	
}
