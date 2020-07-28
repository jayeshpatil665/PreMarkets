package in.specialsoft.premarkets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AboutActivity.this,UserPannelActivity.class);
        startActivity(intent);
        finish();
    }

    public void showProfile(View view) {
        Intent intentLinkedIn1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/jayeshpatil665/"));
        startActivity(intentLinkedIn1);
    }

    public void showmProfile(View view) {
        Intent intentLinkedIn2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ItsMaheshKore"));
        startActivity(intentLinkedIn2);
    }
}