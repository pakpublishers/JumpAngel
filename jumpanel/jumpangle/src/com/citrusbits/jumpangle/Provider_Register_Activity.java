package com.citrusbits.jumpangle;

import java.util.ArrayList;

import org.w3c.dom.Text;

import businessclasses.Motorist;
import businessclasses.Provider;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Provider_Register_Activity extends Activity {

	
	
	EditText FirstName=null;
	EditText LastName=null;
	EditText CellNumber=null;
	EditText EmailAddress=null;
	EditText Pasword=null;
	Button CancelButton=null;
	Button NextButton=null;
	Spinner FleetVehicleCount=null;
	Spinner GeneralServiceDutySpinner=null;
	EditText ProviderWePayID=null;
	
	ToggleButton CommercialFleetInsuranceToggle=null;
	ToggleButton ProfessionallyPermitedFleet=null;
	ToggleButton CommerciallyLicesencedDriver=null;
	ToggleButton NATACertifiedDriver=null;
	
	public static int TotalFleetVehicles=0;
	public static int CurrentFleetVehicle=1;
	
	Provider ProviderUser=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider_registration_view);
		initiateControls();
		initiateListners();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initiateListners() {
		
		CancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		NextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(Provider_Register_Activity.this);//getSharedPreferences("com.citrusbits.jumpangle", Context.MODE_PRIVATE);
				SharedPreferences.Editor myediEditor=sp.edit();
				myediEditor.clear();
				myediEditor.commit();
				
				Intent i=new Intent(Provider_Register_Activity.this, Provider_Add_Vehicle_Activity.class);
				i.putExtra("title", "Provider Add Vehicle\r\nVehicle "+CurrentFleetVehicle+" out of "+TotalFleetVehicles);
				i.putExtra("Provider", getProvider());
				startActivityForResult(i, 0);
			}
		});
		
		FleetVehicleCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				TotalFleetVehicles=pos+1;
				Log.e("selected	",""+TotalFleetVehicles);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				TotalFleetVehicles=0;
				
			}
			
			
		});
		
	}

	private void initiateControls() {
		
		FirstName= (EditText)findViewById(R.id.Emailaddress);
		LastName= (EditText)findViewById(R.id.LoginPasword);
		CellNumber= (EditText)findViewById(R.id.CellNumber);
		EmailAddress= (EditText)findViewById(R.id.EmailAddress);
		Pasword= (EditText)findViewById(R.id.Pasword);
		
		FleetVehicleCount=(Spinner)findViewById(R.id.FleetVehicleCount);
		GeneralServiceDutySpinner=(Spinner)findViewById(R.id.GeneralServiceDutySpinner);
		
		CancelButton=(Button)findViewById(R.id.CancelButton);
		NextButton=(Button)findViewById(R.id.NextButton);
		
		CommercialFleetInsuranceToggle=(ToggleButton)findViewById(R.id.CommercialFleetInsuranceToggle);
		ProfessionallyPermitedFleet=(ToggleButton)findViewById(R.id.ProfessionallyPermitedFleet);
		CommerciallyLicesencedDriver=(ToggleButton)findViewById(R.id.CommerciallyLicesencedDriver);
		NATACertifiedDriver=(ToggleButton)findViewById(R.id.NATACertifiedDriver);
		
		ProviderWePayID=(EditText)findViewById(R.id.ProviderWePayID);
	}
	public Provider getProvider()
	{
		Provider p=new Provider();
		p.setFirstName(FirstName.getText().toString());
		p.setLastName(LastName.getText().toString());
		p.setCellNumber(CellNumber.getText().toString());
		p.setEmailAddress(EmailAddress.getText().toString());
		p.setPasword(Pasword.getText().toString());
		p.setFleetVehicleCount(TotalFleetVehicles);
		p.setGeneralServiceDuty(getResources().getStringArray(R.array.GeneralServiceDutyArray)[GeneralServiceDutySpinner.getSelectedItemPosition()]);
		p.setCommercialFleetInsurance(CommercialFleetInsuranceToggle.isChecked());
		p.setProfessionallyPermittedFleet(ProfessionallyPermitedFleet.isChecked());
		p.setCommerciallyLicensedDriver(CommerciallyLicesencedDriver.isChecked());
		p.setNATACertifiedDrivers(NATACertifiedDriver.isChecked());
		p.setWePayID(ProviderWePayID.getText().toString());
		return p;
		
	}
	
}
