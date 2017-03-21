package kls.vtustore;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.os.PowerManager;


import android.support.design.widget.NavigationView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.files.FileInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Date;
import java.util.Iterator;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import static android.os.Environment.getExternalStorageDirectory;


/**
 * Created by Shreesha K L on 02/10/2016.
 */

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Context t;
    int ii = 0;
    static ProgressDialog mProgressDialog,uProgressBar,pd2;
    static int flag2=0;
    Iterator<FileInfo> filesIterator;
    String backendlesspath = "/myfiles/";
    String username1,name,password24,downfilename,uuuuname;
    static String foldername;
    static int flagnewfolder=0;
    static PowerManager.WakeLock mWakeLock,uWakeLock;
    static int df=0, uf=0;
    static File finalfile;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_side_menu);
        SharedPreferences sharedPref1 = this.getSharedPreferences("Implement here", Context.MODE_PRIVATE);
        username1=sharedPref1.getString("email1","No value");
        password24=sharedPref1.getString("password","No password");
        name=sharedPref1.getString("name","No name");
        uuuuname=name;

     // sideE.setText(username1);
    // sideNa.setText(name);


        Intent intent = getIntent();
        backendlesspath = intent.getExtras().getString("backendlesspath");
        t = this;


        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        pd2 = new ProgressDialog(t);
        pd2.setMessage("Loading");
        pd2.setIndeterminate(false);
        pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd2.setCancelable(true);
        pd2.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                finish();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Backendless.initApp(this, "Implement here", "Implement here", "Implement here");
        if(df==0 && uf==0 && flag2==0)
        {
            flag2 = 1;
            pd2.show();
        }
        Backendless.UserService.login(username1, password24, new AsyncCallback<BackendlessUser>()
        {
            public void handleResponse(BackendlessUser user)
            {
                // user has been logged in


                Backendless.Files.listing(backendlesspath, "*", false, 100, 0, new AsyncCallback<BackendlessCollection<FileInfo>>()
                {
                    @Override
                    public void handleResponse(BackendlessCollection<FileInfo> response)
                    {


                        final String fil[][] = new String[response.getData().size()][5];
                        int noOfObjects = response.getTotalObjects();
                        filesIterator = response.getCurrentPage().iterator();
                        ii = 0;
                        while (filesIterator.hasNext())
                        {
                            FileInfo file = filesIterator.next();
                            String URL = file.getURL();
                            String publicURL = file.getPublicUrl();
                            Date createdOn = new Date(file.getCreatedOn());
                            String fname = file.getName();
                            String fsize = file.getSize() + "";
                            fil[ii][0] = fname;
                            fil[ii][1] = publicURL;
                            fil[ii][2] = createdOn.toString();
                            fil[ii][4] = fsize;
                            if (fname.endsWith(".txt") || fname.endsWith(".pdf") || fname.endsWith(".java") || fname.endsWith(".doc") || fname.endsWith(".docx") ||
                                    fname.endsWith(".ppt") || fname.endsWith(".pptx") || fname.endsWith(".c") || fname.endsWith(".py") || fname.endsWith(".python") ||
                                    fname.endsWith(".cpp") || fname.endsWith(".zip") || fname.endsWith(".sh") || fname.endsWith(".jpg") || fname.endsWith(".jpeg") || fname.endsWith(".png"))
                            {
                                fil[ii][3] = "0"; // O if file
                            } else if (fname.endsWith(".TXT") || fname.endsWith(".PDF") || fname.endsWith(".SH") || fname.endsWith(".JAVA") || fname.endsWith(".DOC") || fname.endsWith(".DOCX") ||
                                    fname.endsWith(".PPT") || fname.endsWith(".PPTX") || fname.endsWith(".C") || fname.endsWith(".PY") || fname.endsWith(".PYTHON") ||
                                    fname.endsWith(".CPP") || fname.endsWith(".ZIP") || fname.endsWith(".JPG") || fname.endsWith(".JPEG") || fname.endsWith(".PNG"))
                            {
                                fil[ii][3] = "0"; // O if file
                            } else
                            {
                                fil[ii][3] = "1"; //  1 if directory
                            }
                            ii++;
                        }
                        //  setContentView(R.layout.activity_main);
                        final ListView listView1 = (ListView) findViewById(R.id.listView1);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(t, android.R.layout.simple_list_item_1)
                        {

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent){

                                View view = super.getView(position, convertView, parent);


                                TextView ListItemShow = (TextView) view.findViewById(android.R.id.text1);

                                ListItemShow.setTextColor(Color.parseColor("#FFFFFF"));

                                return view;
                            }

                        };
                        for (int k = 0; k < fil.length; k++)
                        {
                            if (fil[k][3].equals("1"))
                                arrayAdapter.add("-> " + fil[k][0].toUpperCase());
                            else
                                arrayAdapter.add(fil[k][0]);
                        }
                        listView1.setAdapter(arrayAdapter);
                        if(flag2==1)
                        {
                            flag2 = 0;
                            pd2.dismiss();
                        }
                        if(noOfObjects==0)
                            Toast.makeText(t,"No files",Toast.LENGTH_LONG).show();
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
                            {
                                String pos = listView1.getItemAtPosition(position) + toString();
                             
                                int ItemIndex = (int) listView1.getItemIdAtPosition(position);
                                if (fil[ItemIndex][3] == "1")
                                {

                                    Intent temp = new Intent("android.intent.action.MPAGE");
                                    temp.putExtra("backendlesspath", (backendlesspath + fil[(int) listView1.getItemIdAtPosition(position)][0] + "/"));
                                    startActivity(temp);
                                }
                                else
                                {
                                    int flag = 0;
                                    String filesize;
                                    float fs = Integer.parseInt(fil[ItemIndex][4]) / 1024;
                                    if (fs >= 1024)
                                    {
                                        fs = fs / 1024;
                                        flag = 1;
                                    }
                                    String tt=""+fs;

                                    tt=tt.substring(0,tt.indexOf('.')+2);
                                    if (flag == 1)
                                    {
                                        filesize = tt + " MB";
                                    } else
                                    {
                                        filesize = tt + " KB";
                                    }

                                    String desc = "Name: " + fil[ItemIndex][0] + "\nSize: " + filesize;

                                    new AlertDialog.Builder(t)
                                            .setTitle("File")
                                            .setMessage(desc)
                                            .setNegativeButton("Cancel", null)
                                            .setPositiveButton("Download", new DialogInterface.OnClickListener()
                                            {
                                                public void onClick(DialogInterface arg0, int arg1)
                                                {
                                                    downfilename = fil[(int) listView1.getItemIdAtPosition(position)][0];
                                                    mProgressDialog = new ProgressDialog(t);
                                                    mProgressDialog.setMessage("Downloading");
                                                    mProgressDialog.setIndeterminate(true);
                                                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                    mProgressDialog.setCancelable(true);
                                                    final DownloadTask downloadTask = new DownloadTask(MainPage.this);
                                                    downloadTask.execute(fil[(int) listView1.getItemIdAtPosition(position)][1]);
                                                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                                                    {
                                                        @Override
                                                        public void onCancel(DialogInterface dialog)
                                                        {
                                                            downloadTask.cancel(true);
                                                        }
                                                    });
                                                }
                                            })
                                            .setNeutralButton("Share", new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    String urll = fil[(int) listView1.getItemIdAtPosition(position)][1];


                                                    urll = urll.substring(64);



                                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                                                    // Creates a new text clip to put on the clipboard
                                                    ClipData clip = ClipData.newPlainText("url", urll);
                                                    clipboard.setPrimaryClip(clip);
                                                    Toast.makeText(t, "Copied to Clip Board", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .create().show();


                                    
                                }
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault)
                    {

                        if((backendlessFault.getMessage().startsWith("Not existing user token"))==false)
                        {
                            if(flag2==1)
                            {
                                flag2 = 0;
                                pd2.dismiss();
                            }
                            Toast.makeText(t, "" + backendlessFault.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent temp5 = new Intent("android.intent.action.MPAGE"); //If page loading failed
                            temp5.putExtra("backendlesspath", backendlesspath);
                            startActivity(temp5);
                            finish();
                        }
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault)
            {
                if(flag2==1)
                {
                    flag2 = 0;
                    pd2.dismiss();
                }
                Toast.makeText(t, "Login Failed", Toast.LENGTH_SHORT).show();
                Intent loginintent3 = new Intent("android.intent.action.LOGIN");
                startActivity(loginintent3);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            //super.onBackPressed();
            finish();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        
        int id = item.getItemId();

      
        Toast.makeText(t,"Currently multiple download not supported",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.createfolder)
        {
            AlertDialog.Builder ad=new AlertDialog.Builder(t);
            ad.setTitle("Folder Name");
            final EditText input = new EditText(MainPage.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            ad.setView(input);
            ad.setNegativeButton("Cancel", null);
            ad.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface arg0, int arg1)
                {
                    foldername=input.getText().toString();
                    if(foldername.contains("/")==true)
                    {
                        Toast.makeText(t,"Folder name should not conatin '/'",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        flagnewfolder=1;
                        foldername=backendlesspath+foldername;

                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, 4455);
                    }
                }
            });
            ad.create().show();

        }
        else if (id == R.id.logout)
        {
            SharedPreferences sharedPref =t.getSharedPreferences("Implement here",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            //editor.putInt(getString(R.string.saved_high_score), newHighScore);
            editor.putString("email1","No value");
            editor.putString("password","No password");
            editor.putString("favPage","No value");
            editor.commit();
            //Intent rintent=new Intent();
            Intent loginintent6 = new Intent("android.intent.action.LOGIN");
            startActivity(loginintent6);
            finish();

             // 2 for login
            // Handle the camera action
        }
        else if (id == R.id.uploadhere)
        {
            flagnewfolder = 0;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, 4455);
        }
        else if (id == R.id.feedBack)
        {
            Intent feed=new Intent("android.intent.action.FEEDBACK");
            startActivity(feed);
        }
        else if(id==R.id.homepage)
        {
            Intent tem=new Intent("android.intent.action.MPAGE");
            tem.putExtra("backendlesspath","/myfiles/");
            //   tem.putExtras(b);
            startActivity(tem);
            finish();
        }
        else if(id==R.id.fav)
        {
            SharedPreferences sharedPref = t.getSharedPreferences("Implement here", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            //editor.putInt(getString(R.string.saved_high_score), newHighScore);
            editor.putString("favPage",backendlesspath);
            editor.commit();
           // Snackbar.make(,"Favourite Set", Snackbar.LENGTH_LONG).show();
            Toast.makeText(t,"Favourite Set",Toast.LENGTH_LONG).show();;
        }
        else if(id==R.id.dwnlink)
        {

            AlertDialog.Builder ad=new AlertDialog.Builder(t);
            ad.setTitle("Link");
            final EditText input = new EditText(MainPage.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            ad.setView(input);
            ad.setNegativeButton("Cancel", null);
            ad.setPositiveButton("Download", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface arg0, int arg1)
                {

                    String filepat="Implement here"+input.getText().toString();


                    downfilename=filepat.substring((filepat.lastIndexOf('/'))+1);
                    mProgressDialog = new ProgressDialog(t);
                    mProgressDialog.setMessage("Downloading");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCancelable(true);
                    final DownloadTask downloadTask = new DownloadTask(MainPage.this);
                    downloadTask.execute(filepat);
                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface dialog)
                        {
                            downloadTask.cancel(true);
                        }
                    });

                }
             });
            ad.create().show();
        }
        else if(id==R.id.refresh)
        {
            Intent temp7 = new Intent("android.intent.action.MPAGE"); //If page loading failed
            temp7.putExtra("backendlesspath", backendlesspath);
            startActivity(temp7);
            finish();
        }
      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 4455:   // for file upload
                if (resultCode == RESULT_OK)
                {
                    //File tem=new File(data.getData().getPath());
                   // String fname=tem.getName();
                   // Log.i("fname",fname);
                    String p=data.getData().getPath().substring(data.getData().getPath().indexOf(':')+1,data.getData().getPath().length());
                    finalfile=new File("/storage/emulated/0/"+p);
                    String fname=finalfile.getName();
                    if (fname.endsWith(".txt") || fname.endsWith(".pdf") || fname.endsWith(".java") || fname.endsWith(".doc") || fname.endsWith(".docx") || fname.endsWith(".sh") ||
                            fname.endsWith(".ppt") || fname.endsWith(".pptx") || fname.endsWith(".c") || fname.endsWith(".py") ||fname.endsWith(".python") ||
                            fname.endsWith(".cpp") || fname.endsWith(".zip") || fname.endsWith(".jpg") || fname.endsWith(".jpeg") || fname.endsWith(".png") ||
                            fname.endsWith(".TXT") || fname.endsWith(".PDF") || fname.endsWith(".JAVA") || fname.endsWith(".DOC") || fname.endsWith(".DOCX") ||
                            fname.endsWith(".PPT") || fname.endsWith(".PPTX") || fname.endsWith(".C") || fname.endsWith(".PY") ||fname.endsWith(".PYTHON") ||
                            fname.endsWith(".CPP") || fname.endsWith(".ZIP") || fname.endsWith(".JPG") || fname.endsWith(".JPEG") || fname.endsWith(".PNG")
                            || fname.endsWith(".SH") )
                    {

                        if((ContextCompat.checkSelfPermission(t,WRITE_EXTERNAL_STORAGE))!=PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(MainPage.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},106);
                        }
                        else
                        {
                            uProgressBar = new ProgressDialog(t);
                            uProgressBar.setMessage("Uploading");
                            uProgressBar.setIndeterminate(false);
                            uProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            uProgressBar.setCancelable(false);
                            final UploadTask upt = new UploadTask(MainPage.this);
                            String x[] = {backendlesspath,username1,password24};
                            String y[]={foldername,username1,password24};

                            if(flagnewfolder==1)
                            {
                                upt.execute(y);
                            }
                            else
                            {
                                upt.execute(x);
                            }
                                uProgressBar.setOnCancelListener(new DialogInterface.OnCancelListener()
                            {
                                @Override
                                public void onCancel(DialogInterface dialog)
                                {
                                    upt.cancel(true);

                                    String deletepath;
                                    if(flagnewfolder==1)
                                    {
                                        deletepath=foldername+"/"+finalfile.getName();
                                    }
                                    else
                                    {
                                        deletepath=backendlesspath+finalfile.getName();
                                    }

                                    Backendless.Files.remove( deletepath, new AsyncCallback<Void>()
                                    {
                                        @Override
                                        public void handleResponse(Void aVoid)
                                        {
                                            Toast.makeText(t,"Cancelled",Toast.LENGTH_LONG);
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault backendlessFault)
                                        {

                                        }
                                    });
                                }
                            });
                        }
                    }
                    else
                    {
                        Toast.makeText(t,"Selected file format not supported\n" +
                                "OR\ntry by selecting through internal or sdcard",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 106:
              
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    uProgressBar = new ProgressDialog(t);
                    uProgressBar.setMessage("Uploading");
                    uProgressBar.setIndeterminate(false);
                    uProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    uProgressBar.setCancelable(false);
                    final UploadTask upt = new UploadTask(MainPage.this);
                    String x[] = {backendlesspath,username1,password24};
                    String y[]={foldername,username1,password24};
                    if(flagnewfolder==1)
                        upt.execute(y);
                    else
                        upt.execute(x);
                    uProgressBar.setOnCancelListener(new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface dialog)
                        {
                            upt.cancel(true);

                            String deletepath;
                            if(flagnewfolder==1)
                            {
                                deletepath=foldername+"/"+finalfile.getName();
                            }
                            else
                            {
                                deletepath=backendlesspath+finalfile.getName();
                            }

                            Backendless.Files.remove( deletepath, new AsyncCallback<Void>()
                            {
                                @Override
                                public void handleResponse(Void aVoid)
                                {
                                    Toast.makeText(t,"Cancelled",Toast.LENGTH_LONG);
                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault)
                                {

                                }
                            });
                        }
                    });

                }
                else
                {
                    finish();
                }
                return;


           
        }
    }

    private class DownloadTask extends AsyncTask<String, Integer, String>
    {

        private Context context;
        String filedest;
        String temp4;

        public DownloadTask(Context context)
        {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... sUrl)
        {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();


                if(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).exists())
                {
                  
                    filedest=Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()+"/"+downfilename;
                }
                else
                {
                    filedest=getExternalStorageDirectory().getPath()+"/"+downfilename;
                }

               

                temp4=filedest.substring(0,filedest.lastIndexOf('/')+1);
                try
                {
                   // output = new FileOutputStream(savefiledesc.getPath());
                    output = new FileOutputStream(filedest);
                }
                catch (IOException ie)
                {

                }

                byte data[] = new byte[fileLength];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
               
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                   
                    if (fileLength > 0) // only if total length is known
                    {
                       
                        if(mWakeLock.isHeld())
                        {
                          
                            publishProgress((int) (total * 100 / fileLength));
                        }
                            //}
                    }
                    output.write(data, 0, count);
                }
            }
            catch (Exception e)
            {
                return e.toString();
            }
            finally
            {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                }
                catch (IOException ignored)
                {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            df=1;
           
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();

        }
        @Override
        protected void onProgressUpdate(Integer... progress)
        {
            super.onProgressUpdate(progress);

          
            if(mWakeLock.isHeld())
            {
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgress(progress[0]);
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            df=0;
            if(mWakeLock.isHeld() && df==0)
            {
                mWakeLock.release();
                mProgressDialog.dismiss();
                if (result != null)
                    Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(context, "File saved at\n"+temp4, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private class UploadTask extends AsyncTask<String,Integer,Void>
    {
        final private Context context;
     
        String re=null;
        String upBackendlesspath;
        String uname,pass;
        public UploadTask(Context context)
        {
            this.context = context;
        }
        @Override
        protected Void doInBackground(final String... sUrl)
        {
           
            upBackendlesspath=sUrl[0];
            uname=sUrl[1];
            pass=sUrl[2];
        
           Backendless.initApp(t, "Implement here", "Implement here", "Implement here");
            try
            {
                Backendless.UserService.login(uname, pass, new AsyncCallback<BackendlessUser>()
                {
                    public void handleResponse(BackendlessUser user)
                    {


                        Backendless.Files.upload(finalfile, upBackendlesspath, false, new AsyncCallback<BackendlessFile>()
                        {
                            @Override
                            public void handleResponse(BackendlessFile uploadedFile)
                            {

                                Toast.makeText(t,"File uploaded\nPlease refresh page",Toast.LENGTH_LONG).show();
                                if (uf == 1)
                                {
                                    uf = 0;
                                    uWakeLock.release();
                                    uProgressBar.dismiss();
                                }

                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault)
                            {

                                Toast.makeText(t, "" + backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                                SharedPreferences sharedPref = t.getSharedPreferences("Implement here", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();

                                editor.putString("err", ""+finalfile.getPath());
                                editor.commit();
                                // re = backendlessFault.getMessage().toString();
                                if (uf == 1)
                                {
                                    uf = 0;
                                    uWakeLock.release();
                                    uProgressBar.dismiss();
                                }
                            }

                        });


                       
                    }

                    public void handleFault(BackendlessFault fault)
                    {

                        // login failed, to get the error code call fault.getCode()

                        Toast.makeText(t, fault.getMessage(), Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPref = t.getSharedPreferences("Implement here", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        editor.putString("err", ""+finalfile.getPath());
                        editor.commit();
                        //setContentView(R.layout.login);
                        if (uf == 1)
                        {
                            uf = 0;
                            uWakeLock.release();
                            uProgressBar.dismiss();
                        }

                        //re=fault.getMessage().toString();
                    }
                });
            }
            catch (Exception ie)
            {
                Toast.makeText(t,ie.getMessage(),Toast.LENGTH_LONG).show();
            }
            finally
            {
                return null;
            }

        }
        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();
           
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            uWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            uWakeLock.acquire();
            uProgressBar.setIndeterminate(true);
            uProgressBar.show();
            uf=1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress)
        {

            super.onProgressUpdate(progress);

        }

        @Override
        protected void onPostExecute(Void aa)
        {


        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();

            if (df==1)
            {
                mWakeLock.release();
                mProgressDialog.dismiss();
            }
            if (uf==1)
            {
                uWakeLock.release();
                uProgressBar.dismiss();
            }
        if(flag2==1)
            pd2.dismiss();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
       if(df==1)
        {
            mWakeLock.acquire();
            mProgressDialog.show();
        }
        if(uf==1)
        {
            uWakeLock.acquire();
            uProgressBar.show();
        }
        if(flag2==1)
            pd2.show();
    }

}
