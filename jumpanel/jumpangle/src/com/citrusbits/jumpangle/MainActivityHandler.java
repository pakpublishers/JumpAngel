package com.citrusbits.jumpangle;

import android.os.Handler;
import android.os.Message;

public class MainActivityHandler extends Handler{

	MainActivity MainActivityObj=null;
	
	public MainActivityHandler(MainActivity mainAct)
	{
		super();
		MainActivityObj=mainAct;
		
	}
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
	}

}
