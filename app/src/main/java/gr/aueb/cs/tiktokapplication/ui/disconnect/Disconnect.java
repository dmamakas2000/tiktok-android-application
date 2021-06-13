package gr.aueb.cs.tiktokapplication.ui.disconnect;

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
import android.widget.Toast;

import gr.aueb.cs.tiktokapplication.R;
import gr.aueb.cs.tiktokapplication.appnode.Consumer;
import gr.aueb.cs.tiktokapplication.appnode.ThreadInformation;
import gr.aueb.cs.tiktokapplication.dao.ConsumerDAO;
import gr.aueb.cs.tiktokapplication.ui.register.Register;

public class Disconnect extends Fragment {

    private Button disconnectButton;
    private EditText hashTagToDisconnect;
    private ProgressDialog p;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_disconnect, container, false);

        disconnectButton = root.findViewById(R.id.disconnect_hashtag_button);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hashTagToDisconnect = root.findViewById(R.id.disconnect_hashtag_field);

                if (!hashTagToDisconnect.getText().toString().trim().equals("")) {
                    String text = hashTagToDisconnect.getText().toString().trim();
                    AsyncTaskDisconnectHashtags task = new AsyncTaskDisconnectHashtags();
                    task.execute(text);
                } else {
                    Toast.makeText(root.getContext(), "Please, give a hashtag!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }

    private class AsyncTaskDisconnectHashtags extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Disconnect.this.getContext());
            p.setMessage("Please wait until we find a server..");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            Consumer consumer = ConsumerDAO.getConsumer();
            consumer.disconnect(strings[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String hashtag) {
            super.onPostExecute(hashtag);
            p.hide();
            hashTagToDisconnect.setText("");
        }

    }

}