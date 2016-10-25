package com.example.vladi.solutiongroup.mvc.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vladi.solutiongroup.R;
import com.example.vladi.solutiongroup.mvc.Service.NotificationService;
import com.example.vladi.solutiongroup.mvc.components.NoteCreationDialog;
import com.example.vladi.solutiongroup.mvc.controller.MainNoteViewAdapter;
import com.example.vladi.solutiongroup.mvc.model.MainNoteItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, NoteCreationDialog.Listener {
    private static final String TAG = "MainActivity";

    private TextView mToolbarTitleTextView;
    private Toolbar mActionBarToolbar;
    private ImageView mToolbarBackBtn;
    private ImageView mToolbarDeleteBtn;
    private FloatingActionButton mNewListButton;
    private List<MainNoteItem> mMainNoteItems = new ArrayList<>();
    private MainNoteViewAdapter mMainNoteViewAdapter;
    private String currentState = STATE_NORMAL;

    private static final String STATE_NORMAL = "normal";
    private static final String STATE_DELETE = "delete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initFAB();
        initMainListView();

        //TODO: перенести в отдельную функцию
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    protected void onStart() {
        super.onStart();
        //  readFile();
        setState(STATE_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNewListButton.show();
    }

    @Override
    protected void onPause() {
        //writeFile();
        super.onPause();
        mNewListButton.hide();
    }

   /* void writeFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            TODO: writer+reader from XML. Или SQLite
        }
    }

   // void readFile() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            while ((str = br.readLine()) != null) {
                Log.d(LOG_TAG, str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         }
         */

    // ---------------------------------------------------------------------
    // --- INITIALIZATION
    // ---------------------------------------------------------------------

    private void initToolbar() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView = (TextView) mActionBarToolbar.findViewById(R.id.toolbar_title);
        mToolbarBackBtn = (ImageView) mActionBarToolbar.findViewById(R.id.toolbar_back_btn);
        mToolbarDeleteBtn = (ImageView) mActionBarToolbar.findViewById(R.id.toolbar_delete_btn);
        mToolbarBackBtn.setOnClickListener(this);
        mToolbarDeleteBtn.setOnClickListener(this);
    }

    private void initFAB() {
        mNewListButton = (FloatingActionButton) findViewById(R.id.newListButton);
        mNewListButton.setOnClickListener(this);
    }

    private void initMainListView() {
        ListView listView = (ListView) findViewById(R.id.mainListView);
        mMainNoteViewAdapter = new MainNoteViewAdapter(this, mMainNoteItems);
        listView.setAdapter(mMainNoteViewAdapter);
        listView.setOnItemClickListener(this);
    }


    // ---------------------------------------------------------------------
    // --- BASE LOGIC
    // ---------------------------------------------------------------------

    public void setState(String state) {
        currentState = state;
        if (state.equals(STATE_NORMAL)) {
            mToolbarTitleTextView.setText(R.string.toolbar_text);
            mActionBarToolbar.setBackgroundResource(R.color.colorPrimary);
            mToolbarBackBtn.setVisibility(View.GONE);
            mToolbarDeleteBtn.setVisibility(View.VISIBLE);
            mNewListButton.show();

        } else if (state.equals(STATE_DELETE)) {
            mToolbarTitleTextView.setText(R.string.toolbarDeleteTitle);
            mActionBarToolbar.setBackgroundResource(R.color.transparentRed);
            mToolbarBackBtn.setVisibility(View.VISIBLE);
            mToolbarDeleteBtn.setVisibility(View.GONE);
            mNewListButton.hide();
        }
        mMainNoteViewAdapter.setViewState(state);

    }

    // ---------------------------------------------------------------------
    // --- View.OnClickListener implementation
    // ---------------------------------------------------------------------

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.toolbar_back_btn:
                Log.w(TAG, " MainActivity::onClick(toolbar_back_btn)");
                setState(STATE_NORMAL);
                break;
            case R.id.toolbar_delete_btn:
                Log.w(TAG, "MainActivity::onClick(toolbar_delete_btn)");
                setState(STATE_DELETE);
                break;
            case R.id.newListButton:
                Log.w(TAG, "MainActivity::onClick(newListButton)");//так информативнее =)))
                mNewListButton.hide();
                NoteCreationDialog dialog = new NoteCreationDialog(this);
                dialog.setListener(this).show();
                break;
        }

    }

    // ---------------------------------------------------------------------
    // --- NoteCreationDialog.Listener implementation
    // ---------------------------------------------------------------------

    @Override
    public void onCreateNote(String noteName, String noteBody, int hours, int minute) {
        mMainNoteItems.add(new MainNoteItem(noteName + "\n" + noteBody + "\n" + hours + ":" + minute));
        mMainNoteViewAdapter.notifyDataSetChanged();
        Date date = new Date();
        date.setHours(hours);
        date.setMinutes(minute);
        date.setSeconds(0);
        Log.w(TAG, "onCreateNote: " + date.getTime());

        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra(NotificationService.EXTRA_TITLE, noteName);
        intent.putExtra(NotificationService.EXTRA_BODY, noteBody);
        intent.putExtra(NotificationService.EXTRA_TIME, date.getTime());
        startService(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mNewListButton.show();
    }

    // ---------------------------------------------------------------------
    // --- AdapterView.OnItemClickListener implementation
    // ---------------------------------------------------------------------

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (currentState.equals(STATE_NORMAL)) {
            // TODO: go to list activity
        } else if (currentState.equals(STATE_DELETE)) {
            // TODO: make a dialog.
            // TEMP: !!!
            mMainNoteItems.remove(position);
            mMainNoteViewAdapter.notifyDataSetChanged();
        }
    }
}

