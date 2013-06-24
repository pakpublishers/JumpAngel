package businessclasses;

import com.citrusbits.jumpangle.MainActivity;
import com.citrusbits.jumpangle.R;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.UrbanAirshipProvider;
import com.urbanairship.push.CustomPushNotificationBuilder;
import com.urbanairship.push.PushManager;


import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class PushApplication extends Application{
	public static String APID=null;
	public static Boolean HasNotification=false;
	public static String NotificationString=null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		UAirship.takeOff(this, AirshipConfigOptions.loadDefaultOptions(getBaseContext()));
		PushManager.enablePush();
		
		JumpAngelCustomNotification cb=new JumpAngelCustomNotification(PushApplication.this);
		cb.layout=R.layout.jumpangel_notification;
		cb.layoutIconDrawableId=R.drawable.bluebutton;
		cb.layoutIconId=R.id.icon;
		cb.layoutSubjectId=R.id.subject;
		cb.layoutMessageId=R.id.message;
		
		cb.constantNotificationId=100;
		PushManager.shared().setNotificationBuilder(cb);
		
		String apid= PushManager.shared().getAPID();
		if(apid==null)
		{
			new Thread()
			{
				@Override
				public void run()
				{
					while(PushApplication.APID==null)
						{
						PushApplication.APID= PushManager.shared().getAPID();
						Log.e("Generating Apid","");
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					Log.e("Apid Generated","Successfully");
				}
			}.start();
		}
		
		//while(apid==null)apid= PushManager.shared().getAPID();
		APID=apid;
		if(APID!=null)
			Log.e("myapidasad",APID);
	}

}
