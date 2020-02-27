package com.huawei.hiaicodedemo.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.huawei.hiaicodedemo.R;
import com.huawei.hiaicodedemo.utils.AssetsFileUtil;

import java.util.List;

public class BitmapAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mList;
    public IBitmapListener mListener;
    private Holder mHolder;
    public BitmapAdapter(Context context,List<String> list) {
        this.mContext = context;
        mList = list;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bitmap, parent, false);
        mHolder = new Holder(view);
        return mHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        mHolder.setDataInfo(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemListener(position);
            }
        });
    }


    public void setItemOnClickListener(IBitmapListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mList.size() != 0 ? mList.size() : 0;
    }

    private class Holder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public Holder(View inflate) {
            super(inflate);
            imageView = inflate.findViewById(R.id.bitmap);
        }

        public void setDataInfo(int position) {
            imageView.setImageBitmap(AssetsFileUtil.getBitmapByFilePath(mContext,mList.get(position)));
        }
    }

    public interface IBitmapListener{
        void onItemListener(int position);
    }

}
