module com.projectnotepad.projectnotepad {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;

    opens com.projectnotepad.projectnotepad to javafx.fxml,org.hibernate.orm.core;
    exports com.projectnotepad.projectnotepad;
}