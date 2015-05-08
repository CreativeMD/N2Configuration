package N2Configuration.api.core;

import java.util.ArrayList;
import java.util.List;

public class ConfigSection
{
	private String sectionName;
	private String[] description;
	private Object defaultValue;
	private SectionType sectionType;
	private boolean seperateLines;
	
	public enum SectionType
	{
		Integer,
		String,
		StringArray
	}

	/**
	 * This will create a new ConfigSection. Note, this won't be saved automatically.
	 * @param sectionName - This will be displayed before the Value inside the ConfigurationFile. This will also function as ID!
	 * @param description - This will be displayed above the section Input. Each String represents a new Line.
	 * @param defaultValue - This will be printed if the ConfigurationFile is created/regenerated.
	 * @param sectionType - This will define which type of data you wan't to store.
	 * @param separateLines - If the sectionType is an StringArray, this will cause each String to be printed on a separate line.
	 */
	public ConfigSection(String sectionName, String[] description, Object defaultValue, SectionType sectionType, boolean separateLines)
	{
		this.sectionName = sectionName;
		this.description = description;
		this.defaultValue = defaultValue;
		this.sectionType = sectionType;
		this.seperateLines = separateLines;
	}
	
	public String getSectionName()
	{
		return this.sectionName;
	}
	
	public String[] getDescription()
	{
		return this.description;
	}
	
	public Object getDefaultValue()
	{
		return this.defaultValue;
	}
	
	public SectionType getSectionType()
	{
		return this.sectionType;
	}
	
	public boolean getSeparteLines()
	{
		return this.seperateLines;
	}
}
