package shaiharounian.com.CallGate;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements View.OnClickListener,
        Switch.OnCheckedChangeListener{

    private static final String TAG = "SettingsFragment";
    static final int PICK_CONTACT_REQUEST = 1;
    static final int RESULT_OK = -1;
    static final int RESULT_CANCEL = 3;

    Context context;
    SharedPreferences prefs;
    EditText et_number, et_password;
    Button btn_save, btn_cancel, btn_contacts;
    Switch switch_receiver;
    String number, password;
    boolean isReceiverChecked;
    MainActivity mainActivity;
    Activity activity;
    SMSReceiver smsReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_settings, container, false);
        activity = getActivity();


        // Check if no view has focus:
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        mainActivity = (MainActivity) getActivity();
        smsReceiver = new SMSReceiver();

        prefs = mainActivity.getSharedPreferences("pref", Context.MODE_PRIVATE);
        number = prefs.getString("number", null);
        password = prefs.getString("password", null);
        isReceiverChecked = prefs.getBoolean("isReceiverChecked", false);

        et_number = (EditText) view.findViewById(R.id.et_number);
        et_number.setText(number);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_number.getWindowToken(), 0);

        btn_contacts = (Button) view.findViewById(R.id.btn_contacts);
        btn_contacts.setOnClickListener(this);

        et_password = (EditText) view.findViewById(R.id.et_password);
        et_password.setText(password);


        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        switch_receiver = (Switch) view.findViewById(R.id.switch_receiver);

        if (isReceiverChecked == true) {
            switch_receiver.setChecked(true);
        } else {
            switch_receiver.setChecked(false);
        }
        switch_receiver.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_save:
                doSave();
                break;
            case R.id.btn_cancel:
                doCancel();
                break;
            case R.id.btn_contacts:
                pickContact();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.switch_receiver:
                if (isChecked == true) {
                    isReceiverChecked = true;
                } else {
                    isReceiverChecked = false;
                }
                break;
        }
    }

    public void doSave() {
        number = et_number.getText().toString();
        password = et_password.getText().toString();

        if (number != null && password != null) {

            prefs.edit().putString("number", number).apply();
            prefs.edit().putString("password", password).apply();
            prefs.edit().putBoolean("isReceiverChecked", isReceiverChecked).apply();
            mainActivity.switchFragments();
        } else {
            Toast.makeText(activity, "number and password must be filled!!", Toast.LENGTH_LONG).show();
        }
    }

    public void doCancel() {
        mainActivity.switchFragments();
    }

    public void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = intent.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = activity.getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String pickedNumber = cursor.getString(column);

//                int name = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//                String contactName = cursor.getString(name);
//                cursor.close();
                et_number.setText(pickedNumber);

            }
        }
    }

    public void doTestCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setPackage("com.android.server.telecom");
        callIntent.setData(Uri.parse("tel:" + et_number.getText().toString()));
        startActivity(callIntent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

