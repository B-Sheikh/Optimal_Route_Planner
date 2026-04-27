// Loads Coimbatore city landmarks and road connections into a DeliveryGraph
public class CityData {

    public static DeliveryGraph buildCoimbatoreGraph() {

        DeliveryGraph g = new DeliveryGraph();

        // ── Delivery Points (name, GUI-x, GUI-y, lat, lng, isWarehouse) ──────────────────
        // Warehouse
        g.addPoint(new DeliveryPoint("Gandhipuram",        425, 270, 11.0183, 76.9644, true));

        // Major landmarks
        g.addPoint(new DeliveryPoint("RS Puram",           340, 290, 11.0121, 76.9456, false));
        g.addPoint(new DeliveryPoint("Peelamedu",          627, 242, 11.0263, 77.0101, false));
        g.addPoint(new DeliveryPoint("Ukkadam",            407, 350, 10.9958, 76.9604, false));
        g.addPoint(new DeliveryPoint("Singanallur",        703, 330, 11.0016, 77.0270, false));
        g.addPoint(new DeliveryPoint("Saibaba Colony",     335, 248, 11.0245, 76.9443, false));
        g.addPoint(new DeliveryPoint("Race Course",        469, 320, 11.0045, 76.9744, false));
        g.addPoint(new DeliveryPoint("Tidel Park",         734, 238, 11.0272, 77.0341, false));
        g.addPoint(new DeliveryPoint("Coimbatore Airport", 775, 228, 11.0301, 77.0434, false));
        g.addPoint(new DeliveryPoint("Town Hall",          400, 341, 10.9984, 76.9587, false));
        g.addPoint(new DeliveryPoint("Nehru Stadium",      441, 329, 11.0018, 76.9680, false));
        g.addPoint(new DeliveryPoint("Ganapathy",          470, 221, 11.0321, 76.9745, false));
        g.addPoint(new DeliveryPoint("Vadavalli",          145, 262, 11.0205, 76.9015, false));
        g.addPoint(new DeliveryPoint("Sundarapuram",       460, 497, 10.9546, 76.9723, false));
        g.addPoint(new DeliveryPoint("Ondipudur",          808, 308, 11.0076, 77.0505, false));
        
        // New suburban locations
        g.addPoint(new DeliveryPoint("Saravanampatti",     557, 60,  11.0772, 76.9942, false));
        g.addPoint(new DeliveryPoint("Kovai Pudur",        231, 545, 10.9412, 76.9208, false));
        g.addPoint(new DeliveryPoint("Thudiyalur",         328, 52,  11.0792, 76.9427, false));
        g.addPoint(new DeliveryPoint("Perur",              179, 387, 10.9856, 76.9092, false));
        g.addPoint(new DeliveryPoint("Marudhamalai",       75,  184, 11.0425, 76.8856, false));
        g.addPoint(new DeliveryPoint("Podanur",            554, 450, 10.9678, 76.9934, false));
        g.addPoint(new DeliveryPoint("Kuniyamuthur",       309, 467, 10.9631, 76.9383, false));

        // ── Roads (from, to, distance in km) ──────────────────────────────────
        g.addRoad(new Road("Gandhipuram",        "RS Puram",           2.8));
        g.addRoad(new Road("Gandhipuram",        "Town Hall",          1.5));
        g.addRoad(new Road("Gandhipuram",        "Ganapathy",          2.0));
        g.addRoad(new Road("Gandhipuram",        "Peelamedu",          5.2));
        g.addRoad(new Road("Gandhipuram",        "Race Course",        2.5));

        g.addRoad(new Road("RS Puram",           "Saibaba Colony",     2.2));
        g.addRoad(new Road("RS Puram",           "Town Hall",          2.0));
        g.addRoad(new Road("RS Puram",           "Ukkadam",            3.8));
        g.addRoad(new Road("RS Puram",           "Vadavalli",          5.5));

        g.addRoad(new Road("Saibaba Colony",     "Ganapathy",          3.2));
        g.addRoad(new Road("Saibaba Colony",     "Thudiyalur",         6.5));
        g.addRoad(new Road("Saibaba Colony",     "Vadavalli",          4.8));

        g.addRoad(new Road("Vadavalli",          "Marudhamalai",       3.5));
        g.addRoad(new Road("Vadavalli",          "Perur",              6.2));

        g.addRoad(new Road("Ganapathy",          "Saravanampatti",     5.5));
        g.addRoad(new Road("Ganapathy",          "Peelamedu",          4.5));

        g.addRoad(new Road("Saravanampatti",     "Thudiyalur",         7.0));
        g.addRoad(new Road("Saravanampatti",     "Peelamedu",          6.2));

        g.addRoad(new Road("Peelamedu",          "Tidel Park",         2.5));
        g.addRoad(new Road("Peelamedu",          "Coimbatore Airport", 3.0));
        g.addRoad(new Road("Peelamedu",          "Singanallur",        4.2));

        g.addRoad(new Road("Tidel Park",         "Singanallur",        3.0));
        g.addRoad(new Road("Singanallur",        "Ondipudur",          3.5));
        g.addRoad(new Road("Singanallur",        "Race Course",        5.0));

        g.addRoad(new Road("Race Course",        "Nehru Stadium",      1.2));
        g.addRoad(new Road("Race Course",        "Town Hall",          2.5));

        g.addRoad(new Road("Nehru Stadium",      "Ukkadam",            1.8));
        g.addRoad(new Road("Nehru Stadium",      "Town Hall",          1.5));

        g.addRoad(new Road("Town Hall",          "Ukkadam",            1.2));

        g.addRoad(new Road("Ukkadam",            "Kuniyamuthur",       3.5));
        g.addRoad(new Road("Ukkadam",            "Perur",              4.5));
        g.addRoad(new Road("Ukkadam",            "Podanur",            5.2));

        g.addRoad(new Road("Kuniyamuthur",       "Kovai Pudur",        4.0));
        g.addRoad(new Road("Kuniyamuthur",       "Sundarapuram",       4.5));

        g.addRoad(new Road("Sundarapuram",       "Podanur",            3.2));
        g.addRoad(new Road("Podanur",            "Singanallur",        5.8));
        g.addRoad(new Road("Podanur",            "Ondipudur",          7.5));

        return g;
    }
}
