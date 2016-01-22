package com.example.skiselyov.myapplication.com.example.notepadby.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.skiselyov.myapplication.R;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.model.UserSession;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.Constants;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.CryptoManager;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.RemoteUtil;

import org.json.JSONObject;

import java.io.File;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class FilesListActivity extends ListActivity {
    private ArrayList<String> itemlist = null;

    private UserSession userSession = new UserSession();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);
        itemlist = new ArrayList<>();
        userSession = (UserSession)getIntent().getSerializableExtra(Constants.CONST_USER_EXTRA);
        new RetrieveRSSFeeds().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String fileName = itemlist.get(position);
        new CardFileLoader().execute(Constants.CONST_TEMP_DIR + fileName);
    }

    private class CardFileLoader extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            KeyPair keyPair = CryptoManager.loadPair(getApplicationContext());
            //RemoteUtil.getFilesList("getFilesList", "getFilesList");
            String response = RemoteUtil.uploadFile(params[0], keyPair, userSession);
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject responseCode) {
            super.onPostExecute(responseCode);
        }
    }

    private class RetrieveRSSFeeds extends AsyncTask<Void, Void, Void>
    {
        private RSSListAdaptor rssadaptor = null;

        @Override
        protected Void doInBackground(Void... params) {
            File sdCard = Environment.getExternalStorageDirectory();
            System.out.println(sdCard.getAbsolutePath());
            System.out.println(sdCard.canRead());
            File tempDirectory = new File(Constants.CONST_TEMP_DIR);
            if (!tempDirectory.exists()) {
                boolean directoryCreated = tempDirectory.mkdir();
            }
            File[] cardFiles = tempDirectory.listFiles();
            for (File file : cardFiles) {
                itemlist.add(file.getName());
            }
            rssadaptor = new RSSListAdaptor(FilesListActivity.this, R.layout.rssitemview,itemlist);
            return null;
        }

//        File tmpFile = new File(sdCard.getPath() + "/flink/tmp2.mp3");
//        try {
//        tmpFile.createNewFile();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//        try {
//        PrintWriter printWriter = new PrintWriter(tmpFile);
//        printWriter.write("dasdasdasdadasdasdasdsad");
//        printWriter.write("dasdasdasdadasdasdasdsaddasdqqdeqd");
//        printWriter.close();
//    } catch (FileNotFoundException e) {
//        e.printStackTrace();
//    }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(rssadaptor);
            super.onPostExecute(result);
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
                LayoutInflater vi = (LayoutInflater)FilesListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
