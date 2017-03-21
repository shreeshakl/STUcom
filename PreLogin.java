package kls.vtustore;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Shreesha K L on 21/08/2016.
 */
public class PreLogin extends Activity
{
    String fav;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        SharedPreferences sharedPref = this.getSharedPreferences("Implement here", Context.MODE_PRIVATE);
        String mailsp=sharedPref.getString("email1","No value");
        String passsp=sharedPref.getString("password","No password");
        fav=sharedPref.getString("favPage","No value");
       
        if(mailsp.equals("No value")==false && passsp.equals("No value")==false)
        {

          
            int rpermission= ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int wpermission=ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(rpermission!= PackageManager.PERMISSION_GRANTED || wpermission!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
            }
            else
            {
                Intent tem=new Intent("android.intent.action.MPAGE");
                if (fav.equals("No value")==false)
                    tem.putExtra("backendlesspath",fav);
                else
                    tem.putExtra("backendlesspath","/myfiles/");
                startActivity(tem);
                finish();
            }
        }
        else
        {

            Intent loginintent = new Intent("android.intent.action.LOGIN");
            startActivity(loginintent);
            finish();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 101: {
               
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                   
                    Intent tem=new Intent("android.intent.action.MPAGE");
                    if (fav.equals("No value")==false)
                        tem.putExtra("backendlesspath",fav);
                    else
                        tem.putExtra("backendlesspath","/myfiles/");
                
                    startActivity(tem);
                    finish();

                }
                else
                {
                    finish();

                   
                }
                return;
            }

           
        }
    }



}
