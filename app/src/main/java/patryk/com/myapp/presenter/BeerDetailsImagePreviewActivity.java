package patryk.com.myapp.presenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import patryk.com.myapp.R;

public class BeerDetailsImagePreviewActivity extends AppCompatActivity {

    private String imgUrl;
    private ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_details_image_preview);

        preview = findViewById(R.id.beerDetailsImgPreviewID);

        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imageUrl");
        Picasso.with(this).load(imgUrl).into(preview);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                           | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                           // | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                           // | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
        }
    }

}
