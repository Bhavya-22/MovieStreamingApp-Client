package com.example.moviestreamingappclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviestreamingappclient.Adapter.MovieShowsAdapter;
import com.example.moviestreamingappclient.Model.GetVideoDetails;
import com.example.moviestreamingappclient.Model.MovieItemClickListenerNew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements MovieItemClickListenerNew {

    private ImageView MoviesThumbnail,MoviesCoverImg;
    TextView tv_title,tv_desc;
    FloatingActionButton playFab;
    RecyclerView RvCast, recyclerView_similarMovies;
    MovieShowsAdapter movieShowsAdapter;
    DatabaseReference mDatabaseRef;
    List<GetVideoDetails> uploads,actionMovies,sportsMovies,romanticMovies,adventureMovies,comedyMovies;
    String current_video_url;
    String current_video_category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        inView();
        similarMoviesRecycler();
        similarMovies();

        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MovieDetailActivity.this,MoviePlayerActivity.class);
                intent.putExtra("videoUri",current_video_url);
                startActivity(intent);
            }
        });

    }

    private void similarMovies() {
        if(current_video_category.equals("Action"))
        {
            movieShowsAdapter= new MovieShowsAdapter(this,actionMovies,this);

            recyclerView_similarMovies.setAdapter(movieShowsAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL,false));
            movieShowsAdapter.notifyDataSetChanged();
        }

        if(current_video_category.equals("Sports"))
        {
            movieShowsAdapter= new MovieShowsAdapter(this,sportsMovies,this);

            recyclerView_similarMovies.setAdapter(movieShowsAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL,false));
            movieShowsAdapter.notifyDataSetChanged();
        }
        if(current_video_category.equals("Adventure"))
        {
            movieShowsAdapter= new MovieShowsAdapter(this,adventureMovies,this);

            recyclerView_similarMovies.setAdapter(movieShowsAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL,false));
            movieShowsAdapter.notifyDataSetChanged();
        }
        if(current_video_category.equals("Romantic"))
        {
            movieShowsAdapter= new MovieShowsAdapter(this,romanticMovies,this);

            recyclerView_similarMovies.setAdapter(movieShowsAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL,false));
            movieShowsAdapter.notifyDataSetChanged();
        }
        if(current_video_category.equals("Comedy"))
        {
            movieShowsAdapter= new MovieShowsAdapter(this,comedyMovies,this);

            recyclerView_similarMovies.setAdapter(movieShowsAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL,false));
            movieShowsAdapter.notifyDataSetChanged();
        }
    }

    private void similarMoviesRecycler() {
        uploads= new ArrayList<>();
        sportsMovies= new ArrayList<>();
        comedyMovies= new ArrayList<>();
        actionMovies= new ArrayList<>();
        adventureMovies= new ArrayList<>();
        romanticMovies= new ArrayList<>();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Videos");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postSnapshot: snapshot.getChildren())
                {
                    GetVideoDetails upload= postSnapshot.getValue(GetVideoDetails.class);
                    if(upload.getVideo_category().equals("Action"))
                    {
                        actionMovies.add(upload);
                    }

                    if(upload.getVideo_category().equals("Sports"))
                    {
                        sportsMovies.add(upload);
                    }

                    if(upload.getVideo_category().equals("Adventure"))
                    {
                        adventureMovies.add(upload);
                    }

                    if(upload.getVideo_category().equals("Comedy"))
                    {
                        comedyMovies.add(upload);
                    }

                    if(upload.getVideo_category().equals("Romantic"))
                    {
                        romanticMovies.add(upload);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inView() {
        playFab=findViewById(R.id.play_fab);
        tv_title=findViewById(R.id.details_movie_title);
        tv_desc=findViewById(R.id.details_movie_desc);
        MoviesThumbnail=findViewById(R.id.details_movie_img);
        recyclerView_similarMovies=findViewById(R.id.recycler_similar_movies);
        MoviesCoverImg=findViewById(R.id.details_movie_cover);
        String movieTitle=getIntent().getExtras().getString("title");
        String imgResourceId=getIntent().getExtras().getString("imgURL");
        String imageCover=getIntent().getExtras().getString("imgCover");
        String movieDetailsText=getIntent().getExtras().getString("movieDetails");
        String movieUrl=getIntent().getExtras().getString("movieUrl");
        String movieCategory=getIntent().getExtras().getString("movieCategory");
        current_video_url=movieUrl;
        current_video_category=movieCategory;
        Glide.with(this).load(imgResourceId).into(MoviesThumbnail);
        Glide.with(this).load(imageCover).into(MoviesCoverImg);
        tv_title.setText(movieTitle);
        tv_desc.setText(movieDetailsText);
        Toast.makeText(this,"Title is" +movieTitle,Toast.LENGTH_SHORT).show();
        getSupportActionBar().setTitle(movieTitle);
    }

    @Override
    public void onMovieClick(GetVideoDetails movie, ImageView imageView)
    {
        tv_title.setText(movie.getVideo_name());
        getSupportActionBar().setTitle(movie.getVideo_name());
        Glide.with(this).load(movie.getVideo_thumb()).into(MoviesThumbnail);
        Glide.with(this).load(movie.getVideo_thumb()).into(MoviesCoverImg);
        tv_desc.setText(movie.getVideo_desc());
        current_video_url =movie.getVideo_url();
        current_video_category=movie.getVideo_category();
        Activity activity;
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(MovieDetailActivity.this, imageView,"sharedName" );
        options.toBundle();
    }
}
