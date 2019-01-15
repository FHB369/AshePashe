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

public class RequestAdapterGEN extends RecyclerView.Adapter<RequestAdapterGEN.RequestViewHolder> {
    Context context;
    ArrayList<RequestItem> items = new ArrayList<>();

    public RequestAdapterGEN(Context context, DatabaseReference ref, String UID){
        this.context = context;

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    String title = (String) postSnapShot.child("TITLE").getValue();
                    String pack = (String) postSnapShot.child("PACKAGE").getValue();
                    String date = (String) postSnapShot.child("DATE").getValue();
                    String status = (String) postSnapShot.child("STATUS").getValue();
                    String sender = (String) postSnapShot.child("SENDER").getValue();
                    String key = (String) postSnapShot.getKey();

                    if(status!=null) {
                        if (sender != null && sender.equals(UID) && !status.equals("DONE")) {
                            RequestItem item = new RequestItem(title, date, pack, status, key);
                            items.add(item);
                        }
                    }
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
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_gen_requests, null);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i) {
        RequestItem item = items.get(items.size()-i-1);
        requestViewHolder.TITLE.setText(item.getTitle());
        requestViewHolder.DATE.setText(item.getDate());
        requestViewHolder.PACKAGE.setText(item.getPkg());
        requestViewHolder.STATUS.setText(item.getStatus());

        requestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModifyRequest.class);
                intent.putExtra("REF", item.getKey());
                intent.putExtra("STAT", item.getStatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView TITLE, PACKAGE, DATE;
        Button STATUS;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            TITLE = itemView.findViewById(R.id.title_gen_req);
            PACKAGE = itemView.findViewById(R.id.pkg_gen_req);
            DATE = itemView.findViewById(R.id.date_gen_req);
            STATUS = itemView.findViewById(R.id.status_gen_req);
        }
    }
}
