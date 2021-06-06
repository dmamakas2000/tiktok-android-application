package gr.aueb.cs.tiktokapplication.ui.capture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gr.aueb.cs.tiktokapplication.R;
import gr.aueb.cs.tiktokapplication.Video;

public class Capture extends Fragment {

    private CaptureViewModel mViewModel;

    public static Capture newInstance() {
        return new Capture();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_capture, container, false);

        // Make the button respond properly
        Button button1 = (Button) root.findViewById(R.id.capture_video);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Got there");
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                startActivityForResult(intent, 1);

            }

        });


        return root;
       // return inflater.inflate(R.layout.fragment_capture, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1 && resultCode == 1) {
            Uri videoUri = intent.getData();
            Intent intent1 = new Intent(this.getContext(), Video.class);
            intent1.putExtra("URI", videoUri);
        }
    }


}