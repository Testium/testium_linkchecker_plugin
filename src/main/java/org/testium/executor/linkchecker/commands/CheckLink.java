package org.testium.executor.linkchecker.commands;

import java.net.HttpURLConnection;
import java.net.URL;
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
			"url", String.class, "The URL to check", false, true, true, false );

	public CheckLink( SutInterface iface ) {
		super( COMMAND, "Will check if a link is alive (i.e. responds with 2xx)", iface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_URL );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		String url = (String) this.obtainValue(aVariables, parameters, PARSPEC_URL);
		isLive(url);
	}
	
	
	private static void isLive(String link) throws Exception {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(link);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("HEAD");
			urlConnection.setConnectTimeout(5000); /*
													 * timeout after 5s if can't
													 * connect
													 */
			urlConnection.setReadTimeout(5000); /*
												 * timeout after 5s if the page
												 * is too slow
												 */
			urlConnection.connect();
			String redirectLink = urlConnection.getHeaderField("Location");
			if (redirectLink != null && !link.equals(redirectLink)) {
				isLive(redirectLink);
			} else {
				if ( urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK ) {
System.out.println( "OK - Checked link " + link);
				} else {
System.out.println( "NOK - response " + urlConnection.getResponseCode() + " for " + link);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}
}
