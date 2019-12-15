package com.anton.tunescanner;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class SoundReader {
    final MediaRecorder recorder = new MediaRecorder();
    final String path;

    public SoundReader(String path){
        this.path = sanitizePath(path);
    }
    private String sanitizePath(String path){
        if(!path.startsWith("/")){
            path="/" +path;
        }
        if (!path.contains(".")){
            path += ".3gp";
        }
        return path;

    }
    public void start() throws IOException{
        //String state = android.os.Environment.getExternalStorageState();
        //if(!state.equals(Environment.MEDIA_MOUNTED)){
        //    throw new IOException("No Sd Card Mounted. It is " + state +".");
        //}
        File directory =new File(path).getParentFile();
        if (!directory.exists()&& !directory.mkdir()){
            throw new IOException("Path to file could not be created.");
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }
    public void stop() throws IOException{
        recorder.stop();
        recorder.release();

    }






}
