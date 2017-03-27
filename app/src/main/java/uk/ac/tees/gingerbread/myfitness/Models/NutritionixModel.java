package uk.ac.tees.gingerbread.myfitness.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 07mct on 22/03/2017.
 */

public class NutritionixModel
{
    private String id;
    private String name;
    private int calories;
    private float protein;
    private int servingSize;

    public NutritionixModel(JSONObject object) {
        try {
            this.name = (object.getJSONObject("fields")).getString("item_name");

            try {
                this.calories = (int) Double.parseDouble(object.getJSONObject("fields").getString("nf_calories").trim());
            }
            catch (NumberFormatException s){
                this.calories = 0;
            }

            try {
                this.protein = Float.parseFloat((object.getJSONObject("fields")).getString("nf_protein"));
            }
            catch (NumberFormatException s){
                this.protein = 0;
            }

            try {
                this.servingSize = Integer.parseInt((object.getJSONObject("fields")).getString("nf_serving_size_qty"));
            }
            catch (NumberFormatException s)
            {
                this.servingSize = 1;
            }

            this.id = (object.getJSONObject("fields")).getString("item_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public NutritionixModel(String name, int calories, float protein, int servingSize) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.servingSize = servingSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public String getId() {
        return id;
    }
}
