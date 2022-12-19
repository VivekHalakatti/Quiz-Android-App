package com.example.dummyquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryPage_fragment extends Fragment {
    TextView timetaken_textview, correct_answers;
    TimerViewModel timerViewModel;
    Button btn, restart_button;
    HashMap<Integer, String> sum_map = (HashMap<Integer, String>) QuestionDetails_fragment.map;
    int timetaken;
    int correct_a = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summary_fragment, container, false);
        timetaken_textview = view.findViewById(R.id.timetaken_summary_textview);
        correct_answers = view.findViewById(R.id.Correctly_answered_textview_summary);
        timerViewModel = new TimerViewModel();
        timetaken = timerViewModel.getSecondsforSummary();

        //to calculate the number of correct answers after submission
        for (int i = 0; i < MyAdapter.dataholder.size(); i++) {
            Datamodel datamodel = MyAdapter.dataholder.get(i);
            String answer = datamodel.getExact_ans();
            if (sum_map.get(i) == null) {
                continue;
            }else if(sum_map.get(i).equals(answer)){
                correct_a++;
            }
        }

        correct_answers.setText("Answered correctly : " + String.valueOf(correct_a));
        timetaken_textview.setText("Time taken : " + String.valueOf(250 - timetaken) + " seconds");

        btn = (Button) view.findViewById(R.id.exit_button);
        restart_button = (Button) view.findViewById(R.id.Restart_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        restart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment3 = new SetupPage_fragment();
                getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment3).commit();
                correct_a = 0;
                QuestionDetails_fragment.map = new HashMap<>();
                QuestionsFragment.start=true;
            }
        });
        return view;
    }
}
