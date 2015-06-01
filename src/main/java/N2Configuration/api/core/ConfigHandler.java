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

import N2Configuration.api.N2ConfigApi;

/**
 * Copyright 2015 N247S
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

public class ConfigHandler
{
	public static HashMap<ConfigFile, File> fileList = new HashMap<ConfigFile, File>();
	public static Logger log = N2ConfigApi.log;
	
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
	 * This will register a ConfigurationFile. (.cfg)
	 * Note, when creating a new ConfigFile, it will automatically register itself!
	 * @param configFile
	 * @param fileName - This is the name what the ConfigFile will have. (fileName.cfg)
	 * @param targetDirectory - This is the Directory where the ConfigFile will go.
	 * 	If you want it to be in the default configDir, use "N2ConfigAPI.getConfigDir()"
	 */
	protected static void registerConfigurationFile(ConfigFile configFile, String fileName, File targetDirectory)
	{
		if(N2ConfigApi.DirectoryList.isEmpty())
		{
			File defaultFile = new File(N2ConfigApi.getMCMainDir(), "config");
			N2ConfigApi.DirectoryList.put("Default", defaultFile);
			N2ConfigApi.TempDirectoryList.put(defaultFile, (new File(N2ConfigApi.getConfigDir(), "temp")));
		}
		File newFile;
		if(targetDirectory.isDirectory())
			newFile = new File(targetDirectory, (fileName += ".cfg"));
		else newFile = new File(N2ConfigApi.getConfigDir(), (fileName += ".cfg"));
		
		Iterator file = fileList.keySet().iterator();
			
		while(file.hasNext())
		{
			File currentFile = (File) fileList.get(file.next());
			if(currentFile.getName() == fileName)
				throw new IllegalArgumentException("Filename " + fileName + " already exist!");
		}
		
		if(configFile != null)
		{
			if(!fileList.containsKey(configFile))
				fileList.put(configFile, newFile);
			else
			{
				log.warn("ConfigurationFile is already registered!");
			}
		}
		else throw new NullPointerException();
	}
	
	/**
	 * @param fileName
	 * @return true if the ConfigFile is removed, false otherwise.
	 */
	public static boolean removeConfigurationFile(String fileName)
	{
		ConfigFile configFile = getConfigFileFromName(fileName);
		
		if(configFile == null)
			return false;
		
		fileList.remove(getConfigFileFromName(fileName));
		return true;
	}
	
	/**
	 * This will create new, or check all existing files.
	 * Warning!, you really shouldn't call this if you don't know what you are doing!
	 * (Inaccessible for now, since there isn't a way to store files per ModInstance,
	 * this would only harm the performance when called.)
	 */
	private static void loadAndCheckAllConfigFiles()
	{
		try
		{
			Iterator DirectoryID = N2ConfigApi.DirectoryList.keySet().iterator();
			while(DirectoryID.hasNext())
			{
				File dir = N2ConfigApi.DirectoryList.get(DirectoryID.next());
				dir.mkdirs();
			}
			generateAllFiles();
			Iterator<ConfigFile> file = fileList.keySet().iterator();
			while(file.hasNext())
			{
				ConfigFile current = file.next();
				List<String> invalidSections = current.checkConfigFile();
				if(invalidSections != null)
					current.regenerateConfigFile(invalidSections);
			}
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This will create or check the configFile.
	 * @param configFileName
	 */
	public static void loadAndCheckConfigFile(String configFileName)
	{
		try
		{
			ConfigFile configFile = getConfigFileFromName(configFileName);
			if(configFile != null)
			{
				generateSingleFile(configFile);
			
				List<String> invalidSections = configFile.checkConfigFile();
				configFile.regenerateConfigFile(invalidSections);
			}
			else log.error("Couldn't find ConfigFile: " + configFileName);
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This will create the configFile.
	 * @param configFileName
	 */
	public static void loadConfigFile(String configFileName)
	{
		try
		{
			ConfigFile configFile = getConfigFileFromName(configFileName);
			if(configFile != null)
				generateSingleFile(configFile);
			else log.error("Couldn't find ConfigFile: " + configFileName);
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This will create or check all the configFiles in the StringArray.
	 * @param configFileNames
	 */
	public static void loadAndCheckConfigFileList(String[] configFileNames)
	{
		try
		{
			for(int i = 0; i < configFileNames.length; i++)
			{
				loadAndCheckConfigFile(configFileNames[i]);
			}
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This will create all the configFiles in the StringArray.
	 * @param configFileNames
	 */
	public static void loadConfigFileList(String[] configFileNames)
	{
		try
		{
			for(int i = 0; i < configFileNames.length; i++)
			{
				ConfigFile configFile = getConfigFileFromName(configFileNames[i]);
				if(configFile != null)
					generateSingleFile(configFile);
				else log.error("Couldn't find ConfigFile: " + configFileNames[i]);
			}
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This will create a single File. Note, if an File already exist, it will be overwritten! (except 'Clone' FileTypes)
	 * @param originalFileName - Don't include the FileExtension!
	 * @param extension
	 * @param fileType
	 * @param originalFile
	 * @param targetDirectory - The Directory where this File will be created
	 * @return - The new created File. Note, the 'original' fileType will return an empty File!
	 * @throws IOException
	 */
	public static File generateSingeFile(String originalFileName, FileType fileType, File originalFile, File targetDirectory) throws IOException
	{
		File target = null;
		if(targetDirectory != null)
			if(targetDirectory.isDirectory())
				target = targetDirectory;
			else log.error("File isn't generated. Couldn't resolve targetDirectory!");
		
		File newFile = null;
		
		switch(fileType)
		{
		case Original:
			newFile = originalFile;
			break;
		case BackUp:
			newFile = new File(target, originalFileName + "_BackUp.txt");
			break;
		case Temp:
			newFile = new File(target, originalFileName + "_Temp.tmp");
			break;
		case Clone:
			int i = 1;
			if(ConfigHandler.getConfigFileFromName(originalFileName) == null)
				newFile = new File(target, originalFileName);
			else
			{
				while(true)
				{
					newFile = new File(target, originalFileName + "_copy_" + i + originalFile.getName().substring(originalFileName.length(), originalFile.getName().length()));
					if(newFile.exists()) i++;
					else break;
				}
			}
			break;
		}
		if(newFile.exists())
			newFile.delete();
		newFile.createNewFile();
		if(fileType != FileType.Original)
			copyFile(originalFile, newFile);
		return newFile;
	}
	
	/**
	 * This will create a new ConfigFile. Note, it will override existing files (except 'Clone' FileTypes).
	 * @param fileType
	 * @param configFile
	 * @param targetDirectory - This is the Directory where the ConfigurationFile will be created.
	 * @return - The new created File. Note, the 'original' fileType will return an empty File!
	 * @throws IOException
	 */
	public static File generateSingleFileFromConfigFile(ConfigFile configFile, FileType fileType, File targetDirectory) throws IOException
	{
		return generateSingleFileFromConfigFile(null, configFile, fileType, targetDirectory);
	}
	
	/**
	 * This is mainly used for Clones with specific names.
	 * @param fileName
	 * @param fileType
	 * @param configFile
	 * @param targetDirectory - This is the Directory where the ConfigurationFile will be created.
	 * @return - The new created File. Note, the 'original' fileType will return an empty File!
	 * @throws IOException
	 */
	public static File generateSingleFileFromConfigFile(String fileName, ConfigFile configFile, FileType fileType, File targetDirectory)
	{
		File target = null;
		if(targetDirectory == null)
			target = getFileFromConfigFile(configFile).getParentFile();
		else target = targetDirectory;
		String originalFileName = configFile.getFileName();
		File originalFile = fileList.get(configFile);
		
		File file = null;
		ConfigFile newConfigurationFile = null;
		
		try
		{
			if(fileName != null)
				file = generateSingeFile(fileName, FileType.Original, originalFile, targetDirectory);
			else file = generateSingeFile(originalFileName, fileType, originalFile, targetDirectory);
		}
		catch(Exception e)
		{
			log.catching(e);
		}
		
		if(fileType.equals(FileType.Clone))
		{
			try
			{
				newConfigurationFile = (ConfigFile) configFile.CloneConfigurationFileInstance();
			}
			catch(Exception e)
			{
				log.catching(e);
			}
			
			if(fileName != null)
				newConfigurationFile.setFileName(fileName);
			else newConfigurationFile.setFileName(file.getName().substring(0, file.getName().length()-4));
			
			if(fileName != null)
				registerConfigurationFile(newConfigurationFile, fileName, targetDirectory);
			else registerConfigurationFile(newConfigurationFile, file.getName().substring(0, file.getName().length()-4), targetDirectory);
		}
		
		return file;
	}
	
	/**
	 * Don't, just don't!
	 * @throws IOException
	 */
	private static void generateAllFiles() throws IOException
	{
		Iterator i = fileList.keySet().iterator();
		
		while(i.hasNext())
		{
			ConfigFile currentConfigFile = (ConfigFile) i.next();
			if(currentConfigFile != null)
				{
				File file = getFileFromConfigFile(currentConfigFile);
				File dir = file.getParentFile();
				File dirtemp = N2ConfigApi.getTempDir(dir);
				
				dir.mkdirs();
				dirtemp.mkdirs();
				if(!file.exists())
					file.createNewFile();

				currentConfigFile.generateFile();

				if(!currentConfigFile.getIsWritten())
					currentConfigFile.writeAllSections();
			} 
		}
	}
	
	private static void generateSingleFile(ConfigFile configFile) throws IOException
	{
		if(configFile != null)
		{
			File file = getFileFromConfigFile(configFile);
			File dir = file.getParentFile();
			File dirtemp = N2ConfigApi.getTempDir(dir);
			
			dir.mkdirs();
			dirtemp.mkdirs();
			if(!file.exists())
				file.createNewFile();

			configFile.generateFile();
			
			if(!configFile.getIsWritten())
				configFile.writeAllSections();
		}
	}
	
	/**
	 * This will simply copy the content of one File, into the other.
	 * @param oldFile - File which you want to copy from.
	 * @param copyFile - File which it should copy to.
	 * @throws IOException
	 */
	public static void copyFile(File oldFile, File copyFile)
	{
		try
		{
			if(oldFile != null && copyFile != null)
			{
				BufferedReader reader = new BufferedReader(new FileReader(oldFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(copyFile));
				
				String readedLine;
				
				while((readedLine = reader.readLine()) != null)
				{
					writer.append(readedLine);
					writer.newLine();
					writer.flush();
				}
				writer.close();
				reader.close();
			}
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	public static File getFileFromConfigFile(ConfigFile configFile)
	{
		return fileList.get(configFile);
	}
		
	public static ConfigFile getConfigFileFromName(String configurationFileName)
	{
		Iterator<ConfigFile> currentFile = fileList.keySet().iterator();
		
		while(currentFile.hasNext())
		{
			ConfigFile file = currentFile.next();
			if(file.getFileName().equals(configurationFileName))
				return file;
		}
		return null;
	}
	
	public BufferedWriter getWriter(File file) throws IOException
	{
		return new BufferedWriter(new FileWriter(file));
	}
	
	public BufferedReader getReader(File file) throws IOException
	{
		return new BufferedReader(new FileReader(file));
	}
	
	public void closeWriter(BufferedWriter writer) throws IOException
	{
		writer.flush();
		writer.close();
	}
	
	public void closeReader(BufferedReader reader) throws IOException
	{
		reader.close();
	}
}
