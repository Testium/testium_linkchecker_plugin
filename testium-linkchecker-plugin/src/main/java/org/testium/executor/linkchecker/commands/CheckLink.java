package org.testium.executor.linkchecker.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.GenericCommandExecutor;
import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan Kranenburg
 *
 */
public class CheckLink extends GenericCommandExecutor
{
	private static final String COMMAND = "checkLink";

	public static final SpecifiedParameter PARSPEC_URL = new SpecifiedParameter( 
			"usr", String.class, "The URL to check", false, true, true, false );

	public CheckLink( SutInterface iface ) {
		super( COMMAND, "Will check if a link is alive (i.e. responds with 2xx)", iface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_URL );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

	}
}
