package parboiled;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.AbstractParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParsingResult;

import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
public class SimpleXMLParserTreeTest {
	private static AbstractParseRunner<XmlNode> runner;

	@Before
	public void setup() {
		SimpleXMLParser parser = Parboiled.createParser(SimpleXMLParser.class);
		runner = new ReportingParseRunner<XmlNode>(parser.Document());
//		runner = new TracingParseRunner<XmlElement>(parser.Document());
		
//		System.out.println(format("\n --- %s :",	 input));
	}
		
	@Parameters(name = "{index}: {0}")
	public static Iterable<Object[]> inputs() {
		List<Object[]> result = Lists.newArrayList();

		result.add( new Object[] { "<a><b/></a>", 2 } );
		result.add( new Object[] { "<a><b></b></a>", 2 } );
		result.add( new Object[] { "<c> This is content </c>", 2 } );
		result.add( new Object[] { "<c> This is content <b /></c>", 3 } );
		result.add( new Object[] { "<c> This is content <b></b> </c>", 4 } );
		result.add( new Object[] { "<c> This is content <b /> </c>", 4 } );
		result.add( new Object[] { "<a><b><c><d /></c></b></a>", 4 } );
		
		return result;
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
			assertTrue(result.matched);
			resultValue = result.resultValue;
			assertNotNull(resultValue);
			
			assertTrue(resultValue instanceof XmlDocument);
			assertEquals(1, resultValue.children.size());
			
			resultValue = resultValue.children.get(0); // Ignore document wrapper when counting nodes.
			
	//		System.out.println(resultValue);
	//		System.out.println(format("Nodes: %s expected, %s found", nodeCount, countNodesInTree(resultValue)));
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
