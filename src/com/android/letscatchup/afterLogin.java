package com.android.letscatchup;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class afterLogin extends Activity
{
	public static final String UNAME = null;
	public static final String UID = null;
	Button add,delete,showmap,setting,mylist;
	TextView welcometxt;
	String username;
	String uid_cont;
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		
		//receiving data from the calling Activity
		setContentView(R.layout.afterlogin);
		add=(Button)findViewById(R.id.addFriendsButton);
		showmap=(Button)findViewById(R.id.ShowMap);
		setting=(Button)findViewById(R.id.Settings);
		welcometxt=(TextView)findViewById(R.id.Welcome);
		mylist=(Button)findViewById(R.id.myList);
		Intent intent1=getIntent();
		
	    username=intent1.getStringExtra("UNAME");
	    
		uid_cont=intent1.getStringExtra("UID");
		
		SharedPreferences spPreferences = getSharedPreferences(
				"settings.txt", MODE_PRIVATE);
		SharedPreferences.Editor speEditor = spPreferences.edit();
		speEditor.putString("uid", uid_cont);
		
		speEditor.commit();
		
		System.out.println("username......"+username);
		System.out.println("\n  uid.... "+uid_cont);
		welcometxt.setText(" Welcome "+username);
				
		add.setOnClickListener(new View.OnClickListener()
		{
			
//			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(afterLogin.this,addContact.class);
				intent.putExtra(UID, uid_cont);
				
				//System.out.println("cliked");
				//Toast.makeText(getApplicationContext(), "text", 10000).show();
				startActivity(intent);
			}
		});
		
		
		showmap.setOnClickListener(new View.OnClickListener()
		{
			
		//	@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getApplicationContext(),LetsMap.class);
				
				startActivity(intent);
			}
		});
		
		setting.setOnClickListener(new View.OnClickListener()
		{
			
			//@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(afterLogin.this,settings.class);
				startActivity(intent);
			}
		});


		mylist.setOnClickListener(new View.OnClickListener()
		{
			
			//@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(afterLogin.this,myFriends.class);
				startActivity(intent);
			}
		});


	}
}
