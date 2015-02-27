package com.android.letscatchup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class settings extends Activity {
	Button start, loginButton;
	Spinner spin1,spin2;

	
	private String array_spinner[],array_dist[];

	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		SharedPreferences sp=getSharedPreferences("settings.txt",MODE_PRIVATE);
		
		int s_time_intrvel=sp.getInt("time_index", 0);
		int S_dist_min=sp.getInt("dist_index", 0);
		
		spin1=(Spinner)findViewById(R.id.frequency_spinner);
		array_spinner=new String[5];
		array_spinner[0]="1";
		array_spinner[1]="5";
		array_spinner[2]="10";
		array_spinner[3]="15";
		array_spinner[4]="30";
		
		ArrayAdapter<String> arrad1=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,array_spinner);
	    arrad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spin1.setAdapter(arrad1);
	    
	    spin2=(Spinner)findViewById(R.id.radius_spinner);
		
	    array_dist=new String[5];
		array_dist[0]="1";
		array_dist[1]="2";
		array_dist[2]="5";
		array_dist[3]="10";
		array_dist[4]="15";
		
		ArrayAdapter<String> arrad2=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,array_dist);
	    arrad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spin2.setAdapter(arrad2);
	    
	    
		spin1.setSelection(s_time_intrvel);
		
		spin2.setSelection(S_dist_min);
		
		loginButton=(Button)findViewById(R.id.Loging_Button);
		

		loginButton.setOnClickListener(new OnClickListener()
		{

		//	@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				//This will invoke onStartCommand() method of startsending Service class
				Intent i1=new Intent(settings.this,LoginScreen.class);
				startActivity(i1);
			}
			
		});
		
		
		
		spin1.setOnItemSelectedListener(new OnItemSelectedListener()
        {

	          public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id) {
	               try {
					// TODO Auto-generated method stub
					int min_index = position;
					int freq_to_sleep1 = Integer.parseInt(spin1
							.getItemAtPosition(position).toString());
					String freq_to_sleep2 = spin1.getSelectedItem().toString();
					SharedPreferences spPreferences = getSharedPreferences(
							"settings.txt", MODE_PRIVATE);
					SharedPreferences.Editor speEditor = spPreferences.edit();
					speEditor.putInt("time_interval", freq_to_sleep1);
					System.out.println(freq_to_sleep1 + "and" + freq_to_sleep2);
					speEditor.putInt("time_index", min_index);
					speEditor.commit();
					Toast.makeText(getApplicationContext(), freq_to_sleep1,
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
	                
	          }

	                public void onNothingSelected(AdapterView<?> arg0) {
	            // TODO Auto-generated method stub

	            }

	        });

		
		
		spin2.setOnItemSelectedListener(new OnItemSelectedListener()
        {

	          public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id) {
	               // TODO Auto-generated method stub
	        	  int dist_min= position;
	        	  int freq_to_walk1=Integer.parseInt(spin2.getItemAtPosition(position).toString());
	        	  	String freq_to_walk2=spin2.getSelectedItem().toString();
	        	  	
	        	  	SharedPreferences spPreferences=getSharedPreferences("settings.txt",MODE_PRIVATE);
	        	  	SharedPreferences.Editor speEditor=spPreferences.edit();
	        	  	
	        	  	//speEditor.putInt("walk_dist",freq_to_walk1);
	        	  	speEditor.putString("walk_dist",freq_to_walk2);
		        	
	        	  	System.out.println(freq_to_walk1+"walk1 and walk2"+freq_to_walk2 );
	        	  	speEditor.putInt("dist_index",dist_min );
	        	  	speEditor.commit();
	        	  	
	        	  	
	        	  	//Toast.makeText(getApplicationContext(), freq_to_sleep1, Toast.LENGTH_SHORT).show();
	                
	          }

	                public void onNothingSelected(AdapterView<?> arg0) {
	            // TODO Auto-generated method stub

	            }

	        });


			
	}
}
