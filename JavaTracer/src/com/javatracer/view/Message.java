package com.javatracer.view;

public class Message {
	
	private String message;
	
	
	public Message(int id){
		
		
			
		switch (id) {
					
					case 1:
						this.message = "Select the directory ";
					break;
					
					case 2:
						this.message = "You should choose the directory where are all files .class";
					break;
					
					case 3:
						this.message = "Insert the name of Main class";
					break;
					
					case 4:
						this.message ="Name of main class, if your main class there is in a package you should insert the name of the package";
					break;
					
					case 5:
						this.message = "Insert xml name";
					break;
					
					case 6:
						this.message = "Filename without extension";
					break;
					
					case 7:
						this.message = " already exists, are you sure you want to overwrite the current file?";
					break;
					
					case 8:
						this.message = "Overwrite current file";
					break;
					
					case 9:
						this.message = "White text area";
					break;
					
					case 10:
						this.message = "This class must contain the method: public" 
							       +"static void main(String[] args)";
					break;
					
					case 11:
						this.message = "was not found or loaded main class";
					break;
					
					case 12:
						this.message = "JavaTracer has finished. You can see the trace at the file created in the same directory that you "
								+ "	launched ";
					break;
					
					case 13:
						this.message = "The file name is invalid, should not lead extension "
								+ "	launched ";
					break;

					
					default:
						break;
					}
		
		
	}
	
	public String getMessage() {
		return message;
	}
	
	

}
