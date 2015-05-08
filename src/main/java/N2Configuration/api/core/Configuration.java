package N2Configuration.api.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;

import N2Configuration.api.N2Configuration;

public class Configuration
{
	public static HashMap<ConfigurationFile, File> fileList = new HashMap<ConfigurationFile, File>();
	public static File configFolder = new File(N2Configuration.getFolderDir(), "config/mechanical_crafing_table");
	public static File tempFolder = new File(N2Configuration.getFolderDir(), "config/mechanical_crafting_table/temp");
	public static Logger log = N2Configuration.log;
	
	public enum FileType
	{
		Original,
		BackUp,
		/**
		 * Don't use this until its really an temporary file!, otherwise use backup or clone instead!
		 */
		Temp,
		Clone,
	}

	/**
	 * This will register the a ConfigurationFile and will link an File (.cfg) to this ConfigurationFile.
	 * @param configurationFile
	 * @param fileName - This will be the name of the configuration file. (fileName.cfg)
	 * @param targetDirectory - This is the Directory where the ConfigurationFile should be created.
	 * If this File isn't a Directory, the Default Directory will be used!
	 * @throws IllegalArgumentException When the fileName already exist.
	 */
	protected static void registerConfigurationFile(ConfigurationFile configurationFile, String fileName, File targetDirectory)
	{
		if(targetDirectory.isDirectory())
		{
			File newFile = new File(targetDirectory, (fileName += ".cfg"));
			Iterator file = fileList.keySet().iterator();
			
			while(file.hasNext())
			{
				File currentFile = (File) file.next();
				if(currentFile.getName() == fileName)
					throw new IllegalArgumentException("Filename " + fileName + " already exist!");
			}
			
			if(!fileList.containsKey(configurationFile))
				fileList.put(configurationFile, newFile);
			else
			{
				log.warn("ConfigurationFile is already registered!");
			}
		}
	}
	
	/**
	 * This function will create a new, or check the existing files.
	 */
	public static void loadConfig()
	{
		try
		{
			generateAllFiles();
			Iterator<ConfigurationFile> file = fileList.keySet().iterator();
			if(file.hasNext())
			{
				ConfigurationFile current = file.next();
				current.checkConfigFile();
			}
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This will generate a new File. Not that it will override existing files (except the 'Clone' File)
	 * @param fileType
	 * @param configurationFile
	 * @param targetDirectory - This is the Directory where the ConfigurationFile should be created.
	 * @return This will return the instance of the created File.
	 * @throws IOException
	 */
	public static File generateSingleFile(FileType fileType, ConfigurationFile configurationFile, File targetDirectory) throws IOException
	{
		File target = null;
		if(targetDirectory != null)
			if(targetDirectory.isDirectory())
				target = targetDirectory;
		
		String originalFileName = configurationFile.getOriginalFileName();
		File file = null;
		switch(fileType)
		{
		case Original:
			file = fileList.get(configurationFile);
			break;
		case BackUp:
			if(targetDirectory == null)
				target = getFile(configurationFile).getParentFile();
			file = new File(target, originalFileName + "_BackUp.txt");
			break;
		case Temp:
			if(targetDirectory == null)
				target = N2Configuration.getTempDirectory(getFile(configurationFile).getParentFile());
			file = new File(target, originalFileName + "_Temp.tmp");if(targetDirectory == null)
			break;
		case Clone:
			if(targetDirectory == null)
				target = getFile(configurationFile).getParentFile();
			while(true)
			{
				int i = 1;
				file = new File(target, originalFileName + "_copy_" + i + ".cfg");
				if(file.exists()) i++;
				else break;
			}
			break;
		}
		if(file.exists())
			file.delete();
		file.createNewFile();
		return file;
	}
	
	/**
	 * Don't, just don't!
	 * @throws IOException
	 */
	private static void generateAllFiles() throws IOException
	{
		for(int i = 0; i < fileList.size(); i++)
		{
			File file = fileList.get(i);
			File dir = file.getParentFile();
			File dirtemp = N2Configuration.getTempDirectory(dir);
			
			dir.mkdirs();
			dirtemp.mkdirs();
			
			if(!file.exists())
				file.createNewFile();
		}
	}
	
	/**
	 * This will simply copy the content of one File, into the other.
	 * @param oldFile - File which you want to copy.
	 * @param copyFile - File which it should copy to.
	 * @throws IOException
	 */
	public static void copyFile(File oldFile, File copyFile) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(oldFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(copyFile));
		
		String readedLine = reader.readLine();
		
		while((readedLine = reader.readLine()) != null)
		{
			writer.append(readedLine);
			writer.newLine();
		}
		writer.flush();
		writer.close();
		reader.close();
	}
	
	/**
	 * This will return the File which is linked to the ConfigurationFile.
	 * @param configurationFile
	 * @return
	 */
	public static File getFile(ConfigurationFile configurationFile)
	{
		return fileList.get(configurationFile);
	}
}
