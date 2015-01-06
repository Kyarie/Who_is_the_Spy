package com.example.who_is_the_spy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class PreferenceSettings extends PreferenceActivity {
	
	Boolean sound;
	String background;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		//Storing original preferences   
		Original_Preferences ();
		
		//Adding button
		ListView footer = getListView();
		Button save_button = new Button(this);
		save_button.setText("Save");
        footer.addFooterView(save_button);
        save_button.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
				Intent Homescreen = new Intent(getBaseContext(), MainMenu.class);
    			startActivity(Homescreen);  
		   }
		   });
        

		/*Preference button = (Preference)findPreference("button");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		
			@Override
			public boolean onPreferenceClick(Preference arg0) { 
				Intent Homescreen = new Intent(getBaseContext(), MainMenu.class);
    			startActivity(Homescreen);  
				return true;
				}
			});*/
	}
	
	public void Original_Preferences (){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		sound = sp.getBoolean("Sound Preference", false);
    	background = sp.getString("background_preference","Simple");    	
	}
	
	public void onBackPressed() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		//sp.edit().putBoolean("Sound Preference", sound);
		//sp.edit().putString("background_preference", background);
		
		CheckBoxPreference sound_preference = (CheckBoxPreference)findPreference("Sound Preference");
		sound_preference.setChecked(sound);
		
		ListPreference background_preference = (ListPreference)findPreference("background_preference");
		background_preference.setValue(background);
		
		this.finish();
	}
	
}
