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

	private static final String sectionOutLine = "-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#";
	private static String[] fileDescription;
	private static Logger log = N2ConfigApi.log;
	private String configName;
	private String fileName;
	
	private ConfigFile(String sectionName, String[] description, boolean setExtraSpaces)
	{
		super(sectionName, description, setExtraSpaces);
		
		this.setCustomSectionStarter(sectionOutLine);
		this.setCustomSectionHeadEnder(sectionOutLine);
		this.setCustomSectionEnder(sectionOutLine);
	}
	
	/**
	 * This will automatically register a new ConfigFile.
	 * @param fileName - the name which the configFile will get (fileName.cfg)
	 * @param configName - the name which will be displayed above the configFile
	 * @param targetDirectory - The default Directory where the ConfigurationFile will be generated.
	 * @throws IllegalArgumentException - When this targetDirectory isn't a registered Directory.
	 */
	public ConfigFile(String fileName, String configName, File targetDirectory)
	{
		this("defaultSection", null, true);
		
		if(!targetDirectory.isDirectory())
			throw new IllegalArgumentException("File " + targetDirectory.getPath() + " isn't a ConfigurationDirectory!");
		else ConfigHandler.registerConfigurationFile(this, fileName, targetDirectory);	
		
		this.fileName = fileName;
		this.configName = configName;
		
		this.fileDescription = new String[]
			{
				configName +  " Config File",
				" ",
				"warning, its recomended to backup a custom WORKING config file before editing.",
				"any misstakes may cause an resset!"
			};
		this.setDescription(fileDescription);
		
	}
	
	/**
	 * This method will be called when the File is (re)created.
	 */
	public abstract void generateFile();

	/**
	 * This will write All sections that are registered to this ConfigFile
	 * @throws IOException
	 */
	public void writeAllSections() throws IOException
	{
		BufferedWriter writer = getNewWriter();
		super.writeAllSections(writer);
		closeWriter(writer);
	}
	
	/**
	 * This will write the section. Note, it will be written behind the existing text inside the File!
	 * @param sectionName
	 * @throws IOException
	 */
	public void writeSection(String sectionName) throws IOException
	{
		BufferedWriter writer = getNewWriter();
		super.writeSubSection(writer, sectionName);
		closeWriter(writer);
	}
	
	/**
	 * This method will regenerate a configFile. Note, it will regenerate only invalidSections!
	 * A section is considered invalid when a section doesn't contain the sectionName.
	 * This method will recognize a section to 'its sectionStarter' or its sectionName(if the hideOutLines is enabled).
	 * If not all the ConfigSection can be recognize, it will backup the file, and regenerate the whole File!
	 * @param configurationFile
	 * @param invalidSectionNames - List of IDNames of all the invalid Sections.
	 * @throws IOException
	 */
	public void regenerateConfigFile(List<String> invalidSectionNames) throws IOException
	{
		File tempFile = ConfigHandler.generateSingleFileFromConfigFile(FileType.Temp, this, N2ConfigApi.getTempDir(ConfigHandler.getFileFromConfigFile(this).getParentFile()));
		File configFile = ConfigHandler.generateSingleFileFromConfigFile(FileType.Original, this, ConfigHandler.getFileFromConfigFile(this).getParentFile());
		
		BufferedWriter writer = getNewWriter();
		BufferedReader reader = getNewReader();
		
		int sectionCount = 0;
		
		sectionCount = super.regenConfigChapter(writer, reader, invalidSectionNames);
		
		if(sectionCount != this.AbsoluteSubSectionMap.size())
		{
			log.error("Couldn't resolve the sections in: " + configFile.getPath());
			log.error("recreating this config file, all data will be restored to its default Value");
			File backUpFile = ConfigHandler.generateSingleFileFromConfigFile(ConfigHandler.FileType.BackUp, this, N2ConfigApi.getTempDir(ConfigHandler.getFileFromConfigFile(this).getParentFile()));
			ConfigHandler.copyFile(tempFile, backUpFile);
			ConfigHandler.generateSingleFileFromConfigFile(ConfigHandler.FileType.Original, this, ConfigHandler.getFileFromConfigFile(this).getParentFile());
			this.generateFile();
		}
		closeWriter(writer);
		tempFile.delete();
		closeReader(reader);
	}
	
	/**
	 * This will check if the existing configuration file contains all registered sections.
	 * If this isn't the case it will return an List of String representing all the sections which are considered invalid,
	 * otherwise it will return null. (an section is considered invalid when it doesn't contain its sectionName)
	 * Note, SectionTypes Text and SectionHead will not be checked since they don't contain value's!
	 * @return List(section names) || null - see description.
	 * @throws IOException
	 */
	public List checkConfigFile(BufferedReader reader) throws IOException
	{
		String readedLine;
		List<String> invalidSectionList = new ArrayList<String>();
		
		Iterator<String> section = this.AbsoluteSubSectionMap.keySet().iterator();
		
		while(section.hasNext())
		{
			invalidSectionList.add(section.next());
		}

		while((readedLine = reader.readLine()) != null)
		{
			if(readedLine.startsWith("#") || readedLine.isEmpty())
				continue;

			if(readedLine.contains(";"))
			{
				if (invalidSectionList.contains(readedLine.substring(0, (readedLine.indexOf(";")))))
				{
					invalidSectionList.remove(readedLine.substring(0, (readedLine.indexOf(";"))));
				}
				else continue;
			}
			else continue;
		}	
		if(!invalidSectionList.isEmpty())
		{
			for(int i = 0; i < invalidSectionList.size(); i++)
			{
				if(!(this.AbsoluteSubSectionMap.get(invalidSectionList.get(i)).getSectionType() == SectionType.Text) || !(this.AbsoluteSubSectionMap.get(invalidSectionList.get(i)).getSectionType() == SectionType.SectionHead))
					log.error("Missing Section: " + invalidSectionList.get(i) + " in   ");
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
	public Object getValue(String sectionName) throws IOException
	{
		BufferedReader reader = getNewReader();
		Object value = this.getValue(sectionName, reader);
		closeReader(reader);
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
	
	public String getConfigName()
	{
		return this.configName;
	}
	
	public boolean getIsWritten() throws IOException
	{
		BufferedReader reader = getNewReader();
		String readedLine;
		if((readedLine = reader.readLine()) != null)
		{
			closeReader(reader);
			return true;
		}
		closeReader(reader);
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
