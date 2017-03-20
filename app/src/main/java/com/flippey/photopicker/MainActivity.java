package com.flippey.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.flippey.photopicker.act.AlbumActivity;
import com.flippey.photopicker.utils.ImagePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    private MainActivity mInstance;
    public static final int PHOTO_RQ_CODE = 113;
    private ImagePicker mImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = MainActivity.this;
        mImagePicker = ImagePicker.getInstance();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_add)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                mInstance.startActivityForResult(new Intent(mInstance, AlbumActivity.class), PHOTO_RQ_CODE);
                break;
        }
    }
}
