package com.nearsoft.pleasenoteme.repository;

import android.database.Cursor;
import android.test.mock.MockContext;

import junit.framework.TestCase;

public class NotesDbAdapterTest extends TestCase {

    MockContext context;
    NotesRepository repository;

    private static final String TITLE="This is a title";
    private static final String CONTENT="This is the content of the note";
    private static final String LONG_TITLE="This is a title. This is a title. This is a title";
    private static final String LONG_CONTENT="This is the content of the note. " +
            "This is the content of the note. This is the content of the note" +
            "This is the content of the note. This is the content of the note";


    public void setUp() {
        context = new MockContext();
        repository = new NotesRepository(context);
    }

    public void testCreateNoteNullParameters() throws Exception {
        long id = repository.createNote(null, CONTENT);
        assertEquals(-1, id);
        id = repository.createNote(TITLE, null);
        assertEquals(-1, id);
        id = repository.createNote(null, null);
        assertEquals(-1, id);
    }

    public void testCreateNoteLongValues() throws Exception {
        //Modified from the original estimate
        long id = repository.createNote(TITLE, LONG_CONTENT);
        assertTrue(id > 0);
        id = repository.createNote(LONG_TITLE, CONTENT);
        assertTrue(id > 0);
        id = repository.createNote(LONG_TITLE, LONG_CONTENT);
        assertTrue(id > 0);
    }

    public void testCreateNote() throws Exception {
        long id = repository.createNote(TITLE, CONTENT);
        assertTrue(id > 0);
    }

    public void testGetAllNotes() throws Exception {
        Cursor cursor = repository.getAllNotes();
        assertEquals(4, cursor.getCount());
    }

    public void testGetNotesByName() throws Exception {
        Cursor cursor = repository.getNotesByName(TITLE);
        assertTrue(cursor.getCount() > 0);
    }

    public void testDeleteNoteById() throws Exception {
        //try to delete fake note
        boolean status = repository.deleteNoteById("-1");
        assertFalse(status);

        Cursor cursor = repository.getNotesByName(TITLE);
        String id = "0";
        if (!cursor.isAfterLast()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        }
        assertTrue(repository.deleteNoteById(id));
    }

    public void testDeleteAllNotes() throws Exception {
        Cursor cursor = repository.getAllNotes();
        int currentCount = cursor.getCount();
        repository.deleteAllNotes();
        cursor = repository.getAllNotes();
        int countAfterDelete = cursor.getCount();
        assertTrue(currentCount != countAfterDelete);
    }


}