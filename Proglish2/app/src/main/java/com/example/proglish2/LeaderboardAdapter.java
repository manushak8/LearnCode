package com.example.proglish2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private final List<LeaderboardEntry> leaderboardEntries;

    public LeaderboardAdapter(List<LeaderboardEntry> leaderboardEntries) {
        this.leaderboardEntries = leaderboardEntries;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardEntry entry = leaderboardEntries.get(position);
        holder.rankTextView.setText(String.valueOf(position + 1));
        holder.userEmailTextView.setText(entry.getUserEmail());
        holder.scoreTextView.setText(String.valueOf(entry.getScore()));
    }

    @Override
    public int getItemCount() {
        return leaderboardEntries.size();
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView, userEmailTextView, scoreTextView;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}