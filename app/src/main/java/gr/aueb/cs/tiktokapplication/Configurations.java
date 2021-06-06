package gr.aueb.cs.tiktokapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Objects;

import gr.aueb.cs.tiktokapplication.appnode.Consumer;
import gr.aueb.cs.tiktokapplication.appnode.Publisher;
import gr.aueb.cs.tiktokapplication.dao.ConsumerDAO;
import gr.aueb.cs.tiktokapplication.dao.PublisherDAO;
import gr.aueb.cs.tiktokapplication.ui.addhashtag.add_hashtag;
import gr.aueb.cs.tiktokapplication.video.ChannelName;

public class Configurations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Closing top title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_configurations);

        Intent intent = getIntent();

        String channelName = intent.getStringExtra("CHANNEL_NAME");


        // Make the button respond properly
        Button button1 = (Button) findViewById(R.id.done);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText n1 = (EditText) findViewById(R.id.number1);
                EditText n2 = (EditText) findViewById(R.id.number2);
                EditText n3 = (EditText) findViewById(R.id.number3);
                EditText n4 = (EditText) findViewById(R.id.number4);

                int number1 = Integer.parseInt(n1.getText().toString().trim());
                int number2 = Integer.parseInt(n2.getText().toString().trim());
                int number3 = Integer.parseInt(n3.getText().toString().trim());
                int number4 = Integer.parseInt(n4.getText().toString().trim());

                String ipPub = Integer.toString(number1) + "." + Integer.toString(number2) + "." + Integer.toString(number3) + "." + Integer.toString(number4);
                String ipSub =  Integer.toString(number1) + "." + Integer.toString(number2) + "." + Integer.toString(number3) + "." + Integer.toString(number4 + 1);

                EditText port = (EditText) findViewById(R.id.number_port);

                int portPub =  Integer.parseInt(port.getText().toString().trim());
                int portSub = portPub + 1;


                // Initialize a new Publisher object
                Publisher publisher = new Publisher();
                ArrayList<String> hashtags = new ArrayList<String>();
                ChannelName channel = new ChannelName(channelName, hashtags);
                publisher.init(1, ipPub, portPub, channel);

                // Add Publisher on existing database
                PublisherDAO dao = new PublisherDAO(publisher);

                // Initialize a new Consumer object
                Consumer consumer = new Consumer();
                consumer.init(2, ipSub, portSub);

                // Add Consumer on existing database
                ConsumerDAO daoConsumer = new ConsumerDAO(consumer);

                publisher.retrieveInformation();
                consumer.retrieveInformation();

                ArrayList<String> info = new ArrayList<>();

                info.add(ipPub);
                info.add(ipSub);
                info.add(Integer.toString(portPub));
                info.add(Integer.toString(portSub));
                Intent intent = new Intent(view.getContext(), MainMenu.class);
                intent.putExtra("INFO", info);

                Intent intent1 = new Intent(view.getContext(), add_hashtag.class);
                intent1.putExtra("INFO", info);

                view.getContext().startActivity(intent);

            }

        });

    }
}