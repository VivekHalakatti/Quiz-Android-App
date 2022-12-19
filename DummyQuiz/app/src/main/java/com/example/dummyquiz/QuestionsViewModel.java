package com.example.dummyquiz;

import android.app.Application;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionsViewModel extends AndroidViewModel implements Response.Listener<String>, Response.ErrorListener {

    private static final String API_URL = "https://raw.githubusercontent.com/tVishal96/sample-english-mcqs/master/db.json";
    private static final String RESPONSE_ENTRY_KEY = "questions";
    private static final String RESPONSE_QUESTION_KEY = "question";
    //    private static final String RESPONSE_ID = "id";
    private static final String RESPONSE_OPTIONS = "options";
    private static final String RESPONSE_CORRECT_OPTION = "correct_option";


    private MutableLiveData<List<Datamodel>> dataholder = new MutableLiveData<>();
    private MutableLiveData<RequestStatus> requestStatusLiveData = new MutableLiveData<>();
    private RequestQueue queue;

    public QuestionsViewModel(@NonNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(application);
        requestStatusLiveData.postValue(RequestStatus.IN_PROGRESS);
        fetchQuestions();
    }

    public void refetchQuestions() {
        requestStatusLiveData.postValue(RequestStatus.IN_PROGRESS);
        fetchQuestions();
    }

    public LiveData<List<Datamodel>> getQuestionData() {
        return dataholder;
    }

    public LiveData<RequestStatus> getRequestStatusLiveData() {
        return requestStatusLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        requestStatusLiveData.postValue(RequestStatus.FAILED);
    }

    private void fetchQuestions() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL, this, this);
        queue.add(stringRequest);
    }

    @Override
    public void onResponse(String response) {
        try {
            List<Datamodel> QuestionDatalist = parseResponse(response);
            dataholder.postValue(QuestionDatalist);
            requestStatusLiveData.postValue(RequestStatus.SUCCEEDED);
        } catch (JSONException e) {
            e.printStackTrace();
            requestStatusLiveData.postValue(RequestStatus.FAILED);
        }
    }

    private List<Datamodel> parseResponse(String response) throws JSONException {
        List<Datamodel> models = new ArrayList<>();
        JSONObject res = new JSONObject(response);
        JSONArray entries = res.optJSONArray(RESPONSE_ENTRY_KEY);
        if (entries == null) {
            return models;
        }

        for (int i = 0; i < entries.length(); i++) {
            JSONObject obj = (JSONObject) entries.get(i);
            String question = obj.optString(RESPONSE_QUESTION_KEY);
//          String id = obj.optString(RESPONSE_ID);
            JSONArray options_Array = obj.optJSONArray(RESPONSE_OPTIONS);
            String optionsall = obj.optString(RESPONSE_OPTIONS);
            int correct_opt = Integer.parseInt(obj.optString(RESPONSE_CORRECT_OPTION));
            Boolean bookm = false;
            Boolean ans = false;
            int buttonid = -1;
            List<String> options_arr = new ArrayList<>();

            //to extract options and display them infront of radiobutton.
            for (int j = 0; j < options_Array.length(); j++) {
                options_arr.add(options_Array.getString(j));
            }

            //To store the correct answer before fetching.
            String exact_ans = options_arr.get(correct_opt);

            //to shuffle the options everytime.
            Collections.shuffle(options_arr);
            Datamodel model = new Datamodel(question, correct_opt, bookm, ans, options_arr, optionsall,buttonid,exact_ans);
            models.add(model);
        }

        //to shuffle the rows/questions in recycler view everytime.
        Collections.shuffle(models);
        return models;
    }

    public enum RequestStatus {
        //Progress of API
        IN_PROGRESS,

        //If API request fails.
        FAILED,

        //Upon API request completion.
        SUCCEEDED
    }
}
