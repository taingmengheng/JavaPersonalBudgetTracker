import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class CustomDateTimePicker {

    private JPanel dateTimePanel;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;

    public CustomDateTimePicker() {
        dateTimePanel = new JPanel();
        dateTimePanel.setLayout(new GridLayout(2, 2, 10, 10));

        // Date spinner setup
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateTimePanel.add(new JLabel("Date:"));
        dateTimePanel.add(dateSpinner);

        // Time spinner setup
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        dateTimePanel.add(new JLabel("Time:"));
        dateTimePanel.add(timeSpinner);
    }

    public Component getComponent() {
        return dateTimePanel;
    }

    public LocalDateTime getDateTime() {
        LocalDateTime date = ((java.util.Date) dateSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime time = ((java.util.Date) timeSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        return LocalDateTime.of(date.toLocalDate(), time.toLocalTime());
    }
}
