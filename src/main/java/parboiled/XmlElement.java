package parboiled;

import static java.lang.String.format;

import com.google.common.base.Joiner;

public class XmlElement extends XmlNode {
	final String name;

	public XmlElement(String name) {
		this.name = name;
	}
	
//	List<XmlElementAttribute> attributes = newArrayList();
	XmlElementAttributeSet attributes = new XmlElementAttributeSet();
	
	@Override
	public String toString() {
		if (children.isEmpty()) {
			return format("<%s%s/>", name, attributesToString());
		} else {
			return format("<%s%s>%s</%1$s>", name, attributesToString(), super.toString());			
		}		
	}
	
	private String attributesToString() {
		if (attributes.isEmpty())
			return "";
		
		return " " + Joiner.on(' ').join(attributes) + " ";
	}
}