package mealplanner;


import java.util.ArrayList;

public record Meal(int id, String category, String name, ArrayList<String> ingredients) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(this.name).append("\n");
        sb.append("Ingredients:\n");
        ingredients.forEach(e -> sb.append(e).append("\n"));

        return (sb.toString());
    }
}
