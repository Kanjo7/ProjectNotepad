package com.projectnotepad.projectnotepad;

import javafx.event.ActionEvent;
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
    private TableView<?> tableViewTag;

    @FXML
    private TableColumn<Note, String> tagColumn= new TableColumn<>("Tag");
    @FXML
    private TableColumn<Note, String> NoteTitleColumn= new TableColumn<>("Note");

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnNewTag;
    @FXML
    private Button btnNewNote;
    @FXML
    private Button btnRemoveNote;

    @FXML
    private Button btnRemoveTag;

    @FXML
    private Button btnUpdate;

    @FXML
    private Label lblContent;

    @FXML
    private Label lblSearch;

    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtTagNote;

    @FXML
    private TextField txtContent;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtTitle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        NoteTitleColumn.setCellValueFactory(new PropertyValueFactory<Note, String>("noteTitle"));

        tableViewNote.getItems().setAll(getAllNotes());

        tableViewNote.getSelectionModel().selectedItemProperty().addListener(((observableValue, notes, t1) -> {
            txtTitle.setText(tableViewNote.getSelectionModel().getSelectedItem().getNoteTitle());
            txtContent.setText(tableViewNote.getSelectionModel().getSelectedItem().getNoteContent());
        }));
    }

    public void btnNewNote(ActionEvent e) {
        System.out.println("New Note");
    }

    public List<Note> getAllNotes() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        List<Note> notesList = null;

        try {
            transaction = entityManager.getTransaction();
            //Starta transaktionen
            transaction.begin();
            TypedQuery<Note> allNotesQuery = entityManager.createQuery("from Note", Note.class);
            notesList = allNotesQuery.getResultList();
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return notesList;
    }
}
