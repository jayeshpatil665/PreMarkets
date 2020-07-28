package in.specialsoft.premarkets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import in.specialsoft.premarkets.Prevalent.Prevalent;
import io.paperdb.Paper;

public class UserPannelActivity extends AppCompatActivity {

    TextView message_tv,message_user_tv,daily_quote_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pannel);

        message_tv = findViewById(R.id.message_tv);
        message_user_tv = findViewById(R.id.message_user_tv);
        daily_quote_tv = findViewById(R.id.daily_quote_tv);

        Paper.init(this);
        callOnStart();

    }

    private void callOnStart() {
        message_user_tv.setText(""+Paper.book().read(Prevalent.uName));

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            message_tv.setText("Good Morning !");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            message_tv.setText("Good Afternoon !");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            message_tv.setText("Good Evening !");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            message_tv.setText("Good Night !");
        }

        DatabaseReference contentRef = FirebaseDatabase.getInstance().getReference().child("adminContent").child("quotes");
        contentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String tempQuote = dataSnapshot.child("quote").getValue().toString();
                    daily_quote_tv.setText(" \" "+tempQuote+" \" ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //
    }

    public void rateMe(View view) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details=" + getPackageName())));
        }catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,
                   Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName()) ));
        }
    }

    public void tempMessage(View view) {
        Intent intent = new Intent(UserPannelActivity.this,StocksInFocusActivity.class);
        startActivity(intent);
        finish();
    }

    public void getToPremarketsPosts(View view) {

        Intent intent = new Intent(UserPannelActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void logOutNow(View view) {

        Paper.book().destroy();
        Intent intent = new Intent(UserPannelActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showAboutInfo(View view) {
        Intent intent = new Intent(UserPannelActivity.this,AboutActivity.class);
        startActivity(intent);
        finish();
    }
}