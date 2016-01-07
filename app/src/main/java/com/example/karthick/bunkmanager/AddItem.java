package com.example.karthick.bunkmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

/**
 * Created by karthick on 07/01/16.
 */
public class AddItem extends AppCompatActivity {

    EditText et1,et2;
    Button button;

    private final String dbName = "Android";
    private final String tableName="user";
    SQLiteDatabase sampleDB ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        button=(Button)findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (et1.getText().toString().matches("") || et2.getText().toString().matches("")) {
                    Toast.makeText(AddItem.this,"Please enter a valid subject name or credits",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    final ColorPicker cp = new ColorPicker(AddItem.this, 63, 81, 181);
                    cp.show();

                    Button okColor = (Button) cp.findViewById(R.id.okColorButton);
                    okColor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedColorRGB = cp.getColor();
                            String hexColor = String.format("#%06X", (0xFFFFFF & selectedColorRGB));
                            cp.dismiss();

                            sampleDB =  openOrCreateDatabase(dbName, MODE_PRIVATE, null);
                            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (sub VARCHAR,credits VARCHAR,color VARCHAR);");
                            sampleDB.execSQL("INSERT INTO " + tableName + "(sub,credits,color) Values ('" + et1.getText().toString() + "','" + et2.getText().toString() + "','" + hexColor+ "');");
                            Toast.makeText(AddItem.this,"Successfully Entered!!!",Toast.LENGTH_SHORT).show();

                            finish();
                            Intent intent = new Intent(AddItem.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}
