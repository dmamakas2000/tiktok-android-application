package gr.aueb.cs.tiktokapplication.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import gr.aueb.cs.tiktokapplication.R;
import gr.aueb.cs.tiktokapplication.Video;

public class GalleryFragment extends Fragment {

    private VideoView view;
    private Button uploadButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        view = root.findViewById(R.id.videoView3);

        uploadButton = root.findViewById(R.id.upload_video_button);
        uploadButton.setVisibility(View.INVISIBLE);

        // Make the button respond properly
        Button button1 = (Button) root.findViewById(R.id.open_video);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }

        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            Uri selectedImageUri = intent.getData();
            System.out.println(selectedImageUri.getPath());
            view.setVideoURI(selectedImageUri);
            view.start();

            uploadButton.setVisibility(View.VISIBLE);

        }
    }

}