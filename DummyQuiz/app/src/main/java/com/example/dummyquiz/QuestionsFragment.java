package com.example.dummyquiz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class QuestionsFragment extends Fragment {

    private AlertDialog ConfirmationDialog;
    static Boolean start = true;
    TimerViewModel timerViewModel;
    Button btn;
    TextView timer;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Recyclerview that contains questions
        Fragment fragment5 = new questionslist_frag();
        getParentFragmentManager().beginTransaction().replace(R.id.rec_container, fragment5).commit();

        //Implementing timer
        timer = (TextView) view.findViewById(R.id.timer_display);
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        if(start) {
            timerViewModel.starttimer();
            start=false;
        }
        timerViewModel.getSeconds().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                timer.setText("Timer:- " + String.valueOf(integer));
                if(integer==0){
                    createSummaryFrag();
                }
            }
        });

        //Implementing submit button
        btn = (Button) view.findViewById(R.id.submit_test_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmationDialog = getConfirmationDialog();
                ConfirmationDialog.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        return view;
    }

    //Confirmation after pressing submit
    private AlertDialog getConfirmationDialog() {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Submit test")
                .setMessage("Are you sure you want to submit the test?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        createSummaryFrag();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }

    //Function that calls final summary page
    public void createSummaryFrag(){
        getParentFragmentManager().beginTransaction().replace(R.id.main_container, new SummaryPage_fragment()).commit();
    }
}