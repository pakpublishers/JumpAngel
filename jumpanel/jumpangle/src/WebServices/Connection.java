package WebServices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Connection extends Thread {

	  String SOAP_Action="http://MtbciSBill.org/UpdateClaimArslan";
	  String Methoud_Name="UpdateClaimArslan";
	  String NAMESPACE="http://MtbciSBill.org/";

	Handler ReferenceHandler=null;
	
	SoapObject request=null;
	int ResponseWhat=-1;
	int ErrorWhat=-1;
	
	
	public void setSoapDetails(String Soap_Action,String MethoudName,String Namespace)
	{
		SOAP_Action=Soap_Action;
		Methoud_Name=MethoudName;
		NAMESPACE=Namespace;
		
		request =new SoapObject(NAMESPACE, Methoud_Name);
	}
	public String getNameSpace()
	{
		return NAMESPACE;
	}
	public String getMethoudName()
	{
		return Methoud_Name;
	}
	
	
	public void addSoapObject(SoapObject S_Object)
	{
		request.addSoapObject(S_Object);
	}
	public void addProperty(String PropertyName,Object PropertyValue)
	{
		request.addPropertyIfValue(PropertyName, PropertyValue);
	}
	public void EstablishConnection()
	{
		start();
	}
	public void setResponseErrorWhat(int Responsewhat, int errorwhat)
	{
		ResponseWhat=Responsewhat;
		ErrorWhat=errorwhat;
	}
	
	public Connection(Handler h)
	{
		ReferenceHandler=h;
	}
	@Override
	public void run()
	{
				
		SoapSerializationEnvelope envelop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelop.dotNet=true;
		envelop.setOutputSoapObject(request);
		HttpTransportSE transprt=new HttpTransportSE(Helper.URL, 30000);
		transprt.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		transprt.debug=true;
		try {
				
				transprt.call(SOAP_Action, envelop);
				
				Object s=envelop.getResponse();
				
				SoapObject result=(SoapObject)envelop.bodyIn;
				String data=result.toString();
				Log.e("soapresult",""+data);
				Message m=new Message();
				m.what=ResponseWhat;
					Bundle b=new Bundle();
					b.putString("message", data);
				m.setData(b);
				ReferenceHandler.sendMessage(m);
			}
		catch(java.lang.ClassCastException et)
		{
			Log.e("soaperror",""+et.toString());
			Message m=new Message();
			m.what=ErrorWhat;
				Bundle b=new Bundle();
				b.putString("message", et.getMessage());
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
