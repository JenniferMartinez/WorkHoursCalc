package workhourscalc.workhourscalculator;

import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView clockIn, clockOut, hoursWorked;
    int totalHoursWorked, totalMinutesWorked;
//    Spinner startTime = (Spinner) findViewById(R.id.startTime);
//    Spinner endTime = (Spinner) findViewById(R.id.endTime);
    String timeIn, timeOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner startTimeSpinner = (Spinner) findViewById(R.id.startTimeSpinner);
        Spinner endTimeSpinner = (Spinner) findViewById(R.id.endTimeSpinner);
//        EditText clockInTime = (EditText) findViewById(R.id.clockInTime);
//        EditText clockOutTime = (EditText) findViewById(R.id.clockOutTime);

        //populate spinners
        addTimesOfDayToStartTimeSpinner();
        addTimesOfDayToEndTimeSpinner();

        //add listeners for items selected on spinners
        startTimeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        endTimeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        //add click listener to submit button
        addListenerToSubmitButton();

    }


    //add times of day to start time spinner
    private void addTimesOfDayToStartTimeSpinner()
    {

        Spinner startTime = (Spinner) findViewById(R.id.startTimeSpinner);

        //create list
        List<String> list = new ArrayList<String>();

        //populate list
        list.add("AM");
        list.add("PM");

        //add list to array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        //add adapter to spinner
        startTime.setAdapter(adapter);

    }

    //add times of day to end time spinner
    private void addTimesOfDayToEndTimeSpinner()
    {

        Spinner endTime = (Spinner) findViewById(R.id.endTimeSpinner);

        //create list
        List<String> list = new ArrayList<String>();

        //populate list
        list.add("AM");
        list.add("PM");

        //add list to array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        //add adapter to spinner
        endTime.setAdapter(adapter);

    }

    //format and calculate hours
    private void formatAndGetTimes()
    {
        Spinner startTime = (Spinner) findViewById(R.id.startTimeSpinner);
        Spinner endTime = (Spinner) findViewById(R.id.endTimeSpinner);
        EditText clockInTime = (EditText) findViewById(R.id.clockInTime);
        EditText clockOutTime = (EditText) findViewById(R.id.clockOutTime);

        //store user input
        String timeIn = clockInTime.getText().toString();
        String timeOut = clockOutTime.getText().toString();

        //validate user input length
        timeIn = checkInputLength(timeIn);
        timeOut = checkInputLength(timeOut);

        //get user input values
        timeIn = timeIn + " "+ String.valueOf(startTime.getSelectedItem());
        timeOut = timeOut + " "+ String.valueOf(endTime.getSelectedItem());

//        SimpleDateFormat militaryFormat = new SimpleDateFormat("HH:mm");

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

        try {

            //convert times into Date objects for calculations to be done
            Date start = timeFormat.parse(timeIn);
            Date end = timeFormat.parse(timeOut);

            //calculate amount of hours worked
            calculateTime(start, end);

        } catch (ParseException e) {

        }

    }

    //add listener to submit button
    private void addListenerToSubmitButton()
    {

        Button submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //retrieve values and calculate
                formatAndGetTimes();

                //display output
                displayOutput();

                //reset input values in text boxes
                reset();

            }
        });

    }

    //check user input length
    private String checkInputLength(String time)
    {
        //check to see if time is in hh:mm format
        if( (time.length() > 0 && time.length() < 3))
        {
            //add :00 if the time format is invalid
            time = time + ":00";
        }

        //return valid hh:mm format
        return time;
    }

    //calculate hours and minutes worked
    private int calculateTime(Date startTime, Date endTime){

        //calculate hours using unix time
        double unixTime = startTime.getTime() - endTime.getTime();
        double totalTime = (unixTime/1000)/60.0;
        double totalHours = Math.abs(totalTime/60.0);

        //calculate minutes
        double minutesInDecimal = 0;

        //check if there are any minutes or not
        if(totalHours % 1 != 0)
        {
            //get amount of minutes in decimal form
            minutesInDecimal =  totalHours % 1;

            //subtract minutes from hours
            totalHours = totalHours - minutesInDecimal;
        }

        //convert minutes into whole number
        double totalMinutes = minutesInDecimal * 60;

        //convert hours and minutes into integers
        totalHoursWorked = (int) totalHours;
        totalMinutesWorked = (int) Math.round(totalMinutes); //round to next whole number for precision

        //return hours and minutes worked
        return totalMinutesWorked & totalHoursWorked;
    }

    private void displayOutput() {

        clockIn = (TextView) findViewById(R.id.clockIn);
        clockOut = (TextView) findViewById(R.id.clockOut);
        hoursWorked = (TextView) findViewById(R.id.hoursWorked);

        Spinner start = (Spinner) findViewById(R.id.startTimeSpinner);
        Spinner end = (Spinner) findViewById(R.id.endTimeSpinner);

        EditText clockInTime = (EditText) findViewById(R.id.clockInTime);
        EditText clockOutTime = (EditText) findViewById(R.id.clockOutTime);

        //retrieve times entered
        timeIn = clockInTime.getText().toString() + " "+ String.valueOf(start.getSelectedItem());
        timeOut = clockOutTime.getText().toString() + " "+ String.valueOf(end.getSelectedItem());

        //only show output if both start and end times are entered by user
        if(!userInputEmpty())
        {
            clockIn.setText("Start time: " + timeIn);
            clockOut.setText("End time: " + timeOut);
            hoursWorked.setText("You have worked for " + totalHoursWorked + " hours and " + totalMinutesWorked +" minutes.");

        }
    }

    //checks to see if user input is empty or not
    private boolean userInputEmpty()
    {
        EditText clockInTime = (EditText) findViewById(R.id.clockInTime);
        EditText clockOutTime = (EditText) findViewById(R.id.clockOutTime);

        if(clockInTime.getText().toString().length() != 0 && clockOutTime.getText().toString().length() != 0){
            return false;
        }

        //return true if text boxes are empty
        return true;
    }

    //resets the start and end times inputted by the user after the submit button is clicked
    private void reset()
    {
        EditText startTime = (EditText) findViewById(R.id.clockInTime);
        EditText endTime = (EditText) findViewById(R.id.clockOutTime);

        //reset current input values once submit button is clicked
        startTime.setText("");
        endTime.setText("");

        //move cursor back to the first text box
        startTime.requestFocus();

    }


}
