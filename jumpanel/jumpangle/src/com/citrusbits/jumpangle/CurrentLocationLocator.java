package com.citrusbits.jumpangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import businessclasses.Angel;
import businessclasses.DistanceOnIndex;
import businessclasses.Motorist;
import businessclasses.Provider;
import businessclasses.Vehicle_Info;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import WebServices.Connection;
import WebServices.WebConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class CurrentLocationLocator {

	LocationManager CurrentLocationManager=null;
	MainActivity LocationActivity=null;
	Context context=null;
	
	public static String ThisLat=null;
	public static String ThisLong=null;
	
	Boolean JustUpdateLatLong=false;
	
	Boolean ZoomOnlyFirstTimeFlag=true;
	
	ArrayList<String> AngelMarkerPointers=new ArrayList<String>();
	
	LocationListener CurrentLocationListner=null;
	
	public CurrentLocationLocator(MainActivity locActivity,Context cntx,Boolean justUpdateLatLong)
	{
		JustUpdateLatLong=justUpdateLatLong;
		context=cntx;
		if(locActivity!=null)
		{
			LocationActivity=locActivity;
		}
		CurrentLocationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		CurrentLocationListner=new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				try
				{
				MakeUseOfLocation(location);
				}
				catch (Exception e) {
					Log.e("my made Exception",""+e.toString());
				}
			}
		};
		startLocaitonUpdates();
	}
	
	public void stopLocationUpdates()
	{
		CurrentLocationManager.removeUpdates(CurrentLocationListner);
		CurrentLocationManager=null;
	}
	
	public void startLocationUpdatesAndUpdateProviderLocation()
	{
		CurrentLocationManager.removeUpdates(CurrentLocationListner);
		CurrentLocationManager=null;
		
		CurrentLocationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		CurrentLocationListner=new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				try
				{
				MakeUseOfLocation(location);
				}
				catch (Exception e) {
					
					if(!JustUpdateLatLong)
					{
						
					LocationActivity.mMap.clear();
					
					MarkerOptions mo=new MarkerOptions();
					LatLng mLocation=new LatLng(Double.parseDouble(LocationActivity.RequestMotorist.getLat()), Double.parseDouble(LocationActivity.RequestMotorist.getLong()));
					mo.position(mLocation);
					
					Motorist mtrst=((Motorist)LocationActivity.RequestMotorist);
					mo.title(mtrst.getFirstName()+" "+mtrst.getLastName());
					mo.snippet("Locaiton");
					
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.jumpangel));
					
					
					
					LocationActivity.mMap.addMarker(mo);
					// motorist added
					
					
					MarkerOptions pro=new MarkerOptions();
					LatLng PromLocation=new LatLng(location.getLatitude(), location.getLongitude());
					pro.position(PromLocation);
					
					Provider p=(Provider)LocationActivity.currentLogedUser.getUserObject();
					
					pro.icon(BitmapDescriptorFactory.fromResource(R.drawable.jumpangel));
					pro.title(p.getFirstName()+" "+p.getLastName());
					pro.snippet("My Locaiton");
					LocationActivity.mMap.addMarker(pro);
					
					stopLocationUpdates();
					}
				}
			}
		};
		refreshLocation();
	}
	
	public void startLocaitonUpdates()
	{
		CurrentLocationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		CurrentLocationListner=new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				try
				{
					MakeUseOfLocation(location);
				}
				catch (Exception e) {
					Log.e("my made Exception",""+e.toString());
				}
			}
		};
		refreshLocation();
		Log.e("Current location Locator Started","");
	}
	
	public void refreshLocation()
	{
		if(CurrentLocationManager!=null)
		CurrentLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, CurrentLocationListner);
	}
	public void MakeUseOfLocation(Location location)
	{
		
		final String lat= location.getLatitude()+"";
		final String lng=location.getLongitude()+"";
		ThisLat=lat;
		ThisLong=lng;
		if(!JustUpdateLatLong)
		{
		//LocationActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Motorist").snippet("My Location"));
   	 
			if(LocationActivity.LoadAngelOnlyOnce)
			{
				loadAngels("");
				//LocationActivity.LoadAngelOnlyOnce=false;
			}
			
	   	 	MarkerOptions MyLocation=new MarkerOptions();
	   	 	MyLocation.position(new LatLng(location.getLatitude(), location.getLongitude()));
	   	 	MyLocation.title("Motorist");
	   	 	MyLocation.snippet("My Location");
	   	 	MyLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
	   	 	
	   	 Log.e("Current Locator Location",ThisLat+","+ThisLong);
	   	 	
	   	 	
	   	 	Angel a=new Angel();
	   	 	
	   	 	ArrayList<MarkerOptions> markerslist=new ArrayList<MarkerOptions>();
	   	 	
	   	 	
			for(int i=0; i<LocationActivity.AngelsHashMap.size();i++)
			{
				Angel tempAngel=LocationActivity.AngelsHashMap.get(AngelMarkerPointers.get(i));
				MarkerOptions AngelMarker=new MarkerOptions();
				AngelMarker.position(new LatLng(Double.parseDouble(tempAngel.getlat().trim()), Double.parseDouble(tempAngel.getlng().trim())));
				AngelMarker.title(tempAngel.getf_name()+" "+tempAngel.getl_name());
				AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.jumpangel));
				markerslist.add(AngelMarker);
			}
			//adding this current location marker to this list
				markerslist.add(MyLocation);
			//
			LocationActivity.mMap.clear();
			//LocationActivity.AngelsHashMap.clear();
			
			for(int i=0; i<markerslist.size(); i++)
			{
				LocationActivity.mMap.addMarker(markerslist.get(i));
			}
			//LocationActivity.AngelsHashMap.put(LocationActivity.mMap.addMarker(MyLocation).getId(), null);
			CameraUpdate CameraToMotoristLocaiton=null;
			
			if(ZoomOnlyFirstTimeFlag)
			{
				CameraToMotoristLocaiton=CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),17);
				ZoomOnlyFirstTimeFlag=false;
			}
			else
			{
	   	 		CameraToMotoristLocaiton=CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
			}
			LocationActivity.mMap.animateCamera(CameraToMotoristLocaiton);
			Log.e("asad hashmi",lat+","+lng);
		}
		
	}
	
	public void zoom()
	{
		CameraUpdate CameraToMotoristLocaiton=null;
		
		if(ThisLat!=null && ThisLong!=null && !ThisLat.equals("0") && !ThisLong.equals("0"))
		{
			CameraToMotoristLocaiton=CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(ThisLat), Double.parseDouble(ThisLong)),17);
		
			LocationActivity.mMap.animateCamera(CameraToMotoristLocaiton);
		}
	}
	
	static public String LoadAngelServerceParameter="";
	
	public void loadAngels(final String service)
	{
		LocationActivity.RefereshAngelButton.setEnabled(false);
		LoadAngelServerceParameter=service;
		if(service.trim().equals(""))LocationActivity.LoadAngelOnlyOnce=true;
		
		//LoadingAngelZoomInFlag=true;
		String lat=ThisLat;
		String log=ThisLong;
		
		if(lat==null || log==null)
		{
			Log.e("returning ","because current latitude and longitude is not available.");
			//Toast.makeText(LocationActivity, "Current Location is not configured", Toast.LENGTH_SHORT).show();
			return;
		}
		
		final Handler h=new Handler()
		{
			
			
			@Override
			public void handleMessage(Message m)
			{
				switch (m.what) {
				case 2:
				{
					LocationActivity.mMap.clear();
					LocationActivity.AngelsHashMap.clear();
					break;
				}
				case 3:
				{
					Animation moveleft=AnimationUtils.loadAnimation(LocationActivity.getBaseContext(), R.anim.slidemenuright);
					LocationActivity.HomeDetailLayout.setVisibility(View.VISIBLE);
					LocationActivity.HomeDetailLayout.startAnimation(moveleft);
					Animation MoveFromLeft=AnimationUtils.loadAnimation(LocationActivity.getBaseContext(), R.anim.slidefiltermenuright);
					LocationActivity.FilterLayout.startAnimation(MoveFromLeft);
					LocationActivity.setCurrentSideLayout(LocationActivity.HomeDetailLayout);
					LocationActivity.FilterLayout.setVisibility(View.GONE);
					LocationActivity.HomeDetailLayout.setVisibility(View.VISIBLE);
					
					LocationActivity.FilterButton.setBackgroundResource(R.drawable.filter);
					LocationActivity.FilterButton.setTag("HomeLayout");
					
					Angel.selectedangelindex=0;
					Angel CurrentSelectedAngel= LocationActivity.AngelsHashMap.get(AngelMarkerPointers.get(Angel.selectedangelindex));
					LocationActivity.SelectedAngel=CurrentSelectedAngel;
					try
					{
					LocationActivity.CompanyNameTV.setText(CurrentSelectedAngel.getf_name()+" "+CurrentSelectedAngel.getl_name());
					}catch(Exception k)
					{
						Log.e("Exception Hided",""+k);
					}

					LocationActivity.mMap.clear();
					
					ArrayList<String> AngelMarkerPointers=new ArrayList<String>();
					
					HashMap<String , Angel> HashMapRelpacer=new HashMap<String, Angel>();
					
					
					for(int j=0; j<LocationActivity.currentLocationLocator.AngelMarkerPointers.size(); j++)
					{
						AngelMarkerPointers.add(LocationActivity.currentLocationLocator.AngelMarkerPointers.get(j));
						HashMapRelpacer.put(AngelMarkerPointers.get(j), LocationActivity.AngelsHashMap.get(LocationActivity.currentLocationLocator.AngelMarkerPointers.get(j)));
					}
					LocationActivity.currentLocationLocator.AngelMarkerPointers.clear();
					
					
					
					LocationActivity.AngelsHashMap.clear();
					for(int i=0; i<AngelMarkerPointers.size(); i++)
					{
						try
						{
						String key=AngelMarkerPointers.get(i);
						
						Angel tempAngel=(Angel)HashMapRelpacer.get(key);
						MarkerOptions AngelMarker=new MarkerOptions();
						
						AngelMarker.position(new LatLng(Double.parseDouble(tempAngel.getlat().trim()), Double.parseDouble(tempAngel.getlng().trim())));
						AngelMarker.title(tempAngel.getf_name()+" "+tempAngel.getl_name());
						
						if(LocationActivity.SelectedAngel.getuser_id().equals(tempAngel.getuser_id()))
						{
							AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconangelwithcircle));
						}
						else
						{
							AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.jumpangel));
						}
						
						if(tempAngel.getVehicle()!=null)
							AngelMarker.snippet(tempAngel.getVehicle().getvehicleName());
						String markerpointer=LocationActivity.mMap.addMarker(AngelMarker).getId();
						LocationActivity.currentLocationLocator.AngelMarkerPointers.add(markerpointer);
						LocationActivity.AngelsHashMap.put(markerpointer, tempAngel);
						}catch (Exception e) {
							Log.e("Exception Hided",""+e);
						}
					}
					
					for(int j=0; j<LocationActivity.currentLocationLocator.AngelMarkerPointers.size(); j++)
					{
						AngelMarkerPointers.add(LocationActivity.currentLocationLocator.AngelMarkerPointers.get(j));
					}
					
					
					MarkerOptions MyLocation=new MarkerOptions();
			   	 	MyLocation.position(new LatLng(Double.parseDouble(LocationActivity.currentLocationLocator.ThisLat), Double.parseDouble(LocationActivity.currentLocationLocator.ThisLong)));
			   	 	MyLocation.title("Motorist");
			   	 	MyLocation.snippet("My Location");
			   	 	MyLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
			   	 	
					LocationActivity.mMap.addMarker(MyLocation);
					
					
					
					
					
					sendEmptyMessage(4);
					
					
					
					
					
					Double d=null;
 					try
 					{
 						DecimalFormat df=new DecimalFormat("#0.00");
 						
 						d=Double.parseDouble(CurrentSelectedAngel.getdistance());
 						
 					    d=Double.parseDouble(df.format(d));
 					}
 					catch(Exception k){}
 					
 					
 					try{
 					LocationActivity.DistanceTV.setText(CurrentSelectedAngel.getdistance());
					//LocationActivity.DistanceTV.setText(CurrentSelectedAngel.getdistance());
					LocationActivity.RattingTV.setRating(Float.parseFloat(CurrentSelectedAngel.rating));
 					
					int speed=25;
					double Time=0;
					try{
						Time=Math.round(Double.parseDouble(CurrentSelectedAngel.getdistance().split(" ")[0])/speed);
					}catch(Exception k){}
					LocationActivity.ETimeTV.setText(CurrentSelectedAngel.getduration());
					LocationActivity.SelectedAngel=CurrentSelectedAngel;
					
					LocationActivity.ServicingLayout.removeAllViews();
					LocationActivity.FuelRequestLayout.removeAllViews();
					
					CameraUpdate CameraToMotoristLocaiton=CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(CurrentSelectedAngel.getlat()),Double.parseDouble(CurrentSelectedAngel.getlng())), 20);
			   	 	
					Log.e("Animating to point",CurrentSelectedAngel.getlat() +""+CurrentSelectedAngel.getlng());
					
			   	 	LocationActivity.mMap.animateCamera(CameraToMotoristLocaiton);
					
			   	 	
					
 					if(CurrentSelectedAngel.getVehicle()!=null)
 					{
 						if(LocationActivity.SelectedFilter==1 && !CurrentSelectedAngel.getVehicle().getBattery_Jumps().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("light"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("L", "Light Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("medium"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("M", "Medium Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getBattery_Jumps().toLowerCase().contains("heavy"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("H", "Heavy Duty"));
 							}
 							LocationActivity.FilterStatusTV.setText("Battry Request: ");
 							
 						}
 						else if(LocationActivity.SelectedFilter==2 && !CurrentSelectedAngel.getVehicle().getFuel_Delivery().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("D", "Diesel"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("C", "CNG"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("petrol"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("G", "Gasoline/Petrol"));
 							}
 							LocationActivity.FilterStatusTV.setText("Fuel Req: ");
 						}
 						else if(LocationActivity.SelectedFilter==3)
 						{
 							
 							LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("L", "Light Duty"));
 							
 							LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("M", "Medium Duty"));
 							
 							LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("H", "Heavy Duty"));
 							
 							
 							if(!CurrentSelectedAngel.getVehicle().getTow_Boom().contains("off"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("B", "Tow Boom"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_FlatBed().contains("off"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("F", "Flat Bed"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_Sling().contains("off"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("S", "Tow Sling"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_WheelLift().contains("off"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("WL", "Tow Wheel Lift"));
 							}
 							if(!CurrentSelectedAngel.getVehicle().getTow_Winch().contains("off"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("W", "Tow Winch"));
 							}
 						}
 						else if(LocationActivity.SelectedFilter==4 && !CurrentSelectedAngel.getVehicle().getTire_Changes().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("light"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("L", "Light Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("medium"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("M", "Medium Duty"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getTire_Changes().toLowerCase().contains("heavy"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("H", "Heavy Duty"));
 							}
 							
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("diesel"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("CH", "Tire Change"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getFuel_Delivery().toLowerCase().contains("cng"))
 							{
 								LocationActivity.FuelRequestLayout.addView(LocationActivity.getLabel("R", "Mobile Tire Repair"));
 							}
 							
 							LocationActivity.FilterStatusTV.setText("Tire Change: ");
 							
 						}
 						else if(LocationActivity.SelectedFilter==5 && !CurrentSelectedAngel.getVehicle().getDiagnostics().contains("off"))
 						{
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("diesel"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("D", "Diesel"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("cng"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("C", "CNG"));
 							}
 							if(CurrentSelectedAngel.getVehicle().getDiagnostics().toLowerCase().contains("petrol"))
 							{
 								LocationActivity.ServicingLayout.addView(LocationActivity.getLabel("G", "Gasoline/Petrol"));
 							}
 							LocationActivity.FilterStatusTV.setText("Diagnostics: ");
 						}
 						else if(LocationActivity.SelectedFilter==6 && !CurrentSelectedAngel.getVehicle().getVehicle_Unlocks().contains("off"))
 						{
 						//	ServicingLayout.addView(getLabel("V", "Vehicle Unlocks"));
 							LocationActivity.FilterStatusTV.setText("Vehicle Unlock: ");
 						}
 					}
 					}catch(Exception k){
 						
 						Log.e("Exception hided",""+k);
 						
 					}
 					LocationActivity.UpdateUserProgressDialog.cancel();
					
					break;
				}
				case 1:
				{
					Angel tempAngel=(Angel)m.obj;
					MarkerOptions AngelMarker=new MarkerOptions();
					
					AngelMarker.position(new LatLng(Double.parseDouble(tempAngel.getlat().trim()), Double.parseDouble(tempAngel.getlng().trim())));
					AngelMarker.title(tempAngel.getf_name()+" "+tempAngel.getl_name());
					AngelMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.jumpangel));
					
					if(tempAngel.getVehicle()!=null)
						AngelMarker.snippet(tempAngel.getVehicle().getvehicleName());
					String markerpointer=LocationActivity.mMap.addMarker(AngelMarker).getId();
					AngelMarkerPointers.add(markerpointer);
					LocationActivity.AngelsHashMap.put(markerpointer, tempAngel);
					//LocationActivity.UpdateUserProgressDialog.cancel();
					break;
				}
				case 4:
				{
					MarkerOptions MyLocation=new MarkerOptions();
			   	 	MyLocation.position(new LatLng(Double.parseDouble(ThisLat), Double.parseDouble(ThisLong)));
			   	 	MyLocation.title("Motorist");
			   	 	MyLocation.snippet("My Location");
			   	 	MyLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
			   	 	
					LocationActivity.mMap.addMarker(MyLocation);
					LocationActivity.UpdateUserProgressDialog.cancel();
					break;
				}
				case 61:
				{
					try
					{
					LocationActivity.RefereshAngelButton.setEnabled(true);
					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				    //if(currentapiVersion>=11)
				    //{
				    	//LocationActivity.RefereshAngelButton.setAlpha(1);
				    	LocationActivity.RefereshAngelButton.setBackgroundResource(R.drawable.refresh2);
				    //}
					//LocationActivity.RefereshAngelButton.setAlpha(1);
					}catch (Exception e) {
						// TODO: handle exception
					}
					LocationActivity.UpdateUserProgressDialog.cancel();
					break;
				}
				
				case 5:
				{
				
					//final ArrayList<DistanceOnIndex> distanceList=new ArrayList<DistanceOnIndex>();
					final Handler d=new Handler()
					{
						@Override
						public void handleMessage(Message m)
						{
							if(m.what==1)
							{
								DistanceOnIndex d=new DistanceOnIndex();
								String feets="";
								double ds=0;
								try{
									ds=Double.parseDouble(m.obj.toString().subSequence(0, m.obj.toString().indexOf(" "))+"");
									double feet=ds*3.2;
									
									feets=(String.format("%.02f", feet))+" ft";
								}catch(Exception k){}
								try{
								LocationActivity.AngelsHashMap.get(AngelMarkerPointers.get(m.arg1)).setdistance(feets);
								}catch(Exception dp){}
							}
							else if(m.what==2)
							{
								try
								{
								LocationActivity.AngelsHashMap.get(AngelMarkerPointers.get(m.arg1)).setduration(m.obj.toString());
								}catch(Exception d){}
							}
							
						}
					};
					
					new Thread()
					{
						@Override
						public void run()
						{
							try
							{
							for(int i=0; i<AngelMarkerPointers.size(); i++)
							{
								try
								{
								Angel tempAngel= LocationActivity.AngelsHashMap.get(AngelMarkerPointers.get(0));
								LatLng s=new LatLng(Double.parseDouble(tempAngel.getlat()), Double.parseDouble(tempAngel.getlng()));
								LatLng e=new LatLng(Double.parseDouble(ThisLat), Double.parseDouble(ThisLong));
								LocationActivity.Distance(s, e,d,i);
								}
								catch (Exception e) {
									Log.e("Null Execption arrises and removed","by me");
								}
								
							}
							if(LocationActivity.LoadAngelOnlyOnce)
							{
								LocationActivity.LoadAngelOnlyOnce=false;
								sendEmptyMessage(4);
								
							}
							else
							{
								sendEmptyMessage(3);
							}
							sendEmptyMessage(61);// for angel referesh button
							}catch (Exception e) {
								Log.e("Null Exception arrises and removed this","by me");
								sendEmptyMessage(61);
							}
						}
					}.start();
					break;
				}
				case 16:
				{
					Toast.makeText(LocationActivity, "Found (0) Active Angle on Map", Toast.LENGTH_LONG).show();
					sendEmptyMessage(4);
					LocationActivity.UpdateUserProgressDialog.cancel();
					//LocationActivity.UpdateUserProgressDialog.cancel();
					break;
				}
				default:
					//LocationActivity.UpdateUserProgressDialog.cancel();
					break;
				}
				//LocationActivity.UpdateUserProgressDialog.cancel();
			}
		};
		
		new Thread()
		{
			@Override
			public void run()
			{
				try {
					
					AngelMarkerPointers.clear();
					JSONObject jObject=new JSONObject(getJSON(LocationActivity.getResources().getString(R.string.url),ThisLat+"",ThisLong+"",service));
					Log.e("Angels Response",""+jObject.toString());
					
					String error= jObject.getString("error");
					String Message= jObject.getString("message");
					if(error.toLowerCase().equals("false"))
					{
						final JSONArray DataArray= new JSONArray(jObject.getString("data"));
						
						ArrayList<Angel> angels=new ArrayList<Angel>();
						
						for(int i=0; i<DataArray.length(); i++)
						{
							JSONObject obj=DataArray.getJSONObject(i);
							final Angel tempAngel=new Angel();
							
							
						try{tempAngel.setuser_id(obj.getString("user_id"));}catch(Exception k){}
						try{tempAngel.setlng(obj.getString("long"));}catch(Exception k){}
						try{tempAngel.setlat(obj.getString("lat"));}catch(Exception k){}
						
						try{tempAngel.setl_name(obj.getString("l_name"));}catch(Exception k){}
						try{tempAngel.setf_name(obj.getString("f_name"));}catch(Exception k){}
						try{tempAngel.setdistance(obj.getString("distance"));}catch(Exception k){}
						try{tempAngel.rating=(obj.getString("rating"));}catch(Exception k){}
						try{tempAngel.setContactNumber(obj.getString("phone"));}catch(Exception k){}
							
							//if(obj.has("vehicle_info"))
							//{
								//String VString=obj.getString("vehicle_info");
								
								Vehicle_Info VI=null;
								
										JSONObject vehicle_obj=obj;//vehicle_info.getJSONObject(j);
										VI=new Vehicle_Info();
										try{	VI.setvehicle_Id(vehicle_obj.getString("vehicle_Id"));}catch(Exception k){}
										try{VI.setvehicleName(vehicle_obj.getString("vehicleName"));}catch(Exception k){}
										try{VI.setBattery_Jumps(vehicle_obj.getString("Battery_Jumps"));}catch(Exception k){}
										try{VI.setFuel_Delivery(vehicle_obj.getString("Fuel_Delivery"));}catch(Exception k){}
										try{VI.setTow_Boom(vehicle_obj.getString("Tow_Boom"));}catch(Exception k){}
										try{VI.setTow_FlatBed(vehicle_obj.getString("Tow_FlatBed"));}catch(Exception k){}
										try{VI.setTow_Sling(vehicle_obj.getString("Tow_Sling"));}catch(Exception k){}
										try{VI.setTow_WheelLift(vehicle_obj.getString("Tow_WheelLift"));}catch(Exception k){}
										try{VI.setTow_Winch(vehicle_obj.getString("Tow_Winch"));}catch(Exception k){}
										try{VI.setTire_Changes(vehicle_obj.getString("Tire_Changes"));}catch(Exception k){}
										try{VI.setMobileTireRepair(vehicle_obj.getString("MobileTireRepair"));}catch(Exception k){}
										try{VI.setDiagnostics(vehicle_obj.getString("Diagnostics"));}catch(Exception k){}
										try{VI.setVehicle_Unlocks(vehicle_obj.getString("Vehicle_Unlocks"));}catch(Exception k){}
									//}
									tempAngel.setVehicle(VI);
								//}
								
							//}
							angels.add(tempAngel);
							
						}
						h.sendEmptyMessage(2);
						
						for(int i=0; i<angels.size(); i++)
						{
							Message m=new Message();
							m.obj=angels.get(i);
							m.what=1;
							h.sendMessage(m);
						}
						if(LocationActivity.LoadAngelOnlyOnce)
						{
							//LocationActivity.LoadAngelOnlyOnce=false;
							//h.sendEmptyMessage(4);
							h.sendEmptyMessage(5);
						}
						else
						{
							h.sendEmptyMessage(5);
							//h.sendEmptyMessage(3);
						}
						
						//JSONArray arr=jObject.getJSONArray("data");
						//int k=0;
						
					}
					else
					{
						h.sendEmptyMessage(16);
					}
					
					
				} catch (JSONException e) {
					//Toast.makeText(LocationActivity.getApplicationContext(), "Loading failed : "+e.toString(), Toast.LENGTH_LONG).show();
					Log.e("loadingfailed",""+e.toString());
					e.printStackTrace();
					h.sendEmptyMessage(-1);
				}
				
			}
			
		}.start();
		
		if(!LocationActivity.JustRefereshing)
		{
		LocationActivity.showDialog("Loading Angels");
		}
		LocationActivity.JustRefereshing=false;
	}
	
	public String getJSON(String rurl,String lat,String lng,String service) {
        StringBuilder builder = new StringBuilder();
        //HttpClient client = new DefaultHttpClient();
        try
        {
        	HttpParams httpParameters = new BasicHttpParams();
    		HttpConnectionParams.setConnectionTimeout(httpParameters, 50000);
    		HttpConnectionParams.setSoTimeout(httpParameters, 10000);
    		
    		HttpClient client=new DefaultHttpClient(httpParameters);
        	
        HttpPost httpGet = new HttpPost(rurl);
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
        arr.add(new BasicNameValuePair("methodName", "getAngels"));
        arr.add(new BasicNameValuePair("lat", lat));
        arr.add(new BasicNameValuePair("long", lng));
        arr.add(new BasicNameValuePair("service", service));
        
        httpGet.setEntity(new UrlEncodedFormEntity(arr));
        
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
        }catch (Exception e) {
        	int k=0;
		}
        return builder.toString();
    }
}
