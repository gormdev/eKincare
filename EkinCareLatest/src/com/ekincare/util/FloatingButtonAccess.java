package com.ekincare.util;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.oneclick.ekincare.vo.Medication;

/**
 * Created by RaviTejaN on 04-10-2016.
 */

public interface FloatingButtonAccess {

    public FloatingActionButton getAllergyFab();
    public FloatingActionsMenu getMedicationFab();
    public FloatingActionsMenu getDocumentFab();
    public FloatingActionsMenu getMedicalHistory();
    public FloatingActionButton getProfileFab();
}
