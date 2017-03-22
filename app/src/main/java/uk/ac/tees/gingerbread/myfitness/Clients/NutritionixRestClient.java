package uk.ac.tees.gingerbread.myfitness.Clients;

/**
 * Created by 07mct on 22/03/2017.
 */


import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import uk.ac.tees.gingerbread.myfitness.Models.NutritionixModel;
import uk.ac.tees.gingerbread.myfitness.Adapters.NutritionixAdapter;

/**A rest client for nutritionix.
 *Copy and paste the commented getEntries() below into an activities class.
 */
public class NutritionixRestClient
{
    private static final String BASE_URL = "https://api.nutritionix.com/v1_1/search/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, Header[] headers, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAbsoluteUrl(url), headers, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

//    private void getEntries(final Activity activity, String searchQuery) {
//        List<Header> headers = new ArrayList<>();
//        headers.add(new BasicHeader("Accept", "application/json"));
//
//        NutritionixRestClient.get(activity, searchQuery + "?fields=item_name%2Cnf_calories%2Cnf_protein&appId=f7a6647d&appKey=973127408431e443f91406c6aa837715", headers.toArray(new Header[headers.size()]),
//                null, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                        ArrayList<NutritionixModel> nutritionixArray = new ArrayList<NutritionixModel>();
//                        NutritionixAdapter nutritionixAdapter = new NutritionixAdapter(activity,nutritionixArray);
//
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                nutritionixAdapter.add(new NutritionixModel(response.getJSONObject(i)));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        //Use nutritionix adapter here by updating a listview and setting adapter.
//                    }
//                });
//    }
}
