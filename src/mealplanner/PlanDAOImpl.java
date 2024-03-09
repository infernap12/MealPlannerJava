package mealplanner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlanDAOImpl implements genericDAO<Plan> {

    @Override
    public ArrayList<Plan> fetchByList(String Query) {
        ArrayList<Plan> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getCon().prepareStatement(Query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String day = rs.getString(1);
                    String category = rs.getString(2);
                    int meal_id = rs.getInt(3);
                    list.add(new Plan(day, category, meal_id, 999));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public ArrayList<Plan> fetchAll() {
        return fetchByList("SELECT * FROM plan;");
    }

    @Override
    public Plan findById(int id) {
        return null;
    }

    public Plan findByDayCat(String day, String category) {
        Plan plan = null;
        try (PreparedStatement ps = Database.getCon().prepareStatement("Select * FROM plan WHERE day = ? AND category = ?")) {
            ps.setString(1, day);
            ps.setString(2, category);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String gotDay = rs.getString(1);
                    String gotCategory = rs.getString(2);
                    int meal_id = rs.getInt(3);
                    plan = new Plan(gotDay, gotCategory, meal_id, 999);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return plan;
    }

    @Override
    public Plan findByName(String recordName) {
        return null;
    }

    @Override
    public void insert(Plan plan) {
        try (PreparedStatement ps = Database.getCon().prepareStatement("INSERT INTO plan(day, category, meal_id) " +
                                                                       "VALUES (?, ?, ?) ")) {
            ps.setString(1, plan.day());
            ps.setString(2, plan.category());
            ps.setInt(3, plan.meal_id());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Plan plan) {

    }

    @Override
    public void deleteById(int id) {

    }
}
