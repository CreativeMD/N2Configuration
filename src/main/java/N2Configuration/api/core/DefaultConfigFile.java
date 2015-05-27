package N2Configuration.api.core;

import java.io.File;

public class DefaultConfigFile extends ConfigFile
{
	private static final String sectionOutLine = "-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/";
	private static String[] fileDescription;


	public DefaultConfigFile(String fileName, String configName, File targetDirectory)
	{
		super(fileName, targetDirectory);

		this.setCustomSectionStarter(sectionOutLine);
		this.setCustomSectionHeadEnder(sectionOutLine);
		this.setCustomSectionEnder(sectionOutLine);
		
		
		this.fileDescription = new String[]
			{
				configName +  " Config File",
				" ",
				"warning, its recomended to backup a custom WORKING config file before editing.",
				"any misstakes may cause an resset!"
			};
		this.setDescription(fileDescription);
	}

	@Override
	public void generateFile() {}

	
}
