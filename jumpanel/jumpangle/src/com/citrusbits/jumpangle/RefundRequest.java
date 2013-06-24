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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import businessclasses.JumpAngelCustomNotification;
import businessclasses.RefundDetails;
import businessclasses.Vehicle_Info;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RefundRequest extends Activity{

	EditText RequestDetailsET=null;
	EditText DetailsET=null;
	TextView Firstlabel=null;
	TextView SecondLabel=null;
	Button SubmitButton=null;
	
	RefundDetails refundDetails=new RefundDetails();
	
	String TransactionId="";
	ProgressDialog UpdateUserProgressDialog=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refundrequest);
		
		TransactionId= getIntent().getExtras().getString("Transactionid").trim();
		
		RequestDetailsET=(EditText)findViewById(R.id.RequestDetailsET);
		DetailsET=(EditText)findViewById(R.id.DetailsET);
		Firstlabel=(TextView)findViewById(R.id.Firstlabel);
		SecondLabel=(TextView)findViewById(R.id.SecondLabel);
		SubmitButton=(Button)findViewById(R.id.SubmitButton);
		SubmitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog("Loading Details");
				final Handler h=new Handler()
				{
					@Override
					public void handleMessage(Message m)
					{
					//	Toast.makeText(getBaseContext(), "Amount has been increased", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(getBaseContext(), "Refund Response Submited", Toast.LENGTH_SHORT).show();
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
						JSONObject obj= getrefund_request(TransactionId);
						if(obj==null)
						{
							h.sendEmptyMessage(-1);
						}
						else
						{
							h.sendEmptyMessage(1);
						}
						Log.e("Response increase amount is",""+obj);
						
						
					}
				}.start();
			}
		});
		
		
		
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
					finish();
					break;
				}
				case 1:
				{
					String first="$"+refundDetails.amount+" Refund Request from "+refundDetails.name+" ";
					String second=refundDetails.vehicle.getYear()+", "+refundDetails.vehicle.getMake()+", "+refundDetails.vehicle.getModel()+"\r\nTransaction ID: "+TransactionId;
					
					Firstlabel.setText(first);
					SecondLabel.setText(second);
					RequestDetailsET.setText(refundDetails.motorist_reason);
					break;
				}
				case 2:
				{
					Toast.makeText(getBaseContext(), "(0) Records Found.", Toast.LENGTH_SHORT).show();
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
				JSONObject obj= getrefund_request(TransactionId);
				try {
					if(!obj.getBoolean("error"))
					{
						
						JSONArray arr=obj.getJSONArray("data");
						JSONObject o=arr.getJSONObject(0);
						refundDetails.amount=o.getString("amount");
						refundDetails.transaction_id=o.getString("transaction_id");
						refundDetails.motorist_reason=o.getString("motorist_reason");
						refundDetails.name=o.getString("name");
						Vehicle_Info v=new Vehicle_Info();
						v.setvehicleName(o.getString("vehicleName"));
						v.setVehicle_Type(o.getString("vehicleType"));
						v.setYear(o.getString("modelYear"));
						v.setMake(o.getString("make"));
						v.setModel(o.getString("model"));
						v.setFuel_Type(o.getString("fuelType"));
						v.setColor(o.getString("color"));
						v.setLicensePlate(o.getString("licensePlate"));
						refundDetails.vehicle=v;
						
						h.sendEmptyMessage(1);
					}
					else
					{
						h.sendEmptyMessage(-1);
					}
					
						
						
						
					Log.e("Response increase amount is",""+obj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				h.sendEmptyMessage(1);
			}
		}.start();
		
	}
	public JSONObject getrefund_request(String transaction_id)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", "getrefund_request"));
        arr.add(new BasicNameValuePair("transaction_id", transaction_id.trim()));
       
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
	
	public JSONObject addangelreason(String transaction_id,String angel_reason,String angel_dispute)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", "addangelreason"));
        arr.add(new BasicNameValuePair("transaction_id", transaction_id.trim()));
        arr.add(new BasicNameValuePair("angel_reason", "Denying"));
        arr.add(new BasicNameValuePair("angel_dispute", angel_dispute));
       
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
	
	 public void showDialog(String Message)
		{
			UpdateUserProgressDialog=new ProgressDialog(RefundRequest.this);
			UpdateUserProgressDialog.setTitle(Message);
			UpdateUserProgressDialog.setMessage("Please wait");
			UpdateUserProgressDialog.setCancelable(false);
			UpdateUserProgressDialog.show();
		}
}
