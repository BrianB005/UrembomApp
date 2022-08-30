package com.brianbett.urembom;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RealmHandler  {
    final Realm realm;

    public RealmHandler(Context context) {

        Realm.init(context);
        RealmConfiguration configuration= new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        realm=Realm.getDefaultInstance();
//        realm.beginTransaction();
    }


    public  void addCartItem(RealmCartItem realmCartItem,List<String> colors){

        RealmList<String> colorsList=new RealmList<>();
        colorsList.addAll(colors);
        realmCartItem.setColors(colorsList);
        realmCartItem.setSelectedColor(colorsList.get(0));

        realm.executeTransaction(realm1->{
            realm1.insertOrUpdate(realmCartItem);
        });



    }

    public  void deleteCartItem(String productId){
        final RealmCartItem cartItem=realm.where(RealmCartItem.class).equalTo("productId",productId).findFirst();
        realm.executeTransaction(realm -> {
            assert cartItem != null;
            cartItem.deleteFromRealm();
        });
    }
    public RealmResults<RealmCartItem> getAllCartItems(){
        return realm.where(RealmCartItem.class).findAll();
    }

    public void clearCart(){
        realm.executeTransaction((realm2->realm2.delete(RealmCartItem.class)));

//        Toast.makeText()
    }




}
