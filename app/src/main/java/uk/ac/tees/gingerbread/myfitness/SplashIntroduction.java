package uk.ac.tees.gingerbread.myfitness;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class SplashIntroduction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_introduction);

        // Get context and button objects
        final Context context = this;
        Button button = (Button) findViewById(R.id.splash_intro_next_button);

        // Add next button event listener
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SplashPersonal.class);
                        startActivity(intent);
                    }
                }

        );
    }
}
