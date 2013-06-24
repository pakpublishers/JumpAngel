package businessclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDataSource {

	private SQLiteDatabase database=null;
	private DB dbHelper=null;
	
	public ContactsDataSource(Context cntx)
	{
		dbHelper=new DB(cntx);
	}
	public void open() throws SQLException
	{
		database=dbHelper.getWritableDatabase();
		
	}
	public void close()
	{
		dbHelper.close();
	}
	public String createContact(String ContactName,String ContactCell)
	{
		ContentValues values=new ContentValues();
		
		values.put(DB.CONTACT_TABLE_FILED_NAME, ContactName);
		values.put(DB.CONTACT_TABLE_FIELD_CELLNUMBER, ContactCell);
		
		long id=database.insert(DB.CONTACT_TABLE, null, values);
		int k=0;
		String[] Columns=new String[]{DB.CONTACT_TABLE_FILED_NAME,DB.CONTACT_TABLE_FIELD_CELLNUMBER};
		Cursor crsr=database.query(DB.CONTACT_TABLE, Columns, null, null, null, null, null);
		
		crsr.moveToFirst();
		String Name=crsr.getString(0);
		String Contactcell=crsr.getString(1);
		
		return "Contact Name: "+"Contact Cell: ";
	}
}
