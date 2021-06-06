package gr.aueb.cs.tiktokapplication.ui.register;

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
import java.nio.channels.AsynchronousChannelGroup;
import gr.aueb.cs.tiktokapplication.R;
import gr.aueb.cs.tiktokapplication.appnode.Consumer;
import gr.aueb.cs.tiktokapplication.appnode.Publisher;
import gr.aueb.cs.tiktokapplication.appnode.ThreadInformation;
import gr.aueb.cs.tiktokapplication.dao.ConsumerDAO;
import gr.aueb.cs.tiktokapplication.dao.PublisherDAO;
import gr.aueb.cs.tiktokapplication.ui.addhashtag.add_hashtag;

public class Register extends Fragment {

    private RegisterViewModel mViewModel;
    private Button registerButton;
    private EditText hashTagToRegister;
    private ProgressDialog p;

    public static Register newInstance() {
        return new Register();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register, container, false);

        registerButton = root.findViewById(R.id.register_hashtag_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hashTagToRegister = root.findViewById(R.id.register_hashtag_field);
                String text = hashTagToRegister.getText().toString().trim();
                AsyncTaskRegisterHashtags task = new AsyncTaskRegisterHashtags();
                task.execute(text);
            }
        });

        return root;
    }

    private class AsyncTaskRegisterHashtags extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Register.this.getContext());
            p.setMessage("Please wait until we find a server..");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            Consumer consumer = ConsumerDAO.getConsumer();

            ThreadInformation c = new ThreadInformation(4, consumer.getNodeId(), consumer.getPort(), consumer.getIp(), null, strings[0]);
            consumer.setOpinion(c);

            new Thread(consumer).start();

            return "";
        }

        @Override
        protected void onPostExecute(String hashtag) {
            super.onPostExecute(hashtag);
            p.hide();
        }

    }

}