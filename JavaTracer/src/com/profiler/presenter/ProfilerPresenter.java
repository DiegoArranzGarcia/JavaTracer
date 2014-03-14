package com.profiler.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.general.model.configuration.JavaTracerConfigurationXml;
import com.general.presenter.JavaTracerPresenter;
import com.profiler.model.Profiler;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.model.ProfilerTree;
import com.profiler.model.data.ExcludesClassMethods;
import com.profiler.model.data.ProfileClass;
import com.profiler.model.data.ProfileData;
import com.profiler.model.data.ProfileMethod;
import com.profiler.model.data.ProfilePackage;
import com.profiler.view.ProfilerView;
import com.profiler.view.ProfilerViewInterface;

public class ProfilerPresenter implements ProfilerPresenterInterface {
	
	private JavaTracerPresenter controller;
	
	private ProfilerModelInterface profiler;
	private ProfilerViewInterface view;
	
	private ProfilerTree currentProfileTree;
	
	public ProfilerPresenter(){
		this.profiler = new Profiler();
		this.currentProfileTree = new ProfilerTree();
	}
	
	public void setController(JavaTracerPresenter javaTracerController) {
		this.controller = javaTracerController;
	}
		
	public void showProfile(){
		
		if (view == null){
			view = new ProfilerView();
			view.setPresenter(this);
		}
		
		view.load(currentProfileTree);
		view.setVisible(true);
	}
	
	public void loadTempProfile(){
		currentProfileTree = profiler.getProfileTree();
	}

	public void save() {
		//JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
		JavaTracerConfigurationXml configuration = JavaTracerConfigurationXml.getInstance();
		HashMap<List<String>, Boolean> classes = view.getDataState();
		Iterator<Entry<List<String>,Boolean>> iterator = classes.entrySet().iterator();
		List<String> excludesData = new ArrayList<>();
		ExcludesClassMethods excludedMethods = new ExcludesClassMethods();
		
		while (iterator.hasNext()){
			Entry<List<String>,Boolean> entry = iterator.next();
			if (!entry.getValue()){
				List<String> keys = new ArrayList<String>(entry.getKey());
				ProfileData data = profiler.getData(keys);
				if (data instanceof ProfilePackage){
					String excludePackage = data.getCompleteName() + ".*";
					excludesData.add(excludePackage);
				} else if (data instanceof ProfileClass){
					excludesData.add(data.getCompleteName());
				} else if (data instanceof ProfileMethod){
					ProfileMethod method = (ProfileMethod)data;
					excludedMethods.addMethod(method.getParentCompleteName(),method.getCompleteName());
				}
			}
		}
		
		configuration.addExcludes(excludesData);
		configuration.addExcludesMethods(excludedMethods);
		view.setVisible(false);
		controller.back();
	}

	public void cancel() {
		view.setVisible(false);
		controller.back();
	}

	public void openProfile(File file) {
		currentProfileTree = profiler.openProfile(file);
		showProfile();
	}
	
	public void saveProfile(File file) {
		currentProfileTree.setCheckedClasses(view.getDataState());
		profiler.saveProfile(currentProfileTree, file);
	}
	
	public ProfilerViewInterface getView() {
		return view;
	}

	public ProfilerModelInterface getProfiler() {
		return profiler;
	}

	public void setView(ProfilerView view) {
		this.view = view;
	}

	public void setProfiler(Profiler profiler) {
		this.profiler = profiler;
	}

	public Iterator<Entry<String, Integer>> getClassesInfo() {		
		return profiler.getClassesInfo();
	}

	public ProfilerTree getTree() {
		return profiler.getProfileTree();
	}

	public void refresh() {
		//TODO
	}

}
