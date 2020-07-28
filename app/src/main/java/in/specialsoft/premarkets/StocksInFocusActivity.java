package in.specialsoft.premarkets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StocksInFocusActivity extends AppCompatActivity {

    TextView text_1,text_2,text_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks_in_focus);

        text_1 = findViewById(R.id.text_1);
        text_2 = findViewById(R.id.text_2);
        text_3 = findViewById(R.id.text_3);

        callOnStart();
    }

    private void callOnStart() {

        DatabaseReference contentRef = FirebaseDatabase.getInstance().getReference().child("adminContent").child("stocksFocus");
        contentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String text1 = dataSnapshot.child("focus").getValue().toString();
                    String text2 = dataSnapshot.child("focusNext").getValue().toString();
                    String text3 = dataSnapshot.child("details").getValue().toString();

                    text_1.setText("* Stocks in Focus now : \n"+text1);
                    text_2.setText("# Next weak stock prediction : \n"+text2);
                    text_3.setText("= Description / Info : \n"+text3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StocksInFocusActivity.this,UserPannelActivity.class);
        startActivity(intent);
        finish();
    }
}