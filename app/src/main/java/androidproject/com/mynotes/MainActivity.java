package androidproject.com.mynotes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import data.DatabaseHandler;
import model.MyNote;

public class MainActivity extends AppCompatActivity {

    private EditText title;
    private EditText content;
    private Button saveButton;
    private Button noteView;
    private DatabaseHandler dba;
    private ImageButton micTitle;
    private ImageButton micNote;

    private EditText resultTitleText;
    private EditText resultNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dba = new DatabaseHandler(MainActivity.this);

        title = (EditText) findViewById(R.id.titleEditText);
        content = (EditText) findViewById(R.id.NoteEditText);
        saveButton = (Button) findViewById(R.id.saveButton);
        noteView = (Button) findViewById(R.id.noteView);
        resultTitleText = (EditText) findViewById(R.id.titleEditText);
        resultNoteText = (EditText) findViewById(R.id.NoteEditText);

        //On click for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String til = title.getText().toString().trim();
                String con = content.getText().toString().trim();


                // Make sure user enter a title and content before saving to database
                if (til.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Title", Toast.LENGTH_LONG).show();
                } else if (con.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Content", Toast.LENGTH_SHORT).show();
                } else {
                    saveToDB();
                }


            }
        });

        //On click for noteView button
        noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, DisplayNotesActivity.class);
                startActivity(i);
            }
        });
    }

    // Saving note to DB method
private void saveToDB() {

    MyNote note = new MyNote();

    note.setTitle(title.getText().toString().trim());
    note.setContent(content.getText().toString().trim());

    dba.addNotes(note);
    //dba.close();

    //clear
    title.setText("");
    content.setText("");

    //send user to new activity
    Intent i = new Intent(MainActivity.this, DisplayNotesActivity.class);
    startActivity(i);

    }


    public void editNote() {

        //receive details from previous activity
        Bundle extras = getIntent().getExtras();

        title.setText((CharSequence) extras.get("title"));
        content.setText("\" " + extras.getString("content") + "\" ");

    }


    //Create on button click for Note
    public void onButtonNoteClick(View v) {
        if (v.getId() == R.id.imageButtonNote) {
            promptSpeechInput();
        }
    }


    public void promptSpeechInput() {
        Intent i = new Intent((RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speech");

        //Start Requestion
        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Sorry, Your devise doesn't support speech language!", Toast.LENGTH_LONG).show();
        }
    }


    //Method to recieve speech input
    public void onActivityResult(int request_code, int result_code, Intent i) {

        super.onActivityResult(result_code, result_code, i);

        switch (request_code){
            case 100: if (result_code == RESULT_OK && i != null) {
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                resultNoteText.setText(result.get(0));
            }
            break;
        }
    }





}
