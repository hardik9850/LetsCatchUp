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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class LoginScreen extends Activity 
{
	Button logincancel,login;
	EditText uname,pwd;
	public final static String UNAME=null;
	public final static String UID=null;
	
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	String username,password,result,uid_cont;
	HttpResponse response;
	TextView tv;
	ProgressDialog dialog = null;
	InputStream is;
	int flag=0;
	String uuid_cont;
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginscreen);
		
		uname=(EditText)findViewById(R.id.loginEditUsername);
		pwd=(EditText)findViewById(R.id.loginEditPassword);
		
		logincancel=(Button)findViewById(R.id.loginScreenCancelbutton);
		login=(Button)findViewById(R.id.loginButton);
		
		login.setOnClickListener(new View.OnClickListener()
			{
				//Override
				public void onClick(View arg0) 
					{
					
					
					username=uname.getText().toString();
					password=pwd.getText().toString();
				
					if(username.equals("") || password.equals(""))
						{
							Toast.makeText(LoginScreen.this, "Please enter the required fields", Toast.LENGTH_SHORT).show();
						}
					else
						{
							postdata();	
							// TODO Auto-generated method stub
							Intent intent1=getIntent();
							String data=intent1.getStringExtra(ScreensActivity.NAME);
							Intent intent=new Intent(LoginScreen.this,afterLogin.class);
							intent.putExtra("UNAME", username);
							System.out.println("user in login"+username);
							intent.putExtra("UID", uuid_cont);
							System.out.println("uid in login"+uuid_cont);
								
							if(flag==0)
							startActivity(intent);
						}
					}
			public void postdata()
			{
				Connection conn=null;
				try
					{ 
						Toast.makeText(LoginScreen.this, "Processing your request", Toast.LENGTH_SHORT).show();
						HttpClient httpclient = new DefaultHttpClient();
						//file name to be changed 
				 
						HttpPost httppost = new HttpPost("http://letscatch.comyr.com/login.php");
						//add your data
						nameValuePairs = new ArrayList<NameValuePair>(2);
						// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
			        
						nameValuePairs.add(new BasicNameValuePair("username",uname.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
						nameValuePairs.add(new BasicNameValuePair("password",pwd.getText().toString().trim()));
			       
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						//Execute HTTP Post Request
						response=httpclient.execute(httppost);
			       
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
					    uuid_cont=jobj.getString("uid");
						
						//Toast.makeText(getApplicationContext(), "uid fetched frm server1 "+uuid_cont +" not null",1000).show();
						
						System.out.println("Response : " + uuid_cont);
						if(uuid_cont.equals("0"))
						{
							Toast.makeText(getApplicationContext(), "login error...invalid user", 1000).show();
							Intent err=new Intent(getApplicationContext(),LoginOrRegisterActivity.class);
							startActivity(err);
							flag=1;
						}
						else
						{
							flag=0;
							return;
						}	
					}	
						catch(Exception e){
							}
						}
							
		});
		
		logincancel.setOnClickListener(new View.OnClickListener()
		{
			
			//@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginScreen.this,LoginOrRegisterActivity.class);
				startActivity(intent);
			}
		});
	}

}
