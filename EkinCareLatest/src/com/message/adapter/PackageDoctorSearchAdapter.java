package com.message.adapter;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;

import com.ekincare.R;
import com.message.custominterface.DoctorAutoString;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by RaviTejaN on 22-12-2016.
 */

public class PackageDoctorSearchAdapter extends ArrayAdapter<String> {
    DoctorAutoString doctorautostring;
    private List<String> suggestions;
    private List<String> suggestionsPackageTypes;
    public ArrayList<String> iDs;
    Context mContext;

    AutoCompleteTextView search;
    String[] imageurlssss;
    List<SuggestGetSet> new_suggestions;
    private PreferenceHelper prefs;
    public PackageDoctorSearchAdapter(Activity context, String nameFilter, AutoCompleteTextView searchPlace, DoctorAutoString doctorautostring) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        suggestions = new ArrayList<String>();
        iDs= new ArrayList<String>();
        suggestionsPackageTypes=new ArrayList<String>();
        this.mContext= context;
        this.doctorautostring=doctorautostring;
        search=searchPlace;
    }



    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int index) {
        return suggestions.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                JsonParse jp=new JsonParse();
                if (constraint != null) {
                    // A class that queries a web API, parses the data and
                    // returns an ArrayList<GoEuroGetSet>
                    new_suggestions =jp.getParseJsonWCF(constraint.toString());
                    suggestions.clear();
                    iDs.clear();
                    suggestionsPackageTypes.clear();
                    for (int i=0;i<new_suggestions.size();i++) {
                        suggestions.add(new_suggestions.get(i).getName());
                        iDs.add(new_suggestions.get(i).getId());
                        suggestionsPackageTypes.add(new_suggestions.get(i).getPackage_type());
                    }

                    // Now assign the values and count to the FilterResults
                    // object
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();


                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.open_search, parent, false);
        }
        TextView title = (TextView) v.findViewById(R.id.packagelist);
        TextView typePackage=(TextView)v.findViewById(R.id.package_type);
        title.setText(suggestions.get(position));

        if(suggestionsPackageTypes.get(position).equals("lab_test")){
            typePackage.setText("Lab Test");
        }else if(suggestionsPackageTypes.get(position).equals("home_care")) {
            typePackage.setText("Home Care");
        }else if(suggestionsPackageTypes.get(position).equals("health_coach")){
            typePackage.setText("Health Coach");
        }else if(suggestionsPackageTypes.get(position).equals("package")){
            typePackage.setText("Package");
        }else{
            typePackage.setText(suggestionsPackageTypes.get(position));
        }




        imageurlssss = iDs.toArray(new String[iDs.size()]);
        System.out.println("urlllllllllllll=========="+ Arrays.toString(imageurlssss));
        System.out.println("urlllllllllllll=========="+ prefs.getCustomerKey());

        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doctorautostring.onDoctorAutoString(suggestions.get(position),iDs.get(position));
                notifyDataSetChanged();
            }
        });

        v.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getContext()
                            .getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            search.getWindowToken(), 0);
                }

                return false;
            }
        });


        return v;

    }
    static class ViewHolder {
        public TextView title;
        public TextView typePackage;

    }

    public class JsonParse {
        public JsonParse() {
        }

        public List<SuggestGetSet> getParseJsonWCF(String sName) {
            List<SuggestGetSet> ListData = new ArrayList< SuggestGetSet>();
            try {
                prefs = new PreferenceHelper(mContext);
                String temp = sName.replace(" ", "%20");

                URL js = new URL(TagValues.DOCTOR_AUTO_SEARCH+temp);
                System.out.println("URL========"+ TagValues.DOCTOR_AUTO_SEARCH+temp);
                URLConnection jc = js.openConnection();
                jc.addRequestProperty("X-CUSTOMER-KEY",prefs.getCustomerKey());
                jc.addRequestProperty("X-EKINCARE-KEY", prefs.getEkinKey());
                jc.addRequestProperty("X-DEVICE-ID", Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));

                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                String line = reader.readLine();
                JSONArray jsonArray = new JSONArray(line);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    ListData.add(new SuggestGetSet(r.getString("id"), r.getString("name"), r.getString("package_type")));
                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return ListData;

        }
    }

}
