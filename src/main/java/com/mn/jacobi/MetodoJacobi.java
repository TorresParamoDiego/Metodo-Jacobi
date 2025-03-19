//package com.mn.jacobi;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*public class MetodoJacobi extends Stage {

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
        String valor;
        boolean bandera=true;
        while(bandera) {
            valor = dialogo.showAndWait().get();
            try {
                tamano = Integer.parseInt(valor);
                if(tamano<2)
                    throw new NumberFormatException();
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
}*/