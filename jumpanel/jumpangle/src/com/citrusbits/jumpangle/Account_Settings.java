package com.citrusbits.jumpangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyFactorySpi;
import java.text.DecimalFormat;
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

import businessclasses.CurrentLogedInUser;
import businessclasses.Motorist;
import businessclasses.Provider;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.StaticLayout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Account_Settings extends Activity {

	Button UpdateButton=null;
	EditText EmailAddress=null;
	EditText OldPasword=null;
	EditText NewPasword=null;
	EditText CreditCardNumber=null;
	EditText CreditCardExpDate=null;
	EditText ThreeDigitCode=null;
	EditText Phone=null;
	
	ProgressDialog UpdateUserProgressDialog=null;
	DatePicker dp=null;
	LinearLayout ProviderLayout=null;
	 
	Boolean isProvider=true;
	
	CurrentLogedInUser currentLogedInUser=null;
	AlertDialog ad=null;
	AlertDialog.Builder ab=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		dp=new DatePicker(getBaseContext());
		try
		{
		    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		    if(currentapiVersion>=11)
		    {
		    	dp.setCalendarViewShown(false);
		    }
		}catch(Exception d){}
		
		ab=new AlertDialog.Builder(Account_Settings.this);
		ab.setTitle("Exp. Date");
		
		
		ab.setView(dp);
		ab.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str=dp.getDayOfMonth()+"-"+ (dp.getMonth()+1)+"-"+ dp.getYear();
				CreditCardExpDate.setText(str);
			}
		});
		ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		ad=ab.create();
		
		
		
		
		SharedPreferences sp=getThisSharedPreferences();
		String cuser= sp.getString("currentLogedUser", "");
		currentLogedInUser=new CurrentLogedInUser();
		try {
			currentLogedInUser.parseJSON(new JSONObject(cuser));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(currentLogedInUser.getUserCatagory().toLowerCase().contains("provider"))
		{
			isProvider=true;
		}
		else
		{
			isProvider=false;
		}
		
		
		initiateControls();
		initiateListners();
		
	}
	
	public void showpDialog()
	{
		UpdateUserProgressDialog=new ProgressDialog(Account_Settings.this);
		UpdateUserProgressDialog.setTitle("Updateing record");
		UpdateUserProgressDialog.setMessage("Please wait");
		UpdateUserProgressDialog.setCancelable(false);
		UpdateUserProgressDialog.show();
	}
	
	public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}

	private void initiateListners() {
		
		//PhoneNumberFormattingTextWatcher tv=new PhoneNumberFormattingTextWatcher();
		TextWatcher tv=new TextWatcher() {
			Boolean Append=true;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()==1)
				{
					//Editable ed=new Editable.Factory().newEditable("("+s);
					//s=ed;
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int ln= s.toString().length();
				if((ln==6 || ln==5) && ( s.toString().contains(")")))
				{
					Append=false;
					//Editable ed=new Editable.Factory().newEditable("("+s);
					//s=ed;
				}
				if(ln==10 && s.toString().contains("-"))
				{
					Append=false;
				}
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				if(s.toString().length()==1 && !s.toString().contains("("))
				{
					//Editable ed=new Editable.Factory().newEditable("("+s);
					//s.clearSpans();
					try
					{
					final String t="("+s+"";
					
	                
					s.replace(0, 1, t);
					//s.clear();
					
					
					//s.append(t);
					
					//s.insert(0, t);
					//s.append(t,0,s.length());
					Log.e("value is ",""+s);
					}catch(Exception k){}
				}
				if((s.toString().length()==4 && !s.toString().contains(")")))
					{
						if(Append)
						{
							s.append(") ");
							
						}
						else
						{
							Append=true;
						}
					
					}
				Log.e("s.length()",""+s.length());
				if((s.length()==9 && !s.toString().contains("-")))
				{
					if(Append)
					{
						s.append("-");
						
					}
					else
					{
						Append=true;
					}
				
				}
				Append=true;
			}
		};
		
		Phone.addTextChangedListener(tv);
//		Phone.setOnKeyListener(new View.OnKeyListener() {
//			
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// TODO Auto-generated method stub
//				Log.e("Key Code",""+keyCode+","+KeyEvent.KEYCODE_0);
//				if(keyCode==KeyEvent.KEYCODE_0)return false;
//				if(keyCode==KeyEvent.KEYCODE_1)return false;
//				if(keyCode==KeyEvent.KEYCODE_2)return false;
//				if(keyCode==KeyEvent.KEYCODE_3)return false;
//				if(keyCode==KeyEvent.KEYCODE_4)return false;
//				if(keyCode==KeyEvent.KEYCODE_5)return false;
//				if(keyCode==KeyEvent.KEYCODE_6)return false;
//				if(keyCode==KeyEvent.KEYCODE_7)return false;
//				if(keyCode==KeyEvent.KEYCODE_8)return false;
//				if(keyCode==KeyEvent.KEYCODE_9)return false;
//				if(keyCode==KeyEvent.KEYCODE_DEL)return false;
//				
//				
//				return true;
//			}
//		});
		UpdateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String phon=Phone.getText().toString();
				final String old=OldPasword.getText().toString();
				final String newp=NewPasword.getText().toString();
				final String email=EmailAddress.getText().toString();
				final String CNumber=CreditCardNumber.getText().toString();
				final String CDate=CreditCardExpDate.getText().toString();
				final String ThreeDititCode=ThreeDigitCode.getText().toString();
				
					if(phon.trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Phone.", Toast.LENGTH_LONG).show();
						return;
					}
					if(email.trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Email Address.", Toast.LENGTH_LONG).show();
						return;
					}
					if(old.trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Old Pasword.", Toast.LENGTH_LONG).show();
						return;
					}
					if(newp.trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide New Pasword.", Toast.LENGTH_LONG).show();
						return;
					}
					
					
					
				if(!currentLogedInUser.getUserCatagory().toLowerCase().contains("provider"))
				
				{
					if(CNumber.trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Credit Card Number.", Toast.LENGTH_LONG).show();
						return;
					}
					if(CNumber.length()!=16)
					{
						Toast.makeText(getBaseContext(), "Please provide valid Credit Card Number", Toast.LENGTH_LONG).show();
						return;
					}
					if(CDate.trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Expiry Date.", Toast.LENGTH_LONG).show();
						return;
					}
					if(ThreeDititCode.trim().equals(""))
					{
						Toast.makeText(getBaseContext(), "Please provide Three Digit Code.", Toast.LENGTH_LONG).show();
						return;
					}
					
					String newpp=((Motorist)currentLogedInUser.getUserObject()).getPasword().toString();
					
					if(!old.toString().contentEquals(newpp))
					{
						Toast.makeText(getBaseContext(), "Incorrect Old Password", Toast.LENGTH_LONG).show();
					}
					
				}
				else
				{
					
					String newpp=((Provider)currentLogedInUser.getUserObject()).getPasword().toString();
					if(!old.toString().contentEquals(newpp))
					{
						Toast.makeText(getBaseContext(), "Incorrect Old Password", Toast.LENGTH_LONG).show();
					}
					
				}
				
				
				
				
				if(!Login_Activity.isValidEmail(email))
				{
					Toast.makeText(getBaseContext(), "Please provide valid Email Address", Toast.LENGTH_LONG).show();
					return;
				}
				
				
				
				
				
				
				
			final	Handler h=new Handler()
				{
					@Override
					public void handleMessage(Message m)
					{
						if(m.what==1)
						{
							
							
							
							if(currentLogedInUser.getUserCatagory().trim().toLowerCase().contains("provider"))
							{
								Provider p=(Provider)currentLogedInUser.getUserObject();
								p.setCellNumber(phon);
								p.setEmailAddress(email);
								p.setPasword(newp);
								currentLogedInUser.setUserDetails("provider", p);
								
							}
							else
							{
								Motorist mm=(Motorist)currentLogedInUser.getUserObject();
								mm.setCellNumber(phon);
								mm.setEMailAddress(email);
								mm.setPasword(newp);
								mm.setcreaditcard_no(CNumber);
								mm.setExpiryDate(CDate);
								mm.setThreeDigitSecurityCode(ThreeDititCode);
								currentLogedInUser.setUserDetails("motorist", mm);
							}
							
							
							SharedPreferences sp=getThisSharedPreferences();
							SharedPreferences.Editor edtr= sp.edit();
							edtr.putString("currentLogedUser", currentLogedInUser.getJSONString());
							edtr.commit();
							
							Toast.makeText(getBaseContext(), "Record Successfully Updated", Toast.LENGTH_SHORT).show();
							finish();
							
						}
						else if(m.what==-1)
						{
							Toast.makeText(getBaseContext(), "Connection problem", Toast.LENGTH_SHORT).show();
						}
						UpdateUserProgressDialog.dismiss();
					}
				};
				
				showpDialog();
				new Thread()
				{
					
					@Override
					public void run()
					{
						String CNum=CNumber;
						String CDat=CDate;
						String ThreeDigit=ThreeDititCode;
						
						if(isProvider)
						{
							CNum="";
							CDat="";
							ThreeDigit="";
						}
						else
						{
							
						}
						String Response=updateUser("updateUser", phon, email, newp, CNum, CDat, ThreeDigit,currentLogedInUser.getuser_id());
						if(!Response.toLowerCase().contains("failed"))
						{
							JSONObject obj=null;
							try {
								obj=new JSONObject(Response);
								
								if(isProvider)
								{
									 Provider p=(Provider)currentLogedInUser.getUserObject();
									 p.setCellNumber(phon);
									 p.setPasword(newp);
								}
								 else
								{
									Motorist mo=(Motorist)currentLogedInUser.getUserObject();
									mo.setCellNumber(phon);
									mo.setPasword(newp);
									mo.setcreaditcard_no(CNum);
									mo.setExpiryDate(CDat);
									mo.setThreeDigitSecurityCode(ThreeDigit);
								}
								
								 h.sendEmptyMessage(1);
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								h.sendEmptyMessage(-1);
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
		});
	}

	public String updateUser(String methodName,String phone,String email,String password,String creditcard_no,String creditcard_enddate,String threeDigitCode,String user_id)
    {
    	HttpClient client = new DefaultHttpClient();
        try
        {
       
        HttpPost httpGet = new HttpPost(getResources().getString(R.string.url));
        
        ArrayList<NameValuePair> arr=new ArrayList<NameValuePair>();

        //String android_id=Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);     
        
        arr.add(new BasicNameValuePair("methodName", methodName));
        arr.add(new BasicNameValuePair("phone", phone));
        
        arr.add(new BasicNameValuePair("email", email));
        arr.add(new BasicNameValuePair("password", password));
        arr.add(new BasicNameValuePair("creditcard_no", creditcard_no));
        arr.add(new BasicNameValuePair("creditcard_enddate", creditcard_enddate));
        arr.add(new BasicNameValuePair("threeDigitCode", threeDigitCode));
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
	        	
	        	
	        	//Toast.makeText(getBaseContext(), "Secceeded: "+json.getString("message"), Toast.LENGTH_LONG).show();
	        	
	        	return builder.toString();
	        }
	        else
	        {
	        	String Messageis=json.getString("message");
	        	
				return "failed due to technical problem:"+Messageis ;
	        	
	        }
	    }catch(Exception k){
        	
        	Log.e("Error",""+k.toString());
        	return "failed";
        }
        
        
    }
	
	private void initiateControls() {
		UpdateButton=(Button)findViewById(R.id.UpdateButton);
		EmailAddress=(EditText)findViewById(R.id.EmailAddress);
		OldPasword=(EditText)findViewById(R.id.OldPasword);
		NewPasword=(EditText)findViewById(R.id.NewPasword);
		
		CreditCardNumber=(EditText)findViewById(R.id.CreditCardNumber);
		CreditCardExpDate=(EditText)findViewById(R.id.CreditCardExpDate);
		ThreeDigitCode=(EditText)findViewById(R.id.ThreeDigitCode);
		Phone=(EditText)findViewById(R.id.Phone);
		
		
		CreditCardExpDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ad.show();
			}
		});
		
		ProviderLayout=(LinearLayout)findViewById(R.id.ProviderLayout);
		
		if(isProvider)
		{
			ProviderLayout.setVisibility(View.GONE);
			Provider p=new Provider();
			p=(Provider)currentLogedInUser.getUserObject();
			Phone.setText(p.getCellNumber());
			EmailAddress.setText(p.getEmailAddress());
			
			
		}
		else
		{
			ProviderLayout.setVisibility(View.VISIBLE);
			Motorist p=new Motorist();
			p=(Motorist)currentLogedInUser.getUserObject();
			Phone.setText(p.getCellNumber());
			EmailAddress.setText(p.getEMailAddress());
			CreditCardNumber.setText(p.getcreaditcard_no());
			CreditCardExpDate.setText(p.getExpiryDate());
			
		}
		
		
	}

	
	
}
