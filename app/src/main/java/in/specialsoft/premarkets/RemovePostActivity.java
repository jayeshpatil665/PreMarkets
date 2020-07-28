package in.specialsoft.premarkets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RemovePostActivity extends AppCompatActivity {


    EditText et_post_id;
    Button btn_delete_post;

    private DatabaseReference postRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_post);

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        et_post_id = findViewById(R.id.et_post_id);
        btn_delete_post = findViewById(R.id.btn_delete_post);

        btn_delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_post_id.getText().toString().equals(""))
                {
                    postRef.child(et_post_id.getText().toString().trim()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RemovePostActivity.this, "Post Deleted succesfully", Toast.LENGTH_SHORT).show();
                                        }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RemovePostActivity.this, "Cant delete post with invisible ID", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RemovePostActivity.this,AdminPnnelActivity.class);
        startActivity(intent);
        finish();
    }
}