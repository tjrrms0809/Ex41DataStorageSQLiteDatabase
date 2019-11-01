package com.ahnsafety.ex41datastoragesqlitedatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etName, etAge, etEmail;

    String dbName="Data.db"; //database파일명
    String tableName="member"; //표 이름

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName= findViewById(R.id.et_name);
        etAge= findViewById(R.id.et_age);
        etEmail= findViewById(R.id.et_email);

        //dbName으로 데이터베이스 파일 생성 또는 열기
        db= this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

        //만들어진 DB파일에 "member"라는 이름으로 테이블 생성 작업 수행
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(num integer primary key autoincrement, name text not null, age integer, email text);");

    }

    public void clickInsert(View view) {

        String name= etName.getText().toString();
        int age= Integer.parseInt(etAge.getText().toString());
        String email= etEmail.getText().toString();

        //데이터베이스에 데이터를 삽입(insert)하는 쿼리문 실행
        db.execSQL("INSERT INTO "+ tableName+"(name, age, email) VALUES('"+name+"','"+age+"','"+email+"')");

        etName.setText("");
        etAge.setText("");
        etEmail.setText("");

    }

    public void clickSelectAll(View view) {

        Cursor cursor= db.rawQuery("SELECT * FROM "+tableName, null);//두번째 파라미터 : 검색조건
        //Cursor객체 : 결과 table을 참조하는 객체
        //Cursor객체를 통해 결과 table의 값들을
        //읽어오는 것임.
        if(cursor==null) return;

        StringBuffer buffer= new StringBuffer();

        while ( cursor.moveToNext() ){
            int num= cursor.getInt(0);
            String name= cursor.getString(1);
            int age= cursor.getInt(2);
            String email= cursor.getString(3);

            buffer.append(num+"  "+name+"  "+age+"  "+email+"\n");
        }

        new AlertDialog.Builder(this).setMessage(buffer.toString()).setPositiveButton("OK", null).create().show();
    }


    public void clickSelectByName(View view) {

        String name= etName.getText().toString();

        Cursor cursor=db.rawQuery("SELECT name, age FROM "+tableName+" WHERE name=?", new String[]{name} );
        if(cursor==null) return;

        //참고 : 총 레코드 수(줄,행(row)수)
        int rowNum= cursor.getCount();//데이터의 행의 수

        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext()){
            String na= cursor.getString(0);
            int age= cursor.getInt(1);

            buffer.append(na+"  "+ age+"\n");
        }

        Toast.makeText(this, buffer.toString(), Toast.LENGTH_SHORT).show();

    }

    public void clickUpdate(View view) {
        String name= etName.getText().toString();
        db.execSQL("UPDATE "+tableName+" SET age=30, email='ss@ss.com' WHERE name=?", new String[]{name});
    }

    public void clickDelete(View view) {
        String name= etName.getText().toString();
        db.execSQL("DELETE FROM "+tableName+" WHERE name=?",new String[]{name});

    }
}

