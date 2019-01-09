package project250.cse250.fhb369.ashepashe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.NavigableMap;
import java.util.concurrent.Executor;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpGeneral.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpGeneral#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpGeneral extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SignUpGeneral() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpGeneral.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpGeneral newInstance(String param1, String param2) {
        SignUpGeneral fragment = new SignUpGeneral();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    ImageButton photoPicker;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private ProgressBar progressBar;
    private ImageView shadow;
    public String photo_uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_sign_up_general, container, false);

        photoPicker = v.findViewById(R.id.photo_chooser_gen);
        progressBar = v.findViewById(R.id.progressBar_gen);
        shadow = v.findViewById(R.id.shadow_sign_up_gen);
        shadow.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        final EditText name = v.findViewById(R.id.name_gen),
                email = v.findViewById(R.id.email_gen),
                password = v.findViewById(R.id.password_gen),
                mobile = v.findViewById(R.id.mobile_gen);
        Button signUpGen = v.findViewById(R.id.btn_sign_up_gen);

        photoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
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

        signUpGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String NAME = name.getText().toString().trim(),
                        EMAIL = email.getText().toString().trim(),
                        PASSWORD = password.getText().toString(),
                        MOBILE = mobile.getText().toString();

                if(uri == null){
                    Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
                else if(NAME.isEmpty()){
                    name.setError("Please type your name");
                }
                else if(EMAIL.isEmpty()){
                    email.setError("Please give a valid email");
                }
                else if(PASSWORD.isEmpty()){
                    password.setError("Password can't be empty");
                }
                else if(MOBILE.isEmpty()){
                    mobile.setError("Give a valid mobile number");
                }
                else {
                    shadow.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    createUser(NAME, EMAIL, PASSWORD, MOBILE);
                }
            }
        });

        return v;
    }


    private void createUser(final String NAME, final String EMAIL, String PASSWORD, String MOBILE){
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String UID = user.getUid();

                            mStorageRef = FirebaseStorage.getInstance().getReference();

                            final StorageReference riversRef = mStorageRef.child("PROFILE_IMAGES/GEN/"+NAME+" "+UID+".jpg");

                            riversRef.putFile(uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String downloadUrl = uri.toString();
                                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS").child("GENERAL_USERS").child(UID);
                                                    mDatabase.child("NAME").setValue(NAME);
                                                    mDatabase.child("EMAIL").setValue(EMAIL);
                                                    mDatabase.child("PHOTO").setValue(downloadUrl);
                                                    mDatabase.child("MOBILE_NO").setValue(MOBILE);
                                                    Toast.makeText(getActivity(), "Welcome "+NAME,
                                                            Toast.LENGTH_SHORT).show();
                                                    shadow.setVisibility(View.GONE);
                                                    progressBar.setVisibility(View.GONE);
                                                    Intent intent = new Intent(getActivity(), GEN_Home.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(getActivity(), "Upload image failed\nPlease select a valid image",
                                                    Toast.LENGTH_SHORT).show();
                                            shadow.setVisibility(View.GONE);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                            shadow.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                        .setAspectRatio(1,1)
                        .start(getContext(), this);
                Log.d("BAPPY", "OK1");
            }

            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Log.d("BAPPY", "OK2");
                    Uri resultUri = result.getUri();
                    Log.d("BAPPY", resultUri.toString());
                    Bitmap bitmapImage = null;
                    try {
                        //bitmapImage = new Compressor(getActivity()).compressToBitmap(new File(resultUri.toString()));
                        bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    photoPicker.setImageBitmap(bitmapImage);
                    uri = resultUri;
            }
        }else{
            Log.d("BAPPY", "NOT WORKING");
        }



        Glide
                .with(this)
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                .circleCrop())
                .into(photoPicker);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
