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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminStockFocusChangeActivity extends AppCompatActivity {

    EditText et_adminFocus1,et_adminFocus2,et_adminFocus3;
    Button btn_update_Focus;

    private DatabaseReference stockUpdateRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stock_focus_change);


        et_adminFocus1 = findViewById(R.id.et_adminFocus1);
        et_adminFocus2 = findViewById(R.id.et_adminFocus2);
        et_adminFocus3 = findViewById(R.id.et_adminFocus3);

        btn_update_Focus = findViewById(R.id.btn_update_Focus);

        callOnStart();

        btn_update_Focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockUpdateRef = FirebaseDatabase.getInstance().getReference().child("adminContent").child("stocksFocus");
                if (!et_adminFocus1.getText().toString().equals("") && !et_adminFocus2.getText().toString().equals("") && !et_adminFocus3.getText().toString().equals(""))
                {
                    HashMap<String,Object> focusMap = new HashMap<>();
                    focusMap.put("focus",et_adminFocus1.getText().toString());
                    focusMap.put("focusNext",et_adminFocus2.getText().toString());
                    focusMap.put("details",et_adminFocus3.getText().toString());

                    stockUpdateRef.updateChildren(focusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(AdminStockFocusChangeActivity.this, "Content Updated succesfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdminStockFocusChangeActivity.this,AdminPnnelActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(AdminStockFocusChangeActivity.this, "Error in Updation", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(AdminStockFocusChangeActivity.this, "Cant upload empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

                    et_adminFocus1.setText(""+text1);
                    et_adminFocus2.setText(""+text2);
                    et_adminFocus3.setText(""+text3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminStockFocusChangeActivity.this,AdminPnnelActivity.class);
        startActivity(intent);
        finish();
    }
}