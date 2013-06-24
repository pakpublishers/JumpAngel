package businessclasses;

import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;


import com.citrusbits.jumpangle.MainActivity;
import com.citrusbits.jumpangle.R;

import com.urbanairship.push.CustomPushNotificationBuilder;

public class JumpAngelCustomNotification extends CustomPushNotificationBuilder {

	PushApplication mycontext=null;
	public static MainActivity _Act=null;
	public static Boolean OutFromMainActivity=true;
	
	public JumpAngelCustomNotification(PushApplication contx)
	{
		super();
		mycontext=contx;
	}
	
	 private void setNotifiy(String message){
	        String ns = Context.NOTIFICATION_SERVICE;
	        
	        NotificationManager mNotificationManager = (NotificationManager) mycontext.getSystemService(ns);

	        int icon = R.drawable.bluebutton;//  R.drawable.notification_icon;
	        CharSequence tickerText = "JumpAngel";
	        long when = System.currentTimeMillis();
	        Context context = mycontext.getApplicationContext();
	        CharSequence contentTitle = "JumpAngle";
	        CharSequence contentText = message;

	        Notification notification = new Notification(icon, tickerText, when);
	        Intent notificationIntent = new Intent(context, MainActivity.class);
	        
	        //notificationIntent.putExtra("hasnotification", "yes");
	        //notificationIntent.putExtra("notificationmessage", message);
	        
	        PushApplication.HasNotification=true;
	        PushApplication.NotificationString=message;
	        
	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);    
	        notification.flags = Notification.FLAG_AUTO_CANCEL;
	        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	        mNotificationManager.notify(0, notification);
	        
	    }
	
	@Override
	public Notification buildNotification(String arg0, Map<String, String> arg1) {
		// TODO Auto-generated method stub
		Log.e("zindabad","Notification build SUccessfully");
		
		if(arg1.containsKey("alert"));
		{
			Log.e("alert is",arg0);
		}
		if(OutFromMainActivity)
		{
			setNotifiy(arg0);
		}
		else
		{
			Message m=new Message();
			m.obj=arg0;
			m.what=1;
			_Act.ThisHandler.sendMessage(m);
			//_Act.showDefaultDialog("Message", arg0);
		}
		return null;
		//return super.buildNotification(arg0, arg1);
		
	}

	@Override
	public int getNextId(String arg0, Map<String, String> arg1) {
		// TODO Auto-generated method stub
		return super.getNextId(arg0, arg1);
	}
	
	

}
