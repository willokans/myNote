package androidproject.com.mynotes;

import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import data.DatabaseHandler;

public class NoteDetailActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView title, date, content;
    private Button deleteButton;
    private Button editButton;
    private Button speak;
    private TextToSpeech TTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        title = (TextView) findViewById(R.id.detailsTitle);
        date = (TextView) findViewById(R.id.detailsDataText);
        content = (TextView) findViewById(R.id.detailsTextView);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        speak = (Button) findViewById(R.id.SpeakButton);


        //receive details from previous activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            title.setText(extras.getString("title"));
            date.setText("Created: " + extras.getString("date"));
            content.setText("\" " + extras.getString("content") + "\" ");


            final int id = extras.getInt("id");

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                    dba.deleteNote(id);

                    Toast.makeText(getApplicationContext(), R.string.Note_Deleted_Message, Toast.LENGTH_LONG).show();

                    startActivity(new Intent(NoteDetailActivity.this, DisplayNotesActivity.class));
                }
            });

        }

        //Onclick for speak button
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TTS.isSpeaking()){
                    HashMap<String, String> stringStringHashMap = new HashMap<String, String>();
                    stringStringHashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "Hello how are you");
                    TTS.speak(content.getText().toString(), TextToSpeech.QUEUE_ADD,stringStringHashMap);
                    speak.setVisibility(Button.GONE);
                }else {
                    TTS.stop();
                }
            }
        });
    }


    //Method to speak words

    @Override
    public void onInit(int status) {
        TTS.setOnUtteranceCompletedListener((TextToSpeech.OnUtteranceCompletedListener) NoteDetailActivity.this);


    }




    public void onUtteranceCompleted(String utteranceId) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NoteDetailActivity.this, "Utterance completed", Toast.LENGTH_LONG).show();
                speak.setVisibility(Button.VISIBLE);
            }
        });

    }

    protected void onDestroy() {
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
            TTS = null;
        }
        super.onDestroy();
    }
}

