package parboiled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.AbstractParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import com.google.common.collect.Iterables;

public class SimpleXMLParserTest2 {

	private static AbstractParseRunner<XmlNode> runner;

	@Before
	public void setup() {
		SimpleXMLParser parser = Parboiled.createParser(SimpleXMLParser.class);
		runner = new ReportingParseRunner<XmlNode>(parser.Document());
//		runner = new TracingParseRunner<XmlNode>(parser.Document());
	}
	
	
	@Test
	public void test01() {
		String input = "<a attr1></a>";
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.matched);
		
		XmlElement el = (XmlElement) result.resultValue.children.get(0);
		assertEquals(1, el.attributes.size());		

		XmlElementAttribute attribute = Iterables.getOnlyElement(el.attributes);
		assertEquals("attr1", attribute.id); 
	}	
	
	
	@Test
	public void test02() {
		String input = "<a attr1 attr2></a>";
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.matched);
		
		XmlElement el = (XmlElement) result.resultValue.children.get(0);
		assertEquals(2, el.attributes.size());		
		
		assertEquals("attr1", Iterables.get(el.attributes, 0).id); 
		assertEquals("attr2", Iterables.get(el.attributes, 1).id);
	}	
	
	
	@Test
	public void test03() {
		String input = "<a attr1 attr1></a>";
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.matched);
		
		XmlElement el = (XmlElement) result.resultValue.children.get(0);
		assertEquals(1, el.attributes.size());
		
		assertEquals("attr1", Iterables.get(el.attributes, 0).id);  
		assertNull(Iterables.get(el.attributes, 0).value);
	}	
	
	@Test
	public void test04() {
		String input = "<a attr1=\"val1\" attr1></a>";
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.matched);
		
		XmlElement el = (XmlElement) result.resultValue.children.get(0);
		assertEquals(1, el.attributes.size());
		
		assertEquals("attr1", Iterables.get(el.attributes, 0).id); 		
		assertEquals("val1", Iterables.get(el.attributes, 0).value); 
	}	
	
	@Test
	public void verifyInsertionOrder1() {
		String input = "<a attr1 attr2 attr3 attr4 />";
		
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.matched);
		
		XmlElement el = (XmlElement) result.resultValue.children.get(0);
		assertEquals(4, el.attributes.size());
		
		Iterator<XmlElementAttribute> it = el.attributes.iterator();
		assertEquals("attr1", it.next().id);
		assertEquals("attr2", it.next().id);
		assertEquals("attr3", it.next().id);
		assertEquals("attr4", it.next().id);		
	}
	
	@Test
	public void verifyInsertionOrder2() {
		String input = "<a attr8 attr7 attr6 attr5 />";
		
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.matched);
		
		XmlElement el = (XmlElement) result.resultValue.children.get(0);
		assertEquals(4, el.attributes.size());

		Iterator<XmlElementAttribute> it = el.attributes.iterator();
		assertEquals("attr8", it.next().id);
		assertEquals("attr7", it.next().id);
		assertEquals("attr6", it.next().id);
		assertEquals("attr5", it.next().id);		
	}
}
