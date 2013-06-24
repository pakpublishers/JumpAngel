package com.citrusbits.jumpangle;

import businessclasses.Vehicle_Diagnosis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Vehicle_Diagnosis_Details_Activity extends Activity {

	CheckBox GasolineVehicle=null;  
	CheckBox DeiselVehicle=null;
	CheckBox CNGVehicleCB=null;
	CheckBox HybridVehicle=null;
	RadioGroup BJApplyShoice=null;
	Button BJCancelButton=null;
	Button BJDoneButton=null;
	TextView TitleTV=null;
	
	Vehicle_Diagnosis VD=new Vehicle_Diagnosis();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_diagnosis_detail_view); 
		initiateControls();
		initiateListners();
	}

	private void initiateListners() {
		
		BJDoneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent();
				i.putExtra("vehicle_diagnosis", VD);
				i.putExtra("act", "VD");
				setResult(RESULT_OK, i);
				finish();
			}
		});
		BJCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		BJApplyShoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.BJVehicleOnly)
				{
					VD.setApplyVehicleOnly(true);
				}
				else 
				{
					VD.setApplyVehicleOnly(false);
				}
				
			}
		});
		GasolineVehicle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					VD.setGasolineVehicles(isChecked);
			}
		});
		
		DeiselVehicle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					VD.setDesielVehicles(isChecked);
			}
		});
		CNGVehicleCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					VD.setCNGVehicles(isChecked);
			}
		});
		HybridVehicle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					VD.setHybridVehicles(isChecked);
			}
		});
	}

	private void initiateControls() {
		BJCancelButton=(Button)findViewById(R.id.BJCancelButton);
		BJDoneButton=(Button)findViewById(R.id.BJDoneButton);
		
		
		GasolineVehicle=(CheckBox)findViewById(R.id.GasolineVehicleFD);
		DeiselVehicle=(CheckBox)findViewById(R.id.DeiselVehicle);
		CNGVehicleCB=(CheckBox)findViewById(R.id.CNGVehicleCB);
		HybridVehicle=(CheckBox)findViewById(R.id.HybridVehicle);
		
		 BJApplyShoice=(RadioGroup)findViewById(R.id.BJApplyShoice);
		 
		 TitleTV=(TextView)findViewById(R.id.title);
		 
		String title= getIntent().getStringExtra("title");
		
		TitleTV.setText(title);
		
	}

	
	
	
	
}
