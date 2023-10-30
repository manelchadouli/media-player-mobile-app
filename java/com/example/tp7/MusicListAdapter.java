package com.example.tp7;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    DatabaseHelper databaseHelper;
    ArrayList<AudioModel> songsList;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> songsList, Context context) {
        databaseHelper = new DatabaseHelper(context);
        this.songsList = songsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioModel songData = songsList.get(holder.getAdapterPosition());
        holder.titleTextView.setText(songData.getTitle());

        if (MyMediaPlayer.currentIndex == holder.getAdapterPosition()) {
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.titleTextView.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(v -> {
            MyMediaPlayer.getInstance().reset();
            MyMediaPlayer.currentIndex = position;
            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("LIST", songsList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        boolean isFavorite = databaseHelper.isFavorite(songData.getTitle());
        updateFavoriteState(isFavorite, holder.favoriteIcon);

        holder.favoriteIcon.setOnClickListener(v -> {
            boolean isFavoriteNow = !isFavorite;

            if (isFavoriteNow) {
                databaseHelper.addFavorite(songData);
            } else {
                databaseHelper.removeFavorite(songData.getTitle());
            }
            updateFavoriteState(isFavoriteNow, holder.favoriteIcon);
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView favoriteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            favoriteIcon = itemView.findViewById(R.id.icon_view);
        }
    }

    private void updateFavoriteState(boolean isFavorite, ImageView favoriteIcon) {
        if (isFavorite) {
            favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
    }
}
