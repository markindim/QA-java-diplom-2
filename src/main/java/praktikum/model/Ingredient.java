package praktikum.model;

import java.util.List;

@lombok.Data
public class Ingredient {
    private String success;
    private List<IngredientData> data;
}