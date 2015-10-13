package shaiharounian.com.CallGate;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * TODO:
 * >>>>><<<<<<<<TOP PRIORITY>>>>>>>>>>>>
 * <<<<<upload this to google play>>>>>

 * 1. QA the phone number and password:
 * 3. add the name of the contact to the SMS welcome.
 * 4. permissions for specific numbers for a specific time
 * 5. make calls to be background
 * 6. Move the call task from activity to a service
 * 7. Make notifications
 * 5. Make feedback massages to be editable with turn on/off switch
 * 6. Set animations
 * 8. design
 *10. What would happen when the receiver is activated while the phone is busy
 *    and now the app should make a phone call?
 *11. set the transition animations:
 *    must be set before add/remove/replace
 *12. Add notification list in DB including all the events
 *13. pressing the native btn(Back) in settings frag will go to the main frag
 *14. Set new SMS to read
 *
 */

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) { // State is null - (new show)
            Fragment mainFragment = new MainFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragmentContainer, mainFragment, "MainFragment");
            ft.commit();
        }

//        Fragment settingsFragment = new SettingsFragment();
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.add(R.id.fragmentContainer, settingsFragment, "SettingsFragment");
//        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    public void switchFragments() {
        //switch the fragments:
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        //test is A is present:
        Fragment testA = fm.findFragmentByTag("MainFragment");
        if (testA != null && testA.isVisible()) {
            //found A: replace with B
            Fragment fragSettings = new SettingsFragment();
            ft.replace(R.id.fragmentContainer, fragSettings, "SettingsFragment");
        } else {
            // A not found: replace with A
            Fragment fragCall = new MainFragment();
            ft.replace(R.id.fragmentContainer, fragCall, "MainFragment");
        }
        //do the transaction:
        ft.commit();
    }
}