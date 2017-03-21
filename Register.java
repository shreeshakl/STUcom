package kls.vtustore;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * Created by Shreesha K L on 18/08/2016.
 */
public class Register extends AppCompatActivity
{

        Button b;
        String username;
        String email;
        String passw,repas;
        EditText uName, Pass, eMail,rePass;
        Context con;
        static ProgressDialog pd;
        static int flag=0;

       
        @Override
        protected void onCreate (Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.register);
            con=this;
            b = (Button) findViewById(R.id.r_b);
            uName = (EditText) findViewById(R.id.r_name);
            eMail = (EditText) findViewById(R.id.r_email);
            Pass = (EditText) findViewById(R.id.r_pas);
            rePass = (EditText) findViewById(R.id.repass);
            b.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    username=uName.getText().toString();
                    email=eMail.getText().toString();
                    passw=Pass.getText().toString();
                    repas=rePass.getText().toString();
                    if(username.isEmpty() || email.isEmpty() || passw.isEmpty() || repas.isEmpty())
                    {
                        Toast.makeText(con,"Fill all the fields",Toast.LENGTH_LONG).show();
                    }
                    else if (!repas.equals(passw))
                    {
                        Toast.makeText(con,"Password doesn't match",Toast.LENGTH_LONG).show();
                      
                    }
                    else
                    {
                        pd = new ProgressDialog(con);
                        pd.setMessage("Please wait");
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

                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute("");

                    }
                }
            });
        }
        private class AsyncTaskRunner extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {


                BackendlessUser user = new BackendlessUser();
                user.setEmail(email);
                user.setPassword(passw);
                user.setProperty("name", username);

                Backendless.initApp(con, "Implement here", "Implement here", "Implement here");
                    Backendless.UserService.register(user, new BackendlessCallback<BackendlessUser>()
                    {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser)
                        {
                          

                            flag=0;
                            pd.dismiss();
                            Toast.makeText(con, "Registration Successfull.\nCheck your Email for conformation", Toast.LENGTH_LONG).show();

                            Intent loginintent2 = new Intent("android.intent.action.LOGIN");
                            startActivity(loginintent2);
                          
                            finish();
                           

                        }

                        @Override
                        public void handleFault(BackendlessFault fault)
                        {
                            
                            flag=0;
                            pd.dismiss();

                            Toast.makeText(con,""+fault.getMessage(),Toast.LENGTH_LONG).show();
                            finish();



                        }
                    });

                   return null;
            }

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                flag=1;
                pd.show();
            }
            @Override
            protected void onProgressUpdate(Void... values)
            {
                super.onProgressUpdate(values);

            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

            }
        }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        Intent loginintent23 = new Intent("android.intent.action.LOGIN");
        startActivity(loginintent23);
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

