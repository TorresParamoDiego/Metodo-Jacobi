package com.mn.jacobi;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MenuItem gaussjordan = new MenuItem("Gauss Jordan");
        gaussjordan.setOnAction(event -> new Gauss_Jordan_Interfaz());
        MenuItem mitJacobi = new MenuItem("Metodo Jacobi");
        Menu menMetodos=new Menu("Metodos");
        mitJacobi.setOnAction(event-> new MetodoJacobi());
        menMetodos.getItems().addAll(gaussjordan,mitJacobi);
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
        TextArea txtBiseccion = new TextArea("Método biseccion. Es un método para encontrar raíces de ecuaciones f(x)=0f(x) = 0f(x)=0 en un intervalo [a,b][a, b][a,b] donde f(a)f(a)f(a) y f(b)f(b)f(b) tienen signos opuestos. Se basa en dividir el intervalo a la mitad y seleccionar el subintervalo donde hay un cambio de signo");
        txtBiseccion.setEditable(false);
        txtBiseccion.setWrapText(true);
        txtBiseccion.setMaxWidth(300);
        TextArea txtGaussJordan = new TextArea("Método Gauss-Jordan: Es un método para resolver sistemas de ecuaciones lineales usando eliminación de filas hasta obtener una matriz identidad. Se usa la matriz aumentada [A∣b][A | b][A∣b] y se aplican operaciones fila hasta llegar a la forma reducida por filas (rref).\n" +
                "Útil para encontrar soluciones exactas y calcular inversas de matrices");
        txtGaussJordan.setEditable(false);
        txtGaussJordan.setWrapText(true);
        txtGaussJordan.setMaxWidth(300);
        TextArea txtSecante = new TextArea("Método secante: Es un método para resolver sistemas de ecuaciones lineales usando eliminación de filas hasta obtener una matriz identidad. Se usa la matriz aumentada [A∣b][A | b][A∣b] y se aplican operaciones fila hasta llegar a la forma reducida por filas (rref).\n" +
                "Útil para encontrar soluciones exactas y calcular inversas de matrices\n" +
                "x_{k+1} = x_k - ( f(x_k) * (x_k - x_{k-1}) ) / ( f(x_k) - f(x_{k-1}) )\n");
        txtSecante.setEditable(false);
        txtSecante.setWrapText(true);
        txtSecante.setMaxWidth(300);
        TextArea txtNewton = new TextArea("Método Newton-Raphson multivariables: Método para resolver sistemas de ecuaciones no lineales\n" +
                "Se basa en el uso de derivadas parciales para calcular aproximaciones a la raiz, donde converjan 2 funciones\n" +
                "Δx = (- f1 * (∂f2/∂y) + f2 * (∂f1/∂y)) / ((∂f1/∂x) * (∂f2/∂y) - (∂f2/∂x) * (∂f1/∂y))\n" +
                "Δy = (- f2 * (∂f1/∂x) + f1 * (∂f2/∂x)) / ((∂f1/∂x) * (∂f2/∂y) - (∂f2/∂x) * (∂f1/∂y))\n" +
                "x_{k+1} = x_k + Δx\n" +
                "y_{k+1} = y_k + Δy\n");
        txtNewton.setEditable(false);
        txtNewton.setWrapText(true);
        txtNewton.setMaxWidth(300);
        HBox hBox=new HBox(txtArea,txtBiseccion,txtGaussJordan,txtSecante,txtNewton);
        Label nombres=new Label("Lemus Diaz Andres\n" +
                "Muñoz Salazar Leonardo Manuel\n" +
                "Herrera Oettinghaus Andres\n" +
                "Rodriguez Hernandez Rigoberto\n" +
                "Torres Páramo Diego Alejandro");

        VBox vbox = new VBox(menuBar,hBox,nombres);
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

class MetodoJacobi extends Stage {

    Button btnLlenaDatos;
    TableView table;
    Scene escena;
    VBox vbox;
    Sistema sis;

    public MetodoJacobi(){
        sis=new Sistema(this);
        if(sis.decidirTamano()>=2)
            sis.creaUi();
        creaUI();
        this.setTitle("Jacobi");
        this.setScene(escena);
        this.show();
    }
    void creaUI(){
        table = new TableView();
        creaDiseTabla();
        btnLlenaDatos = new Button("Llenar Datos");
        btnLlenaDatos.setOnAction(e -> {
            sis.show();
        });
        vbox = new VBox(table, btnLlenaDatos);
        escena = new Scene(vbox);
    }
    void creaDiseTabla(){
        TableColumn<Iteracion,String>[] colVariables=new TableColumn[sis.tamano*2];
        TableColumn<Iteracion,String>[] colError=new TableColumn[sis.tamano];
        int j=1;
        for(int i=0;i<sis.tamano*2-1;i++){
            colVariables[i]=new TableColumn<>("X"+(j));
            colVariables[i].setCellValueFactory(new PropertyValueFactory<>("variable"+i));
            final int index = j-1; // Necesario para la lambda

            colVariables[i].setCellValueFactory(cellData ->
                    new ReadOnlyObjectWrapper<>(cellData.getValue().getVariable(index)).asString()
            );
            colVariables[++i]=new TableColumn<>("X"+(j)+"+1");
            colVariables[i].setCellValueFactory(cellData ->
                    new ReadOnlyObjectWrapper<>(cellData.getValue().getVariableNueva(index)).asString()
            );
            colVariables[i].setCellFactory(param ->{
                TableCell<Iteracion,String> tablecell=new TableCell<Iteracion,String>(){
                    protected void updateItem(String item, boolean empty){
                        super.updateItem(item,empty);
                        if(!empty){
                            setText(item);
                            int rowIndex = getIndex();
                            int totalRows=getTableView().getItems().size();
                            if (rowIndex == totalRows - 1) {
                                setStyle("-fx-background-color: rgba(236,25,218,0.35);");
                            } else {
                                setStyle("");
                            }
                        }
                    }
                };
                return tablecell;
            });
            j++;
        }
        for(int i=0;i<sis.tamano;i++){
            final int index = i;
            colError[i]=new TableColumn<>("Error"+(i+1));
            colError[i].setCellValueFactory(cellData ->
                    new ReadOnlyObjectWrapper<>(cellData.getValue().getError(index)).asString()
            );
            colError[i].setCellFactory(param ->{
                TableCell<Iteracion,String> tablecell=new TableCell<Iteracion,String>(){
                    protected void updateItem(String item, boolean empty){
                        super.updateItem(item,empty);
                        if(!empty){
                            double itemDouble=Double.parseDouble(item);
                            setText(String.format("%.6f", itemDouble));
                        }
                    }
                };
                return tablecell;
            });
        }
        TableColumn<Iteracion,String> colNoIteracion;
        colNoIteracion=new TableColumn<>("Numero Iteracion");
        colNoIteracion.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getNoIteracion()).asObject().asString()
        );
        table.getColumns().add(colNoIteracion);
        table.getColumns().addAll(colVariables);
        table.getColumns().addAll(colError);
        table.setEditable(false);
    }


    void llenTabla(ObservableList<Iteracion> datos){
        table.setItems(datos);
        table.refresh();
        if(table.getItems().size()>100){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("WARNING");
            alert.setHeaderText("Demasiadas Iteraciones");
            alert.setContentText("Muchas iteraciones, revisa el sistema" +
                    "\nPuede que no tenga solucion ó multiples soluciones" +
                    "\nReestructura el sistema");
            alert.showAndWait();
        }
    }
}


class Sistema extends Stage {
    GridPane grid;
    Label[] arrVariables;
    TextField[][] arrCoef;
    TextField[] arrResult;
    int tamano;
    Scene escena;
    VBox vbox;
    HBox hbox;
    Button btnCalcular;
    TextField txtError;
    Metodo metodo;
    MetodoJacobi jacobi;

    public Sistema(MetodoJacobi jacobi){
        this.jacobi=jacobi;
    }

    public int decidirTamano(){
        TextInputDialog dialogo = new TextInputDialog("");
        dialogo.setTitle("Tamaño del sistema");
        dialogo.setHeaderText("Ingrese el número de ecuaciones:");
        dialogo.setContentText("N:");
        String valor = "";
        boolean bandera=true;
        while(bandera) {
            bandera=true;
            Optional<String> r =dialogo.showAndWait();
            if(r.isPresent()) {
                valor=r.get();
            }
            try {
                tamano = Integer.parseInt(valor);
                if(tamano<2||valor.isEmpty()) {
                    bandera = true;
                    throw new NumberFormatException();
                }
                else
                    bandera=false;
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Introduzca un numero mayor o igual a 2");
                alert.showAndWait();
            }
        }
        return tamano;
    }
    void creaUi(){
        grid = new GridPane();
        arrVariables = new Label[tamano];
        arrCoef = new TextField[tamano][tamano];
        arrResult = new TextField[tamano];
        txtError = new TextField();
        btnCalcular = new Button("Calcular");
        btnCalcular.setOnAction(e -> {
            metodo=new Metodo(this);
            llenTabla();
            this.close();
        });
        int j;
        for (int i = 0; i < tamano; i++) {
            j=0;
            for (int k = 0; k < tamano; k++) {
                arrVariables[k] = new Label("X" + (k+1));
                arrCoef[i][k] = new TextField();
                arrCoef[i][k].setAlignment(Pos.BASELINE_RIGHT);
                grid.add(arrCoef[i][k],j++,i);
                grid.add(arrVariables[k],j++,i);
                if(k!=tamano-1)
                    grid.add(new Label(" + "),j++,i);
            }
            grid.add(new Label(" = "),j++,i);
            arrResult[i] = new TextField();
            grid.add(arrResult[i],j++,i);
        }
        btnCalcular.setAlignment(Pos.CENTER);
        hbox = new HBox(new Label("Error permitido"),txtError);
        vbox = new VBox(grid,hbox,btnCalcular);
        escena= new Scene(vbox);
        this.setScene(escena);
        this.setTitle("Jacobi");
    }
    public void llenTabla(){
        jacobi.llenTabla(metodo.getIteraciones());

    }
}

class Metodo{
    double[] variables;
    double[] variablesNueva;
    double[] error;
    final double[][] coeficiente;
    double errorPermitido;
    final double[] result;
    ObservableList<Iteracion> iteraciones;
    int cont;
    public Metodo(Sistema sistema){
        cont=1;
        boolean bandera=true;
        iteraciones= FXCollections.observableArrayList();
        variables=new double[sistema.tamano];
        error=new double[sistema.tamano];
        variablesNueva=new double[sistema.tamano];
        coeficiente=new double[sistema.tamano][sistema.tamano];
        result=new double[sistema.tamano];
        try {
            for (int i = 0; i < sistema.tamano; i++) {
                result[i] = Double.parseDouble(sistema.arrResult[i].getText());
            }
        }catch (NumberFormatException e){
            bandera=false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error, introduzca los resultados de manera correcta");
        }
        for(int i=0;i<sistema.tamano;i++){
            error[i]=100;
            variables[i]=0;
        }
        try {
            for (int i = 0; i < sistema.tamano; i++) {
                for (int j = 0; j < sistema.tamano; j++) {

                    coeficiente[i][j] = Double.parseDouble(sistema.arrCoef[i][j].getText());

                }
            }
        }catch(NumberFormatException e){
            bandera = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error, introduzca los coeficientes de manera correcta");
            alert.showAndWait();
        }
        try {
            errorPermitido = Double.parseDouble(sistema.txtError.getText());
        } catch (NumberFormatException e) {
            bandera = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error, introduzca el error permitido de manera correcta");
        }
        if (bandera)
            creaIteraciones();
    }

    public void creaIteraciones(){
        boolean bandera=true;
        Iteracion iteracion;
        double[] variablesNuevaAux;
        double[] errorAux;
        double[] variablesAux;

        while (bandera) {
            variablesNuevaAux=new double[variablesNueva.length];
            errorAux=new double[error.length];
            variablesAux=new double[variables.length];
            iteracion = new Iteracion();
            iteracion.setNoIteracion(cont);
            for (int i = 0; i < variables.length; i++) {
                variablesAux[i]=variables[i];
            }
            iteracion.setVariables(variablesAux);
            for (int i=0;i<variables.length;i++){
                calcNuevaVariable(i);
            }
            for (int i = 0; i < variablesNueva.length; i++) {
                variablesNuevaAux[i]=variablesNueva[i];
            }
            iteracion.setVariablesNueva(variablesNuevaAux);
            cont++;
            for (int i=0;i<variables.length;i++){
                error[i]=calcError(i);
            }
            for (int i = 0; i < error.length; i++) {
                errorAux[i]=error[i];
            }
            int k=0;
            boolean banderaAux=true;
            while (error.length>k&&banderaAux){
                if(error[k]>errorPermitido)
                    banderaAux=false;
                k++;
            }
            if(banderaAux)
                bandera=false;
            iteracion.setError(errorAux);
            for(int i=0;i<variables.length;i++){
                variables[i]=variablesNueva[i];
                variablesNueva[i]=0;
            }
            iteraciones.add(iteracion);
        }
    }
    public double calcError(int i){
        return Math.abs((1-variables[i]/variablesNueva[i])*100);
    }
    public void calcNuevaVariable(int i){
        for(int j=0;j<variables.length;j++){
            if(j!=i) {
                variablesNueva[i] -= coeficiente[i][j] * variables[j];
            }
        }
        variablesNueva[i] += result[i];
        variablesNueva[i]/=coeficiente[i][i];
    }
    public ObservableList<Iteracion> getIteraciones(){
        return iteraciones;
    }
}

class Iteracion{
    private int noIteracion;
    private double[] variables;
    private double[] variablesNueva;
    private double[] error;

    public double getVariable(int valor){
        return variables[valor];
    }
    public double getError(int valor){
        return error[valor];
    }
    public int getNoIteracion() {
        return noIteracion;
    }
    public double getVariableNueva(int valor){
        return variablesNueva[valor];
    }
    public void setNoIteracion(int noIteracion) {
        this.noIteracion = noIteracion;
    }
    public void setVariables(double[] variables) {
        this.variables = variables;
    }
    public void setVariablesNueva(double[] variablesNueva) {
        this.variablesNueva = variablesNueva;
    }
    public void setError(double[] error) {
        this.error = error;
    }
}
class Gauss_Jordan_Interfaz extends Stage {
    float[][] matriz;
    int ordenDeLaMatriz;
    Scene scene;
    VBox vbox;
    GridPane grid, gridMatriz;
    Button boton;
    Label labelOrden;
    Label[] labelNumeroDeEc, labelIgual;
    TextField textFieldOrden;
    TextField[] textFieldIgual;
    TextField[][] textFieldConstante;

    public Gauss_Jordan_Interfaz() {
        UIinicial();
        this.setTitle("Gauss Jordan");
        this.setScene(scene);
        this.show();
    }

    void UIinicial(){
        labelOrden = new Label("Orden de la matriz cuadrada");
        labelOrden.setStyle("-fx-font-size: 20");
        boton = new Button("Introducir ecuación");
        boton.setStyle("-fx-font-size: 20");
        boton.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        boton.setOnAction(actionEvent -> introducirEcuacion());
        textFieldOrden = new TextField();
        grid = new GridPane();
        grid.setStyle("-fx-font-size: 20");
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.add(labelOrden, 0, 0);
        grid.add(textFieldOrden, 1, 0);
        vbox = new VBox(grid, boton);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        scene = new Scene(vbox);
    }

    private void introducirEcuacion() {
        try {
            ordenDeLaMatriz = Integer.parseInt(textFieldOrden.getText());
            if(ordenDeLaMatriz < 2)
                throw new NumberFormatException();
            matriz = new float[ordenDeLaMatriz][ordenDeLaMatriz + 1];
            close();
            UImatriz();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("La orden de la matriz es incorrecta");
            alert.showAndWait();
            textFieldOrden.setText("");
        }
    }

    void UImatriz(){
        Alert alertConstantes = new Alert(Alert.AlertType.INFORMATION);
        alertConstantes.getDialogPane().setStyle("-fx-font-size: 20; -fx-font-weight: bold");
        alertConstantes.setTitle("Gauss Jordan");
        alertConstantes.setHeaderText(null);
        alertConstantes.setContentText("Lo que debes ingresar son las contantes de las ecuaciones ;)");
        alertConstantes.setHeight(Region.USE_COMPUTED_SIZE);
        alertConstantes.setWidth(Region.USE_COMPUTED_SIZE);
        alertConstantes.showAndWait();

        boton = new Button("Gauss-Jordanear");
        boton.setStyle("-fx-font-size: 20");
        boton.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        boton.setPadding(new Insets(10, 10, 10, 10));
        boton.setOnAction(actionEvent -> gauss_jordan());
        labelNumeroDeEc = new Label[ordenDeLaMatriz];
        textFieldConstante = new TextField[ordenDeLaMatriz][ordenDeLaMatriz];
        for (int i = 0; i < ordenDeLaMatriz; i++)
            labelNumeroDeEc[i] = new Label((i+1) + " Ecuación");
        for (int i = 0; i < ordenDeLaMatriz; i++)
            for (int j = 0; j < ordenDeLaMatriz; j++)
                textFieldConstante[i][j] = new TextField();
        gridMatriz = new GridPane();
        gridMatriz.setStyle("-fx-font-size: 20");
        gridMatriz.setPadding(new Insets(10, 10, 10, 10));
        gridMatriz.setHgap(10);
        gridMatriz.setVgap(10);
        for (int i = 0; i < ordenDeLaMatriz; i++)
            gridMatriz.add(labelNumeroDeEc[i], 0, i);
        for (int i = 0; i < ordenDeLaMatriz; i++)
            for (int j = 0; j < ordenDeLaMatriz; j++)
                gridMatriz.add(textFieldConstante[i][j], j + 1, i);

        textFieldIgual = new TextField[ordenDeLaMatriz];
        labelIgual = new Label[ordenDeLaMatriz];

        for (int i = 0; i < ordenDeLaMatriz; i++) {
            textFieldIgual[i] = new TextField();
            textFieldIgual[i].setStyle("-fx-font-size: 20");
            labelIgual[i] = new Label(" = ");
            labelIgual[i].setStyle("-fx-font-size: 20");
            gridMatriz.add(labelIgual[i], ordenDeLaMatriz + 1, i);
            gridMatriz.add(textFieldIgual[i], ordenDeLaMatriz + 2, i);
        }

        vbox = new VBox(gridMatriz, boton);
        vbox.setSpacing(10);
        scene = new Scene(vbox);
        setScene(scene);
        show();
    }

    private void gauss_jordan() {
        boolean datosCompletos = true;
        for (int i = 0; i < ordenDeLaMatriz; i++) {
            for (int j = 0; j < ordenDeLaMatriz; j++)
                try {
                    matriz[i][j] = Float.parseFloat(textFieldConstante[i][j].getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Dato erróneo");
                    alert.showAndWait();
                    textFieldConstante[i][j].setText("");
                    datosCompletos = false;
                }
            try {
                matriz[i][ordenDeLaMatriz] = Float.parseFloat(textFieldIgual[i].getText());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Dato erróneo en constante");
                alert.showAndWait();
                textFieldIgual[i].setText("");
                datosCompletos = false;
            }
        }
        if (datosCompletos) {
            close();
            Gauss_Jordan gaussJordan = new Gauss_Jordan(matriz, ordenDeLaMatriz);
            gaussJordan.resolver();
        }
    }
}
class Gauss_Jordan extends Stage {
    float[][] matriz;
    int orden;
    public List<String> iteraciones;
    VBox vBox;
    Menu menu;
    MenuBar menuBar;
    MenuItem menuItemMostrarResultado, mostrariteraciones, mostrarGrafica;
    Scene scene;
    int flagResoluble;

    /*
    private WebView webView;
    private WebEngine webEngine;
    BorderPane root;
    */

    public Gauss_Jordan(float[][] matriz, int orden) {
        this.matriz = matriz;
        //this.matriz = new float[][]{{-10, 5, 15}, {42, 9, 3}};
        this.orden = orden;
        this.iteraciones = new ArrayList<>();
        UI();
    }

    private void UI() {
        close();

        /*
        webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String url = Paths.get("../geogebra.html").toUri().toString();
        webEngine.load(url);
        root = new BorderPane(webView);
        scene = new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        */

        menuItemMostrarResultado = new MenuItem("Resultado");
        menuItemMostrarResultado.setOnAction(event -> mostrarResultado());
        mostrariteraciones = new MenuItem("Iteraciones");
        mostrariteraciones.setOnAction(event -> getIteraciones());
        mostrarGrafica = new MenuItem("Grafica");
        mostrarGrafica.setOnAction(event -> mostrarGrafica());

        menu = new Menu("Menu");
        menu.getItems().addAll(menuItemMostrarResultado, mostrariteraciones);

        menuBar = new MenuBar();
        menuBar.setStyle("-fx-font-size: 20");
        menuBar.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        menuBar.getMenus().addAll(menu);

        vBox = new VBox(menuBar);
        vBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(10);
        scene = new Scene(vBox, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        setScene(scene);
        show();
    }
    void resolver() {
        flagResoluble = realizarOperaciones();
        if (flagResoluble == 1)
            flagResoluble = verificar();
    }

    int realizarOperaciones() {
        int i, j, k = 0, c;
        flagResoluble = 0;
        for (i = 0; i < orden; i++) {
            if (matriz[i][i] == 0) {
                c = 1;
                while ((i + c) < orden && matriz[i + c][i] == 0)
                    c++;
                if ((i + c) == orden) {
                    flagResoluble = 1;
                    break;
                }
                // Intercambiar la fila i con la fila i+c
                for (k = 0; k <= orden; k++) {
                    float temp = matriz[i][k];
                    matriz[i][k] = matriz[i + c][k];
                    matriz[i + c][k] = temp;
                }
            }
            for (j = 0; j < orden; j++) {
                if (i != j) {
                    float p = matriz[j][i] / matriz[i][i];
                    for (k = 0; k <= orden; k++) {
                        matriz[j][k] = matriz[j][k] - (matriz[i][k] * p);
                    }
                }
            }
            // Guardar la iteración actual después de cada pivote
            iteraciones.add("Iteración " + (i + 1) + ":\n" + guardarIteraciones());
        }
        return flagResoluble;
    }


    int verificar() {
        int i, j;
        float sum;
        flagResoluble = 3;
        for (i = 0; i < orden; i++) {
            sum = 0;
            for (j = 0; j < orden; j++)
                sum = sum + matriz[i][j];
            if (sum == matriz[i][j])
                flagResoluble = 2;
        }
        return flagResoluble;
    }

    public void mostrarResultado() {
        Stage stageResultado = new Stage();
        stageResultado.setTitle("Resultado Gauss-Jordan");

        VBox vboxResultado = new VBox();
        vboxResultado.setSpacing(10);
        vboxResultado.setPadding(new Insets(20, 20, 20, 20));
        vboxResultado.setStyle("-fx-font-size: 20");

        if (flagResoluble == 2) {
            Label label = new Label("Existen soluciones infinitas");
            label.setStyle("-fx-font-size: 20");
            vboxResultado.getChildren().add(label);
        } else if (flagResoluble == 3) {
            Label label = new Label("No existe solución");
            label.setStyle("-fx-font-size: 20");
            vboxResultado.getChildren().add(label);
        } else {
            Label encabezado = new Label("Solución única:");
            encabezado.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
            vboxResultado.getChildren().add(encabezado);

            for (int i = 0; i < orden; i++) {
                double solucion = matriz[i][orden] / matriz[i][i];
                Label label = new Label("x" + (i + 1) + " = " + String.format("%.6f", solucion));
                vboxResultado.getChildren().add(label);
            }
        }
        Scene scene = new Scene(vboxResultado);
        stageResultado.setScene(scene);
        stageResultado.show();
    }

    String guardarIteraciones() {
        String reultado = "";
        for (int i = 0; i < orden; i++) {
            for (int j = 0; j <= orden; j++)
                reultado += String.format("%.6f ", matriz[i][j]);
            reultado += "\n";
        }
        return reultado;
    }

    public void getIteraciones() {
        Stage stageIteraciones = new Stage();
        stageIteraciones.setTitle("Iteraciones");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Region.USE_COMPUTED_SIZE);
        textArea.setMaxHeight(Region.USE_COMPUTED_SIZE);
        textArea.setStyle("-fx-font-size: 20");

        StringBuilder sb = new StringBuilder();
        for (String iteracion : iteraciones)
            sb.append(iteracion).append("\n");
        textArea.setText(sb.toString());

        VBox vboxIteraciones = new VBox(textArea);
        vboxIteraciones.setSpacing(10);
        vboxIteraciones.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(vboxIteraciones);
        stageIteraciones.setScene(scene);

        stageIteraciones.show();
    }


    private void mostrarGrafica() {
    }
}

