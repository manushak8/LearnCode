package com.example.proglish2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViweAdapter extends RecyclerView.Adapter<RecyclerViweAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<LessonsModel> lessonsModels;
    public RecyclerViweAdapter(Context context, ArrayList<LessonsModel> lessonsModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.lessonsModels = lessonsModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecyclerViweAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_virw_row, parent, false);
        return new RecyclerViweAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViweAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(lessonsModels.get(position).getLessonName());
        LessonsModel currentLesson = lessonsModels.get(position);

        holder.itemView.setOnClickListener(v -> {
            int startIndex = position * 3;
            Intent intent = new Intent(context, LessonQuizSelectionActivity.class);
            intent.putExtra("startIndex", startIndex);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lessonsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}