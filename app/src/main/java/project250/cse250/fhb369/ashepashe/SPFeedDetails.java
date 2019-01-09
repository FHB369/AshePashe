package project250.cse250.fhb369.ashepashe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SPFeedDetails extends AppCompatActivity {

    private ImageView coverPhoto;
    private TextView title, catagory, description, package1, package1price, package2, package2price, package3, package3price, rating, reviewers;
    private Button edit;
    String checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spfeed_details);

        String refKey = getIntent().getExtras().getString("RefKey");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Ads").child(refKey);

        coverPhoto = findViewById(R.id.detail_sp_cover_photo);
        title = findViewById(R.id.detail_sp_title);
        catagory = findViewById(R.id.detail_sp_catagory);
        description = findViewById(R.id.detail_sp_description);
        package1 = findViewById(R.id.detail_sp_pack_1);
        package1price = findViewById(R.id.detail_sp_pack_1_price);
        package2 = findViewById(R.id.detail_sp_pack_2);
        package2price = findViewById(R.id.detail_sp_pack_2_price);
        package3 = findViewById(R.id.detail_sp_pack_3);
        package3price = findViewById(R.id.detail_sp_pack_3_price);
        edit = findViewById(R.id.detail_sp_edit_ad);
        rating = findViewById(R.id.sp_feed_details_rating);
        reviewers = findViewById(R.id.sp_feed_reviewers);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("PHOTO").getValue().toString()).resize(1000, 700).centerCrop().into(coverPhoto);
                title.setText(dataSnapshot.child("TITLE").getValue().toString());
                catagory.setText(dataSnapshot.child("CATAGORY").getValue().toString());
                description.setText(dataSnapshot.child("DESCRIPTION").getValue().toString());
                package1.setText(dataSnapshot.child("PACKAGE_1_DETAIL").getValue().toString());
                package1price.setText(dataSnapshot.child("PACKAGE_1_PRICE").getValue().toString()+" টাকা");
                package2.setText(dataSnapshot.child("PACKAGE_2_DETAIL").getValue().toString());
                package2price.setText(dataSnapshot.child("PACKAGE_2_PRICE").getValue().toString()+" টাকা");
                package3.setText(dataSnapshot.child("PACKAGE_3_DETAIL").getValue().toString());
                package3price.setText(dataSnapshot.child("PACKAGE_3_PRICE").getValue().toString()+" টাকা");
                checked = dataSnapshot.child("AVAILABILITY").getValue().toString();
                rating.setText(dataSnapshot.child("RATING").getValue().toString()+"★");
                reviewers.setText(dataSnapshot.child("REVIEWERS").getValue().toString()+" জন ব্যাবহারকারীর রেটিং");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SPFeedDetails.this, EditAd.class);
                intent.putExtra("REF", refKey);
                Log.d("CHECK",checked);
                intent.putExtra("CHK", checked);
                startActivity(intent);
            }
        });
    }
}
