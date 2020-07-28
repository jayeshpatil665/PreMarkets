package in.specialsoft.premarkets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminPnnelActivity extends AppCompatActivity {

    Button admin_logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pnnel);

        admin_logout_btn = findViewById(R.id.admin_logout_btn);

        admin_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPnnelActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    public void addPostActicity(View view) {
        Intent newPostIntent = new Intent(AdminPnnelActivity.this,AdminAddNewPostActivity.class);
        startActivity(newPostIntent);
        finish();
    }

    public void addQuoteActicity(View view) {
        Intent newQuoteIntent = new Intent(AdminPnnelActivity.this,AdminAddQuoteActivity.class);
        startActivity(newQuoteIntent);
        finish();
    }

    public void removePostActicity(View view) {
        Intent deletePostIntent = new Intent(AdminPnnelActivity.this,RemovePostActivity.class);
        startActivity(deletePostIntent);
        finish();
    }

    public void stockFocusUpdateActicity(View view) {

        Intent stockPostIntent = new Intent(AdminPnnelActivity.this,AdminStockFocusChangeActivity.class);
        startActivity(stockPostIntent);
        finish();
    }
}