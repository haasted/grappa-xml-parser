package parboiled;


public class XmlDocument extends XmlNode {
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if (children.isEmpty())
			sb.append("[EMPTY]\n");
		
		if (children.size() > 1) 
			sb.append("WARN : Document seems malformed. contains more than one root.\n" );
		
		for (XmlNode root : children) {
			sb.append(root.toString() + "\n");
		}
		
		return sb.toString();
	}
}