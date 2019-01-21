/**
 *Created by Faisal Haque Bappy on 15-Jan-19.
 */

package project250.cse250.fhb369.ashepashe;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminAction extends AppCompatActivity {

    private Button delete, call, warn, done;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_action);

        delete = findViewById(R.id.delete);
        call = findViewById(R.id.call);
        textView = findViewById(R.id.msg);
        warn = findViewById(R.id.warn);
        done = findViewById(R.id.mark);

        String refKey = getIntent().getExtras().getString("REF");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("COMPLAINS").child(refKey);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView.setText((String) dataSnapshot.child("MESSAGE").getValue() +"\nFor "+(String) dataSnapshot.child("PACKAGE").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String UID = (String) dataSnapshot.child("COMPLAINER").getValue();
                        DatabaseReference rrr = FirebaseDatabase.getInstance().getReference().child("GENERAL_USERS");
                        DatabaseReference eee = FirebaseDatabase.getInstance().getReference().child("SERVICE_PROVIDERS");

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , UID);
                        i.putExtra(Intent.EXTRA_SUBJECT, "Complain status on AshePashe");
                        i.putExtra(Intent.EXTRA_TEXT   , "We have taken action for your complain. Thank you.");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(AdminAction.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String UID = (String) dataSnapshot.child("COMPLAINER").getValue();
                        DatabaseReference rrr = FirebaseDatabase.getInstance().getReference().child("GENERAL_USERS");
                        DatabaseReference eee = FirebaseDatabase.getInstance().getReference().child("SERVICE_PROVIDERS");

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , UID);
                        i.putExtra(Intent.EXTRA_SUBJECT, "Complain about your activity on AshePashe");
                        i.putExtra(Intent.EXTRA_TEXT   , "An user reported that he/she isn't satisfied with your service. So try to improve your quality and better luck next time. Thank you.");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(AdminAction.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String ad = (String) dataSnapshot.child("AD").getValue();
                        DatabaseReference rrr = FirebaseDatabase.getInstance().getReference().child("Ads").child(ad);
                        rrr.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                onBackPressed();
                ref.removeValue();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                ref.removeValue();
            }
        });
    }
}
