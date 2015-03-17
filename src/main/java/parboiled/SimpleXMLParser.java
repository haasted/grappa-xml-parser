package parboiled;

import static com.google.common.base.Strings.isNullOrEmpty;

import org.parboiled.Action;
import org.parboiled.BaseParser;
import org.parboiled.Context;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SkipNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.StringVar;
import org.parboiled.support.Var;

@BuildParseTree 
public class SimpleXMLParser extends BaseParser<XmlNode> {
    Rule Document() {    	
    	return Sequence(createDocument, Element());
    }
    
    Rule Element() {
    	StringVar id = new StringVar();    	
    	return     		
			FirstOf(
				Sequence(OpenElement(id), ElementContent(), CloseElement(id), attachToParent),
				Sequence(ClosedElement(), attachToParent)
			);
		    	
    }
    
    Rule OpenElement(StringVar id) {    	
    	return Sequence('<', Identifier(), id.set(match()), createElement(id), ElementAttributes(), OptionalWhitespace(), '>');
    }
    
    Rule ClosedElement() {
    	StringVar id = new StringVar();
    	return Sequence('<', Identifier(), id.set(match()), createElement(id), ElementAttributes(), OptionalWhitespace(), "/>");
    }
    
    Rule ElementAttributes() {
    	Var<XmlElementAttribute> attr = new Var<XmlElementAttribute>();
    	return ZeroOrMore(OptionalWhitespace(), Identifier(), createElementAttribute(attr), Optional(ElementAttributeValue(attr)), insertElementAttribute(attr));
    }
    
    Rule ElementAttributeValue(Var<XmlElementAttribute> attr) {
    	// Change content rule
    	return Sequence("=",'"', AttributeContent(), recordElementAttributeValue(attr), '"');
    }
    
    Rule CloseElement(StringVar id) {    
    	return Sequence("</", Optional(Identifier()), matchStringVar(id), '>');
    }
        
	Rule ElementContent() {
    	return ZeroOrMore(FirstOf(Text(), Element()));
    }
	
	@SuppressSubnodes
	Rule Text() {
		return Sequence(OneOrMore(NoneOf("<>")), createTextNode);
	}
    
	@SkipNode
	Rule OptionalWhitespace() {
		return ZeroOrMore(AnyOf(" \t\f"));
	}
	
	@SuppressSubnodes
    Rule Identifier() {
        return OneOrMore(FirstOf( CharRange('A', 'z'), CharRange('0', '9') ));
    }    
    
    Rule AttributeContent() {
    	return ZeroOrMore(NoneOf("\""));
    }
    
    /* ----- ACTIONS  ------ */    
    Action<XmlNode> debugAction(final String msg) {
		return new Action<XmlNode>() {
			public boolean run(Context<XmlNode> context) {				
				System.out.printf(" *** %s (%s) \n", msg, context.getValueStack().size());
				return true;
			}			
		};    	
    }
    
    Action<XmlNode> createTextNode = new Action<XmlNode>() {
		public boolean run(Context<XmlNode> context) {			
			TextNode textNode = new TextNode();
			textNode.content = match();
			
			XmlNode peek = peek();
			peek.children.add(textNode);
			
			return true;
		}

		public String toString() {
			return "CreateTextNode action";
		};
	};
    	
	Action<XmlNode> createElementAttribute(final Var<XmlElementAttribute> attributeVar) {
		return new Action<XmlNode>() {
			public boolean run(Context<XmlNode> ctx) {
				XmlElementAttribute attr = new XmlElementAttribute();
				attr.id = ctx.getMatch();				
				attributeVar.set(attr);
				return true;
			}
			
			@Override
			public java.lang.String toString() {
				return "CreateElementAttribute action";
			}
		};
	};
	
	Action<XmlNode> recordElementAttributeValue(final Var<XmlElementAttribute> attributeVar) {
		return new Action<XmlNode>() {
			public boolean run(Context<XmlNode> ctx) {
				XmlElementAttribute attr = attributeVar.get();
				attr.value = ctx.getMatch();				
				return true;
			}			
			
			@Override
			public java.lang.String toString() {
				return "RecordElementAttributeValue action";
			}
		};		
	};
	
	
	Action<XmlNode> insertElementAttribute(final Var<XmlElementAttribute> attributeVar) {
		return new Action<XmlNode>() {
			public boolean run(Context<XmlNode> ctx) {
				XmlElement currentElement = (XmlElement) peek();		
				currentElement.attributes.add(attributeVar.get());

				return true;
			}
			
			@Override
			public java.lang.String toString() {
				return "insertElementAttribute action";
			}
		};
	};
	
	/** Create document element and push it on the value stack. Performed as an action in case the parser wants to make several runs. */
	final Action<XmlElement> createDocument = new Action<XmlElement>() {		
		public boolean run(Context<XmlElement> context) {			
			return push(new XmlDocument());
		}
	};
	
    Action<XmlElement> createElement(final StringVar name) {
    	return new Action<XmlElement>() {
			public boolean run(Context<XmlElement> context) {
				return push(new XmlElement(name.get()));
			} 
			
			@Override
			public java.lang.String toString() {
				return "createElement action";
			}
		};
    }
    
    final Action<XmlElement> attachToParent = new Action<XmlElement>() {
		public boolean run(Context<XmlElement> context) {
			XmlNode peek = peek(1);
			peek.children.add(pop());
			return true;
		}
	};
    
    Action<?> matchStringVar(final StringVar var) {
    	return new Action<Object>() {
			public boolean run(Context<Object> ctx) {
				String match = ctx.getMatch();
				
				if (isNullOrEmpty(match))
					return true;
				
				return match.equals(var.get());
			}
		};
    }
}