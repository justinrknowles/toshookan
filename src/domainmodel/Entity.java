package domainmodel;

import java.util.Iterator;
import java.util.List;

/**
 * Short Description.
 * <p>
 * Full Description.
 * 
 * Created: Dec 2, 2009
 * 
 * @version $Revision: 1.1 $
 * @author $Author: jknowles $
 */
public class Entity {
    
    private String name;
    private String packageName;
    private String description;   
    private List values;
    private String superClass;
    private String type;
    
	/**
	 * short summary of the method
	 * <p>
	 * full description
	 * 
	 * @param propertyName
	 * @return Property
	 */
    public Value getValue(String valueName) {
        
        Iterator valueIter = null;
        Value value = null;
        
        valueIter = values.iterator();
        while(valueIter.hasNext()) {
            value = (Value)valueIter.next();
            if(value.getName().equals(valueName)) {
                return value;
            }
        }
        return null;
    }

	/**
	 * Returns a string that "textually represents" this object.
	 * <p>
	 * Specifically a string is returned that contains all of the instance
	 * variable names and values.  The value is derived using the instance
	 * variable's toString() method.
	 * <p>
	 * An example of the exact format: <code>[[i=0][animal=cat]]</code>
	 * <p>
	 * @return A String representation of this instance.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return
			"[" +
			"[values=" + values + "]" +
			"[description=" + description + "]" +
			"[packageName=" + packageName + "]" +
			"[name=" + name + "]" +
			"[superClass=" +  superClass + "]" +
			"]";
	} 
	
    /**
     * Getter method for description.
     * <p>
     * Full Description.
     * 
     * @return the value of description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Setter method for description.
     * <p>
     * Full Description.
     * 
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter method for name.
     * <p>
     * Full Description.
     * 
     * @return the value of name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Setter method for name.
     * <p>
     * Full Description.
     * 
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Getter method for packageName.
     * <p>
     * Full Description.
     * 
     * @return the value of packageName.
     */
    public String getPackageName() {
        return packageName;
    }
    
    /**
     * Setter method for packageName.
     * <p>
     * Full Description.
     * 
     * @param packageName The packageName to set.
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    /**
     * Getter method for values.
     * <p>
     * Full Description.
     * 
     * @return the value of values.
     */
    public List getValues() {
        return values;
    }
    
    /**
     * Setter method for values.
     * <p>
     * Full Description.
     * 
     * @param values The values to set.
     */
    public void setValues(List values) {
        this.values = values;
    }

	/**
	 * Getter method for superClass.
	 * <p>
	 * Full Description.
	 * 
	 * @return the value of superClass.
	 */
	public String getSuperClass() {
		return superClass;
	}

	/**
	 * Setter method for superClass.
	 * <p>
	 * Full Description.
	 * 
	 * @param superClass The superClass to set.
	 */
	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}