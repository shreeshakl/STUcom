package kls.vtustore;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by Shreesha K L on 04/10/2016.
 */

public class FeedBack extends Activity
{
    Button bt;
    EditText et;
    static ProgressDialog pd;
    static int flag=0;
    Context t;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);
        bt=(Button)findViewById(R.id.fsend);
        et=(EditText)findViewById(R.id.ftextbox);
        t=this;
        SharedPreferences sharedPref = this.getSharedPreferences("Implement here", Context.MODE_PRIVATE);
        final String mail = sharedPref.getString("email1","No value");
        final String name = sharedPref.getString("name","No value");
        final String pass=sharedPref.getString("password","No password");
        final String err=sharedPref.getString("err","No error");
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                flag=1;
                pd = new ProgressDialog(t);
                pd.setMessage("Sending");
                pd.setIndeterminate(false);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCancelable(true);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        finish();
                    }
                });
                pd.show();
                final String body="From: "+mail+" \nName: "+name+" \nMessage: " +et.getText().toString()+"\nError:"+err;
                Backendless.initApp(t, "Implement here", "Implement here");
                Backendless.UserService.login(mail, pass, new AsyncCallback<BackendlessUser>()
                {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser)
                    {
                        Backendless.Messaging.sendTextEmail("Feedback", body, "appDeveloperFeedback@gmail.com", new AsyncCallback<Void>()
                        {
                            @Override
                            public void handleResponse(Void responder)
                            {
                                pd.dismiss();
                                flag=0;
                                Toast.makeText(t, "Feedback Sent", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault)
                            {
                                pd.dismiss();
                                flag=0;
                                Snackbar.make(v,backendlessFault.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault)
                    {
                        pd.dismiss();
                        flag=0;
                        Snackbar.make(v,backendlessFault.getMessage(), Snackbar.LENGTH_LONG).show();
                    }

                });

            }
        });

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(flag==1)
            pd.dismiss();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(flag==1)
            pd.show();
    }
}
