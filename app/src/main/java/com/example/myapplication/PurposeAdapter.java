package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PurposeAdapter extends RecyclerView.Adapter<PurposeAdapter.MyViewHolder> {
    public PurposeAdapter(Context context, ArrayList<CreatePurpose.Record> list) {
        this.context = context;
        this.list = list;
    }

    Context context;
    ArrayList<CreatePurpose.Record> list;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.purposeview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CreatePurpose.Record purpose = list.get(position);
        holder.purpose.setText(purpose.getPurpose());
        holder.code.setText(purpose.getCode());
        holder.type.setText(purpose.getType());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView purpose,code,type;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            purpose = itemView.findViewById(R.id.v_purpose);
            code = itemView.findViewById(R.id.v_purpose_code);
            type = itemView.findViewById(R.id.v_purpose_type);
        }
    }
}
