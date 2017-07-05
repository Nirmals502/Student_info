package student_info.student_info;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class Splash_screen extends Activity {
    Button Btn_Get_started;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash_screen.this, Login_screen.class);
                startActivity(i);

                finish();
                Splash_screen.this.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        }, SPLASH_TIME_OUT);
    }


}




