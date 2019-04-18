package ms.ac.jbnu.se.mschoi.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ms.ac.jbnu.se.mschoi.R;
import ms.ac.jbnu.se.mschoi.net.AsyncHttpTask;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


Button summitbutton = findViewById(R.id.registerButton);
        final EditText email_et = findViewById(R.id.emailText);
        final EditText password_et = findViewById(R.id.pwText);
        summitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              registerAct(email_et.getText().toString(), password_et.getText().toString());
            }
        });



    }

    public void registerAct(String email, String password) {

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");
        Paramname.add("email");
        Paramname.add("password");
        Paramname.add("name_2");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("account_sign_up");
        Paramvalue.add(email);
        Paramvalue.add(password);
        Paramvalue.add("김순태");






        new AsyncHttpTask(RegisterActivity.this, "https://unopenedbox.com/develop/bookamt/api.php", mHandler, Paramname,
                Paramvalue, null, 1, 0);
    }


    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout


            if (msg.what == 1) {

                String result = msg.obj.toString();

               // Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG).show();
                Toast.makeText(RegisterActivity.this, "회원가입 되었습니다.", Toast.LENGTH_LONG).show();
                finish();

            }


        }
    };
}
