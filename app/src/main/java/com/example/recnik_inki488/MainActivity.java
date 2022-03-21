package com.example.recnik_inki488;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int index;
    ListView words_search;
    ArrayAdapter<String> adapter;
    TextInputEditText txtEngEdit;
    public List<String> MkdList = new ArrayList<String>();
    TextInputEditText txtMkdEdit;
    public List<String> EngList = new ArrayList<String>();
    public List<String> MacedonianEnglishList = new ArrayList<String>();
    Boolean isEdit=false;


    public void insert(View view) {
        txtMkdEdit = findViewById(R.id.inputMacedonian);
        txtEngEdit = findViewById(R.id.inputEnglish);
        if(isEdit==false) {
            MkdList.add(txtMkdEdit.getText().toString().trim());
            EngList.add(txtEngEdit.getText().toString().trim());
            MacedonianEnglishList.add(txtMkdEdit.getText().toString().trim()+"-"+txtEngEdit.getText().toString().trim());
        }
        else if (isEdit==true) {
            MkdList.set(index,txtMkdEdit.getText().toString().trim());
            EngList.set(index,txtEngEdit.getText().toString().trim());
            MacedonianEnglishList.set(index,txtMkdEdit.getText().toString().trim()+"-"+txtEngEdit.getText().toString().trim());
            isEdit=false;
            Button saveButton = findViewById(R.id.saveBtn);
            saveButton.setText("Зачувај");
        }
        else{
            System.out.println("Неуспешно");
        }

        txtMkdEdit.setText("");
        txtEngEdit.setText("");

        ListView listView = (ListView) findViewById(R.id.words_search);
        adapter = new MyListAdapter(this, R.layout.list_item, MacedonianEnglishList);
        listView.setAdapter(adapter);

        File file = new File(MainActivity.this.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File gpxfile = new File(file, "Dictionary.txt");
            FileWriter writer = new FileWriter(gpxfile);
            for (int i = 0; i < MacedonianEnglishList.size(); i++) {
                writer.write(MkdList.get(i) + "    " + EngList.get(i) + System.lineSeparator());
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String string = new String();
        File file = new File(MainActivity.this.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File gpxfile = new File(file, "Dictionary.txt");
            FileReader reader = new FileReader(gpxfile);
            reader.close();
            BufferedReader in = new BufferedReader(new FileReader(gpxfile));
            string = in.readLine();
            int i=0;
            while(string!=null){
                String[] words = string.split("    ");
                MkdList.add(words[0]);
                EngList.add(words[1]);
                MacedonianEnglishList.add(words[0]+""+"/"+""+words[1]);
                string = in.readLine();
                i++;
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        ListView listView = (ListView) findViewById(R.id.words_search);
        adapter = new MyListAdapter(this, R.layout.list_item, MacedonianEnglishList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtMkdEdit = findViewById(R.id.inputMacedonian);
                txtEngEdit = findViewById(R.id.inputEnglish);
                txtMkdEdit.setText(MkdList.get(position));
                txtEngEdit.setText(EngList.get(position));

                index=position;
                isEdit=true;
                Button saveButton = findViewById(R.id.saveBtn);
                saveButton.setText("EDIT");
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (Button) convertView.findViewById(R.id.name_list);

                convertView.setTag(viewHolder);
            }
            mainViewHolder = (ViewHolder) convertView.getTag();

            mainViewHolder.title.setText(getItem(position));

            TextInputEditText search = (TextInputEditText) findViewById (R.id.searchInput);

            search.addTextChangedListener (new TextWatcher() {
                @Override
                public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

                }
                @Override
                public void onTextChanged (CharSequence charsequence, int i, int i1, int i2) {
                    MainActivity.this.adapter.getFilter().filter (charsequence) ;
                }

                @Override
                public void afterTextChanged (Editable editable) {

                }
            });

            return convertView;
        }
    }
    public class ViewHolder {


        TextView title;

    }
}