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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button btn_register;
    EditText register_user_name,register_user_id,register_user_pass,register_user_security;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        btn_register = findViewById(R.id.btn_register);

        register_user_name = findViewById(R.id.register_user_name);
        register_user_id = findViewById(R.id.register_user_id);
        register_user_pass = findViewById(R.id.register_user_pass);
        register_user_security = findViewById(R.id.register_user_security);


        loader = new ProgressDialog(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String name = register_user_name.getText().toString();
        String email = register_user_id.getText().toString().trim();
        String pass = register_user_pass.getText().toString().trim();
        String security = register_user_security.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(security))
        {
            Toast.makeText(this, "All Fields are Mendatory !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loader.setTitle("Processing");
            loader.setMessage("Please wait..");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            validateUserId(name,email,pass,security);
        }
    }

    private void validateUserId(final String name, final String email, final String pass,final String security) {
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
                   if (!(dataSnapshot.child("users").child(tempEmail).exists()))
                   {
                       HashMap<String,Object> userDataMap = new HashMap<>();
                       userDataMap.put("name",name);
                       userDataMap.put("email",tempEmail);
                       userDataMap.put("pass",pass);
                       userDataMap.put("security",security);

                       rootRef.child("users").child(tempEmail).updateChildren(userDataMap)
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful())
                                       {
                                           loader.dismiss();
                                           Toast.makeText(RegisterActivity.this, "Registered Sucesfully !", Toast.LENGTH_SHORT).show();
                                           Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                           startActivity(intent);
                                           finish();
                                       }
                                       else
                                       {
                                           loader.dismiss();
                                           Toast.makeText(RegisterActivity.this, "Error in Registering !!", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                   }
                   else
                   {
                       loader.dismiss();
                       Toast.makeText(RegisterActivity.this, "Account already exits !", Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
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
            loader.dismiss();
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