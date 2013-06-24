package businessclasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper{

	static String DATABASE_NAME="JUMPCONTACTSDB";
	static int DATABASE_VERSION=2;
	
	static String CONTACT_TABLE="MyContacts";
	static String CONTACT_TABLE_FILED_NAME="name";
	static String CONTACT_TABLE_FIELD_CELLNUMBER="cellnumber";
	static String CONTACT_TABLE_CREATE="create table "+CONTACT_TABLE+" ("+CONTACT_TABLE_FILED_NAME+" TEXT,"+
	CONTACT_TABLE_FIELD_CELLNUMBER+" TEXT);"
			;
	
	public DB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CONTACT_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
