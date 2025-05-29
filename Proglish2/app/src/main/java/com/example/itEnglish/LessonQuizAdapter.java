package com.example.itEnglish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

public class LessonQuizAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<LessonQuizItem> items;
    private RecyclerViewInterface recyclerViewInterface;

    public LessonQuizAdapter(Context context, ArrayList<LessonQuizItem> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.items = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LessonQuizItem.TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_topic_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.lesson_quiz_item, parent, false);
            return new ContentViewHolder(view, recyclerViewInterface);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LessonQuizItem item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            LessonQuizHeader header = (LessonQuizHeader) item;
            ((HeaderViewHolder) holder).headerTitle.setText(header.getTitle());

            // Progress binding
            if (header.getProgress() >= 0) {
                ((HeaderViewHolder) holder).headerProgress.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).headerProgress.setProgress(header.getProgress());
                ((HeaderViewHolder) holder).headerProgressText.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).headerProgressText.setText(header.getProgress() + "%");
            } else {
                ((HeaderViewHolder) holder).headerProgress.setVisibility(View.GONE);
                ((HeaderViewHolder) holder).headerProgressText.setVisibility(View.GONE);
            }
        } else if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).name.setText(((LessonQuizContent) item).getName());
        }
    }

    // ViewHolder-ները՝ ավելացրու ProgressBar և TextView հղումները
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;
        CircularProgressBar headerProgress;
        TextView headerProgressText;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.headerTitle);
            headerProgress = itemView.findViewById(R.id.headerProgress);
            headerProgressText = itemView.findViewById(R.id.headerProgressText);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ContentViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
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
