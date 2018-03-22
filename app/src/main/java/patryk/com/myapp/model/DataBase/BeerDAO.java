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
        beer = realm.createObject(Beer.class);
        beer.setId(generateId());
        realm.commitTransaction();
    }

    public Beer getBeerById(int id){
        Beer beer = realm.where(Beer.class).equalTo("id", id).findFirst();
        return beer;
    }

    public void deleteBeerById(final long id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Beer.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        });
    }

    public List<Beer> getallBeers(){
        RealmResults<Beer> results = realm.where(Beer.class).findAll();

        return results;
    }


    public List<Beer> getnotesLike(String text){
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


    private int generateId() {
        return realm.where(Beer.class).max("id").intValue() + 1;
    }

    public void close() {
        realm.close();
    }

}
