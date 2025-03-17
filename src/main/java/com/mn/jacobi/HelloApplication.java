package com.mn.jacobi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MenuItem mitJacobi = new MenuItem("Metodo Jacobi");
        Menu menMetodos=new Menu("Metodos");
        mitJacobi.setOnAction(event-> new MetodoJacobi());
        menMetodos.getItems().addAll(mitJacobi);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menMetodos);
        TextArea txtArea = new TextArea("Método Jacobi: Es un método iterativo de resolucion de sistemas de ecuaciones\n" +
                "Se basa en el despeje de las variables en funcion de las demas " +
                "y actualizar sus valores despues cada iteracion hasta " +
                "que converjan en una solucion aceptable segun el error permitido.\n" +
                "Util cuando la matriz del sistema es diagonalmente dominante o simétrica definida " +
                "positiva, de otro modo puede que la solucion no converja\n" +
                "IMPORTANTE. Es importante tomar esto en cuenta para utilizar este programa");
        txtArea.setEditable(false);
        txtArea.setWrapText(true);
        txtArea.setMaxWidth(300);
        VBox vbox = new VBox(menuBar, txtArea);
        Scene escena=new Scene(vbox);
        stage.setTitle("Hello!");
        stage.setScene(escena);
        stage.show();
        stage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch();
    }
}