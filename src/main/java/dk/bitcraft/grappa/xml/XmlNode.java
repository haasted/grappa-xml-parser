package dk.bitcraft.grappa.xml;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

public class XmlNode {
	// TODO Parent
	List<XmlNode> children = newArrayList();
	
	@Override
	public String toString() {
		return Joiner.on(' ').join(children);
	}
	
	public String toStringTree() {
		return toStringTree(0);
	}
	
	private String toStringTree(int indent) {		
		StringBuffer sb = new StringBuffer(Strings.repeat(" ", indent));
		sb.append(format("%s (%s)\n",getClass().getName(), toString()));
		
		for (XmlNode child : children) {
			sb.append(child.toStringTree( indent + 2));			
		}
		
		return sb.toString();
	}
}