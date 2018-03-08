package patryk.com.myapp;

import android.app.VoiceInteractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BeerRecyclerViewAdapter beerRecyclerViewAdapter;
    private ArrayList<Beer> beerList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        beerList = new ArrayList<>();

        beerRecyclerViewAdapter = new BeerRecyclerViewAdapter(beerList, this);
        recyclerView.setAdapter(beerRecyclerViewAdapter);
        beerRecyclerViewAdapter.notifyDataSetChanged();
        jsonResponse();


    }

    public ArrayList<Beer> jsonResponse(){
        beerList.clear();
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.BASE_API_URL + Constants.BEER_LIST_URL_ENDPOINT,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject beerJsonObject = response.getJSONObject(i);
                                System.out.println(response.toString());
                                Beer beer = new Beer();

                                JSONArray foodPairing = beerJsonObject.getJSONArray("food_pairing");
                                StringBuilder sb = new StringBuilder();
                                for(int j=0; j<foodPairing.length(); j++){
                                    sb.append("- " + foodPairing.get(j) + "\n");
                                }

                                beer.setFoodPairing(sb.toString());
                                beer.setName(beerJsonObject.getString("name"));
                                beer.setIbu(beerJsonObject.getString("ibu"));
                                beer.setAlc(beerJsonObject.getString("abv"));
                                beer.setImgUrl(beerJsonObject.getString("image_url"));
                                beer.setId(beerJsonObject.getString("id"));
                                beer.setDescription(beerJsonObject.getString("description"));
                                beer.setFirstBrewed(beerJsonObject.getString("first_brewed"));
                                JSONObject ingredients = beerJsonObject.getJSONObject("ingredients");
                                beer.setYeast(ingredients.getString("yeast"));

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
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        return beerList;
    }

}
