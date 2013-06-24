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

import businessclasses.Vehicle_Info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleAdapter extends ArrayAdapter<Vehicle_Info>{

	VehicleSettings_Activity _Act=null;
	
	ArrayList<Vehicle_Info> data=new ArrayList<Vehicle_Info>();
	
	Boolean isProvider=true;
	
	public VehicleAdapter(Context context, int textViewResourceId,ArrayList<Vehicle_Info> d,VehicleSettings_Activity a,Boolean isprovider) {
		super(context,textViewResourceId,d);
		// TODO Auto-generated constructor stub
		_Act=a;
		data=d;
		isProvider=isprovider;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View v=convertView;
		if(v==null)
		{
			v=LayoutInflater.from(_Act).inflate(R.layout.vehicleitem, null);
		}
		
		TextView VehicleName=(TextView)v.findViewById(R.id.VehicleName);
		TextView VehicleMake=(TextView)v.findViewById(R.id.VehicleMake);
		TextView VehicleModel=(TextView)v.findViewById(R.id.VehicleModel);
		TextView VehicleYear=(TextView)v.findViewById(R.id.VehicleYear);
		TextView Driving=(TextView)v.findViewById(R.id.Driving);
		TextView Services=(TextView)v.findViewById(R.id.Services);
		Button selectionButton=(Button)v.findViewById(R.id.selectionButton);
		
		Button EditButton=(Button)v.findViewById(R.id.EditButton);
		Button DeleteButton=(Button)v.findViewById(R.id.DeleteButton);
		
		if(isProvider)
		{
			if(EditButton!=null)
			{
				EditButton.setVisibility(View.GONE);
			}
			if(DeleteButton!=null)
			{
				DeleteButton.setVisibility(View.GONE);
			}
		}
		else
		{
			if(EditButton!=null)
			{
				EditButton.setVisibility(View.VISIBLE);
				EditButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i=new Intent(_Act, Motorist_Add_Vehicle_Activity.class);
						i.putExtra("vehicleforEdit", data.get(position));
						_Act.startActivity(i);
					}
				});
			}
			if(DeleteButton!=null)
			{
				DeleteButton.setVisibility(View.VISIBLE);
				DeleteButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						AlertDialog.Builder ab=new AlertDialog.Builder(_Act);
						ab.setTitle("Delete Vehicle");
						ab.setMessage("Are you sure.");
						ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							
						
						
						
						_Act.showDialog("Removing Vehicle");
						
						final Handler h =new Handler()
						{
							@Override
							public void handleMessage(Message m)
							{
								switch (m.what) {
								case 1:
								{
									_Act.UpdateUserProgressDialog.cancel();
									Toast.makeText(_Act, m.obj.toString(), Toast.LENGTH_SHORT).show();
									if(m.obj.toString().toLowerCase().contains("successful"))
									{
										_Act.loadVehiclelist();
									}
									VehicleAdapter.this.notifyDataSetChanged();
									
									break;
								}
								case -1:
								{
									_Act.UpdateUserProgressDialog.cancel();
									Toast.makeText(_Act, m.obj.toString(), Toast.LENGTH_SHORT).show();
									VehicleAdapter.this.notifyDataSetChanged();
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
								String masterid=_Act.cu.getmaster_id();
								
								JSONObject Response=deleteVehicle("deleteVehicle", data.get(position).getvehicle_Id(), data.get(position).userid, (masterid.equals("0"))?data.get(position).userid:masterid);
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
					});
							
						ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						AlertDialog ad=ab.create();
						ad.show();
					}
				});
				
			}
		}
		
		if(VehicleName!=null)
		{
			VehicleName.setText(data.get(position).getvehicleName());
		}
		if(VehicleMake!=null)
		{
			VehicleMake.setText(data.get(position).getMake());
		}
		if(VehicleModel!=null)
		{
			VehicleModel.setText(data.get(position).getModel());
		}
		if(VehicleYear!=null)
		{
			VehicleYear.setText(data.get(position).getYear());
		}
		if(Driving!=null)
		{
			if(isProvider)
			{
				((LinearLayout)Driving.getParent()).setVisibility(View.VISIBLE);
				Driving.setText(data.get(position).Driving);
			}
			else
			{
				((LinearLayout)Driving.getParent()).setVisibility(View.GONE);
			}
			
		}
		if(Services!=null)
		{
			if(isProvider)
			{
				((LinearLayout)Services.getParent()).setVisibility(View.VISIBLE);
				Services.setText(data.get(position).Services);
			}
			else
			{
				((LinearLayout)Services.getParent()).setVisibility(View.GONE);
			}
		}
		if(selectionButton!=null)
		{
			if(data.get(position).Assign.equals("0"))
			{
				selectionButton.setText("Make Current");
				selectionButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						_Act.showDialog("Assigning Vehicle");
						
						final Handler h =new Handler()
						{
							@Override
							public void handleMessage(Message m)
							{
								switch (m.what) {
								case 1:
								{
									_Act.UpdateUserProgressDialog.cancel();
									Toast.makeText(_Act, m.obj.toString(), Toast.LENGTH_SHORT).show();
									if(m.obj.toString().toLowerCase().contains("successful"))
									{
										_Act.loadVehiclelist();
										_Act.setVehicle(data.get(position));
									}
									
									break;
								}
								case -1:
								{
									_Act.UpdateUserProgressDialog.cancel();
									Toast.makeText(_Act, m.obj.toString(), Toast.LENGTH_SHORT).show();
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
								String masterid=_Act.cu.getmaster_id();
								
								JSONObject Response=assignVehicle("assignVehicle", data.get(position).getvehicle_Id(), data.get(position).userid, data.get(position).userid);
								Log.e("assign Response",Response.toString());
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
				});
				
			}
			else
			{
				selectionButton.setText("Release");
				
				selectionButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						_Act.showDialog("Releasing Vehicle");
						final Handler h =new Handler()
						{
							@Override
							public void handleMessage(Message m)
							{
								switch (m.what) {
								case 1:
								{
									_Act.UpdateUserProgressDialog.cancel();
									Toast.makeText(_Act, m.obj.toString(), Toast.LENGTH_SHORT).show();
									if(m.obj.toString().toLowerCase().contains("successful"))
									{
										//_Act.showDialog("Loading Vehicles");
										_Act.setVehicle(null);
										_Act.loadVehiclelist();
										
									}
									//
								//	VehicleAdapter.this.notifyDataSetChanged();
									break;
								}
								case -1:
								{
									_Act.UpdateUserProgressDialog.cancel();
									Toast.makeText(_Act, m.obj.toString(), Toast.LENGTH_SHORT).show();
									break;
								}
								default:
									break;
								}
								//_Act.UpdateUserProgressDialog.cancel();
							}
						};
						
						new Thread()
						{
							@Override
							public void run()
							{
								String masterid=_Act.cu.getmaster_id();
								
								JSONObject Response=Release_vehicle("release_vehicle", data.get(position).getvehicle_Id(), data.get(position).userid);
								Log.e("assign Response",Response.toString());
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
				});
				
			}
		}
		
		
		return v;
	}
	
	private JSONObject deleteVehicle(String methodName,String vehicle_Id,String user_id,String master_id) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(_Act.getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        
	        arr.add(new BasicNameValuePair("methodName", methodName));
	        arr.add(new BasicNameValuePair("vehicle_Id", vehicle_Id));
	        arr.add(new BasicNameValuePair("user_id", user_id));
	        arr.add(new BasicNameValuePair("master_id", master_id));
	        Log.e("Input is ",arr.toString());
	        
	        
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
	private JSONObject assignVehicle(String methodName,String vehicle_Id,String assign,String user_id) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(_Act.getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        
	        arr.add(new BasicNameValuePair("methodName", methodName));
	        arr.add(new BasicNameValuePair("vehicle_Id", vehicle_Id));
	        arr.add(new BasicNameValuePair("assign", assign));
	        arr.add(new BasicNameValuePair("user_id", user_id));
	        Log.e("Input is ",arr.toString());
	        
	        
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
	private JSONObject Release_vehicle(String methodName,String vehicle_Id,String user_id) {
		
		 HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(_Act.getResources().getString(R.string.url));
	        BasicHttpParams parmsnew=new BasicHttpParams();
	        
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();
	        
	        
	        arr.add(new BasicNameValuePair("methodName", methodName));
	        arr.add(new BasicNameValuePair("vehicle_Id", vehicle_Id));
	        arr.add(new BasicNameValuePair("user_id", user_id));
	        Log.e("Input is ",arr.toString());
	        
	        
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
