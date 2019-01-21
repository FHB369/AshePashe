/**
 *Created by Faisal Haque Bappy on 07-Jan-19.
 */

package project250.cse250.fhb369.ashepashe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class EditAd extends AppCompatActivity {

    private Button back, submit;
    private Switch active;
    private ImageButton coverPhoto;
    private EditText title, description, package1details, package1price, package2details, package2price, package3details, package3price;
    private Uri coverPhotoUri = null;
    private ImageView shadow;
    private ProgressBar progressBar;

    private FirebaseUser mUser;
    private DatabaseReference mDatabase, mCatagoryDatabase, mUserDatabase;
    private StorageReference mStorage;
    private String refKey, checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ad);

        refKey = getIntent().getExtras().getString("REF");
        checked = getIntent().getExtras().getString("CHK");
        Log.d("CHECK",checked);

        back = findViewById(R.id.buttonBackEdit);

        active = findViewById(R.id.switchActive);

        coverPhoto = findViewById(R.id.edit_cover_photo);

        title = findViewById(R.id.edit_title);
        description = findViewById(R.id.edit_description);

        package1details = findViewById(R.id.edit_package_1);
        package1price = findViewById(R.id.edit_package_1_price);

        package2details = findViewById(R.id.edit_package_2);
        package2price = findViewById(R.id.edit_package_2_price);

        package3details = findViewById(R.id.edit_package_3);
        package3price = findViewById(R.id.edit_package_3_price);

        submit = findViewById(R.id.edit_submit);

        shadow = findViewById(R.id.shadow_edit_service);
        progressBar = findViewById(R.id.progressBar_edit_service);

        shadow.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        if(checked.equals("NOT AVAILABLE")){
            active.setChecked(false);
        }else if(checked.equals("AVAILABLE")){
            active.setChecked(true);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Ads").child(refKey);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("PHOTO").getValue().toString()).resize(1000, 600).centerCrop().into(coverPhoto);
                title.setText(dataSnapshot.child("TITLE").getValue().toString());
                description.setText(dataSnapshot.child("DESCRIPTION").getValue().toString());
                package1details.setText(dataSnapshot.child("PACKAGE_1_DETAIL").getValue().toString());
                package1price.setText(dataSnapshot.child("PACKAGE_1_PRICE").getValue().toString());
                package2details.setText(dataSnapshot.child("PACKAGE_2_DETAIL").getValue().toString());
                package2price.setText(dataSnapshot.child("PACKAGE_2_PRICE").getValue().toString());
                package3details.setText(dataSnapshot.child("PACKAGE_3_DETAIL").getValue().toString());
                package3price.setText(dataSnapshot.child("PACKAGE_3_PRICE").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checked="NOT AVAILABLE";
                }else{
                    checked="AVAILABLE";
                }
            }
        });

        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
                else {
                    startGallery();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adTitle, adDetail, adPac1, adPac1Price, adPac2, adPac2Price, adPac3, adPac3Price;

                adTitle = title.getText().toString().trim();
                adDetail = description.getText().toString().trim();

                adPac1 = package1details.getText().toString().trim();
                adPac1Price = package1price.getText().toString().trim();

                adPac2 = package2details.getText().toString().trim();
                adPac2Price = package2price.getText().toString().trim();

                adPac3 = package3details.getText().toString().trim();
                adPac3Price = package3price.getText().toString().trim();

                if(adTitle.isEmpty()){
                    title.setError("Title can't be empty");
                }else if(adDetail.isEmpty()){
                    description.setError("Please give a description");
                }else if(adPac1.isEmpty()){
                    package1details.setError("Please give a description");
                }else if(adPac1Price.isEmpty()){
                    package1price.setError("Please set a price");
                }else if(adPac2.isEmpty()){
                    package2details.setError("Please give a description");
                }else if(adPac2Price.isEmpty()) {
                    package2price.setError("Please set a price");
                }else if(adPac3.isEmpty()){
                    package3details.setError("Please give a description");
                }else if(adPac3Price.isEmpty()) {
                    package3price.setError("Please set a price");
                }else{
                    postAd(adTitle, adDetail, adPac1, adPac1Price, adPac2, adPac2Price, adPac3, adPac3Price);
                }
            }
        });
    }

    private void postAd(String adTitle, String adDetail, String adPac1, String adPac1Price, String adPac2, String adPac2Price, String adPac3, String adPac3Price) {
        shadow.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("SERVICE_PROVIDERS").child(mUser.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Ads");
        mStorage = FirebaseStorage.getInstance().getReference();
        if(coverPhotoUri==null){
            DatabaseReference mRef = mDatabase.child(refKey);

            mRef.child("AVAILABILITY").setValue(checked);
            mRef.child("TITLE").setValue(adTitle);
            mRef.child("DESCRIPTION").setValue(adDetail);
            mRef.child("PACKAGE_1_DETAIL").setValue(adPac1);
            mRef.child("PACKAGE_1_PRICE").setValue(adPac1Price);
            mRef.child("PACKAGE_2_DETAIL").setValue(adPac2);
            mRef.child("PACKAGE_2_PRICE").setValue(adPac2Price);
            mRef.child("PACKAGE_3_DETAIL").setValue(adPac3);
            mRef.child("PACKAGE_3_PRICE").setValue(adPac3Price);

            shadow.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Posted Edited Successfully", Toast.LENGTH_SHORT).show();
            onBackPressed();
            finish();
        }else {
            final StorageReference ref = mStorage.child("AD_IMAGES/" + adTitle + "_" + mUser.getUid().toString() + ".jpg");
            ref.putFile(coverPhotoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    DatabaseReference mRef = mDatabase.child(refKey);

                                    mRef.child("AVAILABILITY").setValue(checked);
                                    mRef.child("TITLE").setValue(adTitle);
                                    mRef.child("DESCRIPTION").setValue(adDetail);
                                    mRef.child("PHOTO").setValue(downloadUrl);
                                    mRef.child("PACKAGE_1_DETAIL").setValue(adPac1);
                                    mRef.child("PACKAGE_1_PRICE").setValue(adPac1Price);
                                    mRef.child("PACKAGE_2_DETAIL").setValue(adPac2);
                                    mRef.child("PACKAGE_2_PRICE").setValue(adPac2Price);
                                    mRef.child("PACKAGE_3_DETAIL").setValue(adPac3);
                                    mRef.child("PACKAGE_3_PRICE").setValue(adPac3Price);

                                    shadow.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(), "Posted Edited Successfully", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                    finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "Upload image failed\nPlease select a valid image",
                                    Toast.LENGTH_SHORT).show();
                            shadow.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == 1000){
                Uri imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(4,3)
                        .start(this);
            }

            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                Bitmap bitmapImage = null;
                try {
                    //bitmapImage = new Compressor(getActivity()).compressToBitmap(new File(resultUri.toString()));
                    bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                coverPhoto.setImageBitmap(bitmapImage);
                coverPhotoUri = resultUri;
            }
        }else{
            Log.d("BAPPY", "NOT WORKING");
        }



        Glide
                .with(this)
                .load(coverPhotoUri)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(coverPhoto);
    }
}
