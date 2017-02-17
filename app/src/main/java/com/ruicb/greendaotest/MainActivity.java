package com.ruicb.greendaotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ruicb.greendaotest.database.DaoSession;
import com.ruicb.greendaotest.database.Face;
import com.ruicb.greendaotest.database.Sticker;
import com.ruicb.greendaotest.database.StickerBackground;
import com.ruicb.greendaotest.database.StickerCategory;
import com.ruicb.greendaotest.database.StickerFont;
import com.ruicb.greendaotest.database.StickerText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void insertData(View view){
        DaoSession session = App.getDaoSession();
        Face face = new Face(null, ((EditText)findViewById(R.id.edit_text)).getText().toString(), "male", "a", "b");
        session.getFaceDao().insert(face);
        Sticker sticker = new Sticker(null, ((EditText)findViewById(R.id.edit_text)).getText().toString(), "sdcard", "newR", "a", "c");
        session.getStickerDao().insert(sticker);
        StickerBackground stickerBackground = new StickerBackground(null, "backPath", "red");
        session.getStickerBackgroundDao().insert(stickerBackground);
        StickerCategory stickerCategory = new StickerCategory(null, "newRow");
        session.getStickerCategoryDao().insert(stickerCategory);
        StickerText stickerText = new StickerText();
        session.getStickerTextDao().insert(stickerText);
    }

    public void addData(View view){
        DaoSession session = App.getDaoSession();
        //
        session.getStickerFontDao().insert(new StickerFont());
        session.getStickerFontDao().insert(new StickerFont());
        session.getStickerFontDao().insert(new StickerFont());
        session.getStickerFontDao().insert(new StickerFont());
    }

    public void deleteData(View view){
        DaoSession session = App.getDaoSession();
        //
        session.getStickerFontDao().deleteByKey(2L);
        session.getStickerFontDao().deleteByKey(4L);
        //
        session.getStickerFontDao().insert(new StickerFont());
        session.getStickerFontDao().insert(new StickerFont());
    }
}
