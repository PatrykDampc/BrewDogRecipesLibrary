package patryk.com.myapp;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.squareup.picasso.Picasso;


/**
 * Created by patryk on 06.03.2018.
 */

public class BeerDetailsActivity extends AppCompatActivity {

    private Beer beer;

    private ImageView beerImageView;
    private TextView beerName, ibu, alc, yeast, firstBrewed, description, foodPairing, id, tagLine, targetFG, targedOG,
            srm, ph, attenuationLevel, finalVolume, boilVolume, mashTempDuration, fermentationTemperature, brewersTips, contributedBy, malt, hops;
    private ExpandableRelativeLayout beerBreweryInfoLayout;
    private int rotationAngle = 0;
    private ImageView arrow;

   private  String mashTimeDurationCombined;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        beer = (Beer) getIntent().getSerializableExtra("beer");
        setUpViews();
        mashTimeDurationCombined = beer.getMashTemperature() + "℃ / " + beer.getMashduration() + "min";
        System.out.println(mashTimeDurationCombined);

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
        finalVolume.setText(beer.getFinalVolume());
        boilVolume.setText(beer.getBoilVolume());
        mashTempDuration.setText(mashTimeDurationCombined);
        fermentationTemperature.setText(beer.getFermentationTemperature() + "℃");
        brewersTips.setText(beer.getBrewersTips());
        contributedBy.setText("Contributed by " + beer.getContributedBy());
        malt.setText(beer.getMalt());


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
}
