package patryk.com.myapp.presenter;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.squareup.picasso.Picasso;

import patryk.com.myapp.R;
import patryk.com.myapp.model.Beer;
import patryk.com.myapp.model.DataBase.BeerDAO;

public class BeerDetailsActivity extends AppCompatActivity {

    private Beer beer;

    private ImageView beerImageView, arrow;
    private TextView beerName, ibu, alc, yeast, firstBrewed, description, foodPairing, tagLine, targetFG, targedOG,
            srm, ph, attenuationLevel, finalVolume, boilVolume, mashTempDuration, fermentationTemperature, brewersTips, contributedBy, malt, hops;
    private ExpandableRelativeLayout beerBreweryInfoLayout;
    private int rotationAngle = 0;
    private int id;
    Intent intent;
    BeerDAO beerDAO;
    Menu menu;

   private  String mashTimeDurationCombined;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        beer = (Beer) getIntent().getSerializableExtra("beer");
        setUpViews();
        fetchViews();


        intent = new Intent(this, BeerDetailsImagePreviewActivity.class);
        intent.putExtra("imageUrl", beer.getImgUrl());
        beerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        beerDAO = new BeerDAO(this);




    }

    private void fetchViews() {
        mashTimeDurationCombined = beer.getMashTemperature() + "℃ / " + beer.getMashduration() + "min";
        beerName.setText(beer.getName());
        alc.setText("Alcohol: " + beer.getAlc() + "%");
        ibu.setText("IBU: " + beer.getIbu());
        firstBrewed.setText(getString(R.string.first_brewed) + beer.getFirstBrewed());
        yeast.setText("Yeast: " + beer.getYeast());
        description.setText(beer.getDescription());
        foodPairing.setText(beer.getFoodPairing());
        tagLine.setText(beer.getTagLine());
        targetFG.setText(beer.getTargetFG());
        targedOG.setText(beer.getTargedOG());
        srm.setText(beer.getSrm() + " / " + beer.getEbc() + " EBC");
        ph.setText(beer.getPh());
        attenuationLevel.setText(beer.getAttenuationLevel());
        finalVolume.setText(beer.getFinalVolume() + "L");
        boilVolume.setText(beer.getBoilVolume() + "L");
        mashTempDuration.setText(mashTimeDurationCombined);
        fermentationTemperature.setText(beer.getFermentationTemperature() + "℃");
        brewersTips.setText(beer.getBrewersTips());
        contributedBy.setText("Contributed by " + beer.getContributedBy());
        malt.setText(beer.getMalt());
        hops.setText(beer.getHops());

        Picasso.with(getApplicationContext())
                .load(beer.getImgUrl())
                .into(beerImageView);
        beerBreweryInfoLayout.toggle(); // toggle expand and collapse


    }

    public void expandableButton(View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(arrow, "rotation",rotationAngle, rotationAngle + 180);
        anim.setDuration(600);
        anim.start();
        rotationAngle += 180;
        rotationAngle = rotationAngle%360;
        beerBreweryInfoLayout.toggle(); // toggle expand and collapse

    }


    private void setUpViews() {
        beerBreweryInfoLayout  = (ExpandableRelativeLayout) findViewById(R.id.breweryRelativeLayoutID);
        arrow = findViewById(R.id.arrowID);

        beerName = findViewById(R.id.beerNameViewID);
        beerImageView = findViewById(R.id.beerImageViewID);
        alc = findViewById(R.id.alcViewID);
        ibu = findViewById(R.id.ibuViewID);
        firstBrewed = findViewById(R.id.firstBrewedID);
        yeast = findViewById(R.id.yeastViewID);
        description = findViewById(R.id.descriptionViewID);
        foodPairing = findViewById(R.id.foodPairingViewID);
        tagLine = findViewById(R.id.tagLineViewID);
        targetFG = findViewById(R.id.finalGravityViewID);
        targedOG = findViewById(R.id.originalGravityViewID);
        srm = findViewById(R.id.srmViewID);
        ph = findViewById(R.id.phViewID);
        attenuationLevel = findViewById(R.id.attenuationLevelID);
        finalVolume = findViewById(R.id.finalVolumeViewID);
        boilVolume = findViewById(R.id.boilVolumeViewID);
        mashTempDuration = findViewById(R.id.mashTempDurationViewID);
        fermentationTemperature = findViewById(R.id.fermantationTempViewID);
        brewersTips = findViewById(R.id.brewersTipsViewID);
        contributedBy = findViewById(R.id.contributedByViewID);
        malt = findViewById(R.id.maltViewID);
        hops = findViewById(R.id.hopsViewID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        if(isBeerFavourited()){
            menu.getItem(0).setIcon(R.drawable.ic_heart_white_48dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_heart_outline_white_48dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.addToFavsID:
                if(isBeerFavourited()){
                    Toast.makeText(BeerDetailsActivity.this, "Beer already added to fav list!", Toast.LENGTH_SHORT).show();
                } else {
                    beerDAO.insertBeer(beer);
                    Toast.makeText( BeerDetailsActivity.this, "Added to Favourites!", Toast.LENGTH_SHORT).show();
                    menu.getItem(0).setIcon(R.drawable.ic_heart_white_48dp);
                }


                // User chose the "Settings" item, show the app settings UI...
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public Boolean isBeerFavourited(){
        if(beerDAO.getBeerById(beer.getId()) != null){
            return true;
        }
        return false;
    }


}
