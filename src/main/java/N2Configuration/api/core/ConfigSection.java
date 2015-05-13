package N2Configuration.api.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import N2Configuration.api.N2ConfigApi;

public class ConfigSection
{
	private Logger log = N2ConfigApi.log;
	/**
	 * Also function as sectionID
	 */
	private String sectionName;
	private String[] description = new String[]{};
	private Object defaultValue;
	private SectionType sectionType;
	private boolean seperateLines;
	private boolean hideOutLines;
	
	/**
	 * Default sectionStarter
	 */
	private String SectionStarter = "-----------------------------------------------------------------------------------------------------";
	/**
	 * Default sectionHeadEnder is the same as SectionStarter
	 */
	private String SectionHeadEnder = "";
	/**
	 * Default sectionEnder
	 */
	private String SectionEnder   = "-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-";
	
	public enum SectionType
	{
		Short(false),
		ShortArray(true),
		Integer(false),
		IntegerArray(true),
		Long(false), 
		LongArray(true),
		Float(false),
		FloatArray(true),
		Double(false),
		DoubleArray(true),
		Boolean(false),
		BooleanArray(true),
		String(false),
		StringArray(true),
		Text(false),
		SectionHead(true);
		
		private final boolean isArray;
		private SectionType(final boolean isArray) {this.isArray = isArray;}
		public boolean isArray() {return this.isArray;}
	}
	
	/**
	 * This will create a new ConfigSection. Note, this won't be saved automatically!
	 * @param sectionName - This will be displayed before the Value inside the ConfigurationFile. This will also function as ID!
	 * @param description - This will be displayed above the section Input. Each String represents a new Line.
	 * @param defaultValue - This will be printed if the ConfigurationFile is created/regenerated.
	 * @param sectionType - This will define which type of data you wan't to add.
	 * @param separateLines - If the sectionType is an ArrayType, this will cause each defaultValue to be printed on a separate line.
	 * @param hideHeader - If the header should be printed.
	 */
	public ConfigSection(String sectionName, String[] description, Object defaultValue, SectionType sectionType, boolean separateLines, boolean hideOutLines)
	{
		this.sectionName = sectionName;
		this.description = description;
		this.sectionType = sectionType;
		this.defaultValue = defaultValue;
		this.seperateLines = separateLines;
		this.hideOutLines = hideOutLines;
		
		if(hideOutLines && description != null)
		{
			log.error(sectionName + ": hideOutLines can't be false if description isn't null!");
			log.info("Setting hideOutLines to false.");
			this.hideOutLines = false;
		}
	}

	/**
	 * This will create a new ConfigSection. Note, this won't be saved automatically!
	 * @param sectionName - This will be displayed before the Value inside the ConfigurationFile. This will also function as ID!
	 * @param description - This will be displayed above the section Input. Each String represents a new Line.
	 * @param defaultValue - This will be printed if the ConfigurationFile is created/regenerated.
	 * @param sectionType - This will define which type of data you wan't to add.
	 * @param hideOutLines - If the sectionType is an StringArray, this will cause each String to be printed on a separate line.
	 */
	public ConfigSection(String sectionName, String[] description, Object defaultValue, SectionType sectionType, boolean hideOutLines)
	{
		this(sectionName, description, defaultValue, sectionType, false, hideOutLines);
	}
	
	/**
	 * This will create a new ConfigSection. Note, this won't be saved automatically!
	 * @param sectionName - This will be displayed before the Value inside the ConfigurationFile. This will also function as ID!
	 * @param description - This will be displayed above the section Input. Each String represents a new Line.
	 * @param defaultValue - This will be printed if the ConfigurationFile is created/regenerated.
	 * @param sectionType - This will define which type of data you wan't to add.
	 */
	public ConfigSection(String sectionName, String[] description, Object defaultValue, SectionType sectionType)
	{
		this(sectionName, description, defaultValue, sectionType, false, false);
	}
	
	 /**
	  * This will create a new ConfigSection. Note, this won't be saved automatically!
	  * @param sectionName - This will be displayed before the Value inside the ConfigurationFile. This will also function as ID!
	  * @param description - This will be displayed above the section Input. Each String represents a new Line.
	  * @param sectionType - This will define which type of data you wan't to add.
	  */
	public ConfigSection(String sectionName, String[] description, SectionType sectionType)
	{
		this(sectionName, description, null, sectionType, false, false);
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
	
	public boolean getHideOutLines()
	{
		return this.hideOutLines;
	}
	
	public String getStarter()
	{
		return this.SectionStarter;
	}
	
	public String getHeadEnder()
	{
		if(this.SectionHeadEnder.length() > 0)
			return this.SectionHeadEnder;
		else return this.SectionStarter;
	}
	
	public String getEnder()
	{
		return this.SectionEnder;
	}
	
	/**
	 * this will set a custom SectionStarter, this can be done for each individual section!
	 * Symbols that might cause a conflict: '{''}'';'',''#'
	 * @param sectionStarter
	 */
	public void setCustomSectionStarter(String sectionStarter)
	{
		this.SectionStarter = sectionStarter;
	}
	
	/**
	 * this will set a custom SectionHeadEnder, this can be done for each individual section!
	 * Symbols that might cause a conflict: '{''}'';'',''#'
	 * @param sectionStarter
	 */
	public void setCustomSectionHeadEnder(String sectionHeadEnder)
	{
		this.SectionHeadEnder = sectionHeadEnder;
		this.hideOutLines = false;
	}
	
	/**
	 * this will set a custom SectionStarter, this can be done for each individual section!
	 * Symbols that might cause a conflict: '{''}'';'',''#'
	 * @param sectionStarter
	 */
	public void setCustomSectionEnder(String sectionEnder)
	{
		this.SectionEnder = sectionEnder;
	}
	
	public void setDescription(String[] description)
	{
		this.description = description;
	}
	
	public void setDefaultValue(Object defaultvalue)
	{
		this.defaultValue = defaultvalue;
	}
}
