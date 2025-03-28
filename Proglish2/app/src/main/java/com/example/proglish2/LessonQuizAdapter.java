package com.example.proglish2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LessonQuizAdapter extends RecyclerView.Adapter<LessonQuizAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LessonQuizModel> lessonQuizList;
    private RecyclerViewInterface recyclerViewInterface;

    public LessonQuizAdapter(Context context, ArrayList<LessonQuizModel> lessonQuizList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.lessonQuizList = lessonQuizList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lesson_quiz_item, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LessonQuizModel item = lessonQuizList.get(position);
        holder.name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return lessonQuizList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.lessonQuizName);

            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            });
        }
    }
}

