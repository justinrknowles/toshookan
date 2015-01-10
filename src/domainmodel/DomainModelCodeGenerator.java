package domainmodel;

/**
 * Generates code based off a model.
 * <p>
 * Created: Jan 20, 2007
 * 
 * @author <a href="mailto:justinrknowles@gmail.com">Justin R. Knowles</a>
 */
public interface DomainModelCodeGenerator {    
    
    /**
     * Performs the code generation logic.
     * 
     * @throws Exception when the code generation fails
     */
    public void execute() throws Exception;
}
