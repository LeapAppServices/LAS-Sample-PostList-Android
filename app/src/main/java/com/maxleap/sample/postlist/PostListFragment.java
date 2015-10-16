package com.maxleap.sample.postlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.maxleap.FindCallback;
import com.maxleap.LogInCallback;
import com.maxleap.MLAnonymousUtils;
import com.maxleap.MLLog;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.MLUser;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.List;

public class PostListFragment extends ListFragment {

    private static final String TAG = PostListFragment.class.getName();

    private List<String> posts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        registerForContextMenu(getListView());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, posts);
        setListAdapter(adapter);
        setListShown(false);

        MLUser user = MLUser.getCurrentUser();
        if (user == null) {
            MLAnonymousUtils.loginInBackground(new LogInCallback<MLUser>() {

                @Override
                public void done(MLUser use, MLException e) {
                    if (e == null) {
                        MLLog.d(TAG, "finish signing up");
                        updatePostList();
                    } else {
                        setListShown(true);
                        setEmptyText(e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } else {
            updatePostList();
        }
    }


    public void updatePostList() {
        setListShown(false);
        // Create query for objects of type "Post"
        MLQuery<MLObject> query = MLQuery.getQuery("Post");

        // Restrict to cases where the author is the current user.
        // Note that you should pass in a MLUser and not the
        // String reperesentation of that user
        query.whereEqualTo("author", MLUser.getCurrentUser());
        // Run the query
        MLQueryManager.findAllInBackground(query,
                new FindCallback<MLObject>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void done(List<MLObject> objects,
                                     MLException e) {
                        if (getActivity() == null || getActivity().isFinishing()) {
                            return;
                        }
                        setListShown(true);

                        if (e == null) {
                            posts.clear();
                            for (MLObject post : objects) {
                                posts.add(post.getString("textContent"));
                            }
                            ((ArrayAdapter<String>) getListAdapter())
                                    .notifyDataSetChanged();
                        } else {
                            setEmptyText(getString(R.string.empty_list_view));
                            MLLog.e(TAG, e.getMessage());
                        }
                    }

                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_post_list, menu);
    }

    /*
     * Creating posts and refreshing the list will be controlled from the Action
     * Bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {
                updatePostList();
                break;
            }

            case R.id.action_new: {
                newPost();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void newPost() {
        Intent i = new Intent(getActivity(), WritePostActivity.class);
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // If a new post has been added, update
            // the list of posts
            updatePostList();
        }
    }

}
