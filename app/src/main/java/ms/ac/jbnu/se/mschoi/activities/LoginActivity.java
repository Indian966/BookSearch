package ms.ac.jbnu.se.mschoi.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import ms.ac.jbnu.se.mschoi.R;
import ms.ac.jbnu.se.mschoi.net.AsyncHttpTask;

public class LoginActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText pwText;
    private Button Loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button RegisterButton = (Button) findViewById(R.id.RegisterButton);
        Button loginButton = (Button) findViewById(R.id.loginButton);

        final EditText email_et = findViewById(R.id.emailText);
        final EditText password_et = findViewById(R.id.pwText);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent welcomeIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(welcomeIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAct(email_et.getText().toString(), password_et.getText().toString());
            }
        });

        emailText = (EditText) findViewById(R.id.emailText);
        pwText = (EditText) findViewById(R.id.pwText);


    }

    public void loginAct(String email, String password) {

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");
        Paramname.add("email");
        Paramname.add("password");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("account_auth");
        Paramvalue.add(email);
        Paramvalue.add(password);






        new AsyncHttpTask(LoginActivity.this, "https://unopenedbox.com/develop/bookamt/api.php", mHandler, Paramname,
                Paramvalue, null, 1, 0);
    }


    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout


            if (msg.what == 1) {

               String result = msg.obj.toString();



               Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();


            }


        }
    };

}
