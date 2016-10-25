package com.example.vladi.solutiongroup.mvc.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.vladi.solutiongroup.R;


public class NoteCreationDialog extends Dialog implements View.OnClickListener, TextView.OnEditorActionListener {

    private static final String TAG = "NoteCreationDialog";

    private Listener mListener;
    private EditText newListNameEditText;
    private EditText newListBodyEditText;
    private TimePicker newListTimePicker;

    public NoteCreationDialog(Context context) {
        super(context, R.style.AppTheme_Dialog);
        setTitle(R.string.newListDialogTitle);
        setContentView(R.layout.note_creation_dialog);

        newListNameEditText = (EditText) findViewById(R.id.noteCreationDialogNameEditText);
        newListBodyEditText = (EditText) findViewById(R.id.noteCreationDialogBodyEditText); //TODO: Обработать, как и название
        newListTimePicker = (TimePicker)  findViewById(R.id.timePicker);
        newListTimePicker.setIs24HourView(true);
        Button saveButton = (Button) findViewById(R.id.listCreationDialogSaveButton);
        Button cancelButton = (Button) findViewById(R.id.listCreationDialogCancelButton);

        newListNameEditText.setOnEditorActionListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.listCreationDialogSaveButton) dispatchCreateNoteEvent();
        dismiss();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            dispatchCreateNoteEvent();
            dismiss();
            return true;
        }
        return false;
    }

    private void dispatchCreateNoteEvent() {
        if (mListener == null) return;
        String noteName = newListNameEditText.getText().toString();
        String noteBody = newListBodyEditText.getText().toString();

        int hours = newListTimePicker.getCurrentHour();
        int minute = newListTimePicker.getCurrentMinute();

        if (noteName.length() != 0)
            mListener.onCreateNote(noteName, noteBody, hours, minute);

    }


    public NoteCreationDialog setListener(Listener listener) {
        mListener = listener;
        setOnDismissListener(listener);
        return this;
    }

    public interface Listener extends OnDismissListener {
        void onCreateNote(String noteName, String noteBody, int hours, int minute);

        @Override
        void onDismiss(DialogInterface dialog);
    }


}
