package ui;

import model.StudentResult;
import service.ResultCalculator;
import ui.theme.H2ATheme;
import util.ApplicationConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class results and merit standings panel.
 * Styled with the luxury dark aesthetic of https://www.h2a.lu/ .
 */
public class ResultsPanel extends JPanel {

    private final ResultCalculator resultCalculator;

    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private List<StudentResult> allResults;

    public ResultsPanel(ResultCalculator resultCalculator) {
        this.resultCalculator = resultCalculator;

        setLayout(new BorderLayout());
        setBackground(H2ATheme.BG_DARK);

        buildUI();
        refresh();
    }

    private void buildUI() {
        // Header
        JPanel headerPanel = H2ATheme.createHeader("Class Results & Merit List",
                "Official ranked merit standings, overall percentages, and certified performance levels");
        add(headerPanel, BorderLayout.NORTH);

        // Main Card Container
        JPanel contentPanel = new JPanel(new BorderLayout(0, 16));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(22, 28, 22, 28));

        JPanel card = H2ATheme.createCardPanel(18);
        card.setLayout(new BorderLayout(0, 14));

        // Toolbar: Search + Filter
        JPanel toolbarPanel = new JPanel(new BorderLayout(14, 0));
        toolbarPanel.setOpaque(false);

        // Search Input
        searchField = H2ATheme.createTextField();
        searchField.setToolTipText("Filter by Student ID, Roll Number, or Name");
        toolbarPanel.add(searchField, BorderLayout.CENTER);

        // Filter combo + Action buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionsPanel.setOpaque(false);

        JLabel filterLbl = new JLabel("Status:");
        filterLbl.setFont(H2ATheme.FONT_BODY_BOLD);
        filterLbl.setForeground(H2ATheme.TEXT_SECONDARY);
        actionsPanel.add(filterLbl);

        filterCombo = new JComboBox<>(new String[]{"All Results", "PASS", "FAIL", "INCOMPLETE"});
        filterCombo.setFont(H2ATheme.FONT_BODY);
        filterCombo.setBackground(H2ATheme.BG_INPUT);
        filterCombo.setForeground(H2ATheme.TEXT_PRIMARY);
        filterCombo.setPreferredSize(new Dimension(140, 36));
        filterCombo.setBorder(BorderFactory.createLineBorder(H2ATheme.BORDER_INPUT, 1));
        filterCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? H2ATheme.GRADIENT_PURPLE : H2ATheme.BG_INPUT);
                setForeground(H2ATheme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(6, 10, 6, 10));
                return this;
            }
        });
        actionsPanel.add(filterCombo);

        JButton searchBtn = H2ATheme.createGradientButton("Search");
        searchBtn.setPreferredSize(new Dimension(95, 36));
        actionsPanel.add(searchBtn);

        JButton refreshBtn = H2ATheme.createOutlineButton("Show All");
        refreshBtn.setPreferredSize(new Dimension(95, 36));
        actionsPanel.add(refreshBtn);

        toolbarPanel.add(actionsPanel, BorderLayout.EAST);
        card.add(toolbarPanel, BorderLayout.NORTH);

        // Results Table
        String[] columns = {"Rank", "Student ID", "Roll Number", "Student Name",
                "Marks Obtained", "Max Marks", "Percentage", "Grade", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        H2ATheme.stylizeTable(resultsTable);

        // Column widths
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(60);   // Rank
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(90);   // ID
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Roll
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(180);  // Name
        resultsTable.getColumnModel().getColumn(4).setPreferredWidth(95);   // Marks
        resultsTable.getColumnModel().getColumn(5).setPreferredWidth(85);   // Max
        resultsTable.getColumnModel().getColumn(6).setPreferredWidth(95);   // %
        resultsTable.getColumnModel().getColumn(7).setPreferredWidth(75);   // Grade
        resultsTable.getColumnModel().getColumn(8).setPreferredWidth(100);  // Status

        JScrollPane scrollPane = H2ATheme.createScrollPane(resultsTable);
        card.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(card, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Wire events
        searchBtn.addActionListener(e -> applyFilterAndSearch());
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            filterCombo.setSelectedIndex(0);
            refresh();
        });
        filterCombo.addActionListener(e -> applyFilterAndSearch());
        searchField.addActionListener(e -> applyFilterAndSearch());
    }

    private void applyFilterAndSearch() {
        if (allResults == null) return;

        String filter = (String) filterCombo.getSelectedItem();
        String query = searchField.getText().trim().toLowerCase();

        List<StudentResult> filtered = allResults;

        // Apply status filter
        if (filter != null && !filter.equals("All Results")) {
            filtered = filtered.stream()
                    .filter(r -> r.getResultStatus().equals(filter))
                    .collect(Collectors.toList());
        }

        // Apply search query
        if (!query.isEmpty()) {
            filtered = filtered.stream()
                    .filter(r -> r.getStudent().getStudentId().toLowerCase().contains(query)
                            || r.getStudent().getRollNumber().toLowerCase().contains(query)
                            || r.getStudent().getFullName().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        populateTable(filtered);
    }

    private void populateTable(List<StudentResult> results) {
        tableModel.setRowCount(0);

        for (StudentResult r : results) {
            String rank = r.getRank() > 0 ? ("#" + r.getRank()) : "-";
            String percentage = r.getResultStatus().equals(ApplicationConstants.RESULT_INCOMPLETE)
                    ? "-"
                    : String.format("%.2f%%", r.getPercentage());
            String grade = r.getGrade();
            if (grade == null || grade.isEmpty()) {
                grade = "-";
            }

            tableModel.addRow(new Object[]{
                    rank,
                    r.getStudent().getStudentId(),
                    r.getStudent().getRollNumber(),
                    r.getStudent().getFullName(),
                    String.format("%.0f", r.getTotalMarks()),
                    String.format("%.0f", r.getMaxPossibleMarks()),
                    percentage,
                    grade,
                    r.getResultStatus()
            });
        }
    }

    public void refresh() {
        allResults = resultCalculator.calculateAllResults();
        populateTable(allResults);
    }
}
