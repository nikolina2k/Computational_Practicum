package sample;

import javafx.scene.chart.XYChart;

public class RKutta {
    public static XYChart.Series rKutta(double x0, double y0, double x, double N) {
        double currX = x0;
        double currY = y0;
        double k1;
        double k2;
        double k3;
        double k4;
        XYChart.Series ser = new XYChart.Series();
        double n = Math.abs(x - x0) / N;

        while (currX <= x + n) {

            ser.getData().add(new XYChart.Data<>(currX, currY));

            k1 = Variant.funct(currX, currY);
            k2 = Variant.funct(currX + n * 0.5, currY + n * k1 * 0.5);
            k3 = Variant.funct(currX + n * 0.5, currY + n * k2 * 0.5);
            k4 = Variant.funct(currX + n, currY + n * k3);

            currY += n / 6 * (k1 + 2 * k2 + 2 * k3 + k4);
            currX += n;
        }
        return ser;
    }
}