/**
 *Created by Faisal Haque Bappy on 09-Jan-19.
 */

package project250.cse250.fhb369.ashepashe;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RatingDone extends AppCompatActivity {

    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_done);

        String ref = getIntent().getExtras().getString("REF");
        String oldRat = getIntent().getExtras().getString("oldRating");
        String newRat = getIntent().getExtras().getString("newRating");
        String people = getIntent().getExtras().getString("people");

        double Old = Double.parseDouble(oldRat);
        double New = Double.parseDouble(newRat);
        int Count = Integer.parseInt(people);

        double fin = (Old+New)/(Count+1);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Ads").child(ref);
        reference.child("RATING").setValue(String.valueOf(fin).substring(0,3));
        reference.child("REVIEWERS").setValue(String.valueOf(Count+1));

        back = findViewById(R.id.backRatingDone);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
