package kr.ac.jbnu.se.stkim.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import kr.ac.jbnu.se.stkim.R;

public class MainMenuActivity extends Activity {
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(getBaseContext(), R.raw.Mattia_Vlad_Morleo___Sinless);
        mp.setLooping(true);
        mp.start();

    }
}
