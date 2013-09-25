package org.testium.executor.linkchecker.commands;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import net.sf.testium.executor.TestExecutionException;
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
			"url", String.class, "The URL to check", false, true, true, false );

	private static final SpecifiedParameter PARSPEC_CONNECT_TIMEOUT = new SpecifiedParameter( 
			"connectTimeout", Long.class, "timeout if it can't connect", true, true, true, false )
			.setDefaultValue( 5000L ); //milli-seconds

	private static final SpecifiedParameter PARSPEC_RESPONSE_TIMEOUT = new SpecifiedParameter( 
			"responseTimeout", Long.class, "timeout if the page is too slow", true, true, true, false )
			.setDefaultValue( 5000L ); //milli-seconds

	/**
	 * TODO
	 * - method (HEAD or GET)
	 * - followRedirections
	 * - saveResponse
	 * - allowedResponses
	 */

	public CheckLink( SutInterface iface ) {
		super( COMMAND, "Will check if a link is alive (i.e. responds with 2xx)", iface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_URL );
		this.addParamSpec( PARSPEC_CONNECT_TIMEOUT );
		this.addParamSpec( PARSPEC_RESPONSE_TIMEOUT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		String url = (String) this.obtainValue(aVariables, parameters, PARSPEC_URL);
		int connectTimeout = (int) (long) (Long) this.obtainOptionalValue(aVariables, parameters, PARSPEC_CONNECT_TIMEOUT);
		int responseTimeout = (int) (long) (Long) this.obtainOptionalValue(aVariables, parameters, PARSPEC_RESPONSE_TIMEOUT);

		isLive(url, connectTimeout, responseTimeout);
	}
	
	
	private static void isLive(String link, int connectTimeout, int responseTimeout) throws Exception {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(link);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("HEAD");
			urlConnection.setConnectTimeout(connectTimeout); 
			urlConnection.setReadTimeout(responseTimeout);

			urlConnection.connect();
			String redirectLink = urlConnection.getHeaderField("Location");
			if (redirectLink != null && !link.equals(redirectLink)) {
				isLive(redirectLink, connectTimeout, responseTimeout);
			} else {
				if ( urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK ) {
					throw new TestExecutionException( "Got response " + urlConnection.getResponseCode() + " for " + link);
				}
			}
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}
}
