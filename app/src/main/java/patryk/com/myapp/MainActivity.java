package patryk.com.myapp;

import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    private AlertDialog.Builder filterDialog;
    private AlertDialog dialog;

    private int alcFromV, alcToV;
    private int ibuFromV, ibuToV;
    private int ogFromV, ogToV;

    private EditText alcFrom, alcTo;
    private EditText ibuFrom, ibuTo;
    private EditText ogFrom, ogTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beerList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        beerRecyclerViewAdapter = new BeerRecyclerViewAdapter(beerList, this);
        recyclerView.setAdapter(beerRecyclerViewAdapter);
        beerRecyclerViewAdapter.notifyDataSetChanged();
        jsonResponse();

       scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
           @Override
           public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               page = 2;
               loadNextDataFromApi(page);
               page++;
           }
       };
       recyclerView.addOnScrollListener(scrollListener);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterButtonID:
                    showFilterDialog();
                // User chose the "Settings" item, show the app settings UI...
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void showFilterDialog() {
        filterDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.filter_dialog, null);

        alcFrom = view.findViewById(R.id.alcFromID);
        alcTo = view.findViewById(R.id.alcToID);
        ibuFrom = view.findViewById(R.id.ibuFromID);
        ibuTo = view.findViewById(R.id.ibuToID);
        ogFrom = view.findViewById(R.id.ogFromID);
        ogTo = view.findViewById(R.id.ogToID);


        filterDialog.setMessage("Set filters")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        filterDialog.setView(view);
        dialog = filterDialog.create();
        dialog.show();

    }


    public void loadNextDataFromApi(int offset) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.BASE_API_URL + Constants.BEER_LIST_URL_ENDPOINT + Constants.BEER_LIST_NEXR_PAGE + offset,
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
    }

    public ArrayList<Beer> jsonResponse(){
        beerList.clear();

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
                                JSONObject ingredients = beerJsonObject.getJSONObject("ingredients");
                                beer.setYeast(ingredients.getString("yeast"));
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




                                System.out.println("      "  +  sb2.toString()  );


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }


}
