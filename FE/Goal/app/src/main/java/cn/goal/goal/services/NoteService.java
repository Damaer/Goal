package cn.goal.goal.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.goal.goal.services.object.Note;

import java.util.ArrayList;
import java.util.Date;

import static cn.goal.goal.utils.Util.dateToString;

/**
 * Created by chenlin on 25/02/2017.
 */
public class NoteService {
    private static ArrayList<Note> notes; // 所有标签

    protected static void getLocalNotes(SQLiteDatabase db) {
        notes = new ArrayList<>();
        Cursor cursor = db.query("note", null, null, null, null, null, null);
        int idIndex = cursor.getColumnIndex("id");
        int _idIndex = cursor.getColumnIndex("_id");
        int contentIndex = cursor.getColumnIndex("content");
        int createAtIndex = cursor.getColumnIndex("createAt");
        int updateAtIndex = cursor.getColumnIndex("updateAt");
        while (cursor.moveToNext()) {
            Note note = new Note(
                    cursor.getInt(idIndex),
                    cursor.getString(_idIndex),
                    cursor.getString(contentIndex),
                    cursor.getString(createAtIndex),
                    cursor.getString(updateAtIndex)
            );
            notes.add(note);
        }
        cursor.close();
    }

    /**
     * 获取用户所有标签信息
     *
     * @return
     */
    public static ArrayList<Note> getNotes() {
        return notes;
    }

    /**
     * 新建用户标签
     *
     * @param content 标签内容
     * @return 成功返回null, 失败返回错误信息
     */
    public static String createNote(String content) {
        String _id = "null";
        String createAt = dateToString(new Date());

        SQLiteDatabase db = UserService.getDB();
        ContentValues cv = new ContentValues();
        cv.put("_id", _id);
        cv.put("content", content);
        cv.put("createAt", createAt);
        cv.put("updateAt", createAt);
        db.insert("note", null, cv);

        // 获取最后插入的记录id
        Cursor cursor = db.rawQuery("select last_insert_rowid() from note", null);
        int lastId = 0;
        if (cursor.moveToFirst()) lastId = cursor.getInt(0);
        cursor.close();

        if (lastId == 0) return "新建目标失败";
        if (notes == null) notes = new ArrayList<>();

        notes.add(new Note(lastId, _id, content, createAt, createAt));
        return null;
    }

    /**
     * 获取指定id的note信息
     *
     * @param noteId 要获取的noteId
     * @return 成功返回note信息，失败返回null
     */
    public static Note getNote(int noteId) {
        int noteIndex = findNoteById(noteId);
        if (noteIndex == -1) return null;
        return notes.get(noteIndex);
    }

    /**
     * 查找note
     *
     * @param noteId noteId
     * @return noteId在notes数组中的下表Index;
     */
    public static int findNoteById(int noteId) {
        for (int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).getId() == noteId) return i;
        }
        return -1;
    }

    /**
     * 更新标签内容
     *
     * @param note    标签
     * @param content 标签内容
     * @return 更新成功返回null, 失败返回错误信息
     */
    public static String updateNote(Note note, String content) {
        // 本地更新
        String updateAt = dateToString(new Date());
        SQLiteDatabase db = UserService.getDB();
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        cv.put("updateAt", updateAt);
        db.update("note", cv, "id=?", new String[]{String.valueOf(note.getId())});

        // 更新goals数组信息
        note.setContent(content);
        return null;
    }

    /**
     * 删除指定标签
     *
     * @param note 要删除标签
     * @return 成功返回null, 失败返回错误信息
     */
    public static String deleteNote(Note note) {
        // 本地删除
        SQLiteDatabase db = UserService.getDB();
        db.delete("note", "id=?", new String[]{String.valueOf(note.getId())});
        int index = findNoteById(note.getId());
        if (index != -1) {
            notes.remove(index);
        }
        return null;
    }
}
