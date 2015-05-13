package N2Configuration.api.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import N2Configuration.api.N2ConfigApi;
import N2Configuration.api.core.ConfigSection.SectionType;

public class ConfigSectionCollection extends ConfigSection
{
	protected HashMap<String, ConfigSection> AbsoluteSubSectionMap = new HashMap<String, ConfigSection>();
	private HashMap<String, ConfigSection> subSectionMap = new HashMap<String, ConfigSection>();
	private HashMap<Integer, ConfigSection> subSectionOrderMap = new HashMap<Integer, ConfigSection>();
	private Logger log = N2ConfigApi.log;
	private ConfigSectionCollection superConfigSectionCollection;
	private boolean setExtraSpaces;

	/**
	 * @param sectionName
	 * @param description
	 * @param setExtraSpaces - Whether there will be extra spaces printed between subSections or not.
	 */
	public ConfigSectionCollection(String sectionName, String[] description, boolean setExtraSpaces)
	{
		super(sectionName, description, null, SectionType.SectionHead, true, false);
		this.setExtraSpaces = setExtraSpaces;
	}
	
	/**
	 * This will create an new ConfigSectionCollection, and add it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param setExtraSpaces  - Whether there will be extra spaces printed between subSections or not.
	 * @return
	 */
	public ConfigSection addNewConfigSectionCollection(String sectionName, String[] description, boolean setExtraSpaces)
	{
		ConfigSection configSection = new ConfigSectionCollection(sectionName, description, setExtraSpaces);
		((ConfigSectionCollection)configSection).setSuperConfigSectionCollection(this);
		if(!this.subSectionMap.containsKey(sectionName))
		{
			this.subSectionMap.put(sectionName, configSection);
			this.subSectionOrderMap.put(subSectionOrderMap.size(), configSection);
		}
		else log.error("Section: " + sectionName + " is already registered!");
		return configSection;
	}
	
	/**
	 * This will create an new ShortSection, and add it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param hideOutLines
	 * @return
	 */
	public ConfigSection addNewShortSection(String sectionName, String[] description, short defaultValue, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, SectionType.Short, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This will create an new ShortArraySection, and add it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param separateLines
	 * @param hideOutLines
	 * @return
	 */
	public ConfigSection addNewShortArraySection(String sectionName, String[] description, short[] defaultValue,  boolean separateLines, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, SectionType.ShortArray, separateLines, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an IntegerSection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewIntegerSection(String sectionName, String[] description, int defaultValue, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.Integer, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an IntegerArraySection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param separateLines
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewIntegerArraySection(String sectionName, String[] description, int[] defaultValue, boolean separateLines, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.IntegerArray, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an LongSection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewLongSection(String sectionName, String[] description, long defaultValue, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.Long, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an LongArraySection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param separateLines
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewLongArraySection(String sectionName, String[] description, long[] defaultValue, boolean separateLines, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.LongArray, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an FloatSection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewFloatSection(String sectionName, String[] description, float defaultValue, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.Float, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an FloatArraySection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param separateLines
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewFloatArraySection(String sectionName, String[] description, float[] defaultValue, boolean separateLines, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.FloatArray, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an DoubleSection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewDoubleSection(String sectionName, String[] description, double defaultValue, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.Double, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an DoubleArraySection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param separateLines
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewDoubleArraySection(String sectionName, String[] description, double[] defaultValue, boolean separateLines, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.DoubleArray, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an BooleanSection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewBooleanSection(String sectionName, String[] description, boolean defaultValue, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.Boolean, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an BooleanArraySection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param separateLines
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewBooleanArraySection(String sectionName, String[] description, boolean[] defaultValue, boolean separateLines, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.BooleanArray, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an StringSection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewStringSection(String sectionName, String[] description, String defaultValue, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.String, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an StringArraySection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description
	 * @param defaultValue
	 * @param separateLines
	 * @param hideOutLines
	 * @throws IOException
	 */
	public ConfigSection addNewStringArraySection(String sectionName, String[] description, String[] defaultValue, boolean separateLines, boolean hideOutLines)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, defaultValue, ConfigSection.SectionType.StringArray, hideOutLines);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This method will create an StringSection, and ad it to this ConfigSectionCollection.
	 * @param sectionName - The ID of this Section
	 * @param description - If description is null, a newLine (return) will be written instead of a Text Section
	 * @param defaultValue
	 * @throws IOException
	 */
	public ConfigSection addNewTextSection(String sectionName, String[] description)
	{
		ConfigSection configSection = new ConfigSection(sectionName, description, ConfigSection.SectionType.Text);
		addNewSection(sectionName, configSection);
		return configSection;
	}
	
	/**
	 * This will add the ConfigSection to this ConfigSectionCollection.
	 * @param sectionName
	 * @param configSection
	 */
	public void addNewSection(String sectionName, ConfigSection configSection)
	{
		if(!this.subSectionMap.containsKey(sectionName))
		{
			this.subSectionMap.put(sectionName, configSection);
			this.subSectionOrderMap.put(this.subSectionOrderMap.size(), configSection);
			if(this.superConfigSectionCollection != null)
				this.superConfigSectionCollection.insertConfigSection(sectionName, this.AbsoluteSubSectionMap.get(this.AbsoluteSubSectionMap.size()), configSection);
			this.AbsoluteSubSectionMap.put(sectionName, configSection);
		}
		else log.error("Section: " + sectionName + " is already registered!");
	}
	
	/**
	 * @param sectionName
	 * @return - A ConfigSection inside this ConfigSectionCollection or 'nested' ConfigSectionCollections.
	 */
	public ConfigSection getSubSection(String sectionName)
	{
		if(this.subSectionMap.containsKey(sectionName))
			return this.subSectionMap.get(sectionName);
		else
		{
			Iterator<String> CurrentSectionName = this.subSectionMap.keySet().iterator();
			
			while(CurrentSectionName.hasNext())
			{
				ConfigSection section = this.subSectionMap.get(CurrentSectionName.hasNext());
				if(section instanceof ConfigSectionCollection)
					return ((ConfigSectionCollection) section).getSubSection(sectionName);
			}
		}
		return null;
	}
	
	/**
	 * @return - The number of ConfigSection which are stored in this ConfigSectionCollection.
	 */
	public int getSubSectionCount()
	{
		return this.subSectionMap.size();
	}
	
	/**
	 * @return - The Number of ConfigSections which are stored in this ConfigSectionCollection, including the 'nested' ConfigSections.
	 */
	public int getAbsoluteSubSectionCount()
	{
		int finalAmount = 0;
		finalAmount += this.getSubSectionCount();
		
		Iterator<String> currentSectionName = this.subSectionMap.keySet().iterator();
		
		while(currentSectionName.hasNext())
		{
			if(this.subSectionMap.get(currentSectionName.next()) instanceof ConfigSectionCollection)
				finalAmount += ((ConfigSectionCollection)this.subSectionMap.get(currentSectionName.next())).getAbsoluteSubSectionCount();
		}		
		return finalAmount + 1;		
	}
	
	/**
	 * This will insert an section into this ConfigSectionCollection.
	 * @param configSectionName
	 * @param insertConfigSection - The configSection you want to insert.
	 * @param configSectionTarget -  The ConfigSection which the new ConfigSection should be inserted before!
	 * @return True if the insertion was successful, false otherwise.
	 */
	public boolean insertConfigSection(String configSectionName, ConfigSection insertConfigSection, ConfigSection configSectionTarget)
	{
		if(this.superConfigSectionCollection != null)
			this.superConfigSectionCollection.insertConfigSection(configSectionName, insertConfigSection, configSectionTarget);
		else
		{
			int index = 0;
			if(this.subSectionMap.containsValue(configSectionTarget))
			{
				for(int i = 0; i < this.subSectionOrderMap.size(); i++)
				{
					ConfigSection configSection = this.subSectionOrderMap.get(i);
					if(configSection.equals(configSectionTarget))
					{
						index = i;
						break;
					}
				}
				
				HashMap<Integer, ConfigSection> newSubSectionOrderMap = new HashMap<Integer, ConfigSection>();
				for (int il = 0; il < index; il++)
				{
					newSubSectionOrderMap.put(il, this.subSectionOrderMap.get(il));
				}
				newSubSectionOrderMap.put(index, insertConfigSection);
				for(int ik = index; ik < this.subSectionOrderMap.size() + 1; ik++)
				{
					newSubSectionOrderMap.put(ik + 1, this.subSectionMap.get(ik));
				}
				this.subSectionOrderMap = newSubSectionOrderMap;
				return true;
			}
			
			for(int i = 0; i < this.subSectionOrderMap.size(); i++)
			{
				ConfigSection configSection	= this.subSectionOrderMap.get(i);
				if(configSection instanceof ConfigSectionCollection)
				{
					if(((ConfigSectionCollection)configSection).insertConfigSection(configSectionName, insertConfigSection, configSectionTarget))
						return true;
				}
			}
			return false;
		}
		return false;
	}
	
	/**
	 * @param index
	 * @return - The configSection inside this ConfigSectionCollection at index.
	 */
	public ConfigSection getConfigSectionAtIndex(int index)
	{
		int corrector = 0;
		ConfigSection finalsection = null;
		
		if(index > this.subSectionOrderMap.size())
			return null;
		if(index == 0)
			return this;
		
		for(int i = 0; i <= (index - 1 - corrector); i++)
		{
			ConfigSection section = this.subSectionOrderMap.get(i + corrector);
			if(section instanceof ConfigSectionCollection)
			{
				for(int il = 0; il <= (index - i); il++)
				{
					finalsection = ((ConfigSectionCollection) section).getConfigSectionAtIndex(il - 1);
					if(finalsection == null)
						corrector += ((ConfigSectionCollection) section).getSubSectionCount();
				}
			}
			else
			{
				finalsection = this.subSectionOrderMap.get(i + corrector);
			}
		}
		return finalsection;
	}
	
	private void setSuperConfigSectionCollection(ConfigSectionCollection configSectionCollection)
	{
		this.superConfigSectionCollection = configSectionCollection;
	}
	
	/**
	 * @return - The ConfigSectionCollection where this ConfigSectionCollection is 'nested' in.
	 * 	If non exist, it will return null.
	 */
	public ConfigSectionCollection getSuperConfigSectionChapter()
	{
		return this.superConfigSectionCollection;
	}
	
	/**
	 * This will regenerate this ConfigSectionChapter.
	 * @param writer
	 * @param reader
	 * @param invalidSections - Note, SectionType Text will always be reWritten.
	 * @return - The number of ConfigSections it was able to recognize.
	 * @throws IOException
	 */
	public int regenConfigChapter(BufferedWriter writer, BufferedReader reader, List invalidSections) throws IOException
	{
		String readedLine;
		int sectionCount = 0;
		int totalSectionsChecked = 0;
		while((readedLine = reader.readLine()) != null)
		{
			if(readedLine.startsWith(this.getStarter()))
				writer.append(this.getStarter());
			if(this.getDescription() != null ? readedLine.startsWith(this.getDescription()[0]) : false)
			{
				for(int i = 0; i < this.getDescription().length; i++)
				{
					writer.append(this.getDescription()[i]);
					writer.newLine();
					reader.readLine();
				}
			}
			if(this.getDescription() == null)
				break;
			else if(readedLine.startsWith(this.getHeadEnder()))
			{
				writer.append(this.getHeadEnder());
				writer.newLine();	
				writer.flush();
				break;
			}
		}
		writer.newLine();	
		
		while((readedLine = reader.readLine()) != null)
		{
			ConfigSection configSection = null;
			if(sectionCount <= this.subSectionOrderMap.size())
				configSection = this.subSectionOrderMap.get(sectionCount);
			else break;
			
			if(configSection instanceof ConfigSectionCollection)
				totalSectionsChecked += ((ConfigSectionCollection) configSection).regenConfigChapter(writer, reader, invalidSections);
			else if(configSection.getSectionType() == SectionType.Text)
				writeSubSection(writer, configSection);
			
			if(!configSection.getHideOutLines())
			{
				if(readedLine.startsWith(configSection.getStarter()))
				{
					StringBuilder section = new StringBuilder();
					boolean passedHeadEnder = false;
					SectionRegeneration:
					while(readedLine != null)
					{
						section.append(readedLine + "\\n");
						if(readedLine.contains(";"))
						{
							if(invalidSections.contains(readedLine.substring(0, readedLine.indexOf(";"))))
							{
								ConfigSection newSection = this.subSectionMap.get(readedLine.substring(0, readedLine.indexOf(";")));
								writeSubSection(writer, configSection);
							}
						}
						if(readedLine.startsWith(configSection.getEnder()))
						{
							if(!configSection.getEnder().equals(configSection.getHeadEnder()))
								passedHeadEnder = true;
							if(passedHeadEnder)
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
										writer.flush();
									}
								}
								break SectionRegeneration;
							}
							passedHeadEnder = true;
						}
						reader.readLine();
					}
					sectionCount++;
				}
				else
				{
					writer.append(readedLine);
					writer.newLine();
					writer.flush();
				}
			}
			else
			{
				if(readedLine.startsWith(configSection.getSectionName()))
				{
					if(invalidSections.contains(configSection.getSectionName()))
						writeSubSection(writer, configSection);
					else if(configSection.getSectionType().isArray())
					{
						while(readedLine != null)
						{
							writer.append(readedLine);
							writer.newLine();
							writer.flush();
							if(readedLine.endsWith("};"))
								break;
						}
					}
					else
					{
						writer.append(readedLine);
					}	
					writer.newLine();
					writer.flush();
					sectionCount++;
				}
			}
			writer.append(readedLine);
			writer.newLine();
			writer.flush();
		}
		
		writer.newLine();
		writer.append(this.getEnder());
		writer.newLine();
		writer.flush();
		return totalSectionsChecked + sectionCount + 1;
	}
	
	public void writeAllSections(BufferedWriter writer) throws IOException
	{
		writeSectionChapter(writer, this);
		writer.flush();
	}
	
	public void writeSubSection(BufferedWriter writer, String sectionName) throws IOException
	{
		ConfigSection section = subSectionMap.get(sectionName);
		if(section instanceof ConfigSectionCollection)
			((ConfigSectionCollection)section).writeSectionChapter(writer, (ConfigSectionCollection) section);
		else writeSubSection(writer, section);
	}
	
	private void writeSectionChapter(BufferedWriter writer, ConfigSectionCollection section) throws IOException
	{
		if(!section.getHideOutLines())
		{
			writer.append(section.getStarter());
			if(section.getDescription() != null)
			{
				writer.newLine();
				for(int i = 0; i < section.getDescription().length; i++)
				{
					writer.append(section.getDescription()[i]);
					writer.newLine();
				}
				writer.append(section.getHeadEnder());
			}
			writer.newLine();
			writer.newLine();
		}
		
		for (int i = 0; i < this.subSectionOrderMap.size(); i++)
		{
			ConfigSection subSection = this.subSectionOrderMap.get(i);
			if (subSection != null)
				if (subSection instanceof ConfigSectionCollection)
					((ConfigSectionCollection) subSection).writeAllSections(writer);
				else writeSubSection(writer, subSection);
			
			if(section.setExtraSpaces)
				writer.newLine();
		}
		
		writer.newLine();
		if(!section.getHideOutLines())
			writer.append(section.getEnder());

	}
	
	private void writeSubSection(BufferedWriter writer, ConfigSection section) throws IOException
	{
		SectionType sectionType = section.getSectionType();
		
		if(sectionType == SectionType.Text && section.getDescription() == null)
		{
			writer.newLine();
		}
		else if(!section.getHideOutLines())
		{
			writer.append(section.getStarter());
			if(section.getDescription() != null)
			{
				writer.newLine();
				for(int i = 0; i < section.getDescription().length; i++)
				{
					writer.append(section.getDescription()[i]);
					writer.newLine();
				}
				if(sectionType != SectionType.Text)
					writer.append(section.getHeadEnder());
				else writer.append(section.getEnder());
			}
			writer.newLine();
			writer.newLine();
			writer.flush();
		}
		
		if(sectionType != SectionType.Text)
		{
			if(sectionType.isArray())
			{
				if(section.getDefaultValue() != null)
				{
					writer.append(section.getSectionName() + ";{ ");
					if(section.getSeparteLines())
						writer.newLine();
					
					switch (sectionType)
					{
					case ShortArray:
						short[] shortValues = (short[]) section.getDefaultValue();
						for (int i = 0; i < shortValues.length; i++)
						{
							if(section.getSeparteLines())
								writer.append(Short.toString(shortValues[i]) + ", ");
							else
							{
								writer.append(Short.toString(shortValues[i]));
								writer.newLine();
							}
							writer.flush();
						}
						break;
					case IntegerArray:
						int[] intValues = (int[]) section.getDefaultValue();
						for (int i = 0; i < intValues.length; i++)
						{
							if(section.getSeparteLines())
								writer.append(Integer.toString(intValues[i]) + ", ");
							else
							{
								writer.append(Integer.toString(intValues[i]));
								writer.newLine();
							}
							writer.flush();
						}
						break;
					case LongArray:
						long[] longValues = (long[]) section.getDefaultValue();
						for (int i = 0; i < longValues.length; i++)
						{
							if(section.getSeparteLines())
								writer.append(Long.toString(longValues[i]) + ", ");
							else
							{
								writer.append(Long.toString(longValues[i]));
								writer.newLine();
							}
							writer.flush();
						}
						break;
					case FloatArray:
						float[] floatValues = (float[]) section.getDefaultValue();
						for (int i = 0; i < floatValues.length; i++)
						{
							if(section.getSeparteLines())
								writer.append(Float.toString(floatValues[i]) + ", ");
							else
							{
								writer.append(Float.toString(floatValues[i]));
								writer.newLine();
							}
							writer.flush();
						}
						break;
					case DoubleArray:
						double[] doubleValues = (double[]) section.getDefaultValue();
						for (int i = 0; i < doubleValues.length; i++)
						{
							if(section.getSeparteLines())
								writer.append(Double.toString(doubleValues[i]) + ", ");
							else
							{
								writer.append(Double.toString(doubleValues[i]));
								writer.newLine();
							}
							writer.flush();
						}
						break;
					case BooleanArray:
						boolean[] booleanValues = (boolean[]) section.getDefaultValue();
						for (int i = 0; i < booleanValues.length; i++)
						{
							if(section.getSeparteLines())
								writer.append(Boolean.toString(booleanValues[i]) + ", ");
							else
							{
								writer.append(Boolean.toString(booleanValues[i]));
								writer.newLine();
							}
							writer.flush();
						}
						break;
					case StringArray:
						String[] values = (String[]) section.getDefaultValue();
						for (int i = 0; i < values.length; i++)
						{
							if(section.getSeparteLines())
								writer.append(values[i] + ", ");
							else
							{
								writer.append(values[i]);
								writer.newLine();
							}
							writer.flush();
						}
						break;
					default:
						log.error("Did something go wrong? Tried writing an Array while input wasn't an ArrayType");
						break;
					}
					writer.append("};");
					
				}
				else
				{
					writer.append(section.getSectionName() + ";{ ");
					if(section.getSeparteLines())
						writer.newLine();
					writer.append("};");
				}
				writer.newLine();
				writer.flush();
			}
			else
			{
				writer.append(section.getSectionName() + "; ");
				if(section.getDefaultValue() != null)
				{
					switch(sectionType)
					{
					case Short:
						writer.append(Short.toString((short) section.getDefaultValue()));
						break;
					case Integer:
						writer.append(Integer.toString((int) section.getDefaultValue()));
						break;
					case Long:
						writer.append(Long.toString((long) section.getDefaultValue()));
						break;
					case Float:
						writer.append(Float.toString((float) section.getDefaultValue()));
						break;
					case Double:
						writer.append(Double.toString((double) section.getDefaultValue()));
						break;
					case Boolean:
						writer.append(Boolean.toString((boolean) section.getDefaultValue()));
						break;
					case String:
						writer.append((String)section.getDefaultValue());
						break;
					default:
						log.error("Did something go wrong? tried wrinting an primitive dataType while input wasn't an primitive dataType");
						break;
					}
					writer.newLine();
					writer.flush();
				}
				writer.newLine();
				writer.flush();
			}
			if(!section.getHideOutLines())
			{
				writer.append(section.getEnder());
				writer.newLine();
			}
		}
		writer.flush();
	}
	
	/**
	 * Note, it isn't recommended to Call this if you access this method through the ConfigFile.Class!
	 * Use "getValue(String)" instead.
	 * @param sectionName
	 * @param reader
	 * @return - The value from the ConfigFile, or the Default Value if an error occurred.
	 * @throws IOException
	 */
	public Object getValue(String sectionName, BufferedReader reader) throws IOException
	{
		String readedLine;
		ConfigSection configSection = this.getSubSection(sectionName);
		SectionType sectiontype = configSection.getSectionType();
		StringBuilder finalString = new StringBuilder();
		
		getValueLoop:
		while((readedLine = reader.readLine()) != null)
		{
			if(readedLine.startsWith(sectionName))
			{
				
				Pattern p1;
				if(sectiontype.isArray())
					p1 = Pattern.compile(";//{");
				else p1 = Pattern.compile(";");
				
				String[] value = p1.split(readedLine);
				for(String s1 : value)
				{
					if(s1.length() > 0 && !s1.startsWith(sectionName))
					{
						StringBuilder SB1 = new StringBuilder();
						Pattern p2 = Pattern.compile("\\s");
						String[] s2 = p2.split(s1);
						for(String s3 : s2)
						{
							if(sectiontype.isArray())
							{
								if(s3.length() > 0 && !s3.endsWith("};"))
									SB1.append(s3);
								else if(s3.length() > 2 && s3.endsWith("};"))
									SB1.append(s3.substring(0, s3.length()-2));
							}
							else
							{
								if(s3.length() > 0 && !s3.endsWith(";"))
									SB1.append(s3);
								else if(s3.length() > 1 && s3.endsWith(";"))
									SB1.append(s3.substring(0, s3.length()-1));
							}
							String values = SB1.toString();
							
							if(sectiontype.isArray() && configSection.getSeparteLines())
							{
								StringBuilder SB2 = new StringBuilder();
								
								addArrayValues:
								while((readedLine = reader.readLine()) != null)
								{
									Pattern p3 = Pattern.compile("//s");
									String[] s5 = p3.split(readedLine);
									for(String s6 : s5)
									{
										if(s6.length() > 0)
										{
											if(s6.endsWith("};"))
											{
												SB2.append(s6.substring(0, readedLine.length()-2));
												break addArrayValues;
											}
											else
											{
												SB2.append(s6);
											}
										}
									}
								}
								values = SB2.toString();
							}
							if (values.length() > 0)
							{
								switch (sectiontype)
								{
								case Short:
									short finalShortValue;
									if (configSection.getDefaultValue() != null)
										finalShortValue = (short) configSection.getDefaultValue();
									else
										finalShortValue = -1;

									try
									{
										finalShortValue = Short.parseShort(values);
									}
									catch (Exception e)
									{
										log.catching(e);
									}
									return finalShortValue;

								case Integer:
									int finalIntegerValue;
									if (configSection.getDefaultValue() != null)
										finalIntegerValue = (int) configSection.getDefaultValue();
									else
										finalIntegerValue = -1;

									try
									{
										finalIntegerValue = Integer.parseInt(values);
									}
									catch (Exception e)
									{
										log.catching(e);
									}
									return finalIntegerValue;

								case Long:
									long finalLongValue;
									if (configSection.getDefaultValue() != null)
										finalLongValue = (long) configSection.getDefaultValue();
									else
										finalLongValue = -1;

									try
									{
										finalLongValue = Long.parseLong(values);
									}
									catch (Exception e)
									{
										log.catching(e);
									}
									return finalLongValue;

								case Float:
									float finalFloatValue;
									if (configSection.getDefaultValue() != null)
										finalFloatValue = (float) configSection.getDefaultValue();
									else
										finalFloatValue = -1;

									try
									{
										finalFloatValue = Float.parseFloat(values);
									}
									catch (Exception e)
									{
										log.catching(e);
									}
									return finalFloatValue;

								case Double:
									double finalDoubleValue;
									if (configSection.getDefaultValue() != null)
										finalDoubleValue = (double) configSection.getDefaultValue();
									else
										finalDoubleValue = -1;

									try
									{
										finalDoubleValue = Double.parseDouble(values);
									}
									catch (Exception e)
									{
										log.catching(e);
									}
									return finalDoubleValue;

								case Boolean:
									boolean finalBooleanValue;
									if (configSection.getDefaultValue() != null)
										finalBooleanValue = (boolean) configSection.getDefaultValue();
									else
										finalBooleanValue = false;

									try
									{
										finalBooleanValue = Boolean.parseBoolean(values);
									}
									catch (Exception e)
									{
										log.catching(e);
									}
									return finalBooleanValue;

								case String:
									String finalStringValue;
									if (configSection.getDefaultValue() != null)
										finalStringValue = (String) configSection.getDefaultValue();
									else
										finalStringValue = null;

									try
									{
										finalStringValue = values;
									}
									catch (Exception e)
									{
										log.catching(e);
									}
									return finalStringValue;

								case ShortArray:
									HashMap<Integer, Short> finalShortArrayValueMap = new HashMap<Integer, Short>();
									Pattern ShortP1 = Pattern.compile(",");
									String[] shortS1 = ShortP1.split(values);
									for (String shortS2 : shortS1)
									{
										if (shortS2.length() > 0)
										{
											try
											{
												finalShortArrayValueMap.put(finalShortArrayValueMap.size(), Short.parseShort(shortS2));
											}
											catch (Exception e)
											{
												log.catching(e);
											}
										}
										
										if (finalShortArrayValueMap.isEmpty())
											return null;
										else
										{
											short[] finalShortArrayValue = new short[finalShortArrayValueMap.size()];

											for (int i = 0; i < finalShortArrayValue.length; i++)
											{
												finalShortArrayValue[i] = finalShortArrayValueMap.get(i);
											}
											return finalShortArrayValue;
										}
									}

								case IntegerArray:
									HashMap<Integer, Integer> finalIntArrayValueMap = new HashMap<Integer, Integer>();
									Pattern intP1 = Pattern.compile(",");
									String[] intS1 = intP1.split(values);
									for (String intS2 : intS1)
									{
										if (intS2.length() > 0)
										{
											try
											{
												finalIntArrayValueMap
														.put(finalIntArrayValueMap
																.size(),
																Integer.parseInt(intS2));
											}
											catch (Exception e)
											{
												log.catching(e);
											}
										}
										
										if (finalIntArrayValueMap.isEmpty())
											return null;
										else
										{
											int[] finalIntArrayValue = new int[finalIntArrayValueMap.size()];

											for (int i = 0; i < finalIntArrayValue.length; i++)
											{
												finalIntArrayValue[i] = finalIntArrayValueMap.get(i);
											}
											return finalIntArrayValue;
										}
									}

								case LongArray:
									HashMap<Integer, Long> finalLongArrayValueMap = new HashMap<Integer, Long>();
									Pattern longP1 = Pattern.compile(",");
									String[] longS1 = longP1.split(values);
									for (String longS2 : longS1)
									{
										if (longS2.length() > 0)
										{
											try
											{
												finalLongArrayValueMap.put(finalLongArrayValueMap.size(), Long.parseLong(longS2));
											}
											catch (Exception e)
											{
												log.catching(e);
											}
										}
										
										if (finalLongArrayValueMap.isEmpty())
											return null;
										else
										{
											long[] finalLongArrayValue = new long[finalLongArrayValueMap.size()];

											for (int i = 0; i < finalLongArrayValue.length; i++)
											{
												finalLongArrayValue[i] = finalLongArrayValueMap.get(i);
											}
											return finalLongArrayValue;
										}
									}

								case FloatArray:
									HashMap<Integer, Float> finalFloatArrayValueMap = new HashMap<Integer, Float>();
									Pattern floatP1 = Pattern.compile(",");
									String[] floatS1 = floatP1.split(values);
									for (String floatS2 : floatS1)
									{
										if (floatS2.length() > 0)
										{
											try
											{
												finalFloatArrayValueMap.put(finalFloatArrayValueMap.size(), Float.parseFloat(floatS2));
											}
											catch (Exception e)
											{
												log.catching(e);
											}
										}
										
										if (finalFloatArrayValueMap.isEmpty())
											return null;
										else
										{
											float[] finalFloatArrayValue = new float[finalFloatArrayValueMap.size()];

											for (int i = 0; i < finalFloatArrayValue.length; i++)
											{
												finalFloatArrayValue[i] = finalFloatArrayValueMap.get(i);
											}
											return finalFloatArrayValue;
										}
									}

								case DoubleArray:
									HashMap<Integer, Double> finalDoubleArrayValueMap = new HashMap<Integer, Double>();
									Pattern doubleP1 = Pattern.compile(",");
									String[] doubleS1 = doubleP1.split(values);
									for (String doubleS2 : doubleS1)
									{
										if (doubleS2.length() > 0)
										{
											try
											{
												finalDoubleArrayValueMap.put(finalDoubleArrayValueMap.size(), Double.parseDouble(doubleS2));
											}
											catch (Exception e)
											{
												log.catching(e);
											}
										}
										
										if (finalDoubleArrayValueMap.isEmpty())
											return null;
										else
										{
											double[] finalDoubleArrayValue = new double[finalDoubleArrayValueMap.size()];

											for (int i = 0; i < finalDoubleArrayValue.length; i++) 
											{
												finalDoubleArrayValue[i] = finalDoubleArrayValueMap.get(i);
											}
											return finalDoubleArrayValue;
										}
									}

								case BooleanArray:
									HashMap<Integer, Boolean> finalBooleanArrayValueMap = new HashMap<Integer, Boolean>();
									Pattern booleanP1 = Pattern.compile(",");
									String[] booleanS1 = booleanP1.split(values);
									for (String booleanS2 : booleanS1)
									{
										if (booleanS2.length() > 0)
										{
											try
											{
												finalBooleanArrayValueMap.put(finalBooleanArrayValueMap.size(), Boolean.parseBoolean(booleanS2));
											}
											catch (Exception e)
											{
												log.catching(e);
											}
										}
										
										if (finalBooleanArrayValueMap.isEmpty())
											return null;
										else
										{
											boolean[] finalDoubleArrayValue = new boolean[finalBooleanArrayValueMap.size()];

											for (int i = 0; i < finalDoubleArrayValue.length; i++)
											{
												finalDoubleArrayValue[i] = finalBooleanArrayValueMap.get(i);
											}
											return finalDoubleArrayValue;
										}
									}

								case StringArray:
									HashMap<Integer, String> finalStringArrayValueMap = new HashMap<Integer, String>();
									Pattern stringP1 = Pattern.compile(",");
									String[] stringS1 = stringP1.split(values);
									for (String stringS2 : stringS1)
									{
										if (stringS2.length() > 0)
										{
											try
											{
												finalStringArrayValueMap.put(finalStringArrayValueMap.size(), stringS2);
											}
											catch (Exception e)
											{
												log.catching(e);
											}
										}
										
										if (finalStringArrayValueMap.isEmpty())
											return null;
										else
										{
											String[] finalStringArrayValue = new String[finalStringArrayValueMap.size()];

											for (int i = 0; i < finalStringArrayValue.length; i++)
											{
												finalStringArrayValue[i] = finalStringArrayValueMap
														.get(i);
											}
											return finalStringArrayValue;
										}
									}

								default:
									log.error("Did something go wrong? An Error ocured while trying to retrieve Value from: " + sectionName + "!");
									log.info("Its recommended to check/regenerate the configFile while this might be caused by a faulty input.");
									break getValueLoop;
								}
							}
						}
					}
				}
			}	
		}
		log.error("Couldn't find ConfigSection " + sectionName + " in the configFile!, returning Default value instead.");
		log.info("Its recommended to check/regenerate the configFile while this might be caused by a faulty input.");
		return configSection.getDefaultValue();
	}

}
