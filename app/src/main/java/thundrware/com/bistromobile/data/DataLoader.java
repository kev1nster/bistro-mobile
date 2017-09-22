package thundrware.com.bistromobile.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import thundrware.com.bistromobile.data.repositories.AreasRepository;
import thundrware.com.bistromobile.data.repositories.CategoriesRepository;
import thundrware.com.bistromobile.data.repositories.GroupRepository;
import thundrware.com.bistromobile.data.repositories.ProductsRepository;
import thundrware.com.bistromobile.listeners.DataLoadingListener;
import thundrware.com.bistromobile.listeners.DataPersistingListener;
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

    private final List<EntitiesToLoad> entitiesToLoad;
    private DataLoadingListener dataProcessingListener;
    private DataService dataService;

    public DataLoader(DataService dataService, DataLoadingListener listener) {
        this.dataService = dataService;
        entitiesToLoad = new ArrayList<>();
        dataProcessingListener = listener;
    }

    public void load() {

        /*
            Loading Categories
         */

        dataService.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Category>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        entitiesToLoad.add(EntitiesToLoad.Categories);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Category> categoryList) {
                        // has successfully fetched data from the API
                        // now let's try to persist it into database

                        CategoriesRepository repository = new CategoriesRepository();
                        repository.addRangeAsync(categoryList, new DataPersistingListener() {
                            @Override
                            public void onSuccess() {
                                // has successfully persisted data into the database
                                entitiesToLoad.remove(EntitiesToLoad.Categories);
                                onEntityPersisted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dataProcessingListener.onError(e);
                            }
                        });

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dataProcessingListener.onError(e);
                    }
                });

        /*
            Loading Areas
         */

        dataService.getAreas()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Area>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        entitiesToLoad.add(EntitiesToLoad.Areas);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Area> areaList) {
                        AreasRepository repository = new AreasRepository();
                        repository.addRangeAsync(areaList, new DataPersistingListener() {
                            @Override
                            public void onSuccess() {
                                entitiesToLoad.remove(EntitiesToLoad.Areas);
                                onEntityPersisted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dataProcessingListener.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dataProcessingListener.onError(e);
                    }
                });

        /*
            Loading Products
         */

        dataService.getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Product>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        entitiesToLoad.add(EntitiesToLoad.Products);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Product> products) {
                        ProductsRepository repository = new ProductsRepository();
                        repository.addRangeAsync(products, new DataPersistingListener() {
                            @Override
                            public void onSuccess() {
                                entitiesToLoad.remove(EntitiesToLoad.Products);
                                onEntityPersisted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dataProcessingListener.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dataProcessingListener.onError(e);
                    }
                });

        /*
            Loading Groups
         */

        dataService.getGroups()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Group>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        entitiesToLoad.add(EntitiesToLoad.Groups);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Group> groups) {
                        GroupRepository repository = new GroupRepository();
                        repository.addRangeAsync(groups, new DataPersistingListener() {
                            @Override
                            public void onSuccess() {
                                entitiesToLoad.remove(EntitiesToLoad.Groups);
                                onEntityPersisted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dataProcessingListener.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dataProcessingListener.onError(e);
                    }
                });

    }

    private void onEntityPersisted() {
        dataProcessingListener.onLoadingProgress("Mai sunt " + entitiesToLoad.size() + " entități de încărcat...");

        if (entitiesToLoad.size() == 0) {
            dataProcessingListener.onLoadingFinished();
        }
    }


}
