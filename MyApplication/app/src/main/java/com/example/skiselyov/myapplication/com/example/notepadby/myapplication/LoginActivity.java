package com.example.skiselyov.myapplication.com.example.notepadby.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.skiselyov.myapplication.R;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.model.UserSession;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.Constants;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.RemoteUtil;

public class LoginActivity extends Activity {

    private String login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginButtonOnClickListener);

        //  Button registerButtonLink = (Button)findViewById(R.id.registerLink);
        //  registerButtonLink.setOnClickListener(registerButtonLinkOnClick);
    }

    public View.OnClickListener loginButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText editLogin = (EditText) findViewById(R.id.editLogin);
            EditText editPassword = (EditText) findViewById(R.id.editPassword);
            login = editLogin.getText().toString();
            password = editPassword.getText().toString();
            login(login, password);
        }
    };

//    public View.OnClickListener registerButtonLinkOnClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//            startActivity(intent);
//        }
//    };

    private void login(String login, String password) {
        new RemoteLogger().execute(login, password);
    }

    private class RemoteLogger extends AsyncTask<String, Void, UserSession> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Trying to login...");
            progressDialog.show();
        }

        @Override
        protected UserSession doInBackground(String... params) {
            UserSession userSession = new UserSession();
            userSession.setLogin(params[0]);
            userSession.setPassword(params[1]);
            boolean isLogged = RemoteUtil.remoteLogin(userSession);
            if (isLogged) {
                userSession.setIsAuthenticated(true);
                return userSession;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserSession userSession) {
            super.onPostExecute(userSession);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (userSession == null) {
                return;
            }
            //new BackgroundWorker().execute();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(Constants.CONST_USER_EXTRA, userSession);
            startActivity(intent);
        }
    }
}
