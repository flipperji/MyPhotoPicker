package com.flippey.photopicker.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.flippey.photopicker.R;
import com.flippey.photopicker.adapter.AlbumFlodersAdapter;
import com.flippey.photopicker.entity.ImageFolder;
import com.flippey.photopicker.utils.ImagePicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlbumFlodersActivity extends AppCompatActivity {

    @BindView(R.id.tv_album_floders_title)
    TextView mTvTitle;
    @BindView(R.id.rl_album_floders_lv)
    ListView mLv;
    private ImagePicker mImagePicker;
    private List<ImageFolder> mImageFolders;
    private AlbumFlodersActivity mInstance;
    private AlbumFlodersAdapter mAlbumFlodersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_floders);
        ButterKnife.bind(this);
        mInstance = AlbumFlodersActivity.this;
        initView();

    }

    private void initView() {
        mImagePicker = ImagePicker.getInstance();
        mImageFolders = mImagePicker.getImageFolders();
        mTvTitle.setText(String.format(getString(R.string.album_title), mImageFolders.get(0).images.size()));
        mAlbumFlodersAdapter = new AlbumFlodersAdapter(mInstance, mImageFolders);
        mLv.setAdapter(mAlbumFlodersAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mImagePicker.setCurrentImageFolderPosition(position);
                finish();
            }
        });
    }

    @OnClick(R.id.tv_album_floders_back)
    public void onClick() {
        finish();
    }
}
