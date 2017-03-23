package com.flippey.photopicker.act;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flippey.photopicker.R;
import com.flippey.photopicker.adapter.AlbumAdapter;
import com.flippey.photopicker.entity.ImageFolder;
import com.flippey.photopicker.entity.ImageInformation;
import com.flippey.photopicker.utils.CommonUtils;
import com.flippey.photopicker.utils.ImageDataSource;
import com.flippey.photopicker.utils.ImagePicker;
import com.flippey.photopicker.utils.PermissionUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlbumActivity extends AppCompatActivity implements ImagePicker.OnImageSelectedListener, ImageDataSource.OnImagesLoadedListener, AlbumAdapter.OnPhotoItemClickListener {
    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;   //请求开启相机权限
    @BindView(R.id.album_title)
    TextView mTitle;
    @BindView(R.id.album_gv)
    GridView mGv;
    @BindView(R.id.album_tv_select_count)
    TextView mSelectCount;
    @BindView(R.id.album_ll_with_photo)
    LinearLayout mLlWithPhoto;
    @BindView(R.id.album_tv_complete)
    TextView mTvCompleteWithoutPhoto;
    private ImagePicker mImagePicker;
    private AlbumActivity mInstance;
    private AlbumAdapter mAlbumAdapter;
    private List<ImageFolder> mImageFolders;    //所有的图片文件夹
    public static int SELECTEDPOSITION = 0; //默认选中的文件夹的位置
    private static String TAG = "from AlbumActivity";
    private String mAbsolutePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        mInstance = AlbumActivity.this;
        initView();
    }

    private void initView() {
        mImagePicker = ImagePicker.getInstance();
        mImagePicker.clear();
        mImagePicker.addOnImageSelectedListener(this);
        mAlbumAdapter = new AlbumAdapter(mInstance, null);
        onImageSelected(0, null, false);
        //检查进入相册权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (PermissionUtil.isWriteStorage(mInstance)) {
                new ImageDataSource(this, null, this);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        }
    }

    @OnClick({R.id.ll_album_title, R.id.album_tv_cancle, R.id.album_tv_complete_with_photo,R.id.tv_album_floders_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_album_title:
                //点击进入相册列表
                startActivityForResult(new Intent(mInstance, AlbumFlodersActivity.class), mImagePicker.REQUEST_CODE_FLODER);
                break;
            case R.id.album_tv_cancle:
                finish();
                break;
            case R.id.album_tv_complete_with_photo:
                Intent intent = new Intent();
                intent.putExtra(ImagePicker.SELECTED_PHOTO_PATHS, mImagePicker.getSelectedImages());
                setResult(ImagePicker.RESULT_CODE_PATHS, intent);
                finish();
                break;
            case R.id.tv_album_floders_back:
                finish();
                break;
        }
    }

    @Override
    public void onImageSelected(int position, ImageInformation item, boolean isAdd) {
        if (mImagePicker.getSelectImageCount() > 0) {
            mLlWithPhoto.setVisibility(View.VISIBLE);
            mTvCompleteWithoutPhoto.setVisibility(View.GONE);
            mSelectCount.setText(mImagePicker.getSelectImageCount() + "");
        } else {
            mLlWithPhoto.setVisibility(View.GONE);
            mTvCompleteWithoutPhoto.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        mImagePicker.removeOnImageSelectedListener(this);
        super.onDestroy();
    }


    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        mImageFolders = imageFolders;
        mImagePicker.setImageFolders(imageFolders);
        if (imageFolders.size() == 0) {
            mAlbumAdapter.refreshData(null);
        } else {
            mAlbumAdapter.refreshData(imageFolders.get(SELECTEDPOSITION).images);
        }
        mAlbumAdapter.setOnPhotoItemClickListener(this);
        int size = imageFolders.get(SELECTEDPOSITION).images.size();
        mTitle.setText(String.format(getString(R.string.album_title), size));
        mGv.setAdapter(mAlbumAdapter);
    }

    @Override
    public void onPhotoItemClick(View view, ImageInformation imageInformation, int position) {
        Log.e("test", "onClick: "+position);
        crop(imageInformation.path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE_FLODER) {
            SELECTEDPOSITION = mImagePicker.getCurrentImageFolderPosition();
            mImagePicker.setCurrentImageFolderPosition(SELECTEDPOSITION);
            ImageFolder imageFolder = mImageFolders.get(SELECTEDPOSITION);
            mAlbumAdapter.refreshData(imageFolder.images);
            mTitle.setText(imageFolder.name+"(" +imageFolder.images.size()+")");
        } else if (requestCode == ImagePicker.REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            ImagePicker.galleryAddPic(mInstance, mImagePicker.getTakeImageFile());
            ImageInformation imageInformation = new ImageInformation();
            imageInformation.path = mImagePicker.getTakeImageFile().getAbsolutePath();
            //设置拍摄图片的宽高
             BitmapFactory.Options options = new BitmapFactory.Options();
             options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
             BitmapFactory.decodeFile(imageInformation.path, options);
             imageInformation.width = options.outWidth;
             imageInformation.height = options.outHeight;
            mImagePicker.addSelectedImageItem(0, imageInformation, true);
            Intent intent = new Intent();
            intent.putExtra(ImagePicker.SELECTED_PHOTO_PATHS, mImagePicker.getSelectedImages());
            setResult(ImagePicker.RESULT_CODE_PATHS, intent);
            finish();
        }else if (requestCode == ImagePicker.IMAGE_CROP_CODE&&resultCode == RESULT_OK){
            ImageInformation information = new ImageInformation();
            information.path = mAbsolutePath;
            mImagePicker.addSelectedImageItem(0, information, true);
            Intent intent = new Intent();
            intent.putExtra(ImagePicker.SELECTED_PHOTO_PATHS, mImagePicker.getSelectedImages());
            setResult(ImagePicker.RESULT_CODE_PATHS, intent);
            finish();
        }

    }


    public static Bitmap decodeBitmapFromFile(String imagePath, int requestWidth, int requestHeight) {
        if (!TextUtils.isEmpty(imagePath)) {
           /* XIDIUtils.printLog(TAG, "requestWidth: " + requestWidth);
            XIDIUtils.printLog(TAG, "requestHeight: " + requestHeight);*/
            if (requestWidth <= 0 || requestHeight <= 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                return bitmap;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
            BitmapFactory.decodeFile(imagePath, options);
           /* XIDIUtils.printLog(TAG, "original height: " + options.outHeight);
            XIDIUtils.printLog(TAG, "original width: " + options.outWidth);*/
            if (options.outHeight == -1 || options.outWidth == -1) {
                try {
                    ExifInterface exifInterface = new ExifInterface(imagePath);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                  /*  XIDIUtils.printLog(TAG, "exif height: " + height);
                    XIDIUtils.printLog(TAG, "exif width: " + width);*/
                    options.outWidth = width;
                    options.outHeight = height;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           /* options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); //计算获取新的采样率
            XIDIUtils.printLog(TAG, "inSampleSize: " + options.inSampleSize);*/
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(imagePath, options);

        } else {
            return null;
        }
    }

    public  void crop(String imagePath) {
        File file = new File(CommonUtils.createRootPath(mInstance) + "/" + System.currentTimeMillis() + ".jpg");
        mAbsolutePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(CommonUtils.getImageContentUri(new File(imagePath),mInstance), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", ImagePicker.aspectX);
        intent.putExtra("aspectY", ImagePicker.aspectY);
        intent.putExtra("outputX", ImagePicker.outputX);
        intent.putExtra("outputY", ImagePicker.outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, ImagePicker.IMAGE_CROP_CODE);
    }
}
