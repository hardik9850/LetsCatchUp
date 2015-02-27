package com.android.letscatchup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class deleteContect extends Activity implements OnItemClickListener
{
	
	SimpleAdapter adapter = null;
	ListView list = null;
	HashMap<String,String> temp;
	ArrayList<HashMap<String, String>> data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfriends);
		data = new ArrayList<HashMap<String,String>>();
		getContactNumbers(this);
		adapter = new SimpleAdapter(getApplicationContext(),data, android.R.layout.simple_list_item_2,new String[]{ "Name","Phone"},new int[]{android.R.id.text1,android.R.id.text2});
		list = (ListView)findViewById(R.id.listView1);
		
		list.setAdapter(adapter);
		registerForContextMenu(list);
	}
	
	@SuppressWarnings("unchecked")
    public  void getContactNumbers(Context context)
		{
		try
		{
			ArrayList<String>a1 = null;
           String contactNumber2 = null;
           int contactNumberType = Phone.TYPE_MOBILE;
           String nameOfContact = null;
           a1=new ArrayList<String>();
           //if (contactNumber.length() <= 0) {
               ContentResolver cr = context.getContentResolver();
               Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
               if (cur.getCount() > 0)
               	{
                   while (cur.moveToNext())
                   	{
                       String id = cur.getString(cur.getColumnIndex(BaseColumns._ID));
                       nameOfContact = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                       if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                       		{
                    	   		Cursor phones = cr .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                           ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id },null);

                           while (phones.moveToNext()) {
                              String  contactNumber = phones.getString(phones.getColumnIndex(Phone.NUMBER));
                               contactNumberType = phones.getInt(phones.getColumnIndex(Phone.TYPE));
                               //m1.put(contactNumber,nameOfContact);
                              ArrayList<String>a2=new ArrayList<String>();
                              
                              
                               Log.i(null, "...Contact Name ...." + nameOfContact
                                       + "...contact Number..." + contactNumber);
                              
                               //a2.add(nameOfContact);
                               StringBuffer b=new StringBuffer();
                               for(int i=0;i<contactNumber.length();i++)
                               {
                            	 if((contactNumber.charAt(i))!='-')
                            	   b.append(contactNumber.charAt(i)); 
                               }
                               contactNumber2=b.toString();
                               
                               
                               //call server
                               //if this contactNumber2 exist in database ,continue;
                               
                               temp = new HashMap<String, String>();
                               temp.put("Name", nameOfContact);
                               temp.put("Phone", contactNumber2);

                               data.add(temp);

                           }
                           phones.close();
                       }

                   }
               }// end of contact name cursor
               cur.close();
               

		}catch(Exception e){
			e.printStackTrace();
		}
           }//end of getContactNumber.

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(arg0.getItemAtPosition(arg2).equals("Add"))
		{
			Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(getApplicationContext(), "HERE", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add("Remove");
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
		
		
		return super.onContextItemSelected(item);
	}
	
	


}
