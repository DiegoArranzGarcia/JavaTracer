package com.profiler.view;

import java.util.HashMap;

import com.profiler.model.ProfilerTree;
import com.profiler.presenter.ProfilerPresenterInterface;

public interface ProfilerViewInterface {

	public void load(ProfilerTree currentProfileTree);
	
	public void setPresenter(ProfilerPresenterInterface presenter);
	public HashMap<String,Boolean> getDataState();
	public void setVisible(boolean b);


}
