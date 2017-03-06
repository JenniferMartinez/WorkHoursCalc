package workhourscalc.workhourscalculator;


import android.view.View;
import android.widget.AdapterView;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener{


    //when items are selected from spinner list
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

        //when selecting an item
        String item = parent.getItemAtPosition(position).toString();

    }

    public void onNothingSelected(AdapterView<?> arg0) {}


}
