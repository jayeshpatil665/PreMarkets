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

import java.util.HashMap;

public class AdminAddQuoteActivity extends AppCompatActivity {

    EditText et_quote;
    Button btn_add_quote;

    private DatabaseReference quoteRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_quote);

        quoteRef = FirebaseDatabase.getInstance().getReference().child("adminContent").child("quotes");


        et_quote = findViewById(R.id.et_quote);
        btn_add_quote = findViewById(R.id.btn_add_quote);

        btn_add_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_quote.getText().toString().equals(""))
                {
                    HashMap<String,Object> quotemap = new HashMap<>();
                    quotemap.put("quote",et_quote.getText().toString());

                    quoteRef.updateChildren(quotemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(AdminAddQuoteActivity.this, "Quote Updated succesfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(AdminAddQuoteActivity.this, "Error in Quote Updation", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(AdminAddQuoteActivity.this, "Cant upload Empty Quote !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminAddQuoteActivity.this,AdminPnnelActivity.class);
        startActivity(intent);
        finish();
    }
}