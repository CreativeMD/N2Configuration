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

import N2Configuration.api.N2Configuration;

public class ConfigurationFile
{
	private static final String SectionStarter = "-----------------------------------------------------------------------------------------------------";
	private static final String SectionEnder   = "-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-";
	private static Logger log = N2Configuration.log;
	private HashMap<String, ConfigSection> sectionList = new HashMap<String, ConfigSection>();
	private boolean[] isValidList = new boolean[]{};
	private String configName;
	private String fileName;
	
	/**
	 * This will register a new ConfigFile, once 'Configuration.loadConfig()' is called, this File will be loaded as well"
	 * @param fileName - the name which the configFile will get (fileName.cfg)
	 * @param configName - the name which will be displayed above the configFile
	 * @param targetDirectory - The default Directory where the ConfigurationFile should be generated.
	 * @throws IllegalArgumentException When this targetDirectory isn't a Directory.
	 */
	public ConfigurationFile RegisterConfigurationFile(String fileName, String configName, File targetDirectory)
	{
		if(targetDirectory.isDirectory())
		{
			Configuration.registerConfigurationFile(this, fileName, targetDirectory);
			this.fileName = fileName;
		}
		else throw new IllegalArgumentException("File " + targetDirectory.getAbsolutePath() + " isn't a Directory!");
		
		return this;
	}
	
	/**
	 * This method will be called when the File is (re)generated.
	 * When calling the super() it will make the header of the configuration file.
	 * @param writer - (BufferedWriter)
	 * @throws IOException
	 */
	public void generateFile(BufferedWriter writer) throws IOException
	{
		writer.append("# " + this.configName + " Config File");
		writer.newLine();
		writer.newLine();
		writer.append("# warning, its recomended to backup a custom WORKING config file before editing.");
		writer.newLine();
		writer.append("# any misstakes may cause an resset!");
		writer.newLine();
		writer.append("-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
		writer.newLine();
	}
	
	/**
	 * This method will generate an Integer section.
	 * @param writer - (BufferedWriter)
	 * @param sectionName - This will be written before the actual input. This will also function as ID for this section!
	 * @param discription - This will be written above the input (as a header), every String stands for a new line.
	 * @param defaultValue - This value will be written when the file is (re)generated.
	 * @throws IOException
	 */
	public void createNewIntSection(BufferedWriter writer, String sectionName, String[] description, int defaultValue) throws IOException
	{
		this.sectionList.put(sectionName, new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.Integer, false));
		writeSection(writer, sectionName, description, defaultValue, ConfigSection.SectionType.Integer, false);
	}
	
	/**
	 * This method will generate an String section.
	 * @param writer - (BufferedWriter)
	 * @param sectionName - This will be written before the actual input. This will also function as ID for this section!
	 * @param discription - This will be written above the input (as a header), every String stands for a new line.
	 * @param defaultValue - This value will be written when the file is (re)generated.
	 * @throws IOException
	 */
	public void createNewStringSection(BufferedWriter writer, String sectionName, String[] description, String defaultValue) throws IOException
	{
		sectionList.put(sectionName,new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.String, false));
		writeSection(writer, sectionName, description, defaultValue, ConfigSection.SectionType.String, false);
	}
	
	/**
	 * This method will generate an StringArray section.
	 * @param writer - (BufferedWriter)
	 * @param sectionName - This will be written before the actual input. This will also function as ID for this section!
	 * @param discription - This will be written above the input (as a header), every String stands for a new line.
	 * @param defaultValue - This value will be written when the file is (re)generated.
	 * @param separateLines - If every single String (defaultValue) should be written on separate lines.
	 * @throws IOException
	 */
	public void createNewStringArraySection(BufferedWriter writer, String sectionName, String[] description, String defaultValue, boolean separateLines) throws IOException
	{
		sectionList.put(sectionName, new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.StringArray, separateLines));
		writeSection(writer, sectionName, description, defaultValue, ConfigSection.SectionType.StringArray, separateLines);
	}
	
	/**
	 * Are you trying to call this? :P
	 * @param writer - (BufferedWriter)
	 * @param sectionName
	 * @param discription
	 * @param defaultValue
	 * @param sectionType
	 * @param separateLines
	 * @throws IOException
	 */
	private void writeSection(BufferedWriter writer, String sectionName, String[] discription, Object defaultValue, ConfigSection.SectionType sectionType, boolean separateLines) throws IOException
	{
		writer.append(SectionStarter);
		writer.newLine();
		
		for(int i = 0; i < discription.length; i++)
		{
			writer.append("# " + discription[i]);
			writer.newLine();
		}
		writer.append(SectionStarter);
		writer.newLine();
		writer.newLine();
		
		switch(sectionType)
		{
			case Integer:
				writer.append(sectionName + ": " + Integer.toString((int) defaultValue));
				break;
			case String:
				writer.append(sectionName + ": " + defaultValue);
				break;
			case StringArray:
				writer.append(sectionName + ":{ ");

				for(int i = 0; i < Arrays.asList((String[])defaultValue).size(); i++)
				{
					if(separateLines)
					{
						writer.newLine();
						writer.append((String) Arrays.asList((String[]) defaultValue).get(i) + ",");
					}
					else
					{
						writer.append((String) Arrays.asList((String[]) defaultValue).get(i) + ", ");
					}
				}
				
				writer.newLine();
				writer.append("};");
				break;
		}
	
		writer.newLine();
		writer.newLine();
		writer.append(SectionEnder);
		writer.newLine();
		writer.newLine();
	}
	
	/**
	 * This method will regenerate a configFile. Note that it will regenerate only invalidSections!
	 * A section is considered invalid when a section doesn't contain the sectionName.
	 * While the 'checkConfigFile()' method will validate a section on its sectionName,
	 * this method will recognize a section to 'its sectionStarter'.
	 * @param configurationFile
	 * @param invalidSectionNames
	 * @throws IOException
	 */
	public void regenerateConfigFile(ConfigurationFile configurationFile, List<String> invalidSectionNames) throws IOException
	{
		File tempFile = Configuration.generateSingleFile(Configuration.FileType.Temp, configurationFile, N2Configuration.getTempDirectory(Configuration.getFile(configurationFile).getParentFile()));
		File configFile = Configuration.getFile(configurationFile);
		
		Configuration.copyFile(configFile, tempFile);
		
		BufferedReader reader = new BufferedReader(new FileReader(tempFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
		
		String readedLine = reader.readLine();
		
		while((readedLine = reader.readLine()) != null)
		{
			if(readedLine.startsWith(SectionStarter))
			{
				StringBuilder section = new StringBuilder();
				while((readedLine = reader.readLine()) != null)
				{
					section.append(readedLine + "\\n");
					if(invalidSectionNames.contains(readedLine.substring(0, readedLine.indexOf(":")-1)))
					{
						ConfigSection newSection = sectionList.get(readedLine.substring(0, readedLine.indexOf(":")-1));
						writeSection(writer, newSection.getSectionName(), newSection.getDescription(), newSection.getDefaultValue(), newSection.getSectionType(), newSection.getSeparteLines());
					}
					else if(readedLine.startsWith(SectionEnder))
					{
						section.append(readedLine);
						String finalString = section.toString();
						
						Pattern p1 = Pattern.compile("\\n");
						String[] s1 = p1.split(finalString);
						for(String s2 : s1)
						{
							if(s2.length() > 0)
							{
								writer.append(s2);
								writer.newLine();
							}
						}
					}
				}
			}
			else
			{
				writer.append(readedLine);
				writer.newLine();
			}
		}
		writer.flush();
		writer.close();
		reader.close();
	}
	
	/**
	 * This will check if the existing configuration file contains all predefined sections.
	 * If this isn't the case it will return an List of String representing all the sections which aren't considered valid,
	 * otherwise it will return null. (an section is considered invalid when it doesn't contain its sectionName)
	 * @return List(section names) || null - see description.
	 * @throws IOException
	 */
	public List checkConfigFile() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(Configuration.getFile(this)));
		String readedLine = reader.readLine();
		List<String> invalidSectionList = new ArrayList<String>();
		
		Iterator<String> section = sectionList.keySet().iterator();
		
		while(section.hasNext())
		{
			String sectionName = section.next();
			invalidSectionList.add(sectionName);
		}

		while((readedLine = reader.readLine()) != null)
		{
			if(readedLine.startsWith("#") || readedLine.isEmpty())
				continue;

			if (invalidSectionList.contains(readedLine.substring(0, (readedLine.indexOf(":")-1))))
			{
				invalidSectionList.remove(readedLine.substring(0, (readedLine.indexOf(":")-1)));
			}
			else continue;
		}	
		reader.close();
		if(!invalidSectionList.isEmpty())
		{
			for(int i = 0; i < invalidSectionList.size(); i++)
				log.error("Missing Section: " + invalidSectionList.get(i) + " in " + Configuration.getFile(this).getAbsolutePath());
			
			return invalidSectionList;
		}
		return null;
	}
	
	/**
	 * This will get the value of the corresponding sectionName.eo
	 * @param sectionName
	 * @return the value behind the sectionName in the configuration file.
	 * note that if the sectionName or the value is invalid, it will return null(-1 for numeric data types).
	 * @throws IOException
	 */
	public Object getValue(String sectionName) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(Configuration.getFile(this)));
		String readedLine = reader.readLine();
		
		while((readedLine = reader.readLine()) != null)
		{
			if(readedLine.startsWith(sectionName))
			{
				ConfigSection.SectionType sectiontype = sectionList.get(sectionName).getSectionType();
				Pattern P1 = Pattern.compile(readedLine.substring(0, readedLine.indexOf(":")));
				String[] value = P1.split(readedLine);
				
				switch(sectiontype)
				{
				case Integer:
					int finalIntegerValue = 0;
					for (String s1 : value)
					{
							if (s1.length() > 0)
							{
								Pattern p2 = Pattern.compile("\\s");
								String[] value1 = p2.split(s1);
								for (String s2 : value1)
								{
									if (s2.length() > 0)
									{
										reader.close();
										try
										{
											finalIntegerValue = Integer.parseInt(s2);
										}
										catch(Exception e)
										{
											log.catching(e);
											return -1;
										}
										return finalIntegerValue;
									}
								}
							}
						}
					break;
				case String:
					String FinalStringValue;
					for(String s1 : value)
					{
						if(s1.length() > 0)
						{
							Pattern p2 = Pattern.compile("\\s");
							String[] value1 = p2.split(s1);
							for(String s2 : value1)
							{
								if(s2.length() > 0)
								{
									reader.close();
									FinalStringValue = s2;
									return FinalStringValue;
								}
							}
						}
					}
					break;
				case StringArray:
					StringBuilder finalStringArrayValue = new StringBuilder();
					for(String s1 : value)
					{
						Pattern p2 = Pattern.compile("\\s");
						if(!Pattern.matches("\\{", s1))
						{
							String[] s2 = p2.split(s1);
							for(String s3 : s2)
							{
								if(s3.length() > 0)
								{
									Pattern p3 = Pattern.compile(",");
									String[] s4 = p3.split(s3);
									for(String s5 : s4)
									{
										if(s5.length() > 0)
										{
											if(Pattern.matches("\\}", s5))
												return finalStringArrayValue.toString();
											else finalStringArrayValue.append(s5);
										}
									}
								}
							}
						}
					}
					break;
				default:
					log.error("couldn't find section: " + sectionName);
					return null;
				}
			}
			else continue;
		}
		reader.close();
		return null;
	}
	
	
	public String getOriginalFileName()
	{
		return this.fileName;
	}
}
