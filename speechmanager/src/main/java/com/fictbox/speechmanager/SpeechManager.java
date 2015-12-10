package com.fictbox.speechmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import java.util.ArrayList;

/**
 * Created by osamu on 7/23/15.
 */
public class SpeechManager implements RecognitionListener {

    private static final String TAG = "MercadoPlugins";

    //----------------------------
    // Singleton
    //----------------------------
    private static SpeechManager instance = new SpeechManager();
    public static SpeechManager getInstance() {
        return instance;
    }
    private SpeechManager() {}

    //----------------------------
    // Fields
    //----------------------------
    private Context context;
    private SpeechRecognizer speechRecognizer;

    //----------------------------
    // Public methods
    //----------------------------

    /**
     * Initialize SpeechRecognizer which require app context.
     * @param context
     */
    public void initSpeechRecognizer(final Context context) {
        Log.d(TAG, "initSpeechRecognizer START");

        this.context = context;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // メインスレッド上で実行
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
                speechRecognizer.setRecognitionListener(SpeechManager.this);
                Log.d(TAG, "initSpeechRecognizer END");
            }
        });
    }

    /**
     * Starts listening users voice.
     * This singleton receive the recognizer result and return it.
     */
    public void startListening() {
        Log.d(TAG, "--- startListening ---");
        if (context == null) {
            Log.e(TAG, "startListeningしようとしたけどcontextがnull");
            return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // メインスレッド上で実行
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
                speechRecognizer.startListening(intent);
            }
        });
    }

    //----------------------------
    // RecognitionListener implements
    //----------------------------
    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "onError, " + error);
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults");
        ArrayList<String> recData = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(recData.size() > 0) {
            UnityPlayer.UnitySendMessage("SpeechManager", "OnReceiveResult", recData.get(0));
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent");
    }
}
