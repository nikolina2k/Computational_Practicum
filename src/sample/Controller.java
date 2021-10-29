package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Controller {
    public TextField X;
    public TextField x0;
    public TextField N;
    public TextField y0;
    public TextField Nmaximum;
    public TextField Nminimum;

    public CheckBox euler;
    public CheckBox improvedEuler;
    public CheckBox rungeKutta;
    public CheckBox exact;


    @FXML
    private LineChart<Number, Number> functions;
    @FXML
    private LineChart<Number, Number> errors;
    @FXML
    private LineChart<Number, Number> maximumError;

    @FXML
    private NumberAxis functionsXAxis;
    @FXML
    private NumberAxis functionsYAxis;

    @FXML
    private NumberAxis errorsXAxis;
    @FXML
    private NumberAxis errorsYAxis;

    @FXML
    private NumberAxis maximumErrorXAxis;
    @FXML
    private NumberAxis maximumErrorYAxis;

    public XYChart.Series<Number, Number> ser1 = new XYChart.Series<>();
    public XYChart.Series<Number, Number> ser2 = new XYChart.Series<>();
    public XYChart.Series<Number, Number> ser3 = new XYChart.Series<>();
    public XYChart.Series<Number, Number> ser4 = new XYChart.Series<>();

    //ES= ErrorSeries
    private XYChart.Series<Number, Number> eulerES = new XYChart.Series<>();
    private XYChart.Series<Number, Number> improvedEulerES = new XYChart.Series<>();
    private XYChart.Series<Number, Number> rungeKuttaES = new XYChart.Series<>();

    private XYChart.Series<Number, Number> eulerMaxES = new XYChart.Series<>();
    private XYChart.Series<Number, Number> improvedEulerMaxES = new XYChart.Series<>();
    private XYChart.Series<Number, Number> rungeKuttaMaxES = new XYChart.Series<>();

    public Controller() {
    }

    @FXML
    public void initialize() {
        setNames();

        //---------------------------------

        functions.setAnimated(false);

        functionsXAxis.setAutoRanging(false);
        functionsXAxis.setLowerBound(0);
        functionsXAxis.setUpperBound(5);
        functionsXAxis.setTickUnit(0.5);

        functionsYAxis.setAutoRanging(false);
        functionsYAxis.setLowerBound(-10);
        functionsYAxis.setUpperBound(5);
        functionsYAxis.setTickUnit(100.0);

        //---------------------------------

        errors.setAnimated(false);

        errorsXAxis.setAutoRanging(false);
        errorsXAxis.setLowerBound(0);
        errorsXAxis.setUpperBound(2.5);
        errorsXAxis.setTickUnit(0.25);

        errorsYAxis.setAutoRanging(false);
        errorsYAxis.setLowerBound(-2.5);
        errorsYAxis.setUpperBound(2.5);
        errorsYAxis.setTickUnit(10.0);

        //---------------------------------

        maximumError.setAnimated(true);

        maximumErrorXAxis.setAutoRanging(true);
        maximumErrorXAxis.setLowerBound(15);
        maximumErrorXAxis.setUpperBound(100);
        maximumErrorXAxis.setTickUnit(10);

        maximumErrorYAxis.setAutoRanging(true);
        maximumErrorYAxis.setLowerBound(0);
        maximumErrorYAxis.setUpperBound(0.00000001);
        maximumErrorYAxis.setTickUnit(0.000000001);


        maximumError.getData().add(eulerMaxES);
        maximumError.getData().add(improvedEulerMaxES);
        maximumError.getData().add(rungeKuttaMaxES);

        //---------------------------------

        calc();
        calcMaximumError();
    }

    public void setNames() {
        ser1.setName("Euler");
        ser2.setName("Improved Euler");
        ser3.setName("Runge Kutta");
        ser4.setName("Exact");
        functions.setTitle("4/x^2 - y/x - y^2");
        eulerES.setName("Euler");
        eulerMaxES.setName("Euler");
        improvedEulerES.setName("Improved Euler");
        improvedEulerMaxES.setName("Improved Euler");
        rungeKuttaES.setName("Runge Kutta");
        rungeKuttaMaxES.setName("Runge Kutta");
    }

    @FXML
    public void plotEuler() {
        plotGraph(euler.isSelected(), ser1, functions);
        plotGraph(euler.isSelected(), eulerES, errors);
    }

    @FXML
    public void plotImprovedEuler() {
        plotGraph(improvedEuler.isSelected(), ser2, functions);
        plotGraph(improvedEuler.isSelected(), improvedEulerES, errors);
    }

    @FXML
    public void plotRungeKutta() {
        plotGraph(rungeKutta.isSelected(), ser3, functions);
        plotGraph(rungeKutta.isSelected(), rungeKuttaES, errors);
    }

    @FXML
    public void plotExact() {
        plotGraph(exact.isSelected(), ser4, functions);
    }

    @FXML
    public void recalc() {
        calc();
        calcMaximumError();
    }

    @FXML
    public void recalcMaximumError() {
        calcMaximumError();
    }

    private void plotGraph(Boolean checkBV,
                           XYChart.Series<Number, Number> series,
                           LineChart<Number, Number> chart) {
        if (checkBV) {
            chart.getData().add(series);
        } else {
            chart.getData().remove(series);
        }
    }

    private void calc() {
        double x0 = Double.parseDouble(this.x0.getText());
        double X = Double.parseDouble(this.X.getText());
        double y0 = Double.parseDouble(this.y0.getText());
        int N = Integer.parseInt(this.N.getText());
        double c = ((2+y0*x0)/(4*(y0*x0-2)))/(x0*x0*x0*x0*x0);

        ser1.getData().clear();
        ser2.getData().clear();
        ser3.getData().clear();
        ser4.getData().clear();

        ser1.getData().addAll(Euler.euler(x0, y0, X, N).getData());
        ser2.getData().addAll(IEuler.ieuler(x0, y0, X, N).getData());
        ser3.getData().addAll(RKutta.rKutta(x0, y0, X, N).getData());
        ser4.getData().addAll(Exact.exact(x0, y0, X, N, c).getData());

        calcError(ser1, eulerES);
        calcError(ser2, improvedEulerES);
        calcError(ser3, rungeKuttaES);
    }

    //calculation of error and adding the error
    public void calcError(XYChart.Series<Number, Number> generatedSeries,
                          XYChart.Series<Number, Number> errorSeries) {
        errorSeries.getData().clear();
        int elem = ser4.getData().size() > generatedSeries.getData().size() ?
                generatedSeries.getData().size() : ser4.getData().size();

        for (int i = 0; i < elem; i++) {

            double tempo = Math.abs((double) ser4.getData().get(i).getYValue()
                    - (double) generatedSeries.getData().get(i).getYValue());


            errorSeries.getData().add(new XYChart.Data<>(ser4.getData().get(i).getXValue(), tempo));
        }

    }

    public void calcMaximumError() {
        double tempo;
        double x0 = Double.parseDouble(this.x0.getText());
        double X = Double.parseDouble(this.X.getText());
        double y0 = Double.parseDouble(this.y0.getText());
        int Nminimum = Integer.parseInt(this.Nminimum.getText());
        int Nmaximum = Integer.parseInt(this.Nmaximum.getText());
        double c = ((2+y0*x0)/(4*(y0*x0-2)))/(x0*x0*x0*x0*x0);

        eulerMaxES.getData().clear();
        improvedEulerMaxES.getData().clear();
        rungeKuttaMaxES.getData().clear();

        double orgVal;

        for (int N = Nminimum; N <= Nmaximum; N++) {
            orgVal = Exact.function(X, c);
            tempo = Math.abs(orgVal - ((XYChart.Series<Number, Number>) Euler.euler(x0, y0, X, N)).getData().get(N).getYValue().doubleValue());
            eulerMaxES.getData().add(new XYChart.Data<>(N, tempo));
            tempo = Math.abs(orgVal - ((XYChart.Series<Number, Number>) IEuler.ieuler(x0, y0, X, N)).getData().get(N).getYValue().doubleValue());
            improvedEulerMaxES.getData().add(new XYChart.Data<>(N, tempo));
            tempo = Math.abs(orgVal - ((XYChart.Series<Number, Number>) RKutta.rKutta(x0, y0, X, N)).getData().get(N).getYValue().doubleValue());
            rungeKuttaMaxES.getData().add(new XYChart.Data<>(N, tempo));
        }
    }
}