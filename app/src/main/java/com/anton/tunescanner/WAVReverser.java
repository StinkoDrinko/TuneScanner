package com.anton.tunescanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class WAVReverser {
    public WAVReverser(File inputFile) throws IllegalArgumentException, IOException {
        if(inputFile == null || !isWavFile(inputFile)) {
            throw new IllegalArgumentException("WAVReverse(): File must be in wav format.");
        }

        wavFile = inputFile;
        getWavHeader();
    }

    private boolean isWavFile(File file) {
        boolean isWavFile = false;
        String filename = file.getName();
        String[] fileTokens = filename.split("\\.");
        for(String token : fileTokens) {
            if(token.equalsIgnoreCase("wav")) {
                isWavFile = true;
                break;
            }
        }

        return isWavFile;
    }

    private void getWavHeader() throws IOException {
        wavFileBytes = Files.readAllBytes(wavFile.toPath());
        if(wavFileBytes.length == 0)
            throw new IOException("getWavHeader(): Could not read the WAV file.");

        wavHeader = new byte[44];           //there are 44 bytes in the header of the WAV File
        System.arraycopy(wavFileBytes, 0, wavHeader, 0, 44);        //make sure wav file is not corrupt?

    }

    private File reverseWavFile() throws Exception {
        if(wavFile == null)
            throw new IllegalStateException("reverseWavFile(): No file to reverse!");

        byte[][] wavData = getWavFileAudio();
        if (wavData == null || wavData.length == 0)
            return writeWavFile(new byte[0]);

        int numSamples = wavData.length;
        int numBytesPerSample = wavData[0].length;
        byte[][] reverseWavData = new byte[numSamples][numBytesPerSample];

        for(int byteIdx = 0; byteIdx < wavData.length; byteIdx++) {
            reverseWavData[byteIdx / numBytesPerSample][byteIdx % numBytesPerSample] =
                    wavData[wavData.length - (byteIdx/numBytesPerSample) - 1][byteIdx % numBytesPerSample];
        }

        byte[] reverseWavFileBytes = new byte[wavFileBytes.length];
        for(int byteIdx = 0; byteIdx < reverseWavFileBytes.length; byteIdx++) {
            reverseWavFileBytes[byteIdx] = reverseWavData[byteIdx / numBytesPerSample][byteIdx % numBytesPerSample];
        }

        return writeWavFile(reverseWavFileBytes);
    }

    private byte[][] getWavFileAudio() throws IllegalStateException {
        if(wavHeader == null || wavHeader.length == 0)
            throw new IllegalStateException("getWavFileAudio: Wav File Header not found!");

        int bytesPerSample = ((wavHeader[37] & 0xff) <<  8  ) |  (wavHeader[36] & 0xff        );
        int audioByteSize =  ((wavHeader[43] & 0xff) <<  24 ) | ((wavHeader[42] & 0xff) << 16 )  |
                              ((wavHeader[41] & 0xff) <<  8  ) |  (wavHeader[40] & 0xff        );
        int numSamples = audioByteSize / bytesPerSample;

        byte[][] wavData = new byte[numSamples][bytesPerSample];

        for(int byteIdx = 0; byteIdx < wavFileBytes.length; byteIdx++) {
            wavData[byteIdx / bytesPerSample][byteIdx % bytesPerSample] = wavFileBytes[byteIdx];
        }

        return wavData;
    }

    private File writeWavFile(byte[] wavData) throws IOException {
        String path = Paths.get(wavFile.getAbsolutePath()).getParent().getFileName().toString() + "reverse.wav";
        Files.write(Paths.get(path), wavHeader);
        Files.write(Paths.get(path), wavFileBytes);
        return new File(path);
    }

    private byte[] wavFileBytes;
    private byte[] wavHeader;
    private File wavFile;
}
