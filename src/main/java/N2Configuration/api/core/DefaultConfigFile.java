package N2Configuration.api.core;

import java.io.File;

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
