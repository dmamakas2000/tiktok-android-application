package gr.aueb.cs.tiktokapplication.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import gr.aueb.cs.tiktokapplication.R;

public class Video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);
        VideoView view = findViewById(R.id.videoView);

        Uri uri = Uri.parse(getIntent().getExtras().getString("URI"));
        if (uri!=null) {
            view.setVideoURI(uri);
        }

        Uri uriFromGallery = Uri.parse(getIntent().getExtras().getString("URI_FROM_GALLERY"));
        if (uriFromGallery!=null) {
            view.setVideoURI(uriFromGallery);
        }

    }
}