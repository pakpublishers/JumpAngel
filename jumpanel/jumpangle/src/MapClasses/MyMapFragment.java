package MapClasses;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends SupportMapFragment {

	private List<MarkerOptions> mMarkers;
	
	
	public static MyMapFragment create(GoogleMapOptions options, ArrayList<MarkerOptions> markers) {
        MyMapFragment fragment = new MyMapFragment();
        
        Bundle args = new Bundle();
        args.putParcelable("MapOptions", options); //obtained by decompiling google-play-services.jar
        args.putParcelableArrayList("markers", markers);
        fragment.setArguments(args);

        return fragment;
    }
	
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		ArrayList<Parcelable> list = getArguments().getParcelableArrayList("markers");
        mMarkers = new ArrayList<MarkerOptions>(list.size());
        for (Parcelable parcelable : list) {
            mMarkers.add((MarkerOptions) parcelable);
        }
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
        GoogleMap mMap = super.getMap();
        //add the markers
        if (mMap != null) {
            for (MarkerOptions marker : mMarkers) {
                mMap.addMarker(marker);
            }
        }
		
	}

	
	
}
