import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Main application window — Last-Mile Delivery Route Planner (Coimbatore)
public class DeliveryPlannerApp extends JFrame {

    private DeliveryGraph    graph;
    private RouteOptimizer   optimizer;
    private MapPanel         mapPanel;

    private DefaultListModel<String> selectedModel;
    private JLabel  statusLabel;
    private JLabel  distanceLabel;

    private static final String WAREHOUSE = "Gandhipuram";

    public DeliveryPlannerApp() {
        graph     = CityData.buildCoimbatoreGraph();
        optimizer = new RouteOptimizer();

        setTitle("Last-Mile Delivery Route Planner — Coimbatore");
        setSize(1100, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(30, 30, 40));

        buildUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {

        // ── Top title bar ──────────────────────────────────────────────────────
        JLabel title = new JLabel("  📦  Last-Mile Delivery Route Planner — Coimbatore", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(20, 20, 30));
        title.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        add(title, BorderLayout.NORTH);

        // ── Map panel (centre) ─────────────────────────────────────────────────
        mapPanel = new MapPanel(graph);
        mapPanel.setPreferredSize(new Dimension(750, 580));
        add(mapPanel, BorderLayout.CENTER);

        // ── Right control panel ────────────────────────────────────────────────
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBackground(new Color(40, 40, 55));
        controls.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        controls.setPreferredSize(new Dimension(300, 580));

        // -- Delivery locations list --
        JLabel pickLabel = new JLabel("Select Delivery Stops:");
        pickLabel.setForeground(Color.LIGHT_GRAY);
        pickLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        controls.add(pickLabel);
        controls.add(Box.createVerticalStrut(6));

        String[] locations = graph.getPoints().keySet()
                .stream().filter(n -> !n.equals(WAREHOUSE))
                .toArray(String[]::new);

        JList<String> locationList = new JList<>(locations);
        locationList.setBackground(new Color(55, 55, 75));
        locationList.setForeground(Color.WHITE);
        locationList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        locationList.setSelectionBackground(new Color(80, 160, 255));
        locationList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScroll = new JScrollPane(locationList);
        listScroll.setMaximumSize(new Dimension(280, 220));
        listScroll.setPreferredSize(new Dimension(280, 220));
        controls.add(listScroll);
        controls.add(Box.createVerticalStrut(8));

        JButton addBtn = styledButton("➕  Add to Route", new Color(60, 140, 80));
        addBtn.addActionListener(e -> {
            for (String sel : locationList.getSelectedValuesList()) {
                if (!selectedModel.contains(sel)) selectedModel.addElement(sel);
            }
        });
        controls.add(addBtn);
        controls.add(Box.createVerticalStrut(6));

        // -- Selected stops --
        JLabel selLabel = new JLabel("Delivery Queue:");
        selLabel.setForeground(Color.LIGHT_GRAY);
        selLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        controls.add(selLabel);
        controls.add(Box.createVerticalStrut(4));

        selectedModel = new DefaultListModel<>();
        JList<String> selectedList = new JList<>(selectedModel);
        selectedList.setBackground(new Color(55, 55, 75));
        selectedList.setForeground(new Color(255, 210, 80));
        selectedList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane selScroll = new JScrollPane(selectedList);
        selScroll.setMaximumSize(new Dimension(280, 140));
        selScroll.setPreferredSize(new Dimension(280, 140));
        controls.add(selScroll);
        controls.add(Box.createVerticalStrut(6));

        JButton removeBtn = styledButton("❌  Remove Selected", new Color(160, 60, 60));
        removeBtn.addActionListener(e -> {
            List<String> sel = selectedList.getSelectedValuesList();
            sel.forEach(selectedModel::removeElement);
        });
        controls.add(removeBtn);
        controls.add(Box.createVerticalStrut(16));

        // -- Action buttons --
        JButton planBtn = styledButton("🚚  Plan Optimal Route", new Color(50, 110, 200));
        planBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        planBtn.addActionListener(e -> planRoute());
        controls.add(planBtn);
        controls.add(Box.createVerticalStrut(8));

        JButton resetBtn = styledButton("🔄  Reset", new Color(90, 90, 110));
        resetBtn.addActionListener(e -> {
            selectedModel.clear();
            mapPanel.clearRoute();
            statusLabel.setText("Select stops and click Plan Route.");
            distanceLabel.setText("Total Distance: —");
        });
        controls.add(resetBtn);
        controls.add(Box.createVerticalStrut(20));

        // -- Status --
        statusLabel = new JLabel("<html>Select delivery stops<br>and click Plan Optimal Route.</html>");
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        controls.add(statusLabel);
        controls.add(Box.createVerticalStrut(8));

        distanceLabel = new JLabel("Total Distance: —");
        distanceLabel.setForeground(new Color(100, 220, 130));
        distanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        controls.add(distanceLabel);

        add(controls, BorderLayout.EAST);
    }

    private void planRoute() {
        if (selectedModel.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one delivery stop.",
                "No Stops Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> deliveries = new ArrayList<>();
        for (int i = 0; i < selectedModel.size(); i++) {
            deliveries.add(selectedModel.getElementAt(i));
        }

        List<String> route = optimizer.planMultiStopRoute(graph, WAREHOUSE, deliveries);
        double total = optimizer.calculateTotalDistance(graph, route);

        mapPanel.setRoute(route, deliveries);

        // Build readable route string
        StringBuilder sb = new StringBuilder("<html><b>Route:</b><br>");
        for (int i = 0; i < route.size(); i++) {
            String stop = route.get(i);
            if (stop.equals(WAREHOUSE))
                sb.append("<font color='#66CCFF'>").append(stop).append("</font>");
            else if (deliveries.contains(stop))
                sb.append("<font color='#FFCC44'>⬤ ").append(stop).append("</font>");
            else
                sb.append("<font color='#AAAAAA'>").append(stop).append("</font>");
            if (i < route.size() - 1) sb.append(" → ");
        }
        sb.append("</html>");
        statusLabel.setText(sb.toString());
        distanceLabel.setText(String.format("Total Distance: %.1f km", total));
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(280, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeliveryPlannerApp::new);
    }
}
