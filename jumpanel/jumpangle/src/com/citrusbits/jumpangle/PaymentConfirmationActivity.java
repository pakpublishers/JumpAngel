package com.citrusbits.jumpangle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PaymentConfirmationActivity extends Activity{

	TextView Payment_Confirmation_Requesting_TV=null;
	TextView Payment_Confirmation_For_TV=null;
	TextView Payment_Confirmation_From_TV=null;
	TextView Payment_Confirmation_ETA_TV=null;
	TextView Payment_Confirmation_Distance_TV=null;
	TextView Payment_Confirmation_BidAcceptanceTime_TV=null;
	TextView Payment_Confirmation_MaxWaitTime_TV=null;
	TextView Payment_Confirmation_BidAmount_TV=null;
	
	Button ApprovePaymentCancelButton=null;
	Button ApprovePaymentOKButton=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.paymentconfirmationview);
		
		 Payment_Confirmation_Requesting_TV=(TextView)findViewById(R.id.Payment_Confirmation_Requesting_TV);
		 Payment_Confirmation_For_TV=(TextView)findViewById(R.id.Payment_Confirmation_For_TV);
		 Payment_Confirmation_From_TV=(TextView)findViewById(R.id.Payment_Confirmation_From_TV);
		 Payment_Confirmation_ETA_TV=(TextView)findViewById(R.id.Payment_Confirmation_ETA_TV);
		 Payment_Confirmation_Distance_TV=(TextView)findViewById(R.id.Payment_Confirmation_Distance_TV);
		 Payment_Confirmation_BidAcceptanceTime_TV=(TextView)findViewById(R.id.Payment_Confirmation_BidAcceptanceTime_TV);
		 Payment_Confirmation_MaxWaitTime_TV=(TextView)findViewById(R.id.Payment_Confirmation_MaxWaitTime_TV);
		 Payment_Confirmation_BidAmount_TV=(TextView)findViewById(R.id.Payment_Confirmation_BidAmount_TV);
		
		 ApprovePaymentCancelButton=(Button)findViewById(R.id.ApprovePaymentCancelButton);
		 ApprovePaymentOKButton=(Button)findViewById(R.id.ApprovePaymentOKButton);
		 
		 
		 
		 Bundle b=getIntent().getExtras();
		 
		 String service=getResources().getStringArray(R.array.Services_Array)[Integer.parseInt(b.getString("Service"))];
		 
		 Payment_Confirmation_Requesting_TV.setText(service);
		 Payment_Confirmation_For_TV.setText(b.getString("for"));
		 Payment_Confirmation_From_TV.setText(b.getString("from"));
		 Payment_Confirmation_ETA_TV.setText(b.getString("eta"));
		 Payment_Confirmation_Distance_TV.setText(b.getString("distance"));
		 
		 
		 
		 String m=b.getString("bidacceptancetime");
		 
		 int mints=Integer.parseInt(m);
		 int hr=mints/60;
		 int mnt=mints%60;
			
		 String bat=(hr+" hr "+((mnt<10)?("0"+mnt):mnt)+" min");
		 
		 
		 String ms=b.getString("maxwaittime");
		 
		 int mintss=Integer.parseInt(ms);
		 int hrs=mintss/60;
		 int mnts=mintss%60;
		 String mwt=(hrs+" hr "+((mnts<10)?("0"+mnts):mnts)+" min");
		 
		 Payment_Confirmation_BidAcceptanceTime_TV.setText(bat);
		 
		 Payment_Confirmation_MaxWaitTime_TV.setText(mwt);
		 
		 
		 
		 Payment_Confirmation_BidAmount_TV.setText(b.getString("bidamount"));
		 
		 ApprovePaymentOKButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent();
				i.putExtra("Confirmation", "Confirmed");
				setResult(RESULT_OK, i);
				finish();
			}
		});
		 
		 ApprovePaymentCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		 
		 
		
	}
	
	

}
