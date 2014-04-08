/**
 * 
 */
package com.tracer.presenter;

import java.io.File;

public interface TracerPresenterInterface {

    void open(String p_xmlPath);
    void clickedOnAbout();
    void clickOnSettings();
    void clickedOnLoadProfile();
    void clickedOnLoadTrace();
    void clickedOnProfile();
    void clickedOnTrace();
    void clickedExit();
    void editArguments();
    boolean isExecutableJar(File p_file);
    void selectedPath(String p_file_selected);

}
