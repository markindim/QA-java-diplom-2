package praktikum.service;

import praktikum.client.IngredientClient;
import praktikum.model.Ingredient;
import praktikum.model.IngredientData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientGenerator {
    private final IngredientClient ingredientClient = new IngredientClient();
    private final Ingredient ingredient = ingredientClient.getDefaultIngredient();
    private final Map<String, String[]> ingredientMap = new HashMap<>();
    List<IngredientData> ingredients = ingredient.getData();

    public Map<String, String[]> getCorrectIngredients() {
        String[] hashIngredient = new String[ingredients.size()];
        for (int i = 0; i < hashIngredient.length; i++) {
            hashIngredient[i] = ingredients.get(i).get_id();
        }
        ingredientMap.put("ingredients", hashIngredient);
        return ingredientMap;
    }

    public Map<String, String[]> getEmptyIngredients() {
        return ingredientMap;
    }

    public Map<String, String[]> getIncorrectIngredients() {
        ingredientMap.put("ingredients", new String[]{"invalid hash"});
        return ingredientMap;
    }
}