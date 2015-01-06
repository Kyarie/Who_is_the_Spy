package com.example.who_is_the_spy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.view.Menu;
import android.view.MenuItem;

public class MainMenu extends Activity implements OnClickListener{
	//private String[] navigation_items;
	//private DrawerLayout navigation_drawer;
	//private ListView navigation_list;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        //Settings
        User_Preferences ();
        
        //buttons
        Button StartButton=(Button)this.findViewById(R.id.start_button);
        StartButton.setOnClickListener(this);
        Button HowtoplayButton=(Button)this.findViewById(R.id.howtoplay_button);
        HowtoplayButton.setOnClickListener(this);
        Button OptionsButton=(Button)this.findViewById(R.id.options_button);
        OptionsButton.setOnClickListener(this); 
        Button AboutButton=(Button)this.findViewById(R.id.about_button);
        AboutButton.setOnClickListener(this);
        
        //Navigation Drawer
    }
    
    @Override
    public void onClick(View thisView) {
    	switch (thisView.getId()) {
    		case R.id.start_button:
    				Intent showAbout = new Intent(this, PrepScreen.class);
    				startActivity(showAbout);
    				break;
    		case R.id.howtoplay_button:
    			Intent howtoplay = new Intent(this, HowToPlay.class);
    			startActivity(howtoplay);
    			break;
    		case R.id.options_button:
        			Intent SettingsMenu = new Intent(this, PreferenceSettings.class);
        			startActivity(SettingsMenu);
        			break;
    	    case R.id.about_button:
    				Intent AboutMenu = new Intent(this, About.class);
    				startActivity(AboutMenu);
    				break;
    	}
    }    
    
    public void User_Preferences () {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String user_pref = sp.getString("background_preference","Simple");
    	RelativeLayout main_layout = (RelativeLayout)this.findViewById(R.id.mainmenu_root);
        if (user_pref.equals("Simple")){
        	main_layout.setBackgroundResource(R.drawable.background_1);
        }
        else if (user_pref.equals("Night")){
        	main_layout.setBackgroundResource(R.drawable.background_2);
        }
    }
    
    @Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
	    .setTitle("Exit?")
	    .setMessage("Are you sure you want to end the current game?")
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) { 
	        	dialog_exit();
	        }
	     })
	    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) { 
	        	dialog.cancel();
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
    
	public void dialog_exit(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
	
}
