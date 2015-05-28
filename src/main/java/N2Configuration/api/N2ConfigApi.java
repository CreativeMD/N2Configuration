package N2Configuration.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.FMLInjectionData;

/**
 * An simple Configuration API
 * @author N247S
 *
 */
public class N2ConfigApi
{
	public static HashMap<String, File> DirectoryList = new HashMap<String, File>();
	public static HashMap<File, File> TempDirectoryList = new HashMap<File, File>();
	public static Logger log = LogManager.getLogger("N2ConfigAPI");

	/**
	 * This will register a Directory where you can store your (Config)Files.
	 * 
	 * @param directoryPath - This is the path inside the Minecraft default ConfigDirectory.
	 * 	(Illegal DirectoryNames: "/temp")
	 * @param directoryID - With this ID you can retrieve the Instance of the Directory. 
	 * @return - File(Directory)
	 */
	public static File registerCustomConfigDirectory(String directoryPath, String directoryID)
	{
		if(DirectoryList.isEmpty())
		{
			File defaultFile = new File(getMCMainDir(), "config");
			DirectoryList.put("Default", defaultFile);
			TempDirectoryList.put(defaultFile, (new File(getConfigDir(), "temp")));
		}
		
		File newDir = new File(getConfigDir(), directoryPath);
		File newTempDir = new File(getConfigDir(), (directoryPath + "/temp"));
		
		Iterator directory = DirectoryList.keySet().iterator();
		while(directory.hasNext())
		{
			File currentDirectory = (File) (DirectoryList.get(directory.next()));
			if(currentDirectory.getPath().equals(directoryPath))
				throw new IllegalArgumentException("DirectoryPath " + directoryPath + " already exist!");
		}
		
		if(directoryID != null)
			DirectoryList.put(directoryID, newDir);
		else throw new NullPointerException();
		
		TempDirectoryList.put(newDir, newTempDir);
		newTempDir.mkdirs();
		return newDir;
	}
	
	/**
	 * This will register a Directory where you can store your (Configuration)Files.
	 * 
	 * @param ParentDirectory - Where this Directory will be created.
	 * Note, the parentDirectory MUST exist for the auto generate funcions to work!
	 * If you want to create multiple 'nested' Directory's, add them to the directoryPath. ("folder1/folder2/finalfolder")
	 * @param directoryPath - This is the path inside the ParentDirectory.
	 * 	(Illegal DirectoryNames: "/temp")
	 * @param directoryID - With this ID you can retrieve the Instance of the Directory.
	 * @return - File(Directory)
	 */
	public static File registerCustomDirectory(File ParentDirectory, String directoryPath, String directoryID)
	{
		if(DirectoryList.isEmpty())
		{
			File defaultFile = new File(getMCMainDir(), "config");
			DirectoryList.put("Default", defaultFile);
			TempDirectoryList.put(defaultFile, (new File(getConfigDir(), "temp")));
		}
		
		File newDir = new File(ParentDirectory, directoryPath);
		File newTempDir = new File(ParentDirectory, (directoryPath + "/temp"));
		
		Iterator directory = DirectoryList.keySet().iterator();
		while(directory.hasNext())
		{
			File currentDirectory = (File) (DirectoryList.get(directory.next()));
			if(currentDirectory.getPath().equals(directoryPath))
				throw new IllegalArgumentException("DirectoryPath " + directoryPath + " already exist!");
		}
		
		if(directoryID != null)
			DirectoryList.put(directoryID, newDir);
		else throw new NullPointerException();
		
		TempDirectoryList.put(newDir, newTempDir);
		return newDir;
	}
	
	/**
	 * @return - The Minecraft MainFileDirectory (don't use this for "registerCustomDirectory()", it will be added Automatically)
	 */
	public static File getMCMainDir()
	{
		return (File)FMLInjectionData.data()[6];
	}
	
	/**
	 * @return - The Minecraft configDirectory (".../config")
	 */
	public static File getConfigDir()
	{
		if(DirectoryList.isEmpty())
		{
			File defaultFile = new File(getMCMainDir(), "config");
			DirectoryList.put("Default", defaultFile);
			TempDirectoryList.put(defaultFile, (new File(getConfigDir(), "temp")));
		}
		return getFileDirFromID("Default");
	}
	
	/**
	 * @param directory
	 * @return - The names of all the Files with the extension '.cfg' inside this Directory.
	 */ 
	public static String[] getDirContentConfigNames(File directory)
	{
		String[] totalList = getDirContentNames(directory);
		List<String> finalList = new ArrayList<String>();
		for(int i = 0; i < totalList.length; i++)
		{
			String currentName = totalList[i];
			if(currentName.endsWith(".cfg"))
				finalList.add(currentName.substring(0, currentName.length()-4));
		}
		
		return (String[]) finalList.toArray(new String[finalList.size()]);
	}
	
	/**
	 * @param directory
	 * @return - The names of all the Files(and Directory's) inside this Directory
	 */
	public static String[] getDirContentNames(File directory)
	{
		return directory.list();
	}
	
	/**
	 * @param directoryID
	 * @return - File(Directory)
	 */
	public static File getFileDirFromID(String directoryID)
	{
		return DirectoryList.get(directoryID);
	}
	
	/**
	 * @param FileDirectory
	 * @return - The IDName of the FileDirectory
	 */
	public static String getFileIDFromFileDir(File FileDirectory)
	{
		Iterator<String> fileID = DirectoryList.keySet().iterator();
		
		while(fileID.hasNext())
		{
			if(DirectoryList.get(fileID).equals(FileDirectory))
				return fileID.next();
		}
		return null;
	}
	
	/**
	 * Retrieve the 'temp' Directory which is registered with the Directory
	 * @param directory
	 * @return - File(Directory)
	 */
	public static File getTempDir(File directory)
	{
		return TempDirectoryList.get(directory);
	}
}
