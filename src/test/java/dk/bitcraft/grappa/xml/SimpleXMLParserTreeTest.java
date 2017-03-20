package dk.bitcraft.grappa.xml;

import com.github.fge.grappa.Grappa;
import com.github.fge.grappa.run.AbstractParseRunner;
import com.github.fge.grappa.run.ListeningParseRunner;
import com.github.fge.grappa.run.ParsingResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SimpleXMLParserTreeTest {
	private static AbstractParseRunner<XmlNode> runner;

	@Before
	public void setup() {
		SimpleXMLParser parser = Grappa.createParser(SimpleXMLParser.class);
		runner = new ListeningParseRunner<XmlNode>(parser.Document());
//		runner = new TracingParseRunner<XmlElement>(parser.Document());		
	}
		
	@Parameters(name = "{index}: {0}")
	public static Iterable<Object[]> inputs() {
		// Desperate attempt at finding a shorthand notation for test specifications.
		return new ArrayList<Object[]>() {{
				__("<a><b/></a>", 2);
				__("<a><b></b></a>", 2);
				__( "<c> This is content </c>", 2 );
				__( "<c> This is content <b /></c>", 3 );
				__( "<c> This is content <b></b> </c>", 4 );
				__( "<c> This is content <b /> </c>", 4 );
				__( "<a><b><c><d /></c></b></a>", 4 );
			}
		
			void __(Object ... args) {
				this.add(args);
			}
		};	
	}
	
	final String input;
	final int nodeCount;
	
	public SimpleXMLParserTreeTest(String input, int nodeCount) {
		this.input = input;
		this.nodeCount = nodeCount;
	}
		
	@Test
	public void test() {
		XmlNode resultValue = null;
		try {			
			ParsingResult<XmlNode> result = runner.run(input);
			assertTrue(result.isSuccess());
			resultValue = result.getTopStackValue();
			assertNotNull(resultValue);
			
			assertTrue(resultValue instanceof XmlDocument);
			assertEquals(1, resultValue.children.size());
			
			resultValue = resultValue.children.get(0); // Ignore document wrapper when counting nodes.
			assertEquals(nodeCount, countNodesInTree(resultValue));		
		} catch(AssertionError e) {
			if (resultValue != null) {
				System.out.println("- Error report for input :" + input);
				System.out.println(resultValue.toStringTree());
			}
			throw e;
		}
	}	
	
	private static int countNodesInTree(XmlNode el) {
		int count = 1;
		
		for (XmlNode child : el.children) {
			count += countNodesInTree(child);
		} 
		
		return count;
	}
		
}
