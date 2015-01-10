package toshookan.domainmodel;

/**
 * A property of a persistant object.
 * <p>
 * Created: Jan 20, 2007
 * 
 * @author <a href="mailto:justinrknowles@gmail.com">Justin R. Knowles</a>
 */
public class Value {
    
    private String name;
    private String className;
    private String description;
    
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
			"[description=" + description + "]" +
			"[className=" + className + "]" +
			"[name=" + name + "]" +
			"]";
	}	
    
    /**
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param className The className to set.
     */
    public void setClassName(String className) {
        this.className = className;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
