//package com.pkg.tin_tin;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewHolder> {
//
//    EditDataActivity editDataActivity;
//    ArrayList<MenuDataModel> userDataArrayList;
//
//    public MenuRecyclerViewAdapter(EditDataActivity editDataActivity, ArrayList<MenuDataModel> userDataArrayList) {
//        this.editDataActivity = editDataActivity;
//        this.userDataArrayList = userDataArrayList;
//    }
//
//    @NonNull
//    @Override
//    public MenuRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater =LayoutInflater.from(editDataActivity.getBaseContext());
//        View view = layoutInflater.inflate(R.layout.menu_card,parent,false);
//        return new MenuRecyclerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MenuRecyclerViewHolder holder, int position) {
//            holder.setMenu(userDataArrayList.get(position).getMenu());
//            holder.setCost(userDataArrayList.get(position).getCost());
//            holder.setType(userDataArrayList.get(position).getType());
//            holder.setQuantity(userDataArrayList.get(position).getQuantity());
//    }
//
//    @Override
//    public int getItemCount() {
//        return userDataArrayList.size();
//    }
//}
