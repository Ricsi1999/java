/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 *
 * @author riharada
 */
public class GanttChartGUI extends JPanel {

    private static TaskSeries process;
    private static TaskSeries change;
    private static TaskSeries timeLine;
    private TaskSeriesCollection dataset;
    private ChartPanel panel;

    public GanttChartGUI() {
        super();
        process = new TaskSeries("Process");
        change = new TaskSeries("Change");
        timeLine = new TaskSeries("Current Date");
        dataset = new TaskSeriesCollection();
    }
    
    public JPanel getPanel() {
        
        dataset.add(change);
        dataset.add(process);
        dataset.add(timeLine);
        IntervalCategoryDataset _dataset = dataset;
        JFreeChart chart = ChartFactory.createGanttChart(
                "Production Plan", // Chart title  
                "Order ID", // X-Axis Label  
                "Timeline", // Y-Axis Label  
                _dataset);

        panel = new ChartPanel(chart);
        return panel;
    }
    
    public void newProcess(LocalDateTime start, LocalDateTime end, long OrderID) {
        process.add(new Task(String.valueOf(OrderID),
            Date.from(start.atZone(ZoneId.systemDefault()).toInstant()),
            Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
        ));
    }
    
    public void newChange(LocalDateTime start, LocalDateTime end, long OrderID) {
        change.add(new Task(String.valueOf(OrderID),
            Date.from(start.atZone(ZoneId.systemDefault()).toInstant()),
            Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
        ));
    }
    
    public void newTimeline(LocalDateTime start, LocalDateTime end) {
        timeLine.add(new Task("Date",
            Date.from(start.atZone(ZoneId.systemDefault()).toInstant()),
            Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
        ));
    }
    
    public void reset() {
        process.removeAll();
        change.removeAll();
        timeLine.removeAll();
        dataset.removeAll();
    }
}
