package com.faikphone.client;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by BeINone on 2017-04-04.
 */

public class FakeStatusBarService extends Service {

    private AppPreferences mAppPrefs;
    private WindowManager mWindowManager;
    private View mView;
    private TextView mTimeTV;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppPrefs = new AppPreferences(this);

        if (!mAppPrefs.isFakeStatusBarMode()) {
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.view_fake_status_bar, null);

            mTimeTV = (TextView) mView.findViewById(R.id.tv_time);
            setCurrentTime();

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (int) DensityConverter.dpToPx(this, 28),
                    // Allows the view to be on top of the StatusBar
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    // Keeps the button presses from going to the background window
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            // pass touch event to real status bar behind this view
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            // Enables the notification to receive touch events
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            // Draws over status bar
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP | Gravity.CENTER;

            mView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    mWindowManager.addView(mView, params);
                } else {
                    Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            registerReceiver(mTimeChangedReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        } else {
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null) mWindowManager.removeView(mView);
        if (mTimeChangedReceiver != null) {
            try {
                unregisterReceiver(mTimeChangedReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm", Locale.KOREA);
        String currentTime = sdf.format(new Date());
        mTimeTV.setText(currentTime);
    }

    private BroadcastReceiver mTimeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                setCurrentTime();
            }
        }
    };
}
