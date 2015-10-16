package com.maxleap.sample.postlist;

import android.support.v4.app.Fragment;

public class PostListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PostListFragment();
    }

}
