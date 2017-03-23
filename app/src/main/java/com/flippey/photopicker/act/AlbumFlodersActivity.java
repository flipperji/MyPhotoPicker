package com.flippey.photopicker.act;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.flippey.photopicker.R;
import com.flippey.photopicker.adapter.AlbumFlodersAdapter;
import com.flippey.photopicker.entity.ImageFolder;
import com.flippey.photopicker.utils.ImagePicker;

import java.util.List;




public class AlbumFlodersActivity extends FragmentActivity implements View.OnClickListener {

    TextView mTvTitle;
    ListView mLv;
    private ImagePicker mImagePicker;
    private List<ImageFolder> mImageFolders;
    private AlbumFlodersActivity mInstance;
    private AlbumFlodersAdapter mAlbumFlodersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_floders);
        mInstance = AlbumFlodersActivity.this;
        initView();

    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_album_floders_title);
        mLv = (ListView) findViewById(R.id.rl_album_floders_lv);
        findViewById(R.id.tv_album_floders_back).setOnClickListener(this);
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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_album_floders_back:
                finish();
                break;
        }
    }
}
