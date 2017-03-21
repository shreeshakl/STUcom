package kls.vtustore;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import com.backendless.async.callback.AsyncCallback;

import com.backendless.exceptions.BackendlessFault;

public class Login extends AppCompatActivity
{
    Button b,r,fp;
    Context t;
    String email,pass;
    EditText eMail,pAss;
    static ProgressDialog pd;
    static int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar21);
        setSupportActionBar(toolbar);
        t=this;
        b=(Button)findViewById(R.id.login_loginB);
        eMail=(EditText)findViewById(R.id.login_mail);
        pAss=(EditText)findViewById(R.id.login_pass);
        fp=(Button)findViewById(R.id.forgotpass);
        pd = new ProgressDialog(t);
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
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                email=eMail.getText().toString();
                pass=pAss.getText().toString();
                if(email.isEmpty())
                {
                    Snackbar.make(v,"Enter E-mail", Snackbar.LENGTH_LONG).show();
                }
                else if(pass.isEmpty())
                {
                    Snackbar.make(v,"Enter Password", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    flag=1;
                    pd.show();

                    Backendless.initApp(t, "Implement here", "Implement here", "Implement here");
                    Backendless.UserService.login(email, pass, new AsyncCallback<BackendlessUser>()
                    {
                        public void handleResponse(BackendlessUser user)
                        {




                            SharedPreferences sharedPref = t.getSharedPreferences("Implement here", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString("email1", email);
                            editor.putString("password", pass);
                            editor.putString("name", user.getProperty("name").toString());
                            editor.commit();




                            int rpermission= ContextCompat.checkSelfPermission(t,
                                    Manifest.permission.READ_EXTERNAL_STORAGE);
                            int wpermission=ContextCompat.checkSelfPermission(t,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            pd.dismiss();
                            flag=0;
                            if(rpermission!= PackageManager.PERMISSION_GRANTED || wpermission!=PackageManager.PERMISSION_GRANTED)
                            {
                                ActivityCompat.requestPermissions(Login.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1011);
                            }
                            else
                            {
                                Intent tem1=new Intent("android.intent.action.MPAGE");
                                tem1.putExtra("backendlesspath","/myfiles/");
                                startActivity(tem1);
                                finish();
                            }



                        }
                        public void handleFault(BackendlessFault fault)
                        {
                            pd.dismiss();
                            flag=0;



                            Toast.makeText(t,""+fault.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        fp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String te=eMail.getText().toString();
                if(te.isEmpty())
                {
                    Snackbar.make(v,"Enter E-mail", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    flag=1;
                    pd.show();
                    Backendless.UserService.restorePassword(eMail.getText().toString(), new AsyncCallback<Void>()
                    {
                        @Override
                        public void handleResponse(Void aVoid)
                        {


                            flag=0;
                            pd.dismiss();
                            Toast.makeText(t, "Reset link sent to your mail", Toast.LENGTH_LONG).show();
                            Intent loginintent1 = new Intent("android.intent.action.LOGIN");
                            startActivity(loginintent1);

                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault)
                        {
                            flag=0;
                            pd.dismiss();
                            Toast.makeText(t, "" + backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {

       
        getMenuInflater().inflate(R.menu.reg_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        
        int id = item.getItemId();

       
        if (id == R.id.regis)
        {
            Intent regintent=new Intent("android.intent.action.REGISTER");
            startActivity(regintent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1011: {
                
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    /
                    Intent tem1=new Intent("android.intent.action.MPAGE");
                    tem1.putExtra("backendlesspath","/myfiles/");
                    startActivity(tem1);
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

    @Override
    public void onBackPressed()
    {
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

