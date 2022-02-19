package com.example.quicknews;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NewsService {
    public void news(Activity activity, @NonNull TextToSpeech textToSpeech) {

        if(textToSpeech.isSpeaking())
            return;
        OkHttpClient client = new OkHttpClient();

        Request get = new Request.Builder()
                .url("https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=c5e2c8bbb8ee44cdb0e1e95113409816")
                .build();

        client.newCall(get).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if(textToSpeech.isSpeaking())
                        return;
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    final JSONObject response1 = new JSONObject(response.body().string());
                    JSONArray myResponse = response1.getJSONArray("articles");
                    for(int i =0; i< myResponse.length(); i++)
                    {
                        JSONObject jsonObject = myResponse.getJSONObject(i);
                        textToSpeech.speak("news "+(i+1),TextToSpeech.QUEUE_ADD, null);
                        textToSpeech.speak(jsonObject.getString("description"),TextToSpeech.QUEUE_ADD, null);

                    }
                    Log.i("data", responseBody.string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
