package dk.bitcraft.grappa.xml;

import static java.lang.String.format;

import com.google.common.base.Objects;

public class XmlElementAttribute {
	String id, value;
	
	
	@Override
	public String toString() {
		if (value == null) 
			return id;
		
		return format("%s=\"%s\">", id, value);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj instanceof XmlElementAttribute) {
			return Objects.equal(id, ((XmlElementAttribute) obj).id);
		}
		
		return false;
	}
}