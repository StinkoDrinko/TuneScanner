package com.anton.tunescanner;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.media.AudioRecord;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class SoundReader {
    private static final int RECORDER_SAMPLERATE=44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    private AudioRecord record= null;
    private boolean isRecording = false;
    private Thread recordingThread=null;
    //private MediaRecorder recorder;
    private final String path;

private Activity context;

    public void startRecording() {
        record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufferSize);

        record.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    public void stopRecording() {
        // stops the recording activity
        if (null != record) {
            isRecording = false;
            record.stop();
            record.release();
            record = null;
            recordingThread = null;
        }
    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte

        short sData[] = new short[bufferSize];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            record.read(sData, 0, bufferSize);

            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, bufferSize);
            } catch (IOException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        try {
            os.close();

        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    public SoundReader(String path, Activity context){
        this.path = sanitizePath(path);
        this.context = context;
    }
    private String sanitizePath(String path){
        if(!path.startsWith("/")){
            path="/" +path;
        }
        if (!path.contains(".")){
            path += ".wav";
        }
        return path;

    }
    //public void start() throws IOException{
    //    //String state = android.os.Environment.getExternalStorageState();
    //    //if(!state.equals(Environment.MEDIA_MOUNTED)){
    //    //    throw new IOException("No Sd Card Mounted. It is " + state +".");
    //    //}
    //    File directory =new File(path).getParentFile();
    //    if (directory != null && !directory.exists() && !directory.mkdir()){
    //        throw new IOException("Path to file could not be created.");
    //    }
    //    if(record == null)
    //        record = new AudioRecord();
    //    record.setAudioSource(MediaRecorder.AudioSource.MIC);
    //    record.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    //    record.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    //    record.setOutputFile(path);
    //    record.prepare();
    //    record.start();
    //}
    //public void stop() {
    //    if (recorder!= null) {//
//
//            recorder.stop();
//            recorder.release();
//            recorder = null;
//        }
 //   }






}
