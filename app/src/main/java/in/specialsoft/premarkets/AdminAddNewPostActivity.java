package in.specialsoft.premarkets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class AdminAddNewPostActivity extends AppCompatActivity {


     String pID,pHeading,pDescription,saveDate,saveTime,postRandomKey,downloadImageUrl;

    Button add_new_post;
    ImageView select_post_image;
    EditText post_id,post_heading,post_description;

    private static  int galleryPick =1;
    private Uri ImageUri;

    private ProgressDialog loading;

    private StorageReference postImagesRef;
    private DatabaseReference postsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_post);

        loading = new ProgressDialog(this);

        postImagesRef = FirebaseStorage.getInstance().getReference().child("postImages");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        select_post_image = findViewById(R.id.select_post_image);
        add_new_post = findViewById(R.id.add_new_post);

        post_id = findViewById(R.id.post_id);
        post_heading = findViewById(R.id.post_heading);
        post_description = findViewById(R.id.post_description);

        select_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        add_new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePostData();
            }
        });

        genrateProductID();
    }

    private void genrateProductID() {
        Random random = new Random();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        String saveCurrentTime = currentTime.format(calForDate.getTime());

        postRandomKey = saveCurrentTime.toString().toLowerCase()+random.nextInt(100)+"".trim();
        post_id.setText(postRandomKey);

    }

    private void validatePostData() {

        pDescription = post_description.getText().toString();
        pHeading = post_heading.getText().toString();
        pID = post_id.getText().toString();

        if (ImageUri == null || pDescription.equals("") || pID.equals("") || pHeading.equals(""))
        {
            Toast.makeText(this, "All fields require including image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loading.setTitle("Post publishing Process");
            loading.setMessage("...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            storePostInfo();
        }

    }

    private void storePostInfo() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(calendar.getTime());

        final StorageReference filePath = postImagesRef.child(ImageUri.getLastPathSegment() + post_id.getText().toString()+".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                String message = e.toString();
                Toast.makeText(AdminAddNewPostActivity.this, "ERROR : In uploading Image ! "+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            //getting prost Image succesfully
                            savePostInfoToDatabase();
                        }
                    }
                });

            }
        });
    }

    private void savePostInfoToDatabase() {

        HashMap<String,Object> postmap = new HashMap<>();
        postmap.put("pid",pID);
        postmap.put("date",saveDate);
        postmap.put("time",saveTime);
        postmap.put("description",pDescription);
        postmap.put("image",downloadImageUrl);
        postmap.put("heading",pHeading);

        postsRef.child(pID).updateChildren(postmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    loading.dismiss();
                    Toast.makeText(AdminAddNewPostActivity.this, " Post Uploaded Sucessfully", Toast.LENGTH_SHORT).show();
                    Intent uploadSucess = new Intent(AdminAddNewPostActivity.this,AdminPnnelActivity.class);
                    startActivity(uploadSucess);
                    finish();
                }
                else
                {
                    loading.dismiss();
                    Toast.makeText(AdminAddNewPostActivity.this, " Error : in Uploading Post", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==galleryPick && resultCode== RESULT_OK && data!=null);
        {
            ImageUri = data.getData();
            select_post_image.setImageURI(ImageUri);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminAddNewPostActivity.this,AdminPnnelActivity.class);
        startActivity(intent);
        finish();
    }
}