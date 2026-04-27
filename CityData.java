// Loads Coimbatore city landmarks and road connections into a DeliveryGraph
public class CityData {

    public static DeliveryGraph buildCoimbatoreGraph() {

        DeliveryGraph g = new DeliveryGraph();

        // ── Delivery Points (name, GUI-x, GUI-y, isWarehouse) ──────────────────
        // Warehouse
        g.addPoint(new DeliveryPoint("Gandhipuram",        420, 280, true));

        // Major landmarks as delivery destinations
        g.addPoint(new DeliveryPoint("RS Puram",           280, 320, false));
        g.addPoint(new DeliveryPoint("Peelamedu",          600, 200, false));
        g.addPoint(new DeliveryPoint("Ukkadam",            400, 420, false));
        g.addPoint(new DeliveryPoint("Singanallur",        620, 360, false));
        g.addPoint(new DeliveryPoint("Saibaba Colony",     220, 220, false));
        g.addPoint(new DeliveryPoint("Race Course",        340, 200, false));
        g.addPoint(new DeliveryPoint("Tidel Park",         580, 300, false));
        g.addPoint(new DeliveryPoint("Coimbatore Airport", 660, 180, false));
        g.addPoint(new DeliveryPoint("Town Hall",          390, 240, false));
        g.addPoint(new DeliveryPoint("Nehru Stadium",      460, 360, false));
        g.addPoint(new DeliveryPoint("Ganapathy",          240, 300, false));
        g.addPoint(new DeliveryPoint("Vadavalli",          140, 280, false));
        g.addPoint(new DeliveryPoint("Sundarapuram",       180, 380, false));
        g.addPoint(new DeliveryPoint("Ondipudur",          580, 440, false));

        // ── Roads (from, to, distance in km) ──────────────────────────────────
        g.addRoad(new Road("Gandhipuram",        "RS Puram",           2.5));
        g.addRoad(new Road("Gandhipuram",        "Town Hall",          1.2));
        g.addRoad(new Road("Gandhipuram",        "Nehru Stadium",      2.0));
        g.addRoad(new Road("Gandhipuram",        "Peelamedu",          5.5));
        g.addRoad(new Road("Gandhipuram",        "Race Course",        2.8));

        g.addRoad(new Road("RS Puram",           "Saibaba Colony",     2.0));
        g.addRoad(new Road("RS Puram",           "Ganapathy",          3.0));
        g.addRoad(new Road("RS Puram",           "Ukkadam",            3.5));
        g.addRoad(new Road("RS Puram",           "Town Hall",          1.8));

        g.addRoad(new Road("Race Course",        "Saibaba Colony",     2.5));
        g.addRoad(new Road("Race Course",        "Town Hall",          1.5));
        g.addRoad(new Road("Race Course",        "Peelamedu",          4.0));

        g.addRoad(new Road("Saibaba Colony",     "Vadavalli",          4.5));
        g.addRoad(new Road("Saibaba Colony",     "Ganapathy",          2.0));

        g.addRoad(new Road("Ganapathy",          "Vadavalli",          3.5));
        g.addRoad(new Road("Ganapathy",          "Sundarapuram",       3.0));

        g.addRoad(new Road("Vadavalli",          "Sundarapuram",       2.8));

        g.addRoad(new Road("Town Hall",          "Peelamedu",          4.5));
        g.addRoad(new Road("Town Hall",          "Nehru Stadium",      1.5));

        g.addRoad(new Road("Peelamedu",          "Coimbatore Airport", 2.0));
        g.addRoad(new Road("Peelamedu",          "Tidel Park",         2.2));
        g.addRoad(new Road("Peelamedu",          "Singanallur",        3.5));

        g.addRoad(new Road("Tidel Park",         "Singanallur",        2.5));
        g.addRoad(new Road("Tidel Park",         "Nehru Stadium",      3.0));

        g.addRoad(new Road("Singanallur",        "Ondipudur",          2.8));
        g.addRoad(new Road("Singanallur",        "Nehru Stadium",      3.2));

        g.addRoad(new Road("Nehru Stadium",      "Ukkadam",            2.0));
        g.addRoad(new Road("Nehru Stadium",      "Ondipudur",          3.5));

        g.addRoad(new Road("Ukkadam",            "Sundarapuram",       4.0));
        g.addRoad(new Road("Ukkadam",            "Ondipudur",          3.0));

        g.addRoad(new Road("Coimbatore Airport", "Singanallur",        4.0));

        return g;
    }
}
