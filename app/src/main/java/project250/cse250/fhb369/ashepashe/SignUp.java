package project250.cse250.fhb369.ashepashe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.core.utilities.Utilities;

import java.util.List;

public class SignUp extends AppCompatActivity {

    private Button sp,gen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        gen=findViewById(R.id.gen);
        sp=findViewById(R.id.sp);

        loadFragment(new SignUpGeneral());

        gen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                gen.setBackground(getDrawable(R.drawable.ic_gen_sign_up_enabled));
                sp.setBackground(getDrawable(R.drawable.ic_sp_sign_up_enabled));
                loadFragment(new SignUpGeneral());
            }
        });

        sp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                gen.setBackground(getDrawable(R.drawable.ic_gen_sign_up));
                sp.setBackground(getDrawable(R.drawable.ic_sp_sign_up));
                loadFragment(new SignUpSP());
            }
        });
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

    @Override
    public void onBackPressed() {
        popBackStackTillEntry(0);
        super.onBackPressed();

        overridePendingTransition(R.anim.still, R.anim.slow_fade_out);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
