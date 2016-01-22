package com.example.skiselyov.myapplication.com.example.notepadby.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.skiselyov.myapplication.R;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.model.UserSession;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.Constants;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.CryptoManager;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.RemoteUtil;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ListActivity {
    private ArrayList<String> itemlist = null;

    private UserSession userSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemlist = new ArrayList<>();
        userSession = (UserSession) getIntent().getSerializableExtra(Constants.CONST_USER_EXTRA);
        new GetFilesTask().execute();
        Button button = (Button) findViewById(R.id.fileNewButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilesListActivity.class);
                intent.putExtra(Constants.CONST_USER_EXTRA, userSession);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String data = itemlist.get(position);
        new CheckSignatureTask().execute(data);
    }

    private class CheckSignatureTask extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {
            KeyPair keyPair = CryptoManager.loadPair(getApplicationContext());
            try {
                return RemoteUtil.checkSignature(params[0], userSession, keyPair);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class GetFilesTask extends AsyncTask<Void, Void, Void>
    {
        private RSSListAdaptor rssadaptor = null;

        @Override
        protected Void doInBackground(Void... params) {
            String result = RemoteUtil.getFilesList(userSession);
            itemlist = new ArrayList<>();
            Collections.addAll(itemlist, result.split(":"));
            rssadaptor = new RSSListAdaptor(MainActivity.this, R.layout.rssitemview,itemlist);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(rssadaptor);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class RSSListAdaptor extends ArrayAdapter<String>{
        private List<String> files = null;

        public RSSListAdaptor(Context context, int textviewid, List<String> objects) {
            super(context, textviewid, objects);
            this.files = objects;
        }

        @Override
        public int getCount() {
            return ((null != files) ? files.size() : 0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public String getItem(int position) {
            return ((null != files) ? files.get(position) : null);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (null == view) {
                LayoutInflater vi = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.rssitemview, null);
            }
            String file = files.get(position);
            if (null != file) {
                TextView title = (TextView)view.findViewById(R.id.textView);
                title.setText(file);
            }
            return view;
        }
    }
}
