package com.flippey.photopicker.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flippey.photopicker.R;
import com.flippey.photopicker.act.AlbumActivity;
import com.flippey.photopicker.entity.ImageInformation;
import com.flippey.photopicker.utils.ImagePicker;
import com.flippey.photopicker.utils.PermissionUtil;

import java.io.File;
import java.util.ArrayList;


/**
 * @ Author      Flippey
 * @ Creat Time  2017/1/19 19:37
 */

public class AlbumAdapter extends BaseAdapter {
    private ArrayList<ImageInformation> mPhotoList; //当前需要显示的所有图片数据
    private ArrayList<ImageInformation> mSelectedPhotos; //被选中的图片数据
    private static final int ITEM_TYPE_CAMERA = 0;  //条目是相机
    private static final int ITEM_TYPE_NORMAL = 1;  //条目不是相机
    private Activity mActivity;
    private int mPhotoSize; //展示的图片的宽度
    private OnPhotoItemClickListener listener;   //图片被点击的监听
    private ImagePicker mImagePicker;

    public AlbumAdapter(Activity activity, ArrayList<ImageInformation> photoList) {
        mActivity = activity;
        if (photoList == null || photoList.size() == 0) {
            mPhotoList = new ArrayList<>();
        } else {
            mPhotoList = photoList;
        }
        mPhotoSize =getPhotoItemWidth(mActivity);

        mImagePicker = ImagePicker.getInstance();
        //mImagePicker = ImagePicker.getInstance();
        mSelectedPhotos = mImagePicker.getSelectedImages();
    }

    @Override
    public int getCount() {
        return mPhotoList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public ImageInformation getItem(int position) {
        if (position == 0)
            return null;
        return mPhotoList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE_CAMERA) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_ablum_camera, parent, false);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mPhotoSize));
            convertView.setTag(null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PermissionUtil.isReadCamera(mActivity)) {
                        mImagePicker.takePicture(mActivity, ImagePicker.REQUEST_CODE_CAMERA);
                    } else {
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, AlbumActivity.REQUEST_PERMISSION_CAMERA);
                    }
                }
            });
        } else {
            final AlbumViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_album, parent, false);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mPhotoSize));
                holder = new AlbumViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (AlbumViewHolder) convertView.getTag();
            }
            final ImageInformation imageInformation = getItem(position);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onPhotoItemClick(holder.rootView, imageInformation, position);
                }
            });
            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageInformation.path.endsWith("gif")) {
                        //XIDIUtils.showToast(mActivity, "暂不支持gif图片");
                        Toast.makeText(mActivity, "暂不支持gif图片", Toast.LENGTH_SHORT);
                        holder.cb.setChecked(false);
                        holder.ivShadow.setVisibility(View.GONE);
                    } else {
                        mImagePicker.addSelectedImageItem(position, imageInformation, holder.cb.isChecked());
                        holder.ivShadow.setVisibility(View.VISIBLE);
                    }
                    if (!holder.cb.isChecked()) {
                        holder.ivShadow.setVisibility(View.GONE);
                    }
                }
            });
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                            .RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(50);
                    holder.cb.startAnimation(scaleAnimation);
                }
            });
            boolean contains = mSelectedPhotos.contains(imageInformation);
            if (contains) {
                holder.ivShadow.setVisibility(View.VISIBLE);
                holder.cb.setChecked(true);
            } else {
                holder.ivShadow.setVisibility(View.GONE);
                holder.cb.setChecked(false);
            }
            Glide.with(mActivity)
                    .load(Uri.fromFile(new File(imageInformation.path)))
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivPhoto);
        }

        return convertView;
    }


    public interface OnPhotoItemClickListener {
        void onPhotoItemClick(View view, ImageInformation imageInformation, int position);
    }

    public void setOnPhotoItemClickListener(OnPhotoItemClickListener listener) {
        this.listener = listener;
    }

    class AlbumViewHolder {
        ImageView ivPhoto;
        ImageView ivShadow;
        CheckBox cb;
        View rootView;

        public AlbumViewHolder(View view) {
            rootView = view;
            ivPhoto = (ImageView) view.findViewById(R.id.item_album_iv_photo);
            ivShadow = (ImageView) view.findViewById(R.id.item_album_iv_shadow);
            cb = (CheckBox) view.findViewById(R.id.item_album_cb);
        }

    }

    public void refreshData(ArrayList<ImageInformation> photoList) {
        if (photoList == null || photoList.size() == 0) {
            mPhotoList = new ArrayList<>();
        } else {
            mPhotoList = photoList;
        }
        notifyDataSetChanged();
    }


    /**
     * 根据屏幕密度获取item的宽度
     *
     * @param context
     * @return
     */
    public static int getPhotoItemWidth(Context context) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        int columnSpace = (int) (2 * context.getResources().getDisplayMetrics().density);
        return (screenWidth - columnSpace * 3) / 3;
    }
}
