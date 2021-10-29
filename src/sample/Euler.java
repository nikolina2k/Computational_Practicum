package sample;

import javafx.scene.chart.XYChart;

public class Euler {
    public static XYChart.Series euler(double x0, double y0, double x, double N) {
        Double currX = x0;
        Double currY = y0;
        XYChart.Series ser = new XYChart.Series();
        Double n = Math.abs(x - x0) / N;
        ser.getData().add(new XYChart.Data<>(currX, currY));

        //euler method and graph
        while (currX <= x) {
            ser.getData().add(new XYChart.Data<>(currX, currY));

            currY += n * Variant.funct(currX, currY);
            currX += n;
        }
        return ser;
    }
}