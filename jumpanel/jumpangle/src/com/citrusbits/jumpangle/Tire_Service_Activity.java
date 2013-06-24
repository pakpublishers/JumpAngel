package com.citrusbits.jumpangle;

import businessclasses.BattryJump;
import businessclasses.FuelDeliveryJump;
import businessclasses.TireService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Tire_Service_Activity extends Activity {

	Button BJCancelButton=null;
	Button BJDoneButton=null;
	CheckBox TireChanges=null;
	CheckBox MobileTireRepair=null;
	
	RadioGroup BJApplyShoice=null;
	
	TextView TitleTV=null;
	
	TireService TS=new TireService();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tire_service_detail_view);
		initiateControls();
		initiateListners();
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==RESULT_OK)
		{
			if(data.getStringExtra("act").equals("TireChange"))
			{
				TS.setTireChanges((BattryJump) data.getSerializableExtra("BattryJump"));
			//	Log.e("lightduty is ",""+BJ.getLightDuty().toString());
			}
			else if(data.getStringExtra("act").equals("MobileTireRepair"))
			{
				TS.setMobileTireRepair((BattryJump) data.getSerializableExtra("BattryJump"));
				//Log.e("Mobile tire Repair : ",TS.getMobileTireRepair().getHeavyDuty().toString());
			//	Log.e("gasoline is ",""+FD.getGasoline_Pertol().toString());
			}
		}
		
	}

	private void initiateListners() {
		
		BJDoneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent();
				i.putExtra("TireService", TS);
				i.putExtra("act", "TS");
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
		TireChanges.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent TireIntent=new Intent(Tire_Service_Activity.this, Battry_Jump_Service_Activity.class);
					TireIntent.putExtra("title", "Tire Duty Detail\r\n Vehicle Name  Boom");
					TireIntent.putExtra("act", "TireChange");
					startActivityForResult(TireIntent, 0);
				}
				else
				{
					TS.setTireChanges(null);
				}
				
			}
		});
		MobileTireRepair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent TireIntent=new Intent(Tire_Service_Activity.this, Battry_Jump_Service_Activity.class);
					TireIntent.putExtra("title", "Tire Duty Detail\r\n Vehicle Name  Boom");
					TireIntent.putExtra("act", "MobileTireRepair");
					startActivityForResult(TireIntent, 0);
				}
				else
				{
					TS.setMobileTireRepair(null);
				}
			}
		});
		
		BJApplyShoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.BJVehicleOnly)
				{
					//BJ.setApplyVehicleOnly(true);
				}
				else 
				{
					//BJ.setApplyVehicleOnly(false);
				}
				
			}
		});
		
	}

	private void initiateControls() {
		BJCancelButton=(Button)findViewById(R.id.BJCancelButton);
		BJDoneButton=(Button)findViewById(R.id.BJDoneButton);
		
		TireChanges=(CheckBox)findViewById(R.id.Tire_TireChanges);
		MobileTireRepair=(CheckBox)findViewById(R.id.Tire_MobileTireRepair);
		 
		 
		 BJApplyShoice=(RadioGroup)findViewById(R.id.BJApplyShoice);
		 
		 TitleTV=(TextView)findViewById(R.id.titleTire);
		 
		String title= getIntent().getStringExtra("title");
		
		TitleTV.setText(title);
	    
		
	}

	
	
}
