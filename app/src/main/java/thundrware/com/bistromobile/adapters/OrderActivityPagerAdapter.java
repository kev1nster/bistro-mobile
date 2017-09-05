package thundrware.com.bistromobile.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.data.repositories.CategoriesRepository;
import thundrware.com.bistromobile.data.repositories.GroupRepository;
import thundrware.com.bistromobile.data.repositories.ProductsRepository;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Group;


public class OrderActivityPagerAdapter extends FragmentStatePagerAdapter {

    private List<Category> categories;
    private Context context;

    public OrderActivityPagerAdapter(Context context, List<Category> categoryList, FragmentManager fm) {
        super(fm);
        categories = categoryList;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new OrderActivityFragment();
        Bundle args = new Bundle();
        args.putInt(OrderActivityFragment.CATEGORY_ID, categories.get(position).getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getName().toUpperCase();
    }

    public static class OrderActivityFragment extends Fragment {

        @BindView(R.id.orderActivityFragmentRecyclerView)
        RecyclerView recyclerView;

        public static final String CATEGORY_ID = "category";
        private int categoryId;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.order_activity_fragment_layout, container, false);
            ButterKnife.bind(this, view);

            categoryId = getArguments().getInt(CATEGORY_ID);



            // create a new list to add items to it when I process them
            List<ProductGroup> productGroupList = new ArrayList<>();

            ProductsRepository productsRepository = new ProductsRepository();
            GroupRepository groupRepository = new GroupRepository();
            CategoriesRepository categoriesRepository = new CategoriesRepository();

            // for each group in the database, create a ProductGroup object, add its title and products that belong to each group

            String categoryGroupsStringList = categoriesRepository.get(categoryId).getGroupsList();
            List<Group> groups = groupRepository.getWhereGroupIsIn(categoryGroupsStringList);

            for (int i=0; i < groups.size(); i++) {

                ProductGroup productGroup = new ProductGroup(groups.get(i).getName(), productsRepository.getWhereGroupIdEquals(groups.get(i).getId()));
                productGroupList.add(productGroup);
            }


            ProductsAdapter adapter = new ProductsAdapter(getActivity(), categoryId, productGroupList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            return view;
        }
    }
}
