package com.example.ashwinchauhan.imagegallrey.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ashwinchauhan.imagegallrey.R;
import com.example.ashwinchauhan.imagegallrey.model.Image;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyviewHolder> {

    List<Image> images;
    Context mContext;

    public GalleryAdapter(Context applicationContext, ArrayList<Image> images) {
        this.images=images;
        this.mContext=applicationContext;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Image image=images.get(position);

        Glide.with(mContext).load(image.getMedium())
                .thumbnail(0.9f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int positon);
    }

    public static class RecycleTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;


        public RecycleTouchListener(Context context,final RecyclerView recyclerView,final GalleryAdapter.ClickListener clickListener){
            this.clickListener=clickListener;
            gestureDetector=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if (child!=null && clickListener!=null)
                    {
                        clickListener.onLongClick(child,recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

// MyviewHolder classs

    public class MyviewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        public MyviewHolder(View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.thumbnail);

        }
    }
}
