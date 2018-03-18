package patryk.com.myapp.model;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import patryk.com.myapp.utils.BeerRecyclerViewAdapter;

/**
 * Created by patryk on 14.03.2018.
 */

public class JsonDataCollector {

    RequestQueue requestQueue;
    Context context;


    public JsonDataCollector(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }




    public void response(String url, int page, final BeerRecyclerViewAdapter beerRecyclerViewAdapter, final ArrayList<Beer> beerList) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url + "page=" + page,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject beerJsonObject = response.getJSONObject(i);

                                Beer beer = new Beer();

                                JSONArray foodPairing = beerJsonObject.getJSONArray("food_pairing");
                                StringBuilder sb = new StringBuilder();
                                for(int j=0; j<foodPairing.length(); j++){
                                    sb.append("- " + foodPairing.get(j) + "\n");
                                }
                                beer.setFoodPairing(sb.toString());

                                beer.setId(beerJsonObject.getString("id"));
                                beer.setName(beerJsonObject.getString("name"));
                                beer.setIbu(beerJsonObject.getString("ibu"));
                                beer.setAlc(beerJsonObject.getString("abv"));
                                beer.setImgUrl(beerJsonObject.getString("image_url"));
                                beer.setId(beerJsonObject.getString("id"));
                                beer.setDescription(beerJsonObject.getString("description"));
                                beer.setFirstBrewed(beerJsonObject.getString("first_brewed"));
                                beer.setTagLine(beerJsonObject.getString("tagline"));
                                beer.setContributedBy(beerJsonObject.getString("contributed_by"));
                                beer.setTargetFG(beerJsonObject.getString("target_fg"));
                                beer.setTargedOG(beerJsonObject.getString("target_og"));
                                beer.setEbc(beerJsonObject.getString("ebc"));
                                beer.setSrm(beerJsonObject.getString("srm"));
                                beer.setPh(beerJsonObject.getString("ph"));
                                beer.setAttenuationLevel(beerJsonObject.getString("attenuation_level"));
                                beer.setBrewersTips(beerJsonObject.getString("brewers_tips"));


                                JSONObject volume = beerJsonObject.getJSONObject("volume");
                                beer.setFinalVolume(volume.getString("value"));
                                JSONObject boilVolume = beerJsonObject.getJSONObject("boil_volume");
                                beer.setBoilVolume(boilVolume.getString("value"));

                                JSONObject method = beerJsonObject.getJSONObject("method");
                                JSONArray mashTemp = method.getJSONArray("mash_temp");
                                JSONObject temperature = mashTemp.getJSONObject(0);
                                JSONObject temp = temperature.getJSONObject("temp");
                                beer.setMashTemperature(temp.getString("value"));
                                beer.setMashduration(temperature.getString("duration"));
                                JSONObject fermantationTemp = method.getJSONObject("fermentation");
                                JSONObject ferTemp = fermantationTemp.getJSONObject("temp");

                                beer.setFermentationTemperature(ferTemp.getString("value"));
                                JSONObject ingredients = beerJsonObject.getJSONObject("ingredients");
                                beer.setYeast(ingredients.getString("yeast"));
                                JSONArray malt = ingredients.getJSONArray("malt");
                                StringBuilder sb2 = new StringBuilder();
                                for(int j=0; j<malt.length(); j++){
                                    JSONObject mal = malt.getJSONObject(j);
                                    JSONObject val = mal.getJSONObject("amount");
                                    sb2.append(mal.getString("name") +" - "+ val.getString("value") + "kg" + "\n");
                                }
                                beer.setMalt(sb2.toString());

                                JSONArray hops = ingredients.getJSONArray("hops");
                                StringBuilder sb3 = new StringBuilder();
                                for(int j=0; j<hops.length(); j++){
                                    JSONObject hop = hops.getJSONObject(j);
                                    JSONObject val = hop.getJSONObject("amount");
                                    sb3.append(hop.getString("name") +" - "+ val.getString("value") + "g, at: " +
                                            hop.getString("add") +", attribute: "+ hop.getString("attribute") + "\n");
                                }
                                beer.setHops(sb3.toString());


                                beerList.add(beer);
                            }
                            beerRecyclerViewAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        System.out.println("ERROR");

                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

}
