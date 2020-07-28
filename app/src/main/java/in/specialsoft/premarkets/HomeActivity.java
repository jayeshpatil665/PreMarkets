package in.specialsoft.premarkets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import in.specialsoft.premarkets.Model.Posts;
import in.specialsoft.premarkets.ViewHolder.PostViewHolder;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference postsRef;

    TextView tv_post_name,tv_post_description,tv_post_id;
    ImageView img_post_img;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");


        tv_post_name = findViewById(R.id.tv_post_name);
        tv_post_description = findViewById(R.id.tv_post_description);
        img_post_img = findViewById(R.id.img_post_img);
        tv_post_id = findViewById(R.id.tv_post_id);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_menu);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(postsRef,Posts.class)
                .build();

        FirebaseRecyclerAdapter<Posts, PostViewHolder> adapter =
                new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {
                holder.tv_post_name.setText(model.getHeading());
                holder.tv_post_description.setText(model.getDescription());
                holder.tv_post_id.setText("Post ID : "+ model.getPid());
                Picasso.get().load(model.getImage()).into(holder.img_post_img);
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_layout,parent,false);
                PostViewHolder holder = new PostViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HomeActivity.this,UserPannelActivity.class);
        startActivity(intent);
        finish();
    }
}