package project250.cse250.fhb369.ashepashe;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminFeedAdapter extends RecyclerView.Adapter<AdminFeedAdapter.AdminViewHolder> {
    Context context;
    ArrayList<AdminItem> items = new ArrayList<>();

    public AdminFeedAdapter(Context context, DatabaseReference ref){
        this.context = context;

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    String Name = (String) postSnapShot.child("COMPLAINER").getValue();
                    String Message = (String) postSnapShot.child("MESSAGE").getValue();
                    String Key = (String) postSnapShot.getKey();

                    AdminItem item = new AdminItem(Name, Message, Key);
                    items.add(item);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_feed_item, null);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder requestViewHolder, int i) {
        AdminItem item = items.get(items.size()-i-1);
        requestViewHolder.NAME.setText(item.getName());
        requestViewHolder.MESSAGE.setText(item.getMessage());

        requestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminAction.class);
                intent.putExtra("REF", item.getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class AdminViewHolder extends RecyclerView.ViewHolder {

        TextView NAME, MESSAGE;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            NAME = itemView.findViewById(R.id.sender_admin);
            MESSAGE = itemView.findViewById(R.id.message_admin);
        }
    }
}
