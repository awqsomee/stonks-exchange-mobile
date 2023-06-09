package com.example.stonksexchange.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.stonksexchange.App;
import com.example.stonksexchange.activity.MainActivity;
import com.example.stonksexchange.api.ApiService;
import com.example.stonksexchange.api.ErrorUtils;
import com.example.stonksexchange.api.domain.user.UserDataResponse;
import com.example.stonksexchange.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cropper {
    App app = App.getInstance();
    private ImageView imageView;
    private ImageView overlay;
    private ImageView avatar;
    private Bitmap originalBitmap;
    private ScrollView scrollView;
    private Context context;
    private Bitmap croppedBitmap;

    public Cropper(Context context, ImageView imageView, ImageView overlay, ImageView avatar, ScrollView scrollView) {
        this.context = context;
        this.imageView = imageView;
        this.overlay = overlay;
        this.avatar = avatar;
        this.scrollView = scrollView;
    }

    public void loadAndSetupImage(Uri imageUri) {
        try {
            originalBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            imageView.setImageBitmap(originalBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupOverlayTouchListener() {
        overlay.setOnTouchListener(new View.OnTouchListener() {
            private float scaleFactor = 1f;
            private ScaleGestureDetector scaleGestureDetector;
            private GestureDetector gestureDetector;
            private float lastTouchX, lastTouchY;
            private int originalLeft, originalTop;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Initialize ScaleGestureDetector and GestureDetector if not already created
                if (scaleGestureDetector == null) {
                    scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
                }
                if (gestureDetector == null) {
                    gestureDetector = new GestureDetector(context, new GestureListener());
                }

                // Pass the touch event to the ScaleGestureDetector and GestureDetector
                scaleGestureDetector.onTouchEvent(event);
                gestureDetector.onTouchEvent(event);

                // Handle touch events for moving the overlay
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        // Save the original position of the overlay
                        originalLeft = overlay.getLeft();
                        originalTop = overlay.getTop();

                        // Save the last touch coordinates
                        lastTouchX = event.getRawX();
                        lastTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Calculate the distance moved
                        float dx = event.getRawX() - lastTouchX;
                        float dy = event.getRawY() - lastTouchY;

                        // Update the position of the overlay based on the distance moved
                        overlay.offsetLeftAndRight((int) dx);
                        overlay.offsetTopAndBottom((int) dy);

                        // Update the last touch coordinates
                        lastTouchX = event.getRawX();
                        lastTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Check if the overlay has moved, perform necessary actions if needed
                        if (overlay.getLeft() != originalLeft || overlay.getTop() != originalTop) {
                            // Overlay has moved, handle the movement
                            handleOverlayMove();
                        }
                        break;
                }

                return true; // Consume the touch event
            }

            // ScaleGestureDetector for pinch-to-zoom gesture
            class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    // Update the scale factor based on the gesture detector's scaling factor
                    scaleFactor *= detector.getScaleFactor();

                    // Limit the scale factor within a certain range
                    scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

                    // Apply the scale factor to the overlay
                    overlay.setScaleX(scaleFactor);
                    overlay.setScaleY(scaleFactor);
                    handleOverlayMove();

                    return true;
                }
            }

            // GestureDetector for double-tap gesture
            class GestureListener extends GestureDetector.SimpleOnGestureListener {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    // Perform the desired action on double-tap (e.g., send the cropped image to the server)
                    handleOverlayMove();
                    sendCroppedImageToServer(croppedBitmap);
                    return true;
                }
            }
        });
    }

    private void handleOverlayMove() {
        // Get the dimensions of the original image
        int imageWidth = imageView.getDrawable().getIntrinsicWidth();
        int imageHeight = imageView.getDrawable().getIntrinsicHeight();

        // Get the dimensions and position of the circular overlay
        int overlayWidth = overlay.getWidth();
        int overlayHeight = overlay.getHeight();
        int overlayX = (int) overlay.getX();
        int overlayY = (int) overlay.getY();

// Get the scale factor of the overlay
        float scaleFactor = overlay.getScaleX();

        // Calculate the crop parameters based on the overlay position, size, and scale factor
        int cropX = (int) (((overlayX - (overlayWidth * (scaleFactor - 1) / 2)) / imageView.getWidth()) * imageWidth);
        int cropY = (int) (((overlayY - (overlayHeight * (scaleFactor - 1) / 2)) / imageView.getHeight()) * imageHeight);
        int cropSize = (int) ((overlayWidth / imageView.getWidth()) * imageWidth * scaleFactor);

        // Update the crop area with the new position
        updateCropArea(cropX, cropY, cropSize);
    }


    private void updateCropArea(int cropX, int cropY, int cropSize) {
        // Get the original bitmap from the ImageView
        Bitmap originalBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

// Adjust cropX and cropY if they are negative
        if (cropX < 0) {
            cropX = 0;
        }
        if (cropY < 0) {
            cropY = 0;
        }

// Calculate the maximum allowed width and height for cropping
        int maxWidth = originalBitmap.getWidth() - cropX;
        int maxHeight = originalBitmap.getHeight() - cropY;

// Limit the cropSize to the maximum allowed values
        cropSize = Math.min(cropSize, Math.min(maxWidth, maxHeight));

// Create a blank bitmap with the same dimensions as the original image
        croppedBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

// Create a canvas object using the blank bitmap
        Canvas canvas = new Canvas(croppedBitmap);

// Draw a circle on the canvas using the desired dimensions for the circular crop
        float centerX = cropX + cropSize / 2f;
        float centerY = cropY + cropSize / 2f;
        float radius = cropSize / 2f;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(centerX, centerY, radius, paint);

// Set the Xfermode of the paint object to PorterDuff.Mode.SRC_IN to apply the circular shape
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

// Draw the original image onto the canvas, applying the circular mask
        canvas.drawBitmap(originalBitmap, 0, 0, paint);

// Retrieve the cropped bitmap from the canvas
        croppedBitmap = Bitmap.createBitmap(croppedBitmap, cropX, cropY, cropSize, cropSize);
    }

    private void sendCroppedImageToServer(Bitmap croppedBitmap) {
        // Here, you can convert the cropped bitmap to PNG or any other desired format
        // and send it to the server using an appropriate method (e.g., HTTP POST).

        // Example: Convert the cropped bitmap to PNG and save it to a file
        try {
            File outputFile = new File(context.getCacheDir(), "cropped_image.png");
            FileOutputStream fos = new FileOutputStream(outputFile);
            croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), outputFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", outputFile.getName(), requestBody);

            // Step 5: Make a request to the server using Retrofit
            Call<UserDataResponse> call = ApiService.AuthApiService.uploadAvatar(filePart);
            scrollView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            overlay.setVisibility(View.GONE);
            call.enqueue(new Callback<UserDataResponse>() {
                @Override
                public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                    if (response.isSuccessful()) {
                        UserDataResponse data = response.body();
                        User user = data.getUser();
                        app.setUserData(user);
                        if (app.getUser().getAvatar() != null) {
                            RequestCreator avatarImage = Picasso.get().load("https://stonks-kaivr.amvera.io/" + app.getUser().getAvatar());
                            avatarImage.into(avatar);
                            avatarImage.into(MainActivity.getAccAuthButton());
                        }
                    } else {
                        ErrorUtils.handleErrorResponse(response, context);
                    }
                }

                @Override
                public void onFailure(Call<UserDataResponse> call, Throwable t) {
                    ErrorUtils.failureRequest(context);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

