package thundrware.com.bistromobile.data;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
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
                .subscribe(new Observer<List<Category>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Category> categoryList) {
                        categoriesRepository.addRange(categoryList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mEntitiesToLoadList.remove(EntitiesToLoad.Categories);
                        onTaskFinishedHandler("Configurare categorii...");
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
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Product> products) {
                        productsRepository.addRange(products);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mEntitiesToLoadList.remove(EntitiesToLoad.Products);
                        onTaskFinishedHandler("Configurare produse...");
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
                .subscribe(new Observer<List<Group>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Group> groups) {
                        groupRepository.addRange(groups);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mEntitiesToLoadList.remove(EntitiesToLoad.Groups);
                        onTaskFinishedHandler("Configurare grupe...");
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
                .subscribe(new Observer<List<Area>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Area> areaList) {
                        areasRepository.addRange(areaList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mEntitiesToLoadList.remove(EntitiesToLoad.Areas);
                        onTaskFinishedHandler("Configurare zone...");
                    }
                });
        return this;
    }

    public boolean hasFinished() {
        return (mEntitiesToLoadList.size() == 0);
    }

    private void onTaskFinishedHandler(String message) {
        if (mEntitiesToLoadList.size() == 0) {
            mProcessFinishedListener.onProcessFinished();
        } else {
            mProcessFinishedListener.onProcessStatusUpdate(message);
        }
    }



}
