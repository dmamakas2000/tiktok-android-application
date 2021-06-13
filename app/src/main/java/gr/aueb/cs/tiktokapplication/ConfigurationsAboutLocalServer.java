package gr.aueb.cs.tiktokapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class ConfigurationsAboutLocalServer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Closing top title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_configurations_about_local_server);

        // Make the button respond properly
        Button button1 = (Button) findViewById(R.id.server_next_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ip1 = (EditText) findViewById(R.id.server_number_1);
                EditText ip2 = (EditText) findViewById(R.id.server_number_2);
                EditText ip3 = (EditText) findViewById(R.id.server_number_3);
                EditText ip4 = (EditText) findViewById(R.id.server_number_4);

                String ipServer = ip1.getText().toString().trim() + "." + ip2.getText().toString().trim() + "." + ip3.getText().toString().trim() + "." + ip4.getText().toString().trim();

                System.out.println(ipServer);

                Intent intent = new Intent(view.getContext(), Configurations.class);
                intent.putExtra("SERVER_IP", ipServer);
                view.getContext().startActivity(intent);
            }

        });

    }
}