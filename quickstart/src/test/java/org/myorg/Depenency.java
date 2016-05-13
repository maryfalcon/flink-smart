package org.myorg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * User: NotePad.by
 * Date: 5/13/2016.
 */
public class Depenency  extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Size (kB)");
        yAxis.setLabel("Time (ms)");
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();

        List<Double> start = new ArrayList<Double>();
        series.getData().add(new XYChart.Data(1000, 8));
        series.getData().add(new XYChart.Data(2000, 11));
        series.getData().add(new XYChart.Data(3000, 13));
        series.getData().add(new XYChart.Data(4000, 16));
        series.getData().add(new XYChart.Data(5000, 18));
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}