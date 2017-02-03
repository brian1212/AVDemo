package brian.com.avdemo.realm;

import java.util.List;

import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.model.TvModel;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm mRealm;

    public RealmHelper() {
        mRealm = Realm.getDefaultInstance();
    }

    public void insertItem(final ItemModel item) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ItemModel itemModel = realm.createObject(ItemModel.class);
                itemModel.setId(item.getId());
                itemModel.setCat_id(item.getCat_id());
                itemModel.setEnglish(item.getEnglish());
                itemModel.setVietnam(item.getVietnam());
            }
        });
    }

    public void insertTv(final TvModel model) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TvModel tvModel = realm.createObject(TvModel.class);
                tvModel.setName(model.getName());
                tvModel.setThumbnail_url(model.getThumbnail_url());
                tvModel.setApp_store_url(model.getApp_store_url());
                tvModel.setPlay_store_url(model.getPlay_store_url());
            }
        });
    }

    public List<TvModel> fetchAllTv() {
        RealmResults<TvModel> results = mRealm.where(TvModel.class).findAll();
        return results;
    }

    public List<ItemModel> fetchAllItemChecked() {
        RealmResults<ItemModel> results = mRealm.where(ItemModel.class).equalTo("isChecked", true).findAll();
        return results;
    }

    public List<ItemModel> fetchAllItemClicked() {
        RealmResults<ItemModel> results = mRealm.where(ItemModel.class).equalTo("isClicked", true).findAll();
        return results;
    }

    public List<ItemModel> fetchAllChildItem() {
        List<ItemModel> results = mRealm.copyFromRealm(mRealm.where(ItemModel.class).isNotNull("cat_id").findAll());
        return results;
    }

    public List<ItemModel> fetchAllItem() {
        List<ItemModel> results = mRealm.copyFromRealm(mRealm.where(ItemModel.class).findAll());
        return results;
    }

    public List<ItemModel> fetchAllItemByCat(String id) {
        List<ItemModel> results = mRealm.copyFromRealm(mRealm.where(ItemModel.class).equalTo("cat_id", id, Case.INSENSITIVE).findAll());
        return results;
    }

    public void deleteAllTv() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TvModel> results = mRealm.where(TvModel.class).findAll();
                results.deleteAllFromRealm();
            }
        });
    }

    public void isClicked(int id, boolean newName) {
        ItemModel item = mRealm.where(ItemModel.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        item.setClicked(newName);
        mRealm.commitTransaction();
    }

    public void isChecked(int id, boolean newName) {
        ItemModel item = mRealm.where(ItemModel.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        item.setChecked(newName);
        mRealm.commitTransaction();
    }

//    public boolean isItemExist(final int id) {
//        RealmResults<ItemModel> realmResults = mRealm.where(ItemModel.class).findAll();
//        ItemModel item = realmResults.where().equalTo("id", id).findFirst();
//        if (item == null) {
//            return false;
//        } else {
//            return true;
//        }
//    }

//    public void deleteAllItem() {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                RealmResults<ItemModel> results = mRealm.where(ItemModel.class).findAll();
//                results.deleteAllFromRealm();
//            }
//        });
//    }

//    public void deleteItemById(final int id) {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                RealmResults<ItemModel> realmResults = mRealm.where(ItemModel.class).findAll();
//                ItemModel item = realmResults.where().equalTo("id", id).findFirst();
//                item.deleteFromRealm();
//            }
//        });
//    }

    public ItemModel getItemById(final int id) {
        return  mRealm.where(ItemModel.class).equalTo("id", id).findFirst();
    }
}
