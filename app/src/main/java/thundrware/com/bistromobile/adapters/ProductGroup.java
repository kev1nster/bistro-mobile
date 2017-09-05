package thundrware.com.bistromobile.adapters;

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import thundrware.com.bistromobile.models.Product;

public class ProductGroup extends ExpandableGroup<Product> {

    public ProductGroup(String title, List<Product> items) {
        super(title, items);
    }

    protected ProductGroup(Parcel in) {
        super(in);
    }
}