module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jasperreports;
    requires junit;
    requires org.mockito;
    requires org.testng;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
}