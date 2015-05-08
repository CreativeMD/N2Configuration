package N2Configuration.api;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.FMLInjectionData;

/**
 * An simple Configuration API
 * @author N247S
 *
 */
public class N2Configuration
{
	public static File configFolder = new File(getFolderDir(), "config/mechanical_crafing_table");
	public static HashMap<String, File> DirectoryList = new HashMap<String, File>();
	public static Logger log = LogManager.getLogger("N2ConfigAPI");

	/**
	 * This will register a Directory where you can store your ConfigurationFiles.
	 * @param directoryPath - This is the path inside the Minecraft default Directory.
	 * In order to store your ConfigurationFile into ".../config" folder, your input should be "config"
	 * @param directoryID - With this ID you can retrieve the Instance of your Directory.
	 * @return - File(Directory)
	 */
	public static File registerDirectory(String directoryPath, String directoryID)
	{
		File newDir = new File(getFolderDir(), directoryPath);
		File newTempDir = new File(getFolderDir(), (directoryPath + "/temp"));
		
		Iterator directory = DirectoryList.keySet().iterator();
		while(directory.hasNext())
		{
			File currentDirectory = (File) directory.next();
			if(currentDirectory.getPath() == directoryPath && directoryPath != "config")
				throw new IllegalArgumentException("DirectoryPath " + directoryPath + " already exist!");
		}
		
		DirectoryList.put(directoryID, newDir);
		DirectoryList.put((directoryID + "temp"), newTempDir);
		return newDir;
	}
	
	public static File getFolderDir()
	{
		return (File)FMLInjectionData.data()[6];
	}
	
	/**
	 * This will get the File from its directoryID.
	 * @param directoryID
	 * @return File(Directory)
	 */
	public static File getFileFromID(String directoryID)
	{
		return DirectoryList.get(directoryID);
	}
	
	/**
	 * This will get the 'temp' Directory which is generated with the usual Directory
	 * @param directory
	 * @return File(Directory)
	 */
	public static File getTempDirectory(File directory)
	{
		if(!directory.getPath().endsWith("temp"))
		{
			return getFileFromID(directory.getName()+ "temp");
		}
		else log.error("Couldn't get the Temporary Directory from Directory: " + directory.getAbsolutePath());
		return null;
	}
}
