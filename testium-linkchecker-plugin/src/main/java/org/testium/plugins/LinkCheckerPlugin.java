package org.testium.plugins;

import org.testium.executor.linkchecker.LinkCheckerInterface;
import org.testtoolinterfaces.utils.RunTimeData;

import net.sf.testium.configuration.ConfigurationException;
import net.sf.testium.executor.SupportedInterfaceList;
import net.sf.testium.plugins.Plugin;
import net.sf.testium.plugins.PluginCollection;


/**
 * @author Arjan Kranenburg
 *
 */
public class LinkCheckerPlugin implements Plugin
{
	private static final String INTERFACE_NAME = "Links";
	public LinkCheckerPlugin() {
		// nop
	}
	
	public void loadPlugIn(
	                        PluginCollection aPluginCollection,
	                        RunTimeData anRtData
	                      ) throws ConfigurationException {
		SupportedInterfaceList interfaceList = aPluginCollection.getInterfaces();

		// Create new Interface: Links
		LinkCheckerInterface iface = new LinkCheckerInterface( INTERFACE_NAME );
		interfaceList.add(iface);

//		createCustomKeywords(anRtData, interfaceList, aTestStepMetaExecutor,
//				ifConfig, iface);
	}
}
