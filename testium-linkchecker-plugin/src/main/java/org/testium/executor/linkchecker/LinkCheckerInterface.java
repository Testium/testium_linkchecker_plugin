package org.testium.executor.linkchecker;

import org.testium.executor.linkchecker.commands.CheckLink;
import org.testium.executor.linkchecker.commands.GetLinks;
import net.sf.testium.executor.CustomInterface;

/**
 *  
 * @author Arjan Kranenburg
 *
 */
public class LinkCheckerInterface extends CustomInterface {

	/**
	 * 
	 */
	public LinkCheckerInterface(String aName) {
		super( aName );

		add( new CheckLink( this ) );
		add( new GetLinks( this ) );
	}

	@Override
	public String toString()
	{
		return this.getInterfaceName();
	}
}
