package ms.ac.jbnu.se.mschoi.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import ms.ac.jbnu.se.mschoi.R;

public class MainActivity extends Activity {
    private MediaPlayer mp;
    private Button favoriteBookButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        favoriteBookButton =(Button)findViewById(R.id.favoriteBookButton);
        favoriteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FavoriteBookActivity.class);
                startActivity(intent);
            }
        });

        mp = MediaPlayer.create(this, R.raw.bgm1);
        mp.setLooping(true);
        mp.start();//음악재생
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, BookListActivity.class);
        startActivity(intent);
    }

}
