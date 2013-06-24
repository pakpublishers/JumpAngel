package com.citrusbits.jumpangle;

import businessclasses.UnlockVehicle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

public class Vehicle_Unlock_Service extends Activity {

	Button UnlockCancelButton=null;
	Button UnlockDoneButton=null;
	RadioGroup ApplyChoiceGroup=null;
	
	UnlockVehicle UV=new UnlockVehicle();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_unlock_service_view);
		initiateControls();
		initiateListners();
	}

	private void initiateListners() {
		UnlockCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		UnlockDoneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent();
				i.putExtra("unlockvehicle", UV);
				i.putExtra("act", "UV");
				setResult(RESULT_OK, i);
				finish();
				
			}
		});
		ApplyChoiceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.UnlockApplytoVehicleOnly)
				{
					UV.setApplytoVahicleOnly(true);
				}
				else
				{
					UV.setApplytoVahicleOnly(false);
				}
			}
		});
	}

	private void initiateControls() {
		UnlockCancelButton=(Button)findViewById(R.id.UnlockCancelButton);
		UnlockDoneButton=(Button)findViewById(R.id.UnlockDoneButton);
		ApplyChoiceGroup=(RadioGroup)findViewById(R.id.ApplyChoiceGroup);
	}

}
