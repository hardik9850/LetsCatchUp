package com.android.letscatchup;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LoginOrRegisterActivity extends Activity
{
	Button register,signin,cancelregisterorsignin;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerorsignin);
		
		register=(Button)findViewById(R.id.RegisterButton);
		signin=(Button)findViewById(R.id.SigninButton);
		cancelregisterorsignin=(Button)findViewById(R.id.cancelRegisterSigninButton);
		
		//after clicking register this happens
		register.setOnClickListener(new View.OnClickListener()
		{		
		//	@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent registerintent= new Intent(LoginOrRegisterActivity.this,ScreensActivity.class);
				startActivity(registerintent);
			}
		});
		
		//after clicking signin this happens
		signin.setOnClickListener(new View.OnClickListener()
		{	
			//@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent signinintent=new Intent(LoginOrRegisterActivity.this,LoginScreen.class);
				startActivity(signinintent);				
			}
		});
		
		//cancel
		cancelregisterorsignin.setOnClickListener(new View.OnClickListener()
		{	
			//@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
