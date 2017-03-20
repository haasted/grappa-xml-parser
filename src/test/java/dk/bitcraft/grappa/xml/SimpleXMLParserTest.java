package dk.bitcraft.grappa.xml;

import com.google.common.base.Function;

import com.github.fge.grappa.Grappa;
import com.github.fge.grappa.run.AbstractParseRunner;
import com.github.fge.grappa.run.ListeningParseRunner;
import com.github.fge.grappa.run.ParsingResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import java.util.List;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SimpleXMLParserTest {

	private static AbstractParseRunner<XmlNode> runner;

	@Before
	public void setup() {
		SimpleXMLParser parser = Grappa.createParser(SimpleXMLParser.class);
		runner = new ListeningParseRunner<XmlNode>(parser.Document());
//		runner = new ReportingParseRunner<XmlNode>(parser.Document());
//		runner = new TracingParseRunner<XmlNode>(parser.Document());
	}
	
	@Parameters(name = "{index}: {0} [{1}]")
	public static Iterable<Object[]> inputs() {	
		List<String> valid = newArrayList(
				"<ab></ab>",
				"<ab />",
				"<AB>content</AB>",
				"<AcB></>", // Allow closing tags without identifier.
				"<aab verified></>",
				
				// Attributes
				"<aab verified=\"false\"></>",				
				"<aab verified=\"special chars <>\"></>",				
							
				// Nested elements
				"<ab><cd></cd><ef /></ab>", 
				
				// Mixed content
				"<ab>text<cd />more text</ab>",
				
				"<aab ></>"				
				);
		
		List<String> invalid = newArrayList(
				"<aab<",
				"<AB>content</CD>", 
				"<AcB></ACB>"
				);
		
		return concat(transform(valid, VALID), transform(invalid, INVALID));
	}	
	
	private final String input;
	private final boolean valid;
	public SimpleXMLParserTest(String input, boolean valid) {
		System.out.println("\n--- Testing " + input);
		this.input = input;
		this.valid = valid;
	}
	
	@Test
	public void test() {
		assertEquals(input, valid, runner.run(input).isSuccess());
		
		if (valid) {
			ParsingResult<XmlNode> result = runner.run(input);
			XmlNode resultValue = result.getTopStackValue();
			System.out.println(resultValue.toString());
			
			assertTrue(resultValue instanceof XmlDocument);
			XmlDocument doc = (XmlDocument) resultValue;
			assertEquals(1, doc.children.size());		
			
			System.out.println(resultValue.toStringTree());
		}		
	}	
	
	private static Function<String, Object[]> VALID = new Function<String, Object[]>() {		
		public Object[] apply(String input) {
			return new Object[] { input, true };
		}
	};
	
	private static Function<String, Object[]> INVALID = new Function<String, Object[]>() {		
		public Object[] apply(String input) {
			return new Object[] { input, false };
		}
	};	
}
