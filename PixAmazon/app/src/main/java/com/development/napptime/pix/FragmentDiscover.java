package com.development.napptime.pix;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Napptime on 2/10/15.
 */

public class FragmentDiscover extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_discover,container,false);

        //ListView list;
        String[] web = {
                "Google Plus"
        } ;
        Integer[] imageId = {
                R.drawable.samplepic1
        };

        ListView list = (ListView)view.findViewById(R.id.list);
        CustomList cus = new CustomList(getActivity(),web,imageId);
        list.setAdapter(cus);


        return view;
    }
}
