/**
 *Created by Faisal Haque Bappy on 15-Jan-19.
 */

package project250.cse250.fhb369.ashepashe;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AdminFeed extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feed);

        recyclerView = findViewById(R.id.admin_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdminFeedAdapter adapter = new AdminFeedAdapter(this, FirebaseDatabase.getInstance().getReference().child("COMPLAINS"));
        recyclerView.setAdapter(adapter);

        button = findViewById(R.id.logOut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminFeed.this, SplashActivity.class);
                startActivity(intent);
                finish();
                FirebaseAuth.getInstance().signOut();
            }
        });
    }
}
