package com.profiler.view;

import java.util.HashMap;

import com.profiler.presenter.ProfilerPresenterInterface;

public interface ProfilerViewInterface {

	public void load(HashMap<String, Integer> classes, int numCalledMethods);
	public void setPresenter(ProfilerPresenterInterface presenter);
	public HashMap<String,Boolean> getDataState();
	public void setVisible(boolean b);
	public void setTitle(String profileFile);

}
