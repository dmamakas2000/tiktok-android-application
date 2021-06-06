package gr.aueb.cs.tiktokapplication.ui.removehashtag;

import androidx.lifecycle.ViewModelProvider;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import gr.aueb.cs.tiktokapplication.R;
import gr.aueb.cs.tiktokapplication.appnode.Publisher;
import gr.aueb.cs.tiktokapplication.appnode.ThreadInformation;
import gr.aueb.cs.tiktokapplication.dao.PublisherDAO;
import gr.aueb.cs.tiktokapplication.ui.addhashtag.add_hashtag;

public class RemoveHashtag extends Fragment {

    private RemoveHashtagViewModel mViewModel;
    private Button removeHashtagButton;
    private ProgressDialog p;
    private EditText hashTagField;

    public static RemoveHashtag newInstance() {
        return new RemoveHashtag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_remove_hashtag, container, false);

        removeHashtagButton = root.findViewById(R.id.remove_hashtag_button);
        removeHashtagButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hashTagField = root.findViewById(R.id.remove_hashtag_field);
                String text = hashTagField.getText().toString().trim();
                AsyncTaskRemoveHashtag remove = new AsyncTaskRemoveHashtag();
                remove.execute(text);
            }
        });

        return root;
    }

    private class AsyncTaskRemoveHashtag extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(RemoveHashtag.this.getContext());
            p.setMessage("Please wait until we find a server..");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            // Remove the hashtag from the channel
            Publisher p = PublisherDAO.getPublisher();      // Get publisher's instance

            ThreadInformation threadInformation = new ThreadInformation(2, (int) p.getId(), p.getPort(), p.getIp(), p.getChannelname(), strings[0]);
            p.setOption(threadInformation);

            // Start a new thread
            new Thread(p).start();

            return "";
        }

        @Override
        protected void onPostExecute(String hashtag) {
            super.onPostExecute(hashtag);
            p.hide();
        }

    }
}