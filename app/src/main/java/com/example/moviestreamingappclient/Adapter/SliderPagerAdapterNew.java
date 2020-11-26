package com.example.moviestreamingappclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.moviestreamingappclient.Model.SliderSide;
import com.example.moviestreamingappclient.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SliderPagerAdapterNew extends PagerAdapter {

    //Code this to get slide from firebase to MainActivity

    private Context mContext;
    List<SliderSide> mList;

    public SliderPagerAdapterNew(Context mContext, List<SliderSide> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout= inflater.inflate(R.layout.slide_item,null);
        ImageView slideImage = slideLayout.findViewById(R.id.slide_img);
        TextView slideTitle = slideLayout.findViewById(R.id.slide_title);

        FloatingActionButton floatingActionButton=slideLayout.findViewById(R.id.floatingActionButton);
        Glide.with(mContext).load(mList.get(position).getVideo_thumb()).into(slideImage);
        slideTitle.setText(mList.get(position).getVideo_name()+"\n"+mList.get(position).getVideo_desc());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We will do latter something here

            }
        });

         container.addView(slideLayout);
         return slideLayout;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
