package kr.ac.jbnu.se.stkim.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;


import kr.ac.jbnu.se.stkim.R;

public class MainActivity extends Activity {
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.bgm1);
        mp.setLooping(true);
        mp.start();//음악재생
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, BookListActivity.class);
        startActivity(intent);
    }

}
