package com.example.moviestreamingappclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviestreamingappclient.Model.GetVideoDetails;
import com.example.moviestreamingappclient.Model.MovieItemClickListenerNew;
import com.example.moviestreamingappclient.R;

import java.util.List;

public class MovieShowsAdapter extends RecyclerView.Adapter<MovieShowsAdapter.MyViewHolder> {

    private Context context;
    private List<GetVideoDetails> uploads;
    MovieItemClickListenerNew movieItemClickListenerNew;

    public MovieShowsAdapter(Context context, List<GetVideoDetails> uploads, MovieItemClickListenerNew movieItemClickListenerNew) {
        this.context = context;
        this.uploads = uploads;
        this.movieItemClickListenerNew = movieItemClickListenerNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(context).inflate(R.layout.movie_item_new, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieShowsAdapter.MyViewHolder holder, int position) {
            GetVideoDetails getVideoDetails= uploads.get(position);
            holder.tvTitle.setText(getVideoDetails.getVideo_name());
            Glide.with(context).load(getVideoDetails.getVideo_thumb()).into(holder.ImgMovie);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ImgMovie;
        ConstraintLayout container;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle= itemView.findViewById(R.id.item_movie_title);
            ImgMovie =itemView.findViewById(R.id.item_movie_img);
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieItemClickListenerNew.onMovieClick(uploads.get(getAdapterPosition()),ImgMovie);
                }
            });
        }
    }
}
