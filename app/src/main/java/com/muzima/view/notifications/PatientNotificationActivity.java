/*
 * Copyright (c) The Trustees of Indiana University, Moi University
 * and Vanderbilt University Medical Center. All Rights Reserved.
 *
 * This version of the code is licensed under the MPL 2.0 Open Source license
 * with additional health care disclaimer.
 * If the user is an entity intending to commercialize any application that uses
 *  this code in a for-profit venture,please contact the copyright holder.
 */

package com.muzima.view.notifications;

import android.content.Intent;
import android.os.Bundle;
import com.muzima.R;
import com.muzima.adapters.MuzimaPagerAdapter;
import com.muzima.adapters.notification.PatientNotificationPagerAdapter;
import com.muzima.api.model.Patient;
import com.muzima.utils.ThemeUtils;
import com.muzima.view.patients.PatientSummaryActivity;

public class PatientNotificationActivity extends NotificationActivityBase {
    private Patient patient;
    private final ThemeUtils themeUtils = new ThemeUtils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        themeUtils.onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_pager);
        Intent intent = getIntent();
        patient = (Patient) intent.getSerializableExtra(PatientSummaryActivity.PATIENT);
        initPager();
        initPagerIndicator();
        getSupportActionBar().setTitle(patient.getSummary());
        logEvent("VIEW_PATIENT_NOTIFICATIONS","{\"patientuuid\":\""+patient.getUuid()+"\"}");
    }

    @Override
    protected void onResume() {
        super.onResume();
        themeUtils.onResume(this);
    }

    @Override
    protected MuzimaPagerAdapter createNotificationsPagerAdapter() {
        return new PatientNotificationPagerAdapter(getApplicationContext(), getSupportFragmentManager(), patient);
    }
}
