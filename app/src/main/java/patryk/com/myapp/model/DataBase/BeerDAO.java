package patryk.com.myapp.model.DataBase;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import patryk.com.myapp.model.Beer;

/**
 * Created by patryk on 22.03.2018.
 */

public class BeerDAO {

    private Context context;
    private Realm realm;



    public BeerDAO(Context context) {
        this.context = context;

        Realm.init(context);
        realm = Realm.getDefaultInstance();

    }

    public void insertBeer(Beer beer){
        realm.beginTransaction();
        realm.createObject(Beer.class, beer.getId());
        realm.commitTransaction();
    }

    public Beer getBeerById(String id){
        Beer beer = realm.where(Beer.class).equalTo("id", id).findFirst();
        return beer;
    }

    public void deleteBeerById(final String id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Beer.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        });
    }

    public List<Beer> getAllBeers(){
        RealmResults<Beer> results = realm.where(Beer.class).findAll();

        return results;
    }


    public List<Beer> getBeersLike(String text){
        RealmResults<Beer> results = realm.where(Beer.class).contains("name", text).findAll();
        return results;
    }

    public void deleteAllBeers() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Beer.class)
                        .findAll()
                        .deleteAllFromRealm();
            }
        });
    }



    public void close() {
        realm.close();
    }

}
