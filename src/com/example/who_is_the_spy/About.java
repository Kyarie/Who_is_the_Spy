package com.example.who_is_the_spy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		User_Preferences ();
	}
	
	public void User_Preferences () {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String user_pref = sp.getString("background_preference","Simple");
    	RelativeLayout main_layout = (RelativeLayout)this.findViewById(R.id.about_root);
        if (user_pref.equals("Simple")){
        	main_layout.setBackgroundResource(R.drawable.background_1);
        }
        else if (user_pref.equals("Night")){
        	main_layout.setBackgroundResource(R.drawable.background_2);
        }
    }
	
}
