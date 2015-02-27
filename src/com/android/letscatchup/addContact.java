package com.android.letscatchup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class addContact extends Activity implements OnItemClickListener
{
	SimpleAdapter adapter = null;
	ListView list = null;
	HashMap<String,String> temp;
	ArrayList<HashMap<String, String>> data;
	List<NameValuePair> nameValuePairs;
	String result,uid_cont,friend_found;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfriends);
		
		Intent intent1=getIntent();
		uid_cont=intent1.getStringExtra(LoginScreen.UID);
		
		Toast.makeText(getApplicationContext(),"my id"+uid_cont, 3000).show();
		
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
	//		Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(getApplicationContext(), "HERE", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	//	Toast.makeText(getApplicationContext(),""+((TextView)v.findViewById(android.R.id.text2)).getText(),Toast.LENGTH_SHORT).show();
		menu.add("Add");
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ContentResolver cr = getContentResolver();
		AdapterContextMenuInfo metadata = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = metadata.position;
		
		String selectName = adapter.getItem(pos).toString();
		System.out.println(selectName.trim());
		System.out.println(selectName+"is  addedd ");
		String array[]=selectName.split(TELEPHONY_SERVICE);
		
		String fnumber=selectName.substring(selectName.lastIndexOf("=")+1, selectName.lastIndexOf("}"));
		
		System.out.println(fnumber+"answeeeeeeeeeeeeeeeeeeeeeerrr");
		
		

		//Toast.makeText(getApplicationContext(), "Done"+fnumber, Toast.LENGTH_SHORT).show();
		Connection conn=null;
		try
			{ 
		//		Toast.makeText(addContact.this, "sending data", Toast.LENGTH_SHORT).show();
				HttpClient httpclient = new DefaultHttpClient();
				//file name to be changed 
		 
				HttpPost httppost = new HttpPost("http://letscatch.comyr.com/userfriend.php");
				//add your data
				nameValuePairs = new ArrayList<NameValuePair>(2);
				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
	        
				nameValuePairs.add(new BasicNameValuePair("uid",uid_cont));  // $Edittext_value = $_POST['Edittext_value'];
				nameValuePairs.add(new BasicNameValuePair("fnumber",fnumber));  // $Edittext_value = $_POST['Edittext_value'];
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				//Execute HTTP Post Request
				 HttpResponse response=httpclient.execute(httppost);
	       
				
			
				HttpEntity entity = response.getEntity();
				
		//		String is=entity.getContent().toString();
			InputStream is= entity.getContent();	
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine())!=null){
                    sb.append(line+"\n");
                }
                is.close();
                result=sb.toString();
                
                //retireving result from server
                JSONObject jobj = new JSONObject(result);
			    friend_found=jobj.getString("added");
				
				if(friend_found.equals("yes"))
				{
					Toast.makeText(getApplicationContext(),"Added Successfully",4000).show();
								
				}
				else
				if(friend_found.equals("exist"))
				{
					Toast.makeText(getApplicationContext(),"This person is already in your list",4000).show();
									
				}	
				else
				{
					Toast.makeText(getApplicationContext(),"Your Friend is not using Lets Catch Up",4000).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			//	Toast.makeText(getApplicationContext(), "uid fetched frm server1"+uid_cont, 1000).show();

		return super.onContextItemSelected(item);
	}
}
