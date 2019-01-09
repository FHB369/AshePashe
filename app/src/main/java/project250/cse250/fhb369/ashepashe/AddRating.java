package project250.cse250.fhb369.ashepashe;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddRating extends AppCompatActivity {

    private Button oneStar, twoStar, threeStar, fourStar, fiveStar, submit;
    private int rating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating);

        String ref = getIntent().getExtras().getString("REF");

        assert ref != null;
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Ads").child(ref);

        oneStar = findViewById(R.id.oneStar);
        twoStar = findViewById(R.id.twoStar);
        threeStar = findViewById(R.id.threeStar);
        fourStar = findViewById(R.id.fourStar);
        fiveStar = findViewById(R.id.fiveStar);

        submit = findViewById(R.id.btn_submit_rating);

        oneStar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                oneStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                rating = 1;
            }
        });

        twoStar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                oneStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                twoStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                rating = 2;
            }
        });

        threeStar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                oneStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                twoStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                threeStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                rating = 3;
            }
        });

        fourStar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                oneStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                twoStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                threeStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                fourStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                rating = 4;
            }
        });

        fiveStar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                oneStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                twoStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                threeStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                fourStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                fiveStar.setBackground(getDrawable(R.drawable.ic_star_green_24dp));
                rating = 5;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Orating = (String) dataSnapshot.child("RATING").getValue();
                        String reviewers = (String) dataSnapshot.child("REVIEWERS").getValue();

                        Intent intent = new Intent(AddRating.this, RatingDone.class);
                        intent.putExtra("oldRating", Orating);
                        intent.putExtra("people", reviewers);
                        intent.putExtra("newRating", String.valueOf(rating));
                        intent.putExtra("REF", ref);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slow_fade_in, R.anim.slow_fade_out);
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
