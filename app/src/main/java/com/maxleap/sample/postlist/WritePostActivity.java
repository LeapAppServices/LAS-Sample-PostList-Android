package com.maxleap.sample.postlist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maxleap.MLDataManager;
import com.maxleap.MLObject;
import com.maxleap.MLUser;
import com.maxleap.SaveCallback;
import com.maxleap.exception.MLException;

public class WritePostActivity extends AppCompatActivity {

    private TextView mPostContentTextView;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        mPostContentTextView = ((EditText) findViewById(R.id.blog_post_content));

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // When the user clicks "Save," upload the post to ML
                // Create the Post object
                MLObject post = new MLObject("Post");
                post.put("textContent", mPostContentTextView.getText().toString());

                // Create an author relationship with the current user
                post.put("author", MLUser.getCurrentUser());

                WritePostActivity.this.mProgressDialog = ProgressDialog.show(
                        WritePostActivity.this, "", "Loading...", true);
                // Save the post and return
                MLDataManager.saveInBackground(post, new SaveCallback() {

                    @Override
                    public void done(MLException exception) {
                        if (exception == null) {
                            WritePostActivity.this.mProgressDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error saving: " + exception.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

}
