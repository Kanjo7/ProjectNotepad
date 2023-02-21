package com.projectnotepad.projectnotepad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import javax.persistence.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NoteController implements Initializable {

    EntityManagerFactory entityManagerFactory = HelloApplication.ENTITY_MANAGER_FACTORY;
    @FXML
    private TableView<Note> tableViewNote;

    @FXML
    private TableView<Tag> tableViewTag;
    @FXML
    private TableColumn<Tag, Integer> tagIdColumn;
    @FXML
    private TableColumn<Tag, String> tagColumn;
    @FXML
    private TableColumn<Note, Integer> noteIdColumn;
    @FXML
    private TableColumn<Note, String> noteTitleColumn;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnUpdate;

    @FXML
    private Label lblContent;

    @FXML
    private Label lblSearch;

    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtTag;

    @FXML
    private TextField txtContent;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        noteIdColumn.setCellValueFactory(new PropertyValueFactory<Note, Integer>("noteId"));
        noteTitleColumn.setCellValueFactory(new PropertyValueFactory<Note, String>("noteTitle"));
        tagIdColumn.setCellValueFactory(new PropertyValueFactory<Tag, Integer>("tagId"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<Tag, String>("tagContent"));

        tableViewNote.getItems().setAll(getAllNotes());
        tableViewTag.getItems().setAll(getAllTags());

        tableViewNote.getSelectionModel().selectedItemProperty().addListener(((observableValue, notes, t1) -> {
            txtTitle.setText(tableViewNote.getSelectionModel().getSelectedItem().getNoteTitle());
            txtContent.setText(tableViewNote.getSelectionModel().getSelectedItem().getNoteContent());
        }));

        tableViewTag.getSelectionModel().selectedItemProperty().addListener(((observableValue, tags, t2) -> {
            txtTag.setText(tableViewTag.getSelectionModel().getSelectedItem().getTagContent());

        }));
    }

    public void btnNewNote() {
        Note noteToAdd = new Note(txtTitle.getText(), txtContent.getText());
        addNote(noteToAdd);

    }

    public void btnUpdate() {
        Note noteToUpdate = tableViewNote.getSelectionModel().getSelectedItem();
        noteToUpdate.setNoteTitle(txtTitle.getText());
        noteToUpdate.setNoteContent(txtContent.getText());
        updateNote(noteToUpdate);
        createNoteTable();
    }


    public void btnRemoveNote() {
        Note noteToDelete = tableViewNote.getSelectionModel().getSelectedItem();
        deleteNote(noteToDelete.getNoteId());
    }

    public void btnRemoveTag() {
        Tag tagToDelete = tableViewTag.getSelectionModel().getSelectedItem();
        deleteTag(tagToDelete.getTagId());
    }

    public void btnNewTag() {
        Tag tagToAdd = new Tag(txtTag.getText());
        addTag(tagToAdd);
    }

    public TableView<Note> createNoteTable() {
        TableView tableViewNote = new TableView<>();

        noteIdColumn.setCellValueFactory(new PropertyValueFactory<Note, Integer>("noteId"));

        noteTitleColumn.setCellValueFactory(new PropertyValueFactory<Note, String>("noteTitle"));

        tableViewNote.getColumns().addAll(noteIdColumn, noteTitleColumn);

        tableViewNote.getItems().setAll(getNote());

        return tableViewNote;
    }


    public TableView<Tag> createTagTable() {

        TableView tableViewTag = new TableView<>();

        tagIdColumn.setCellValueFactory(new PropertyValueFactory<Tag, Integer>("tagId"));

        tagColumn.setCellValueFactory(new PropertyValueFactory<Tag, String>("tagContent"));

        tableViewTag.getColumns().addAll(tagIdColumn, tagColumn);

        tableViewTag.getItems().setAll(getNote());

        return tableViewTag;
    }












    public List<Note> getAllNotes() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        List<Note> noteList = null;

        try {
            transaction = entityManager.getTransaction();
            //Starta transaktionen
            transaction.begin();
            TypedQuery<Note> allNoteQuery = entityManager.createQuery("from Note", Note.class);
            noteList = allNoteQuery.getResultList();
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return noteList;
    }

    public List<Tag> getAllTags() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        List<Tag> tagList = null;

        try {
            transaction = entityManager.getTransaction();
            //Starta transaktionen
            transaction.begin();
            TypedQuery<Tag> allTagQuery = entityManager.createQuery("from Tag", Tag.class);
            tagList = allTagQuery.getResultList();
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return tagList;
    }

    public boolean addNote(Note theNote) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        boolean isSuccess = true;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(theNote);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            isSuccess = false;

        } finally {
            entityManager.close();
        }
        return isSuccess;
    }

    public boolean addTag(Tag theTag) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        boolean isSuccess = true;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(theTag);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            isSuccess = false;

        } finally {
            entityManager.close();
        }
        return isSuccess;
    }


    public boolean deleteNote(int theNoteId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        boolean isSuccess = true;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Note theNoteToRemove = entityManager.find(Note.class, theNoteId);

            entityManager.remove(theNoteToRemove);

            entityManager.flush();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            isSuccess = false;

        } finally {
            entityManager.close();
        }
        return isSuccess;
    }

    public boolean deleteTag(int theTagId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        boolean isSuccess = true;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Tag theTagToRemove = entityManager.find(Tag.class, theTagId);

            entityManager.remove(theTagToRemove);

            entityManager.flush();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            isSuccess = false;

        } finally {
            entityManager.close();
        }
        return isSuccess;
    }

    public boolean updateNote(Note theNote) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        boolean isSuccess = true;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Note theNoteToUpdate = entityManager.find(Note.class, theNote.getNoteId());
            theNoteToUpdate.setNoteTitle(theNote.getNoteTitle());
            theNoteToUpdate.setNoteContent(theNote.getNoteContent());
            entityManager.merge(theNoteToUpdate);
            // Commit the changes
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            isSuccess = false;

        } finally {
            entityManager.close();
        }
        return isSuccess;
    }

    public ObservableList<Note> getNote() {
        ObservableList<Note> observableNote = FXCollections.observableArrayList();
        observableNote.addAll(getAllNotes());
        return observableNote;
    }

}
