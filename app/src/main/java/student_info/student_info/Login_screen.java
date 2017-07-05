package student_info.student_info;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import student_info.student_info.Service_handler.Constant;
import student_info.student_info.Service_handler.ServiceHandler;

public class Login_screen extends Activity {
    Button Btn_login, Btn_signup;
    EditText Edt_Txt_username, Edt_txt_password;
    private ProgressDialog pDialog;
    String Str_Status = "no_satus";
    String Str_message = "no_satus";
    String Str_email,Str_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_screen);
        Btn_login = (Button) findViewById(R.id.button4);
        Btn_signup = (Button) findViewById(R.id.button3);
        Edt_Txt_username = (EditText) findViewById(R.id.editText3);
        Edt_txt_password = (EditText) findViewById(R.id.editText4);
        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edt_Txt_username.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_Txt_username.startAnimation(anm);
                    Edt_Txt_username.setError("Username should not be empty");
                } else if (Edt_txt_password.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_txt_password.startAnimation(anm);
                    Edt_txt_password.setError("Enter the Password");
                } else {
                    Str_email = Edt_Txt_username.getText().toString();
                    Str_password = Edt_txt_password.getText().toString();
                    new Login().execute();
                }
            }
        });
        Btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_screen.this, Sign_up.class);
                startActivity(i);

                finish();
                Login_screen.this.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        });

    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    private class Login extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Login_screen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("email", Str_email));
            nameValuePairs.add(new BasicNameValuePair("password", Str_password));
            nameValuePairs.add(new BasicNameValuePair("device_id", "android12345678"));
            nameValuePairs.add(new BasicNameValuePair("os_type", "Android"));
            nameValuePairs.add(new BasicNameValuePair("os_version", "6.0"));
            nameValuePairs.add(new BasicNameValuePair("hardware", "android12345678"));
            nameValuePairs.add(new BasicNameValuePair("app_version", "1.0"));


            String jsonStr = sh.makeServiceCall(Constant.LOGIN,
                    ServiceHandler.POST, nameValuePairs);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Getting JSON Array node
                // JSONArray array1 = null;
                try {
                    Str_Status = jsonObj.getString("status");
                    Str_message = jsonObj.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            pDialog.dismiss();

            if (Str_Status.contentEquals("true")) {
                Toast.makeText(Login_screen.this, "Login Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Login_screen.this, Home_screen.class);
                startActivity(i);

                finish();
                Login_screen.this.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            } else if (Str_Status.contentEquals("false")) {
                Toast.makeText(Login_screen.this, Str_message, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
