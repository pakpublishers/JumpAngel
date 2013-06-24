package businessclasses;

import java.util.ArrayList;
import java.util.List;

import com.citrusbits.jumpangle.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class IncreaseAmountListAdapter extends ArrayAdapter<String> {

	Context cntx=null;
	
	ArrayList<String> data=new ArrayList<String>();
	
	public IncreaseAmountListAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<String> objects) {
		super(context, resource, textViewResourceId, objects);
		cntx=context;
		
		data=objects;
		
		
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View v=convertView;
		if(v==null)
		{
			v=LayoutInflater.from(cntx).inflate(R.layout.item, null);
		}
		TextView MyText=(TextView)v.findViewById(R.id.MyText);
		if(MyText!=null)
		{
			MyText.setText(data.get(position));
		}
		return v;
		
	}

	
	
}
