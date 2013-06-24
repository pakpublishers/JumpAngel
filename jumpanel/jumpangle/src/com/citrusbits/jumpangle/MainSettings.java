package com.citrusbits.jumpangle;


import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainSettings extends Activity{

	Button AccountSettings=null;
	Button VehicleSettings=null;
	Button SignOutButton=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainsettings);
		initiateControls();
		initiateListners();
		
	}

	private void initiateListners() {
		AccountSettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainSettings.this, Account_Settings.class);
				startActivity(i);
			}
		});
		VehicleSettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainSettings.this, VehicleSettings_Activity.class);
				startActivity(i);
			}
		});
		SignOutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initiateControls() {
		AccountSettings=(Button)findViewById(R.id.AccountSettings);
		VehicleSettings=(Button)findViewById(R.id.VehicleSettings);
		SignOutButton=(Button)findViewById(R.id.SignOutButton);
	}
	

}
