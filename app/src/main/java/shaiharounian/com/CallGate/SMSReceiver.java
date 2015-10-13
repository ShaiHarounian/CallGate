package shaiharounian.com.CallGate;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";

    SharedPreferences prefs;
    String number;
    String messageBody;
    String password;
    String address;
    boolean isReceiverChecked;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        prefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        number = prefs.getString("number", null);
        password = prefs.getString("password", null);
        isReceiverChecked = prefs.getBoolean("isReceiverChecked", false);

        if (isReceiverChecked) {
            // Retrieves a map of extended data from the intent.
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                final Object[] pdusObj = (Object[]) extras.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {

                    // read the sms:
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    //get the sender's address messageBody:
                    address = currentMessage.getDisplayOriginatingAddress();
                    //get the message's body
                    messageBody = currentMessage.getDisplayMessageBody();
                }
            }

            if (messageBody.equals(password)) {
                Intent callIntent = new Intent(context, CallActivity.class);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.putExtra("address", address);
                context.startActivity(callIntent);

//            Intent callIntent = new Intent();
//            callIntent.setAction(Intent.ACTION_CALL);
//            intent.setData(Uri.parse("tel:" + number));
//            callIntent.setClassName("shaiharounian.com.clicktocall", "shaiharounian.com.clicktocall.MainActivity");
//            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(callIntent);
            }
        }
    }
}
