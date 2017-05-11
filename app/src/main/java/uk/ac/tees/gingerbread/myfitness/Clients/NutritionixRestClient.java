package uk.ac.tees.gingerbread.myfitness.Clients;

/**
 * Created by 07mct on 22/03/2017.
 */


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

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
}
