package com.anton.tunescanner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SoundReader sound;
    private SoundPlayer player;

    public void StartRecord (View view){
        try {
            sound.start();
        }
        catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void StopRecord(View view){
        sound.stop();
    }
    public void StartPlayBack(View view){
        try{
            File file = new File(getFilesDir().getAbsolutePath()+ "temp.3gp");
            player = new SoundPlayer(file);
            player.startPlayback();
        }
        catch (IOException g){
            Toast.makeText(this,g.getMessage(),Toast.LENGTH_LONG).show();
        }
        catch (IllegalArgumentException g){
            Toast.makeText(this,g.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void StopPlayback (View view){
        if (player!=null) {
            player.stopPlayback();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sound = new SoundReader(getFilesDir().getAbsolutePath()+ "temp.3gp");

    }
}
