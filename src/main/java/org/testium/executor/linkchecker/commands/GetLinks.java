package org.testium.executor.linkchecker.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.testium.executor.general.GenericCommandExecutor;
import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.systemundertest.SutInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.impl.TestStepCommandResultImpl;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetLinks extends GenericCommandExecutor
{
	private static final String COMMAND = "getLinks";

	public static final SpecifiedParameter PARSPEC_HTML_FILE = new SpecifiedParameter( 
			"htmlFile", String.class, "The HTML file to fetch the links from", false, true, true, false );

	public static final SpecifiedParameter PARSPEC_LIST_NAME = new SpecifiedParameter( 
			"listName", String.class, "The name of the list to store", false, true, true, false );

	public static final SpecifiedParameter PARSPEC_FILTER = new SpecifiedParameter( 
			"filter", String.class, 
			"The URL is only added to the list if it matches this Regular Expression. See also http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#sum",
			true, true, true, false ).setDefaultValue(".*");

	public GetLinks( SutInterface iface ) {
		super( COMMAND, "Fetches the links from an HTML file", iface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_HTML_FILE );
		this.addParamSpec( PARSPEC_LIST_NAME );
		this.addParamSpec( PARSPEC_FILTER );
	}

	@Override
	public TestStepResult execute(TestStepCommand aStep,
			RunTimeData aVariables, File aLogDir) throws TestSuiteException {
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepCommandResult result = new TestStepCommandResultImpl(aStep);

		try {
			String fileName = (String) this.obtainValue(aVariables, parameters, PARSPEC_HTML_FILE);
			String listName = (String) this.obtainValue(aVariables, parameters, PARSPEC_LIST_NAME);
			String filter = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_FILTER);

			ArrayList<String> list = new ArrayList<String>();

			File HTML_File = new File( aLogDir, fileName );
			Document doc = Jsoup.parse(HTML_File, "UTF-8");
			Elements linkElements = doc.select("a[href]");
			Iterator<Element> elemItr = linkElements.iterator();
			while ( elemItr.hasNext() ) {
				Element elem = elemItr.next();
				String link = elem.attr("abs:href");
				if ( ! link.isEmpty() ) {
					if ( link.matches(filter) ) {
						list.add(link);
					}
				}
			}

			RunTimeVariable rtVariable = new RunTimeVariable( listName, list );
			aVariables.add(rtVariable);

			result.setResult(VERDICT.PASSED);
		} catch (Exception e) {
e.printStackTrace();
			failTest(aLogDir, result, e);
		}

		return result;
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		throw new Error( "Method should not have been called" );
	}
}
