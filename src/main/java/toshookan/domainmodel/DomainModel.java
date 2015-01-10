package toshookan.domainmodel;

import java.util.Collection;

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
public class DomainModel {
    
    private Collection entities;
    
    /**
     * Getter method for entities.
     * <p>
     * Full Description.
     * 
     * @return the value of entities.
     */
    public Collection getEntities() {
        return entities;
    }
    
    /**
     * Setter method for entities.
     * <p>
     * Full Description.
     * 
     * @param entities The entities to set.
     */
    public void setEntities(Collection entities) {
        this.entities = entities;
    }
}
