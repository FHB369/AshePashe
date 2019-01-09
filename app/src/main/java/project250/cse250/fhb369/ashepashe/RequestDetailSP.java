package project250.cse250.fhb369.ashepashe;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestDetailSP extends AppCompatActivity {

    private TextView title, pack, status, user;
    private Button contact, direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail_sp);

        title = findViewById(R.id.title_detail_req_sp);
        pack = findViewById(R.id.package_detail_req_sp);
        status = findViewById(R.id.status_detail_req_sp);
        user = findViewById(R.id.sender_detail_req_sp);

        contact = findViewById(R.id.btn_req_contact_sp);
        direction = findViewById(R.id.btn_req_direction_sp);

        String refKey = getIntent().getExtras().getString("REF");
        String stat = getIntent().getExtras().getString("STAT");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("REQUESTS").child(refKey);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title.setText((String) dataSnapshot.child("TITLE").getValue());
                pack.setText((String) dataSnapshot.child("PACKAGE").getValue());
                status.setText((String) dataSnapshot.child("STATUS").getValue());
                String re = (String) dataSnapshot.child("SENDER").getValue();
                DatabaseReference rf = FirebaseDatabase.getInstance().getReference().child("USERS").child("GENERAL_USERS").child(re);
                rf.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user.setText((String) dataSnapshot.child("NAME").getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String re = (String) dataSnapshot.child("SENDER").getValue();
                        DatabaseReference rf = FirebaseDatabase.getInstance().getReference().child("USERS").child("GENERAL_USERS").child(re);
                        rf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                String mobileNumb = (String) dataSnapshot.child("MOBILE_NO").getValue();
                                intent.setData(Uri.parse("tel:" + mobileNumb));
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        double lat = (double) dataSnapshot.child("SENDER_LATITUDE").getValue();
                        double lon = (double) dataSnapshot.child("SENDER_LONGITUDE").getValue();
                        Intent intent = new Intent(RequestDetailSP.this, MapDirection.class);
                        intent.putExtra("LAT", lat);
                        intent.putExtra("LON", lon);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
