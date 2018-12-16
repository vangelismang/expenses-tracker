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

        List<BigDecimal> expenses = service.getTotalAmountPerMonth();
        List<BigDecimal> salary = salaryService.getTotalAmountPerMonth();
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
        seriesA.setData(expenses.get(0), expenses.get(1), expenses.get(2), expenses.get(3), expenses.get(4), expenses.get(5),
                expenses.get(6), expenses.get(7), expenses.get(8), expenses.get(9), expenses.get(10), expenses.get(11));
        seriesB.setData(salary.get(0), salary.get(1), salary.get(2), salary.get(3), salary.get(4), salary.get(5),
                salary.get(6), salary.get(7), salary.get(8), salary.get(9), salary.get(10), salary.get(11));
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
