import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

// Custom panel that draws the Coimbatore delivery network map
public class MapPanel extends JPanel {

    private DeliveryGraph   graph;
    private List<String>    route;        // full path (including intermediate nodes)
    private List<String>    deliveries;   // actual delivery stops

    private static final Color BG_COLOR         = new Color(22, 28, 45);
    private static final Color ROAD_COLOR        = new Color(50, 60, 85);
    private static final Color ROUTE_COLOR       = new Color(0, 210, 255); // Cyan for route
    private static final Color NODE_DEFAULT      = new Color(100, 110, 140);
    private static final Color NODE_WAREHOUSE    = new Color(255, 80, 80); // Red for warehouse
    private static final Color NODE_DELIVERY     = new Color(255, 200, 50);
    private static final Color NODE_ON_ROUTE     = new Color(80, 255, 180);
    private static final Color LABEL_COLOR       = Color.WHITE;
    private static final Color WEIGHT_COLOR      = new Color(130, 145, 170);

    public MapPanel(DeliveryGraph graph) {
        this.graph     = graph;
        this.route     = new ArrayList<>();
        this.deliveries = new ArrayList<>();
        setBackground(BG_COLOR);
        setToolTipText(""); // Enable tooltips
    }

    public void setRoute(List<String> route, List<String> deliveries) {
        this.route      = route;
        this.deliveries = deliveries;
        repaint();
    }

    public void clearRoute() {
        this.route      = new ArrayList<>();
        this.deliveries = new ArrayList<>();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawAllRoads(g2);
        if (!route.isEmpty()) drawRoute(g2);
        drawNodes(g2);
        drawLegend(g2);
    }

    @Override
    public String getToolTipText(java.awt.event.MouseEvent e) {
        for (DeliveryPoint dp : graph.getPoints().values()) {
            if (Math.hypot(e.getX() - dp.getX(), e.getY() - dp.getY()) < 18) {
                return String.format("<html><b>%s</b><br>Lat: %.4f, Lng: %.4f%s</html>", 
                    dp.getName(), dp.getLat(), dp.getLng(), 
                    dp.isWarehouse() ? " (Warehouse)" : "");
            }
        }
        return null;
    }

    private void drawAllRoads(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(ROAD_COLOR);

        Map<String, Map<String, Double>> adj = graph.getAdjacency();
        Set<String> drawn = new HashSet<>();

        for (String from : adj.keySet()) {
            for (Map.Entry<String, Double> entry : adj.get(from).entrySet()) {
                String to  = entry.getKey();
                String key = from.compareTo(to) < 0 ? from + "|" + to : to + "|" + from;
                if (drawn.contains(key)) continue;
                drawn.add(key);

                DeliveryPoint pf = graph.getPoint(from);
                DeliveryPoint pt = graph.getPoint(to);
                g2.drawLine(pf.getX(), pf.getY(), pt.getX(), pt.getY());

                // Draw distance label at midpoint
                int mx = (pf.getX() + pt.getX()) / 2;
                int my = (pf.getY() + pt.getY()) / 2;
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.setColor(WEIGHT_COLOR);
                g2.drawString(entry.getValue() + "km", mx + 3, my - 3);
                g2.setColor(ROAD_COLOR);
            }
        }
    }

    private void drawRoute(Graphics2D g2) {
        // Draw thick orange route line
        g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(ROUTE_COLOR);

        for (int i = 0; i < route.size() - 1; i++) {
            DeliveryPoint p1 = graph.getPoint(route.get(i));
            DeliveryPoint p2 = graph.getPoint(route.get(i + 1));
            if (p1 != null && p2 != null) {
                g2.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());

                // Draw arrowhead
                drawArrow(g2, p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }
    }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double angle  = Math.atan2(y2 - y1, x2 - x1);
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;
        int arrowSize = 10;

        int ax1 = (int) (mx - arrowSize * Math.cos(angle - Math.PI / 6));
        int ay1 = (int) (my - arrowSize * Math.sin(angle - Math.PI / 6));
        int ax2 = (int) (mx - arrowSize * Math.cos(angle + Math.PI / 6));
        int ay2 = (int) (my - arrowSize * Math.sin(angle + Math.PI / 6));

        int[] xs = { mx, ax1, ax2 };
        int[] ys = { my, ay1, ay2 };
        g2.fillPolygon(xs, ys, 3);
    }

    private void drawNodes(Graphics2D g2) {
        Set<String> routeSet = new HashSet<>(route);

        for (DeliveryPoint dp : graph.getPoints().values()) {
            int x = dp.getX(), y = dp.getY();
            int r = 18;

            Color fill;
            if (dp.isWarehouse())            fill = NODE_WAREHOUSE;
            else if (deliveries.contains(dp.getName())) fill = NODE_DELIVERY;
            else if (routeSet.contains(dp.getName()))   fill = NODE_ON_ROUTE;
            else                             fill = NODE_DEFAULT;

            // Shadow
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillOval(x - r + 2, y - r + 2, r * 2, r * 2);

            // Node circle
            g2.setColor(fill);
            g2.fillOval(x - r, y - r, r * 2, r * 2);

            // Border
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(x - r, y - r, r * 2, r * 2);

            // Label
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            String label = dp.isWarehouse() ? "🏭" : dp.getName().split(" ")[0];
            int lw = fm.stringWidth(label);

            g2.setColor(Color.BLACK);
            g2.drawString(label, x - lw / 2 + 1, y + 4 + 1);
            g2.setColor(LABEL_COLOR);
            g2.drawString(label, x - lw / 2, y + 4);

            // Full name below node
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            fm = g2.getFontMetrics();
            int nw = fm.stringWidth(dp.getName());
            g2.setColor(new Color(200, 210, 230));
            g2.drawString(dp.getName(), x - nw / 2, y + r + 13);
        }
    }

    private void drawLegend(Graphics2D g2) {
        int lx = 14, ly = 14;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(lx - 6, ly - 6, 180, 100, 10, 10);

        String[][] items = {
            {"Warehouse",       String.valueOf(NODE_WAREHOUSE.getRGB())},
            {"Delivery Stop",   String.valueOf(NODE_DELIVERY.getRGB())},
            {"Route Node",      String.valueOf(NODE_ON_ROUTE.getRGB())},
            {"Other Location",  String.valueOf(NODE_DEFAULT.getRGB())}
        };

        Color[] colors = { NODE_WAREHOUSE, NODE_DELIVERY, NODE_ON_ROUTE, NODE_DEFAULT };

        for (int i = 0; i < items.length; i++) {
            g2.setColor(colors[i]);
            g2.fillOval(lx, ly + i * 22, 12, 12);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString(items[i][0], lx + 18, ly + i * 22 + 11);
        }
    }
}
