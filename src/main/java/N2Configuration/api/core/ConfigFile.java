package N2Configuration.api.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import N2Configuration.api.N2ConfigApi;
import N2Configuration.api.core.ConfigSection.SectionType;
import N2Configuration.api.core.ConfigHandler.FileType;

public abstract class ConfigFile extends ConfigSectionCollection implements Cloneable
{

	private static Logger log = N2ConfigApi.log;
	private String fileName;
	
	private ConfigFile(String sectionName, String[] description, boolean setExtraSpaces)
	{
		super(sectionName, description, setExtraSpaces);
	}
	
	/**
	 * This will automatically register a new ConfigFile.
	 * @param fileName - the name which the configFile will get (fileName.cfg)
	 * @param configName - the name which will be displayed above the configFile
	 * @param targetDirectory - The default Directory where the ConfigurationFile will be generated.
	 * @throws IllegalArgumentException - When this targetDirectory isn't a registered Directory.
	 */
	public ConfigFile(String fileName, File targetDirectory)
	{
		this("defaultSection", null, true);
		
		if(!targetDirectory.isDirectory())
			throw new IllegalArgumentException("File " + targetDirectory.getPath() + " isn't a ConfigurationDirectory!");
		else ConfigHandler.registerConfigurationFile(this, fileName, targetDirectory);	
		
		this.fileName = fileName;
	}
	
	protected void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	/**
	 * This method will be called when the File is (re)created.
	 */
	public abstract void generateFile();

	/**
	 * This will write All sections that are registered to this ConfigFile
	 * @throws IOException
	 */
	public void writeAllSections()
	{
		try
		{
			BufferedWriter writer = getNewWriter();
			super.writeAllSections(writer);
			closeWriter(writer);
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This will write the section. Note, it will be written behind the existing text inside the File!
	 * @param sectionName
	 * @throws IOException
	 */
	public void writeSection(String sectionName)
	{
		try
		{
			BufferedWriter writer = getNewWriter();
			super.writeSubSection(writer, sectionName);
			closeWriter(writer);
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This method will regenerate a configFile. Note, it will regenerate only invalidSections!
	 * If something goes wrong during the regeneration, it will backup the file, and regenerate the whole File!
	 * @param configurationFile
	 * @param invalidSectionNames - List of IDNames of all the invalid Sections.
	 * @throws IOException
	 */
	public void regenerateConfigFile(List<String> invalidSectionNames)
	{
		try
		{
			File tempFile = ConfigHandler.generateSingleFileFromConfigFile(this, FileType.Temp, N2ConfigApi.getTempDir(ConfigHandler.getFileFromConfigFile(this).getParentFile()));
			File configFile = ConfigHandler.generateSingleFileFromConfigFile(this, FileType.Original, ConfigHandler.getFileFromConfigFile(this).getParentFile());
			
			BufferedWriter writer = getNewWriter();
			BufferedReader reader = new BufferedReader(new FileReader(tempFile));
			
			int sectionCount = 0;
			
			sectionCount = super.regenConfigChapter(writer, reader, invalidSectionNames);
			
			if(sectionCount != this.AbsoluteSubSectionMap.size())
			{
				log.error("Couldn't resolve the sections in: " + configFile.getPath());
				log.error("recreating this config file, all data will be restored to its default Value");
				File backUpFile = ConfigHandler.generateSingleFileFromConfigFile(this, ConfigHandler.FileType.BackUp, N2ConfigApi.getTempDir(ConfigHandler.getFileFromConfigFile(this).getParentFile()));
				ConfigHandler.copyFile(tempFile, backUpFile);
				ConfigHandler.generateSingleFileFromConfigFile(this, ConfigHandler.FileType.Original, ConfigHandler.getFileFromConfigFile(this).getParentFile());
			}
			closeWriter(writer);
			tempFile.delete();
			closeReader(reader);
		}
		catch(Exception e)
		{
			log.catching(e);
		}
	}
	
	/**
	 * This will check if the existing configuration file contains all registered sections.
	 * If this isn't the case it will return an List of String representing all the sections which are considered invalid,
	 * otherwise it will return null. (an section is considered invalid when it doesn't contain its sectionName)
	 * Note, SectionTypes Text and SectionHead will not be checked since they don't contain value's!
	 * @return List(section names) || null - see description.
	 * @throws IOException
	 */
	public List checkConfigFile()
	{
		String readedLine;
		List<String> invalidSectionList = new ArrayList<String>();
		
		Iterator<String> section = this.AbsoluteSubSectionMap.keySet().iterator();
		
		while(section.hasNext())
		{
			invalidSectionList.add(section.next());
		}

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(ConfigHandler.getFileFromConfigFile(this)));
			while((readedLine = reader.readLine()) != null)
			{
				if(readedLine.startsWith("#") || readedLine.isEmpty())
					continue;

				if(readedLine.contains(";"))
				{
					for(int i = 0; i < invalidSectionList.size(); i++)
					{
						String subString = readedLine.replaceAll("[\\s.*]", "");
						char firstLetter = subString.charAt(0);
						try
						{
							if (invalidSectionList.contains(readedLine.substring(readedLine.indexOf(firstLetter), readedLine.indexOf(";"))))
							{
								invalidSectionList.remove(readedLine.substring(readedLine.indexOf(firstLetter), readedLine.indexOf(";")));
							}
						}
						catch(Exception e){}
					}
				}
			}
			reader.close();
		}
		catch (Exception e)
		{
			log.catching(e);
		}	
		if(!invalidSectionList.isEmpty())
		{
			for(int i = 0; i < invalidSectionList.size(); i++)
			{
				if(!(this.AbsoluteSubSectionMap.get(invalidSectionList.get(i)).getSectionType() == SectionType.Text) || !(this.AbsoluteSubSectionMap.get(invalidSectionList.get(i)).getSectionType() == SectionType.SectionHead))
					log.error("Missing Section: " + invalidSectionList.get(i) + " in " + ConfigHandler.getFileFromConfigFile(this).getName());
			}
			return invalidSectionList;
		}
		return null;
	}

	
	/**
	 * @param sectionName
	 * @return - The value of this section, or the Default value if the value can't be resolved
	 * @throws IOException
	 */
	public Object getValue(String sectionName)
	{
		Object value = null;
		try
		{
			BufferedReader reader = getNewReader();
			value = this.getValue(sectionName, reader);
			closeReader(reader);
		}
		catch(Exception e)
		{
			log.catching(e);
		}
		return value;
	}
	
	/**
	 * Use only if you know what you are doing! If you want to Clone a configFile,
	 * use the "ConfigHandler.generateSingleFileFromConfigFile()" instead with "FilType.Clone" as parameter.
	 * @return - new Instance of this ConfigFile.
	 * @throws CloneNotSupportedException
	 */
	protected ConfigFile CloneConfigurationFileInstance() throws CloneNotSupportedException
	{
		return (ConfigFile) super.clone();
	}
		
	public String getFileName()
	{
		return this.fileName;
	}
	
	public boolean getIsWritten()
	{
		try
		{
			BufferedReader reader = getNewReader();
			String readedLine;
			if((readedLine = reader.readLine()) != null)
			{
				closeReader(reader);
				return true;
			}
			closeReader(reader);
		}
		catch(Exception e)
		{
			log.catching(e);
		}
		return false;
	}
	
	/**
	 * @return - A new BufferedWriter. Don't forget to close it after using!
	 * @throws IOException
	 */
	public BufferedWriter getNewWriter() throws IOException
	{
		return new BufferedWriter(new FileWriter(ConfigHandler.getFileFromConfigFile(this)));
	}
	
	public void closeWriter(BufferedWriter writer) throws IOException
	{
		writer.flush();
		writer.close();
	}
	
	/**
	 * @return A new BufferedReader. Don't forget to close it after using!
	 * @throws IOException
	 */
	public BufferedReader getNewReader() throws IOException
	{
		return new BufferedReader(new FileReader(ConfigHandler.getFileFromConfigFile(this)));
	}
	
	public void closeReader(BufferedReader reader) throws IOException
	{
		reader.close();
	}
}
