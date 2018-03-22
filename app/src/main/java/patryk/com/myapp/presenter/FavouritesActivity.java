package patryk.com.myapp.presenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import patryk.com.myapp.R;
import patryk.com.myapp.model.Beer;
import patryk.com.myapp.model.BeerDAO;
import patryk.com.myapp.utils.BeerRecyclerViewAdapter;

public class FavouritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BeerRecyclerViewAdapter beerRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Beer> beerList;
    private BeerDAO beerDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        beerDAO = new BeerDAO(this);
        beerList = beerDAO.getAllBeers();

        setUpRecyclerViewAndAdapter();




    }



    private void setUpRecyclerViewAndAdapter() {
        recyclerView = findViewById(R.id.favouritesRecyclerViewID);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        beerRecyclerViewAdapter = new BeerRecyclerViewAdapter(beerList, this);
        recyclerView.setAdapter(beerRecyclerViewAdapter);
    }
}
