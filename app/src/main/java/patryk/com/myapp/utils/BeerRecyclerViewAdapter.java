package patryk.com.myapp.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import patryk.com.myapp.R;
import patryk.com.myapp.model.Beer;
import patryk.com.myapp.presenter.BeerDetailsActivity;

public class BeerRecyclerViewAdapter extends RecyclerView.Adapter<BeerRecyclerViewAdapter.ViewHolder> {
    private List<Beer> beerList;
    private Context context;

    public BeerRecyclerViewAdapter(List<Beer> beerList, Context context) {
        this.beerList = beerList;
        this.context = context;
    }

    @Override
    public BeerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Beer beer = beerList.get(position);
        String imageUrl = beer.getImgUrl();

        holder.beerName.setText(beer.getName());
        holder.ibu.setText("IBU: " + beer.getIbu());
        Picasso.with(context).load(imageUrl).into(holder.beerImage);
        holder.alc.setText(context.getString(R.string.alc) +" "+ beer.getAlc() + "%");
        holder.firstBrewed.setText(context.getString(R.string.first_brewed) + beer.getFirstBrewed());



    }

    @Override
    public int getItemCount() {
        return beerList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView beerName, alc, ibu, firstBrewed;
        ImageView beerImage;

        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;


            beerName = itemView.findViewById(R.id.beerNameListID);
            ibu = itemView.findViewById(R.id.ibuListID);
            beerImage = itemView.findViewById(R.id.beerImageListID);
            alc = itemView.findViewById(R.id.beerAlcListID);
            firstBrewed = itemView.findViewById(R.id.firstBewwedListID);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Beer beer = beerList.get(getAdapterPosition());
                      Intent intent = new Intent(context, BeerDetailsActivity.class);
//                    Gson gson = new Gson();
//                    String beerAsString = gson.toJson(beer);
//                    intent.putExtra("beer", beerAsString);

                    intent.putExtra("beer", beer);

                    ctx.startActivity(intent);
                }
            });


        }
    }
}
