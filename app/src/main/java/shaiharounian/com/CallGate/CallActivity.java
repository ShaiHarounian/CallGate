package shaiharounian.com.CallGate;

//test

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;

public class CallActivity extends Activity {

    private static final String TAG = "CallActivity";
    private static final int PENDING_MAIN_ACTIVITY_ID = 100;
    private static final int TEST_NOTIF_ID = 1;
    private static final int TEST_COUNT_NOTIF = 2;
    private static final int PENDING_CALL_ID = 3 ;
    private static int count = 1;

    String number = null;
    SharedPreferences prefs = null;
    String senderNum;
    private NotificationCompat.Builder notificationBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_call);

        notificationBuilder = new NotificationCompat.Builder(getApplicationContext());

        prefs = getSharedPreferences("pref", Context.MODE_PRIVATE);
        number = prefs.getString("number", null);

        Intent intent = getIntent();
        senderNum = intent.getStringExtra("address");

        doCall();
        sendSMSFeedback();
        sendCountNotification();
//        addCount();
//        sendNotification();
//        sendBigViewNotification();
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call, menu);
        return true;
    }

    private void doCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setPackage("com.android.server.telecom");
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    private void sendSMSFeedback() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(senderNum, null, "Welcome!", null, null);
    }

    private void addCount() {

        if (notificationBuilder != null) {
            notificationBuilder.setContentText("You have new messages").setNumber(++count);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(TEST_COUNT_NOTIF, notificationBuilder.build());
        } else {
            sendCountNotification();
        }
    }

    private void sendCountNotification() {

        notificationBuilder.setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle(" CallGate:")
                .setAutoCancel(true);

        if (notificationBuilder != null) {
            notificationBuilder.setContentText("the gate was opened few times").setNumber(++count);
        } else {
            notificationBuilder.setContentText("someone opened the gate").setNumber(1);
        }

        // Open the MainActivity when clicking this notification
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(PENDING_MAIN_ACTIVITY_ID, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(TEST_COUNT_NOTIF, notificationBuilder.build());
    }

    private void sendNotification() {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle(" CallGate:")
                .setContentText("Someone opened the gate")
//                .setSound()
//                .addAction()
//                .setColor()

                .setAutoCancel(true);

//      Create an explicit intent for an MainActivity in my app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of my application
        // to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Add the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(TEST_NOTIF_ID, notificationBuilder.build());
    }

    private void sendBigViewNotification() {

        // prepare intent which is triggered if the

        Intent intent = new Intent(this, MainActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(this)
                .setContentTitle("Gate opened")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.notification_template_icon_bg, "Call", pIntent)
                .addAction(R.drawable.notification_template_icon_bg, "More", pIntent)
                .addAction(R.drawable.notification_template_icon_bg, "And more", pIntent).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);


//        Intent notificationIntent = new Intent(this, MainActivity.class);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.notification_template_icon_bg)
//                .setContentTitle("Test Big")
//                .setContentText("text big!!!!");
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(notificationIntent);
//
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.setBigContentTitle("You have many messages");
//
//        inboxStyle.addLine("msg1");
//        inboxStyle.addLine("msg2");
//
//        builder.setStyle(inboxStyle);
//
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(PENDING_MAIN_ACTIVITY_ID, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(TEST_NOTIF_ID, builder.build());
    }


//    private void addCount() {
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.notification_template_icon_bg)
//                .setContentTitle(" CallGate:");
//
//        if (notificationBuilder != null) {
//            notificationBuilder.setContentText("You have new messages").setNumber(++count);
//
//        } else {
//            notificationBuilder.setContentText(address + "opened the gate");
//        }
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(TEST_COUNT_NOTIF, notificationBuilder.build());
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent mainActivity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        number = prefs.getString("number", null);
    }
}
