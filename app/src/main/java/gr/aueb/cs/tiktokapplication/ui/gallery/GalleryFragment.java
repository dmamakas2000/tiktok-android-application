package gr.aueb.cs.tiktokapplication.ui.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.InputStream;

import gr.aueb.cs.tiktokapplication.DisplayVideoFile;
import gr.aueb.cs.tiktokapplication.MainMenu;
import gr.aueb.cs.tiktokapplication.R;
import gr.aueb.cs.tiktokapplication.Video;
import gr.aueb.cs.tiktokapplication.appnode.Consumer;
import gr.aueb.cs.tiktokapplication.appnode.Publisher;
import gr.aueb.cs.tiktokapplication.appnode.ThreadInformation;
import gr.aueb.cs.tiktokapplication.appnode.Value;
import gr.aueb.cs.tiktokapplication.appnode.VideoFile;
import gr.aueb.cs.tiktokapplication.dao.ConsumerDAO;
import gr.aueb.cs.tiktokapplication.dao.PublisherDAO;
import gr.aueb.cs.tiktokapplication.ui.addhashtag.add_hashtag;
import gr.aueb.cs.tiktokapplication.ui.register.Register;

public class GalleryFragment extends Fragment {

    private VideoView view;
    private Button uploadButton;
    private EditText hashtagEditText;
    private ProgressDialog p;
    private static Uri videoUri;
    private static View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        root = inflater.inflate(R.layout.fragment_gallery, container, false);

        view = root.findViewById(R.id.videoView3);

        uploadButton = root.findViewById(R.id.upload_video_button);
        uploadButton.setVisibility(View.INVISIBLE);

        hashtagEditText = root.findViewById(R.id.gallery_add_hashtag);
        hashtagEditText.setVisibility(View.INVISIBLE);

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

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hashtagToBePushed = hashtagEditText.getText().toString().trim();

                if (!hashtagToBePushed.equals("")) {
                    InputStream s;
                    try {
                        s = root.getContext().getContentResolver().openInputStream(videoUri);

                        String[] mediaColumns = {MediaStore.Video.Media.SIZE};
                        Cursor cursor = getContext().getContentResolver().query(videoUri, mediaColumns, null, null, null);
                        cursor.moveToFirst();
                        int sizeColInd = cursor.getColumnIndex(mediaColumns[0]);
                        long fileSize = cursor.getLong(sizeColInd);
                        cursor.close();
                        System.out.println(fileSize);


                        Publisher p = PublisherDAO.getPublisher();
                        p.setAndroidVideoInputStream(s);
                        p.setAndroidVideoBytes(fileSize);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    AsyncTaskUploadVideo uploadVideo = new AsyncTaskUploadVideo();
                    uploadVideo.execute(hashtagToBePushed);
                } else {
                    Toast.makeText(root.getContext(), "You should give a hashtag to this video!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            Uri selectedImageUri = intent.getData();

            videoUri = selectedImageUri;

            System.out.println(selectedImageUri.getPath());
            view.setVideoURI(selectedImageUri);
            view.start();

            uploadButton.setVisibility(View.VISIBLE);
            hashtagEditText.setVisibility(View.VISIBLE);

        }
    }

    private class AsyncTaskUploadVideo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(GalleryFragment.this.getContext());
            p.setMessage("Uploading video. Please wait..");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Publisher publisher = PublisherDAO.getPublisher();

            VideoFile videoValueFile = new VideoFile();
            videoValueFile.setVideoName(videoUri.getPath());
            Value videoValue = new Value(videoValueFile);

            publisher.push(strings[0], videoValue);

            return "";
        }

        @Override
        protected void onPostExecute(String hashtag) {
            super.onPostExecute(hashtag);
            p.hide();
            Toast.makeText(root.getContext(), "Uploaded!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(root.getContext(), MainMenu.class);
            root.getContext().startActivity(intent);
        }

    }



}