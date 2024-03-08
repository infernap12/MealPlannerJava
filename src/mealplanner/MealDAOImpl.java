package mealplanner;

import java.sql.*;
import java.util.ArrayList;

public class MealDAOImpl implements genericDAO<Meal> {
    IngredientDAOImpl ingredientDAO = new IngredientDAOImpl();

    public ArrayList<Meal> fetchByCategory(String category) {
        ArrayList<Meal> list = new ArrayList<>();
        String statement = "SELECT * from meals";
        String tail = category.toLowerCase().matches("all") ? ";" : " where category = '%s';".formatted(category);
        statement = statement.concat(tail);
        try (PreparedStatement ps = Database.getCon().prepareStatement(statement)) {
            try (ResultSet rs = ps.executeQuery()) {
                try (PreparedStatement ps2 = Database.getCon().prepareStatement("SELECT *" +
                                                                                "from ingredients " +
                                                                                "where meal_id = ?;")) {

                    while (rs.next()) {
                        int id = rs.getInt(1);
                        assert category.equals(rs.getString(2));
                        String name = rs.getString(3);
                        ps2.setInt(1, id);
                        ArrayList<String> ingredients;
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            ingredients = new ArrayList<>();
                            while (rs2.next()) {
                                ingredients.add(rs2.getString(3));
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        Meal meal = new Meal(id, category, name, ingredients);
                        list.add(meal);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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
    public ArrayList<Meal> fetchByList(String Query) {
        ArrayList<Meal> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getCon().prepareStatement(Query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String category = rs.getString(2);
                    String name = rs.getString(3);
                    ArrayList<String> ingredients = ingredientDAO.fetchByMealID(id);

                    Meal meal = new Meal(id, category, name, ingredients);
                    list.add(meal);
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
    public ArrayList<Meal> fetchAll() {
        return fetchByList("SELECT * FROM meals;");
    }

    @Override
    public Meal findById(int id) {
        Meal meal;
        try (PreparedStatement ps = Database.getCon().prepareStatement("SELECT * FROM meals WHERE meal_id = ?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                meal = null;
                if (rs.next()) {
                    String category = rs.getString(2);
                    String name = rs.getString(3);
                    ArrayList<String> ingredients = ingredientDAO.fetchByMealID(id);
                    meal = new Meal(id, category, name, ingredients);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meal;
    }

    @Override
    public Meal findByName(String mealName) {
        Meal meal;
        try (PreparedStatement ps = Database.getCon().prepareStatement("SELECT * FROM meals WHERE meal = ?;")) {
            ps.setString(1, mealName);
            try (ResultSet rs = ps.executeQuery()) {
                meal = null;
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String category = rs.getString(2);
                    ArrayList<String> ingredients = ingredientDAO.fetchByMealID(id);
                    meal = new Meal(id, category, mealName, ingredients);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meal;
    }

    @Override
    public void insert(Meal meal) {
        try (PreparedStatement ps = Database.getCon().prepareStatement("INSERT INTO meals(meal_id, category, meal) " +
                                                                       "VALUES (?, ?, ?) ")) {
            int meal_id = Database.hsIsRWorded("meal_id");
            ps.setInt(1, meal_id);
            ps.setString(2, meal.category());
            ps.setString(3, meal.name());
            ps.execute();
            for (String ingredient : meal.ingredients()) {
                ingredientDAO.insert(ingredient, meal_id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Meal meal) {

    }

    @Override
    public void deleteById(int id) {

    }
}
