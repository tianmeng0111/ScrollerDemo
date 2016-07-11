package com.tm.example.scrollerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tm.example.scrollerdemo.view.CustomLinearLayout;

public class MainActivity extends AppCompatActivity {
    private CustomLinearLayout llMenu;

    private boolean isMenuShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llMenu = (CustomLinearLayout) findViewById(R.id.ll_menu);

        llMenu.smoothScrollTo(-240, 0);
    }

    public void scroll(View view) {
        if (isMenuShow){
            llMenu.smoothScrollTo(0, 0);
            isMenuShow = false;
        } else {
            llMenu.smoothScrollTo(-240, 0);
            isMenuShow = true;
        }
    }

    public void edit(View view) {
        Log.e("---", "============edit click");
        Toast.makeText(MainActivity.this, "edit click", Toast.LENGTH_LONG).show();
    }
    public void delete(View view) {
        Log.e("---", "============delete click");
        Toast.makeText(MainActivity.this, "delete click", Toast.LENGTH_LONG).show();
    }

    public void goListView(View view) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_main,  menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_main:
//                Intent intent = new Intent(MainActivity.this, ListActivity.class);
//                startActivity(intent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
