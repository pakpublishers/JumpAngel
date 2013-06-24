package businessclasses;

import java.util.ArrayList;

import com.citrusbits.jumpangle.EventHistory_Activity;
import com.citrusbits.jumpangle.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class EventHistoryAdapter extends ArrayAdapter<EventHistoryDetails>{

	ArrayList<EventHistoryDetails> data=new ArrayList<EventHistoryDetails>();
	EventHistory_Activity _Act=null;
	public EventHistoryAdapter(Context context, int textViewResourceId,ArrayList<EventHistoryDetails> obj,EventHistory_Activity act) {
		super(context, textViewResourceId,obj);
		data=obj;
		
		_Act=act;
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v=convertView;
		if(v==null)
		{
			v=LayoutInflater.from(_Act).inflate(R.layout.eventhistorylisttiem, null);
		}
		
		RatingBar ratingBar1=(RatingBar)v.findViewById(R.id.ratingBar1);
		TextView BidAmount=(TextView)v.findViewById(R.id.BidAmount);
		TextView CompletionTime=(TextView)v.findViewById(R.id.CompletionTime);
		TextView otherpartyangel=(TextView)v.findViewById(R.id.otherpartyangel);
		TextView service=(TextView)v.findViewById(R.id.service);
		TextView TransactionID=(TextView)v.findViewById(R.id.TransactionID);
		
		
		if(ratingBar1!=null)
		{
			ratingBar1.setRating(Float.parseFloat(data.get(position).rating));
			ratingBar1.setEnabled(false);
		}
		if(BidAmount!=null)
		{
			BidAmount.setText(data.get(position).amount);
		}
		if(CompletionTime!=null)
		{
			CompletionTime.setText(data.get(position).senting_time);
		}
		if(otherpartyangel!=null)
		{
			otherpartyangel.setText(data.get(position).angel_name);
		}
		if(service!=null)
		{
			service.setText(data.get(position).service);
		}
		if(TransactionID!=null)
		{
			TransactionID.setText(data.get(position).transaction_id);
		}
		
		return v;
	}
	
	

}
