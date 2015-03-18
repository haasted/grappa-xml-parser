# grappa-xml-parser

This is an experiment in creating a parser using the [Grappa framework](https://github.com/fge/grappa) (formerly [parboiled](https://github.com/sirthias/parboiled)).

The parser reads a simplified variant of XML, which allows for some leniency in comparison to actual XML parsers:
* Element names in closing tags are optional.
* Attributes are not required to have a value specified, thus avoiding the common `enabled="true|on|YES|1"` confusion. 

A few examples of "XML documents" that are accepted by the parser

###### Omit the identifier of a closing tag
```xml
<a>Content <b/></>
```

###### Omit the value of an attribute
```xml
<a enabled>Content</a>
```

As the parser was created as an experiment, it doesn't include support for some of the more advanced aspects of XML, e.g. namespaces, doctype headers, xml declarations etc. 


Please note that the project is intended as an example, and should never be used instead of an actual XML parsing component.
