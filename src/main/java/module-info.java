module com.mn.jacobi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens com.mn.jacobi to javafx.fxml, javafx.base;
    exports com.mn.jacobi;
}