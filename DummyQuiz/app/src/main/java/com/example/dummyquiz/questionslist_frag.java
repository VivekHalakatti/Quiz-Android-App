package com.example.dummyquiz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class questionslist_frag extends Fragment implements LifecycleOwner, MyAdapter.OnItemClickListener {

    private AlertDialog failureDialog;
    private ProgressDialog loadingDialog;
    private QuestionsViewModel viewModel;
    RecyclerView recyclerView;
    static List<Datamodel> questions_list;
    static int pos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_layout, container, false);
        recyclerView = view.findViewById(R.id.rec_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, getDefaultViewModelProviderFactory());
        viewModel = viewModelProvider.get(QuestionsViewModel.class);
        setUpLiveData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (failureDialog != null) {
            failureDialog.dismiss();
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void setUpLiveData() {
        viewModel.getQuestionData().observe(getViewLifecycleOwner(), new Observer<List<Datamodel>>() {
            @Override
            public void onChanged(List<Datamodel> questionModels) {
                handleQuestionsList(questionModels);
            }
        });
        viewModel.getRequestStatusLiveData().observe(getViewLifecycleOwner(), new Observer<QuestionsViewModel.RequestStatus>() {
            @Override
            public void onChanged(QuestionsViewModel.RequestStatus requestStatus) {
                handleRequestStatus(requestStatus);
            }
        });
    }

    private void handleRequestStatus(QuestionsViewModel.RequestStatus requestStatus) {
        switch (requestStatus) {
            case IN_PROGRESS:
                showSpinner();
                break;
            case SUCCEEDED:
                hideSpinner();
                break;
            case FAILED:
                showError();
                break;
        }
    }


    private void handleQuestionsList(List<Datamodel> questionModels) {
        questions_list = questionModels;
        MyAdapter adapter = new MyAdapter(questionModels, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void showSpinner() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(getActivity());
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setTitle("Fetching questions");
            loadingDialog.setMessage("Please wait...");
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
    }

    private void hideSpinner() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void showError() {
        hideSpinner();
        Toast.makeText(getContext(), "failed fetching", Toast.LENGTH_SHORT).show();
        if (failureDialog == null) {
            failureDialog = getFailureDialog();
        }
        failureDialog.show();
    }

    private AlertDialog getFailureDialog() {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Question fetch request failed")
                .setMessage("Question fetching is failed, do you want to retry?")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        viewModel.refetchQuestions();
                    }
                })
                .setNegativeButton("Close app", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadingDialog.setCanceledOnTouchOutside(true);
                        System.exit(0);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }

    @Override
    public void OnItemClicked(int adapterPosition) {
        Fragment fragment6 = new QuestionDetails_fragment();
        getParentFragmentManager().beginTransaction().replace(R.id.rec_container, fragment6).addToBackStack(null).commit();
        pos = adapterPosition;
    }
}
