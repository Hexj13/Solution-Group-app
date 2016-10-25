package com.example.vladi.solutiongroup.mvc.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.vladi.solutiongroup.R;




public class NotificationService extends Service {

    public static final String EXTRA_TITLE = "NotificationService_title";
    public static final String EXTRA_BODY = "NotificationService_body";
    public static final String EXTRA_TIME = "NotificationService_time";

    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = 1;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class NotificationServiceBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        String title = intent.getStringExtra(EXTRA_TITLE);
        String body = intent.getStringExtra(EXTRA_BODY);
        long time = intent.getLongExtra(EXTRA_TIME, 0L);

        Log.wtf("Servis", "we are get intent!\ntitle = " + title + "\nbody = " + body + "\ntime = " + time);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, null, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new NotificationServiceBinder();

    public void showNotification(String noteName, String noteBody, long time) {
        //CharSequence text = getText(R.string.local_service_started);
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                //new Intent(this, LocalServiceActivities.Controller.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_query_builder_black_24dp)  // the status icon
                //.setTicker(text)  // the status text
                .setWhen(time)  // the time stamp
                .setContentTitle(noteName)  // the label of the entry
                .setContentText(noteBody)  // the contents of the entry
                //.setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        mNM.notify(NOTIFICATION, notification);
    }

}