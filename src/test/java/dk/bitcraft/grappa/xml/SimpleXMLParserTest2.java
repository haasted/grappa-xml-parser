package dk.bitcraft.grappa.xml;

import com.google.common.collect.Iterables;

import com.github.fge.grappa.Grappa;
import com.github.fge.grappa.run.AbstractParseRunner;
import com.github.fge.grappa.run.ListeningParseRunner;
import com.github.fge.grappa.run.ParsingResult;

import org.junit.Before;
import org.junit.Test;


import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleXMLParserTest2 {

	private static AbstractParseRunner<XmlNode> runner;

	@Before
	public void setup() {
		SimpleXMLParser parser = Grappa.createParser(SimpleXMLParser.class);
		runner = new ListeningParseRunner<XmlNode>(parser.Document());
//		runner = new ReportingParseRunner<XmlNode>(parser.Document());
//		runner = new TracingParseRunner<XmlNode>(parser.Document());
	}
	
	
	@Test
	public void test01() {
		String input = "<a attr1></a>";
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.isSuccess());
		
		XmlElement el = (XmlElement) result.getTopStackValue().children.get(0);
		assertEquals(1, el.attributes.size());		

		XmlElementAttribute attribute = Iterables.getOnlyElement(el.attributes);
		assertEquals("attr1", attribute.id); 
	}	
	
	
	@Test
	public void test02() {
		String input = "<a attr1 attr2></a>";
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.isSuccess());
		
		XmlElement el = (XmlElement) result.getTopStackValue().children.get(0);
		assertEquals(2, el.attributes.size());		
		
		assertEquals("attr1", Iterables.get(el.attributes, 0).id); 
		assertEquals("attr2", Iterables.get(el.attributes, 1).id);
	}	
	
	
	@Test
	public void test03() {
		String input = "<a attr1 attr1></a>";
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertFalse(result.isSuccess()); // Two identically named attributes.
	}	
	
	@Test
	public void test04() {
		String input = "<a attr1=\"val1\" attr1></a>";
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertFalse(result.isSuccess()); // Two identically named attributes.
	}	
	
	@Test
	public void verifyInsertionOrder1() {
		String input = "<a attr1 attr2 attr3 attr4 />";
		
		ParsingResult<XmlNode> result = runner.run(input);
		
		assertTrue(result.isSuccess());
		
		XmlElement el = (XmlElement) result.getTopStackValue().children.get(0);
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
		
		assertTrue(result.isSuccess());
		
		XmlElement el = (XmlElement) result.getTopStackValue().children.get(0);
		assertEquals(4, el.attributes.size());

		Iterator<XmlElementAttribute> it = el.attributes.iterator();
		assertEquals("attr8", it.next().id);
		assertEquals("attr7", it.next().id);
		assertEquals("attr6", it.next().id);
		assertEquals("attr5", it.next().id);		
	}
}
