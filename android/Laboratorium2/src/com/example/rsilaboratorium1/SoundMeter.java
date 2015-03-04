package com.example.rsilaboratorium1;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class SoundMeter {
	 // This file is used to record voice
    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mListener = null;
    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;
    private String outputFile = Environment.getExternalStorageDirectory().

			getAbsolutePath() + "/Recording.3gpp"; 
    
    public void start() {
        if (mListener == null) {
                 
                    mListener = new MediaRecorder();
                    mListener.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                    mListener.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mListener.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    mListener.setOutputFile("/dev/null"); 
                     
                    try {
                        mListener.prepare();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                     
                   mListener.start();
                   mEMA = 0.0;
            }
    }
     
    public void stop() {
            if (mListener != null) {
                    mListener.stop();       
                    mListener.release();
                    mListener = null;
            }
    }
    
    public void recording(){
    	try {
    		mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.setOutputFile(outputFile);
			mRecorder.prepare();
			mRecorder.start();

		} catch (IllegalStateException e) {
			e.printStackTrace();

		} catch (IOException e) {			
			e.printStackTrace();
		} 
    }
    
    public void stopRecording(){
    	mRecorder.stop();
    }
    
    public double getAmplitude() {
            if (mListener != null)
                    return  (mListener.getMaxAmplitude()/2700.0);
            else
                    return 0;

    }

    public double getAmplitudeEMA() {
            double amp = getAmplitude();
            mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
            return mEMA;
    }
}
