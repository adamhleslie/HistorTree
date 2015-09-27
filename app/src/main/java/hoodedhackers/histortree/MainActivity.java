package hoodedhackers.histortree;

import java.util.List;
import java.util.Locale;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ibm.mobile.services.cloudcode.IBMCloudCode;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;
import com.ibm.mobile.services.data.geo.IBMPosition;
import com.ibm.mobile.services.location.IBMLocation;
import com.ibm.mobile.services.location.device.geo.IBMGeoAcquisitionPolicy;

import org.json.JSONArray;

import bolts.Continuation;
import bolts.Task;
//import com.ibm.mobile.services.push.IBMPush;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    NodeViewFragment frag;
    public static final String CLASS_NAME = "MainActivity";
    List<Item> itemList;
    ArrayAdapter<Item> lvArrayAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    Node curNode;
    Button leftButton;
    Button upButton;
    Button rightButton;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IBMBluemix.initialize(this, "8d612e87-518d-4eed-a10a-f9564b801637",
                "6c7a2404073020ea99a1dadbbcb4fe096ac9673c", "http://HistorTree.mybluemix.net");
        IBMCloudCode.initializeService();
        IBMData.initializeService();
//        IBMPush.initializeService();
//        IBMLocation location = IBMLocation.initializeService();
//        Log.d("FAILED", "PRE-GETSERVICE");
//        IBMLocation ibm = IBMLocation.getService();
//        Log.d("FAILED", "POST-GETSERVICE");
//        ibm.startAcquisition();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildGoogleApiClient();

//        IBMPosition currentPosition = location.getLocationContext().getGeoPosition();
//        JSONArray json = currentPosition.getJSON();
//        try {
//            Log.d("LONGITUDE", " " + json.getInt(0));
//        } catch (org.json.JSONException j) {
//            Log.d("FAILURE", "LONGITUDE");
//        }
//        try {
//            Log.d("LATITUDE", " " + json.getInt(0));
//        } catch (org.json.JSONException j) {
//            Log.d("FAILURE", "LATITUDE");
//        }
        /// CONTACT SERVER, CREATE NODES
        ////A
//        listItems();

        curNode = new Node();
        curNode.name = "NAME";
        curNode.left = new Node();
        curNode.left.name = "LEFT";
        curNode.vals = new String[] {"A", "B", "C", "D"};
        curNode.left.vals = new String[] {"D", "C", "B", "A"};

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        leftButton = (Button) findViewById(R.id.button_left);
        upButton = (Button) findViewById(R.id.button_up);
        rightButton = (Button) findViewById(R.id.button_right);

        //hideButtons();
    }

    private synchronized void buildGoogleApiClient() {
        GoogleApiCallBack callbacks = new GoogleApiCallBack();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(callbacks)
                .build();
    }


//    /**
//     * Refreshes itemList from data service.
//     *
//     * An IBMQuery is used to find all the list items.
//     */
//    public void listItems() {
//        try {
//            IBMQuery<Item> query = IBMQuery.queryForClass(Item.class);
//            /**
//             * IBMQueryResult is used to receive array of objects from server.
//             *
//             * onResult is called when it successfully retrieves the objects associated with the
//             * query, and will reorder these items based on creation time.
//             *
//             * onError is called when an error occurs during the query.
//             */
//            query.find().continueWith(new Continuation<List<Item>, Void>() {
//
//                @Override
//                public Void then(Task<List<Item>> task) throws Exception {
//                    // Log error message, if the save task is cancelled.
//                    if (task.isCancelled()) {
//                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
//                    }
//                    // Log error message, if the save task fails.
//                    if (task.isFaulted()) {
//                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
//                    }
//
//                    // If the result succeeds, load the list.
//                    else {
//                        final List<Item> objects = task.getResult();
//                        // Clear local itemList, as we'll be reordering & repopulating from DataService.
//                        itemList.clear();
//                        for (IBMDataObject item : objects) {
//                            itemList.add((Item) item);
//                        }
//                        lvArrayAdapter.notifyDataSetChanged();
//                    }
//                    return null;
//                }
//            },Task.UI_THREAD_EXECUTOR);
//
//        }  catch (IBMDataException error) {
//            Log.e(CLASS_NAME, "Exception : " + error.getMessage());
//        }
//    }



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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void moveLeft(View views){
        curNode = curNode.left;
        if(curNode == null)
            Log.d("FAIL", "FAIL");
        else
            Log.d("WIN", "WIN");
        rebase();
    }
    public void moveUp(View views){
        curNode = curNode.parent;
        rebase();
    }
    public void moveRight(View views){
        curNode = curNode.right;
        rebase();
    }
    private void rebase(){
        frag.setNode(curNode);
        hideButtons();
        frag.setText();
    }
    private void hideButtons() {

//        if(curNode.left == null)
//            leftButton.setVisibility(View.GONE);
//        else
//            leftButton.setVisibility(View.VISIBLE);
//        if(curNode.parent == null)
//            upButton.setVisibility(View.GONE);
//        else
//            upButton.setVisibility(View.VISIBLE);
//        if(curNode.right == null)
//            rightButton.setVisibility(View.GONE);
//        else
//            rightButton.setVisibility(View.VISIBLE);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            NodeViewFragment newFrag = NodeViewFragment.newInstance(position + 1);
            frag = newFrag;
            newFrag.setNode(curNode);
            return newFrag;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
            }
            return null;
        }
    }

    private class GoogleApiCallBack implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnected(Bundle bundle) {
            //connect successfully
            Location userLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    }
}
