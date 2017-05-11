package uk.ac.tees.gingerbread.myfitness.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 07mct on 22/03/2017.
 */

public class NutritionixQuery extends AsyncTask<Void,Void,String>
{
    private Context activityContext;
    private String searchQuery;
    private String result;

    /**Constructs an object for interacting with the api.
     * Takes context so that errors can be returned.
     *
     * @param activityContext
     */
    public NutritionixQuery(Context activityContext, String searchQuery)
    {
        this.activityContext = activityContext;
        this.searchQuery = searchQuery;
    }

    protected String doInBackground(Void... urls) {
        try {
            URL url = new URL("https://api.nutritionix.com/v1_1/search/"
                    + searchQuery +
                    "?fields=item_name%2" +
                    "Cnf_calories%2" +
                    "Cnf_protein" +
                    "&appId=f7a6647d" +
                    "&appKey=973127408431e443f91406c6aa837715");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Toast.makeText(activityContext ,"Error",  Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            Toast.makeText(activityContext ,"Error",  Toast.LENGTH_LONG).show();
        }
        this.result = response;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
