package com.example.who_is_the_spy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        beginAnimation ();
    }
    
    private void beginAnimation() {       
     // Image Animation
     ImageView image = (ImageView) findViewById(R.id.awesoft_logo);
     Animation imageAnim = AnimationUtils.loadAnimation(this, R.animator.splash_image);
     image.startAnimation(imageAnim);        

     // Prepare end of animation event
     imageAnim.setAnimationListener(new AnimationListener() {
         public void onAnimationEnd(Animation animation) {             
             //Open the Main Menu
        	 /* New Handler to start the Menu-Activity 
              * and close this Splash-Screen after some seconds.*/
             new Handler().postDelayed(new Runnable(){
                 @Override
                 public void run() {
                     /* Create an Intent that will start the Menu-Activity. */
                     Intent Homescreen = new Intent(Splash.this, MainMenu.class);
                     startActivity(Homescreen);
                     Splash.this.finish();
                 }
             }, 1000);
         }

         //Required method, nothing to do here
         public void onAnimationRepeat(Animation animation) {
         }

         //Required method, nothing to do here
         public void onAnimationStart(Animation animation) {
         }
     });
 }
    
    @Override
    protected void onPause() {
        super.onPause();      
        // Clear the animation. Start fresh on resume.   
        ImageView image = (ImageView) findViewById(R.id.awesoft_logo);
        image.clearAnimation();        
    }

    @Override
    protected void onResume() {
        super.onResume();       
        //Start the animation from the beginning.
        beginAnimation();
    }

}
