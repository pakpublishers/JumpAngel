package com.citrusbits.jumpangle;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.ksoap2.serialization.SoapObject;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import businessclasses.Angel;
import businessclasses.ContactsDataSource;
import businessclasses.CurrentLogedInUser;
import businessclasses.JumpAngelCustomNotification;
import businessclasses.Motorist;
import businessclasses.Provider;
import businessclasses.PushApplication;
import businessclasses.RequestDetails;
import businessclasses.Vehicle_Info;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.z;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;

import MapClasses.MyMapFragment;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.sax.TextElementListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends FragmentActivity 
{
	CurrentLogedInUser currentLogedUser=null;
	
	String WaitTime="null";
	String MaxWaitofArrival="null";
	String Reson="";
	public Boolean JustRefereshing=false;
	
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	    JumpAngelCustomNotification._Act=MainActivity.this;
	    
	    
	}
	
	public int SelectedFilter=1;
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		
		JumpAngelCustomNotification.OutFromMainActivity=true;
		//Toast.makeText(getBaseContext(), "Activity Stoped", Toast.LENGTH_LONG).show();
	}

	
	public void startrequestingToProvider()
	{
		final int minutes=Integer.parseInt(WaitTime);//2;//asad change it//RequestDetaillayout_WaitTimeSpinner.getSelectedItemPosition();//response time
		final int arrivaltime=Integer.parseInt(MaxWaitofArrival);//RequestDetailLayout_MaxWaitForArrival.getSelectedItemPosition();//arrival time
		final String amount=RequestDetailLayout_OfferAmmount.getText().toString();
		
		currentLogedUser.setRequestAmount(RequestDetailLayout_OfferAmmount.getText().toString());
		
		final int counter=0;
		TimerStopFlag=false;
		
		final Handler h=new Handler()
		{
			@Override
			public void handleMessage(Message m)
			{
				switch (m.what) {
				case 1:
				{
					int mints=Integer.parseInt(m.arg1+"");
					int hr=mints/60;
					int mnt=mints%60;
					
					
					WaitResponseTimeTV.setText(hr+":"+((mnt<10)?("0"+mnt):mnt)+":"+((m.arg2<10)?("0"+m.arg2):m.arg2));
					break;
				}
				case 2:
				{
					WaitResponseTimeTV.setText("00:00:00");
					PushApplication.HasNotification=false;
					PushApplication.NotificationString=null;
					
					TimerStopFlag=true;
					WaitResponseTimeTV.setText("00:00:00");
					//Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
					
					setCurrentSideLayout(HomeDetailLayout);
					HomeDetailLayout.setVisibility(View.VISIBLE);
					currentLocationLocator.startLocaitonUpdates();
					break;
				}
				case 3:
				{
					showDefaultDialog("Failed to send Notification", m.obj.toString());
					WaitResponseTimeTV.setText("00:00:00");
					Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
					HomeDetailLayout.setVisibility(View.VISIBLE);
					setCurrentSideLayout(HomeDetailLayout);
					CurrentSideLayout.startAnimation(moveleft);
					Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuright);
					RequestApproveDetailLayout.startAnimation(MoveFromLeft);
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
				JSONObject response=notifications("notifications", currentLogedUser.getuser_id(),SelectedAngel.getuser_id(), "0", (minutes)+"", (arrivaltime)+"", amount,"test");
				try
				{
					
				if(response==null)
				{
					TimerStopFlag=true;
					Log.e("Time Closed","Timer Closed");
					Message m=new Message();
					m.obj="Technical Problem: Parser Exception";
					m.what=3;
					h.sendMessage(m);
				}
				else if(response.getString("error").contains("true"))
				{
					
					TimerStopFlag=true;
					Log.e("Time Closed","Timer Closed");
					Message m=new Message();
					m.obj=response.getString("message");
					m.what=3;
					h.sendMessage(m);
				}
				else
				{
					try {
						JSONArray arr=response.getJSONArray("data");
						JSONObject obj=arr.getJSONObject(0);
						
							RequestDetails dtl=new RequestDetails();
							dtl.setrequest_id(obj.getString("request_id"));
							dtl.setmotorist_id(obj.getString("motorist_id"));
							dtl.setmaster_id(obj.getString("master_id"));
							dtl.setangel_id(obj.getString("angel_id"));
							dtl.setsenting_time(obj.getString("senting_time"));
							dtl.setaccept_decline_time(obj.getString("accept_decline_time"));
							dtl.setmessage(obj.getString("message"));
							dtl.setservice(obj.getString("service"));
							dtl.setreponsetime(obj.getString("reponsetime"));
							dtl.setarrivaltime(obj.getString("arrivaltime"));
							dtl.setamount(obj.getString("amount"));
							dtl.setrequest_authorize(obj.getString("request_authorize"));
							dtl.setpush(obj.getString("push"));
							dtl.setrequest_status(obj.getString("request_status"));
							dtl.setcancelledby(obj.getString("cancelledby"));
							currentLogedUser.setrequestDetails(dtl);
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				}catch (Exception e) {
					TimerStopFlag=true;
					Log.e("Time Closed","Timer Closed");
					Message m=new Message();
					m.obj="Technical Problem: Parser Exception";
					m.what=3;
					h.sendMessage(m);
				}
			}
		}.start();
		new Thread()
		{
			int min=minutes-1;
			int sec=60;
			@Override
			public void run()
			{
				int sec=60;
				do
				{
					sec--;
					if(min==0 && sec==0)
					{
						TimerStopFlag=true;
						h.sendEmptyMessage(2);
					}
					else
					{
						if(sec==0)
						{
							min--;
							sec=60;
						}
						Message m=new Message();
						m.arg1=min;
						m.arg2=sec;
						m.what=1;
						h.sendMessage(m);
						Log.e("Timer Min,Sec 3",min+","+sec);
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(!TimerStopFlag);
			}
		}.start();
		
		Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuleft);
		RequestWaitforResponseLayout.setVisibility(View.VISIBLE);
		setCurrentSideLayout(RequestWaitforResponseLayout);
		CurrentSideLayout.startAnimation(moveleft);
		Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuleft);
		RequestApproveDetailLayout.startAnimation(MoveFromLeft);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
			if(arg1==RESULT_OK && arg2!=null)
			{
				if(arg2.hasExtra("Confirmation"))
				{
					if(arg2.getExtras().getString("Confirmation").contains("Confirmed"))
					{
						startrequestingToProvider();
						
					}
				}
				else
				if(arg2.hasExtra("myaction"))
				{
					if(arg2.getExtras().getString("myaction").trim().contains("resetandpayed"))
					{
						currentLocationLocator=new CurrentLocationLocator(MainActivity.this, getBaseContext(), false);
						
						currentLocationLocator.loadAngels("1");
						SelectedFilter=1;
						//RefereshAngelButton.setVisibility(View.GONE);
						//FilterButton.setVisibility(View.GONE);
						
						MainBackButton.setVisibility(View.GONE);
						RefereshAngelButton.setVisibility(View.VISIBLE);
						MenuButton.setVisibility(View.VISIBLE);
						FilterButton.setVisibility(View.VISIBLE);
						CurrentLocaitonButton.setVisibility(View.VISIBLE);
						
						showDefaultDialog("Amount Payed", "$"+currentLogedUser.getRequestAmount()+" payed to Angel");
						setCurrentSideLayout(FilterLayout);
						FilterLayout.setVisibility(View.VISIBLE);
						
					}
					else if(arg2.getExtras().getString("myaction").trim().contains("resetandrefund"))
					{
						currentLocationLocator=new CurrentLocationLocator(MainActivity.this, getBaseContext(), false);
						
						currentLocationLocator.loadAngels("1");
						SelectedFilter=1;
						///RefereshAngelButton.setVisibility(View.GONE);
						///FilterButton.setVisibility(View.GONE);
						
						MainBackButton.setVisibility(View.GONE);
						RefereshAngelButton.setVisibility(View.VISIBLE);
						MenuButton.setVisibility(View.VISIBLE);
						FilterButton.setVisibility(View.VISIBLE);
						CurrentLocaitonButton.setVisibility(View.VISIBLE);
						
						//showDefaultDialog("Amount Payed", "$"+currentLogedUser.getRequestAmount()+" payed to Angel");
						setCurrentSideLayout(FilterLayout);
						FilterLayout.setVisibility(View.VISIBLE);
						
					}
				}
			}
	}

	public Handler ThisHandler=new Handler()
	{
		@Override
		public void handleMessage(final Message m)
		{
			if(m.what==1)
			{
				int UserCase=1; 
				
				if(currentLogedUser.getUserCatagory().trim().equals("provider"))
				{
					UserCase=1;
				}
				else
				{
					UserCase=0;
				}
				
				switch (UserCase) {
				case 0:
				{
					String d=m.obj.toString();
					if(m.obj.toString().toLowerCase().contains("rejected your"))
					{
						AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
						ab.setTitle("Request Rejected");
						ab.setMessage(m.obj.toString());
						ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								PushApplication.HasNotification=false;
								PushApplication.NotificationString=null;
								
								TimerStopFlag=true;
								WaitResponseTimeTV.setText("00:00:00");
								//Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
								
								setCurrentSideLayout(HomeDetailLayout);
								HomeDetailLayout.setVisibility(View.VISIBLE);
								currentLocationLocator.startLocaitonUpdates();
							}
						});
						AlertDialog ad=ab.create();
						ad.show();
						
						
						
						
						
					}else
					if(m.obj.toString().toLowerCase().contains("cancelled your request"))
					{
						AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
						ab.setTitle("Request Canceled");
						ab.setMessage(m.obj.toString());
						ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								PushApplication.HasNotification=false;
								PushApplication.NotificationString=null;
								//showDialogBox("Message", m.obj.toString());
								TimerStopFlag=true;
								TimerStopFlagProviderAcceptFarwordStart=true;
								WaitResponseTimeTV.setText("00:00:00");
								setCurrentSideLayout(HomeDetailLayout);
								HomeDetailLayout.setVisibility(View.VISIBLE);
								currentLocationLocator.startLocaitonUpdates();
							}
						});
						AlertDialog ad=ab.create();
						ad.show();
						
						
						
						
					}else
						if(m.obj.toString().toLowerCase().contains("you are 100 feet away from angel"))
						{
							PushApplication.HasNotification=false;
							PushApplication.NotificationString=null;
							//showDefaultDialog("Message", m.obj.toString());
							if(CurrentSideLayout==RequestWaitforForwordLayout)
							{
								AlertDialog.Builder db=new AlertDialog.Builder(MainActivity.this);
						    	db.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										arg0.cancel();
										setCurrentSideLayout(RequestService_Completed);
										//
										//String Text="Assistance Type: "+currentLogedUser.getrequestDetails().getservice()+"\r\n";
										 //Text +="Vehivle        : "+currentLogedUser.getVehicleComplete().getYear()+","+currentLogedUser.getVehicleComplete().getvehicleName()+","+currentLogedUser.getVehicleComplete().getModel()+"\r\n";
										 //Text +="Color          : "+currentLogedUser.getVehicleComplete().getColor()+"\r\n";
										 //Text +="License Plate  : "+currentLogedUser.getVehicleComplete().getLicensePlate()+"\r\n";
										 //Text +="Charge Pending : $"+currentLogedUser.getRequestAmount();
										 //ServiceCompletedDetail_TV.setText(Text);
										 
										 String[] arr=getResources().getStringArray(R.array.Services_Array);
										 try
											
										 	{
											SC_AssistanceType.setText(arr[Integer.parseInt(currentLogedUser.getrequestDetails().getservice())]);
											}catch (Exception e) {}
											
											SC_Vehicle.setText(currentLogedUser.getVehicleComplete().getYear()+","+currentLogedUser.getVehicleComplete().getvehicleName()+","+currentLogedUser.getVehicleComplete().getModel());
											SC_Color.setText(currentLogedUser.getVehicleComplete().getColor());
											SC_LicencePlate.setText(currentLogedUser.getVehicleComplete().getLicensePlate());
											VC_ChargePending.setText(" $"+currentLogedUser.getRequestAmount());
											TimerStopFlagFarword=true;
											//GetAngelLocationFlagForTime=true;
											GetAngelLocationFlagForTime=false;
										RequestService_Completed.setVisibility(View.VISIBLE);
									}
								});
						    	db.setTitle("Visual Confirmed");
						    	db.setMessage("you are 100 feet away from angel");
						    	AlertDialog ad=db.create();
						    	ad.show();
							}
							
						}
					else
					if(m.obj.toString().toLowerCase().contains("accepted your request"))
					{
						PushApplication.HasNotification=false;
						PushApplication.NotificationString=null;
						showDialogBox("Message", m.obj.toString());
						TimerStopFlag=true;
						startAcceptenceFunciton();
						try{
						currentLocationLocator.stopLocationUpdates();
						}catch(Exception k)
						{}
						
						
						//String Text="Assistance Type: "+currentLogedUser.getrequestDetails().getservice()+"\r\n";
							// Text +="Vehivle        : "+currentLogedUser.getVehicleComplete().getYear()+","+currentLogedUser.getVehicleComplete().getvehicleName()+","+currentLogedUser.getVehicleComplete().getModel()+"\r\n";
							 //Text +="Color          : "+currentLogedUser.getVehicleComplete().getColor()+"\r\n";
							 //Text +="License Plate  : "+currentLogedUser.getVehicleComplete().getLicensePlate()+"\r\n";
						//RequestFarword_DetailTextView.setText(Text);
						
							 
						String[] arr=getResources().getStringArray(R.array.Services_Array);
						try
						{
							VC_AssistanceType.setText(arr[Integer.parseInt(currentLogedUser.getrequestDetails().getservice())]);
						}catch (Exception e) {
							// TODO: handle exception
						}
						
						VC_Vehicle.setText(currentLogedUser.getVehicleComplete().getYear()+","+currentLogedUser.getVehicleComplete().getvehicleName()+","+currentLogedUser.getVehicleComplete().getModel());
						VC_Color.setText(currentLogedUser.getVehicleComplete().getColor());
						VC_LicencePlate.setText(currentLogedUser.getVehicleComplete().getLicensePlate());
						
						GetAngelLocationFlagForTime=true;
						startlocateProvider();
						
						RequestWaitforForwordLayout.setVisibility(View.VISIBLE);
						setCurrentSideLayout(RequestWaitforForwordLayout);
					
					}
					else
					{
						PushApplication.HasNotification=false;
						PushApplication.NotificationString=null;
						showDialogBox("Message", m.obj.toString());
					}
					
					break;
				}
				case 1:
				{
					String push=m.obj.toString();
					Log.e("Push Recieved",push);
					
					RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
					if(lp.height!=(int)(200*getResources().getDisplayMetrics().density))
					{
						lp.height=(int)(200*getResources().getDisplayMetrics().density); 
						AngleDetailLayout.setLayoutParams(lp);
						
					}
					
					
					final String  p=push;
					if(m.obj.toString().toLowerCase().contains("refund request"))
					{
						PushApplication.HasNotification=false;
						PushApplication.NotificationString=null;
						AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
					    ab.setPositiveButton("View", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								String TID=p.substring(p.indexOf(":")+1).trim();
								Intent i=new Intent(MainActivity.this, RefundRequest.class);
								i.putExtra("Transactionid", TID.trim());
								startActivityForResult(i, 0);
							}
					    });
					    ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
					    ab.setTitle("Refund Request Recieved");
					    ab.setMessage(push);
					    AlertDialog ad=ab.create();
					    ad.show();
					    
					}
					else if(m.obj.toString().toLowerCase().contains("requesting help."))
					{
						TimerStopFlagProviderAcceptDeclineStart=false;
						//showDefaultDialog("Request Recieved", );
						
						PushApplication.HasNotification=false;
						PushApplication.NotificationString=null;
						
						AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
					    ab.setPositiveButton("View", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								
								RefereshAngelButton.setVisibility(View.INVISIBLE);
						    	FilterButton.setVisibility(View.INVISIBLE);
								
							    setCurrentSideLayout(RequestProviderSide_AcceptDeclineLayout);
							    FilterLayout.setVisibility(View.GONE);
							    RequestProviderSide_AcceptDeclineLayout.setVisibility(View.VISIBLE);
							    
							    loadRequestDetailsAndStartTimeForProvier();
							    
							    RequestProviderSide_AcceptButton.setOnClickListener(new View.OnClickListener() {
							    	
							    	@Override
							    	public void onClick(View v) {
							    		showDialog("Loading");
							    		final Handler hndlr=new Handler()
							    		{
							    			@Override
							    			public void handleMessage(Message m)
							    			{
							    				
							    				
							    				Toast.makeText(getBaseContext(), "Accept Request Sent.", Toast.LENGTH_LONG).show();
							    				TimerStopFlagProviderAcceptDeclineStart=true;
							    				RequestProviderSide_AcceptDeclineTimer.setText("00:00:00");
							    				
							    				UpdateUserProgressDialog.cancel();
							    				
							    				String D="Assistence Type : \r\n";
							    				D+="Vehicle :"+RequestVehicle.getvehicleName()+" \r\n";
							    				D+="Licence Plate :"+RequestVehicle.getMake()+" \r\n";
							    				D+="Driver Name: "+RequestMotorist.getFirstName()+" "+RequestMotorist.getLastName()+"\r\n";
							    				
							    				
							    				try{
							    					PRO_PLUS_AssistanceType.setText(getResources().getStringArray(R.array.Services_Array)[Integer.parseInt(currentLogedUser.getrequestDetails().getservice().trim())]);
							    					}catch(Exception k){}
							    					
							    					PRO_PLUS_Vehicle.setText(
							    							RequestVehicle.getvehicleName()+", "+RequestVehicle.getMake()+", "+RequestVehicle.getYear()+", "+RequestVehicle.getColor()
							    							);
							    					PRO_PLUS_Color.setText("$"+currentLogedUser.getrequestDetails().getamount());
							    					PRO_PLUS_LicencePlate.setText(RequestVehicle.getLicensePlate());
							    					
							    					
							    				
							    				//RequestProviderSide_FarwordDetailTV.setText(D);
							    				
							    				currentLocationLocator.startLocationUpdatesAndUpdateProviderLocation();
							    				
							    				setCurrentSideLayout(RequestProviderSide_FarwordLayout);
							    				RequestProviderSide_FarwordLayout.setVisibility(View.VISIBLE);
							    				int timeforwait =Integer.parseInt((currentLogedUser.getrequestDetails().getarrivaltime().split(":")[0]));
							    				
							    				
							    				int minuts=-1;
							    				if(currentLogedUser.getrequestDetails().getarrivaltime().trim().contains(":"))
							    				{
							    					String[] arr=currentLogedUser.getrequestDetails().getarrivaltime().trim().split(":");
							    					minuts=(Integer.parseInt(arr[0])*60)+(Integer.parseInt(arr[1]));
							    				}
							    				else
							    				{
							    					minuts=Integer.parseInt(currentLogedUser.getrequestDetails().getarrivaltime().trim());
							    				}
												int hr=minuts/60;
												int mnt=minuts%60;
												
												RequestProviderSide_FarwordTotalTimeTV.setText(hr+" hr, "+mnt+" min");
							    				startTimerForProviderForware(timeforwait);
							    				// i m here
							    				
							    				startupdatingProvider();
							    			}
							    		};
							    		
							    		new Thread()
							    		{
							    			@Override
							    			public void run()
							    			{
							    				acceptORreject_request(currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getrequestDetails().getmotorist_id(), currentLogedUser.getuser_id(), "1", "0", "Bid Amount too high");
							    				hndlr.sendEmptyMessage(1);
							    			}
							    		}.start();
							    		
							    	}
							    });
							    
							   
							    RequestProviderSide_CancelButton.setOnClickListener(new View.OnClickListener() {
							    	//comeback
							    	@Override
							    	public void onClick(View vi) {
							    		
							    		
							    		AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
							    		ab.setTitle("Jump Angels");
							    		ab.setMessage("Reason for decline.");
							    		ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												dialog.cancel();
											}
										});
										final AlertDialog ad=ab.create();
										
										
										View v=LayoutInflater.from(MainActivity.this).inflate(R.layout.reasondialog, null);
										
										LinearLayout TP=(LinearLayout)v.findViewById(R.id.MainRLayout);
										for(int i=0; i<TP.getChildCount(); i++)
										{
											Button c=(Button)TP.getChildAt(i);
											View.OnClickListener cl=new View.OnClickListener() {
												
												@Override
												public void onClick(View v) {
													ad.cancel();
													Reson=((Button)v).getText().toString();
													cancelReqest("2");
													currentLocationLocator.startLocaitonUpdates();
												}
											};
											c.setOnClickListener(cl);
											
										}
										ad.setView(v);
										ad.show();
//										
//							    		
//							    		
//							    		final Handler hndlr=new Handler()
//							    		{
//							    			@Override
//							    			public void handleMessage(Message m)
//							    			{
//							    				Toast.makeText(getBaseContext(), "Request Canceled Successfully", Toast.LENGTH_LONG).show();
//							    				TimerStopFlagProviderAcceptDeclineStart=true;
//							    				RequestProviderSide_AcceptDeclineTimer.setText("00:00:00");
//							    				
//							    				setCurrentSideLayout(HomeDetailLayout);
//							    				HomeDetailLayout.setVisibility(View.VISIBLE);
//							    			}
//							    		};
//							    		
//							    		new Thread()
//							    		{
//							    			@Override
//							    			public void run()
//							    			{
//							    				acceptORreject_request(currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getrequestDetails().getmotorist_id(), currentLogedUser.getuser_id(), "2", "0", "Bid Amount too high");
//							    				hndlr.sendEmptyMessage(1);
//							    			}
//							    		}.start();
							    	}
							    });
							    

								
							}
						});
					    ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
					    ab.setTitle("Request recieved");
					    ab.setMessage(push);
					    AlertDialog ad=ab.create();
					    ad.show();
						
					}
					else
						if(m.obj.toString().toLowerCase().contains("you are 100 feet away from"))
						{
							//showDefaultDialog("Message", m.obj.toString());
							
							PushApplication.HasNotification=false;
							PushApplication.NotificationString=null;
							
							AlertDialog.Builder db=new AlertDialog.Builder(MainActivity.this);
					    	db.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									arg0.cancel();
									setCurrentSideLayout(RequestService_Completed);
									UpdateAngelLocaitonFlag=false;
									GetAngelLocationFlagForTime=false;
									 String[] arr=getResources().getStringArray(R.array.Services_Array);
										try
										{
											SC_AssistanceType.setText(arr[Integer.parseInt(currentLogedUser.getrequestDetails().getservice())]);
										}catch (Exception e) {
										}
										SC_Vehicle.setText(RequestVehicle.getvehicleName()+", "+RequestVehicle.getMake()+", "+RequestVehicle.getYear());
										
										SC_Color.setText(RequestVehicle.getColor());
										SC_LicencePlate.setText(RequestVehicle.getLicensePlate());
										VC_ChargePending.setText(" $"+currentLogedUser.getrequestDetails().getamount());
										TimerStopFlagProviderAcceptFarwordStart=true;
										RequestService_Completed.setVisibility(View.VISIBLE);
								}
							});
					    	
					    	db.setTitle("Visual Confirmed");
					    	db.setMessage(push);
					    	AlertDialog ad=db.create();
					    	ad.show();
							
						}
					if(m.obj.toString().toLowerCase().contains("cancelled your request"))
					{
						AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
						ab.setTitle("Request Canceled");
						ab.setMessage(m.obj.toString());
						ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								PushApplication.HasNotification=false;
								PushApplication.NotificationString=null;
								//showDialogBox("Message", m.obj.toString());
								TimerStopFlag=true;
								TimerStopFlagProviderAcceptFarwordStart=true;
								WaitResponseTimeTV.setText("00:00:00");
								setCurrentSideLayout(FilterLayout);
								UpdateAngelLocaitonFlag=false;
								//come here
								FilterLayout.setVisibility(View.VISIBLE);
								currentLocationLocator.startLocaitonUpdates();
							}
						});
						AlertDialog ad=ab.create();
						ad.show();
					}
					else
					{	
						RelativeLayout.LayoutParams lpp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
						lpp.height=(int)(80*getResources().getDisplayMetrics().density); 
						AngleDetailLayout.setLayoutParams(lpp);
						PushApplication.HasNotification=false;
						PushApplication.NotificationString=null;
						showDialogBox("Message",push);
					}
					
					break;
				}
	
				default:
					break;
				}
		}
		}
	};
	
	Boolean TimerStopFlagProviderAcceptDeclineStart=false;
	Boolean TimerStopFlagProviderAcceptFarwordStart=false;
	
	public void startTimerForProviderForware(final int minutes)
	{
		final Handler h=new Handler()
		{
			@Override
			public void handleMessage(Message m)
			{
				switch (m.what) {
				case 4:
				{
					RequestProviderSide_FarwordTimeRemaningTV.setText("00:00:00");
					// 
					
					final Handler hndlr=new Handler()
					{
						@Override
						public void handleMessage(Message m)
						{
							Toast.makeText(getBaseContext(), "Time out", Toast.LENGTH_LONG).show();
							TimerStopFlagProviderAcceptDeclineStart=true;
							RequestProviderSide_AcceptDeclineTimer.setText("00:00:00");
							
							setCurrentSideLayout(HomeDetailLayout);
							HomeDetailLayout.setVisibility(View.VISIBLE);
						}
					};
					
					new Thread()
					{
						@Override
						public void run()
						{
							acceptORreject_request(currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getrequestDetails().getmotorist_id(), currentLogedUser.getuser_id(), "3", "0", "Time out");
							hndlr.sendEmptyMessage(1);
						}
					}.start();
					
					
					break;
				}
				case 2:
				{
					
					int minuts=Integer.parseInt(m.arg1+"");
					int hr=minuts/60;
					int mnt=minuts%60;
					
					RequestProviderSide_FarwordTimeRemaningTV.setText(hr+":"+mnt+":"+m.arg2+"");
					break;
				}

				default:
					break;
				}
			}
		};
		new Thread()
		{
			int min=minutes-1;
			int sec=60;
			@Override
			public void run()
			{
				int sec=60;
				do
				{
					sec--;
					if(min==0 && sec==0)
					{
						TimerStopFlagProviderAcceptFarwordStart=true;
						h.sendEmptyMessage(4);
					}
					else
					{
						if(sec==0)
						{
							min--;
							sec=60;
						}
						Message m=new Message();
						m.arg1=min;
						m.arg2=sec;
						m.what=2;
						h.sendMessage(m);
						Log.e("Timer Min asad,Sec",min+","+sec);
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(!TimerStopFlagProviderAcceptFarwordStart);
			}
		}.start();
	}
	
	
	public void startProvideResponseTimer(final int minutes)
	{
		final Handler h=new Handler()
		{
			@Override
			public void handleMessage(Message m)
			{
				switch (m.what) {
				case 4:
				{
					RequestProviderSide_AcceptDeclineTimer.setText("00:00:00");
					// 
					
					final Handler hndlr=new Handler()
					{
						@Override
						public void handleMessage(Message m)
						{
							Toast.makeText(getBaseContext(), "Time out", Toast.LENGTH_LONG).show();
							TimerStopFlagProviderAcceptDeclineStart=true;
							RequestProviderSide_AcceptDeclineTimer.setText("00:00:00");
							
							setCurrentSideLayout(HomeDetailLayout);
							HomeDetailLayout.setVisibility(View.VISIBLE);
						}
					};
					
					
					
					new Thread()
					{
						@Override
						public void run()
						{
							acceptORreject_request(currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getrequestDetails().getmotorist_id(), currentLogedUser.getuser_id(), "2", "0", "Time out");
							hndlr.sendEmptyMessage(1);
						}
					}.start();
					
					
					
					break;
				}
				case 2:// asadhashmi
				{
					int minuts=Integer.parseInt(m.arg1+"");
					int hr=minuts/60;
					int mnt=minuts%60;
					
					RequestProviderSide_AcceptDeclineTimer.setText(hr+":"+((mnt<10)?("0"+mnt):mnt)+":"+((m.arg2<10)?("0"+m.arg2):m.arg2));
					//RequestProviderSide_AcceptDeclineTimer.setText("00:"+m.arg1+":"+m.arg2+"");
					break;
				}

				default:
					break;
				}
			}
		};
		new Thread()
		{
			int min=minutes-1;
			int sec=60;
			@Override
			public void run()
			{
				int sec=60;
				do
				{
					sec--;
					if(min==0 && sec==0)
					{
						TimerStopFlagProviderAcceptDeclineStart=true;
						h.sendEmptyMessage(4);
					}
					else
					{
						if(sec==0)
						{
							min--;
							sec=60;
						}
						Message m=new Message();
						m.arg1=min;
						m.arg2=sec;
						m.what=2;
						h.sendMessage(m);
						Log.e("Timer Min,Sec 1",min+","+sec);
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(!TimerStopFlagProviderAcceptDeclineStart);
			}
		}.start();
	}
	
	public void loadRequestDetailsAndStartTimeForProvier()
	{
		final Handler h=new Handler()
		{
			@Override
			public void handleMessage(final Message m)
			{
				switch (m.what) {
				case 1:
				{
//					String D=RequestMotorist.getFirstName()+" "+RequestMotorist.getLastName()+"\r\n \r\n";
//					D+="Vehicle: "+RequestVehicle.getvehicleName()+"\r\n";
//					D+="Assistance Type: \r\n";
//					D+="Provider Rated :\r\n";
//					D+="Max. Wait Time: "+currentLogedUser.getrequestDetails().getreponsetime()+"\r\n";
//					
					
					try{
					PRO_AssistanceType.setText(getResources().getStringArray(R.array.Services_Array)[Integer.parseInt(currentLogedUser.getrequestDetails().getservice().trim())]);
					}catch(Exception k){}
					
					PRO_Vehicle.setText(
							RequestVehicle.getvehicleName()+", "+RequestVehicle.getMake()+", "+RequestVehicle.getYear()+", "+RequestVehicle.getColor()
							);
					PRO_Color.setText("$"+currentLogedUser.getrequestDetails().getamount());
					PRO_LicencePlate.setText(RequestVehicle.getLicensePlate());
					PRO_MaxWaitTime.setText(currentLogedUser.getrequestDetails().getreponsetime()+"min");
					
					int mint=Integer.parseInt(currentLogedUser.getrequestDetails().getreponsetime().split(" min")[0]);
					startProvideResponseTimer(mint);
					
					//RequestProviderSide_AcceptDeclineDetailTV.setText(D);
					
					UpdateUserProgressDialog.cancel();
					
					break;
				}
				case -1:
				{
					Toast.makeText(getBaseContext(), "Conneciton problem", Toast.LENGTH_SHORT).show();
					break;
				}

				default:
					break;
				}
			}
		};
		showDialog("loading Request Details");
		//showDefaultDialog("Please wait", "Loading Request Details.");
		new Thread()
		{
			@Override
			public void run()
			{
				String response= load_RequestDetails_of_Motorist();
				if(!response.contains("failed"))
				{
					JSONObject obj=null;
					try {
						obj=new JSONObject(response);
						
						RequestDetails rd=new RequestDetails();
						rd.setrequest_id(obj.getString("request_id"));
						rd.setmotorist_id(obj.getString("motorist_id"));
						rd.setmaster_id(obj.getString("master_id"));
						rd.setangel_id(obj.getString("angel_id"));
						rd.setsenting_time(obj.getString("senting_time"));
						rd.setaccept_decline_time(obj.getString("accept_decline_time"));
						rd.setmessage(obj.getString("message"));
						rd.setservice(obj.getString("service"));
						rd.setreponsetime(obj.getString("reponsetime"));
						rd.setarrivaltime(obj.getString("arrivaltime"));
						
						rd.setamount(obj.getString("amount"));
						
						currentLogedUser.setrequestDetails(rd);
						RequestVehicle=new Vehicle_Info();
						
						RequestVehicle.setvehicleName(obj.getString("vehicleName"));
						RequestVehicle.setVehicle_Type(obj.getString("vehicleType"));
						RequestVehicle.setYear(obj.getString("modelYear"));
						RequestVehicle.setMake(obj.getString("make"));
						RequestVehicle.setModel(obj.getString("model"));
						RequestVehicle.setFuel_Type(obj.getString("fuelType"));
						RequestVehicle.setColor(obj.getString("color"));
						RequestVehicle.setLicensePlate(obj.getString("licensePlate"));
						
						RequestMotorist=new Motorist();
						RequestMotorist.setLat(obj.getString("lat"));
						RequestMotorist.setLong(obj.getString("long"));
						RequestMotorist.setFirstName(obj.getString("f_name"));
						RequestMotorist.setLastName(obj.getString("l_name"));
						RequestMotorist.setEMailAddress(obj.getString("email"));
						RequestMotorist.setCellNumber(obj.getString("phone"));
						
						h.sendEmptyMessage(1);
						Log.e("response",obj.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					h.sendEmptyMessage(-1);
				}
				
			}
		}.start();
	}
	
	public String load_RequestDetails_of_Motorist()
	{
		HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", "get_helprequest"));
        arr.add(new BasicNameValuePair("angel_id",   currentLogedUser.getuser_id()));
        
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
	        
	        Log.e("regresponse",""+builder.toString());
	        JSONObject json= new JSONObject(builder.toString());
	        if(Boolean.parseBoolean(json.getString("error"))==false)
	        {
	        	JSONArray arrr=json.getJSONArray("data");
	        	JSONObject data= arrr.getJSONObject(0);
	        	//Toast.makeText(getBaseContext(), "Secceeded: "+json.getString("message"), Toast.LENGTH_LONG).show();
	        	Log.e("Response",data.toString());
	        	return data.toString();
	        }
	        else
	        {
	        	String Messageis=json.getString("message");
	        	
				return Messageis ;
	        	
	        }
	    }catch(Exception k){
        	
        	Log.e("Error",""+k.toString());
        	return "failed";
        }
	}
	
	public void startProvideAcceptDeclineTimer()
	{
		
	}
	

	public Boolean GetAngelLocationFlagForTime=false;
	
	
	public void startupdatingProvider()
	{
		
		
		
		
		currentLocationLocator=new CurrentLocationLocator(MainActivity.this, getBaseContext(), true);
		final Handler h=new Handler()
		{
			@Override
			public void handleMessage(Message m)
			{
				switch (m.what) {
				case 1:
				{
					mMap.clear();
					
					MarkerOptions mo=new MarkerOptions();
					
					final LatLng mLocation=new LatLng(Double.parseDouble(RequestMotorist.getLat()), Double.parseDouble(RequestMotorist.getLong()));
					mo.position(mLocation);
					//Motorist mtrst=((Motorist)currentLogedUser.getUserObject());
					
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
					mMap.addMarker(mo);
					
					MarkerOptions pro=new MarkerOptions();
					final LatLng PromLocation=new LatLng(Double.parseDouble(currentLocationLocator.ThisLat), Double.parseDouble(currentLocationLocator.ThisLong));
					pro.position(PromLocation);
					
					//pro.title(SelectedAngel.getf_name()+" "+SelectedAngel.getl_name());
					
					pro.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconangelwithcircle));
					mMap.addMarker(pro);
					
//					final Handler hk=new Handler()
//					{
//						@Override
//						public void handleMessage(Message m)
//						{
//							DistenceCheck.setText("Distance Value:"+DistanceValue+",Distance Text:"+DistanceText);
//						}
//					};
//					
//					new Thread()
//					{
//						@Override
//						public void run()
//						{
//							Distance(mLocation, PromLocation);
//							hk.sendEmptyMessage(2);
//						}
//					}.start();
					
					break;
				}
				case 2:
				{
					
					break;
				}
				default:
					break;
				}
			}
		};
		UpdateAngelLocaitonFlag=true;
		new Thread()
		{
			@Override
			public void run()
			{
				do
				{
					currentLocationLocator.refreshLocation();
					
					JSONObject response=updatelocation(currentLogedUser.getuser_id(), currentLogedUser.getrequestDetails().getrequest_id());
					
					Log.e("updatelocation response",""+response.toString());
					
					try
					{
					if(response==null)
					{
						
					}
					else if(response.getString("error").contains("true"))
					{
						startlocateProvider();
					}
					else {
						//Log.e("response is ",response);
						JSONObject obj=null;
						try {
							obj = response.getJSONObject("data");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//try{SelectedAngel.setlat(obj.getString("lat"));}catch(Exception k){}
						//try{SelectedAngel.setf_name(obj.getString("f_name"));}catch(Exception k){}
						//try{SelectedAngel.setl_name(obj.getString("l_name"));}catch(Exception k){}
						//try{SelectedAngel.setuser_id(obj.getString("user_id"));}catch(Exception k){}
						//try{SelectedAngel.setlng(obj.getString("long"));}catch(Exception k){}
						h.sendEmptyMessage(1);
					}
					}
					catch(Exception k){}
					
					try {Thread.sleep(2000);} catch (InterruptedException e) {}
					
				}while(UpdateAngelLocaitonFlag);
			}
		}.start();
	}
	
	public void justShowAllAngels()
	{
		Angel CurrentSelectedAngel= AngelsHashMap.get(currentLocationLocator.AngelMarkerPointers.get(Angel.selectedangelindex));
		
		SelectedAngel=CurrentSelectedAngel;
		
		
		mMap.clear();
		
		ArrayList<String> AngelMarkerPointers=new ArrayList<String>();
		
		HashMap<String , Angel> HashMapRelpacer=new HashMap<String, Angel>();
		
		for(int j=0; j<currentLocationLocator.AngelMarkerPointers.size(); j++)
		{
			AngelMarkerPointers.add(currentLocationLocator.AngelMarkerPointers.get(j));
			HashMapRelpacer.put(AngelMarkerPointers.get(j), AngelsHashMap.get(currentLocationLocator.AngelMarkerPointers.get(j)));
		}
		currentLocationLocator.AngelMarkerPointers.clear();
		
		AngelsHashMap.clear();
		for(int i=0; i<AngelMarkerPointers.size(); i++)
		{
			
			String key=AngelMarkerPointers.get(i);
			
			Angel tempAngel=(Angel)HashMapRelpacer.get(key);
			MarkerOptions AngelMarker=new MarkerOptions();
			
			AngelMarker.position(new LatLng(Double.parseDouble(tempAngel.getlat().trim()), Double.parseDouble(tempAngel.getlng().trim())));
			AngelMarker.title(tempAngel.getf_name()+" "+tempAngel.getl_name());
			
			if(SelectedAngel.getuser_id().equals(tempAngel.getuser_id()))
			{
				AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconangelwithcircle));
			}
			else
			{
				AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.jumpangel));
			}
			
			if(tempAngel.getVehicle()!=null)
				AngelMarker.snippet(tempAngel.getVehicle().getvehicleName());
			String markerpointer=mMap.addMarker(AngelMarker).getId();
			currentLocationLocator.AngelMarkerPointers.add(markerpointer);
			AngelsHashMap.put(markerpointer, tempAngel);
		}
		
		for(int j=0; j<currentLocationLocator.AngelMarkerPointers.size(); j++)
		{
			AngelMarkerPointers.add(currentLocationLocator.AngelMarkerPointers.get(j));
		}
		
		
		MarkerOptions MyLocation=new MarkerOptions();
   	 	MyLocation.position(new LatLng(Double.parseDouble(currentLocationLocator.ThisLat), Double.parseDouble(currentLocationLocator.ThisLong)));
   	 	MyLocation.title("Motorist");
   	 	MyLocation.snippet("My Location");
   	 	MyLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
   	 	
		mMap.addMarker(MyLocation);
		
		
		
		
		CompanyNameTV.setText(CurrentSelectedAngel.getf_name()+" "+CurrentSelectedAngel.getl_name());
		
		Double d=null;
			try
			{
				DecimalFormat df=new DecimalFormat("#0.00");
				
				d=Double.parseDouble(CurrentSelectedAngel.getdistance());
				
			    d=Double.parseDouble(df.format(d));
			}
			catch(Exception k){}
			
			DistanceTV.setText(CurrentSelectedAngel.getdistance());
		
		RattingTV.setRating(Float.parseFloat(CurrentSelectedAngel.rating));
		
		int speed=25;
		double Time=0;
		try{
			Time=Math.round(Double.parseDouble(CurrentSelectedAngel.getdistance().split(" ")[0])/speed);
		}catch(Exception k){}
		ETimeTV.setText(CurrentSelectedAngel.getduration());
		SelectedAngel=CurrentSelectedAngel;
		ServicingLayout.removeAllViews();
		FuelRequestLayout.removeAllViews();
		
		CameraUpdate CameraToMotoristLocaiton=CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(CurrentSelectedAngel.getlat()),Double.parseDouble(CurrentSelectedAngel.getlng())), 20);
   	 	
		Log.e("Animating to point",CurrentSelectedAngel.getlat() +""+CurrentSelectedAngel.getlng());
		
   	 	mMap.animateCamera(CameraToMotoristLocaiton);
		
			if(CurrentSelectedAngel.getVehicle()!=null)
			{
				if(SelectedFilter==1 && !CurrentSelectedAngel.getVehicle().getBattery_Jumps().contains("off"))
				{
					if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("light"))
					{
						ServicingLayout.addView(getLabel("L", "Light Duty"));
					}
					if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("medium"))
					{
						ServicingLayout.addView(getLabel("M", "Medium Duty"));
					}
					if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("heavy"))
					{
						ServicingLayout.addView(getLabel("H", "Heavy Duty"));
					}
					
				}
				else if(SelectedFilter==2 && !CurrentSelectedAngel.getVehicle().getFuel_Delivery().contains("off"))
				{
					if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
					{
						FuelRequestLayout.addView(getLabel("D", "Diesel"));
					}
					if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
					{
						FuelRequestLayout.addView(getLabel("C", "CNG"));
					}
					if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("petrol"))
					{
						FuelRequestLayout.addView(getLabel("G", "Gasoline/Petrol"));
					}
				}
				else if(SelectedFilter==3)
				{
					
						ServicingLayout.addView(getLabel("L", "Light Duty"));
					
						ServicingLayout.addView(getLabel("M", "Medium Duty"));
					
						ServicingLayout.addView(getLabel("H", "Heavy Duty"));
					
					
					if(!CurrentSelectedAngel.getVehicle().getTow_Boom().contains("off"))
					{
						FuelRequestLayout.addView(getLabel("B", "Tow Boom"));
					}
					if(!CurrentSelectedAngel.getVehicle().getTow_FlatBed().contains("off"))
					{
						FuelRequestLayout.addView(getLabel("F", "Flat Bed"));
					}
					if(!CurrentSelectedAngel.getVehicle().getTow_Sling().contains("off"))
					{
						FuelRequestLayout.addView(getLabel("S", "Tow Sling"));
					}
					if(!CurrentSelectedAngel.getVehicle().getTow_WheelLift().contains("off"))
					{
						FuelRequestLayout.addView(getLabel("WL", "Tow Wheel Lift"));
					}
					if(!CurrentSelectedAngel.getVehicle().getTow_Winch().contains("off"))
					{
						FuelRequestLayout.addView(getLabel("W", "Tow Winch"));
					}
				}
				else if(SelectedFilter==4 && !CurrentSelectedAngel.getVehicle().getTire_Changes().contains("off"))
				{
					if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("light"))
					{
						ServicingLayout.addView(getLabel("L", "Light Duty"));
					}
					if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("medium"))
					{
						ServicingLayout.addView(getLabel("M", "Medium Duty"));
					}
					if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("heavy"))
					{
						ServicingLayout.addView(getLabel("H", "Heavy Duty"));
					}
					
					if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
					{
						FuelRequestLayout.addView(getLabel("CH", "Tire Change"));
					}
					if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
					{
						FuelRequestLayout.addView(getLabel("R", "Mobile Tire Repair"));
					}
					
				}
				else if(SelectedFilter==5 && !CurrentSelectedAngel.getVehicle().getDiagnostics().contains("off"))
				{
					if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("diesel"))
					{
						ServicingLayout.addView(getLabel("D", "Diesel"));
					}
					if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("cng"))
					{
						ServicingLayout.addView(getLabel("C", "CNG"));
					}
					if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("petrol"))
					{
						ServicingLayout.addView(getLabel("G", "Gasoline/Petrol"));
					}
				}
				else if(SelectedFilter==6 && !CurrentSelectedAngel.getVehicle().getVehicle_Unlocks().contains("off"))
				{
				//	ServicingLayout.addView(getLabel("V", "Vehicle Unlocks"));
				}
			}
	}
	
	public void justShowSelectedProvider()
	{
		mMap.clear();
		
		MarkerOptions mo=new MarkerOptions();
		
		LatLng mLocation=new LatLng(Double.parseDouble(currentLocationLocator.ThisLat), Double.parseDouble(currentLocationLocator.ThisLong));
		mo.position(mLocation);
		//asadtest
		Motorist mtrst=((Motorist)currentLogedUser.getUserObject());
		mo.title(mtrst.getFirstName()+" "+mtrst.getLastName());
		
		mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
		mMap.addMarker(mo);
		
		MarkerOptions pro=new MarkerOptions();
		LatLng PromLocation=new LatLng(Double.parseDouble(SelectedAngel.getlat()), Double.parseDouble(SelectedAngel.getlng()));
		pro.position(PromLocation);
		
		pro.title(SelectedAngel.getf_name()+" "+SelectedAngel.getl_name());
		
		pro.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconangelwithcircle));
		mMap.addMarker(pro);
	}
	
	
	public void startlocateProvider()
	{
		
		final Handler h=new Handler()
		{
			@Override
			public void handleMessage(Message m)
			{
				switch (m.what) {
				case 1:
				{
					mMap.clear();
					
					MarkerOptions mo=new MarkerOptions();
					
					LatLng mLocation=new LatLng(Double.parseDouble(currentLocationLocator.ThisLat), Double.parseDouble(currentLocationLocator.ThisLong));
					mo.position(mLocation);
					//asadtest
					Motorist mtrst=((Motorist)currentLogedUser.getUserObject());
					mo.title(mtrst.getFirstName()+" "+mtrst.getLastName());
					
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
					mMap.addMarker(mo);
					
					MarkerOptions pro=new MarkerOptions();
					LatLng PromLocation=new LatLng(Double.parseDouble(SelectedAngel.getlat()), Double.parseDouble(SelectedAngel.getlng()));
					pro.position(PromLocation);
					
					pro.title(SelectedAngel.getf_name()+" "+SelectedAngel.getl_name());
					
					pro.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconangelwithcircle));
					mMap.addMarker(pro);
					
					
					break;
				}
				case 2:
				{
					
					int minuts=Integer.parseInt(m.arg1+"");
					int hr=minuts/60;
					int mnt=minuts%60;
					
					
					
					RequestFarword_Timer.setText(hr+":"+((mnt<10)?("0"+mnt):mnt)+":"+((m.arg2<10)?("0"+m.arg2):m.arg2));
					break;
				}
				case 3:
				{
					RequestFarword_Timer.setText("00:00:00");
					break;
				}
				case 4:
				{
					//showDefaultDialog("Failed to send Notification", m.obj.toString());
					RequestFarword_Timer.setText("00:00:00");
					Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
					HomeDetailLayout.setVisibility(View.VISIBLE);
					setCurrentSideLayout(HomeDetailLayout);
					CurrentSideLayout.startAnimation(moveleft);
					Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuright);
					RequestApproveDetailLayout.startAnimation(MoveFromLeft);
				}

				default:
					break;
				}
			}
		};
		GetAngelLocationFlagForTime=true;
		new Thread()
		{
			@Override
			public void run()
			{
				do
				{
					String response=getupdatelocation(currentLogedUser.getrequestDetails().getangel_id());
					Log.e("getupdatelocation response",""+response);
					Log.e("GetAngelLocationFlagForTime",""+GetAngelLocationFlagForTime);
					if(response.toLowerCase().trim().contains("failed due to"))
					{
						startlocateProvider();
					}
					else {
						Log.e("response is ",response);
						JSONObject obj=null;
						try {
							obj = new JSONObject(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try{SelectedAngel.setlat(obj.getString("lat"));}catch(Exception k){}
						try{SelectedAngel.setf_name(obj.getString("f_name"));}catch(Exception k){}
						try{SelectedAngel.setl_name(obj.getString("l_name"));}catch(Exception k){}
						try{SelectedAngel.setuser_id(obj.getString("user_id"));}catch(Exception k){}
						try{SelectedAngel.setlng(obj.getString("long"));}catch(Exception k){}
						h.sendEmptyMessage(1);
					}
					
					try {Thread.sleep(2000);} catch (InterruptedException e) {}
					
				}while(GetAngelLocationFlagForTime);
			}
		}.start();
		
		final int minutes=Integer.parseInt(MaxWaitofArrival);
		
		RequestFarword_ETA.setText((((minutes/60)<10)?"0"+minutes/60:minutes/60)+" hr, "+(((minutes%60)<10)?"0"+minutes%60:minutes%60)+" min");
		
		
		
		TimerStopFlagFarword=false;
		//GetAngelLocationFlagForTime=false;
		new Thread()
		{
			int min=minutes-1;
			int sec=60;
			@Override
			public void run()
			{
				int sec=60;
				do
				{
					sec--;
					if(min==0 && sec==0)
					{
						TimerStopFlagFarword=true;
						GetAngelLocationFlagForTime=false;
						h.sendEmptyMessage(4);
					}
					else
					{
						if(sec==0)
						{
							min--;
							sec=60;
						}
						Message m=new Message();
						m.arg1=min;
						m.arg2=sec;
						m.what=2;
						h.sendMessage(m);
						Log.e("Timer Min,Sec 2",min+","+sec);
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(!TimerStopFlagFarword);
			}
		}.start();
		
		RequestFarword_CancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getBaseContext(), "Canceling Request", Toast.LENGTH_SHORT).show();
				final Handler hndl=new Handler()
				{
					@Override
					public void handleMessage(Message m)
					{
						String Response=m.obj.toString();
						if(Response.contains("failed"))
						{
							showDefaultDialog("Failed to Cancel", "Network Problem");
						}
						else
						{
							JSONObject obj=null;
							try {
								obj=new JSONObject(m.obj.toString());
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try
							{
								if(!Boolean.parseBoolean(obj.getString("error")))
								{
									showDefaultDialog("Cancel Completed", obj.getString("message"));
									h.sendEmptyMessage(4);
									currentLocationLocator.startLocaitonUpdates();
								}
								else
								{
									showDefaultDialog("Failed", obj.getString("message"));
								}
								
								Log.e("Response is ",m.obj.toString());
							
							}catch(Exception k){showDefaultDialog("Failed to Cancel", "Json Convertion Problem");}
							
							
							
						}
					}
				};
				new Thread()
				{
					@Override
					public void run()
					{
						String response=acceptORreject_request(currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getuser_id(), SelectedAngel.getuser_id(), "3", "1", "Service is not good");
						Message m=new Message();
						m.obj=response;
						hndl.sendMessage(m);
					}
				}.start();
				
				
				
			}
		});
		
		
	}
	
	
	public String acceptORreject_request(String request_id,String motorist_id,String angel_id,String request_status,String push,String reason)
	{
		String data="failed";
		HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", "acceptORreject_request"));
        arr.add(new BasicNameValuePair("request_id", request_id));
        arr.add(new BasicNameValuePair("motorist_id", motorist_id));
        arr.add(new BasicNameValuePair("angel_id", angel_id));
        arr.add(new BasicNameValuePair("request_status", request_status));
        arr.add(new BasicNameValuePair("push", push));
        arr.add(new BasicNameValuePair("reason", reason));
        
        Log.e("acceptORreject_request parameters",arr.toString());
        
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
	        data=builder.toString();
	        //Log.e("regresponse",""+builder.toString());
	        //JSONObject json= new JSONObject(builder.toString());
	        //String s="";
	        //return s;
	    }
        catch(Exception l)
        {
        	//return "failed";
        }
        return data;
	}
	
	public void startAcceptenceFunciton()
	{
		Toast.makeText(getBaseContext(), "Started for : "+MaxWaitofArrival, Toast.LENGTH_LONG).show(); 
	}
	
	public void validateNotification(String Message)
	{
		if(Message.toLowerCase().contains("cancelled your request"))
		{
			showDefaultDialog("Request Canceled", Message);
		}
	}
	
	public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//mMapFragment.getView().getParent().requestTransparentRegion(mMapFragment.getView());
		
		
		Bundle b= getIntent().getExtras();
		JumpAngelCustomNotification.OutFromMainActivity=false;
		JumpAngelCustomNotification._Act=MainActivity.this;
		
		if(b!=null && !getIntent().hasExtra("hasnotification"))
		{
			Bundle PassedMotorist= getIntent().getExtras();
			currentLogedUser =(CurrentLogedInUser) PassedMotorist.getBundle("cu").getSerializable("currentuser");
			

			SharedPreferences sp=getThisSharedPreferences();
			SharedPreferences.Editor edtr=sp.edit();
			edtr.putString("currentLogedUser", currentLogedUser.getJSONString());
			edtr.commit();
			
			
			int k=0;
		}
		
		if(getThisSharedPreferences().contains("currentLogedUser"))
		{
			try {
				currentLogedUser=new CurrentLogedInUser();
				currentLogedUser.parseJSON(new JSONObject(getThisSharedPreferences().getString("currentLogedUser", "null")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		if(currentLogedUser!=null)//user is loged in
		{
			ButtonRegister.setVisibility(View.GONE);
			SelectAngelDetailButton.setVisibility(View.VISIBLE);
			Settings_btn.setEnabled(true);
			setEnable_ForLoginUser(false);
			if(currentLogedUser.getUserCatagory().toLowerCase().equals("provider"))
			{
				Provider logedprovider=(Provider)currentLogedUser.getUserObject();
				
				WelcomeTV.setText("Welcome "+toFirstuperCase(logedprovider.getFirstName()+" "+logedprovider.getLastName()));
			}
			else
			{
				Motorist logedmotorist=(Motorist)currentLogedUser.getUserObject();
				WelcomeTV.setText("Welcome "+toFirstuperCase(logedmotorist.getFirstName()+" "+logedmotorist.getLastName()));
				
			}
			
		}
		else
		{
			ButtonRegister.setVisibility(View.VISIBLE);
			SelectAngelDetailButton.setVisibility(View.GONE);
			setEnable_ForLoginUser(true);
			Settings_btn.setEnabled(false);
		}
		//Notification Handling here
		
		if(PushApplication.HasNotification)
		{
			//validateNotification(getIntent().getExtras().getString("notificationmessage"));
			
			//Toast.makeText(getBaseContext(), "Has notification,"+getIntent().getExtras().getString("notificationmessage"), Toast.LENGTH_LONG).show();
			Message m=new Message();
			m.what=1;
			m.obj=PushApplication.NotificationString;//getIntent().getExtras().getString("notificationmessage").toString();
			if(getIntent()!=null)
			{
				try
				{
					PushApplication.HasNotification=false;
					PushApplication.NotificationString=null;
					//getIntent().getExtras().remove("notificationmessage");
					//getIntent().getExtras().remove("hasnotification");
					
				}catch (Exception e) {
					Log.e("Exception hided",""+e);
				}
			}
			
			ThisHandler.sendMessage(m);
		}
		
		loadCurrentVehicle();
		
		if(Menulayout.getVisibility()==View.VISIBLE)
		{
			translateMenuLayoutLeft();
			DialogDismiser.setVisibility(View.GONE);
		}
		if(currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
        {
        	SelectAngelDetailButton.setVisibility(View.INVISIBLE);
        }
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(getIntent()!=null)
		{
			try
			{
			//getIntent().getExtras().remove("notificationmessage");
			//getIntent().getExtras().remove("hasnotification");
			}catch (Exception e) {
				Log.e("Exception hided",""+e);
			}
		}
		
		
	}




	public String toFirstuperCase(String v)
	{
		String d="";
		String[] arr=v.split(" ");
		
		for(int i=0; i<arr.length; i++)
		{
			String q="";
			String p=arr[i];
			int ln=p.length();
			for(int j=0; j<ln; j++)
			{
				q+=(j==0)?(p.charAt(j)+"").toUpperCase():p.charAt(j)+"";
			}
			d+=q+" ";
		}
		d.trim();
		return d;
	}
	
	private void loadCurrentVehicle() {
		if(currentLogedUser!=null && currentLogedUser.getVehicleComplete()!=null && currentLogedUser.getVehicleComplete().isnull()==false && !currentLogedUser.getVehicleComplete().getvehicle_Id().equals("null"))
		{
			if(currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
			{
				WelcomeDetailTV.setText("Driving: "+toFirstuperCase(currentLogedUser.getVehicleComplete().getvehicleName()).trim()+".");
			}
			else
			{
				WelcomeDetailTV.setText("Does "+toFirstuperCase(currentLogedUser.getVehicleComplete().getvehicleName())+" needs Assistance?");
			}
		}
		else
		{
			WelcomeDetailTV.setText("Please Chose vehicle first?");
		}
	}


	@Override
	protected void onStart() {
		
		super.onStart();
	}
	
	public ProgressDialog UpdateUserProgressDialog=null;
	public void showDialog(String Message)
	{
		if(UpdateUserProgressDialog==null)
			UpdateUserProgressDialog=new ProgressDialog(MainActivity.this);
		
		UpdateUserProgressDialog.setTitle(Message);
		UpdateUserProgressDialog.setMessage("Please wait");
		UpdateUserProgressDialog.setCancelable(false);
		UpdateUserProgressDialog.show();
	}
	
	
	Boolean TimerStopFlag=false;
	Boolean TimerStopFlagFarword=false;
	
	protected Dialog mSplashDialog;
	LinearLayout GripBar=null;
	RelativeLayout AngleDetailLayout=null;
	Button MenuBackButton=null;
	RelativeLayout Menulayout=null;
	Button MenuButton=null;
	Button FilterMenuBackButton=null;
	RelativeLayout FilterMenulayout=null;
	Button FilterButton=null;
	Spinner ServiceSpineer=null;
	Spinner DutySpinner=null;
	Spinner RattingSpinner=null;
	//Button LoginButton=null;
	Button Settings_btn=null;
	
	
	
	//Button RegisterButton=null;
	LinearLayout DialogDismiser=null;
	Button LogOutButton=null;
	RelativeLayout MapLayout=null;
	
	
	//TextView DistenceCheck=null;
	
	TextView VC_AssistanceType=null;
	TextView VC_Vehicle=null;
	TextView VC_Color=null;
	TextView VC_LicencePlate=null;
	TextView VC_ChargePending=null;
	
	TextView PRO_AssistanceType=null;
	TextView PRO_Vehicle=null;
	TextView PRO_Color=null;
	TextView PRO_LicencePlate=null;
	TextView PRO_MaxWaitTime=null;
	
	TextView PRO_PLUS_AssistanceType=null;
	TextView PRO_PLUS_Vehicle=null;
	TextView PRO_PLUS_Color=null;
	TextView PRO_PLUS_LicencePlate=null;
	
	
	Boolean UpdateAngelLocaitonFlag=false;
	
	
	TextView SC_AssistanceType=null;
	TextView SC_Vehicle=null;
	TextView SC_Color=null;
	TextView SC_LicencePlate=null;
	
	LinearLayout MapEventInterceptor=null;
	
	LinearLayout FilterIconsArea=null;
	
	Button CallMeButtonProvider=null;
	
	//TextView CurrentlyDrivingTV=null;
	TextView CompanyNameTV=null;
	//TextView ServicesTV=null;
	RatingBar RattingTV=null;
	TextView DistanceTV=null;
	TextView ETimeTV=null;
	TextView WelcomeDetailTV=null;
	TextView WelcomeTV=null;
	
	LinearLayout BattryJumpFilter=null;
	LinearLayout FuelDileveryFilter=null;
	LinearLayout BoomTowFilter=null;
	LinearLayout TireChangesFilter=null;
	LinearLayout DiagnosisFilter=null;
	LinearLayout VehicleUnlockFilter=null;
	Button RefereshAngelButton=null;
	
	TextView AngelBidDetailTV=null;
	
	Button SelectAngelDetailButton=null;
	Button CancelButtonRequestView=null;
	Button RequestSubmitDetailButton=null;
	
//	CheckBox ApprovePaymentCheckBox=null;
	Button ApprovePaymentOKButton=null;
	Button ApprovePaymentCancelButton=null;
	Button ApprovePaymentTermsAndConditionButton=null;
	
	Button AngelCancelButton=null;
	
	
	Button VisualConfirmedMotorist=null;
	
	Button BackAngelButton=null;
	Button NextAngelButton=null;
	
	LinearLayout ServicingLayout=null;
	LinearLayout FuelRequestLayout=null;
	
	
	Button CurrentLocaitonButton=null;
	
	
	TextView FilterStatusTV=null;
	
	Button EventHistory=null;
	
	
	Button ButtonRegister=null;
	
	RelativeLayout FilterLayout=null;
	RelativeLayout CurrentSideLayout=null;
	RelativeLayout HomeDetailLayout=null;
	RelativeLayout RequestDetailLayout=null;
	RelativeLayout RequestApproveDetailLayout=null;
	RelativeLayout RequestWaitforResponseLayout=null;
	RelativeLayout RequestWaitforForwordLayout=null;
	RelativeLayout RequestService_Completed=null;
	RelativeLayout RequestService_RatingMotorist=null;
	
	TextView RequestRatingService_DetailTV=null;
	
	
	TextView RequestDetailLayout_ServiceTypeTV=null;
	
	
	RelativeLayout RequestProviderSide_AcceptDeclineLayout=null;
	
	Button RequestProviderSide_ChangeButton=null;
	Button RequestProviderSide_AcceptButton=null;
	TextView RequestProviderSide_AcceptDeclineTimer=null;
	Button RequestProviderSide_CancelButton=null;
	//TextView RequestProviderSide_AcceptDeclineDetailTV=null;
	Button MainBackButton=null;
	
	RelativeLayout RequestProviderSide_FarwordLayout=null;
	//TextView RequestProviderSide_FarwordDetailTV=null;
	TextView RequestProviderSide_FarwordTotalTimeTV=null;
	TextView RequestProviderSide_FarwordTimeRemaningTV=null;
	Button RequestProviderSide_FarwordCancelButton=null;
	Button RequestProviderSide_FarwordCallMeButton=null;
	
	TextView RequestFarword_ETA=null;
	
	
	Button MotoristAwardRating =null;
	
	RatingBar MotoristsetAwardRating=null;
	
	//Spinner RequestDetaillayout_WaitTimeSpinner=null;
	
	Button RequestDetaillayout_WaitTimeButton=null;
	
	EditText RequestDetailLayout_OfferAmmount=null;
	//Spinner RequestDetailLayout_MaxWaitForArrival=null;
	Button RequestDetailLayout_MaxWaitForArrivalButton=null;
	
	
	GoogleMap mMap;
    SupportMapFragment mMapFragment;
    CurrentLocationLocator currentLocationLocator=null;
    
    Button WaitResponseCancelButton=null;
    TextView WaitResponseTimeTV=null;
    
    //TextView RequestFarword_DetailTextView=null;
    TextView RequestFarword_Timer=null;
    Button RequestFarword_CancelButton=null;
    Button RequestFarowrd_CallMEButton=null;
    
   // TextView ServiceCompletedDetail_TV=null;
    Button RequestCompleted_ServiceCompletedButton=null;
    Button RequestCompleted_CallMeButton=null;
    
    
   public static Boolean LoadAngelOnlyOnce=true;
    
    MainActivityHandler MainHandler=null;
    
    HashMap<String, Angel> AngelsHashMap=new HashMap<String, Angel>();
    
    Angel SelectedAngel=null;
    public Vehicle_Info RequestVehicle=null;
    public Motorist RequestMotorist=null;
    
    PushApplication MyPushApplication=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //showSplashScreen();
        setContentView(R.layout.activity_main);
        initiatePushNotificationSetup();
        initiateControls();
        initiateListners();
        initiateMapListners();
        		
        
        initiateBasicChecks();
    }

    private void initiateBasicChecks() {
    	
    	
		if(getThisSharedPreferences().contains("currentLogedUser") )
		{
			
			
				try {
					currentLogedUser=new CurrentLogedInUser();
					currentLogedUser.parseJSON(new JSONObject(getThisSharedPreferences().getString("currentLogedUser", "null")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
			{
				FilterIconsArea.setVisibility(View.GONE);
				RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
				lp.height=(int)(60*getResources().getDisplayMetrics().density); 
				AngleDetailLayout.setLayoutParams(lp);
			}
			
		}
		else
		{
			FilterIconsArea.setVisibility(View.VISIBLE);
		}
	}


	private void initiatePushNotificationSetup()
    {
//    	SharedPreferences sp=getSharedPreferences("asad", 0);
//    	//SharedPreferences.Editor edr=sp.edit();
//    	//edr.putString("hashmi", "This is Hashmi");
//    	//edr.commit();
//    	SharedPreferences sp2=getSharedPreferences("asad", 0);
//    	String ifnal=sp2.getString("hashmi", "");
//    	int oioi=0;
    	
//    	ContactsDataSource s=new ContactsDataSource(getBaseContext());
    	//s.open();
    	//s.createContact("asad", "03428749839493");
    	
    }
    
    public TextView getLabel(String Title,final String value)
	 {
		 TextView L=new TextView(getBaseContext());
			//L.setBackgroundColor(Color.GRAY);
		 	L.setBackgroundResource(R.drawable.myedittext);
			L.setTextColor(Color.BLACK);
			L.setText(Title);
			
			if(!currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
			{
			
			View.OnClickListener ocl=new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuleft);
					//HomeDetailLayout.setVisibility(View.GONE);
					//HomeDetailLayout.startAnimation(moveleft);
					//Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuleft);
					//RequestDetailLayout.startAnimation(MoveFromLeft);
					//setCurrentSideLayout(HomeDetailLayout);
					///RequestDetailLayout.setVisibility(View.VISIBLE);
					
					RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
					
					lp.height=(int)(300*getResources().getDisplayMetrics().density); 
					AngleDetailLayout.setLayoutParams(lp);
				
					RequestDetailLayout.setVisibility(View.VISIBLE);
					SelectAngelDetailButton.setVisibility(View.GONE);
					//RequestDetailLayout.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.scalefrombottom));
					
					((RelativeLayout)NextAngelButton.getParent()).setVisibility(View.GONE);
					
					((LinearLayout)RequestDetailLayout_ServiceTypeTV.getParent()).setVisibility(View.VISIBLE);
					((LinearLayout)FuelRequestLayout.getParent()).setVisibility(View.GONE);
					Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.inbottom);
					RequestDetailLayout.startAnimation(MoveFromLeft);
					
					RequestDetailLayout_ServiceTypeTV.setText(value);
					currentLogedUser.setRequestService(value);
				}
			};
			L.setOnClickListener(ocl);
			}
			L.setPadding(5, 1, 5, 1);
			
			
			LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			lp.leftMargin=10;
			lp.gravity=Gravity.CENTER;
			
			
			lp.width=(int)(30*getResources().getDisplayMetrics().density);//LinearLayout.LayoutParams.WRAP_CONTENT;
			lp.height=(int)(20*getResources().getDisplayMetrics().density);//LinearLayout.LayoutParams.WRAP_CONTENT;
			L.setLayoutParams(lp);
			return L;
	 }
    
    private void initiateMapListners() {
		
    	mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFragment));
    	
        mMap = mMapFragment.getMap();
    	 if(mMap!=null)
    	 {
    		
		         mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
		 			
		        	
		        	 
		 			@Override
		 			public boolean onMarkerClick(Marker marker) {
		 				
		 				try
		 				{
		 				if(!marker.getTitle().toLowerCase().trim().equals("motorist"))
		 				{
		 					Angel CurrentSelectedAngel= AngelsHashMap.get(marker.getId());
		 					
		 						//CurrentlyDrivingTV.setText(CurrentSelectedAngel.getVehicle().getvehicleName());
		 					CompanyNameTV.setText(CurrentSelectedAngel.getf_name()+" "+CurrentSelectedAngel.getl_name());
		 		//			ServicesTV.setText("need to discuss");
		 					//RattingTV.setText("need to discuess");
		 					ServicingLayout.removeAllViews();
		 					if(CurrentSelectedAngel.getVehicle()!=null)
		 					{
		 						if(!CurrentSelectedAngel.getVehicle().getBattery_Jumps().contains("off"))
		 						{
		 							ServicingLayout.addView(getLabel("B", "Battry Jump"));
		 						}
		 						if(!CurrentSelectedAngel.getVehicle().getFuel_Delivery().contains("off"))
		 						{
		 							ServicingLayout.addView(getLabel("F", "Fuel Dilevery"));
		 						}
		 						if(!CurrentSelectedAngel.getVehicle().getTow_Boom().contains("off"))
		 						{
		 							ServicingLayout.addView(getLabel("BT", "Tow Boom"));
		 						}
		 						if(!CurrentSelectedAngel.getVehicle().getTire_Changes().contains("off"))
		 						{
		 							ServicingLayout.addView(getLabel("T", "Tire Changes"));
		 						}
		 						if(!CurrentSelectedAngel.getVehicle().getDiagnostics().contains("off"))
		 						{
		 							ServicingLayout.addView(getLabel("D", "Diagnosis"));
		 						}
		 						if(!CurrentSelectedAngel.getVehicle().getVehicle_Unlocks().contains("off"))
		 						{
		 							ServicingLayout.addView(getLabel("V", "Vehicle Unlocks"));
		 						}
		 						
		 					}
		 					
		 					Double d=null;
		 					try
		 					{
		 						DecimalFormat df=new DecimalFormat("#0.00");
		 						
		 						d=Double.parseDouble(CurrentSelectedAngel.getdistance());
		 						
		 					    d=Double.parseDouble(df.format(d));
		 					}
		 					catch(Exception k){}
		 					
		 					DistanceTV.setText(d+"");
		 					int speed=25;
		 					double Time=Math.round(Double.parseDouble(CurrentSelectedAngel.getdistance())/speed);
		 					ETimeTV.setText(Time+"");
		 					SelectedAngel=CurrentSelectedAngel;
		 				}
		 				}catch(Exception k){
		 					Log.e("",""+k);
		 				}
		 				return false;
		 			}
		 		});
    	 }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
		{
			RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
			if(lp.height==(int)(350*getResources().getDisplayMetrics().density))
			{
				lp.height=(int)(200*getResources().getDisplayMetrics().density); 
				AngleDetailLayout.setLayoutParams(lp);
				return false;
			}
			else
			{
				return false;
			}
				
			
			
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);


	    // Checks whether a hardware keyboard is available
	    if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
	    	String CNumber=SelectedAngel.getContactNumber();
			Log.e("","Calling Selected Provider");
			//Toast.makeText(getBaseContext(), "calling", Toast.LENGTH_SHORT).show();
			if(CNumber==null || CNumber.equals("")){
	              System.out.println("no calling");
	              Toast.makeText(getBaseContext(), "No contact info available", Toast.LENGTH_LONG).show();
	             
	            }else {
	              
	              Intent dial = new Intent();
	              dial.setAction("android.intent.action.DIAL");
	              dial.setData(Uri.parse("tel:"+CNumber.toString()));
	              startActivity(dial); 
	              /*
	              System.out.println("calling");
	              Intent intent = new Intent(Intent.ACTION_CALL);

	              intent.setData(Uri.parse("tel:" + phone.toString()));
	              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	              startActivity(intent);
	              */
	            }
	    }
	}
	

	private void initiateListners() {
		
		MainBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setCurrentSideLayout(FilterLayout);
				FilterLayout.setVisibility(View.VISIBLE);
				MainBackButton.setVisibility(View.GONE);
				RefereshAngelButton.setVisibility(View.VISIBLE);
				MenuButton.setVisibility(View.VISIBLE);
				FilterButton.setVisibility(View.VISIBLE);
				CurrentLocaitonButton.setVisibility(View.VISIBLE);
				RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
				if(lp.height>(int)(200*getResources().getDisplayMetrics().density))
				{
					lp.height=(int)(200*getResources().getDisplayMetrics().density); 
					AngleDetailLayout.setLayoutParams(lp);
					
				}
				
				((RelativeLayout)NextAngelButton.getParent()).setVisibility(View.VISIBLE);
				((LinearLayout)RequestDetailLayout_ServiceTypeTV.getParent()).setVisibility(View.GONE);
				((LinearLayout)FuelRequestLayout.getParent()).setVisibility(View.VISIBLE);
				
				RequestDetailLayout.setVisibility(View.GONE);
				//Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
				//HomeDetailLayout.setVisibility(View.VISIBLE);
				
			}
		});
		
		
		RequestProviderSide_FarwordCallMeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String CNumber=RequestMotorist.getCellNumber();
				Log.e("","Calling to requested Motorist on number "+CNumber);
				//Toast.makeText(getBaseContext(), "calling", Toast.LENGTH_SHORT).show();
				if(CNumber==null || CNumber.equals("")){
		              System.out.println("no calling");
		              Toast.makeText(getBaseContext(), "No contact info available", Toast.LENGTH_LONG).show();
		             
		            }else {
		              
		              Intent dial = new Intent();
		              dial.setAction("android.intent.action.DIAL");
		              dial.setData(Uri.parse("tel:"+CNumber.toString()));
		              startActivity(dial); 
		              
		            }
			}
		});
		CallMeButtonProvider.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String CNumber="";
				if(currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
				{
					CNumber=RequestMotorist.getCellNumber();
				}
				else
				{
					CNumber=SelectedAngel.getContactNumber();
				}
				Log.e("","Calling Selected Provider on number "+CNumber);
				//Toast.makeText(getBaseContext(), "calling", Toast.LENGTH_SHORT).show();
				if(CNumber==null || CNumber.equals("")){
		              System.out.println("no calling");
		              Toast.makeText(getBaseContext(), "No contact info available", Toast.LENGTH_LONG).show();
		             
		            }else {
		              
		              Intent dial = new Intent();
		              dial.setAction("android.intent.action.DIAL");
		              dial.setData(Uri.parse("tel:"+CNumber.toString()));
		              startActivity(dial); 
		              
		            }
			}
		});
		
		
		
		
		RequestCompleted_CallMeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				String CNumber="";
				if(currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
				{
					CNumber=RequestMotorist.getCellNumber();
				}
				else
				{
					CNumber=SelectedAngel.getContactNumber();
				}
				
				Log.e("","Calling Selected Provider on number "+CNumber);
				//Toast.makeText(getBaseContext(), "calling", Toast.LENGTH_SHORT).show();
				if(CNumber==null || CNumber.equals("")){
		              System.out.println("no calling");
		              Toast.makeText(getBaseContext(), "No contact info available", Toast.LENGTH_LONG).show();
		             
		            }else {
		              
		              Intent dial = new Intent();
		              dial.setAction("android.intent.action.DIAL");
		              dial.setData(Uri.parse("tel:"+CNumber.toString()));
		              startActivity(dial); 
		             
		            }
			}
		});
		
		RequestFarowrd_CallMEButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String CNumber=SelectedAngel.getContactNumber();
				Log.e("","Calling Selected Provider on number "+CNumber);
				//Toast.makeText(getBaseContext(), "calling", Toast.LENGTH_SHORT).show();
				if(CNumber==null || CNumber.equals("")){
		              System.out.println("no calling");
		              Toast.makeText(getBaseContext(), "No contact info available", Toast.LENGTH_LONG).show();
		             
		            }else {
		              
		              Intent dial = new Intent();
		              dial.setAction("android.intent.action.DIAL");
		              dial.setData(Uri.parse("tel:"+CNumber.toString()));
		              startActivity(dial); 
		              
		            }
				
			}
		});
		
//		MapEventInterceptor.setOnTouchListener(new View.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				
//				if(event.getAction()==MotionEvent.ACTION_MOVE)
//				{
//					AngleDetailLayout.setVisibility(View.GONE);
//					Log.e("","Moving");
//					return false;
//				}
//				if(event.getAction()==MotionEvent.ACTION_UP)
//				{
//					Log.e("","UP");
//					AngleDetailLayout.setVisibility(View.VISIBLE);
//					return false;
//				}
//				return false;
//			}
//		});
		
		HomeDetailLayout.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if(event.getAction()==MotionEvent.ACTION_MOVE)
				{
					//event.get
					//RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) v.getLayoutParams();
					//lp.leftMargin+=1;
					
					//v.setLayoutParams(lp);
					//v.setTranslationX(v.getTranslationX()+1);
					Log.e("i m draged","draged event occured");
				}
				
				
				return true;
			}
		});
		
		RequestProviderSide_FarwordCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
				ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						UpdateAngelLocaitonFlag=false;
						GetAngelLocationFlagForTime=false;
						
						cancelReqest("3");
					}
				});
				ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				ab.setTitle("Cancelation");
				ab.setMessage("Are you sure you want to cancel.");
				AlertDialog ad=ab.create();
				ad.show();
			}
		});
		
		AngelCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
				
				//lp.height=(int)(200*getResources().getDisplayMetrics().density); 
				//AngleDetailLayout.setLayoutParams(lp);
				RequestDetailLayout.setVisibility(View.GONE);
				SelectAngelDetailButton.setVisibility(View.VISIBLE);
				
				
				//RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
				final int height=lp.height;
					//lp.height=(int)(300*getResources().getDisplayMetrics().density); 
					//AngleDetailLayout.setLayoutParams(lp);
					final Handler h=new Handler()
					{
						@Override
						public void handleMessage(Message m)
						{
							RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
								
								lp.height -=( (int)(m.arg1*getResources().getDisplayMetrics().density)); 
								AngleDetailLayout.setLayoutParams(lp);
						}
					};
				new Thread()
				{
					@Override
					public void run()
					{
						for(int i= height; i>=200*getResources().getDisplayMetrics().density; i--)
						{
							Message m=new Message();
							m.what=1;
							m.arg1=1;
							h.sendMessage(m);
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}.start();
				
				//MainBackButton.setVisibility(View.GONE);
				//RefereshAngelButton.setVisibility(View.VISIBLE);
				//MenuButton.setVisibility(View.VISIBLE);
				//FilterButton.setVisibility(View.VISIBLE);
				//CurrentLocaitonButton.setVisibility(View.VISIBLE);
				
				Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.inbottom);
				
				((RelativeLayout)NextAngelButton.getParent()).startAnimation(MoveFromLeft);
				
				((RelativeLayout)NextAngelButton.getParent()).setVisibility(View.VISIBLE);
				((LinearLayout)RequestDetailLayout_ServiceTypeTV.getParent()).setVisibility(View.GONE);
				((LinearLayout)FuelRequestLayout.getParent()).setVisibility(View.VISIBLE);
				
				//Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
				//HomeDetailLayout.setVisibility(View.VISIBLE);
				
				currentLocationLocator.LoadAngelServerceParameter="";
				
				//Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuright);
				//FilterLayout.startAnimation(moveleft);
				//HomeDetailLayout.startAnimation(MoveFromLeft);
				//setCurrentSideLayout(HomeDetailLayout);
				//FilterLayout.setVisibility(View.VISIBLE);
				//HomeDetailLayout.setVisibility(View.GONE);
				//FilterButton.setBackgroundResource(R.drawable.caricon);
				//FilterButton.setTag("FilterLayout");
				//RefereshAngelButton.setVisibility(View.VISIBLE);
				//FilterButton.setVisibility(View.VISIBLE);
				
			}
		});
		
		
		CurrentLocaitonButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentLocationLocator.zoom();
			}
		});
		
		
		
		
		
		
		
		
//		RequestDetailLayout_OfferAmmount.setOnTouchListener(new View.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if(event.getAction()==MotionEvent.ACTION_UP)
//				{
//					//Log.e("y",""+AngleDetailLayout.getY());
//					RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
//					lp.height=(int)(350*getResources().getDisplayMetrics().density); 
//					AngleDetailLayout.setLayoutParams(lp);
//					//AngleDetailLayout.setY(AngleDetailLayout.getY()-200);
//					//Log.e("after y",""+AngleDetailLayout.getY());
//				}
//				return false;
//			}
//		});
		
//		RequestDetailLayout_ServiceTypeTV.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
		
		
		 RequestCompleted_ServiceCompletedButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setCurrentSideLayout(RequestService_RatingMotorist);
				RequestService_RatingMotorist.setVisibility(View.VISIBLE);
				MotoristsetAwardRating.setRating(0);
			}
		});
		 
		VisualConfirmedMotorist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setCurrentSideLayout(RequestService_Completed);
				UpdateAngelLocaitonFlag=false;
				GetAngelLocationFlagForTime=false;
				ScaleAnimation sa=new ScaleAnimation(0.0f, 0.0f, 1f, 0.0f);
				CurrentSideLayout.startAnimation(sa);
				
//				String Text="Assistance Type: "+currentLogedUser.getrequestDetails().getservice()+"\r\n";
//				 Text +="Vehivle        : "+currentLogedUser.getVehicleComplete().getYear()+","+currentLogedUser.getVehicleComplete().getvehicleName()+","+currentLogedUser.getVehicleComplete().getModel()+"\r\n";
//				 Text +="Color          : "+currentLogedUser.getVehicleComplete().getColor()+"\r\n";
//				 Text +="License Plate  : "+currentLogedUser.getVehicleComplete().getLicensePlate()+"\r\n";
//				 Text +="Charge Pending : $"+currentLogedUser.getRequestAmount();
//				 ServiceCompletedDetail_TV.setText(Text);
				 String[] arr=getResources().getStringArray(R.array.Services_Array);
					try
					{
						VC_AssistanceType.setText(arr[Integer.parseInt(currentLogedUser.getrequestDetails().getservice())]);
					}catch (Exception e) {
						// TODO: handle exception
					}
					
					VC_Vehicle.setText(currentLogedUser.getVehicleComplete().getYear()+","+currentLogedUser.getVehicleComplete().getvehicleName()+","+currentLogedUser.getVehicleComplete().getModel());
					VC_Color.setText(currentLogedUser.getVehicleComplete().getColor());
					VC_LicencePlate.setText(currentLogedUser.getVehicleComplete().getLicensePlate());
					VC_ChargePending.setText(" $"+currentLogedUser.getRequestAmount());
					TimerStopFlagFarword=true;
					GetAngelLocationFlagForTime=true;
				 
				 RequestRatingService_DetailTV.setText("Angel : "+SelectedAngel.getf_name()+" "+SelectedAngel.getl_name());
				RequestService_Completed.setVisibility(View.VISIBLE);
			}
		});
		
		MotoristAwardRating.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Rate here
				
				final float rating=MotoristsetAwardRating.getRating();
				
				final Handler h=new Handler()
				{
					@Override
					public void handleMessage(Message m)
					{
						if(m.what==1)
						{
							UpdateUserProgressDialog.cancel();
							
							SharedPreferences sp=getThisSharedPreferences();
							SharedPreferences.Editor edtr=sp.edit();
							edtr.putString("currentLogedUser", currentLogedUser.getJSONString());
							edtr.commit();
							
							Toast.makeText(getBaseContext(), m.obj.toString(), Toast.LENGTH_SHORT).show();
							Intent i=new Intent(MainActivity.this, Payment_Activity.class);
							i.putExtra("angel", SelectedAngel);
							startActivityForResult(i, 0);
							SelectedAngel.rating=rating+"";
							GetAngelLocationFlagForTime=false;
							
							
							MainBackButton.setVisibility(View.GONE);
							RefereshAngelButton.setVisibility(View.VISIBLE);
							MenuButton.setVisibility(View.VISIBLE);
							FilterButton.setVisibility(View.VISIBLE);
							CurrentLocaitonButton.setVisibility(View.VISIBLE);
							
						}
						else if(m.what==-1)
						{
							try{
							Toast.makeText(getBaseContext(), m.obj.toString(), Toast.LENGTH_SHORT).show();
							}catch(Exception k){}
						}
						else if (m.what==2)
						{
							Toast.makeText(getBaseContext(), "Rated successfully", Toast.LENGTH_SHORT).show();
							setCurrentSideLayout(FilterLayout);
							FilterLayout.setVisibility(View.VISIBLE);
							currentLocationLocator.startLocaitonUpdates();
							GetAngelLocationFlagForTime=false;

							RelativeLayout.LayoutParams lpp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
							
							lpp.height=(int)(80*getResources().getDisplayMetrics().density); 
							AngleDetailLayout.setLayoutParams(lpp);
							
							
							
							MainBackButton.setVisibility(View.GONE);
							RefereshAngelButton.setVisibility(View.VISIBLE);
							MenuButton.setVisibility(View.VISIBLE);
							FilterButton.setVisibility(View.VISIBLE);
							CurrentLocaitonButton.setVisibility(View.VISIBLE);
						}
						UpdateUserProgressDialog.cancel();
					}
				};
				showDialog("Rating.");
				new Thread()
				{
					@Override
					public void run()
					{
						JSONObject Response=null;
						if(!currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
						{
							Response=rating("rating", currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getuser_id(), SelectedAngel.getuser_id(), rating+"");
						}
						else
						{
							Response=rating("rating", currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getuser_id(), currentLogedUser.getrequestDetails().getmotorist_id(), rating+"");
						}
						
						if(Response!=null)
						{
							try
							{
								Log.e("Rating response",""+Response);
								Message m=new Message();
								
								if(!currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
								{
									m.what=1;
								}
								else
								{
									m.what=2;
								}
								
								m.obj=Response.getString("message");
								
								h.sendMessage(m);
								
							}catch(Exception g){
								h.sendEmptyMessage(-1);
								int k=0;
							}
						}
						else
						{
							h.sendEmptyMessage(-1);
						}
						
					}
				}.start();
				
				
				
			}
		});
		
		RequestDetaillayout_WaitTimeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				//AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
				
				final Dialog a=new Dialog(MainActivity.this);
				
				
				View vb=LayoutInflater.from(MainActivity.this).inflate(R.layout.mytimepiker, null);
				
				final TimePicker tp=(TimePicker)vb.findViewById(R.id.RequestTimePicker);
				tp.setIs24HourView(true);
				
				if(RequestDetaillayout_WaitTimeButton.getText().toString().equals("0 Min"))
				{
					tp.setCurrentHour(0);
					tp.setCurrentMinute(0);
				}
				else
				{
					try
					{
						String[] arr=RequestDetaillayout_WaitTimeButton.getText().toString().split(", ");
						
						String scv=arr[0].subSequence(0, arr[0].indexOf(" hr")).toString();
						String mntv=arr[1].subSequence(0, arr[1].indexOf(" min")).toString();
						int hour=Integer.parseInt(scv);
						int min=Integer.parseInt(mntv);
						tp.setCurrentHour(hour);
						tp.setCurrentMinute(min);
						
					}catch (Exception e) {
						
					}
				}
				
				
				Button setbutton=(Button)vb.findViewById(R.id.SetTime);
				
				setbutton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						int ch=(int)tp.getCurrentHour();
						int minut=tp.getCurrentMinute();
						
						
						WaitTime=((ch*60)+minut)+"";
						if(ch==0 && minut==0)
						{
							RequestDetaillayout_WaitTimeButton.setText("");
						}
						else
						{
							RequestDetaillayout_WaitTimeButton.setText(((ch==0)?"":(((ch<10)?("0"+ch):ch)+" hr, "))+((minut<10)?("0"+minut):minut)+" min");
						}
						//RequestDetaillayout_WaitTimeButton.setText(((ch<10)?("0"+ch):ch)+":"+minut);
						Log.e("WaitTime is ",""+WaitTime);
						a.cancel();
					}
				});
				
				Button CancelButton=(Button)vb.findViewById(R.id.CancelTime);
				CancelButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						a.cancel();
					}
				});
				
				//AlertDialog ad=ab.create();
				a.setContentView(vb);
				//ad.setView(vb);
				a.setTitle("Enter Wait Time");
				a.show();
				Log.e("Dialog","shown");
			
			}
		});
		
		RequestDetailLayout_MaxWaitForArrivalButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				//AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
				
				final Dialog a=new Dialog(MainActivity.this);
				
				
				View vb=LayoutInflater.from(MainActivity.this).inflate(R.layout.mytimepiker, null);
				
				final TimePicker tp=(TimePicker)vb.findViewById(R.id.RequestTimePicker);
				tp.setIs24HourView(true);
				
				if(RequestDetailLayout_MaxWaitForArrivalButton.getText().toString().equals("0 Min"))
				{
					tp.setCurrentHour(0);
					tp.setCurrentMinute(0);
				}
				else
				{
					try
					{
						String[] arr=RequestDetailLayout_MaxWaitForArrivalButton.getText().toString().split(", ");
						
						String scv=arr[0].subSequence(0, arr[0].indexOf(" hr")).toString();
						String mntv=arr[1].subSequence(0, arr[1].indexOf(" min")).toString();
						int hour=Integer.parseInt(scv);
						int min=Integer.parseInt(mntv);
						tp.setCurrentHour(hour);
						tp.setCurrentMinute(min);
						
					}catch (Exception e) {
						
					}
				}
				
				Button setbutton=(Button)vb.findViewById(R.id.SetTime);
				
				setbutton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						int ch=(int)tp.getCurrentHour();
						int minut=tp.getCurrentMinute();
						
						
						MaxWaitofArrival=((ch*60)+minut)+"";
						
						if(ch==0 && minut==0)
						{
							RequestDetailLayout_MaxWaitForArrivalButton.setText("");
						}
						else
						{
							RequestDetailLayout_MaxWaitForArrivalButton.setText(((ch==0)?"":(((ch<10)?("0"+ch):ch)+" hr, "))+((minut<10)?("0"+minut):minut)+" min");
						}
						Log.e("Max WaitTime is ",""+MaxWaitofArrival);
						a.cancel();
					}
				});
				
				Button CancelButton=(Button)vb.findViewById(R.id.CancelTime);
				CancelButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						a.cancel();
					}
				});
				
				//AlertDialog ad=ab.create();
				a.setContentView(vb);
				//ad.setView(vb);
				a.setTitle("Enter Max Wait Time");
				a.show();
				Log.e("Dialog","shown");
			
			}
		});
		
		
		NextAngelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try
				{
				
				if(Angel.selectedangelindex+1<=currentLocationLocator.AngelMarkerPointers.size()-1)
				{
				Angel.selectedangelindex++;
				Angel CurrentSelectedAngel= AngelsHashMap.get(currentLocationLocator.AngelMarkerPointers.get(Angel.selectedangelindex));
					
					SelectedAngel=CurrentSelectedAngel;
					
					
					mMap.clear();
					
					ArrayList<String> AngelMarkerPointers=new ArrayList<String>();
					
					HashMap<String , Angel> HashMapRelpacer=new HashMap<String, Angel>();
					
					for(int j=0; j<currentLocationLocator.AngelMarkerPointers.size(); j++)
					{
						AngelMarkerPointers.add(currentLocationLocator.AngelMarkerPointers.get(j));
						HashMapRelpacer.put(AngelMarkerPointers.get(j), AngelsHashMap.get(currentLocationLocator.AngelMarkerPointers.get(j)));
					}
					currentLocationLocator.AngelMarkerPointers.clear();
					
					AngelsHashMap.clear();
					for(int i=0; i<AngelMarkerPointers.size(); i++)
					{
						
						String key=AngelMarkerPointers.get(i);
						
						Angel tempAngel=(Angel)HashMapRelpacer.get(key);
						MarkerOptions AngelMarker=new MarkerOptions();
						
						AngelMarker.position(new LatLng(Double.parseDouble(tempAngel.getlat().trim()), Double.parseDouble(tempAngel.getlng().trim())));
						AngelMarker.title(tempAngel.getf_name()+" "+tempAngel.getl_name());
						
						if(SelectedAngel.getuser_id().equals(tempAngel.getuser_id()))
						{
							AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconangelwithcircle));
						}
						else
						{
							AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.jumpangel));
						}
						
						if(tempAngel.getVehicle()!=null)
							AngelMarker.snippet(tempAngel.getVehicle().getvehicleName());
						String markerpointer=mMap.addMarker(AngelMarker).getId();
						currentLocationLocator.AngelMarkerPointers.add(markerpointer);
						AngelsHashMap.put(markerpointer, tempAngel);
					}
					
					for(int j=0; j<currentLocationLocator.AngelMarkerPointers.size(); j++)
					{
						AngelMarkerPointers.add(currentLocationLocator.AngelMarkerPointers.get(j));
					}
					
					
					MarkerOptions MyLocation=new MarkerOptions();
			   	 	MyLocation.position(new LatLng(Double.parseDouble(currentLocationLocator.ThisLat), Double.parseDouble(currentLocationLocator.ThisLong)));
			   	 	MyLocation.title("Motorist");
			   	 	MyLocation.snippet("My Location");
			   	 	MyLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
			   	 	
					mMap.addMarker(MyLocation);
					
					
					
					
					CompanyNameTV.setText(CurrentSelectedAngel.getf_name()+" "+CurrentSelectedAngel.getl_name());
					
					Double d=null;
 					try
 					{
 						DecimalFormat df=new DecimalFormat("#0.00");
 						
 						d=Double.parseDouble(CurrentSelectedAngel.getdistance());
 						
 					    d=Double.parseDouble(df.format(d));
 					}
 					catch(Exception k){}
 					
 					DistanceTV.setText(CurrentSelectedAngel.getdistance());
					
					RattingTV.setRating(Float.parseFloat(CurrentSelectedAngel.rating));
					
					int speed=25;
					double Time=0;
					try{
						Time=Math.round(Double.parseDouble(CurrentSelectedAngel.getdistance().split(" ")[0])/speed);
					}catch(Exception k){}
					ETimeTV.setText(CurrentSelectedAngel.getduration());
					SelectedAngel=CurrentSelectedAngel;
					ServicingLayout.removeAllViews();
					FuelRequestLayout.removeAllViews();
					
					CameraUpdate CameraToMotoristLocaiton=CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(CurrentSelectedAngel.getlat()),Double.parseDouble(CurrentSelectedAngel.getlng())), 20);
			   	 	
					Log.e("Animating to point",CurrentSelectedAngel.getlat() +""+CurrentSelectedAngel.getlng());
					
			   	 	mMap.animateCamera(CameraToMotoristLocaiton);
					
 					if(CurrentSelectedAngel.getVehicle()!=null)
 					{
 						if(SelectedFilter==1 && !CurrentSelectedAngel.getVehicle().getBattery_Jumps().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("light"))
 							{
 								ServicingLayout.addView(getLabel("L", "Light Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("medium"))
 							{
 								ServicingLayout.addView(getLabel("M", "Medium Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("heavy"))
 							{
 								ServicingLayout.addView(getLabel("H", "Heavy Duty"));
 							}
 							
 						}
 						else if(SelectedFilter==2 && !CurrentSelectedAngel.getVehicle().getFuel_Delivery().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
 							{
 								FuelRequestLayout.addView(getLabel("D", "Diesel"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
 							{
 								FuelRequestLayout.addView(getLabel("C", "CNG"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("petrol"))
 							{
 								FuelRequestLayout.addView(getLabel("G", "Gasoline/Petrol"));
 							}
 						}
 						else if(SelectedFilter==3)
 						{
 							
 								ServicingLayout.addView(getLabel("L", "Light Duty"));
 							
 								ServicingLayout.addView(getLabel("M", "Medium Duty"));
 							
 								ServicingLayout.addView(getLabel("H", "Heavy Duty"));
 							
 							
 							if(!CurrentSelectedAngel.getVehicle().getTow_Boom().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("B", "Tow Boom"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_FlatBed().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("F", "Flat Bed"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_Sling().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("S", "Tow Sling"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_WheelLift().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("WL", "Tow Wheel Lift"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_Winch().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("W", "Tow Winch"));
 							}
 						}
 						else if(SelectedFilter==4 && !CurrentSelectedAngel.getVehicle().getTire_Changes().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("light"))
 							{
 								ServicingLayout.addView(getLabel("L", "Light Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("medium"))
 							{
 								ServicingLayout.addView(getLabel("M", "Medium Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("heavy"))
 							{
 								ServicingLayout.addView(getLabel("H", "Heavy Duty"));
 							}
 							
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
 							{
 								FuelRequestLayout.addView(getLabel("CH", "Tire Change"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
 							{
 								FuelRequestLayout.addView(getLabel("R", "Mobile Tire Repair"));
 							}
 							
 						}
 						else if(SelectedFilter==5 && !CurrentSelectedAngel.getVehicle().getDiagnostics().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("diesel"))
 							{
 								ServicingLayout.addView(getLabel("D", "Diesel"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("cng"))
 							{
 								ServicingLayout.addView(getLabel("C", "CNG"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("petrol"))
 							{
 								ServicingLayout.addView(getLabel("G", "Gasoline/Petrol"));
 							}
 						}
 						else if(SelectedFilter==6 && !CurrentSelectedAngel.getVehicle().getVehicle_Unlocks().contains("off"))
 						{
 						//	ServicingLayout.addView(getLabel("V", "Vehicle Unlocks"));
 						}
 				
// 						if(!CurrentSelectedAngel.getVehicle().getFuel_Delivery().contains("off"))
// 						{
// 							
// 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
// 							{
// 								FuelRequestLayout.addView(getLabel("D", "Diesel"));
// 							}
// 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
// 							{
// 								FuelRequestLayout.addView(getLabel("C", "CNG"));
// 							}
// 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("petrol"))
// 							{
// 								FuelRequestLayout.addView(getLabel("P", "Gasoline/Petrol"));
// 							}
// 							Log.e("Fuel Dilevery",CurrentSelectedAngel.getVehicle().getFuel_Delivery());
// 							
// 							
// 						}
 						
 						
 					}
				}
				else
				{
					//Toast.makeText(getBaseContext(), "Limit crossed", Toast.LENGTH_SHORT).show();
				}
				}
				catch(Exception d){
					Log.e("my exp",""+d);
					
				}
			}
		});
		BackAngelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try
				{
				
				if(Angel.selectedangelindex-1>=0)
				{
				Angel.selectedangelindex--;
				Angel CurrentSelectedAngel= AngelsHashMap.get(currentLocationLocator.AngelMarkerPointers.get(Angel.selectedangelindex));
					
				SelectedAngel=CurrentSelectedAngel;
				
				
				

				mMap.clear();
				
				ArrayList<String> AngelMarkerPointers=new ArrayList<String>();
				
				HashMap<String , Angel> HashMapRelpacer=new HashMap<String, Angel>();
				
				
				for(int j=0; j<currentLocationLocator.AngelMarkerPointers.size(); j++)
				{
					AngelMarkerPointers.add(currentLocationLocator.AngelMarkerPointers.get(j));
					HashMapRelpacer.put(AngelMarkerPointers.get(j), AngelsHashMap.get(currentLocationLocator.AngelMarkerPointers.get(j)));
				}
				currentLocationLocator.AngelMarkerPointers.clear();
				
				
				
				AngelsHashMap.clear();
				for(int i=0; i<AngelMarkerPointers.size(); i++)
				{
					
					String key=AngelMarkerPointers.get(i);
					
					Angel tempAngel=(Angel)HashMapRelpacer.get(key);
					MarkerOptions AngelMarker=new MarkerOptions();
					
					AngelMarker.position(new LatLng(Double.parseDouble(tempAngel.getlat().trim()), Double.parseDouble(tempAngel.getlng().trim())));
					AngelMarker.title(tempAngel.getf_name()+" "+tempAngel.getl_name());
					
					if(SelectedAngel.getuser_id().equals(tempAngel.getuser_id()))
					{
						AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconangelwithcircle));
					}
					else
					{
						AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.jumpangel));
					}
					
					if(tempAngel.getVehicle()!=null)
						AngelMarker.snippet(tempAngel.getVehicle().getvehicleName());
					String markerpointer=mMap.addMarker(AngelMarker).getId();
					currentLocationLocator.AngelMarkerPointers.add(markerpointer);
					AngelsHashMap.put(markerpointer, tempAngel);
				}
				
				for(int j=0; j<currentLocationLocator.AngelMarkerPointers.size(); j++)
				{
					AngelMarkerPointers.add(currentLocationLocator.AngelMarkerPointers.get(j));
				}
				
				
				MarkerOptions MyLocation=new MarkerOptions();
		   	 	MyLocation.position(new LatLng(Double.parseDouble(currentLocationLocator.ThisLat), Double.parseDouble(currentLocationLocator.ThisLong)));
		   	 	MyLocation.title("Motorist");
		   	 	MyLocation.snippet("My Location");
		   	 	MyLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
		   	 	
				mMap.addMarker(MyLocation);
				
				
				
				
				
						CompanyNameTV.setText(CurrentSelectedAngel.getf_name()+" "+CurrentSelectedAngel.getl_name());
						
						Double d=null;
	 					try
	 					{
	 						DecimalFormat df=new DecimalFormat("#0.00");
	 						
	 						d=Double.parseDouble(CurrentSelectedAngel.getdistance());
	 						
	 					    d=Double.parseDouble(df.format(d));
	 					}
	 					catch(Exception k){}
	 					
	 					DistanceTV.setText(CurrentSelectedAngel.getdistance());
						
						//DistanceTV.setText(CurrentSelectedAngel.getdistance());
						RattingTV.setRating(Float.parseFloat(CurrentSelectedAngel.rating));
					int speed=25;
					double Time=0;
					try{
						Time=Math.round(Double.parseDouble(CurrentSelectedAngel.getdistance().split(" ")[0])/speed);
					}catch(Exception k){}
					ETimeTV.setText(CurrentSelectedAngel.getduration());
					SelectedAngel=CurrentSelectedAngel;
					ServicingLayout.removeAllViews();
					FuelRequestLayout.removeAllViews();
					
					CameraUpdate CameraToMotoristLocaiton=CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(CurrentSelectedAngel.getlat()),Double.parseDouble(CurrentSelectedAngel.getlng())), 20);
			   	 	
					Log.e("Animating to point",CurrentSelectedAngel.getlat() +""+CurrentSelectedAngel.getlng());
					
			   	 	mMap.animateCamera(CameraToMotoristLocaiton);
					
 					if(CurrentSelectedAngel.getVehicle()!=null)
 					{
 						if(SelectedFilter==1 && !CurrentSelectedAngel.getVehicle().getBattery_Jumps().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("light"))
 							{
 								ServicingLayout.addView(getLabel("L", "Light Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("medium"))
 							{
 								ServicingLayout.addView(getLabel("M", "Medium Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("heavy"))
 							{
 								ServicingLayout.addView(getLabel("H", "Heavy Duty"));
 							}
 							
 						}
 						else if(SelectedFilter==2 && !CurrentSelectedAngel.getVehicle().getFuel_Delivery().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
 							{
 								FuelRequestLayout.addView(getLabel("D", "Diesel"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
 							{
 								FuelRequestLayout.addView(getLabel("C", "CNG"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("petrol"))
 							{
 								FuelRequestLayout.addView(getLabel("G", "Gasoline/Petrol"));
 							}
 						}
 						else if(SelectedFilter==3)
 						{
 							
 								ServicingLayout.addView(getLabel("L", "Light Duty"));
 							
 								ServicingLayout.addView(getLabel("M", "Medium Duty"));
 							
 								ServicingLayout.addView(getLabel("H", "Heavy Duty"));
 							
 							
 							if(!CurrentSelectedAngel.getVehicle().getTow_Boom().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("B", "Tow Boom"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_FlatBed().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("F", "Flat Bed"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_Sling().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("S", "Tow Sling"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_WheelLift().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("WL", "Tow Wheel Lift"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_Winch().contains("off"))
 							{
 								FuelRequestLayout.addView(getLabel("W", "Tow Winch"));
 							}
 						}
 						else if(SelectedFilter==4 && !CurrentSelectedAngel.getVehicle().getTire_Changes().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("light"))
 							{
 								ServicingLayout.addView(getLabel("L", "Light Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("medium"))
 							{
 								ServicingLayout.addView(getLabel("M", "Medium Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("heavy"))
 							{
 								ServicingLayout.addView(getLabel("H", "Heavy Duty"));
 							}
 							
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
 							{
 								FuelRequestLayout.addView(getLabel("CH", "Tire Change"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
 							{
 								FuelRequestLayout.addView(getLabel("R", "Mobile Tire Repair"));
 							}
 							
 						}
 						else if(SelectedFilter==5 && !CurrentSelectedAngel.getVehicle().getDiagnostics().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("diesel"))
 							{
 								ServicingLayout.addView(getLabel("D", "Diesel"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("cng"))
 							{
 								ServicingLayout.addView(getLabel("C", "CNG"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("petrol"))
 							{
 								ServicingLayout.addView(getLabel("G", "Gasoline/Petrol"));
 							}
 						}
 						else if(SelectedFilter==6 && !CurrentSelectedAngel.getVehicle().getVehicle_Unlocks().contains("off"))
 						{
 						//	ServicingLayout.addView(getLabel("V", "Vehicle Unlocks"));
 						}
// 						if(!CurrentSelectedAngel.getVehicle().getFuel_Delivery().contains("off"))
// 						{
// 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
// 							{
// 								FuelRequestLayout.addView(getLabel("D", "Diesel"));
// 							}
// 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
// 							{
// 								FuelRequestLayout.addView(getLabel("C", "CNG"));
// 							}
// 							
// 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("petrol"))
// 							{
// 								FuelRequestLayout.addView(getLabel("P", "Gasoline/Petrol"));
// 							}
// 							Log.e("Fuel Dilevery",CurrentSelectedAngel.getVehicle().getFuel_Delivery());
// 							
// 						}
 						
 						
 					}
				}
				else
				{
					//Toast.makeText(getBaseContext(), "Limit crossed", 10).show();
				}
				}catch(Exception d){}
				
			}
		});
		
		
		BattryJumpFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentLocationLocator.loadAngels("1");
				SelectedFilter=1;
				
				MainBackButton.setVisibility(View.VISIBLE);
				RefereshAngelButton.setVisibility(View.GONE);
				MenuButton.setVisibility(View.GONE);
				FilterButton.setVisibility(View.GONE);
				CurrentLocaitonButton.setVisibility(View.GONE);
			}
		});
		FuelDileveryFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentLocationLocator.loadAngels("2");
				SelectedFilter=2;
				
				MainBackButton.setVisibility(View.VISIBLE);
				RefereshAngelButton.setVisibility(View.GONE);
				MenuButton.setVisibility(View.GONE);
				FilterButton.setVisibility(View.GONE);
				CurrentLocaitonButton.setVisibility(View.GONE);
			}
		});
		BoomTowFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentLocationLocator.loadAngels("3");
				SelectedFilter=3;
				MainBackButton.setVisibility(View.VISIBLE);
				RefereshAngelButton.setVisibility(View.GONE);
				MenuButton.setVisibility(View.GONE);
				FilterButton.setVisibility(View.GONE);
				CurrentLocaitonButton.setVisibility(View.GONE);
			}
		});
		TireChangesFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentLocationLocator.loadAngels("4");
				SelectedFilter=4;
				MainBackButton.setVisibility(View.VISIBLE);
				RefereshAngelButton.setVisibility(View.GONE);
				MenuButton.setVisibility(View.GONE);
				FilterButton.setVisibility(View.GONE);
				CurrentLocaitonButton.setVisibility(View.GONE);
			}
		});
		DiagnosisFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentLocationLocator.loadAngels("5");
				SelectedFilter=5;
				MainBackButton.setVisibility(View.VISIBLE);
				RefereshAngelButton.setVisibility(View.GONE);
				MenuButton.setVisibility(View.GONE);
				FilterButton.setVisibility(View.GONE);
				CurrentLocaitonButton.setVisibility(View.GONE);
			}
		});
		VehicleUnlockFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentLocationLocator.loadAngels("6");
				SelectedFilter=6;
				MainBackButton.setVisibility(View.VISIBLE);
				RefereshAngelButton.setVisibility(View.GONE);
				MenuButton.setVisibility(View.GONE);
				FilterButton.setVisibility(View.GONE);
				CurrentLocaitonButton.setVisibility(View.GONE);
			}
		});
		RefereshAngelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JustRefereshing=true;
				currentLocationLocator.loadAngels(currentLocationLocator.LoadAngelServerceParameter);
				SelectedFilter=1;
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			   // if(currentapiVersion>=11)
			    //{
			    	RefereshAngelButton.setBackgroundResource(R.drawable.opacityreferesh);
//			    }
//			    else
//			    {
//			    	//RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)RefereshAngelButton.getLayoutParams();
//			    	//lp.
//			    }
			}
		});
		
		EventHistory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this, EventHistory_Activity.class);
				startActivity(i);
			}
		});
		
		Settings_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.this, Account_Settings.class);
				startActivityForResult(i, 0);
			}
		});
		
		GripBar.setOnTouchListener(new View.OnTouchListener() {
			//int LastLocationY=0;
			float lastfingerY=0;
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(arg1.getAction()==MotionEvent.ACTION_DOWN)
				{
					
					RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) AngleDetailLayout.getLayoutParams();
					float d=getResources().getDisplayMetrics().density;
					
					if(lp.height!=((int)(16*d)))
					{
					lp.height=(int)(16*d);
					}
					else
					{
						lp.height=(int)(200*d);
					}
					Log.e("values is "+lp.height,""+((int)(16+d)));
					
					AngleDetailLayout.setLayoutParams(lp);
					
					//lastfingerY=(int)arg1.getRawY();
				}
				
				if(arg1.getAction()==MotionEvent.ACTION_MOVE)
				{
//					synchronized (this) {
//						
//					
//					float NewLocation=lastfingerY-(arg1.getY());
//					RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) AngleDetailLayout.getLayoutParams();
//					
//					if(lastfingerY<arg1.getRawY() && lp.height>100)
//					{
//						lp.height=lp.height-(int)15;
//					}
//					else if(lastfingerY>arg1.getRawY())
//					{
//						lp.height=lp.height+(int)15;
//					}
//					
//					AngleDetailLayout.setLayoutParams(lp);
//					lastfingerY=(float)arg1.getRawY();
//					}
//							
				}
				return true;
			}
		});
		
		MenuBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				translateMenuLayoutLeft();
				DialogDismiser.setVisibility(View.GONE);
				
			}
		});
		MenuButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Animation TranslateMenuView=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);//new TranslateAnimation(1f, -200f, 0f, 0f);
				TranslateMenuView.setDuration(300);
				
				Menulayout.setVisibility(View.VISIBLE);
				Menulayout.startAnimation(TranslateMenuView);
				DialogDismiser.setVisibility(View.VISIBLE);
				
			}
		});
		FilterMenuBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				translateFilterLayoutRigth();
				DialogDismiser.setVisibility(View.GONE);
			}
		});
		FilterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				if(v.getTag()!=null && FilterButton.getTag().toString().toLowerCase().contains("homelayout"))
				{
					Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
					HomeDetailLayout.setVisibility(View.VISIBLE);
					
					Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuright);
					FilterLayout.startAnimation(moveleft);
					HomeDetailLayout.startAnimation(MoveFromLeft);
					setCurrentSideLayout(HomeDetailLayout);
					FilterLayout.setVisibility(View.VISIBLE);
					HomeDetailLayout.setVisibility(View.GONE);
					FilterButton.setBackgroundResource(R.drawable.caricon);
					FilterButton.setTag("FilterLayout");
				}
				else
				{
//					if(currentLogedUser.getUserCatagory().toLowerCase().contains("provider"))
//					{
//						Intent i=new Intent(MainActivity.this, Provider_Add_Vehicle_Activity.class);
//						startActivity(i);
//					}
//					else
//					{
//						Intent i=new Intent(MainActivity.this, Motorist_Add_Vehicle_Activity.class);
//						startActivity(i);
//					}
					
					
						Intent i=new Intent(MainActivity.this, VehicleSettings_Activity.class);
						startActivity(i);
					
					
				}
				
//				Animation TranslateMenuView=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuleft);//new TranslateAnimation(1f, -200f, 0f, 0f);
//				TranslateMenuView.setDuration(300);
//				FilterMenulayout.setVisibility(View.VISIBLE);
//				FilterMenulayout.startAnimation(TranslateMenuView);
//				DialogDismiser.setVisibility(View.VISIBLE);
				
			}
		});
//		LoginButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Intent LoginIntent= new Intent(MainActivity.this, Login_Activity.class);
//				startActivityForResult(LoginIntent, 0);
//				
//				
//			}
//		});
//		
//		RegisterButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				Intent RegisterIntent=new Intent(MainActivity.this,Registration_Choice_Activity.class);
//				startActivityForResult(RegisterIntent,0);
//				
//			}
//		});
		
		DialogDismiser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(Menulayout.getVisibility()==View.VISIBLE)
				{
					translateMenuLayoutLeft();
				}
				else
				{
					translateFilterLayoutRigth();
				}
				DialogDismiser.setVisibility(View.GONE);
			}
		});
		LogOutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				AlertDialog.Builder ABuilder=new AlertDialog.Builder(MainActivity.this);
				ABuilder.setTitle("Log Out");
				ABuilder.setMessage("Are you sure you want to log out.");
				ABuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					
						showDialog("Sign out");
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
									Toast.makeText(getBaseContext(), "Connection Problem", Toast.LENGTH_SHORT).show();
									finish();
									break;
								}
								case 1:
								{
									SharedPreferences sp=getThisSharedPreferences();
									SharedPreferences.Editor edtr=sp.edit();
									edtr.clear();
									edtr.clear().remove("currentLogedUser");
									try{getIntent().getExtras().clear();}catch(Exception d){}
									currentLogedUser=null;
									edtr.commit();
									//MainActivity.this.onResume();
									Intent i=new Intent(MainActivity.this, FirstLayout_Activity.class);
									i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(i);
									break;
								}
									
								}
								
								UpdateUserProgressDialog.cancel();
							}
						};
						
						new Thread()
						{
							@Override
							public void run()
							{
								JSONObject obj= signout(currentLogedUser.getuser_id());
								if(obj==null)
								{
									h.sendEmptyMessage(-1);	
								}
								else
								{
									h.sendEmptyMessage(1);
								}
							}
						}.start();
						
						
						
					}
				});
				ABuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog ad=ABuilder.create();
				ad.show();
			}
		});
		
		
		
		SelectAngelDetailButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(SelectedAngel!=null)
				{
					SelectAngelDetailButton.setEnabled(false);
					//RequestDetailLayout_ServiceTypeTV.setText(value);
					Log.e("Selected angel is ",SelectedAngel.getf_name()+" "+SelectedAngel.getl_name());
					//Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuleft);
					//HomeDetailLayout.setVisibility(View.GONE);
					//HomeDetailLayout.startAnimation(moveleft);
					
//					Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.inbottom);
//					RequestDetailLayout.startAnimation(MoveFromLeft);
//					MoveFromLeft.setAnimationListener(new Animation.AnimationListener() {
//						
//						@Override
//						public void onAnimationStart(Animation animation) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//						@Override
//						public void onAnimationRepeat(Animation animation) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//						@Override
//						public void onAnimationEnd(Animation animation) {
//							// TODO Auto-generated method stub
//							SelectAngelDetailButton.setEnabled(true);
//						}
//					});
					
					RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
					final int height=lp.height;
						//lp.height=(int)(300*getResources().getDisplayMetrics().density); 
						//AngleDetailLayout.setLayoutParams(lp);
						final Handler h=new Handler()
						{
							@Override
							public void handleMessage(Message m)
							{
								RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
									
									lp.height +=( (int)(m.arg1*getResources().getDisplayMetrics().density)); 
									AngleDetailLayout.setLayoutParams(lp);
							}
						};
					new Thread()
					{
						@Override
						public void run()
						{
							for(int i= height; i<=300*getResources().getDisplayMetrics().density; i++)
							{
								Message m=new Message();
								m.what=1;
								m.arg1=1;
								h.sendMessage(m);
								try {
									Thread.sleep(3);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}.start();
						
						
					RequestDetailLayout.setVisibility(View.VISIBLE);
					SelectAngelDetailButton.setVisibility(View.GONE);
					//RequestDetailLayout.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.scalefrombottom));
					
					((RelativeLayout)NextAngelButton.getParent()).setVisibility(View.GONE);
					
					((LinearLayout)RequestDetailLayout_ServiceTypeTV.getParent()).setVisibility(View.VISIBLE);
					((LinearLayout)FuelRequestLayout.getParent()).setVisibility(View.GONE);
					
					
					//setCurrentSideLayout(RequestDetailLayout);
					//comebhere
					RequestDetailLayout_ServiceTypeTV.setText(getResources().getStringArray(R.array.Services_Array)[SelectedFilter-1]);
					RequestDetailLayout_MaxWaitForArrivalButton.setText("0 Min");
					RequestDetailLayout_OfferAmmount.setText("");
					RequestDetaillayout_WaitTimeButton.setText("0 Min");
					
					currentLogedUser.setRequestService(getResources().getStringArray(R.array.Services_Array)[SelectedFilter-1]);
					justShowSelectedProvider();
					SelectAngelDetailButton.setEnabled(true);
				}
				else
				{
					showDefaultDialog("Message", "Please select provider");
				}
			}
		});
		CancelButtonRequestView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CancelButtonRequestView.setEnabled(false);
				
				
			
				Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
				HomeDetailLayout.setVisibility(View.VISIBLE);
				
				HomeDetailLayout.startAnimation(moveleft);
				moveleft.setAnimationListener(new Animation.AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						CancelButtonRequestView.setEnabled(true);
					}
				});
				
				
				Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuright);
				RequestDetailLayout.startAnimation(MoveFromLeft);
				setCurrentSideLayout(HomeDetailLayout);
				justShowAllAngels();
			}
		});
		RequestSubmitDetailButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(RequestDetailLayout_OfferAmmount.getText().toString().trim().equals("") || RequestDetailLayout_OfferAmmount.getText().toString().equals("0"))
				{
					showDefaultDialog("Alert", "Please enter Offer Amount");
					return;
				}
				if(RequestDetaillayout_WaitTimeButton.getText().toString().trim().equals("") || (RequestDetaillayout_WaitTimeButton.getText().toString().toLowerCase().contains("00 hr, 00 min") || RequestDetaillayout_WaitTimeButton.getText().toString().toLowerCase().contains("0 min")) && RequestDetaillayout_WaitTimeButton.getText().toString().toLowerCase().length()<6)
				{
					showDefaultDialog("Alert", "Please Enter Wait Time.");
					return;
				}
				if(RequestDetailLayout_MaxWaitForArrivalButton.getText().toString().trim().equals("") || ( RequestDetailLayout_MaxWaitForArrivalButton.getText().toString().toLowerCase().contains("00 hr, 00 min") || RequestDetailLayout_MaxWaitForArrivalButton.getText().toString().toLowerCase().contains("0 min")) && RequestDetailLayout_MaxWaitForArrivalButton.getText().toString().toLowerCase().length()<6)
				{
					showDefaultDialog("Alert", "Please Enter Max Arrival Wait Time.");
					return;
				}
				
				if(RequestDetailLayout_ServiceTypeTV.getText().toString().trim().equals(""))
				{
					showDefaultDialog("Alert", "Please choose Service Type first.");
					return;
				}
				
				if(currentLogedUser.getVehicleComplete()==null || currentLogedUser.getVehicleComplete().getvehicle_Id()==null || currentLogedUser.getVehicleComplete().getvehicle_Id().trim().equals(""))
				{
					//Toast.makeText(MainActivity.this, "Please choose vehicle first.", Toast.LENGTH_LONG).show();
					
					AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
					ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i=new Intent(MainActivity.this, VehicleSettings_Activity.class);
							startActivityForResult(i, 0);
						}
					});
					ab.setTitle("Jump Angels");
					ab.setMessage("Please choose vehicle first");
					AlertDialog ad=ab.create();
					ad.show();
					
					return;
				}
				
				if(!currentLogedUser.getUserCatagory().trim().toLowerCase().contains("provider") )
				{
					Motorist m=(Motorist)currentLogedUser.getUserObject();
					if(m.getcreaditcard_no()==null || m.getcreaditcard_no().trim().equals("") || m.getcreaditcard_no().trim().length()<15)
					{
						
						AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
						ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent i=new Intent(MainActivity.this, Account_Settings.class);
								startActivityForResult(i, 0);
							}
						});
						ab.setTitle("Jump Angels");
						ab.setMessage("Please add Credit Card Information.");
						AlertDialog ad=ab.create();
						ad.show();
						return;
					}
				}
				
//				Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuleft);
//				RequestApproveDetailLayout.setVisibility(View.VISIBLE);
//				setCurrentSideLayout(RequestApproveDetailLayout);
//				CurrentSideLayout.startAnimation(moveleft);
//				Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuleft);
//				RequestApproveDetailLayout.startAnimation(MoveFromLeft);
				
				int speed=25;
				//ouble Time=Math.round(Double.parseDouble(SelectedAngel.getdistance())/speed);
				
				
				
//				AngelBidDetailTV.setText("Requesting : "+SelectedAngel.getf_name()+""+SelectedAngel.getl_name()+"\r\n"+
//						"To : Need to discuss |r\n"+
//						"At : "+SelectedAngel.getlat()+","+SelectedAngel.getlng()+"\r\n"+
//						"Extimated Time to Location :"+Time+"km/h with speed of 25 km/h"+"\r\n"+
//						"Max wait Time : Set by requester"+"\r\n"+
//						"Bit Amount : Set by requester \r\n"
//						);
//				
				//AlertDialog.Builder db=new AlertDialog.Builder(MainActivity.this);
				
				//comebhere
				
				RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)AngleDetailLayout.getLayoutParams();
				
				lp.height=(int)(200*getResources().getDisplayMetrics().density); 
				AngleDetailLayout.setLayoutParams(lp);
				RequestDetailLayout.setVisibility(View.GONE);
				
				((RelativeLayout)NextAngelButton.getParent()).setVisibility(View.VISIBLE);
				((LinearLayout)RequestDetailLayout_ServiceTypeTV.getParent()).setVisibility(View.GONE);
				((LinearLayout)FuelRequestLayout.getParent()).setVisibility(View.VISIBLE);
				
				
				
				Intent i=new Intent(MainActivity.this, PaymentConfirmationActivity.class);
				i.putExtra("Service", SelectedFilter+"");
				i.putExtra("for", currentLogedUser.getVehicleComplete().getvehicleName());
				i.putExtra("from", SelectedAngel.getf_name()+" "+SelectedAngel.getl_name());
				i.putExtra("eta", SelectedAngel.getdistance());
				i.putExtra("distance", SelectedAngel.getdistance());
				i.putExtra("bidacceptancetime", WaitTime);
				i.putExtra("maxwaittime", MaxWaitofArrival);
				i.putExtra("bidamount", "$ "+RequestDetailLayout_OfferAmmount.getText());
				
				startActivityForResult(i, 0);
				
//				final Dialog ad=new Dialog(MainActivity.this);
//				
//				RelativeLayout DialogView=(RelativeLayout)LayoutInflater.from(getBaseContext()).inflate(R.layout.paymentconfirmationview, null);
//				ad.setContentView(DialogView);
//				ad.setTitle("Confirmation.");
//
//				ad.show();
//				ad.getWindow().setLayout(LayoutParams.MATCH_PARENT, (int)((280)*getResources().getDisplayMetrics().density));
//				
//				ApprovePaymentOKButton=(Button)DialogView.findViewById(R.id.ApprovePaymentOKButton);
//				ApprovePaymentCancelButton=(Button)DialogView.findViewById(R.id.ApprovePaymentCancelButton);
//			//	ApprovePaymentCheckBox=(CheckBox)DialogView.findViewById(R.id.ApprovePaymentCheckBox);
//				ApprovePaymentTermsAndConditionButton=(Button)DialogView.findViewById(R.id.ApprovePaymentTermsAndConditionButton);
//				
//				TextView tv=(TextView)DialogView.findViewById(R.id.ConfirmationDialogPaymentDescriptionTV);
//				tv.setText("Approve Payment for $"+RequestDetailLayout_OfferAmmount.getText().toString());
//				SelectedAngel.OfferAmount=RequestDetailLayout_OfferAmmount.getText().toString();
//				
//				ApprovePaymentOKButton.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						ad.cancel();
//
//						
//						
//						
//					}
//				});
//				
////				ApprovePaymentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////					
////					@Override
////					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////						if(isChecked)
////						{
////							ApprovePaymentOKButton.setEnabled(true);
////						}
////						else
////						{
////							ApprovePaymentOKButton.setEnabled(false);
////						}
////						
////					}
////				});
//				ApprovePaymentCancelButton.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						
//						ad.cancel();
//						
//						Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuright);
//						RequestDetailLayout.setVisibility(View.VISIBLE);
//						setCurrentSideLayout(RequestDetailLayout);
//						CurrentSideLayout.startAnimation(moveleft);
//						Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuleft);
//						RequestApproveDetailLayout.startAnimation(MoveFromLeft);
//					}
//				});
				
				WaitResponseCancelButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
						ab.setTitle("Confirmation");
						ab.setMessage("Are you sure. \r\nYou want to cancel request.");
						ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
						
								//Send Notification to provider from here.
								TimerStopFlag=true;
								Log.e("Time Closed","Timer Closed");
								arg0.cancel();
								WaitResponseTimeTV.setText("00:00:00");
								Animation moveleft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuright);
								HomeDetailLayout.setVisibility(View.VISIBLE);
								setCurrentSideLayout(HomeDetailLayout);
								CurrentSideLayout.startAnimation(moveleft);
								Animation MoveFromLeft=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuright);
								RequestApproveDetailLayout.startAnimation(MoveFromLeft);
								currentLocationLocator.startLocaitonUpdates();
								justShowAllAngels();
								
							}
						});
						ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								
							}
						});
						AlertDialog aab=ab.create();
						aab.show();
						
					}
				});
				
				//finished here
			}
		});
		ButtonRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.this, Registration_Choice_Activity.class);
				startActivityForResult(i, 0);
			}
		});
		
	}
	public void showDialogBox(String Title,String mesage)
	{
		AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
		ab.setTitle(Title);
		ab.setMessage(mesage);
		ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
		
				arg0.cancel();
				
			}
		});
		
		AlertDialog aab=ab.create();
		aab.show();
	}
	

	private void initiateControls() {
		
		MainBackButton=(Button)findViewById(R.id.MainBackButton);
		
		//DistenceCheck=(TextView)findViewById(R.id.DistenceCheck);
		
		FilterIconsArea=(LinearLayout)findViewById(R.id.FilterIconsArea);
		MapEventInterceptor=(LinearLayout)findViewById(R.id.MapEventInterceptor);
		AngelCancelButton=(Button)findViewById(R.id.AngelCancelButton);
		
		CurrentLocaitonButton=(Button)findViewById(R.id.CurrentLocaitonButton);
		
		FilterStatusTV=(TextView)findViewById(R.id.FilterStatusTV);
		
		ServicingLayout=(LinearLayout)findViewById(R.id.ServicingLayout);
		FuelRequestLayout=(LinearLayout)findViewById(R.id.FuelRequestLayout);
		
		RequestFarword_ETA=(TextView)findViewById(R.id.RequestFarword_ETA);
		
		VC_AssistanceType=(TextView)findViewById(R.id.VC_AssistanceType);
		VC_Vehicle=(TextView)findViewById(R.id.VC_Vehicle);
		VC_Color=(TextView)findViewById(R.id.VC_Color);
		 VC_LicencePlate=(TextView)findViewById(R.id.VC_LicencePlate);
		 VC_ChargePending=(TextView)findViewById(R.id.VC_ChargePending);
		 
		 PRO_AssistanceType=(TextView)findViewById(R.id.PRO_AssistanceType);
		 PRO_Vehicle=(TextView)findViewById(R.id.PRO_Vehicle);
		 PRO_Color=(TextView)findViewById(R.id.PRO_Color);
		 PRO_LicencePlate=(TextView)findViewById(R.id.PRO_LicencePlate);
		 PRO_MaxWaitTime=(TextView)findViewById(R.id.PRO_MaxWaitTime);
		 
		 PRO_PLUS_AssistanceType=(TextView)findViewById(R.id.PRO_PLUS_AssistanceType);
		 PRO_PLUS_Vehicle=(TextView)findViewById(R.id.PRO_PLUS_Vehicle);
		 PRO_PLUS_Color=(TextView)findViewById(R.id.PRO_PLUS_Color);
		 PRO_PLUS_LicencePlate=(TextView)findViewById(R.id.PRO_PLUS_LicencePlate);
		 
		 
		 
		 
		 SC_AssistanceType=(TextView)findViewById(R.id.SC_AssistanceType);
			SC_Vehicle=(TextView)findViewById(R.id.SC_Vehicle);
			SC_Color=(TextView)findViewById(R.id.SC_Color);
			 SC_LicencePlate=(TextView)findViewById(R.id.SC_LicencePlate);
		
			 MotoristsetAwardRating=(RatingBar)findViewById(R.id.MotoristsetAwardRating);
			 
			 
		MotoristAwardRating=(Button)findViewById(R.id.MotoristAwardRating);
		RequestRatingService_DetailTV=(TextView)findViewById(R.id.RequestRatingService_DetailTV);
		
		RequestService_RatingMotorist=(RelativeLayout)findViewById(R.id.RequestService_RatingMotorist);
		
		VisualConfirmedMotorist=(Button)findViewById(R.id.VisualConfirmedMotorist);
		
		GripBar=(LinearLayout) findViewById(R.id.GripBar);
		AngleDetailLayout=(RelativeLayout)findViewById(R.id.AngleDetailLayout);
		MenuBackButton=(Button)findViewById(R.id.MenuBackButton);
		Menulayout=(RelativeLayout)findViewById(R.id.Menulayout);
		MenuButton=(Button)findViewById(R.id.MenuButton);
		FilterMenuBackButton=(Button)findViewById(R.id.FilterMenuBackButton);
		FilterMenulayout=(RelativeLayout)findViewById(R.id.FilterMenulayout);
		FilterButton=(Button)findViewById(R.id.FilterButton);
		ServiceSpineer=(Spinner)findViewById(R.id.ServiceSpineer);
		DutySpinner=(Spinner)findViewById(R.id.DutySpinner);
		RattingSpinner=(Spinner)findViewById(R.id.RattingSpinner);
		initilizeSpinnerDefaultVaues();
		//LoginButton=(Button)findViewById(R.id.LoginButton);
		Settings_btn=(Button)findViewById(R.id.Settings_btn);
		
		
		BattryJumpFilter=(LinearLayout )findViewById(R.id.BattryJumpFilter);
		FuelDileveryFilter=(LinearLayout )findViewById(R.id.FuelDileveryFilter);
		BoomTowFilter=(LinearLayout )findViewById(R.id.BoomTowFilter);
		TireChangesFilter=(LinearLayout )findViewById(R.id.TireChangesFilter);
		DiagnosisFilter=(LinearLayout )findViewById(R.id.DiagnosisFilter);
		VehicleUnlockFilter=(LinearLayout )findViewById(R.id.VehicleUnlockFilter);
		RefereshAngelButton=(Button )findViewById(R.id.RefereshAngelButton);
		
		
		//RegisterButton=(Button)findViewById(R.id.RegisterButton);
		DialogDismiser=(LinearLayout)findViewById(R.id.DialogDismiser);
		LogOutButton=(Button)findViewById(R.id.LogOutButton);
		MapLayout=(RelativeLayout)findViewById(R.id.MapLayout);
		
		AngelBidDetailTV=(TextView)findViewById(R.id.AngelBidDetailTV);
		
		//CurrentlyDrivingTV=(TextView)findViewById(R.id.CurrentlyDrivingTV);
		CompanyNameTV=(TextView)findViewById(R.id.CompanyNameTV);
		//ServicesTV=(TextView)findViewById(R.id.ServicesTV);
		RattingTV=(RatingBar)findViewById(R.id.RattingTV);
		DistanceTV=(TextView)findViewById(R.id.DistanceTV);
		ETimeTV=(TextView)findViewById(R.id.ETimeTV);
		WelcomeDetailTV=(TextView)findViewById(R.id.WelcomeDetailTV);
		WelcomeTV=(TextView)findViewById(R.id.WelcomeTV);
		RequestSubmitDetailButton=(Button)findViewById(R.id.LoginUserButton);
		
		RequestDetailLayout_ServiceTypeTV=(TextView)findViewById(R.id.RequestDetailLayout_ServiceTypeTV);
		
		 BackAngelButton=(Button)findViewById(R.id.BackAngelButton);
		 NextAngelButton=(Button)findViewById(R.id.NextAngelButton);
		
		
		CancelButtonRequestView=(Button)findViewById(R.id.CancelButtonRequestView);
		
		HomeDetailLayout=(RelativeLayout)findViewById(R.id.HomeDetailLayout);
		CurrentSideLayout=FilterLayout;
		
		
		RequestDetailLayout=(RelativeLayout)findViewById(R.id.RequestDetailLayout);
		RequestApproveDetailLayout=(RelativeLayout)findViewById(R.id.RequestApproveDetailLayout);
		RequestWaitforForwordLayout=(RelativeLayout)findViewById(R.id.RequestWaitforForwordLayout);
		FilterLayout=(RelativeLayout)findViewById(R.id.FilterLayout);
		
		RequestWaitforResponseLayout=(RelativeLayout)findViewById(R.id.RequestWaitforResponseLayout);
		
		WaitResponseTimeTV=(TextView)findViewById(R.id.WaitResponseTimeTV);
		WaitResponseCancelButton=(Button)findViewById(R.id.WaitResponseCancelButton);
		
		SelectAngelDetailButton=(Button)findViewById(R.id.SelectAngelDetailButton);
		
		//RequestDetaillayout_WaitTimeSpinner=(Spinner)findViewById(R.id.RequestDetaillayout_WaitTimeSpinner);
		
		RequestDetaillayout_WaitTimeButton=(Button)findViewById(R.id.RequestDetaillayout_WaitTimeButton);
		
		RequestDetailLayout_OfferAmmount=(EditText)findViewById(R.id.RequestDetailLayout_OfferAmmount);
		//RequestDetailLayout_MaxWaitForArrival=(Spinner)findViewById(R.id.RequestDetailLayout_MaxWaitForArrival);
		RequestDetailLayout_MaxWaitForArrivalButton=(Button)findViewById(R.id.RequestDetailLayout_MaxWaitForArrivalButton);
		RequestService_Completed=(RelativeLayout)findViewById(R.id.RequestService_Completed);
		
		ButtonRegister=(Button)findViewById(R.id.ButtonRegister);
		
		//RequestFarword_DetailTextView=(TextView)findViewById(R.id.RequestFarword_DetailTextView);
	    RequestFarword_Timer=(TextView)findViewById(R.id.RequestFarword_Timer);
	    RequestFarword_CancelButton=(Button)findViewById(R.id.RequestFarword_CancelButton);
	    RequestFarowrd_CallMEButton=(Button)findViewById(R.id.RequestFarowrd_CallMEButton);
	    
	    
	    //ServiceCompletedDetail_TV=(TextView)findViewById(R.id.ServiceCompletedDetail_TV);
	    RequestCompleted_ServiceCompletedButton=(Button)findViewById(R.id.RequestCompleted_ServiceCompletedButton);
	    RequestCompleted_CallMeButton=(Button)findViewById(R.id.RequestCompleted_CallMeButton);
	    
	    
	    CallMeButtonProvider=(Button)findViewById(R.id.CallMeButtonProvider);
	    
	    // provider side controls
	    
	    RequestProviderSide_AcceptDeclineLayout=(RelativeLayout)findViewById(R.id.RequestProviderSide_AcceptDeclineLayout);
		
		RequestProviderSide_ChangeButton=(Button)findViewById(R.id.RequestProviderSide_ChangeButton);
		RequestProviderSide_AcceptButton=(Button)findViewById(R.id.RequestProviderSide_AcceptButton);
		RequestProviderSide_AcceptDeclineTimer=(TextView)findViewById(R.id.RequestProviderSide_AcceptDeclineTimer);
		RequestProviderSide_CancelButton=(Button)findViewById(R.id.RequestProviderSide_CancelButton);
		//RequestProviderSide_AcceptDeclineDetailTV=(TextView)findViewById(R.id.RequestProviderSide_AcceptDeclineDetailTV);
	    
		
		RequestProviderSide_FarwordLayout=(RelativeLayout )findViewById(R.id.RequestProviderSide_FarwordLayout);
		//RequestProviderSide_FarwordDetailTV=(TextView )findViewById(R.id.RequestProviderSide_FarwordDetailTV);
		RequestProviderSide_FarwordTotalTimeTV=(TextView )findViewById(R.id.RequestProviderSide_FarwordTotalTimeTV);
		RequestProviderSide_FarwordTimeRemaningTV=(TextView )findViewById(R.id.RequestProviderSide_FarwordTimeRemaningTV);
		RequestProviderSide_FarwordCancelButton=(Button )findViewById(R.id.RequestProviderSide_FarwordCancelButton);
		RequestProviderSide_FarwordCallMeButton=(Button )findViewById(R.id.RequestProviderSide_FarwordCallMeButton);
		
		EventHistory=(Button)findViewById(R.id.EventHistory);
        
        MainHandler=new MainActivityHandler(this);
        currentLocationLocator=new CurrentLocationLocator(this, getBaseContext(),false);
        currentLocationLocator.refreshLocation();
        
        
        
	}
	

	private void initilizeSpinnerDefaultVaues() {
		String[] ServiceList=new String[]{
				"Battery Jump",
				"Tire Change",
				"Mobile Tire Repair",
				"Tow",
				"Fuel Delievery",
				"Vehicle Diagnostics",
				"Vehicle Unlock"
		};
		ArrayAdapter<String> ServiceAdapter=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,ServiceList);
		ServiceSpineer.setAdapter(ServiceAdapter);
		ServiceAdapter.notifyDataSetChanged();
		
		
		String[] DutyList=new String[]{
				"Light",
				"Medium",
				"Heavy",
		};
		ArrayAdapter<String> DutyAdapter=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,DutyList);
		DutySpinner.setAdapter(DutyAdapter);
		DutyAdapter.notifyDataSetChanged();
		
		String[] RatingList=new String[]{
				"1",
				"2",
				"3",
				"4",
				"5"
		};
		ArrayAdapter<String> RatingListAdapter=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,RatingList);
		RattingSpinner.setAdapter(RatingListAdapter);
		RatingListAdapter.notifyDataSetChanged();
		
	}

	protected void showSplashScreen() {
        mSplashDialog = new Dialog(this, android.R.style.Holo_SegmentedButton);
        mSplashDialog.setContentView(R.layout.splashscreen);
        mSplashDialog.setCancelable(false);
        mSplashDialog.show();
         
        // Set Runnable to remove splash screen just in case
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            removeSplashScreen();
          }
        }, 3000);
    }
    
    protected void removeSplashScreen() {
        if (mSplashDialog != null) {
            mSplashDialog.dismiss();
            mSplashDialog = null;
        }
    }

    
    public void translateMenuLayoutLeft()
    {
    	Animation TranslateMenuView=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuleft);//new TranslateAnimation(1f, -200f, 0f, 0f);
		TranslateMenuView.setDuration(300);
		TranslateMenuView.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
			
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				Menulayout.setVisibility(View.GONE);
				
			}
		});
		
		Menulayout.startAnimation(TranslateMenuView);
    	
    }
    public void translateFilterLayoutRigth()
    {
    	
    	Animation TranslateMenuView=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuright);//new TranslateAnimation(1f, -200f, 0f, 0f);
		TranslateMenuView.setDuration(300);
		
		TranslateMenuView.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				FilterMenulayout.setVisibility(View.GONE);
				
			}
		});
		FilterMenulayout.startAnimation(TranslateMenuView);
    }
    public Handler getMainHandler()
    {
    	return MainHandler;
    }
    public void showDefaultDialog(String Title, String Message)
    {
    	AlertDialog.Builder db=new AlertDialog.Builder(MainActivity.this);
    	db.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.cancel();
				
			}
		});
    	db.setTitle(Title);
    	db.setMessage(Message);
    	AlertDialog ad=db.create();
    	ad.show();
        
    }
    public void setCurrentSideLayout(RelativeLayout LayoutToSetCurrent)
    {// it will must hide the previos and set params as current layout
    	if(CurrentSideLayout!=null)
    	{
    		CurrentSideLayout.setVisibility(View.GONE);
    	}
    	CurrentSideLayout=LayoutToSetCurrent;
    }
    
    public void setEnable_ForLoginUser(Boolean value_forButtons)
    {
    	//RegisterButton.setEnabled(value_forButtons);
    //	LoginButton.setEnabled(value_forButtons);
    	
    }
    public JSONObject notifications(String methodName,String user_id,String other_id,String master_id,String reponsetime,String arrivaltime,String amount,String service)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", methodName));
        arr.add(new BasicNameValuePair("user_id", user_id));
        
        arr.add(new BasicNameValuePair("other_id", other_id));
        arr.add(new BasicNameValuePair("master_id", master_id));
        arr.add(new BasicNameValuePair("reponsetime", reponsetime));
        arr.add(new BasicNameValuePair("arrivaltime", arrivaltime));
        arr.add(new BasicNameValuePair("amount", amount));
        arr.add(new BasicNameValuePair("service", "1"));
        arr.add(new BasicNameValuePair("lat",CurrentLocationLocator.ThisLat ));
        arr.add(new BasicNameValuePair("long", CurrentLocationLocator.ThisLong));
        
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
	        
	        Log.e("regresponse",""+builder.toString());
	        JSONObject json= new JSONObject(builder.toString());
	        return json;
//	        if(Boolean.parseBoolean(json.getString("error"))==false)
//	        {
//	        	
//	        	JSONArray data= json.getJSONArray("data");
//	        	//Toast.makeText(getBaseContext(), "Secceeded: "+json.getString("message"), Toast.LENGTH_LONG).show();
//	        	Log.e("Response",data.toString());
//	        	return data.toString();
//	        }
//	        else
//	        {
//	        	String Messageis=json.getString("message");
//	        	TimerStopFlag=true;
//				return json.toString();
//				
//				//return "failed due to technical problem:"+Messageis ;
//	        	
//	        }
	    }catch(Exception k){
        	
        	Log.e("Error",""+k.toString());
        	return null;
        }
        
        
    }
    
    public JSONObject rating(String methodName,String request_id,String motorist_id,String angel_id,String rating)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", methodName));
        arr.add(new BasicNameValuePair("request_id", request_id));
        
        arr.add(new BasicNameValuePair("user_id", motorist_id));
        arr.add(new BasicNameValuePair("rater_id", angel_id));
        arr.add(new BasicNameValuePair("rating", rating));
        
        Log.e("Ratings Parameters",""+arr);
        
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
	        
	        Log.e("regresponse",""+builder.toString());
	        JSONObject json= new JSONObject(builder.toString());
	        return json;
	    }catch(Exception k){
	    	
        	Log.e("Error in parsing json object",""+k.toString());
        	return null;
        }
        
        
    }
    
    public String getupdatelocation(String user_id)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", "getupdatelocation"));
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
	            }
	            else 
	            {

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
	        	//Toast.makeText(getBaseContext(), "Secceeded: "+json.getString("message"), Toast.LENGTH_LONG).show();
	        	Log.e("Response",data.toString());
	        	return data.toString();
	        }
	        else
	        {
	        	String Messageis=json.getString("message");
	        	TimerStopFlag=true;
				
				
				return "failed due to technical problem:"+Messageis ;
	        	
	        }
	    }catch(Exception k){
        	
        	Log.e("Error",""+k.toString());
        	return "failed";
        }
    }
    public JSONObject updatelocation(String user_id,String request_id)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", "updatelocation"));
        arr.add(new BasicNameValuePair("user_id", user_id));
        arr.add(new BasicNameValuePair("lat", currentLocationLocator.ThisLat));
        arr.add(new BasicNameValuePair("long", currentLocationLocator.ThisLong));
        arr.add(new BasicNameValuePair("request_id", request_id));
        
        Log.e("location update parameters",""+arr);
        
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
	        
	        Log.e("updatelocation response",""+builder.toString());
	        JSONObject json= new JSONObject(builder.toString());
	        return json;
	       
	    }catch(Exception k){
        	
        	Log.e("Error",""+k.toString());
        	return null;
        }
    }
    
    public String DistanceValue="";
    public String DistanceText="";
    
    public void Distance(LatLng s,LatLng e,final Handler h,final int index)
    {
    	String stance="";
    	HttpClient client = new DefaultHttpClient();
        try
        {

        String uurl="http://maps.googleapis.com/maps/api/directions/xml?origin="+s.latitude+","+s.longitude+"&destination="+e.latitude+","+e.longitude+"&sensor=false&units=metric&mode=driving";
        Log.e("Distance url",""+uurl);
        HttpPost httpGet = new HttpPost(uurl);
        
        HttpResponse response = client.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
        	InputStream is= response.getEntity().getContent();
        	
        	RootElement root = new RootElement("DirectionsResponse");
        	Element route=root.getChild("route");
        	Element leg=route.getChild("leg");
        	Element distance=leg.getChild("distance");
        Element  value=distance.getChild("value");
        Element text=distance.getChild("text");
        
        
        Element step =leg.getChild("step");
        Element duration=step.getChild("duration");
        
        Element dtext=duration.getChild("text");
        
        dtext.setTextElementListener(new TextElementListener() {
			
			@Override
			public void end(String body) {
				DistanceText=body;
				Message m=new Message();
				m.what=2;
				m.arg1=index;
				m.obj=body;
				h.sendMessage(m);
				Log.e("Duration Text Located",""+body);
			}
			
			@Override
			public void start(Attributes attributes) {
				Log.e("Duration Text Locating","");
			}
		});
        
        
        value.setTextElementListener(new TextElementListener() {
			
			@Override
			public void end(String body) {
				DistanceValue=body;
				Log.e("Distance value Located",""+body);
			}
			
			@Override
			public void start(Attributes attributes) {
				Log.e("Distance value locating","");
			}
		});
        text.setTextElementListener(new TextElementListener() {
			
			@Override
			public void end(String body) {
				DistanceText=body;
				Message m=new Message();
				m.what=1;
				m.arg1=index;
				m.obj=body;
				h.sendMessage(m);
				Log.e("Distance Text Located",""+body);
			}
			
			@Override
			public void start(Attributes attributes) {
				Log.e("Distance Text Locating","");
			}
		});
        
        	Xml.parse(is, Xml.Encoding.ISO_8859_1, root.getContentHandler());
        }
        
   
    
    }catch (Exception sd) {
		// TODO: handle exception
	}
    
    }
    public JSONObject signout(String user_id)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", "signout"));
        arr.add(new BasicNameValuePair("user_id", user_id));
       
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
    public void cancelReqest(final String Status)
	{
    	
    	
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
					Toast.makeText(getBaseContext(), "Connection Problem", Toast.LENGTH_SHORT).show();
					
					break;
				}
				case 1:
				{
					Toast.makeText(getBaseContext(), "Request Canceled", Toast.LENGTH_SHORT).show();
					setCurrentSideLayout(FilterLayout);
					TimerStopFlagProviderAcceptDeclineStart=true;
					FilterLayout.setVisibility(View.VISIBLE);
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
				
				String obj= acceptORreject_request(currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getrequestDetails().getmotorist_id(), currentLogedUser.getuser_id(), Status, "0", Reson);
				Log.e("Cancel Response",""+obj);
				if(obj.contains("failed") && obj.length()<10)
				{
					h.sendEmptyMessage(-1);
				}
				else
				{
					h.sendEmptyMessage(1);
				}
				
				
				
			}
		}.start();
		
	}
   
   
}
