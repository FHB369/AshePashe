/**
 *Created by Faisal Haque Bappy on 15-Jan-19.
 */

package project250.cse250.fhb369.ashepashe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryFeedGen extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_feed_gen);

        recyclerView = findViewById(R.id.gen_history_recycler);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("REQUESTS");
        String mUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        HistoryFeedAdapterGen adapter = new HistoryFeedAdapterGen(this, mDatabase, mUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
