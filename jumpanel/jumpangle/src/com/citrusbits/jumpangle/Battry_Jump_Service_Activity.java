package com.citrusbits.jumpangle;

import businessclasses.BattryJump;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Battry_Jump_Service_Activity extends Activity {

	Button BJCancelButton=null;
	Button BJDoneButton=null;
	CheckBox BJLightDutyCheckBox=null;
	CheckBox BJMediumDutyCheckBox=null;
	CheckBox BJHeavyDutyCheckBox=null;
	RadioGroup BJApplyShoice=null;
	
	TextView TitleTV=null;
	
	BattryJump BJ=new BattryJump();
	
	String ActivitytoFollow="";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battry_jump_service_view);
		initiateControls();
		initiateListners();
		
		
	}

	private void initiateListners() {
		
		BJDoneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent();
				i.putExtra("BattryJump", BJ);
				i.putExtra("act", ActivitytoFollow);
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
		BJLightDutyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				BJ.setLightDuty(isChecked);
				
			}
		});
		BJMediumDutyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				BJ.setMediumDuty(isChecked);
				
			}
		});
		BJHeavyDutyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				BJ.setHeavyDuty(isChecked);
				
			}
		});
		BJApplyShoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.BJVehicleOnly)
				{
					BJ.setApplyVehicleOnly(true);
				}
				else 
				{
					BJ.setApplyVehicleOnly(false);
				}
				
			}
		});
		
	}

	private void initiateControls() {
		BJCancelButton=(Button)findViewById(R.id.BJCancelButton);
		BJDoneButton=(Button)findViewById(R.id.BJDoneButton);
		
		 BJLightDutyCheckBox=(CheckBox)findViewById(R.id.GasolineVehicleFD);
		 BJMediumDutyCheckBox=(CheckBox)findViewById(R.id.DeiselVehicle);
		 BJHeavyDutyCheckBox=(CheckBox)findViewById(R.id.CNGVehicleCB);
		 
		 BJApplyShoice=(RadioGroup)findViewById(R.id.BJApplyShoice);
		 
		 TitleTV=(TextView)findViewById(R.id.title);
		 
		String title= getIntent().getStringExtra("title");
		
		TitleTV.setText(title);
	    ActivitytoFollow=getIntent().getStringExtra("act");
		
	}

	
	
}
