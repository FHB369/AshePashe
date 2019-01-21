/**
 *Created by Faisal Haque Bappy on 15-Jan-19.
 */

package project250.cse250.fhb369.ashepashe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendComplain extends AppCompatActivity {

    EditText editText;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_complain);

        editText = findViewById(R.id.editText_complain);
        send = findViewById(R.id.submit_complain);

        String AD = getIntent().getExtras().getString("AD");
        String PACK = getIntent().getExtras().getString("PKG");
        String REQ = getIntent().getExtras().getString("REF");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("COMPLAINS");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString().trim();

                if(!msg.isEmpty()){
                    DatabaseReference r = ref.push();

                    r.child("AD").setValue(AD);
                    r.child("PACKAGE").setValue(PACK);
                    r.child("REQUEST_ID").setValue(REQ);
                    r.child("MESSAGE").setValue(msg);
                    r.child("COMPLAINER").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                    Toast.makeText(getApplicationContext(), "Complain Sent", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }
            }
        });
    }
}
