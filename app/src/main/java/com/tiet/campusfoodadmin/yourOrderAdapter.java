package com.tiet.campusfoodadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class yourOrderAdapter extends RecyclerView.Adapter<yourOrderAdapter.ViewHolder> {
List<YourOrdersRVModel> list;

    public yourOrderAdapter(List<YourOrdersRVModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public yourOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.your_orders_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull yourOrderAdapter.ViewHolder holder, int position) {
        holder.total.setText(list.get(position).getTotal());
        holder.description.setText(list.get(position).getDescription());
        holder.name.setText(list.get(position).getName());
        holder.phNO.setText(list.get(position).getPhNO());
        holder.isPending.setText(list.get(position).getIsPending());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,description,total,isPending,phNO;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.customer_name);
            description=itemView.findViewById(R.id.order_detail);
            total=itemView.findViewById(R.id.order_total);
            phNO=itemView.findViewById(R.id.customer_phno);
            isPending=itemView.findViewById(R.id.order_status);
        }
    }
}
