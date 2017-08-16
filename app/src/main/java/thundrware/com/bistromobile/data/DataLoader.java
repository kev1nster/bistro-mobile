package thundrware.com.bistromobile.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import thundrware.com.bistromobile.data.repositories.AreasRepository;
import thundrware.com.bistromobile.data.repositories.CategoriesRepository;
import thundrware.com.bistromobile.data.repositories.GroupRepository;
import thundrware.com.bistromobile.data.repositories.ProductsRepository;
import thundrware.com.bistromobile.listeners.DataProcessingListener;
import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Group;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.networking.DataService;

public class DataLoader {

    protected enum EntitiesToLoad {
        Categories,
        Areas,
        Products,
        Groups
    }

    private List<EntitiesToLoad> mEntitiesToLoadList;
    private DataProcessingListener mProcessFinishedListener;

    private DataService mDataService;

    public DataLoader(DataService dataService, DataProcessingListener processStatusListener) {
        mDataService = dataService;
        mEntitiesToLoadList = new ArrayList<>();
        mProcessFinishedListener = processStatusListener;
    }

    public DataLoader loadCategories() {

        final CategoriesRepository categoriesRepository = new CategoriesRepository();

        mEntitiesToLoadList.add(EntitiesToLoad.Categories);

        mDataService.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Category>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Category> categoryList) {
                        onTaskProgressHandler("Configurare categorii...");
                        categoriesRepository.addRange(categoryList);
                        Log.e("RXJAVA_TEST", "Categoriile au fost adăugate, mai sunt inca " + mEntitiesToLoadList.size() + " entitati de adaugat");
                        mEntitiesToLoadList.remove(EntitiesToLoad.Categories);
                        taskFinishChecker();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
        return this;
    }

    public DataLoader loadProducts() {

        final ProductsRepository productsRepository = new ProductsRepository();

        mEntitiesToLoadList.add(EntitiesToLoad.Products);

        mDataService.getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Product>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Product> products) {
                        onTaskProgressHandler("Configurare produse...");
                        productsRepository.addRange(products);
                        Log.e("RXJAVA_TEST", "Produsele au fost adăugate, mai sunt inca " + mEntitiesToLoadList.size() + " entitati de adaugat");
                        mEntitiesToLoadList.remove(EntitiesToLoad.Products);
                        taskFinishChecker();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

        return this;

    }

    public DataLoader loadGroups() {
        final GroupRepository groupRepository = new GroupRepository();

        mEntitiesToLoadList.add(EntitiesToLoad.Groups);

        mDataService.getGroups()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Group>>() {

                    @Override
                   public void onSubscribe(@NonNull Disposable d) {

                   }

                   @Override
                   public void onSuccess(@NonNull List<Group> groups) {
                       onTaskProgressHandler("Configurare grupe...");
                       groupRepository.addRange(groups);
                       Log.e("RXJAVA_TEST", "Grupele au fost adăugate, mai sunt inca " + mEntitiesToLoadList.size() + " entitati de adaugat");
                       mEntitiesToLoadList.remove(EntitiesToLoad.Groups);
                       taskFinishChecker();
                   }

                   @Override
                   public void onError(@NonNull Throwable e) {

                   }

                });
            return this;
    }

    public DataLoader loadAreas() {
        final AreasRepository areasRepository = new AreasRepository();

        mEntitiesToLoadList.add(EntitiesToLoad.Areas);

        mDataService.getAreas()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Area>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Area> areaList) {
                        onTaskProgressHandler("Configurare zone...");
                        areasRepository.addRange(areaList);
                        Log.e("RXJAVA_TEST", "Zonele au fost adăugate, mai sunt inca " + mEntitiesToLoadList.size() + " entitati de adaugat");
                        mEntitiesToLoadList.remove(EntitiesToLoad.Areas);
                        taskFinishChecker();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
        return this;
    }

    public boolean hasFinished() {
        return (mEntitiesToLoadList.size() == 0);
    }


    private void onTaskProgressHandler(String message) {
        mProcessFinishedListener.onProcessStatusUpdate(message);
    }

    private void taskFinishChecker() {
        if (hasFinished()) {
            mProcessFinishedListener.onProcessFinished();
        }
    }



}
