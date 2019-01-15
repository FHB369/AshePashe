package project250.cse250.fhb369.ashepashe;

import android.content.Intent;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
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

public class ModifyRequest extends AppCompatActivity {

    private TextView title, pack, address_time;
    private Button done, cancel, phone, status, complain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_request);

        title = findViewById(R.id.title_modify_req);
        pack = findViewById(R.id.package_modify_req);
        status = findViewById(R.id.status_modify_req);
        address_time = findViewById(R.id.address_time_detail);

        done = findViewById(R.id.btn_req_complete);
        cancel = findViewById(R.id.btn_req_cancel);
        phone = findViewById(R.id.btn_phone_sp);
        complain = findViewById(R.id.btn_send_complain);

        String refKey = getIntent().getExtras().getString("REF");
        String stat = getIntent().getExtras().getString("STAT");

        if(stat.equals("DONE")){
            done.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            complain.setVisibility(View.VISIBLE);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("REQUESTS").child(refKey);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title.setText((String) dataSnapshot.child("TITLE").getValue());
                pack.setText((String) dataSnapshot.child("PACKAGE").getValue());
                String ADD_TIME = "ঠিকানাঃ " + (String) dataSnapshot.child("ADDRESS").getValue() +"\nকাঙ্ক্ষিত সময়ঃ "+ (String) dataSnapshot.child("TIME").getValue();
                address_time.setText(ADD_TIME);

                String stats = (String) dataSnapshot.child("STATUS").getValue();
                if(stat.equals("REQUESTED")){
                    done.setVisibility(View.GONE);
                    phone.setVisibility(View.GONE);
                    complain.setVisibility(View.GONE);
                    status.setText(stats);
                }else if(stat.equals("DONE")){
                    complain.setVisibility(View.VISIBLE);
                    status.setText(stats);
                }else{
                    status.setText(stats);
                    complain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyRequest.this, GEN_Home.class);
                startActivity(intent);
                finish();
                ref.removeValue();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("STATUS").setValue("DONE");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String adRef = (String) dataSnapshot.child("AD").getValue();
                        Intent intent = new Intent(ModifyRequest.this, AddRating.class);
                        intent.putExtra("REF", adRef);

                        String reciever = (String) dataSnapshot.child("RECIEVER").getValue();
                        DatabaseReference rrrr = FirebaseDatabase.getInstance().getReference().child("USERS/SERVICE_PROVIDERS").child(reciever);
                        rrrr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String email = (String) dataSnapshot.child("EMAIL").getValue();
                                PushNotification pushNotification = new PushNotification(email, "A request has been marked as done");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String UID = (String) dataSnapshot.child("RECIEVER").getValue();
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE_PROVIDERS").child(UID);
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String numb = (String) dataSnapshot.child("MOBILE_NO").getValue();
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:"+numb));
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

        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyRequest.this, SendComplain.class);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String ad = (String) dataSnapshot.child("AD").getValue();
                        String pack = (String) dataSnapshot.child("PACKAGE").getValue();

                        intent.putExtra("AD", ad);
                        intent.putExtra("PKG", pack);
                        intent.putExtra("REF", refKey);

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
