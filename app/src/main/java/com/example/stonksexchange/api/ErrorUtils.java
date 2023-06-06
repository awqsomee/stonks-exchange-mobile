package com.example.stonksexchange.api;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

public class ErrorUtils {
    public static void failureRequest(Context context) {
        Toast.makeText(context, "Сервер недоступен", Toast.LENGTH_SHORT).show();
    }

    public static void handleErrorResponse(Response<?> response, Context context) {
        try {
            String errorBody = response.errorBody().string();
            System.out.println(errorBody);
            JSONObject jsonObject = new JSONObject(errorBody);
            String errorMessage = jsonObject.optString("message");
            if (jsonObject.has("errors")) {
                errorMessage = errorMessage + "\n" + jsonObject.getJSONObject("errors").getJSONArray("errors").getJSONObject(0).getString("msg");
            }
            // Display error message in Toast
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Внутренняя ошибка на сервере", Toast.LENGTH_SHORT).show();
        }
    }
}
