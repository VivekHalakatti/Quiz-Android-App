package com.example.dummyquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuestionDetails_fragment extends Fragment {
//    static int correct_counter;
    static Map<Integer, String> map = new HashMap<>();
    int position;
    Button next_button, prev_button;
    TextView que;
    List<Datamodel> list;
    Datamodel datamodel;
    RadioGroup radioGroup;
    RadioButton opt1, opt2, opt3, opt4;
    List<String> options_list;
    ToggleButton button_t;
    int selectedId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.questions_details, container, false);
        button_t = view.findViewById(R.id.toggleButton);
        que = view.findViewById(R.id.question_detail);
        list = questionslist_frag.questions_list;
        position = questionslist_frag.pos;
        datamodel = list.get(position);
        que.setText(datamodel.getQuestion());
        options_list = datamodel.getOptions();

        //Initializing radiobuttons
        opt1 = view.findViewById(R.id.option_1);
        opt2 = view.findViewById(R.id.option_2);
        opt3 = view.findViewById(R.id.option_3);
        opt4 = view.findViewById(R.id.option_4);
        opt1.setText(options_list.get(0));
        opt2.setText(options_list.get(1));
        opt3.setText(options_list.get(2));
        opt4.setText(options_list.get(3));

        //Initializing radiogroup
        radioGroup = view.findViewById(R.id.radiogroup);

        //Initializing next and previous buttons
        next_button = view.findViewById(R.id.next_detail_button);
        prev_button = view.findViewById(R.id.previous_detail_button);

        //Setting onclicklisteners for above initialized button buttons, toggles and radiogroup.
        //Adding listener to toggle
        button_t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    datamodel.setBookmark(true);
                    button_t.setText("Saved!");
                } else {
                    datamodel.setBookmark(false);
                    button_t.setText("Bookmark?");
                }
            }
        });
        button_t.setChecked(datamodel.isBookmark());
//        Toast.makeText(getContext(),"Answer: "+String.valueOf(datamodel.getCorrect_option()),Toast.LENGTH_SHORT).show();
        if (position == list.size() - 1) {
            next_button.setVisibility(View.INVISIBLE);
        }

        if (position == 0) {
            prev_button.setVisibility(View.INVISIBLE);
        }

        //Setting listener to next button
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.clearCheck();
                prev_button.setVisibility(View.VISIBLE);
                position = position + 1;
                if (position != list.size()) {
                    datamodel = list.get(position);
                    que.setText(datamodel.getQuestion());
                    options_list = datamodel.getOptions();
                    opt1.setText(options_list.get(0));
                    opt2.setText(options_list.get(1));
                    opt3.setText(options_list.get(2));
                    opt4.setText(options_list.get(3));
                }
                if (position >= list.size() - 1) {
                    next_button.setVisibility(View.INVISIBLE);
                }
                radioGroup.check(datamodel.getButtonID());
                button_t.setChecked(datamodel.isBookmark());
            }
        });

        //setting listener to previous(prev) button
        prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId() != -1) {
                    radioGroup.clearCheck();
                }
                next_button.setVisibility(View.VISIBLE);
                position = position - 1;
                if (position >= 0) {
                    datamodel = list.get(position);
                    que.setText(datamodel.getQuestion());
                    options_list = datamodel.getOptions();
                    opt1.setText(options_list.get(0));
                    opt2.setText(options_list.get(1));
                    opt3.setText(options_list.get(2));
                    opt4.setText(options_list.get(3));
                }
                if (position <= 0) {
                    prev_button.setVisibility(View.INVISIBLE);
                }
                radioGroup.check(datamodel.getButtonID());
                button_t.setChecked(datamodel.isBookmark());
            }
        });

        //setting listener to radiogroup
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                datamodel.setAnswered(true);
                if (i != -1) {

                    //finding selected radiobutton by id
                    Log.d("debugger1", "inside onchecked");
                    RadioButton radioButton = view.findViewById(i);

                    //fetching the text from the radiobutton selection
                    String checker = (String) radioButton.getText();

                    //Setting radiobutton checkers according to selection
                    selectedId = radioGroup.getCheckedRadioButtonId();
                    datamodel.setButtonID(selectedId);

                    //Adding selected values to map
                    map.put(position, checker);
                    Log.d("debugger1", "Correct answer = " + map + " " + "position " + String.valueOf(position));
                }
            }
        });
        return view;
    }
}
