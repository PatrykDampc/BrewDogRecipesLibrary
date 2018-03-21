package patryk.com.myapp.model.dataBase;

/**
 * Created by patryk on 21.03.2018.
 */

public interface DataBaseConstans {
    String TABLE_NAME = "beers";

    interface Columns{
        String BEER_ID = "_id";
        String SERIALIZED_BEER = "serialized_beer";
    }
}
