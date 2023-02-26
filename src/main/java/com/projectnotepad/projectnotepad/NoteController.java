package com.projectnotepad.projectnotepad;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NoteController implements Initializable {

    EntityManagerFactory entityManagerFactory = HelloApplication.ENTITY_MANAGER_FACTORY;

    /*-----------------------------------------------------------------------------------------------------------
     * Variablar
     ------------------------------------------------------------------------------------------------------------*/

    // skapar en tableview för noter
    @FXML
    private TableView<Note> tableViewNote;

    // skapar en tableview för taggar
    @FXML
    private TableView<Tag> tableViewTag;

    //Skapar en ID kolumn för taggar
    @FXML
    private TableColumn<Tag, Integer> tagIdColumn;

    //skapar en kolumn för taggar
    @FXML
    private TableColumn<Tag, String> tagColumn;

    //skapar en ID kolumn för noter
    @FXML
    private TableColumn<Note, Integer> noteIdColumn;

    //skapar en kolumn för rubrik
    @FXML
    private TableColumn<Note, String> noteTitleColumn;

    //Textfält Tagg
    @FXML
    private TextField txtTag;

    //Textfält Innehåll
    @FXML
    private TextField txtContent;

    //Textfält Rubrik
    @FXML
    private TextField txtTitle;

    /*-----------------------------------------------------------------------------------------------------------
     * Metoder
     ------------------------------------------------------------------------------------------------------------*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //Skapar celler i kolumner
        noteIdColumn.setCellValueFactory(new PropertyValueFactory<Note, Integer>("noteId"));
        noteTitleColumn.setCellValueFactory(new PropertyValueFactory<Note, String>("noteTitle"));
        tagIdColumn.setCellValueFactory(new PropertyValueFactory<Tag, Integer>("tagId"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<Tag, String>("tagContent"));

        //Hämtar tabeller från databasen
        tableViewNote.getItems().setAll(getAllNotes());
        tableViewTag.getItems().setAll(getAllTags());

        //Adds a ChangeListener which will be notified whenever the value of the ObservableValue changes.
        tableViewNote.getSelectionModel().selectedItemProperty().addListener(((observableValue, notes, t1) -> {
            txtTitle.setText(tableViewNote.getSelectionModel().getSelectedItem().getNoteTitle());
            txtContent.setText(tableViewNote.getSelectionModel().getSelectedItem().getNoteContent());
        }));

        tableViewTag.getSelectionModel().selectedItemProperty().addListener(((observableValue, tags, t1) -> {
            txtTag.setText(tableViewTag.getSelectionModel().getSelectedItem().getTagContent());

        }));
    }

    //Knappen ny anteckning
    public void btnNewNote() {
        Note noteToAdd = new Note(txtTitle.getText(), txtContent.getText());
        addNote(noteToAdd);

        updateNoteTableView();

        txtTitle.clear();
        txtContent.clear();
    }

    //knappen uppdatera
    public void btnUpdate() {
        Note noteToUpdate = tableViewNote.getSelectionModel().getSelectedItem();
        noteToUpdate.setNoteTitle(txtTitle.getText());
        noteToUpdate.setNoteContent(txtContent.getText());
        updateNote(noteToUpdate);

        updateNoteTableView();

        txtTitle.clear();
        txtContent.clear();
    }

    //knappen radera
    public void btnRemoveNote() {
        Note noteToDelete = tableViewNote.getSelectionModel().getSelectedItem();
        deleteNote(noteToDelete.getNoteId());

        updateNoteTableView();
    }

    //Knappen radera tagg
    public void btnRemoveTag() {
        Tag tagToDelete = tableViewTag.getSelectionModel().getSelectedItem();
        deleteTag(tagToDelete.getTagId());

        updateTagTableview();
    }

    //knappen ny tagg
    public void btnNewTag() {
        Tag tagToAdd = new Tag(txtTag.getText());
        addTag(tagToAdd);

        updateTagTableview();

        txtTag.clear();
    }

    //uppdatera tableview note efter ändring
    public void updateNoteTableView() {
        tableViewNote.getItems().setAll(getAllNotes());
    }

    //uppdatera tableview tag efter ändring
    public void updateTagTableview() {
        tableViewTag.getItems().setAll(getAllTags());
    }

    //Databas relaterade metoder för att skapa,läsa,uppdatera och ta bort

    //Hämtar listan på alla noter från databasen
    public List<Note> getAllNotes() {
        //skapar ett objekt och ropar på createEntityManager. Lagrar resultatet i entityManager
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        List<Note> noteList = null;
        try {
            transaction = entityManager.getTransaction();
            //Starta transaktionen
            transaction.begin();
            //skickar en förfrågan till databasen för att hämta noter
            TypedQuery<Note> allNoteQuery = entityManager.createQuery("from Note", Note.class);
            noteList = allNoteQuery.getResultList();
            //skickar iväg
            transaction.commit();
            //fångar exception
        } catch (Exception e) {
            if (transaction != null) {
                //rullar tillbaka om något går fel
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return noteList;
    }

    //Hämta alla taggar
    public List<Tag> getAllTags() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        List<Tag> tagList = null;

        try {
            transaction = entityManager.getTransaction();
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

    //Lägger till noter
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

    //Lägger till taggar
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

    //radera noter
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

    //radera taggar
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

    //updaterar noter
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
}
