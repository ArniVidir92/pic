package com.development.napptime.pix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by SnorriAgust on 17/03/2015.
 */
public class FragmentPickGrpDialog extends DialogFragment {

    ListView groupsList;
    ArrayAdapter<String> adapter;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String[] bla = {"bla", "yo", "sup"};

        groupsList = new ListView(getActivity());
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, bla);
        groupsList.setAdapter(adapter);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_pickGrp)
                .setView(groupsList)
                .setPositiveButton(R.string.dialog_setGrp, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //set grp
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
