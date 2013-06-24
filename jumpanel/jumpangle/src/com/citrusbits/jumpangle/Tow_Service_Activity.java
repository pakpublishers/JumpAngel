package com.citrusbits.jumpangle;

import businessclasses.BattryJump;
import businessclasses.TowService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

public class Tow_Service_Activity extends Activity {

	Button BJCancelButton=null;
	Button BJDoneButton=null;
	CheckBox TowBoom=null;
	CheckBox TowFlatBed=null;
	CheckBox TowSling=null;
	CheckBox TowWheelLift=null;
	CheckBox TowWinch=null;
	
	RadioGroup BJApplyShoice=null;
	
	TowService BJ=new TowService();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tow_service_view);
		initiateControls();
		initiateListners();
		
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			if(data.getStringExtra("act").equals("TowBoom"))
			{
				BJ.setBoom((BattryJump) data.getSerializableExtra("BattryJump"));
				//Log.e("my result is : ",BJ.getBoom().getHeavyDuty().toString());
				
				//Log.e("lightduty is ",""+BJ.getLightDuty().toString());
			}
			if(data.getStringExtra("act").equals("flatbed"))
			{
				BJ.setFlat_Bed((BattryJump) data.getSerializableExtra("BattryJump"));
				//Log.e("my result is : ",BJ.getBoom().getHeavyDuty().toString());
				
				//Log.e("lightduty is ",""+BJ.getLightDuty().toString());
			}
			if(data.getStringExtra("act").equals("sling"))
			{
				BJ.setSling((BattryJump) data.getSerializableExtra("BattryJump"));
			//	Log.e("my result is : ",BJ.getBoom().getHeavyDuty().toString());
				
				//Log.e("lightduty is ",""+BJ.getLightDuty().toString());
			}
			if(data.getStringExtra("act").equals("wheellift"))
			{
				BJ.setWheelLift((BattryJump) data.getSerializableExtra("BattryJump"));
				//Log.e("my result is : ",BJ.getBoom().getHeavyDuty().toString());
				
				//Log.e("lightduty is ",""+BJ.getLightDuty().toString());
			}
			if(data.getStringExtra("act").equals("winch"))
			{
				BJ.setWinch((BattryJump) data.getSerializableExtra("BattryJump"));
				//Log.e("my result is : ",BJ.getBoom().getHeavyDuty().toString());
				
				//Log.e("lightduty is ",""+BJ.getLightDuty().toString());
			}
		}
		
	}



	private void initiateListners() {
		
		BJDoneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent();
				i.putExtra("tow", BJ);
				i.putExtra("act", "TOW");
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
		TowBoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//BJ.setBoom(isChecked);
				if(isChecked)
				{
					Intent BoomIntent=new Intent(Tow_Service_Activity.this, Battry_Jump_Service_Activity.class);
					BoomIntent.putExtra("title", "Tow Duty Detail\r\n Vehicle Name  Boom");
					BoomIntent.putExtra("act", "TowBoom");
					startActivityForResult(BoomIntent, 0);
				}
				else
				{
					BJ.setBoom(null);
				}
				
				BattryJump bmp= new BattryJump();
				bmp.setHeavyDuty(true);
				bmp.setLightDuty(true);
				
				BJ.setBoom(bmp);
				
			}
		});
		TowFlatBed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent BoomIntent=new Intent(Tow_Service_Activity.this, Battry_Jump_Service_Activity.class);
					BoomIntent.putExtra("title", "Tow Duty Detail\r\n Vehicle Name  flatbed");
					BoomIntent.putExtra("act", "flatbed");
					startActivityForResult(BoomIntent, 0);
				}
				else
				{
					BJ.setFlat_Bed(null);
				}
				
			}
		});
		TowSling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent BoomIntent=new Intent(Tow_Service_Activity.this, Battry_Jump_Service_Activity.class);
					BoomIntent.putExtra("title", "Tow Duty Detail\r\n Vehicle Name  sling");
					BoomIntent.putExtra("act", "sling");
					startActivityForResult(BoomIntent, 0);
				}
				else
				{
					BJ.setSling(null);
				}
				
			}
		});
		TowWheelLift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent BoomIntent=new Intent(Tow_Service_Activity.this, Battry_Jump_Service_Activity.class);
					BoomIntent.putExtra("title", "Tow Duty Detail\r\n Vehicle Name  wheellift");
					BoomIntent.putExtra("act", "wheellift");
					startActivityForResult(BoomIntent, 0);
				}
				else
				{
					BJ.setWheelLift(null);
				}
				
			}
		});
		TowWinch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					Intent BoomIntent=new Intent(Tow_Service_Activity.this, Battry_Jump_Service_Activity.class);
					BoomIntent.putExtra("title", "Tow Duty Detail\r\n Vehicle Name  winch");
					BoomIntent.putExtra("act", "winch");
					startActivityForResult(BoomIntent, 0);
				}
				else
				{
					BJ.setWinch(null);
				}
				
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
		
		 TowBoom=(CheckBox)findViewById(R.id.Tow_Boom);
		 TowFlatBed=(CheckBox)findViewById(R.id.Tow_Flatbed);
		 TowSling=(CheckBox)findViewById(R.id.Tow_Sling);
		 TowWheelLift=(CheckBox)findViewById(R.id.TowWheelLift);
		 TowWinch=(CheckBox)findViewById(R.id.TowWinch);
		 
		 BJApplyShoice=(RadioGroup)findViewById(R.id.BJApplyShoice);
		
	}

	
	
}
