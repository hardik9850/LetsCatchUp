package com.android.letscatchup;	

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
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
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ScreensActivity extends Activity 
{
	int flag=1;
	Button create,cancelaccount;
	EditText name,mobile,username,password;
	String result,existing_user;
	//data to send to another activity
	public final static String NAME=null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);  //screen/layout name

        name=(EditText)findViewById(R.id.editName);
        mobile=(EditText)findViewById(R.id.editMobile);
        username=(EditText)findViewById(R.id.editUsername);
        password=(EditText)findViewById(R.id.editPassword);        
        create=(Button)findViewById(R.id.createAccountButton);        
        cancelaccount=(Button)findViewById(R.id.cancelaccountbutton);
        
        create.setOnClickListener(new View.OnClickListener()
        {			
				
			//code to retrieve data from cloud and check if the entered record is present or not
	 
	   		 		//@Override		
			public void onClick(View arg0) 
			{				
				// TODO Auto-generated method stub
				
				//get the data from cloud,check if the record already exist,if not create record by storing it on cloud

   			        	String user_name,user_mobile,login_name,login_password;
   			        	user_name=name.getText().toString();
   			        	user_mobile=mobile.getText().toString();
   			        	login_name=username.getText().toString();
   			        	login_password=password.getText().toString();
   			        	
   			       	if(user_name.equals("") || user_mobile.equals("")|| login_name.equals("")|| login_password.equals(""))
   			       	{
   			       		Toast.makeText(getApplicationContext(), "Please enter the required fields.", Toast.LENGTH_SHORT).show();
   			      	}//if
	   	       
				
   			       	else
   			       	{
   			       		Connection conn=null;
   			       		HttpClient httpclient = new DefaultHttpClient();
   			       		HttpPost httppost = new HttpPost("http://letscatch.comyr.com/users.php");
   			       		
   			       		try 
   			       		{
   			        		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
   			       			nameValuePairs.add(new BasicNameValuePair("Name",user_name));
   			       			nameValuePairs.add(new BasicNameValuePair("Mobile",user_mobile));
   			       			nameValuePairs.add(new BasicNameValuePair("Username",login_name));
   			       			nameValuePairs.add(new BasicNameValuePair("Password",login_password));
   			        
   			       			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
   			        
   			       			// Execute HTTP Post Request
   			                HttpResponse response = httpclient.execute(httppost);
   							HttpEntity entity = response.getEntity();
   			                InputStream is= entity.getContent();	
   							
						BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
	                    StringBuilder sb = new StringBuilder();
	                    String line = null;
	                    while((line = reader.readLine())!=null){
	                        sb.append(line+"\n");
	                    }
	                    is.close();
	                    result=sb.toString();
	                  
						
	                    flag=0;
						JSONObject jobj = new JSONObject(result);
					    existing_user=jobj.getString("found");
				
					    if(existing_user.equals("yes"))
					    {
					    	Toast.makeText(getApplicationContext(), "Sorry,Please Select Differnet Username", 3000).show();
					    	flag=1;
					    }	
					    else
					    {
					    	flag=0;
					    }	
   			       			//httpclient.execute(httppost);	   			        
   			       		}//try
   			        
   			       		catch (Exception e3) 
   			       		{
   			       			// TODO Auto-generated catch block
   			       		Toast.makeText(ScreensActivity.this,e3.getMessage(), Toast.LENGTH_LONG).show();
   			       			Log.e("problem", "caught");
   			       		
   			       			e3.printStackTrace();
   			       			
   			       		}//catch
   			       	 finally{  
   	                    if (conn != null){  
   	                        try{  
   	                            conn.close ();  
   	                        }  
   	                        catch (Exception e) { e.printStackTrace(); }  
   	                    }
   	                }
   	                
   			       	 	if(flag==0)
   			       	 	{	
   			       	 		Toast.makeText(ScreensActivity.this, "Your account has been created succesfully", 30000).show();
   			       	 		Intent intent=new Intent(ScreensActivity.this, LoginScreen.class);
				
   			       	 		EditText name=(EditText)findViewById(R.id.editName);
   			       	 		String message=name.getText().toString();
   			       	 		intent.putExtra(NAME, message);
   			       	 		flag=1;
   			       	 		startActivity(intent);
   			       	 	}
   			       	 	else
   			       	 	{
   			       	 		new Intent(ScreensActivity.this, ScreensActivity.class);
   			       	 		startActivity(new Intent(ScreensActivity.this, ScreensActivity.class));
   			       	 	}	
	   		 	}
			}//onClick
		});
        
        cancelaccount.setOnClickListener(new View.OnClickListener()
        {
			
			//@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(ScreensActivity.this,LoginOrRegisterActivity.class);
				startActivity(intent);
			}
		});
    }
}