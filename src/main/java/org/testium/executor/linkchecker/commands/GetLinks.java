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

/**
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetLinks extends GenericCommandExecutor
{
	private static final String COMMAND = "getLinks";

	public static final SpecifiedParameter PARSPEC_HTML_FILE = new SpecifiedParameter( 
			"html_file", String.class, "The HTML file to fetch the links from", false, true, true, false );

	public GetLinks( SutInterface iface ) {
		super( COMMAND, "Fetches the links from an HTML file", iface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_HTML_FILE );
	}

	@Override
	public TestStepResult execute(TestStepCommand aStep,
			RunTimeData aVariables, File aLogDir) throws TestSuiteException {
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepCommandResult result = new TestStepCommandResultImpl(aStep);

		try {
			String fileName = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_HTML_FILE);
System.out.println("FileName obtained " + fileName);
			
			File HTML_File = new File( aLogDir, fileName );
			Document doc = Jsoup.parse(HTML_File, "UTF-8");
System.out.println("Parsed HTML doc " + doc.title());
			Elements linkElements = doc.select("a[href]");
System.out.println("Elements with linked " + linkElements.size() );
			Iterator<Element> elemItr = linkElements.iterator();
			while ( elemItr.hasNext() ) {
				Element elem = elemItr.next();
				String link = elem.attr("abs:href");
System.out.println("Found link: " + link );
			}

			result.setResult(VERDICT.PASSED);
		} catch (Exception e) {
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
