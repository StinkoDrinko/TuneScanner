package com.anton.tunescanner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SoundReader sound;
    public void Listener (View view){

        try {
            sound.start();

        }
        catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();


        }


    }
public void Stop(View view){
        try{
            sound.stop();
        }
        catch (IOException f){
            Toast.makeText(this,f.getMessage(),Toast.LENGTH_LONG).show();
        }
}




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sound = new SoundReader(getFilesDir().getAbsolutePath()+ "temp.3gp");

    }
}
