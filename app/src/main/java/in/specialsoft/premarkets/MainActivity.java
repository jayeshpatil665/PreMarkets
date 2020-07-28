package in.specialsoft.premarkets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.specialsoft.premarkets.Model.Users;
import in.specialsoft.premarkets.Prevalent.Prevalent;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button main_login_button,main_signup_button;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_login_button = findViewById(R.id.main_login_button);
        main_signup_button = findViewById(R.id.main_signup_button);

        loading = new ProgressDialog(this);


        Paper.init(this);
        String UserEmailKey = Paper.book().read(Prevalent.UserEmail);
        String UserPassKey = Paper.book().read(Prevalent.UserPass);

        if (UserEmailKey !="" && UserPassKey !="")
        {
            if (!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPassKey))
            {
                loading.setTitle("Checking Login Credentials");
                loading.setMessage("Please wait..");
                loading.setCanceledOnTouchOutside(false);
                loading.show();

                allowAccess(UserEmailKey,UserPassKey);
            }
        }

        main_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        main_signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void allowAccess(final String tempEmail, final String pass) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(tempEmail).exists())
                {
                    //retrive - data from User class
                    Users usersData = dataSnapshot.child("users").child(tempEmail).getValue(Users.class);

                    if (usersData.getEmail().equals(tempEmail))
                    {
                        if (usersData.getPass().equals(pass))
                        {
                            loading.dismiss();
                            Intent intent = new Intent(MainActivity.this,UserPannelActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(MainActivity.this, "check Login credentials !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        loading.dismiss();
                        Toast.makeText(MainActivity.this, "check Login credentials !", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    loading.dismiss();
                    Toast.makeText(MainActivity.this, "check Login credentials !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}