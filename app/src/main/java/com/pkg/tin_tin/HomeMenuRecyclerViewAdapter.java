package com.pkg.tin_tin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeMenuRecyclerViewAdapter extends RecyclerView.Adapter<HomeMenuRecyclerViewHolder> {

    HomeActivity homeActivity;
    ArrayList<MenuDataModel> userDataArrayList;

    public HomeMenuRecyclerViewAdapter(HomeActivity homeActivity, ArrayList<MenuDataModel> userDataArrayList) {
        this.homeActivity = homeActivity;
        this.userDataArrayList = userDataArrayList;
    }

    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();



@NonNull
@Override
public HomeMenuRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater= LayoutInflater.from(homeActivity.getBaseContext());
        View view=layoutInflater.inflate(R.layout.home_menu_card,parent,false);
        return new HomeMenuRecyclerViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull HomeMenuRecyclerViewHolder holder,final int position){
        holder.setMenu(userDataArrayList.get(position).getMenu());
        holder.setCost(userDataArrayList.get(position).getCost());
        holder.setType(userDataArrayList.get(position).getType());
        holder.setQuantity(userDataArrayList.get(position).getQuantity());
}

    @Override
    public int getItemCount() {
        return userDataArrayList.size();
    }
}