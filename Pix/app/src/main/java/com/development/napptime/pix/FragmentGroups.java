package com.development.napptime.pix;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by Napptime on 2/10/15.
 *
 * Klasi þessi sér um að sýna þá hópa sem notandi er nú þegar skráður í og er því miðsvæði appsins
 * og upphafsskjár appsins að inn/nýskráningu undanskildri þegar appið er komið lengra.
 */
public class FragmentGroups extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        //setContentView(R.layout.main);


/*
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
*/

        final String[] groupNames = {
                "Google",
                "Ivan",
                "Arni",
                "Snorri",
                "Lexi"
        } ;

        int[] imageId = {
                R.drawable.samplepic6,
                R.drawable.samplepic6,
                R.drawable.samplepic6,
                R.drawable.samplepic6,
                R.drawable.samplepic6
        };


        GroupGridAdapter adapter = new GroupGridAdapter(getActivity(), groupNames, imageId);
        GridView grid=(GridView) view.findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " +groupNames[+ position], Toast.LENGTH_SHORT).show();
            }
        });





        return view;
    }
}
