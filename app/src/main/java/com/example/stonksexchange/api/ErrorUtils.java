package com.example.stonksexchange.api;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

public class ErrorUtils {
    public static void handleErrorResponse(Response<?> response, Context context) {
        try {
            // Parse error response JSON
            String errorBody = response.errorBody().string();
            System.out.println(errorBody);
            JSONObject jsonObject = new JSONObject(errorBody);
            String errorMessage = jsonObject.optString("message");

            // Display error message in Toast
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Внутренняя ошибка", Toast.LENGTH_SHORT).show();
        }
    }
}
