package domainmodel;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * XML DomainModel data file handler. 
 * <p>
 * This handler converts the XML file into a populated DomainModel instance.
 * <p>
 * Created: Jan 20, 2007
 * 
 * @author <a href="mailto:justinrknowles@gmail.com">Justin R. Knowles</a>
 */
public class DomainModelHandler extends DefaultHandler {
    
    private static final String CLASSNAME = DomainModelHandler.class.getName();
    
    private Logger logger = Logger.getLogger(CLASSNAME);

    public static final String TAG_DOMAIN_MODEL = "domainModel";
    public static final String TAG_ENTITIES = "entities";
    public static final String TAG_ENTITY = "entity";
    public static final String ATTR_ENTITY_NAME = "name";
    public static final String ATTR_ENTITY_PACKAGE = "package"; 
    public static final String ATTR_ENTITY_SUPERCLASS = "super-class";
    public static final String ATTR_ENTITY_TYPE = "type";
    public static final String TAG_ENTITY_DESCRIPTION = "entity-description";
    public static final String TAG_VALUES = "values";
    public static final String TAG_VALUE = "value";
    public static final String ATTR_VALUE_NAME = "name";
    public static final String ATTR_VALUE_CLASS = "class";
    public static final String TAG_VALUE_DESCRIPTION = "value-description";


    private DomainModel domainModel;
    private Entity entity;
    private Value value;
    
	/**
	 * startElement
	 * <p>
	 * Creates new objects for populating and sets values that are represented 
	 * in the document as attributes.
	 * 
	 * @param namespaceURI
	 * @param sName
	 * @param qName
	 * @param attrs
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(
			String namespaceURI,
			String sName,
			String qName,
			Attributes attrs)
			throws SAXException {
		
	    resetCharacters();
	    
		if(TAG_DOMAIN_MODEL.equals(qName)) {
		    domainModel = new DomainModel();
		    domainModel.setEntities(new ArrayList());
		}
		else if(TAG_ENTITY.equals(qName)) {
		    entity = new Entity();
		    entity.setValues(new ArrayList());
		    entity.setName(attrs.getValue(ATTR_ENTITY_NAME));
		    entity.setPackageName(attrs.getValue(ATTR_ENTITY_PACKAGE));
		    entity.setSuperClass(attrs.getValue(ATTR_ENTITY_SUPERCLASS));
		    entity.setType(attrs.getValue(ATTR_ENTITY_TYPE));
		    if(entity.getType() == null) {
		    	entity.setType("entity");
		    }
		    domainModel.getEntities().add(entity);
		}
		else if(TAG_VALUE.equals(qName)) {
		    value = new Value();
		    value.setName(attrs.getValue(ATTR_VALUE_NAME));
		    value.setClassName(attrs.getValue(ATTR_VALUE_CLASS));
		    entity.getValues().add(value);
		}		
	}
	
	/**
	 * endElement
	 * <p>
	 * Sets object values with character data.
	 * 
	 * @param namespaceURI
	 * @param sName
	 * @param qName
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String sName, String qName)
			throws SAXException {
	    
	    if(TAG_ENTITY_DESCRIPTION.equals(qName)) {
	        entity.setDescription(getCharacters(true));
	    }
	    else if(TAG_VALUE_DESCRIPTION.equals(qName)) {
	        value.setDescription(getCharacters(true));
	    }     
	}
    
	/**
	 * Short Description.
	 * <p>
	 * Full Description.
	 * 
	 * @return DomainModel
	 */
    public DomainModel getDomainModel() {        
        return domainModel;
    }
}
