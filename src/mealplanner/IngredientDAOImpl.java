package mealplanner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IngredientDAOImpl implements genericDAO<String>{

    @Override
    public ArrayList<String> fetchByList(String Query) {
        ArrayList<String> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getCon().prepareStatement(Query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString(3);
                    list.add(name);
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
    public ArrayList<String> fetchAll() {
        return fetchByList("SELECT * FROM ingredients;");
    }

    @Override
    public String findById(int id) {
        return null;
    }

    @Override
    public String findByName(String recordName) {
        return null;
    }

    public void insert(String name, int meal_id) {
        insert(name + ',' + meal_id);
    }

    @Override
    public void insert(String ingredient) {
        String[] arr = ingredient.split(",");
        String name = arr[0];
        int meal_id = Integer.parseInt(arr[1]);
        try (PreparedStatement ps = Database.getCon().prepareStatement("INSERT INTO ingredients VALUES(?, ?, ?)")) {
            int ingredient_id = Database.hsIsRWorded("ingredient_id");
            ps.setInt(1, ingredient_id);
            ps.setInt(2, meal_id);
            ps.setString(3, name);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(String ingredient) {

    }

    @Override
    public void deleteById(int id) {

    }

    public ArrayList<String> fetchByMealID(int id) {
        return fetchByList("SELECT * FROM ingredients where meal_id = %d".formatted(id));
    }
}
