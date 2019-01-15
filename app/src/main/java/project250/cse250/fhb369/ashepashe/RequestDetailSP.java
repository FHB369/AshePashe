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

    private TextView title, pack, user;
    private Button contact, direction, status, cancel;

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
        cancel = findViewById(R.id.btn_req_cancel_sp);

        String refKey = getIntent().getExtras().getString("REF");
        String stat = getIntent().getExtras().getString("STAT");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("REQUESTS").child(refKey);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ADD = (String) dataSnapshot.child("ADDRESS").getValue();
                String TIME = (String) dataSnapshot.child("TIME").getValue();

                title.setText((String) dataSnapshot.child("TITLE").getValue());
                pack.setText((String) dataSnapshot.child("PACKAGE").getValue());
                String STAT = (String) dataSnapshot.child("STATUS").getValue();
                if(STAT.equals("REQUESTED")){
                    status.setText("ACCEPT");
                    contact.setVisibility(View.GONE);
                    direction.setVisibility(View.GONE);
                }else if(STAT.equals("PENDING")){
                    status.setText("PENDING");
                    cancel.setVisibility(View.GONE);
                }else if(STAT.equals("DONE")){
                    status.setText("DONE");
                    cancel.setVisibility(View.GONE);
                    contact.setVisibility(View.GONE);
                    direction.setVisibility(View.GONE);
                }
                String re = (String) dataSnapshot.child("SENDER").getValue();
                DatabaseReference rf = FirebaseDatabase.getInstance().getReference().child("USERS").child("GENERAL_USERS").child(re);
                rf.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String detail = (String) dataSnapshot.child("NAME").getValue()+"\nAddress: "+ADD+"\n Deadline: "+TIME;
                        user.setText(detail);
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

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stat.equals("REQUESTED")){
                    ref.child("STATUS").setValue("PENDING");
                    status.setText("PENDING");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String sender = (String) dataSnapshot.child("SENDER").getValue();
                            String title = (String) dataSnapshot.child("TITLE").getValue();

                            DatabaseReference rrrr = FirebaseDatabase.getInstance().getReference().child("USERS/GENERAL_USERS").child(sender);
                            rrrr.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String email = (String) dataSnapshot.child("EMAIL").getValue();
                                    PushNotification pushNotification = new PushNotification(email, "Your request for "+title+"is accepted");
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
                    contact.setVisibility(View.VISIBLE);
                    direction.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.GONE);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                ref.removeValue();
            }
        });
    }
}
