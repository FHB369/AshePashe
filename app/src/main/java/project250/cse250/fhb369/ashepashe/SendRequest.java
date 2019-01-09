package project250.cse250.fhb369.ashepashe;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendRequest extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button back;
    private ImageView done;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        progressBar = findViewById(R.id.progressBarSendReq);
        back = findViewById(R.id.buttonBackDetail);
        textView = findViewById(R.id.textViewComplete);
        done = findViewById(R.id.doneProgress);

        done.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        String refKey = getIntent().getExtras().getString("REF");
        String pkg = getIntent().getExtras().getString("PKG");

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("REQUESTS");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Ads").child(refKey);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String reciever = dataSnapshot.child("UID").getValue().toString();
                String title = dataSnapshot.child("TITLE").getValue().toString();
                DatabaseReference request = mRef.push();
                request.child("TITLE").setValue(title);
                request.child("SENDER").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                request.child("RECIEVER").setValue(reciever);
                request.child("AD").setValue(refKey);
                request.child("PACKAGE").setValue(pkg);
                request.child("STATUS").setValue("PENDING");
                request.child("SENDER_LATITUDE").setValue(GEN_Home.currentLocation.getLatitude());
                request.child("SENDER_LONGITUDE").setValue(GEN_Home.currentLocation.getLongitude());
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
                String currentDateandTime = sdf.format(new Date());
                request.child("DATE").setValue(currentDateandTime);
                done.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
