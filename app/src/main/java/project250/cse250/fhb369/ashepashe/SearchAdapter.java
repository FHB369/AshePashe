package project250.cse250.fhb369.ashepashe;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.GENFeedViewHolder> {
    Context context;
    ArrayList<GENFeedItems> items = new ArrayList<>();

    public SearchAdapter(Context context, DatabaseReference ref, String UID, String search, String cat){
        this.context = context;

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                System.out.println(dataSnapshot.getChildren().toString());
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    String title = (String) postSnapShot.child("TITLE").getValue();
                    String price1 = (String) postSnapShot.child("PACKAGE_1_PRICE").getValue();
                    String price2 = (String) postSnapShot.child("PACKAGE_2_PRICE").getValue();
                    String price3 = (String) postSnapShot.child("PACKAGE_3_PRICE").getValue();
                    String centerLat = (String) postSnapShot.child("LATITUDE").getValue();
                    String centerLon = (String) postSnapShot.child("LONGITUDE").getValue();
                    String radius = (String) postSnapShot.child("COVERAGE_RADIUS").getValue();
                    String UID = (String) postSnapShot.child("UID").getValue();
                    String AVAIL = (String) postSnapShot.child("AVAILABILITY").getValue();
                    String rating = (String) postSnapShot.child("RATING").getValue();
                    String catagory = (String) postSnapShot.child("CATAGORY").getValue();
                    String titleLow = title.toLowerCase();
                    String ser = search.toLowerCase();

                    LatLng center = null;

                    if(centerLat!=null && centerLon!=null) {
                        center = new LatLng(Double.parseDouble(centerLat), Double.parseDouble(centerLon));
                    }
                    LatLng current = null;

                    if (GEN_Home.currentLocation!=null){
                        current = GEN_Home.currentLocation;
                    }

                    double distance = calculateDistance(center, current);

                    int min=0;

                    if(price1!=null && price2!=null && price3!=null) {
                        int a = Integer.parseInt(price1);
                        int b = Integer.parseInt(price2);
                        int c = Integer.parseInt(price3);

                        min = Math.min(Math.min(a,b),c);
                    }

                    String price;
                    if(min==0){
                        price=price1;
                    }else{
                        price = String.valueOf(min);
                    }

                    String photo = (String) postSnapShot.child("PHOTO").getValue();
                    String key = (String) postSnapShot.getKey().toString();

                    if(AVAIL.equals("AVAILABLE") && distance<=Double.parseDouble(radius)){
                        if(titleLow.contains(ser) && cat.equals("ALL")) {
                            GENFeedItems genFeedItems = new GENFeedItems(title, photo, price, key, rating + "★");
                            items.add(genFeedItems);
                        }else if(titleLow.contains(ser) && cat.equals(catagory)){
                            GENFeedItems genFeedItems = new GENFeedItems(title, photo, price, key, rating + "★");
                            items.add(genFeedItems);
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        });
    }

    private double deg2rad(double deg){
        return deg*(Math.PI/180);
    }

    private double calculateDistance(LatLng a, LatLng b){
        int R = 6371;
        double lat1 = a.getLatitude();
        double lat2 = b.getLatitude();
        double lon1 = a.getLongitude();
        double lon2 = b.getLongitude();

        double dLat = deg2rad(lat2-lat1);
        double dLon = deg2rad(lon2-lon1);

        double ans = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(ans), Math.sqrt(1-ans));
        double d = R * c;
        return d;
    }

    @NonNull
    @Override
    public GENFeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_gen_home, null);
        return new GENFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GENFeedViewHolder genFeedViewHolder, int i) {
        GENFeedItems item = items.get(i);
        genFeedViewHolder.TITLE.setText(item.getTITLE());
        genFeedViewHolder.PACKAGE_1_PRICE.setText(item.getPACKAGE_1_PRICE());
        genFeedViewHolder.RATING.setText(item.getRating());
        Picasso.get()
                .load(item.getPHOTO())
                .resize(800,600)
                .centerCrop()
                .into(genFeedViewHolder.PHOTO);

        genFeedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GENFeedDetails.class);
                intent.putExtra("RefKey", item.getkey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class GENFeedViewHolder extends RecyclerView.ViewHolder {

        ImageView PHOTO;
        TextView TITLE, PACKAGE_1_PRICE, RATING;

        public GENFeedViewHolder(@NonNull View itemView) {
            super(itemView);

            PHOTO = itemView.findViewById(R.id.cover_photo_feed_gen);
            TITLE = itemView.findViewById(R.id.title_feed_gen);
            PACKAGE_1_PRICE = itemView.findViewById(R.id.price_feed_gen);
            RATING = itemView.findViewById(R.id.rating_feed_gen);
        }
    }
}
