package com.citrusbits.jumpangle;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Registration_Choice_Activity extends Activity{
	
	Button MotoristRegistrationButton=null;
	Button ProvoderRegistrationButton=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_choice_selection_view);
		initiateControls();
		initiateListners();
	}
	private void initiateControls()
	{
		MotoristRegistrationButton=(Button)findViewById(R.id.MotoristRegistrationButton);
		ProvoderRegistrationButton=(Button)findViewById(R.id.ProvoderRegistrationButton);
		
	}
	private void initiateListners()
	{
		MotoristRegistrationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent MotoristRegistrationActivityIntent=new Intent(Registration_Choice_Activity.this, Motorist_Register_Activity.class);
				finish();
				
				startActivity(MotoristRegistrationActivityIntent);
			}
		});
		ProvoderRegistrationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent MotoristRegistrationActivityIntent=new Intent(Registration_Choice_Activity.this, Provider_New_Register_Activity.class);
				finish();
				startActivity(MotoristRegistrationActivityIntent);
			}
		});
	}
	
	

}
