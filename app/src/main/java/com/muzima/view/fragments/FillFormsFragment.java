/*
 * Copyright (c) The Trustees of Indiana University, Moi University
 * and Vanderbilt University Medical Center. All Rights Reserved.
 *
 * This version of the code is licensed under the MPL 2.0 Open Source license
 * with additional health care disclaimer.
 * If the user is an entity intending to commercialize any application that uses
 * this code in a for-profit venture, please contact the copyright holder.
 */

package com.muzima.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muzima.MuzimaApplication;
import com.muzima.R;
import com.muzima.adapters.forms.ClientSummaryFormsAdapter;
import com.muzima.api.model.Patient;
import com.muzima.controller.PatientController;
import com.muzima.model.DownloadedForm;
import com.muzima.tasks.FormsLoaderService;
import com.muzima.view.forms.FormViewIntent;
import com.muzima.view.forms.FormsWithDataActivity;

import java.util.ArrayList;
import java.util.List;

import static com.muzima.view.relationship.RelationshipsListActivity.INDEX_PATIENT;

public class FillFormsFragment extends Fragment implements FormsLoaderService.FormsLoadedCallback, ClientSummaryFormsAdapter.OnFormClickedListener {

    private RecyclerView formsRecyclerView;
    private ClientSummaryFormsAdapter formsAdapter;
    private Patient patient;
    private String patientUuid;
    private List<DownloadedForm> forms = new ArrayList<>();

    public FillFormsFragment(String patientUuid) {
        this.patientUuid = patientUuid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fill_forms, container, false);
        initializeResources(view);
        loadData();
        return view;
    }

    private void loadData() {
        ((MuzimaApplication) getActivity().getApplicationContext()).getExecutorService()
                .execute(new FormsLoaderService(getActivity().getApplicationContext(), this));
    }

    private void initializeResources(View view) {
        formsRecyclerView = view.findViewById(R.id.fragment_fill_forms_recycler_view);
        formsAdapter = new ClientSummaryFormsAdapter(forms, this);
        formsRecyclerView.setAdapter(formsAdapter);
        formsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        try {
            patient = ((MuzimaApplication) getActivity().getApplicationContext()).getPatientController().getPatientByUuid(patientUuid);
        }catch (PatientController.PatientLoadException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onFormsLoaded(final List<DownloadedForm> formList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                forms.addAll(formList);
                formsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFormClickedListener(int position) {
        DownloadedForm form = forms.get(position);
        Intent intent = new FormViewIntent(getActivity(), form, patient , false);
        intent.putExtra(INDEX_PATIENT, patient);
        getActivity().startActivityForResult(intent, FormsWithDataActivity.FORM_VIEW_ACTIVITY_RESULT);
    }
}