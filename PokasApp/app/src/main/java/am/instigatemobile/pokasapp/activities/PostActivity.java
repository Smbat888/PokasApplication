package am.instigatemobile.pokasapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import am.instigatemobile.pokasapp.R;

public class PostActivity extends AppCompatActivity {

    private ImageButton addImageButton;
    private EditText postTitle;
    private EditText postDesc;
    private Button submitButton;

    private Uri imageUri = null;
    private ProgressDialog progressDialog;

    private StorageReference storage;
    private DatabaseReference database;

    private static final int GALERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Blog");

        addImageButton = (ImageButton) findViewById(R.id.addImage);
        postTitle = (EditText) findViewById(R.id.postTitle);
        postDesc = (EditText) findViewById(R.id.postDesc);
        submitButton = (Button) findViewById(R.id.submitButton);

        progressDialog = new ProgressDialog(this);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALERY_REQUEST);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting() {
        progressDialog.setMessage("Posting to Blog");
        progressDialog.show();
        final String titleValue = postTitle.getText().toString().trim();
        final String descValue = postDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descValue) && imageUri != null) {
            StorageReference filePath =  storage.child("Blog_Images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = database.push();
                    newPost.child("title").setValue(titleValue);
                    newPost.child("desc").setValue(descValue);
                    newPost.child("image").setValue(downloadUri.toString());
                    newPost.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());

                    progressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this,BlogActivity.class));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALERY_REQUEST &&resultCode == RESULT_OK) {
            imageUri = data.getData();
            addImageButton.setImageURI(imageUri);
        }
    }
}
