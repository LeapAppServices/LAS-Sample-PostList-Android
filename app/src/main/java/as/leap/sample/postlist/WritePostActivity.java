package as.leap.sample.postlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import as.leap.LASDataManager;
import as.leap.LASObject;
import as.leap.LASUser;
import as.leap.callback.SaveCallback;
import as.leap.exception.LASException;

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
                // When the user clicks "Save," upload the post to LAS
                // Create the Post object
                LASObject post = new LASObject("Post");
                post.put("textContent", mPostContentTextView.getText().toString());

                // Create an author relationship with the current user
                post.put("author", LASUser.getCurrentUser());

                WritePostActivity.this.mProgressDialog = ProgressDialog.show(
                        WritePostActivity.this, "", "Loading...", true);
                // Save the post and return
                LASDataManager.saveInBackground(post, new SaveCallback() {

                    @Override
                    public void done(LASException exception) {
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
