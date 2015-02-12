package com.development.napptime.pix;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Napptime on 2/10/15.
 */

public class FragmentDiscover extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_discover,container,false);

        ListView listView = (ListView) view.findViewById(R.id.list);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };
        //ListView list;
        String[] titles = {
                "TheBoyZ",
                "BigBangTheory",
                "Family Vacation",
                "Public"
        } ;

        String[] comments = {
                "It's just monkey business",
                "The big bang theory cast",
                "Silly vacation photos",
                "Everyone can vote, everyone can participate"
        } ;

        Integer[] imageId = {
                R.drawable.samplepic1,
                R.drawable.samplepic2,
                R.drawable.samplepic3,
                R.drawable.samplepic4
        };

        ListView list = (ListView) view.findViewById(R.id.groupList);
        groupListAdapter cus = new groupListAdapter(getActivity(),titles, comments,imageId);
        list.setAdapter(cus);

        return view;
    }
}
