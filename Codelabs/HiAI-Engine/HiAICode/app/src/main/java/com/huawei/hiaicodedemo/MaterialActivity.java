package com.huawei.hiaicodedemo;

import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.huawei.hiaicodedemo.adapter.BitmapAdapter;
import com.huawei.hiaicodedemo.utils.AssetsFileUtil;

import java.util.List;

public class MaterialActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private String mDirPath;
    private List<String> mList;

    @Override
    protected void init() {
        mDirPath = getIntent().getStringExtra(KEY_DIR_PATH);
        mList = AssetsFileUtil.getFileNameListByDirPath(this,mDirPath);
        initView();

    }

    @Override
    protected int layout() {
        return R.layout.activity_material;
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycleView);
        BitmapAdapter bitmapAdapter = new BitmapAdapter(this,mList);
        bitmapAdapter.setItemOnClickListener(new BitmapAdapter.IBitmapListener() {
            @Override
            public void onItemListener(int position) {
                Intent intent = new Intent();
                intent.putExtra(KEY_FILE_PATH,mList.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(bitmapAdapter);
    }


}