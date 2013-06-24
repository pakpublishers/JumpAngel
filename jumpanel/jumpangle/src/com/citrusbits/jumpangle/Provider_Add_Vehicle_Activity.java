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
import org.json.JSONException;
import org.json.JSONObject;

import businessclasses.CurrentLogedInUser;
import businessclasses.Motorist;
import businessclasses.Provider;
import businessclasses.VehicleBundle;
import businessclasses.Vehicle_Info;
import businessclasses.Vehivle_Service;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Provider_Add_Vehicle_Activity extends Activity{

	Button ProviderAddVehicleCancelButton=null;
	
	Button AddVehicleServiceProfile=null;
	EditText AddVehicleVehicleName=null;
	Spinner AddVehicleVehicleType=null;
	Spinner AddVehicleVehicleYear=null;
	EditText AddVehicleVehicleMake=null;
	EditText AddVehicleVechicleModel=null;
	Spinner AddVehicleVehicleFuel=null;
	EditText AddVehicleVehicleColor=null;
	EditText AddVehicleVehicleLicencePlate=null;
	Spinner AddVehicleMotoristPlateState=null;
	TextView PAV_Title=null;
	
	Motorist MotoristForRegistration=null;
	
	//VehicleBundle VB=new VehicleBundle();
	Provider ProviderUser=null;
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			if(data.hasExtra("vehicleservice"))
			{
				//VB.set_Vehicle_Service((Vehivle_Service)data.getParcelableExtra("vehicleservice"));
				//VB.set_Vehivle_Infor(getVehicleFromControls());
			//	Toast.makeText(getBaseContext(), "Vehicle "+VB.get_Vehivle_Infor().getvehicleName()+" is Added", Toast.LENGTH_LONG).show();
				clearAllControls();
				
				int k=0;
			}
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider_add_vehicle);
		initiateControls();
		initiateListners();
		
		if(getIntent().hasExtra("Provider"))
		{
			ProviderUser=(Provider)getIntent().getSerializableExtra("Provider");
			Log.e("Provider Recieved",ProviderUser.getFirstName().toString());
		}
	}

	private void initiateListners() {
	
		AddVehicleServiceProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				

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
				
				
				Intent i=new Intent(Provider_Add_Vehicle_Activity.this, Add_Service_Profile_Activity.class);
				i.putExtra("VehicleInfo", getVehicleFromControls());
				i.putExtra("Provider", ProviderUser);
				startActivityForResult(i, 0);
				
			}
		});
		ProviderAddVehicleCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	
	private void initiateControls() {
		ProviderAddVehicleCancelButton=(Button)findViewById(R.id.ProviderAddVehicleCancelButton);
	     AddVehicleServiceProfile=(Button)findViewById(R.id.AddVehicleServiceProfile);
		 AddVehicleVehicleName=(EditText)findViewById(R.id.AddVehicleVehicleName);
		 AddVehicleVehicleType=(Spinner)findViewById(R.id.AddVehicleVehicleType);
		 AddVehicleVehicleYear=(Spinner)findViewById(R.id.AddVehicleVehicleYear);
		 AddVehicleVehicleMake=(EditText)findViewById(R.id.AddVehicleVehicleMake);
		 AddVehicleVechicleModel=(EditText)findViewById(R.id.AddVehicleVechicleModel);       
		 AddVehicleVehicleFuel=(Spinner)findViewById(R.id.AddVehicleVehicleFuel);
		 AddVehicleVehicleColor=(EditText)findViewById(R.id.AddVehicleVehicleColor);
		 AddVehicleVehicleLicencePlate=(EditText)findViewById(R.id.AddVehicleVehicleLicencePlate);
		 AddVehicleMotoristPlateState=(Spinner)findViewById(R.id.AddVehicleMotoristPlateState);
		 
		 PAV_Title=(TextView)findViewById(R.id.PAV_Title);
		 //PAV_Title.setText(getIntent().getStringExtra("title"));
		 AddVehicleServiceProfile.setText("Add Vehicle Service profile");
		 //AddVehicleServiceProfile.setText("Add Vehicle "+Provider_Register_Activity.CurrentFleetVehicle+" Service profile");
	}
	public Vehicle_Info getVehicleFromControls()
	{
		Vehicle_Info VI=new Vehicle_Info();
		VI.setvehicleName(AddVehicleVehicleName.getText().toString());
		VI.setVehicle_Type(AddVehicleVehicleType.getSelectedItem().toString());
		VI.setYear(AddVehicleVehicleYear.getSelectedItem().toString());
		VI.setMake(AddVehicleVehicleMake.getText().toString());
		VI.setModel(AddVehicleVechicleModel.getText().toString());
		VI.setFuel_Type(AddVehicleVehicleFuel.getSelectedItem().toString());
		VI.setColor(AddVehicleVehicleColor.getText().toString());
		VI.setPlateState(AddVehicleMotoristPlateState.getSelectedItem().toString());
		VI.setLicensePlate(AddVehicleVehicleLicencePlate.getText().toString());
		return VI;
	}
	void clearAllControls()
	{
		 AddVehicleVehicleName.setText("");
		 //AddVehicleVehicleType.setText("");
		 //AddVehicleVehicleYear.setText("");
		 AddVehicleVehicleMake.setText("");
		 AddVehicleVechicleModel.setText("");
		 //AddVehicleVehicleFuel.setText("");
		 AddVehicleVehicleColor.setText("");
		 AddVehicleVehicleLicencePlate.setText("");
		 //AddVehicleMotoristPlateState.setText("");
	}
}
