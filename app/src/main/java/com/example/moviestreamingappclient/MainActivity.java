package com.example.moviestreamingappclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.moviestreamingappclient.Adapter.MovieShowsAdapter;
import com.example.moviestreamingappclient.Adapter.SliderPagerAdapterNew;
import com.example.moviestreamingappclient.Model.GetVideoDetails;
import com.example.moviestreamingappclient.Model.MovieItemClickListenerNew;
import com.example.moviestreamingappclient.Model.SliderSide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.ActionBar.*;

public class MainActivity extends AppCompatActivity  implements MovieItemClickListenerNew {

    MovieShowsAdapter movieShowsAdapter;
    DatabaseReference mDatabaseReference;
    private List<GetVideoDetails> uploads,uploadsListLatests,uploadsListPopular;
    private List<GetVideoDetails> actionsMovies,sportsMovies,comedyMovies,romanticMovies,adventureMovies;
    private ViewPager sliderPager;
    private List<SliderSide> uploadsSlider;
    private TabLayout tabMoviesActions,indicator;
    private RecyclerView moviesRv,moviesRvweek,tab;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        progressDialog=new ProgressDialog(this);
        inViews();
        addAllMovies();
        iniPopularMovies();
        iniWeekMovies();
        moviesViewTab();
    }

    private void addAllMovies()
    {
        uploads =new ArrayList<>();
        uploadsListLatests =new ArrayList<>();
        uploadsListPopular =new ArrayList<>();
        actionsMovies =new ArrayList<>();
        adventureMovies =new ArrayList<>();
        comedyMovies =new ArrayList<>();
        sportsMovies =new ArrayList<>();
        romanticMovies =new ArrayList<>();
        uploadsSlider =new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postSnapshot: snapshot.getChildren())
                {
                    GetVideoDetails upload= postSnapshot.getValue(GetVideoDetails.class);
                    SliderSide slide= postSnapshot.getValue(SliderSide.class);
                    if(upload.getVideo_type().equals("Latest Movies"))
                    {
                        uploadsListLatests.add(upload);
                    }
                    if(upload.getVideo_type().equals("Best Popular Movies"))
                    {
                        uploadsListPopular.add(upload);
                    }
                    if(upload.getVideo_category().equals("Action"))
                    {
                        actionsMovies.add(upload);
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
                    if(upload.getVideo_category().equals("Sports"))
                    {
                        sportsMovies.add(upload);
                    }
                    if(upload.getVideo_slide().equals("Slide Movies"))
                    {
                        uploadsSlider.add(slide);
                    }

                    uploads.add(upload);
                }
                iniSlider();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniSlider() {
        SliderPagerAdapterNew adapterNew =new SliderPagerAdapterNew(this,uploadsSlider);
        sliderPager.setAdapter(adapterNew);
        adapterNew.notifyDataSetChanged();
        //Setup timer

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),4000,6000);
        indicator.setupWithViewPager(sliderPager,true);

    }

    private void iniWeekMovies()
        {
            movieShowsAdapter =new MovieShowsAdapter(this,uploadsListLatests,this);
            moviesRvweek.setAdapter(movieShowsAdapter);
            moviesRvweek.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieShowsAdapter.notifyDataSetChanged();
        }
    private void iniPopularMovies()
    {
        movieShowsAdapter =new MovieShowsAdapter(this,uploadsListPopular,this);
        moviesRv.setAdapter(movieShowsAdapter);
        moviesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowsAdapter.notifyDataSetChanged();
    }

    private void moviesViewTab()
    {
        getActionMovies();
        tabMoviesActions.addTab(tabMoviesActions.newTab().setText("Action"));
        tabMoviesActions.addTab(tabMoviesActions.newTab().setText("Adventure"));
        tabMoviesActions.addTab(tabMoviesActions.newTab().setText("Comedy"));
        tabMoviesActions.addTab(tabMoviesActions.newTab().setText("Romantic"));
        tabMoviesActions.addTab(tabMoviesActions.newTab().setText("Sports"));
        tabMoviesActions.setTabGravity(TabLayout.GRAVITY_FILL);
        tabMoviesActions.setTabTextColors(ColorStateList.valueOf(Color.WHITE));

        tabMoviesActions.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                        getActionMovies();
                        break;
                    case 1:
                        getAdventureMovies();
                        break;
                    case 2:
                        getComedyMovies();
                        break;
                    case 3:
                        getRomanticMovies();
                        break;
                    case 4:
                        getSportsMovies();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void inViews()
    {
        tabMoviesActions=findViewById(R.id.tabActionMovies);
        indicator=findViewById(R.id.indicator);
        sliderPager=findViewById(R.id.slider_pager);
        moviesRvweek=findViewById(R.id.rv_movies_week);
        moviesRv=findViewById(R.id.Rv_movies);
        tab=findViewById(R.id.tabrecycler);
    }

    @Override
    public void onMovieClick(GetVideoDetails movie, ImageView imageView) {
    Intent in = new Intent(MainActivity.this,MovieDetailActivity.class);
    in.putExtra("title",movie.getVideo_name());
    in.putExtra("imgURL",movie.getVideo_thumb());
    in.putExtra("imgCover",movie.getVideo_thumb());
    in.putExtra("movieDetails",movie.getVideo_desc());
    in.putExtra("movieUrl",movie.getVideo_url());
    in.putExtra("movieCategory",movie.getVideo_category());
    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView,"sharedName");
    startActivity(in,options.toBundle());


    }

    public class SliderTimer extends TimerTask
    {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(sliderPager.getCurrentItem()<uploadsSlider.size()-1)
                    {
                        sliderPager.setCurrentItem(sliderPager.getCurrentItem()+1);
                    }
                    else
                        {
                            sliderPager.setCurrentItem(0);
                        }
                }
            });
        }
    }

    private void getActionMovies()
    {
        movieShowsAdapter =new MovieShowsAdapter(this,actionsMovies,this);
        tab.setAdapter(movieShowsAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowsAdapter.notifyDataSetChanged();
    }

    private void getSportsMovies()
    {
        movieShowsAdapter =new MovieShowsAdapter(this,sportsMovies,this);
        tab.setAdapter(movieShowsAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowsAdapter.notifyDataSetChanged();
    }

    private void getRomanticMovies()
    {
        movieShowsAdapter =new MovieShowsAdapter(this,romanticMovies,this);
        tab.setAdapter(movieShowsAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowsAdapter.notifyDataSetChanged();
    }

    private void getComedyMovies()
    {
        movieShowsAdapter =new MovieShowsAdapter(this,comedyMovies,this);
        tab.setAdapter(movieShowsAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowsAdapter.notifyDataSetChanged();
    }

    private void getAdventureMovies()
    {
        movieShowsAdapter =new MovieShowsAdapter(this,adventureMovies,this);
        tab.setAdapter(movieShowsAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowsAdapter.notifyDataSetChanged();
    }
}
