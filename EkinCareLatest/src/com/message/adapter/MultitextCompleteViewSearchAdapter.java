package com.message.adapter;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.ekincare.R;
import com.google.gson.Gson;
import com.message.model.PackageItem;
import com.message.model.PackageList;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.SuggestGetSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaviTejaN on 22-12-2016.
 */

public class MultitextCompleteViewSearchAdapter extends ArrayAdapter<String>
{
    private static final int MAX_RESULTS = 10;
    private final PreferenceHelper prefs;
    private Context mContext;
    private List<String> resultList = new ArrayList<>();

    public MultitextCompleteViewSearchAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        mContext = context;
        prefs = new PreferenceHelper(mContext);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.open_search, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.packagelist)).setText(getItem(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<String> packageItemList = findPackages(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = packageItemList;
                    filterResults.count = packageItemList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<String>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<String> findPackages(Context context, String temp) {
        List<String> ListData = new ArrayList<>();
        try {
            URL js = new URL(TagValues.PACKAGE_AUTO_SEARCH+temp);
            System.out.println("URL========"+ TagValues.PACKAGE_AUTO_SEARCH+temp);

            URLConnection jc = js.openConnection();
            jc.addRequestProperty("X-CUSTOMER-KEY",prefs.getCustomerKey());
            jc.addRequestProperty("X-EKINCARE-KEY", prefs.getEkinKey());
            jc.addRequestProperty("X-DEVICE-ID", Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));

            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONArray jsonArray = new JSONArray(line);

            System.out.println("MultitextCompleteViewSearchAdapter.findPackages jsonArray="+jsonArray.length() + " " + jsonArray );

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);

                ListData.add( r.getString("name"));
                System.out.println("MultitextCompleteViewSearchAdapter.findPackages ListData="+ListData.size() + " " + " " + r.getString("name") + ListData.toString());

            }
        } catch (Exception e1) {
            e1.printStackTrace();
            System.out.println("MultitextCompleteViewSearchAdapter.findPackages Exception");
        }
        return ListData;
    }
}
