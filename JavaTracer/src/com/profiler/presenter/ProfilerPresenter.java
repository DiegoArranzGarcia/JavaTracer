package com.profiler.presenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.general.model.FileUtilities;
import com.general.model.configuration.JavaTracerConfiguration;
import com.general.presenter.JavaTracerPresenter;
import com.profiler.model.Profiler;
import com.profiler.model.ProfilerModelInterface;
import com.profiler.model.ProfilerTree;
import com.profiler.model.data.ExcludedClassesMethods;
import com.profiler.model.data.ProfileClass;
import com.profiler.model.data.ProfileData;
import com.profiler.model.data.ProfileMethod;
import com.profiler.model.data.ProfilePackage;
import com.profiler.view.ProfilerView;
import com.profiler.view.ProfilerViewInterface;
import com.profiler.view.ProfilerViewWithoutSplit;

public class ProfilerPresenter implements ProfilerPresenterInterface {
	
	private static final String XML_FILTER_FILES = "XML Files (*.xml)";
	private static final String XML_EXT = "xml";
	private static final String OPEN_PROFILE = "Open profile";
	
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
		
	public void showProfile(String profileFile){
		
		if (view == null){
			view = new ProfilerView();
			view.setPresenter(this);
			/*view.load(currentProfileTree.getClasses(),currentProfileTree.getNumCalls());
			view.setVisible(true);
			view.setTitle(profileFile);*/
		}
		
		view.load(currentProfileTree.getClasses(),currentProfileTree.getNumCalls());
		view.setVisible(true);
		view.setTitle(profileFile);
		//controller.setVisible(false);
	}
	
	public void loadTempProfile(){
		currentProfileTree = profiler.getProfileTree();
	}

	public void save() {
		JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
		HashMap<String, Boolean> classes = view.getDataState();
		Iterator<Entry<String,Boolean>> iterator = classes.entrySet().iterator();
		
		ExcludedClassesMethods excludedMethods = configuration.getExcludeClassMethods();
		List<String> excludesData = configuration.getExcludesList();
		
		while (iterator.hasNext()){
			Entry<String,Boolean> entry = iterator.next();
			String keys = entry.getKey();
			ProfileData data = profiler.getData(keys);
			if (entry.getValue()){
				if (data instanceof ProfilePackage){
					String excludePackage = data.getCompleteName() + ".*";
					if (!excludesData.contains(excludePackage))
						excludesData.add(excludePackage);
				} else if (data instanceof ProfileClass){
					if (!excludesData.contains(data.getCompleteName()))
						excludesData.add(data.getCompleteName());
				} else if (data instanceof ProfileMethod){
					ProfileMethod method = (ProfileMethod)data;
					excludedMethods.addMethod(method.getParentCompleteName(),method.getCompleteName());
				}
			} else {
				if (data instanceof ProfilePackage){
					String excludePackage = data.getCompleteName() + ".*";
					excludesData.remove(excludePackage);
				} else if (data instanceof ProfileClass){
					excludesData.remove(data.getCompleteName());
				} else if (data instanceof ProfileMethod){
					ProfileMethod method = (ProfileMethod)data;
					excludedMethods.removeMethod(method.getParentCompleteName(),method.getCompleteName());
				}
			}
		}
		
		configuration.saveConfiguration(JavaTracerConfiguration.getInstance().getNameActualXML());
	}

	public void cancel() {
		view.setVisible(false);
		//controller.back();
	}

	public void openProfile(File file) {
		currentProfileTree = profiler.openProfile(file);
		showProfile(file.getName());
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

	public void doubleClick(String completeName) {
		
		ArrayList<String>  futureNode=new ArrayList<String>();
		
		while(completeName.contains(".")){
			
			futureNode.add(completeName.substring(0, completeName.indexOf(".")));
			completeName=completeName.substring(completeName.indexOf(".")+1, completeName.length());
			
		}
		futureNode.add(completeName);
		
		
		ProfileData node=profiler.getProfileTree().getData(futureNode);
	
		if(node!=null){	
		
				int isClass=profiler.getProfileTree().foundClass(node.getParent(),completeName);
				if(isClass!=-1){
					HashMap<String,Integer> methodsInfo=new HashMap<String,Integer>();
					List<ProfileData> methods=node.getChildren();
					int i=0;
					int numCalledMethods=0;
					while(i<methods.size()){
						numCalledMethods=numCalledMethods+methods.get(i).getNumCalls();
						methodsInfo.put(methods.get(i).getName(), methods.get(i).getNumCalls());	
						i++;	
						}
			
					ProfilerViewWithoutSplit methodProfiler=new ProfilerViewWithoutSplit(node.getCompleteName());
					methodProfiler.load(methodsInfo, numCalledMethods);
					methodProfiler.setVisible(true);
			
				}
		
			}
	}

	public void clickedOnOpenProfile() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(XML_FILTER_FILES, XML_EXT));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle(OPEN_PROFILE);
		chooser.setCurrentDirectory(new File(FileUtilities.CURRENT_DIR));
		chooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
		//return directory file
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				String path = chooser.getSelectedFile().getCanonicalPath();
				openProfile(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { 
			chooser.cancelSelection();
		}
		
	}

	public void clickedOnSettings() {
		controller.clickedOnSettings();
	}

	public void clickedOnAbout() {
		controller.clickedOnAbout();
	}

}
