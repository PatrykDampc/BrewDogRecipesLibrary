package patryk.com.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by patryk on 06.03.2018.
 */

public class BeerDetailsActivity extends AppCompatActivity {

    private Beer beer;

    private ImageView beerImageView;
    private TextView beerName, alc, ibu, firstBrewed, yeast, description, foodPairing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        beer = (Beer) getIntent().getSerializableExtra("beer");
        setUpViews();

        beerName.setText(beer.getName());
        alc.setText("Alcohol: " + beer.getAlc() + "%");
        ibu.setText("IBU: " + beer.getIbu());
        firstBrewed.setText(getString(R.string.first_brewed) + beer.getFirstBrewed());
        yeast.setText("Yeast: " + beer.getYeast());
        description.setText(beer.getDescription());
        foodPairing.setText(beer.getFoodPairing());

        Picasso.with(getApplicationContext())
                .load(beer.getImgUrl())
                .into(beerImageView);

    }



    private void setUpViews() {
        beerName = findViewById(R.id.beerNameViewID);
        beerImageView = findViewById(R.id.beerImageViewID);
        alc = findViewById(R.id.alcViewID);
        ibu = findViewById(R.id.ibuViewID);
        firstBrewed = findViewById(R.id.firstBrewedID);
        yeast = findViewById(R.id.yeastViewID);
        description = findViewById(R.id.descriptionViewID);
        foodPairing = findViewById(R.id.foodPairingViewID);
    }
}
