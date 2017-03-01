package cn.goal.goal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import cn.goal.goal.R;
import cn.goal.goal.services.NoteService;

/**
 * Created by Tommy on 2017/2/23.
 */

public class NoteEditActivity extends AppCompatActivity {
    private ImageButton buttonReturn;
    private ImageButton buttonConfirm;
    private EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        buttonReturn = (ImageButton)findViewById(R.id.return_button);
        buttonConfirm = (ImageButton)findViewById(R.id.confirm);
        editText = (EditText)findViewById(R.id.note_content);

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString() != null && !editText.getText().toString().equals("")){
                    NoteService.createNote(editText.getText().toString());
                }
                finish();
            }
        });
    }
}
