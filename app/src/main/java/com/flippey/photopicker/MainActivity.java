package com.flippey.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flippey.photopicker.act.AlbumActivity;
import com.flippey.photopicker.entity.ImageInformation;
import com.flippey.photopicker.utils.CommonUtils;
import com.flippey.photopicker.utils.ImagePicker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    ImageView mIvPhoto;
    private MainActivity mInstance;
    public static final int PHOTO_RQ_CODE = 113;
    private ImagePicker mImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = MainActivity.this;
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo);
        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInstance.startActivityForResult(new Intent(mInstance, AlbumActivity.class), PHOTO_RQ_CODE);
            }
        });
        mImagePicker = ImagePicker.getInstance();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_RQ_CODE) {
            if (data != null && resultCode == ImagePicker.RESULT_CODE_PATHS) {
                ArrayList<ImageInformation> images = (ArrayList<ImageInformation>) data.getSerializableExtra(ImagePicker.SELECTED_PHOTO_PATHS);
                ImageInformation imageInformation = images.get(0);
                try {
                    FileInputStream file = new FileInputStream(imageInformation.path);
                    Bitmap bitmap = BitmapFactory.decodeStream(file);
                    mIvPhoto.setImageBitmap(CommonUtils.toRoundBitmap(bitmap,400,400,0, Color.BLACK));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
