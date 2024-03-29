package mealplanner;


import java.sql.*;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/meals_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1111";

    private static Connection postgresConnection;

    //dumb hecking hyperskill not allowed to use serial
    //I'll show you.
    //I have the power of god and anime on my side
    public static int hsIsRWorded(String tableName) throws SQLException {
        PreparedStatement ps2 = postgresConnection.prepareStatement("SELECT nextval(?)");
        ps2.setString(1, tableName.concat("_seq"));
        ResultSet rs = ps2.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public static void initDB() throws SQLException {
        postgresConnection = getCon();

        PreparedStatement ps = postgresConnection.prepareStatement("CREATE TABLE IF NOT EXISTS meals(meal_id INT PRIMARY KEY, category VARCHAR NOT NULL, meal VARCHAR NOT NULL);\n" +
                                                                   "CREATE TABLE IF NOT EXISTS ingredients(ingredient_id INT PRIMARY KEY, meal_id INT NOT NULL REFERENCES meals, ingredient VARCHAR NOT NULL);\n" +
                                                                   "CREATE TABLE IF NOT EXISTS plan(day TEXT, category TEXT, meal_id INT REFERENCES meals, plan_id SERIAL PRIMARY KEY);\n" +
                                                                   "CREATE SEQUENCE IF NOT EXISTS ingredient_id_seq;\n" +
                                                                   "CREATE SEQUENCE IF NOT EXISTS meal_id_seq;");
        ps.execute();
    }

    public static Connection getCon() {
        try {
            if (postgresConnection == null || !postgresConnection.isValid(10)) {
                postgresConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                postgresConnection.setAutoCommit(true);
            }
        } catch (SQLException ignored) {
        }
        return postgresConnection;
    }

    public static void close() {
        try {
            postgresConnection.close();
        } catch (SQLException e) {
            System.out.println("Whoops, db didnt like being asked to close");
            System.out.println(e.getMessage());
        }
    }

    public static void run(String statement) throws SQLException {
        PreparedStatement ps = postgresConnection.prepareStatement(statement);
        ps.execute();
    }

//    public static ArrayList<Meal> fetch(String category) throws SQLException {
//        ArrayList<Meal> list = new ArrayList<>();
//        String statement = "SELECT * from meals";
//        String tail = category.toLowerCase().matches("all") ? ";" : " where category = '%s';".formatted(category);
//        statement = statement.concat(tail);
//        PreparedStatement ps = postgresConnection.prepareStatement(statement);
//        ResultSet rs = null;
//        try {
//            rs = ps.executeQuery();
//        } catch (SQLException e) {
//            return list;
//        }
//        PreparedStatement ps2 = postgresConnection.prepareStatement("SELECT *" +
//                                                                    "from ingredients " +
//                                                                    "where meal_id = ?;");
//
//        while (rs.next()) {
//            int id = rs.getInt(1);
//            assert category.equals(rs.getString(2));
//            String name = rs.getString(3);
//            ps2.setInt(1, id);
//            ResultSet rs2 = ps2.executeQuery();
//            ArrayList<String> ingredients = new ArrayList<>();
//            while (rs2.next()) {
//                ingredients.add(rs2.getString(3));
//            }
//            Meal meal = new Meal(id, category, name, ingredients.toArray(new String[0]));
//            list.add(meal);
//        }
//        return list;
//    }
}
