package project250.cse250.fhb369.ashepashe;

import android.content.Intent;
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

    private TextView title, pack, status;
    private Button done, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_request);

        title = findViewById(R.id.title_modify_req);
        pack = findViewById(R.id.package_modify_req);
        status = findViewById(R.id.status_modify_req);

        done = findViewById(R.id.btn_req_complete);
        cancel = findViewById(R.id.btn_req_cancel);

        String refKey = getIntent().getExtras().getString("REF");
        String stat = getIntent().getExtras().getString("STAT");

        if(stat.equals("DONE")){
            done.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("REQUESTS").child(refKey);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title.setText((String) dataSnapshot.child("TITLE").getValue());
                pack.setText((String) dataSnapshot.child("PACKAGE").getValue());
                status.setText((String) dataSnapshot.child("STATUS").getValue());
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
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
