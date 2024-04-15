package com.zybooks.nutrifittracker.repo;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

    public interface OnRoutineDataReceivedListener {
        void onWorkoutsReceived(List<Workout> workoutList);
        void onExercisesReceived(Workout workout, List<Exercise> exerciseList);
        void onErrorResponse(VolleyError error);
    }

    private final String WEBAPI_BASE_URL = "https://yourapi.com/routine-helper.php";
    private final String TAG = "RoutineFetcher";

    private final RequestQueue mRequestQueue;

    public RoutineFetcher(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void fetchWorkouts(final OnRoutineDataReceivedListener listener) {

        String url = Uri.parse(WEBAPI_BASE_URL).buildUpon()
                .appendQueryParameter("type", "workouts").build().toString();

        // Request all workouts
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> listener.onWorkoutsReceived(jsonToWorkouts(response)),
                listener::onErrorResponse);

        mRequestQueue.add(request);
    }

    private List<Workout> jsonToWorkouts(JSONArray jsonArray) {

        // Create a list of workouts
        List<Workout> workoutList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject workoutObj = jsonArray.getJSONObject(i);

                Workout workout = new Workout();
                workout.setText(workoutObj.getString("text"));
                workout.setUpdateTime(workoutObj.getLong("updated"));
                workoutList.add(workout);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON array: " + e.getMessage());
        }

        return workoutList;
    }

    public void fetchExercises(final Workout workout, final OnRoutineDataReceivedListener listener) {

        String url = Uri.parse(WEBAPI_BASE_URL).buildUpon()
                .appendQueryParameter("type", "exercises")
                .appendQueryParameter("workout", workout.getText())
                .build().toString();

        // Request exercises for this workout
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> listener.onExercisesReceived(workout, jsonToExercises(response)),
                listener::onErrorResponse);

        mRequestQueue.add(jsArrayRequest);
    }

    private List<Exercise> jsonToExercises(JSONArray jsonArray) {

        // Create a list of exercises
        List<Exercise> exerciseList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject exerciseObj = jsonArray.getJSONObject(i);

                Exercise exercise = new Exercise();
                exercise.setExerciseName(exerciseObj.getString("exercise_name"));
                exercise.setReps(exerciseObj.getInt("reps"));
                exercise.setWeight(exerciseObj.getDouble("weight"));
                exerciseList.add(exercise);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON array: " + e.getMessage());
        }

        return exerciseList;
    }
}
