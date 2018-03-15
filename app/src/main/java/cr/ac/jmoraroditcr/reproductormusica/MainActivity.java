package cr.ac.jmoraroditcr.reproductormusica;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean playing = false;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int songNumber = 20;
    private float volume = 0.5f;
    private String[] songs = new String[songNumber];
    private final List<Integer> resourceIdList = new ArrayList<Integer>();
    private int currentSong = 0;
    private String currentSongName = "";


    public void onNextSongClicked(View view){
        nextSong();
    }
    public void onPrevSongClicked(View view){
        prevSong();
    }
    public void playButtonClicked(View view) {
        ImageView button = (ImageView) view;
        if(!playing){
            mediaPlayer.start();
            playing = true;

            button.setImageResource(R.drawable.pausebutton);
        }else{
            playing = false;
            mediaPlayer.pause();
            button.setImageResource(R.drawable.playbutton);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadSongs();
        fixSongsName();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,songs);
        for(int i = 0 ; i<songs.length; i++){
            Log.i("songs","song"+songs[i]);
        }
        ListView listView = findViewById(R.id.songsListView);
        listView.setAdapter(adapter);
        seekBar = findViewById(R.id.volumeSeekBar);
        mediaPlayer = MediaPlayer.create(this, resourceIdList.get(0));
        setVolume();
        TextView textView = findViewById(R.id.txtCancion);
        setCurrentSongName();
        setSongName();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                nextSong();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                ImageView imageView = findViewById(R.id.imgVolume);

                if(progress == 0){
                    imageView.setImageResource(R.drawable.novolumebutton);
                }else{
                    if(progress == 100)
                        imageView.setImageResource(R.drawable.volumebutton);
                    else
                        imageView.setImageResource(R.drawable.medvolumebutton);
                }

                volume = progress / 100.0f;
                setVolume();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        ListView songsListView = findViewById(R.id.songsListView);
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                currentSong = position;
                changeSong();
            }
        });
    }
    public void nextSong(){
        currentSong += 1;
        currentSong %= songNumber;
        changeSong();
    }
    public void prevSong(){
        if(currentSong == 0)
            currentSong = songNumber-1;
        else
            currentSong -=1;
        changeSong();
    }
    public void changeSong(){
        setCurrentSongName();
        setSongName();
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), resourceIdList.get(currentSong));
        setVolume();
        if(playing)
            mediaPlayer.start();
        //resetSongSeekBar
    }
    public void setVolume(){
        mediaPlayer.setVolume(volume,volume);
    }
    public void setCurrentSongName(){
        currentSongName = songs[currentSong];
    }
    public void setSongName(){
        TextView textView = findViewById(R.id.txtCancion);
        textView.setText(currentSongName);
    }
    public void loadSongs(){
        final R.raw rawResources = new R.raw();
        final Class<R.raw> rawClass = R.raw.class;
        final Field[] fields = rawClass.getDeclaredFields();


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
    }/*
    public void fixSongsName(){
        for (int i = 0; i < resourceIdList.size(); i++) {
            if(songs[i] != ""){
                String firstLetter = (char)(songs[i].charAt(0)-32) + "";
                songs[i].replaceFirst(String.valueOf(songs[i].charAt(0)),(songs[i].charAt(0)-32)+"");
                int underScoreIndex = songs[i].indexOf("_") + 1;
                songs[i] = firstLetter + songs[i].substring(1,underScoreIndex-1)+" - "+ (char)(songs[i].charAt(underScoreIndex)-32) +  songs[i].substring(underScoreIndex+1);
                songs[i].replaceFirst("_"," - ");
            }
        }
    }*/
    public void fixSongsName(){
        for (int i = 0; i < resourceIdList.size(); i++) {
            if(songs[i] != ""){
                int underScoreIndex;
                String firstLetter = (char)(songs[i].charAt(0)-32) + "";
                for(int j = 0; j < songs[i].length(); j++){
                    if(songs[i].charAt(j) == '_') {
                        underScoreIndex = j + 1;
                        songs[i] = firstLetter + songs[i].substring(1,underScoreIndex-1)+" "+ (char)(songs[i].charAt(underScoreIndex)-32) +  songs[i].substring(underScoreIndex+1);
                    }else
                        if(songs[i].charAt(j) == '0'){
                            underScoreIndex = j + 1;
                            songs[i] = firstLetter + songs[i].substring(1,underScoreIndex-1)+" - "+ (char)(songs[i].charAt(underScoreIndex)-32) +  songs[i].substring(underScoreIndex+1);
                        }

                }
            }
        }
    }
}
