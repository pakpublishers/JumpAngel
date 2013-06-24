package WebServices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WebConnection extends Thread {

	  String SOAP_Action="";
	  String Methoud_Name="";
	  String NAMESPACE="";
	
	int ErrorWhat=-1;
	int SuccessWhat=-1;
	
	Handler ReferenceHandler=null;
	SoapObject request=null;
	
	public void addProperty(String PropertyName,Object PropertyValue)
	{
		request.addPropertyIfValue(PropertyName, PropertyValue);
	}
	
	public void setHeaderDetails(String SoapAction,String MethoudName,String NameSpace)
	{
		SOAP_Action=SoapAction;
		Methoud_Name=MethoudName;
		NAMESPACE=NameSpace;
		request =new SoapObject(NAMESPACE, Methoud_Name);
	}
	
	public void setWhats(int Error,int Success)
	{
		ErrorWhat=Error;
		SuccessWhat=Success;
	}
	public WebConnection(Handler h)
	{
		ReferenceHandler=h;
		
	}
	public void Start()
	{
		start();
	}
	
	
	@Override
	public void run()
	{
		SoapSerializationEnvelope envelop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelop.dotNet=true;
		envelop.setOutputSoapObject(request);
		HttpTransportSE transprt=new HttpTransportSE(Helper.URL, 30000);
		
		//new HttpTransportSE(Helper.URL);
		
		
		
		transprt.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		transprt.debug=true;
		try {
				
				transprt.call(SOAP_Action, envelop);
				Object s=envelop.getResponse();
				
				SoapObject result=(SoapObject)envelop.bodyIn;
				String data=result.toString();
				
				Message m=new Message();
				m.obj=(Object)result;
				m.what=SuccessWhat;
					Bundle b=new Bundle();
					b.putString("message", data);
				m.setData(b);
				ReferenceHandler.sendMessage(m);
			}
	
		catch(java.lang.ClassCastException ej)
		{
			Log.e("soaperror",""+ej.toString());
			Message m=new Message();
			m.what=ErrorWhat;
				Bundle b=new Bundle();
				b.putString("message", ej.getMessage());
			m.setData(b);
			ReferenceHandler.sendMessage(m);
		}
		catch(Exception k)
		{
			Log.e("soaperror",""+k.toString());
			
			Message m=new Message();
			m.what=ErrorWhat;
				Bundle b=new Bundle();
				b.putString("message", k.getMessage());
			m.setData(b);
			ReferenceHandler.sendMessage(m);
		}
	
		
	}
}
