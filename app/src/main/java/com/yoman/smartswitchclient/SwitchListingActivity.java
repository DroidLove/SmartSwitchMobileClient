package com.yoman.smartswitchclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SwitchListingActivity extends AppCompatActivity {

    RecyclerView rv_custom_switch;
    SwitchAdapter mSwitchAdapter;
    ArrayList<ModelSwitch> mArrayList;
    String TAG = getClass().getName();
    String[] switchNames = new String[]{"Light 1", "Light 2", "Light 3", "Light 4", "Fan 1", "Fan 2", "Other 1", "Other 2"};
    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences mSharedPreferences;
    String isFirstLaunch = "";
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_listing);

        init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_custom_switch.setLayoutManager(layoutManager);

        mSwitchAdapter = new SwitchAdapter(this, mArrayList);
        rv_custom_switch.setAdapter(mSwitchAdapter);
    }

    private void init() {

        rv_custom_switch = (RecyclerView) findViewById(R.id.rv_custom_switch);
        progress_bar = (ProgressBar) findViewById(R.id.progress);
        database = FirebaseDatabase.getInstance();
        mArrayList = new ArrayList<>();
        Map<String, Object> sendPost = new HashMap<>();
        mSharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);

        isFirstLaunch = mSharedPreferences.getString("isFirstLaunch", "");
        myRef = database.getReference("switchlist");

        if (TextUtils.isEmpty(isFirstLaunch)) {

            // Restricting repeating values to firebase db
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("isFirstLaunch", "false");
            editor.commit();

            for (int i = 0; i < switchNames.length; i++) {
                if (switchNames[i].contains("Light")) {

                    ModelSwitch lightSwitch = new ModelSwitch(Contants.SWTICH_TYPE_LIGHTS, switchNames[i], "s" + i, false);
//                    mArrayList.add(lightSwitch);
                    myRef.child("s" + i).setValue(lightSwitch);

                } else if (switchNames[i].contains("Fan")) {

                    ModelSwitch fanSwitch = new ModelSwitch(Contants.SWTICH_TYPE_FANS, switchNames[i], "s" + i, false);
//                    mArrayList.add(fanSwitch);
                    myRef.child("s" + i).setValue(fanSwitch);

                } else if (switchNames[i].contains("Other")) {

                    ModelSwitch otherSwitch = new ModelSwitch(Contants.SWTICH_TYPE_OTHERS, switchNames[i], "s" + i, false);
//                    mArrayList.add(otherSwitch);
                    myRef.child("s" + i).setValue(otherSwitch);
                }
            }
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot swtchDataSnapshot : dataSnapshot.getChildren()) {
                    ModelSwitch modelSwitch = swtchDataSnapshot.getValue(ModelSwitch.class);
                    mArrayList.add(modelSwitch);
                    mSwitchAdapter.notifyDataSetChanged();
                }

                progress_bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                    ModelSwitch note = noteDataSnapshot.getValue(ModelSwitch.class);
//                    mArrayList.add(note);
//                    mSwitchAdapter.notifyDataSetChanged();
//                }
//
//                progress_bar.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                    ModelSwitch note = noteDataSnapshot.getValue(ModelSwitch.class);
//                    mArrayList.add(note);
//                    mSwitchAdapter.notifyDataSetChanged();
//                }
//
//                progress_bar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }

    private void initFirebaseRealtimeDatabase() {

        DatabaseReference myRef = database.getReference("message");

        Map<String, Object> sendPost = new HashMap<String, Object>();
        sendPost.put("ButtonStatus", 1);
        sendPost.put("ButtonStatus2", 1);
        myRef.setValue(sendPost);

        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
    }

    private class SwitchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<ModelSwitch> mArrayList;
        Context mContext;

        public SwitchAdapter(Context mContext, ArrayList<ModelSwitch> mArrayList) {
            this.mArrayList = mArrayList;
            this.mContext = mContext;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View convertView = LayoutInflater.from(mContext).inflate(R.layout.row_layout_switch, parent, false);
            MyViewHolder holder = new MyViewHolder(convertView);

            return holder;

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            initialSetup(((MyViewHolder) holder), position);

        }

        private void initialSetup(final MyViewHolder holder, final int position) {
            if (mArrayList.get(position).getSwitchType().equalsIgnoreCase(Contants.SWTICH_TYPE_LIGHTS)) {
                holder.image_switch_type.setImageResource(R.drawable.lightbulb);
            } else if (mArrayList.get(position).getSwitchType().equalsIgnoreCase(Contants.SWTICH_TYPE_FANS)) {
                holder.image_switch_type.setImageResource(R.drawable.fan);
            } else if (mArrayList.get(position).getSwitchType().equalsIgnoreCase(Contants.SWTICH_TYPE_OTHERS)) {
                holder.image_switch_type.setImageResource(R.drawable.switch_others);
            }

            holder.textView_switch_name.setText(mArrayList.get(position).getSwitchname());

            if (mArrayList.get(position).getSwitchStatus()) {
                holder.switch_status.setChecked(true);
            } else {
                holder.switch_status.setChecked(false);
            }

            holder.switch_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {

                        mArrayList.get(position).setSwitchStatus(true);

                        myRef.child(mArrayList.get(position).getSwitchNumber()).setValue(mArrayList.get(position));

                    } else {

                        mArrayList.get(position).setSwitchStatus(false);

                        myRef.child(mArrayList.get(position).getSwitchNumber()).setValue(mArrayList.get(position));
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView image_switch_type;
            TextView textView_switch_name;
            SwitchCompat switch_status;

            public MyViewHolder(View itemView) {
                super(itemView);
                image_switch_type = (ImageView) itemView.findViewById(R.id.image_switch_type);
                textView_switch_name = (TextView) itemView.findViewById(R.id.textView_switch_name);
                switch_status = (SwitchCompat) itemView.findViewById(R.id.switch_status);
            }

        }

    }

}
