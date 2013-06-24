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
import org.json.JSONException;
import org.json.JSONObject;

import businessclasses.Angel;
import businessclasses.CurrentLogedInUser;
import businessclasses.IncreaseAmountListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Payment_Activity extends Activity{

	TextView PaymentAssistanceType=null;
	TextView Payment_Status=null;
	TextView Payment_Provider=null;
	TextView Payment_Technician=null;
	RatingBar Payment_Rating=null;
	TextView ChargePending=null;
	TextView Payment_BidAmount=null;
	
	Button PayNowButton=null;
	Button RequestRefund=null;
	Button Payment_IncreaseAmount=null;
	
	CurrentLogedInUser currentLogedUser=new CurrentLogedInUser();
	
	Angel providerthis=new Angel();
	
	Dialog NumberPickerDialog=null;
	
	ListView MyListView=null;
	View view=null;
	ProgressDialog UpdateUserProgressDialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment);
		
		PaymentAssistanceType=(TextView)findViewById(R.id.PaymentAssistanceType);
		Payment_Status=(TextView)findViewById(R.id.Payment_Status);
		Payment_Provider=(TextView)findViewById(R.id.Payment_Provider);
		Payment_Technician=(TextView)findViewById(R.id.Payment_Technician);
		Payment_Rating=(RatingBar)findViewById(R.id.Payment_Rating);
		ChargePending=(TextView)findViewById(R.id.ChargePending);
		Payment_BidAmount=(TextView)findViewById(R.id.Payment_BidAmount);
		Payment_IncreaseAmount=(Button)findViewById(R.id.Payment_IncreaseAmount);
		
		PayNowButton=(Button)findViewById(R.id.PayNowButton);
		
		providerthis=(Angel)getIntent().getExtras().getSerializable("angel");
		RequestRefund=(Button)findViewById(R.id.RequestRefund);
		
		view=LayoutInflater.from(Payment_Activity.this).inflate(R.layout.mylistview, null);
		MyListView=(ListView)view.findViewById(R.id.MyListView);
		
		if(getThisSharedPreferences().contains("currentLogedUser"))
		{
			try {
				currentLogedUser=new CurrentLogedInUser();
				String user=getThisSharedPreferences().getString("currentLogedUser", "null");
				
				currentLogedUser.parseJSON(new JSONObject(user));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		RequestRefund.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder ab=new AlertDialog.Builder(Payment_Activity.this);
				final EditText et=new EditText(getBaseContext());
				ab.setView(et);
				ab.setTitle("Reason");
				ab.setMessage("Please insert Reason");
				ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				ab.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						final String reson=et.getText().toString();
						dialog.cancel();
						final Handler h=new Handler()
						{
							@Override
							public void handleMessage(Message m)
							{
								Toast.makeText(getBaseContext(), m.obj.toString(), Toast.LENGTH_SHORT).show();
								Intent i=new Intent();
								i.putExtra("myaction", "resetandrefund");
								setResult(RESULT_OK, i);
								UpdateUserProgressDialog.cancel();
								finish();
							}
						};
						showDialog("Requesting for refund");
						new Thread()
						{
							@Override
							public void run()
							{
								JSONObject obj=refund(currentLogedUser.getrequestDetails().getrequest_id(), "1", "1", reson);
								
								Log.e("Response increase amount is",""+obj);
								
								Message m=new Message();
								m.what=1;
								try {
									m.obj=obj.getString("message");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								h.sendMessage(m);
							}
						}.start();
						
					}
				});
				
				AlertDialog ad=ab.create();
				ad.show();
				
				
			}
		});
		
		PayNowButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				final Handler h=new Handler()
				{
					@Override
					public void handleMessage(Message m)
					{
						Toast.makeText(getBaseContext(), "Amount has been increased", Toast.LENGTH_SHORT).show();
						
						
						Intent i=new Intent();
						i.putExtra("myaction", "resetandpayed");
						setResult(RESULT_OK, i);
						UpdateUserProgressDialog.cancel();
						finish();
					}
				};
				showDialog("Loading");
				new Thread()
				{
					@Override
					public void run()
					{
						JSONObject obj=paynow(currentLogedUser.getrequestDetails().getrequest_id(), currentLogedUser.getuser_id(), currentLogedUser.getrequestDetails().getangel_id());
						Log.e("Response increase amount is",""+obj);
						h.sendEmptyMessage(1);
					}
				}.start();
				
				
				
			}
		});
		
		
		 String[] arr=getResources().getStringArray(R.array.Services_Array);
			try
			{
				PaymentAssistanceType.setText(arr[Integer.parseInt(currentLogedUser.getrequestDetails().getservice())]);
				
			}
				catch (Exception e) {
				// TODO: handle exception
			}
			Payment_Status.setText("Pending");
			Payment_Provider.setText(providerthis.getf_name()+" "+providerthis.getl_name());
			Payment_Technician.setText(providerthis.getf_name()+" "+providerthis.getl_name());
			Payment_Technician.setText("");
			Payment_Rating.setRating(Float.parseFloat(providerthis.rating));
			ChargePending.setText("$"+providerthis.OfferAmount);
			Payment_BidAmount.setText("$"+providerthis.OfferAmount);
			
			Payment_IncreaseAmount.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					NumberPickerDialog=new Dialog(Payment_Activity.this);
					
					
					ArrayList<String> arr=new ArrayList<String>();
					
					int start=Integer.parseInt(providerthis.OfferAmount);
					
					for(int i=start; i<start+100; i++)
					{
						arr.add(i+"");
					}
					//ArrayAdapter<String> adp=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, arr);
					IncreaseAmountListAdapter adp=new IncreaseAmountListAdapter(getBaseContext(), android.R.layout.simple_list_item_1, android.R.layout.simple_list_item_1, arr);
					MyListView.setAdapter(adp);
					adp.notifyDataSetChanged();
					NumberPickerDialog.setTitle("Increase Amount");
					NumberPickerDialog.setContentView(view);
					NumberPickerDialog.show();
					
				}
			});
			
			MyListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					TextView v=(TextView)arg1.findViewById(R.id.MyText);
					showDialog("Increasing Amount");
					
					final Handler h=new Handler()
					{
						@Override
						public void handleMessage(Message m)
						{
							Toast.makeText(getBaseContext(), "Amount has been increased", Toast.LENGTH_SHORT).show();
							NumberPickerDialog.cancel();
							UpdateUserProgressDialog.cancel();
						}
					};
					
					new Thread()
					{
						@Override
						public void run()
						{
							JSONObject obj= increase_amount(currentLogedUser.getuser_id(), currentLogedUser.getRequestAmount(), currentLogedUser.getrequestDetails().getrequest_id());
							Log.e("Response increase amount is",""+obj);
							h.sendEmptyMessage(1);
						}
					}.start();
				}
			});
			
	}
	
	public void showDialogBox(String Title,String mesage)
	{
		AlertDialog.Builder ab=new AlertDialog.Builder(Payment_Activity.this);
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
	
	public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
	 public JSONObject increase_amount(String user_id,String amount,String request_id)
	    {
	    	HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

	        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", "increase_amount"));
	        arr.add(new BasicNameValuePair("amount", amount));
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
	 public JSONObject paynow(String request_id,String motorist_id,String angel_id)
	    {
	    	HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

	        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", "paynow"));
	        arr.add(new BasicNameValuePair("request_id", request_id));
	        arr.add(new BasicNameValuePair("motorist_id", motorist_id));
	        arr.add(new BasicNameValuePair("angel_id", angel_id));
	        
	        
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
	 
	 public JSONObject refund(String request_id,String angel_arrive,String service_completed,String reason)
	    {
	    	HttpClient client = new DefaultHttpClient();
	        try
	        {
	       
	        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
	        
	        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

	        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
	        
	        arr.add(new BasicNameValuePair("methodName", "refund"));
	        arr.add(new BasicNameValuePair("request_id", request_id));
	        arr.add(new BasicNameValuePair("angel_arrive", angel_arrive));
	        arr.add(new BasicNameValuePair("service_completed", service_completed));
	        arr.add(new BasicNameValuePair("reason", reason));
	        
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
	 
	 public void showDialog(String Message)
		{
			UpdateUserProgressDialog=new ProgressDialog(Payment_Activity.this);
			UpdateUserProgressDialog.setTitle(Message);
			UpdateUserProgressDialog.setMessage("Please wait");
			UpdateUserProgressDialog.setCancelable(false);
			UpdateUserProgressDialog.show();
		}
	
}
