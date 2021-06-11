package gr.aueb.cs.tiktokapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import java.io.File;

import gr.aueb.cs.tiktokapplication.ui.home.HomeFragment;

public class DisplayVideoFile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Closing top title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_display_video_file);

        // Get the video URI from internal storage
        Uri videoToDisplay = Uri.fromFile(new File("/storage/emulated/0/Android/data/gr.aueb.cs.tiktokapplication/files/Download/tiktokvideo.mp4"));

        // Start the video view
        VideoView view = (VideoView) findViewById(R.id.display_video_file_view);
        view.setVideoURI(videoToDisplay);
        view.start();

        // Make the button respond properly
        Button button1 = (Button) findViewById(R.id.back_to_home);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainMenu.class);
                view.getContext().startActivity(intent);
            }

        });

    }
}