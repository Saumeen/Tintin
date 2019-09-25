package com.pkg.tin_tin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditMenuRecyclerViewAdapter extends RecyclerView.Adapter<EditMenuRecyclerViewHolder> {

    EditDataActivity editDataActivity;
    ArrayList<MenuDataModel> userDataArrayList;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public EditMenuRecyclerViewAdapter(EditDataActivity editDataActivity, ArrayList<MenuDataModel> userDataArrayList) {
        this.editDataActivity = editDataActivity;
        this.userDataArrayList = userDataArrayList;
    }

    @NonNull
    @Override
    public EditMenuRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(editDataActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.edit_menu_card,parent,false);
        return new EditMenuRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditMenuRecyclerViewHolder holder, final int position) {
            holder.setMenu(userDataArrayList.get(position).getMenu());
            holder.setCost(userDataArrayList.get(position).getCost());
            holder.setType(userDataArrayList.get(position).getType());
            holder.setQuantity(userDataArrayList.get(position).getQuantity());
            holder.remove.setTag(position);
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   remove(v,position);
                    //Toast.makeText(v.getContext(),position+"Remove button",Toast.LENGTH_LONG).show();
                }
            });


    }

    private void remove(final View v, final int position) {
        Log.d("In",firebaseUser.getEmail());
        db.collection("SupplierUsers").whereEqualTo("Email",firebaseUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot qs = task.getResult();
                    List<DocumentSnapshot> list = qs.getDocuments();
                    removeData(list.get(0).getId(),position,v);
                }
            }
        });
    }

    private void removeData(String id, final int position, final View v) {

        final CollectionReference collectionReference = db.collection("SupplierUsers").document(id).collection("Menu");
        Query query = collectionReference.whereEqualTo("Menu",userDataArrayList.get(position).getMenu())
                .whereEqualTo("Cost",userDataArrayList.get(position).getCost());
        Log.d("In--",query.get().toString());
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("In--","ONcomplate");
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        Log.d("In--","Before delete");
                        collectionReference.document(documentSnapshot.getId()).delete();
                        userDataArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, userDataArrayList.size());
                        Toast.makeText(v.getContext(), "Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(v.getContext(), "Failer----", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDataArrayList.size();
    }
}
