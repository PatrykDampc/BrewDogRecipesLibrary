package patryk.com.myapp.utils;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

/**
 * Created by patryk on 22.03.2018.
 */

public class App extends Application {

    Context context = this;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(context);



    }




}
