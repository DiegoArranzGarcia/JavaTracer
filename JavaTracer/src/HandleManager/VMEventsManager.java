package HandleManager;

import DataBase.DataBaseWriter;
import DataBase.InterfaceInfo;

/**
 * When a new type of event has to be register, the manager must extends this class.  
 */
public class VMEventsManager {
	
	private DataBaseWriter dataBaseWriter;
	private boolean enableOutput;

	/**
	 * This constructor sets by default not to show any outputs.
	 */
	public VMEventsManager(){
		this.enableOutput = false;
	}
	
	/**
	 * This constructor allows to enable or disable the output. </br>
	 * It will use the default System.out.
	 * @param enableOutput - True if want to be enabled, false for disabled.
	 */
	public VMEventsManager(boolean enableOutput){
		this.enableOutput = enableOutput;
	}
	
	/**
	 * This constructor allows to set the DataBaseWriter to write the output.</br>
	 * By default, it'll be enabled.
	 * @param dataBaseWriter - The DataBaseWriter to user.
	 */
	public VMEventsManager(DataBaseWriter dataBaseWriter){
		this.enableOutput = true;
		this.dataBaseWriter = dataBaseWriter;
	}
	
	/**
	 * @return the DataBaseWriter.
	 */
	public DataBaseWriter getDataBaseWriter() {
		return dataBaseWriter;
	}

	/** 
	 * @return True if enabled, false if disabled.
	 */
	public boolean isEnableOutput() {
		return enableOutput;
	}

	/**
	 * @param DataBaseWriter the DataBaseWriter to set
	 */
	public void setDataBaseWriter(DataBaseWriter dataBaseWriter) {
		this.dataBaseWriter = dataBaseWriter;
	}

	/**
	 * @param enableOutput the enableOutput to set
	 */
	public void setEnableOutput(boolean enableOutput) {
		this.enableOutput = enableOutput;
	}

	/**
	 * Writes the output. It'll be displaying depending on the params that have been set.</br>
	 * If enableOutput it's false, no output will be displayed. </br>
	 * Else if the DataBaseWriter it's set it will be displayed as the DataBaseWriter do.
	 * if it's null System.out will be used. 
	 * @param output - The output that will be displayed.
	 */
	public void writeOutput(InterfaceInfo output){
		if (enableOutput){
			if (dataBaseWriter!=null) dataBaseWriter.writeOutput(output);
			else System.out.println(output.toString());
		}
	}
	
}
