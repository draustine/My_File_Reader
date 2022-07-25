package com.draustine.myfilereader;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<String> template = new ArrayList<>();
    private final List<String> messageList = new ArrayList<>();
    private static BufferedReader beneficiaries = null;
    private TextView textView = null;
    private static final int PERMISSION_REQUEST = 101; //sms sending
    private Context context;
    private SmsManager smsManager;
    private TextView unitCost;
    private TextView totalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textView = findViewById(R.id.display);
        textView.setMovementMethod(new ScrollingMovementMethod());
        unitCost = findViewById(R.id.unitSmsCost);
        totalCost = findViewById(R.id.totalSmsCost);
    }



    public void send_messages(View view) throws IOException {

        smsManager = SmsManager.getDefault();
        int count;
        String phone;
        String message;
        count = messageList.size();
        if(count == 0) {
            composeMessages();
            count = messageList.size();
        }
        if(count != 0) {
            //textView.setText(String.valueOf(count));
            for(int i = 0; i < count; i ++){
                String member = messageList.get(i);
                String[] members = member.split("@");
                phone = members[0];
                message = members[1];

                sendTheMessage(phone, message);
                if(i == 0){
                    sendTheMessage("131", "EOCNOFF");
                    sendTheMessage("3036", "ACN OFF");
                }else if(i == count - 1){
                    sendTheMessage("131", "EOCNON");
                    sendTheMessage("3036", "ACN ON");
                }
            }
        }
        // Convert count to String and display it
        String countString = String.valueOf(count);
        Toast.makeText(this, countString + " Messages Sent", Toast.LENGTH_LONG).show();
    }


    private void getPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (!(permissionCheck == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST);
        }
    }



    private void sendTheMessage(String phone, String message){
        //SmsManager smsManager = context.getSystemService(SmsManager.class);

        smsManager.sendTextMessage(phone, null, message, null, null);

    }

    private void composeMessages()throws IOException{
        getTheReaders();

        Integer smsCount = 0;
        int smsLenght;
        int smsSize = 160;


        // Cycle through beneficiaries and extract ones with birthdays on the day
        int count = 0;
        int age;
        String ageOrd;
        //String cDate;
        String iDate;
        String txt ;
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue(), cMonth, fMonth = 0;
        int day   = localDate.getDayOfMonth(), cDay, fDay = 0;
        iDate = Numbers.getOrdinal(day) + " day of " + MonthName.getMonthName(month) + " " + year;

        String benLine;
        StringBuilder tempstr = new StringBuilder("Clients with birthday on " + iDate);
        //Cycle through the list of clients with upcoming birthdays
        int bCount = 0;
        while((benLine = beneficiaries.readLine()) != null){
            bCount++;
            String[] contents = benLine.split("@");
            if(bCount == 1){
                //Get the day and month file was composed
                fMonth =Integer.parseInt(contents[0]);
                fDay = Integer.parseInt(contents[1]);
            }else{
                //Split each line and check for equality of day and month
                cDay = Integer.parseInt(contents[3]);
                cMonth = Integer.parseInt(contents[2]);
                age = Integer.parseInt(contents[4]);
                if(!(cDay == fDay && cMonth == fMonth)){
                    age = age + 1;
                }
                ageOrd = Numbers.getOrdinal(age);

                //cDate = Numbers.getOrdinal(cDay) + " day of " + MonthName.getMonthName(cMonth) + " " + String.valueOf(year);
                if(cDay == day && cMonth == month) {
                    count++;
                    txt = template.get(0) + " " + contents[0] + template.get(1) + "\n";
                    txt = txt + template.get(2) + " " + ageOrd + " " + template.get(3) + "\n";
                    txt = txt + template.get(4) + "\n" + template.get(5) + "\n" + template.get(6);
                    tempstr.append("\n").append("\n").append(count).append("\n").append(txt);
                    //Add to the messaging list
                    messageList.add(contents[1] + "@" + txt);
                    smsLenght = txt.length();
                    if(smsLenght <= smsSize){
                        smsCount = smsCount + 1;
                    }else if(smsLenght % smsSize > 0){
                        smsCount = smsCount + 1 + (int)(smsLenght/smsSize);
                    }else{
                        smsCount = smsCount + (int)(smsLenght/smsSize);
                    }
                }

            }

        }
        int smsUnitCost = Integer.parseInt(unitCost.getText().toString());
        String smsCost = "N" + String.valueOf(smsUnitCost * smsCount);
        totalCost.setText(smsCost);
        textView.setText(tempstr.toString());
    }

    private InputStream getDropBoxFile() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new URL("https://www.dropbox.com/s/7rxxn2vt9eceqbs/Upcoming%20Birthdays.txt?dl=0").openConnection().getInputStream();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    private InputStream getTheFile(int path) throws Resources.NotFoundException {
        InputStream thisInput = null;
        try {
            thisInput = getResources().openRawResource(path);


        }catch(Resources.NotFoundException rnf){
            rnf.printStackTrace();
        }
        return thisInput;
    }


    private void getTheReaders() {
        InputStream mInput;
        InputStream cInput;
        try {
            //get the reader for list of celebrants
            cInput = getTheFile(R.raw.upcoming_birthdays);
            //cInput = getDropBoxFile();


            InputStreamReader isr = new InputStreamReader(cInput);
            beneficiaries = new BufferedReader(isr);


            // Get the reader for message template
            BufferedReader messageTemplate;
            mInput = getTheFile(R.raw.message);
            InputStreamReader mtr = new InputStreamReader(mInput);
            messageTemplate = new BufferedReader(mtr);
            String line;
            while ((line = messageTemplate.readLine()) != null) {
                template.add(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }



    }

    public void compose_messages(View view) throws IOException {
        getPermission();
        composeMessages();
    }
}