package shaiharounian.com.CallGate;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MainFragment";
    private static final int TEST_NOTIF_ID = 1;
    private static final int TEST_COUNT_NOTIF = 2;
    private int count = 1;


    Context context;
    MainActivity activity;
    Button btn_Call, btn_settings;
    SharedPreferences prefs;
    String number;

    NotificationCompat.Builder builder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_main, container, false);

        activity = (MainActivity) getActivity();

        prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        number = prefs.getString("number", null);

        btn_Call = (Button) view.findViewById(R.id.btn_notificationTest);
        btn_Call.setOnClickListener(this);

        btn_settings = (Button) view.findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notificationTest:
//                sendNotification();
//                sendBigViewNotification();
                sendCountNotification();
//                addCount();
//                sendCountNotification();
                break;
            case R.id.btn_settings:
                activity.switchFragments();
                break;
        }
    }

    public void doCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setPackage("com.android.server.telecom");
        callIntent.setData(Uri.parse("tel:" + number));
        activity.startActivity(callIntent);
    }

    public void sendNotification() {
//
//      Create an explicit intent for a MainActivity in my app
        Intent mainActivityIntent = new Intent(activity, CallActivity.class);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle(" Title (test):")
                .setContentText("content")
                .setAutoCancel(true);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of my application
        // to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Add the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(mainActivityIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(TEST_NOTIF_ID, notificationBuilder.build());
    }

    private void sendBigViewNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity)
                .setSmallIcon(R.drawable.ic_media_play)
                .setContentTitle("Test Big")
                .setContentText("text big!!!!");

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("You have many messages");

        inboxStyle.addLine("msg1");
        inboxStyle.addLine("msg2");

        mBuilder.setStyle(inboxStyle);

        NotificationManager mNotificationManager = (NotificationManager)
                activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(TEST_NOTIF_ID, mBuilder.build());
    }

    private void sendCountNotification() {
        builder = new NotificationCompat.Builder(activity)
                .setSmallIcon(R.drawable.ic_media_play)
                .setContentTitle("CallGate")
                .setAutoCancel(true);

        if (builder == null) {
            builder.setContentText("You have a messageBody").setNumber(1);
        } else {
            builder.setContentText(" people opened the gate").setNumber(count++);
        }

        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(TEST_COUNT_NOTIF, builder.build());
    }

    private void addCount() {
        if (builder != null) {
            builder.setContentText("You have new messages").setNumber(++count);
            NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(TEST_COUNT_NOTIF, builder.build());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        number = prefs.getString("number", null);
    }
}
