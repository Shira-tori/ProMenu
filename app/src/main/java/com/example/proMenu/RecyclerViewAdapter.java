package com.example.proMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private String[] storesArray;

    public RecyclerViewAdapter(Context context, String[] stores) {
        storesArray = stores;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_layout, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.getStoreName().setText(storesArray[position]);
    }

    @Override
    public int getItemCount() {
        return storesArray.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView storeNameText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeNameText = itemView.findViewById(R.id.storeNameText);
        }

        public TextView getStoreName(){
            return storeNameText;
        }
    }

}
