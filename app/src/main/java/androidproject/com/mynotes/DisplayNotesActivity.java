package androidproject.com.mynotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import data.DatabaseHandler;
import model.MyNote;

public class DisplayNotesActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<MyNote> dbNotes = new ArrayList<>();
    private NoteAdaptor noteAdaptor;
    private ListView listView;
    private Button buttonAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);

        listView = (ListView)  findViewById(R.id.noteList);
        buttonAddNote = (Button) findViewById(R.id.backToMain);

        //fetch table
        refreshData();

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DisplayNotesActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void refreshData() {

        //clear array list in db
        dbNotes.clear();


        dba = new DatabaseHandler(getApplicationContext());

        //get note save and put into new arraylist
        ArrayList<MyNote> notesFromDB = dba.getNotes();

        for (int i=0; i < notesFromDB.size(); i++) {
            String title = notesFromDB.get(i).getTitle();
            String dateText = notesFromDB.get(i).getRecordDate();
            String content = notesFromDB.get(i).getContent();
            int nId = notesFromDB.get(i).getItemId();

            //create an object and put arraylist inside of it
            MyNote myNote = new MyNote();
            myNote.setTitle(title);
            myNote.setContent(content);
            myNote.setRecordDate(dateText);
            myNote.setItemId(nId);

            dbNotes.add(myNote);

            //listView.setBackgroundColor(Color.rgb(211, 211, 211));

        }

        dba.close();


        //set up adaptor
        noteAdaptor = new NoteAdaptor(DisplayNotesActivity.this, R.layout.note_row, dbNotes);

        //set adaptor
        listView.setAdapter(noteAdaptor);
        noteAdaptor.notifyDataSetChanged();


    }

    public class NoteAdaptor extends ArrayAdapter<MyNote> {

        Activity activity;
        int layoutResource;
        ArrayList<MyNote> nData = new ArrayList<>();

        public NoteAdaptor(@NonNull Activity act, @LayoutRes int resource, ArrayList<MyNote> data) {
            super(act, resource, data);

            //initialize
            activity = act;
            layoutResource = resource;
            nData = data;
            notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return nData.size();
        }

        @Nullable
        @Override
        public MyNote getItem(int position) {
            return nData.get(position);
        }

        @Override
        public int getPosition(@Nullable MyNote item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            //using the view holder pattern

            View row = convertView;
            final ViewHolder holder;

            if(row == null || (row.getTag()) == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                row = inflater.inflate(layoutResource, null);
                holder = new ViewHolder();

                holder.nTitle = (TextView) row.findViewById(R.id.noteName);
                holder.nData = (TextView) row.findViewById(R.id.dateText);

                row.setTag(holder);
            } else {

                holder = (ViewHolder) row.getTag();
            }

            holder.myNote = getItem(position);

            holder.nTitle.setText(holder.myNote.getTitle());
            holder.nData.setText(holder.myNote.getRecordDate());

            //Create onclick listener for title
            final ViewHolder finalHolder = holder;

            holder.nTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = finalHolder.myNote.getContent().toString();
                    String dateText = finalHolder.myNote.getRecordDate().toString();
                    String title = finalHolder.myNote.getTitle().toString();
                    int nId = finalHolder.myNote.getItemId();



                    Intent i = new Intent(DisplayNotesActivity.this, NoteDetailActivity.class);
                    i.putExtra("content", text);
                    i.putExtra("date", dateText);
                    i.putExtra("title", title);
                    i.putExtra("id", nId);

                    //start activity
                    startActivity(i);
                }
            });

            return row;
        }



        class ViewHolder{

            MyNote myNote;
            TextView nTitle;
            TextView nId;
            TextView nContent;
            TextView nData;
        }
    }
}
