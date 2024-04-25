package com.zybooks.nutrifittracker.repo;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zybooks.nutrifittracker.model.Exercise;
import com.zybooks.nutrifittracker.model.Workout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoutineFetcher {

    public interface OnStudyDataReceivedListener {
        void onSubjectsReceived(List<Workout> workoutList);
        void onQuestionsReceived(Workout workout, List<Exercise> exerciseList);
        void onErrorResponse(VolleyError error);
    }

    private final String WEBAPI_BASE_URL = "https://wp.zybooks.com/study-helper.php";
    private final String TAG = "StudyFetcher";

    private final RequestQueue mRequestQueue;

    public RoutineFetcher(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void fetchSubjects(final OnStudyDataReceivedListener listener) {

        String url = Uri.parse(WEBAPI_BASE_URL).buildUpon()
                .appendQueryParameter("type", "subjects").build().toString();

        // Request all subjects
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> listener.onSubjectsReceived(jsonToSubjects(response)),
                listener::onErrorResponse);

        mRequestQueue.add(request);
    }

    private List<Workout> jsonToSubjects(JSONObject json) {

        // Create a list of subjects
        List<Workout> workoutList = new ArrayList<>();

        try {
            JSONArray subjectArray = json.getJSONArray("subjects");

            for (int i = 0; i < subjectArray.length(); i++) {
                JSONObject subjectObj = subjectArray.getJSONObject(i);

                Workout workout = new Workout(subjectObj.getString("subject"));
                workout.setUpdateTime(subjectObj.getLong("updatetime"));
                workoutList.add(workout);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Field missing in the JSON data: " + e.getMessage());
        }

        return workoutList;
    }

    public void fetchQuestions(final Workout workout, final OnStudyDataReceivedListener listener) {

        String url = Uri.parse(WEBAPI_BASE_URL).buildUpon()
                .appendQueryParameter("type", "questions")
                .appendQueryParameter("subject", workout.getText())
                .build().toString();

        // Request questions for this subject
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> listener.onQuestionsReceived(workout, jsonToQuestions(response)),
                listener::onErrorResponse);

        mRequestQueue.add(jsObjRequest);
    }

    private List<Exercise> jsonToQuestions(JSONObject json) {

        // Create a list of questions
        List<Exercise> exerciseList = new ArrayList<>();

        try {
            JSONArray questionArray = json.getJSONArray("questions");

            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject questionObj = questionArray.getJSONObject(i);

                Exercise exercise = new Exercise();
                exercise.setText(questionObj.getString("question"));
                exercise.setAnswer(questionObj.getString("answer"));
                exercise.setSubjectId(0);
                exerciseList.add(exercise);
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Field missing in the JSON data: " + e.getMessage());
        }

        return exerciseList;
    }
}