package gr.aueb.cs.tiktokapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Objects;

import gr.aueb.cs.tiktokapplication.appnode.Publisher;
import gr.aueb.cs.tiktokapplication.video.ChannelName;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Closing top title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_sign_up);

        // Make the button respond properly
        ImageButton button1 = (ImageButton) findViewById(R.id.createChannelButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText c = (EditText) findViewById(R.id.channelName);

                String channelName = c.getText().toString().trim();

                System.out.println(channelName);

                if (channelName.equals("")) {
                    // Display error message
                    Toast toast = Toast.makeText(getApplicationContext(), "Please provide a channel name!",  Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    Intent intent = new Intent(view.getContext(), Configurations.class);

                    intent.putExtra("CHANNEL_NAME", channelName);

                    view.getContext().startActivity(intent);
                }

            }

        });
    }
}