package com.sns.todo_app_android_fireabse.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.Note;

import java.util.HashMap;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private EditText editTextTags;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        LogUtil.d(" Log " + " = NoteActivity　＝" );

        setContentView(R.layout.activity_main);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        editTextTags = findViewById(R.id.edit_text_tags);

//        textViewData = findViewById(R.id.text_view_data);
    }

    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }
        int priority = Integer.parseInt(editTextPriority.getText().toString());
        String tagInput = editTextTags.getText().toString();
        String[] tagArray = tagInput.split("\\s*,\\s*");
        Map<String, Boolean> tags = new HashMap<>();
        for (String tag : tagArray) {
            tags.put(tag, true);
        }
        Note note = new Note(title, description, priority, tags);
        notebookRef.document("14g7Y5YjuaRcmGAiikUi")
                .collection("Child Notes").add(note);
    }
    public void loadNotes(View v) {
        notebookRef.document("14g7Y5YjuaRcmGAiikUi")
                .collection("Child Notes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());
                            String documentId = note.getDocumentId();
                            data += "ID: " + documentId;
                            for (String tag : note.getTags().keySet()) {
                                data += "\n-" + tag;
                            }
                            data += "\n\n";
                        }
                        textViewData.setText(data);
                    }
                });
    }
}