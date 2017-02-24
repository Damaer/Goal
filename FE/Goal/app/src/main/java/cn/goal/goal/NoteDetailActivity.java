package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.Note;
import cn.goal.goal.utils.Share;

/**
 * Created by Tommy on 2017/2/21.
 */

public class NoteDetailActivity extends AppCompatActivity{

    private ImageButton returnButton;
    private ImageButton createGoalButton;
    private ImageButton shareButton;
    private ImageButton MenuButton;

    private PopupMenu notePopUpMenu;
    private EditText editText;

    private int noteIndex;
    private Note note;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_detail);

        if (getIntent() == null) {
            Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        noteIndex = getIntent().getExtras().getInt("noteIndex");
        note = UserService.getNote(noteIndex);
        returnButton = (ImageButton)findViewById(R.id.return_button);
        createGoalButton = (ImageButton)findViewById(R.id.create_goal);
        shareButton = (ImageButton)findViewById(R.id.share);
        MenuButton = (ImageButton)findViewById(R.id.menu_more);

        editText = (EditText)findViewById(R.id.note_content);
        createMenu();
        addListener();
    }

    protected void onResume() {
        super.onResume();
        editText.setText(note.getContent());
    }
    private void addListener(){
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notePopUpMenu.show();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.shareText(v.getContext(), "share test");
            }
        });
    }
    private void createMenu(){
        notePopUpMenu = new PopupMenu(this,MenuButton);
        notePopUpMenu.getMenuInflater().inflate(R.menu.note_operation_popupmenu,notePopUpMenu.getMenu());
    }
}
