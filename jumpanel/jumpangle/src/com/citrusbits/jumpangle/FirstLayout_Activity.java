package com.citrusbits.jumpangle;


import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class FirstLayout_Activity extends Activity{

	LinearLayout LoginButton=null;
	LinearLayout RegistrationButton=null;
	LinearLayout dots=null;
	ViewFlipper vf=null;
	
//	HorizontalScrollView horizontalScrollView1=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstlayout);
		
		initiateControsl();
		initiateListners();
		
		
		
	}

	public SharedPreferences getThisSharedPreferences() {
		
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	private void initiateControsl() {
		// TODO Auto-generated method stub
		vf=(ViewFlipper)findViewById(R.id.viewFlipper1);
		dots=(LinearLayout)findViewById(R.id.dots);
		LoginButton=(LinearLayout)findViewById(R.id.LoginButton);
		RegistrationButton=(LinearLayout)findViewById(R.id.RegistrationButton);
		addChild(0);
		//horizontalScrollView1=(HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(getThisSharedPreferences().contains("currentLogedUser"))
		{
			Intent i=new Intent(FirstLayout_Activity.this, MainActivity.class);
			startActivity(i);
		}
		
	}

	private void initiateListners() {
		
		
		
		vf.setOnTouchListener(new View.OnTouchListener() {
			float previoursx=0;
			Boolean draged=false;
			int index=0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					previoursx=event.getX();
				}
				if(event.getAction()==MotionEvent.ACTION_MOVE)
				{
					
					draged=true;
				}
				
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					if(draged)
					{
						if(previoursx>event.getX())
						{
							//Animation in=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidefiltermenuleftf);
							//in.setInterpolator(new BounceInterpolator());
							//Animation out=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slidemenuleftf);
							//out.setInterpolator(outToLeftAnimation());
							vf.setInAnimation(inFromRightAnimation());
							vf.setOutAnimation(outToLeftAnimation());
							
				            
							vf.showNext();
							index++;
						}
						else
						{
							//Animation in=AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
							//in.setInterpolator(new BounceInterpolator());
							//Animation out=AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_out_right);
							//out.setInterpolator(null);
							vf.setInAnimation(inFromLeftAnimation());
							vf.setOutAnimation(outToRightAnimation());
							
							index--;
							vf.showPrevious();
						}
						addChild(vf.getDisplayedChild());
						
						
						previoursx=2;
						draged=false;
					}
				}
				return true;
			}
		});
		
		//vf.canScrollHorizontally(ViewFlipper.SCROLLBAR_POSITION_DEFAULT);
		// TODO Auto-generated method stub
		
//		horizontalScrollView1.setOnTouchListener(new View.OnTouchListener() {
//			// total are 5
//			int currentindex=0;
//			float previousX=0;
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if(event.getAction()==MotionEvent.ACTION_MOVE)
//				{
//					Log.e(previousX+","+event.getRawX(),"Scroling");
//					//
//					if(previousX<event.getRawX())
//					{
//						horizontalScrollView1.scrollTo(horizontalScrollView1.getScrollX()-5, horizontalScrollView1.getScrollY());
//					}
//					else
//					{
//						horizontalScrollView1.scrollTo(horizontalScrollView1.getScrollX()+5, horizontalScrollView1.getScrollY());
//					}
//					previousX=event.getRawX();
//					
//				}
//				if(event.getAction()==MotionEvent.ACTION_UP)
//				{
//					LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams)horizontalScrollView1.getChildAt(1).getLayoutParams();
//					Log.e("left margin",""+lp.leftMargin);
//				}
//				return true;
//			}
//		});
		
		
		LoginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(FirstLayout_Activity.this, Login_Activity.class);
				startActivityForResult(i, 0);
			}
		});
		RegistrationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(FirstLayout_Activity.this, Registration_Choice_Activity.class);
				startActivityForResult(i, 0);
			}
		});
	}

	public void addChild(int index)
	{
		dots.removeAllViews();
		Log.e("Clearing views","all views");
		for(int i=0; i<vf.getChildCount(); i++)
		{
			LinearLayout dot=new LinearLayout(getBaseContext());
			LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.leftMargin=3* (int)getResources().getDisplayMetrics().density;
			lp.rightMargin=3*(int)getResources().getDisplayMetrics().density;
			dot.setLayoutParams(lp);
			if(i==index)
			{
				dot.setBackgroundResource(R.drawable.reddot);
			}
			else
			{
				dot.setBackgroundResource(R.drawable.blackdot);
			}
			
			dots.addView(dot);
			Log.e("Adding View",""+i);
			
		}
		Log.e("index is",""+index);
		
	}
	
	
	 private Animation outToLeftAnimation() {
	        // Animation outtoLeft = new TranslateAnimation(
	        /*
	         * Animation outtoLeft = new
	         * ScaleAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
	         * Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
	         * 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	         */
	        Animation outtoLeft = AnimationUtils.loadAnimation(getBaseContext(), R.anim.outleft);
	        //outtoLeft.setInterpolator(new AccelerateInterpolator());
	        //outtoLeft.setDuration(time-80);
	        return outtoLeft;
	    }

	    private Animation inFromLeftAnimation() {
	        // Animation inFromLeft = new TranslateAnimation(
	        /*
	         * Animation inFromLeft = new
	         * ScaleAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
	         * Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
	         * 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	         */
	        Animation inFromLeft = AnimationUtils.loadAnimation(getBaseContext(), R.anim.inleft);
	        //inFromLeft.setDuration(time);
	        //inFromLeft.setInterpolator(new AccelerateInterpolator());
	        return inFromLeft;
	    }

	    private Animation outToRightAnimation() {
	        // Animation outtoRight = new TranslateAnimation(
	        /*
	         * Animation outtoRight = new
	         * ScaleAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
	         * Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
	         * 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	         */
	        Animation outtoRight = AnimationUtils.loadAnimation(getBaseContext(), R.anim.outright);
	        //outtoRight.setDuration(time-80);
	        //outtoRight.setInterpolator(new AccelerateInterpolator());
	        return outtoRight;
	    }
	    private Animation inFromRightAnimation() {

	    	
	        Animation inFromRight = AnimationUtils.loadAnimation(getBaseContext(), R.anim.inright);
	                
	        
	        //inFromRight.setDuration(time);
	        //inFromRight.setInterpolator(new AccelerateInterpolator());
	        return inFromRight;
	    }
	    int time=150;
}
