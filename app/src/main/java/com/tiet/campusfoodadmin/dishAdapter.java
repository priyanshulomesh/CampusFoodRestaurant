package com.tiet.campusfoodadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.internal.StorageReferenceUri;

import java.time.Instant;
import java.util.ArrayList;

public class dishAdapter extends RecyclerView.Adapter<dishAdapter.DishViewHolder>{
    ArrayList<DishDetailStorage> dishDetailStorage;
    Context context;


    public dishAdapter(Context context,ArrayList<DishDetailStorage> dishDetailStorage) {
        this.dishDetailStorage = dishDetailStorage;
        this.context = context;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listed_dishes_rv,parent,false);
        DishViewHolder dishViewHolder=new DishViewHolder(view);
        return dishViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {

        DishDetailStorage dishDetailStorage1= dishDetailStorage.get(position);

        Glide.with(context).load(dishDetailStorage1.getImage()).into(holder.dishViewImage);
        holder.dishViewName.setText(dishDetailStorage1.getDishName());
        holder.dishViewDescription.setText(dishDetailStorage1.getDishDescription());
        holder.dishViewPrice.setText(dishDetailStorage1.getDishPrice());

        holder.dishCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailedDishActivity.class);
                intent.putExtra("Image",dishDetailStorage.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("Description",dishDetailStorage.get(holder.getAdapterPosition()).getDishDescription());
                intent.putExtra("Name",dishDetailStorage.get(holder.getAdapterPosition()).getDishName());
                intent.putExtra("Price",dishDetailStorage.get(holder.getAdapterPosition()).getDishPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishDetailStorage.size();
    }
    public void searchDishData(ArrayList<DishDetailStorage>searchList)
    {
        dishDetailStorage=searchList;//searched data to push in rv array
        notifyDataSetChanged();//adapter refresh
    }
    public static class DishViewHolder extends RecyclerView.ViewHolder
    {
        ImageView dishViewImage;
        TextView dishViewName;
        TextView dishViewDescription;
        TextView dishViewPrice;
        CardView dishCard;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishViewName=itemView.findViewById(R.id.dish_view_name);
            dishCard=itemView.findViewById(R.id.dish_card);
            dishViewDescription=itemView.findViewById(R.id.dish_view_description);
            dishViewPrice=itemView.findViewById(R.id.dish_view_price);
            dishViewImage=itemView.findViewById(R.id.dish_view_image);
        }
    }
}
