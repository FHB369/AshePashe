package project250.cse250.fhb369.ashepashe;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;

public class SP_Home extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard_sp:
                    loadFragment(new FragmentSPDashboard());
                    return true;
                case R.id.navigation_requests_sp:
                    loadFragment(new FragmentSPRequests());
                    return true;
                case R.id.navigation_services_sp:
                    loadFragment(new FragmentSPServices());
                    return true;
                case R.id.navigation_profile_sp:
                    loadFragment(new FragmentSPProfile());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_home);

        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null) {
            OneSignal.sendTag("User_ID", (String) mAuth.getCurrentUser().getEmail());
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_sp);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new FragmentSPDashboard());
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.sp_home_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void popBackStackTillEntry(int entryIndex) {

        if (getSupportFragmentManager() == null) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex) {
            return;
        }
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            getSupportFragmentManager().popBackStackImmediate(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }


    }

//    @Override
////    public void onBackPressed() {
////        popBackStackTillEntry(0);
////        moveTaskToBack(true);
////        loadFragment(new FragmentSPDashboard());
////    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            popBackStackTillEntry(0);
            moveTaskToBack(true);
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        loadFragment(new FragmentSPDashboard());

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
