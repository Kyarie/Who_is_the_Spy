package com.example.who_is_the_spy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class PrepScreen extends Activity implements OnClickListener{
	
	EditText edittext_players;
	int num_of_players = 3;
	EditText[] player_name = new EditText[20];
	String[] name_list = new String[20];	
	boolean blank_card = false;
	boolean spam_prevention = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prep_screen);
		
		//Settings
		User_Preferences ();
		EditText player_name1 = (EditText)this.findViewById(R.id.player_name1);
		EditText player_name2 = (EditText)this.findViewById(R.id.player_name2);
		EditText player_name3 = (EditText)this.findViewById(R.id.player_name3);		
    	EditText_Colour (player_name1);
    	EditText_Colour (player_name2);
    	EditText_Colour (player_name3);
    			
		//buttons
		Button PlusButton =(Button)this.findViewById(R.id.prep_plus);
		PlusButton.setOnClickListener(this);
		Button MinusButton =(Button)this.findViewById(R.id.prep_minus);
		MinusButton.setOnClickListener(this);        
        Button StartButton =(Button)this.findViewById(R.id.prep_start);
        StartButton.setOnClickListener(this); 
        Button CancelButton =(Button)this.findViewById(R.id.prep_cancel);
        CancelButton.setOnClickListener(this);
	}
	
	public void User_Preferences () {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String user_pref = sp.getString("background_preference","Simple");
    	LinearLayout main_layout = (LinearLayout)this.findViewById(R.id.prepscreen_root);    	
        if (user_pref.equals("Simple")){
        	main_layout.setBackgroundResource(R.drawable.background_1); 	
        }
        else if (user_pref.equals("Night")){
        	main_layout.setBackgroundResource(R.drawable.background_2); 
        }
    }
	
	public void EditText_Colour (EditText element){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String user_pref = sp.getString("background_preference","Simple");
    	if (user_pref.equals("Simple")){
        	element.setBackgroundColor(0xCC000000);
        	element.setTextColor(Color.WHITE);
        }
        else if (user_pref.equals("Night")){
        	element.setBackgroundColor(0xCCFFFFFF);
        	element.setTextColor(Color.BLACK);
        }
	}
	
	@Override
	public void onClick(View thisView) {
    	switch (thisView.getId()) {
    		case R.id.prep_plus:
    			if (num_of_players < 20){
	    			player_name[num_of_players] = new EditText(this);
	    			player_name[num_of_players].setEms(10);	    			
	    			player_name[num_of_players].setHeight(80);
	    			player_name[num_of_players].setTextSize(20);	    			
	    			player_name[num_of_players].setPadding(0, 1, 0, 1);	    			
	    			EditText_Colour (player_name[num_of_players]);
	    			
	    			LinearLayout names_layout = (LinearLayout)findViewById(R.id.prep_names_list);	            
	    			names_layout.setGravity(Gravity.CENTER_HORIZONTAL);
	    		    LayoutParams names_param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	    		   
	    		    names_param.setMargins(0, 20, 0, 0);
	    		    names_layout.addView(player_name[num_of_players], names_param);
	    			View child = getLayoutInflater().inflate(R.layout.prep_screen_inflate, null);         
	    			names_layout.addView(child);    			
	    			num_of_players++;
    			}
    			else {
    				invalidCharacterToast("Maximum 20 participants");
    			}
    			break;
    		case R.id.prep_minus:
    			if (num_of_players > 3){
	    			player_name[num_of_players-1].setVisibility(View.GONE);
	    			num_of_players--;
    			}
    			else {
    				invalidCharacterToast("Minimum 3 participants");
    			}
    			break;
    		case R.id.prep_start:
				//Receiving input from EditText
				/*edittext_players = (EditText)findViewById(R.id.number_of_players);
				String number_of_players = edittext_players.getText().toString();*/
    			boolean check = true;
				for (int i=0; i<num_of_players; i++){
					if (i<3){
						int resID = getResources().getIdentifier("player_name" + (i+1),"id", getPackageName());
						player_name[i] = (EditText)findViewById(resID);
					}
					name_list[i] = player_name[i].getText().toString();
					if (name_list[i] == null){
						invalidCharacterToast("Please fill out all player names");
						check = false;
						break;
					}
				}
				if (check == false){
					break;
				}
				
				Spinner category_spinner = (Spinner)this.findViewById(R.id.category_spinner);				
				String category = category_spinner.getSelectedItem().toString();				
				
				//Passing data to GameScreen activity	    				
				Bundle b=new Bundle();
				b.putStringArray("names", name_list);
				b.putInt("numberofplayers", num_of_players);
				b.putString("category", category);
				b.putBoolean("BlankCard", blank_card);
				
			    Intent startGame = new Intent(getApplicationContext(), GameScreen.class);	    			    
			    startGame.putExtras(b);
    			startActivity(startGame);
    			break;
    		case R.id.prep_cancel:
				Intent Homescreen = new Intent(this, MainMenu.class);
				startActivity(Homescreen);
				break;
    	}
    }	
	
	public void onToggleClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();	    
	   
	    if (on) {
	    	if (num_of_players > 3){
	    		blank_card = true;
	    	}
	    	else {
	    		invalidCharacterToast("Blank card require at least 4 participants");
	    		ToggleButton tb = (ToggleButton)this.findViewById(R.id.blank_toggle);
	    		tb.setChecked(false);
	    	}
	    } else {
	    	blank_card = false;
	    }
	}
	
	public void invalidCharacterToast(String alert_phrase) {
	    if (spam_prevention == false){
	    spam_prevention = true;
		TextView displayView = new TextView(this);
        displayView.setBackgroundColor(Color.DKGRAY);
        displayView.setTextColor(Color.YELLOW);
        displayView.setPadding(10,10,10,10);
        displayView.setText(alert_phrase);
        Toast theToast = new Toast(this);
        theToast.setView(displayView);
        theToast.setDuration(Toast.LENGTH_SHORT);
        theToast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        theToast.show();
        Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
		         public void run() { 
		        	 spam_prevention = false;
		         } 
		    }, 2000);
	    }
     }	
}
