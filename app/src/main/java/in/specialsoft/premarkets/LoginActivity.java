package in.specialsoft.premarkets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.specialsoft.premarkets.Model.Users;
import in.specialsoft.premarkets.Prevalent.Prevalent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText login_user_id,login_user_pass;
    private Button btn_login;
    private String parentDbName = "users";
    private ProgressDialog loading;

    private TextView tv_iam_andmin;
    String email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);

        login_user_id =  (EditText) findViewById(R.id.login_user_id);
        login_user_pass = (EditText) findViewById(R.id.login_user_pass);

        tv_iam_andmin = findViewById(R.id.tv_iam_andmin);

        loading = new ProgressDialog(this);

        Paper.init(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = login_user_id.getText().toString();
                pass = login_user_pass.getText().toString();
                loginUser(email,pass);
            }
        });

        tv_iam_andmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setText("Login as Admin");
                tv_iam_andmin.setVisibility(v.INVISIBLE);
                parentDbName = "admins";
            }
        });
    }

    private void loginUser(String email, String pass) {

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "All Fields are Mendatory !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loading.setTitle("Checking Login Credentials");
            loading.setMessage("Please wait..");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            validateUserId(email,pass);
        }

    }

    private void validateUserId(final String email, final String pass) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        //encode Email
        final String tempEmail = EncodeString(email);
        boolean valid = false;
        valid = checkStringContents(tempEmail);
        if (valid)
        {
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(parentDbName).child(tempEmail).exists())
                    {
                        //retrive - set data to User class
                        Users usersData = dataSnapshot.child(parentDbName).child(tempEmail).getValue(Users.class);

                        if (usersData.getEmail().equals(tempEmail))
                        {
                            if (usersData.getPass().equals(pass))
                            {

                                if (parentDbName.equals("admins"))
                                {
                                    loading.dismiss();
                                    Toast.makeText(LoginActivity.this, "Welcome Admin !", Toast.LENGTH_SHORT).show();
                                    Intent adminIntent = new Intent(LoginActivity.this,AdminPnnelActivity.class);
                                    startActivity(adminIntent);
                                    finish();
                                }
                                else
                                {
                                    Paper.book().write(Prevalent.UserEmail,tempEmail);
                                    Paper.book().write(Prevalent.UserPass,pass);
                                    Paper.book().write(Prevalent.uName,usersData.getName());
                                    loading.dismiss();
                                    Toast.makeText(LoginActivity.this, "Login Succesfully !", Toast.LENGTH_SHORT).show();
                                    //Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    Intent intent = new Intent(LoginActivity.this,UserPannelActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else
                            {
                                loading.dismiss();
                                Toast.makeText(LoginActivity.this, "check Login credentials !", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "check Login credentials !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, "check Login credentials !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {

        }

    }

    private boolean checkStringContents(String tempEmail) {
        int hash = tempEmail.indexOf('#');
        int dolar = tempEmail.indexOf('$');
        int b1 = tempEmail.indexOf('[');
        int b2 = tempEmail.indexOf(']');
        if (hash == -1 && dolar == -1 && b1 == -1 && b2 == -1)
        {
            return true;
        }
        else
        {
            loading.dismiss();
            Toast.makeText(this, "Email Must not contain '#', '$', '[', or ']' ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }



}