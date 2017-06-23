package com.example.dudco.minimal_todo;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView list;
    MyListAdapter adapter;

    LinearLayout notodos;
    FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        notodos = (LinearLayout) findViewById(R.id.notodos);
        container = (FrameLayout) findViewById(R.id.container);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        findViewById(R.id.main_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddTodoActivity.class), 200);
            }
        });

        list = (ListView) findViewById(R.id.list);
        adapter = new MyListAdapter();
        list.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
//                super.onChanged();
                if(adapter.getCount() > 0){
                    notodos.setVisibility(View.INVISIBLE);
                    container.setVisibility(View.VISIBLE);
                }else{
                    notodos.setVisibility(View.VISIBLE);
                    container.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 200){
                Toast.makeText(this, "okay", Toast.LENGTH_SHORT).show();
                Log.d("dudco", data.getLongExtra("date", 0) + "    " + data.getStringExtra("title"));
                Long lDate = data.getLongExtra("date", 0);
                Date date = new Date(lDate);
                String title = data.getStringExtra("title");
                adapter.addItem(new ListItemData(date, title));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting :
                Toast.makeText(this, "Click Setting!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this, "Click Abuut!", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    class MyListAdapter extends BaseAdapter {

        ArrayList<ListItemData> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void addItem(ListItemData item){
            items.add(item);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int i, View parents, ViewGroup viewGroup) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, viewGroup, false);
            ListItemData item = items.get(i);

            Log.d("dudco", item.getDate() + "   " + item.getTitle());
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            TextView title = view.findViewById(R.id.toDoListItemTextview);
            TextView date = view.findViewById(R.id.todoListItemTimeTextView);
            ImageView image = view.findViewById(R.id.toDoListItemColorImageView);

            title.setText(item.getTitle());
            date.setText(AddTodoActivity.formatDate("d MMM, yyyy, h:mm a", item.getDate()));

            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(item.getTitle().substring(0,1), color);

            image.setImageDrawable(myDrawable);
            return view;
        }
    }

    class ListItemData{
        private Date date;
        private String title;

        public ListItemData(Date date, String title) {
            this.date = date;
            this.title = title;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
