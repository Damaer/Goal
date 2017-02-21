package cn.goal.goal;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

/**
 * Created by Tommy on 2017/2/21.
 */

public class NoteDetailAndEditActivity extends AppCompatActivity{

    ImageButton returnButton;
    ImageButton createGoalButton;
    ImageButton shareButton;
    ImageButton MenuButton;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.note_detail_and_edit);

        returnButton = (ImageButton)findViewById(R.id.return_button);
        createGoalButton = (ImageButton)findViewById(R.id.create_goal);
        shareButton = (ImageButton)findViewById(R.id.share);
        MenuButton = (ImageButton)findViewById(R.id.menu_more);

    }

}
