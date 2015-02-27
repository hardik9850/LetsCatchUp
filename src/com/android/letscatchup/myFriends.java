package com.android.letscatchup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class myFriends extends Activity {
	
	ArrayAdapter<String>  adapter;
	ListView listview;
	String result,uid,friendnames,remove_cont;
	SharedPreferences sp;
	
	List<NameValuePair> nameValuePairs;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		sp=getSharedPreferences("settings.txt",MODE_PRIVATE);
	 	uid=(sp.getString("uid", "0"));

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfriendlist);
		
		
			adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1);
//			adapter.add("1234");
			listview= (ListView)findViewById(R.id.friendlistView1);
			listview.setAdapter(adapter);
			registerForContextMenu(listview);
			getContactLists();
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.add("Delete");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ContentResolver cr = getContentResolver();
		AdapterContextMenuInfo metadata = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = metadata.position;
		//selecting contact to delete
		String selectName = adapter.getItem(pos).toString();
		
		try
		{ 
	//		Toast.makeText(addContact.this, "sending data", Toast.LENGTH_SHORT).show();
			HttpClient httpclient = new DefaultHttpClient();
			//file name to be changed 
	 
			HttpPost httppost = new HttpPost("http://letscatch.comyr.com/delete.php");
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("uid",uid));
			nameValuePairs.add(new BasicNameValuePair("name",selectName));
			
			
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
            
			JSONObject jobj = new JSONObject(result);
		    remove_cont=jobj.getString("deleted");
		    
		    if(remove_cont.equals("yes"))
		    {
		    	Toast.makeText(getApplicationContext(), "Friend has been removed ", 3000).show();
		    	getContactLists();
		    }	
		    else
		    {
		    	Toast.makeText(getApplicationContext(), "Error while removing this Friend ", 3000).show();
		    }	

            adapter.clear();         
    
		}catch(Exception e){}	
		return super.onContextItemSelected(item);
	}
	
	@SuppressWarnings("unchecked")
	public void getContactLists()
	{
		
		adapter.clear();
		
		try
		{ 
	//		Toast.makeText(addContact.this, "sending data", Toast.LENGTH_SHORT).show();
			HttpClient httpclient = new DefaultHttpClient();
			//file name to be changed 
	 
			HttpPost httppost = new HttpPost("http://letscatch.comyr.com/mylist.php");
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
			
			nameValuePairs.add(new BasicNameValuePair("uid",uid));
			
			
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
            adapter.clear();         
            //retireving result from server
            JSONObject j = new JSONObject(result);
            
            JSONArray jr = new JSONArray(j.getString("list"));
            
            for(int i=0;i<jr.length();i++)
            {
            	friendnames=jr.getString(i);
             //JSONObject jobj = jr.getJSONObject(i);
             //friendnames=jobj.getString("name");
             adapter.add(friendnames);

            } 

            
   //         }
             
       	 listview.setAdapter(adapter);//listview.invalidate();
      }catch(Exception e){
			e.printStackTrace();
			
		}

		
			}
	
}
