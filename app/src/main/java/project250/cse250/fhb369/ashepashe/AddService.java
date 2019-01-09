package project250.cse250.fhb369.ashepashe;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class AddService extends AppCompatActivity {

    public static LatLng center = null;
    public static double radius = 0.0;

    private Button back, setLocation, submit;
    private Spinner catagory;
    private ImageButton coverPhoto;
    private EditText title, description, package1details, package1price, package2details, package2price, package3details, package3price;
    private String catagoryIndex = null;
    private Uri coverPhotoUri = null;
    private ImageView shadow;
    private ProgressBar progressBar;

    private FirebaseUser mUser;
    private DatabaseReference mDatabase, mCatagoryDatabase, mUserDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        back = findViewById(R.id.buttonBack);

        catagory = findViewById(R.id.spinner_catagory);

        coverPhoto = findViewById(R.id.service_cover_photo);

        title = findViewById(R.id.service_title);
        description = findViewById(R.id.service_description);

        package1details = findViewById(R.id.service_package_1);
        package1price = findViewById(R.id.service_package_1_price);

        package2details = findViewById(R.id.service_package_2);
        package2price = findViewById(R.id.service_package_2_price);

        package3details = findViewById(R.id.service_package_3);
        package3price = findViewById(R.id.service_package_3_price);

        setLocation = findViewById(R.id.btn_set_location);

        submit = findViewById(R.id.service_submit);

        shadow = findViewById(R.id.shadow_add_service);
        progressBar = findViewById(R.id.progressBar_add_service);

        shadow.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.service_catagory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catagory.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        catagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:{
                        catagoryIndex = "REPAIR";
                        return;
                    }
                    case 2:{
                        catagoryIndex = "LAUNDRY";
                        return;
                    }
                    case 3:{
                        catagoryIndex = "TRANSPORT";
                        return;
                    }
                    case 4:{
                        catagoryIndex = "FOOD";
                        return;
                    }
                    case 5:{
                        catagoryIndex = "CLEANING";
                        return;
                    }
                    case 6:{
                        catagoryIndex = "SHIFTING";
                        return;
                    }
                    case 7:{
                        catagoryIndex = "HEALTH CARE";
                        return;
                    }
                    case 8:{
                        catagoryIndex = "OTHERS";
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

        setLocation.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(AddService.this, SelectArea.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left_exit);
                }else{
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            2000);
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

                if(catagoryIndex==null){
                    Toast.makeText(getApplicationContext(), "Please select a catagory first.", Toast.LENGTH_SHORT).show();
                }else if(coverPhotoUri==null){
                    Toast.makeText(getApplicationContext(), "Please select a cover photo.", Toast.LENGTH_SHORT).show();
                }else if(adTitle.isEmpty()){
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
                }else if(center==null && radius==0.0){
                    Toast.makeText(getApplicationContext(), "Please select a coverage area.", Toast.LENGTH_SHORT).show();
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
        mCatagoryDatabase = mDatabase.child(catagoryIndex);
        mStorage = FirebaseStorage.getInstance().getReference();

        final StorageReference ref = mStorage.child("AD_IMAGES/"+adTitle+"_"+mUser.getUid().toString()+".jpg");
        ref.putFile(coverPhotoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                DatabaseReference mRef = mDatabase.push();

                                mRef.child("UID").setValue(mUser.getUid());
                                mRef.child("AVAILABILITY").setValue("AVAILABLE");
                                mRef.child("TITLE").setValue(adTitle);
                                mRef.child("DESCRIPTION").setValue(adDetail);
                                mRef.child("PHOTO").setValue(downloadUrl);
                                mRef.child("CATAGORY").setValue(catagoryIndex);
                                mRef.child("LATITUDE").setValue(String.valueOf(center.getLatitude()));
                                mRef.child("LONGITUDE").setValue(String.valueOf(center.getLongitude()));
                                mRef.child("COVERAGE_RADIUS").setValue(String.valueOf(radius));
                                mRef.child("PACKAGE_1_DETAIL").setValue(adPac1);
                                mRef.child("PACKAGE_1_PRICE").setValue(adPac1Price);
                                mRef.child("PACKAGE_2_DETAIL").setValue(adPac2);
                                mRef.child("PACKAGE_2_PRICE").setValue(adPac2Price);
                                mRef.child("PACKAGE_3_DETAIL").setValue(adPac3);
                                mRef.child("PACKAGE_3_PRICE").setValue(adPac3Price);
                                mRef.child("RATING").setValue("0");
                                mRef.child("REVIEWERS").setValue("0");

                                shadow.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "Posted Ad Successfully", Toast.LENGTH_SHORT).show();
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
