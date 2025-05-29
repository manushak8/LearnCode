package com.example.itEnglish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private List<Word> wordList;

    public WordAdapter(List<Word> wordList) {
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_item, parent, false); // ԱՅՍՏԵՂ ՖԱՅԼԻ ԱՆՈՒՆԸ ՀԱՄԱՊԱՏԱՍԽԱՆԻ
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.wordText.setText(word.getWord());
        holder.translationText.setText(word.getTranslation());
        holder.exampleText.setText(word.getExample());
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void updateList(List<Word> newList) {
        this.wordList = newList;
        notifyDataSetChanged();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView wordText, translationText, exampleText;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordText = itemView.findViewById(R.id.wordText);
            translationText = itemView.findViewById(R.id.translationText);
            exampleText = itemView.findViewById(R.id.exampleText);
        }
    }
}