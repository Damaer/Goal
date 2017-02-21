package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

/**
 * Created by Tommy on 2017/2/21.
 */

public class NoteDetailAndEditActivity extends AppCompatActivity{

    private ImageButton returnButton;
    private ImageButton createGoalButton;
    private ImageButton shareButton;
    private ImageButton MenuButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_detail_and_edit);

        returnButton = (ImageButton)findViewById(R.id.return_button);
        createGoalButton = (ImageButton)findViewById(R.id.create_goal);
        shareButton = (ImageButton)findViewById(R.id.share);
        MenuButton = (ImageButton)findViewById(R.id.menu_more);

    }

}
