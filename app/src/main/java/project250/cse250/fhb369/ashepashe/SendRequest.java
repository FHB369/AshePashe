/**
 *Created by Faisal Haque Bappy on 09-Jan-19.
 */

package project250.cse250.fhb369.ashepashe;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class SendRequest extends AppCompatActivity {

    private Button back, submit;
    private ImageView done;
    private TextView textView, formTitle;
    private EditText address, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        back = findViewById(R.id.buttonBackDetail);
        textView = findViewById(R.id.textViewComplete);
        done = findViewById(R.id.doneProgress);
        submit = findViewById(R.id.submit_request);
        formTitle = findViewById(R.id.send_request_form);
        address = findViewById(R.id.send_request_address);
        time = findViewById(R.id.send_request_time);

        done.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        String refKey = getIntent().getExtras().getString("REF");
        String pkg = getIntent().getExtras().getString("PKG");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Address = address.getText().toString().trim();
                String Time = time.getText().toString().trim();

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("REQUESTS");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Ads").child(refKey);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String reciever = dataSnapshot.child("UID").getValue().toString();
                        String title = dataSnapshot.child("TITLE").getValue().toString();
                        DatabaseReference request = mRef.push();
                        request.child("TITLE").setValue(title);
                        request.child("SENDER").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        request.child("RECIEVER").setValue(reciever);
                        request.child("AD").setValue(refKey);
                        request.child("PACKAGE").setValue(pkg);
                        request.child("STATUS").setValue("REQUESTED");
                        request.child("SENDER_LATITUDE").setValue(GEN_Home.currentLocation.getLatitude());
                        request.child("SENDER_LONGITUDE").setValue(GEN_Home.currentLocation.getLongitude());
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
                        String currentDateandTime = sdf.format(new Date());
                        request.child("DATE").setValue(currentDateandTime);
                        request.child("ADDRESS").setValue(Address);
                        request.child("TIME").setValue(Time);

                        formTitle.setVisibility(View.GONE);
                        address.setVisibility(View.GONE);
                        time.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                        done.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);

                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("USERS").child("SERVICE_PROVIDERS").child(reciever);
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Email = (String) dataSnapshot.child("EMAIL").getValue();
                                sendNotification(Email, title, pkg);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendNotification(String reciever, String title, String pkg) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    //This is a Simple Logic to Send Notification different Device Programmatically....
                    send_email = reciever;
                    String msg = "New request recieved\n"+title+" "+pkg;

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic YWU5NjQ1M2YtNmNiYy00ZjQyLWJlZDYtNDdlMTJiOGViOWY4");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"acc06a9c-7f7a-463a-9c0d-90ab4fa043b7\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \""+msg+"\"},"
                                + "\"large_icon\": \"ic_stat_onesignal_default\","
                                + "\"priority\": 10"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }
}
