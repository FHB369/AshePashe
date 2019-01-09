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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SPFeedAdapter extends RecyclerView.Adapter<SPFeedAdapter.SPFeedViewHolder> {
    Context context;
    ArrayList<SPFeedItems> items = new ArrayList<>();

    public SPFeedAdapter(Context context, DatabaseReference ref, String UID){
        this.context = context;

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                System.out.println(dataSnapshot.getChildren().toString());
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    String title = (String) postSnapShot.child("TITLE").getValue();
                    String price1 = (String) postSnapShot.child("PACKAGE_1_PRICE").getValue();
                    String price2 = (String) postSnapShot.child("PACKAGE_2_PRICE").getValue();
                    String price3 = (String) postSnapShot.child("PACKAGE_3_PRICE").getValue();
                    String UID = (String) postSnapShot.child("UID").getValue();

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

                    if(UID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        SPFeedItems spFeedItems = new SPFeedItems(title, photo, price, key);
                        items.add(spFeedItems);
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

    @NonNull
    @Override
    public SPFeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_sp_services, null);
        return new SPFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SPFeedViewHolder spFeedViewHolder, int i) {
        SPFeedItems item = items.get(i);
        spFeedViewHolder.TITLE.setText(item.getTITLE());
        spFeedViewHolder.PACKAGE_1_PRICE.setText(item.getPACKAGE_1_PRICE());
        Picasso.get()
                .load(item.getPHOTO())
                .resize(800,600)
                .centerCrop()
                .into(spFeedViewHolder.PHOTO);

        spFeedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SPFeedDetails.class);
                intent.putExtra("RefKey", item.getkey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SPFeedViewHolder extends RecyclerView.ViewHolder {

        ImageView PHOTO;
        TextView TITLE, PACKAGE_1_PRICE;

        public SPFeedViewHolder(@NonNull View itemView) {
            super(itemView);

            PHOTO = itemView.findViewById(R.id.cover_photo_feed_sp);
            TITLE = itemView.findViewById(R.id.title_feed_sp);
            PACKAGE_1_PRICE = itemView.findViewById(R.id.price_feed_sp);
        }
    }
}
