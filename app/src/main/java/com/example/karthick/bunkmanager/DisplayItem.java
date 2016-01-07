package com.example.karthick.bunkmanager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.ArrayList;

/**
 * Created by karthick on 07/01/16.
 */
public class DisplayItem extends AppCompatActivity {

    private final String dbName = "Android";
    private final String tableName="user";
    SQLiteDatabase sampleDB ;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    ArrayList<String> sub,credits;
    String name;
    CircleDisplay cd;
    TextView ed1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mainListView = (ListView) findViewById( R.id.mainListView );
        mainListView.setTextFilterEnabled(true);
        sub = new ArrayList<String>();
        credits = new ArrayList<String>();
        ed1=(TextView) findViewById(R.id.textView2);
        cd = (CircleDisplay) findViewById(R.id.circleDisplay);
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                removeItemFromList(arg0, position);
                return true;
            }
        });
        call();
    }
    protected void removeItemFromList(AdapterView<?> arg0,int position) {
        final int deletePosition = position;
        name = (String) arg0.getItemAtPosition(deletePosition);
        AlertDialog.Builder alert = new AlertDialog.Builder(
                DisplayItem.this);
        alert.setTitle("Delete");
        alert.setMessage("Do You Want To Delete?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sub.remove(deletePosition);
                delete(name);
                listAdapter.notifyDataSetChanged();
                listAdapter.notifyDataSetInvalidated();
                Toast.makeText(DisplayItem.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                call();
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    public void delete(String del)
    {
        sampleDB =  this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (sub VARCHAR,credits VARCHAR,color VARCHAR);");
        //sampleDB.execSQL("DELETE * FROM " + tableName+"WHERE id='"+del+"'");
        sampleDB.delete(tableName, "sub = ?", new String[]{del});
    }
    public void call()
    {

        try {
            sampleDB =  this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (sub VARCHAR,credits VARCHAR,color VARCHAR);");
            refresh();
            Cursor c = sampleDB.rawQuery("SELECT DISTINCT sub,credits,color FROM " + tableName +" ORDER BY sub Collate NOCASE ", null);
            if(c.getCount()==0)
                ed1.setVisibility(View.VISIBLE);
            else
                ed1.setVisibility(View.INVISIBLE);
            if (c != null ) {

                if  (c.moveToFirst()) {
                    do {
                        String subj = c.getString(c.getColumnIndex("sub"));
                        String cnt=c.getString(c.getColumnIndex("credits"));
                        String col=c.getString(c.getColumnIndex("color"));

                        sub.add(subj);
                        credits.add("Number Of Credits:"+cnt+" Color:"+col);
                    }while (c.moveToNext());
                }
            }
            //listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sub);
            listAdapter = new ArrayAdapter(this,R.layout.list_item,sub){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    TwoLineListItem row;
                    if(convertView == null){
                        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        row = (TwoLineListItem)inflater.inflate(R.layout.list_item, null);
                    }else{
                        row = (TwoLineListItem)convertView;
                    }
                    row.getText1().setText(sub.get(position));
                    row.getText2().setText(credits.get(position));
                    return row;
                }
            };
            //adapter=new CustomListAdapter(this, sub);
            mainListView.setAdapter( listAdapter );
        } catch (SQLiteException se ) {
            Toast.makeText(getApplicationContext(), "Couldn't create or open the database", Toast.LENGTH_LONG).show();
        } finally {
            if (sampleDB != null) {
                //sampleDB.execSQL("DELETE FROM " + tableName);
                sampleDB.close();
            }
        }
    }
    public void refresh()
    {
        sub.clear();
        credits.clear();
        mainListView.setAdapter(listAdapter);
    }
}
