package patryk.com.myapp.presenter;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import patryk.com.myapp.utils.BeerRecyclerViewAdapter;
import patryk.com.myapp.R;
import patryk.com.myapp.model.Beer;
import patryk.com.myapp.model.JsonDataCollector;
import patryk.com.myapp.utils.Constants;
import patryk.com.myapp.utils.EndlessRecyclerViewScrollListener;

import static patryk.com.myapp.utils.util.deleteCache;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BeerRecyclerViewAdapter beerRecyclerViewAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    private AlertDialog.Builder filterDialog;
    private AlertDialog dialog;
    private EditText alcFrom, alcTo, ibuFrom, ibuTo;
    private SearchView searchView;
    private JsonDataCollector jsonDataCollector;

    private int alcFromV, alcToV, ibuFromV, ibuToV;
    private List<Beer> beerList;
    private String url = Constants.BASE_API_URL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonDataCollector = new JsonDataCollector(this);
        beerList = new ArrayList<>();
        setUpRecyclerViewAndAdapter();
        makeRequest();

    }

    private void setUpRecyclerViewAndAdapter() {
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        beerRecyclerViewAdapter = new BeerRecyclerViewAdapter(beerList, this);
        recyclerView.setAdapter(beerRecyclerViewAdapter);
    }

    private void makeRequest() {
       beerList.clear();
        jsonDataCollector.response(url,1, beerRecyclerViewAdapter, beerList);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                jsonDataCollector.response(url,page++, beerRecyclerViewAdapter, beerList);

            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        System.out.println("                                        " + url);
    }

    private void showFilterDialog() {
        filterDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.filter_dialog, null);

        alcFrom = view.findViewById(R.id.alcFromID);
        alcTo = view.findViewById(R.id.alcToID);
        ibuFrom = view.findViewById(R.id.ibuFromID);
        ibuTo = view.findViewById(R.id.ibuToID);


        filterDialog.setMessage("Set filters")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StringBuilder sb = new StringBuilder();

                        if( !alcFrom.getText().toString().equals("") && alcFrom.getText().toString().length() > 0 )
                        {
                            alcFromV = Integer.parseInt(alcFrom.getText().toString());
                            sb.append("abv_gt=" + alcFromV + "&");
                        }
                        if( !alcTo.getText().toString().equals("") && alcTo.getText().toString().length() > 0 )
                        {
                            alcToV = Integer.parseInt(alcTo.getText().toString());
                            sb.append("abv_lt=" + alcToV + "&");
                        }
                        if( !ibuFrom.getText().toString().equals("") && ibuFrom.getText().toString().length() > 0 )
                        {
                            ibuFromV = Integer.parseInt(ibuFrom.getText().toString());
                            sb.append("ibu_gt=" + ibuFromV + "&");
                        }
                        if( !ibuTo.getText().toString().equals("") && ibuTo.getText().toString().length() > 0 )
                        {
                            ibuToV = Integer.parseInt(ibuTo.getText().toString());
                            sb.append("ibu_lt=" + ibuToV + "&");
                        }

                        url = Constants.BASE_API_URL + sb.toString();
                        makeRequest();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).setNeutralButton("Reset filters", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                url = Constants.BASE_API_URL ;
                makeRequest();

            }
        });

        filterDialog.setView(view);
        dialog = filterDialog.create();
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterButtonID:
                    showFilterDialog();
                // User chose the "Settings" item, show the app settings UI...
                return true;
            case R.id.favouritesButtonListID:
                    startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
                Toast.makeText(MainActivity.this, "favourites", Toast.LENGTH_SHORT).show();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(true);

        if(searchView.isIconified()){
            url = Constants.BASE_API_URL;
            makeRequest();
        }


        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                if(!newText.toString().equals("") || newText.toString().length() > 0) {
                    url = Constants.BASE_API_URL + "beer_name=" + newText + "&";
                    makeRequest();
                } else {
                    url = Constants.BASE_API_URL;
                    makeRequest();
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                if(!query.toString().equals("") || query.toString().length() > 0) {
                    url = Constants.BASE_API_URL + "beer_name=" + query + "&";
                    makeRequest();
                } else {
                    url = Constants.BASE_API_URL;
                    makeRequest();
                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            searchView.clearFocus();
            url = Constants.BASE_API_URL;
            makeRequest();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteCache(this);
    }

}
