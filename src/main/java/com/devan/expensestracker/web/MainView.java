package com.devan.expensestracker.web;

import com.devan.expensestracker.service.ExpensesService;
import com.devan.expensestracker.service.SalaryService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Route("my-expenses")
public class MainView extends VerticalLayout {

    private Button button;

    public MainView(ExpensesService service, SalaryService salaryService) throws ParseException {

        List<Number> expenses = service.getTotalAmountPerMonth();
        List<Number> salary = salaryService.getTotalAmountPerMonth();
        Chart chart = new Chart(ChartType.COLUMN);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("My expenses");
        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setMarker(new Marker(true));
        Tooltip tooltip = new Tooltip();
        tooltip.setHeaderFormat("{series.value}");

        conf.setTooltip(tooltip);
        conf.setPlotOptions(plotOptions);
        // Series configuration
        ListSeries seriesA = new ListSeries("Monthly expenses in euro");
        ListSeries seriesB = new ListSeries("Monthly salary in euro");
        seriesA.setData(expenses);
        seriesB.setData(salary);
        conf.addSeries(seriesA);
        conf.addSeries(seriesB);
        // Axis configuration
        XAxis xaxis = new XAxis();
        xaxis.setCategories("January", "February",   "March",
                "April",    "May", "June",
                "July",  "August", "September", "October", "November", "December");
        xaxis.setTitle("Month");
        conf.addxAxis(xaxis);
        // Set the Y axis title
        YAxis yaxis = new YAxis();
        yaxis.setTitle("Euro");
        yaxis.getLabels().setFormatter(
                "function() {return this.value + \' €\';}");
        yaxis.getLabels().setStep(2);
        conf.addyAxis(yaxis);

        add(chart);

    }

}
