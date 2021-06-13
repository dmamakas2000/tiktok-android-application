package gr.aueb.cs.tiktokapplication.ui.play;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import gr.aueb.cs.tiktokapplication.utils.DisplayVideoFile;
import gr.aueb.cs.tiktokapplication.R;
import gr.aueb.cs.tiktokapplication.appnode.Consumer;
import gr.aueb.cs.tiktokapplication.dao.ConsumerDAO;

public class Play extends Fragment {

    private PlayViewModel mViewModel;
    private EditText keyToPlay;
    private Button playVideoButton;
    private ProgressDialog p;
    public static View root;

    public static Play newInstance() {
        return new Play();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_play, container, false);

        keyToPlay = root.findViewById(R.id.play_video_field);

        playVideoButton = root.findViewById(R.id.play_video_button);

        playVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = keyToPlay.getText().toString().trim();

                if (!key.equals("")) {
                    ContextWrapper contextWrapper = new ContextWrapper(getActivity().getApplicationContext());
                    File dir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(dir, "tiktokvideo" + ".mp4");

                    String filePath = file.getPath();

                    AsyncTaskPlayVideo taskPlayVideo = new AsyncTaskPlayVideo();
                    taskPlayVideo.execute(key, filePath);
                } else {
                    Toast.makeText(root.getContext(), "Please, give a key!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }

    private class AsyncTaskPlayVideo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Play.this.getContext());
            p.setMessage("Downloading video. Please wait..");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Consumer consumer = ConsumerDAO.getConsumer();

            String path = strings[1];

            String data = keyToPlay.getText().toString().trim() + " " + path;

            consumer.playData(data);

            return "";
        }

        @Override
        protected void onPostExecute(String h) {
            super.onPostExecute(h);
            p.hide();
            Intent intent = new Intent(root.getContext(), DisplayVideoFile.class);
            root.getContext().startActivity(intent);
        }

    }
}