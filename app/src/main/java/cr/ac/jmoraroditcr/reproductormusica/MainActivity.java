package cr.ac.jmoraroditcr.reproductormusica;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean playing = false;
    MediaPlayer mediaPlayer;
    int songNumber = 20;
    String[] songs = new String[songNumber];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadSongs();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,songs);
        for(int i = 0 ; i<songs.length; i++){
            Log.i("songs","song"+songs[i]);
        }
        ListView listView = findViewById(R.id.songsListView);
        listView.setAdapter(adapter);
        //mediaPlayer = new MediaPlayer(findViewById(R.raw.Eminem_River));
    }

    public void loadSongs(){
        final R.raw rawResources = new R.raw();
        final Class<R.raw> rawClass = R.raw.class;
        final Field[] fields = rawClass.getDeclaredFields();
        final List<Integer> resourceIdList = new ArrayList<Integer>();

        for(int i = 0; i < songNumber; i++){
            songs[i] = "";
        }

        for (int i = 0, max = fields.length; i < max; i++) {
            final int resourceId;
            try {
                resourceId = fields[i].getInt(rawResources);
                resourceIdList.add(resourceId);
            }
            catch (Exception e) {
                continue;
            }
        }

        Resources resources = getResources();
        for (int i = 0; i < resourceIdList.size(); i++) {
            String resourceName = resources.getResourceEntryName(resourceIdList.get(i));
            resourceName.replace("cr.ac.jmoraroditcr.reproductormusica:raw/"," ");
            songs[i] = resourceName;
        }
    }
}
