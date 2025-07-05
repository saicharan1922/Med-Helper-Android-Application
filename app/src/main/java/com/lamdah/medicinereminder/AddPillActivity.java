package com.lamdah.medicinereminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.lamdah.medicinereminder.database.DatabaseHelper;
import com.lamdah.medicinereminder.models.Pill;
import com.lamdah.medicinereminder.receivers.AlarmReceiver;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

@ExperimentalGetImage public class AddPillActivity extends AppCompatActivity {

    private EditText mPillNameEditText;
    private TextView mReminderTimeLabelTextView;
    private TextView mScheduleLabelTextView;
    private TextView mReminderTimeTextView;
    private Button mMondayButton;
    private Button mTuesdayButton;
    private Button mWednesdayButton;
    private Button mThursdayButton;
    private Button mFridayButton;
    private Button mSaturdayButton;
    private Button mSundayButton;

    private int mHour;
    private int mMinute;
    private DatabaseHelper databaseHelper;

    private PreviewView mPreviewView;
    private View mRectangleView;
    private Button mDoneButton;
    private ImageCapture imageCapture;
    private final Handler handler = new Handler();
    private static final String TAG = "AddPillActivity";
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if (mPreviewView.getVisibility() == View.VISIBLE) {
                takePictureAndAnalyzeText();
                handler.postDelayed(this, 2000);
            }
        }
    };
    private static final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
    private static final int REQUEST_CODE_PERMISSIONS = 10;

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startImageRecognitionProcess() {
        if (handler != null && runnableCode != null && imageCapture != null) {
            handler.post(runnableCode);
        }
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder()
                .build();

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageCapture)
                .build();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, useCaseGroup);

        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());

        // Start the image recognition process after the camera has been set up
        startImageRecognitionProcess();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pill);

        // Find views
        mPillNameEditText = findViewById(R.id.pill_name_edit_text);
        ImageView mCameraIconImageView = findViewById(R.id.camera_icon_image_view);
        mReminderTimeLabelTextView = findViewById(R.id.reminder_time_label_text_view);
        mReminderTimeTextView = findViewById(R.id.reminder_time_text_view);
        mScheduleLabelTextView = findViewById(R.id.schedule_label_text_view);
        mMondayButton = findViewById(R.id.monday_button);
        mTuesdayButton = findViewById(R.id.tuesday_button);
        mWednesdayButton = findViewById(R.id.wednesday_button);
        mThursdayButton = findViewById(R.id.thursday_button);
        mFridayButton = findViewById(R.id.friday_button);
        mSaturdayButton = findViewById(R.id.saturday_button);
        mSundayButton = findViewById(R.id.sunday_button);
        Button mCancelButton = findViewById(R.id.cancel_button);
        Button mSetAlarmButton = findViewById(R.id.set_alarm_button);

        databaseHelper = new DatabaseHelper(this);
        mPreviewView = findViewById(R.id.camera_view);
        mRectangleView = findViewById(R.id.rectangle_view);
        mDoneButton = findViewById(R.id.done_button);

        // Set camera icon click listener
        mCameraIconImageView.setOnClickListener(v -> {
            if (allPermissionsGranted()) {
                // Start the camera
                startCamera();
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }
        });

        // Add a new click listener for the mDoneButton to start the image recognition process
        mDoneButton.setOnClickListener(v -> {
            mPreviewView.setVisibility(View.GONE);
            mRectangleView.setVisibility(View.GONE);
            mDoneButton.setVisibility(View.GONE);
        });



        // Set reminder time click listener
        mReminderTimeTextView.setOnClickListener(v -> {
            // Get current time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch time picker dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddPillActivity.this,
                    (view, hourOfDay, minute) -> {
                        mHour = hourOfDay;
                        mMinute = minute;
                        // Update reminder time text view
                        mReminderTimeTextView.setText(String.format("%02d:%02d", mHour, mMinute));
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        });

        // Set reminder time click listener
        mReminderTimeTextView.setOnClickListener(v -> {
            // Get current time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch time picker dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddPillActivity.this,
                    (view, hourOfDay, minute) -> {
                        mHour = hourOfDay;
                        mMinute = minute;
                        // Update reminder time text view
                        mReminderTimeTextView.setText(String.format("%02d:%02d", mHour, mMinute));
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        });

        // Set schedule button click listeners
        mMondayButton.setOnClickListener(v -> {
            mMondayButton.setSelected(!mMondayButton.isSelected());
            mMondayButton.requestLayout();
            mMondayButton.invalidate();
            if (mMondayButton.isSelected()){
                mMondayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_selected, null));
            }else{
                mMondayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_normal, null));
            }
        });
        mTuesdayButton.setOnClickListener(v -> {
            // Toggle button state
            mTuesdayButton.setSelected(!mTuesdayButton.isSelected());
            if (mTuesdayButton.isSelected()){
                mTuesdayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_selected, null));
            }else{
                mTuesdayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_normal, null));
            }
        });
        mWednesdayButton.setOnClickListener(v -> {
            // Toggle button state
            mWednesdayButton.setSelected(!mWednesdayButton.isSelected());
            if (mWednesdayButton.isSelected()){
                mWednesdayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_selected, null));
            }else{
                mWednesdayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_normal, null));
            }
        });
        mThursdayButton.setOnClickListener(v -> {
            // Toggle button state
            mThursdayButton.setSelected(!mThursdayButton.isSelected());
            if (mThursdayButton.isSelected()){
                mThursdayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_selected, null));
            }else{
                mThursdayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_normal, null));
            }
        });
        mFridayButton.setOnClickListener(v -> {
            // Toggle button state
            mFridayButton.setSelected(!mFridayButton.isSelected());
            if (mFridayButton.isSelected()){
                mFridayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_selected, null));
            }else{
                mFridayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_normal, null));
            }
        });
        mSaturdayButton.setOnClickListener(v -> {
            // Toggle button state
            mSaturdayButton.setSelected(!mSaturdayButton.isSelected());
            if (mSaturdayButton.isSelected()){
                mSaturdayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_selected, null));
            }else{
                mSaturdayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_normal, null));
            }
        });
        mSundayButton.setOnClickListener(v -> {
            // Toggle button state
            mSundayButton.setSelected(!mSundayButton.isSelected());
            if (mSundayButton.isSelected()){
                mSundayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_selected, null));
            }else{
                mSundayButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_normal, null));
            }
        });
        // Set cancel button click listener
        mCancelButton.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        // Set set alarm button click listener
        mSetAlarmButton.setOnClickListener(v -> {
            String pillName = mPillNameEditText.getText().toString();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, mHour);
            calendar.set(Calendar.MINUTE, mMinute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long datetime = calendar.getTimeInMillis();

            boolean[] days = new boolean[]{
                    mMondayButton.isSelected(),
                    mTuesdayButton.isSelected(),
                    mWednesdayButton.isSelected(),
                    mThursdayButton.isSelected(),
                    mFridayButton.isSelected(),
                    mSaturdayButton.isSelected(),
                    mSundayButton.isSelected()
            };

            Pill pill = new Pill(-1, pillName, datetime, days);

            boolean isInserted = databaseHelper.addPill(pill);
            if (isInserted) {
                Toast.makeText(getApplicationContext(), "Pill saved", Toast.LENGTH_SHORT).show();

                // Set up the alarm
                Intent alarmIntent = new Intent(AddPillActivity.this, AlarmReceiver.class);
                alarmIntent.putExtra("pill", pill);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddPillActivity.this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                // Set the alarm to trigger at the specified time
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, datetime, pendingIntent);
                }
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Error saving pill", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCamera() {
        mRectangleView.setVisibility(View.VISIBLE);
        mDoneButton.setVisibility(View.VISIBLE);
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
                mPreviewView.setVisibility(View.VISIBLE);

                // Start the image recognition process after the camera has been set up
                startImageRecognitionProcess();
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }
    private void takePictureAndAnalyzeText() {
        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(ImageProxy image) {
                super.onCaptureSuccess(image);
                analyzeTextFromImage(image.getImage());
            }

            @Override
            public void onError(ImageCaptureException exception) {
                super.onError(exception);
                exception.printStackTrace();
            }
        });
    }

    private void analyzeTextFromImage(Image image) {


        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromMediaImage(image, 0);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        detector.processImage(firebaseVisionImage)
                .addOnSuccessListener(firebaseVisionText -> {
                    String resultText = firebaseVisionText.getText();
                    if (!resultText.isEmpty()) {
                        mPillNameEditText.setText(resultText);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error processing image: ", e));
    }

}