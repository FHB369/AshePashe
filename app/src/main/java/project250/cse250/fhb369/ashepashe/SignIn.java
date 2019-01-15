package project250.cse250.fhb369.ashepashe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private EditText email, password;
    private Button signIn;
    private ImageView shadow;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabase1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.email_sign_in);
        password = findViewById(R.id.password_sign_in);
        signIn = findViewById(R.id.btn_sign_in);
        shadow = findViewById(R.id.shadow_sign_in);
        progressBar = findViewById(R.id.progressBar_sign_in);

        shadow.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL = email.getText().toString().trim(),
                        PASSWORD = password.getText().toString().trim();

                if(EMAIL.isEmpty()){
                    email.setError("Please give a valid email");
                }else if(PASSWORD.isEmpty()){
                    password.setError("Password can't be empty");
                }else{
                    shadow.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    signInUser(EMAIL, PASSWORD);
                }
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Authentication Complete", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String UID = user.getUid();
                    divertUser(UID);
                }else{
                    Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
                shadow.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void divertUser(final String uid) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS").child("GENERAL_USERS");
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE_PROVIDERS");

        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("admin@ashepashe.com")){
            Intent intent = new Intent(SignIn.this, AdminFeed.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left_exit);
            finish();
            return;
        }

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)){
                    Intent intent = new Intent(SignIn.this, GEN_Home.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left_exit);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)){
                    Intent intent = new Intent(SignIn.this, SP_Home.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left_exit);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.still, R.anim.slow_fade_out);
    }
}
