package student_info.student_info;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import student_info.student_info.Service_handler.Constant;
import student_info.student_info.Service_handler.ServiceHandler;

public class Sign_up extends Activity {
    Button Btn_signup;
    Boolean Email_matcher;
    EditText Edt_Txt_Email, Edt_txt_password, Edt_txt_name, Edt_txt_confirm_password;
    int length;
    private ProgressDialog pDialog;
    String Str_Status = "no_satus";
    String Str_message = "no_satus";
    String Str_email, Str_password, Str_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);
        Btn_signup = (Button) findViewById(R.id.button3);
        Edt_Txt_Email = (EditText) findViewById(R.id.editText3);
        Edt_txt_password = (EditText) findViewById(R.id.editText7);
        Edt_txt_name = (EditText) findViewById(R.id.editText5);
        Edt_txt_confirm_password = (EditText) findViewById(R.id.editText6);
        Btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email_matcher = emailValidator(Edt_Txt_Email.getText().toString());
                String Str_length = Edt_txt_password.getText().toString();
                length = Str_length.length();
                if (Edt_txt_name.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_txt_name.startAnimation(anm);
                    Edt_txt_name.setError("Enter Name");
                } else if (Edt_Txt_Email.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_Txt_Email.startAnimation(anm);
                    Edt_Txt_Email.setError("Enter Email");

                } else if (Email_matcher == false) {
                    Animation anm = Shake_Animation();
                    Edt_Txt_Email.startAnimation(anm);
                    Edt_Txt_Email.setError("Enter the valid Email");
                } else if (Edt_txt_password.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_txt_password.startAnimation(anm);
                    Edt_txt_password.setError("Enter Password");

                } else if (length < 7) {
                    Animation anm = Shake_Animation();
                    Edt_txt_password.startAnimation(anm);
                    Edt_txt_password.setError("Password should be more than 6 character");
                } else if (Edt_txt_confirm_password.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Edt_txt_confirm_password.startAnimation(anm);
                    Edt_txt_confirm_password.setError("Confirm Password");
                } else if (!Edt_txt_confirm_password.getText().toString().contentEquals(Edt_txt_password.getText().toString())) {
                    Animation anm = Shake_Animation();
                    Edt_txt_confirm_password.startAnimation(anm);
                    Edt_txt_confirm_password.setError("Password do not match");
                } else {
                    Str_email = Edt_Txt_Email.getText().toString();
                    Str_password = Edt_txt_confirm_password.getText().toString();
                    Str_name = Edt_txt_name.getText().toString();
                    new Register().execute();
                }
            }
        });
    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        // final String EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private class Register extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Sign_up.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("email", Str_email));
            nameValuePairs.add(new BasicNameValuePair("password", Str_password));
            nameValuePairs.add(new BasicNameValuePair("device_id", "android12345678"));
            nameValuePairs.add(new BasicNameValuePair("os_type", "Android"));
            nameValuePairs.add(new BasicNameValuePair("os_version", "6.0"));
            nameValuePairs.add(new BasicNameValuePair("hardware", "android12345678"));
            nameValuePairs.add(new BasicNameValuePair("app_version", "1.0"));
            nameValuePairs.add(new BasicNameValuePair("name", Str_name));


            String jsonStr = sh.makeServiceCall(Constant.SIGN_UP,
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
                    Str_message = jsonObj.getString("errors");
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
                Toast.makeText(Sign_up.this, "Register Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Sign_up.this, Home_screen.class);
                startActivity(i);

                finish();
                Sign_up.this.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            } else if (Str_Status.contentEquals("false")) {
                Toast.makeText(Sign_up.this, Str_message, Toast.LENGTH_LONG).show();
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
