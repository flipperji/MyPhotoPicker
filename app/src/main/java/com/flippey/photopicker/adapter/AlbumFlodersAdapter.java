package com.flippey.photopicker.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flippey.photopicker.R;
import com.flippey.photopicker.entity.ImageFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @ Author      Flippey
 * @ Creat Time  2017/1/20 17:20
 */

public class AlbumFlodersAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<ImageFolder> mImageFolders;
    public AlbumFlodersAdapter(Activity activity, List<ImageFolder> folderList) {
        mActivity = activity;
        if (folderList != null && folderList.size() > 0) {
            mImageFolders = folderList;
        } else {
            mImageFolders = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return mImageFolders.size();
    }

    @Override
    public ImageFolder getItem(int position) {
        return mImageFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumFloderViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_album_floders, parent, false);
            viewHolder = new AlbumFloderViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AlbumFloderViewHolder) convertView.getTag();
        }
        ImageFolder imageFolder = mImageFolders.get(position);
        viewHolder.tvFloder.setText(imageFolder.name + "(" + imageFolder.images.size() + ")");
        //ImageUtil.glideLoadImage(mActivity, imageFolder.mImageInformation.path, viewHolder.ivPhoto);
        Glide.with(mActivity)
                .load(Uri.fromFile(new File(imageFolder.mImageInformation.path)))
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.ivPhoto);

        return convertView;
    }

    class AlbumFloderViewHolder {
        ImageView ivPhoto;
        TextView tvFloder;
        public AlbumFloderViewHolder(View view) {
            ivPhoto = (ImageView) view.findViewById(R.id.item_album_floders_iv);
            tvFloder = (TextView) view.findViewById(R.id.item_album_floders_tv_name);
        }
    }

}
