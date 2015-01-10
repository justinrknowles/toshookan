package domainmodel;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;
import org.apache.velocity.test.provider.TestProvider;

/**
 * Implementation of DomainModelCodeGenerator that uses Apache Velocity to
 * generate the domain model code.
 * <p>
 * Velocity is a Java-based template engine that uses a simple yet powerful
 * template language to reference objects and define control flow in text based
 * templates.
 * <p>
 * This implementation allows for easy management of the generated source while
 * remaining simple in design. Because the source is defined in templates little
 * or no coding is required to modify the code that is generated.
 * 
 * Created: Dec 2, 2009
 * 
 * @version $Revision: 1.2 $
 * @author $Author: jknowles $
 */
public class VelocityDomainModelCodeGenerator implements
		DomainModelCodeGenerator, LogSystem {

	private static final String CLASSNAME = VelocityDomainModelCodeGenerator.class
			.getName();

	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

	private static final Map LOG_LEVEL_MAP = new HashMap();
	static {
		LOG_LEVEL_MAP.put(new Integer(LogSystem.DEBUG_ID), Level.FINER);
		LOG_LEVEL_MAP.put(new Integer(LogSystem.INFO_ID), Level.FINE);
		LOG_LEVEL_MAP.put(new Integer(LogSystem.WARN_ID), Level.WARNING);
		LOG_LEVEL_MAP.put(new Integer(LogSystem.ERROR_ID), Level.SEVERE);
	}

	private static final Map TEST_DATA = new HashMap();
	static {
		TEST_DATA.put("int", "1");
		TEST_DATA.put("Integer", "1");
		TEST_DATA.put("long", "1l");
		TEST_DATA.put("Long", "1l");
		TEST_DATA.put("float", "1f");
		TEST_DATA.put("Float", "1f");
		TEST_DATA.put("double", "1d");
		TEST_DATA.put("Double", "1d");
		TEST_DATA.put("boolean", "true");
		TEST_DATA.put("Boolean", "true");
		TEST_DATA.put("String", "\"\"");
	}

	/*
	 * The key in the context map that identifies the name of the template that
	 * should be used to generate the code file.
	 */
	static final String CONTEXT_TEMPLATE_NAME = "devtools.codegen.VelocityCodeEngine.templateName";

	/*
	 * The key in the context map that identifies the name of the generated code
	 * file relative to the configured source root.
	 */
	static final String CONTEXT_CODE_FILE_NAME = "devtools.codegen.VelocityCodeEngine.codeFileName";
	
	/*
	 * The key in the context map that identifies the root of the generated code.
	 */
	static final String CONTEXT_CODE_DIR_NAME = "devtools.codegen.VelocityCodeEngine.codeDirName";

	private DomainModel domainModel;
	private String templateRoot;

	/**
	 * Constructor for class VelocityDomainModelCodeGenerator.
	 * 
	 * @param domainModel
	 * @param templateRoot
	 */
	public VelocityDomainModelCodeGenerator(DomainModel domainModel,
			String templateRoot) {

		this.domainModel = domainModel;
		this.templateRoot = templateRoot;
	}

	/**
	 * Short Description.
	 * <p>
	 * Full Description.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		DomainModel model;
		DomainModelHandler handler;
		DomainModelCodeGenerator generator;
		String domainModelXmlFile;
		String templateRoot;

		domainModelXmlFile = "resources/domainModel.xml";
		templateRoot = "resources/";

		try {
			handler = new DomainModelHandler();
			handler.parse(new File(domainModelXmlFile));
			model = handler.getDomainModel();

			generator = new VelocityDomainModelCodeGenerator(model,
					templateRoot);
			generator.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs the code generation logic.
	 * <p>
	 * full description
	 * 
	 * @throws Exception
	 *             when the code generation fails
	 */
	public void execute() throws Exception {

		Iterator contextIter;
		VelocityContext context;
		Template template;
		File file;
		Writer writer;
		Collection contexts;
		Iterator entitiesIter;
		Entity entity;

		/*
		 * Convert the model into context maps, one map for each file to be
		 * generated.
		 */
		contexts = new ArrayList();
		entitiesIter = domainModel.getEntities().iterator();
		while (entitiesIter.hasNext()) {
			entity = (Entity) entitiesIter.next();
			contexts.add(createEntityContext(entity));
			contexts.add(createEntityTestContext(entity));
		}

		/*
		 * Velocity Constants are not accessed directly because they live on the
		 * Runtime which is not part of the public API.
		 */
		Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM, this);
		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateRoot);
		Velocity.init();

		/*
		 * Use Velocity to generate the files.
		 */
		contextIter = contexts.iterator();
		while (contextIter.hasNext()) {
			context = new VelocityContext((Map) contextIter.next());
			template = Velocity.getTemplate((String) context
					.get(CONTEXT_TEMPLATE_NAME));
			file = new File((String) context.get(CONTEXT_CODE_DIR_NAME)
					+ (String) context.get(CONTEXT_CODE_FILE_NAME));

			logVelocityMessage(
					1,
					"VelocityCodeEngine: creating file "
							+ file.getAbsolutePath());

			file.getParentFile().mkdirs();
			writer = new FileWriter(file);
			template.merge(context, writer);
			writer.close();
		}
	}

	/**
	 * short summary of the method
	 * <p>
	 * full description
	 * <p>
	 * Default context entries:
	 * <ul>
	 * <li>classCreateDate</li>
	 * <li>classDescription</li>
	 * <li>classImports</li>
	 * <li>className</li>
	 * <li>classPackage</li>
	 * <li>entityClassName</li>
	 * <li>entityDescription</li>
	 * <li>entityInstanceName</li>
	 * <li>entityPackage</li>
	 * <li>entityValues</li>
	 * <li>value.className</li>
	 * <li>value.testValue</li>
	 * <li>value.description</li>
	 * <li>value.getter</li>
	 * <li>value.instanceName</li>
	 * <li>value.name</li>
	 * <li>value.setter</li>
	 * </ul>
	 * 
	 * @param entity
	 * @return entity context
	 */
	private static Map createEntityContext(Entity entity) {

		Map context;
		Value value;
		Map contextValue;
		Iterator valueIter;

		context = new HashMap();
		
		context.put(VelocityDomainModelCodeGenerator.CONTEXT_TEMPLATE_NAME,
				entity.getType() + ".vm");		
		context.put(VelocityDomainModelCodeGenerator.CONTEXT_CODE_DIR_NAME,
				"gen/src/");
		context.put(
				VelocityDomainModelCodeGenerator.CONTEXT_CODE_FILE_NAME,
				getFileNameFromClassName(entity.getPackageName() + "."
						+ entity.getName()));
		context.put("className", entity.getName());
		context.put("classPackage", entity.getPackageName());
		context.put("classSuperClass", getClassName(entity.getSuperClass()));

		context.put("classCreateDate", new Date());
		context.put("classDescription",
				splitDescription(77, entity.getDescription()));
		context.put("classImports", new HashSet());
		context.put("className", getClassName(entity.getName()));
		context.put("classPackage", entity.getPackageName());

		context.put("entityClassName", getClassName(entity.getName()));
		context.put("entityDisplayName", getDisplayName(entity.getName()));
		context.put("entityDescription",
				splitDescription(77, entity.getDescription()));
		context.put("entityInstanceName",
				uncapitalize(getClassName(entity.getName())));
		context.put("entityPackage", entity.getPackageName());
		context.put("entityValues", new ArrayList());

		valueIter = entity.getValues().iterator();
		while (valueIter.hasNext()) {
			value = (Value) valueIter.next();

			contextValue = new HashMap();
			((List) context.get("entityValues")).add(contextValue);

			contextValue.put("className", getClassName(value.getClassName()));
			contextValue.put("testValue", getTestValue(value.getClassName()));
			contextValue.put("description",
					splitDescription(72, value.getDescription()));
			if(value.getClassName() != null) {
				contextValue.put("getter",
						((value.getClassName().equals("boolean") || value.getClassName().equals("Boolean")) ? "is" : "get")
								+ capitalize(value.getName()));
			}
			contextValue.put("setter", "set" + capitalize(value.getName()));
			contextValue.put("instanceName", value.getName());
			contextValue.put("name", capitalize(value.getName()));
			((Set) context.get("classImports")).addAll(getImport(value
					.getClassName()));
		}
		((Set) context.get("classImports")).addAll(getImport(entity
				.getSuperClass()));
		((Set) context.get("classImports")).remove(null);

		return context;
	}

	/**
	 * Creates default context entries but sets template and sync for an entity
	 * test case instead of an entity.
	 * 
	 * @param entity
	 * @return
	 */
	private static Map createEntityTestContext(Entity entity) {

		Map context;

		context = createEntityContext(entity);

		context.put(VelocityDomainModelCodeGenerator.CONTEXT_TEMPLATE_NAME,
				entity.getType() + "Test.vm");
		context.put(VelocityDomainModelCodeGenerator.CONTEXT_CODE_DIR_NAME,
				"gen/test/");
		context.put(
				VelocityDomainModelCodeGenerator.CONTEXT_CODE_FILE_NAME,
				getFileNameFromClassName(entity.getPackageName() + "."
						+ entity.getName() + "Test"));

		return context;
	}

	/**
	 * Filters the supplied type into a class import.
	 * <p>
	 * If no import is needed for the class i.e. no package is specified or the
	 * specified package is java.lang null is returned.
	 * 
	 * @param fullClassName
	 *            an objects fully qualified class name.
	 * @return the import statement portion of the class name or null.
	 */
	private static Collection getImport(String fullClassName) {		
		
		Collection result;
		boolean generic;
		int openAngleIndex;
		String unwrappedClassName;
		String[] multiValueGeneric;
		
		result = new ArrayList();
		if (StringUtil.isSet(fullClassName)) {
		
			openAngleIndex = fullClassName.indexOf('<');
			
			if(openAngleIndex > -1) {				
				//get front
				result.add(fullClassName.substring(0, openAngleIndex));
				
				//unwrap
				unwrappedClassName = fullClassName.substring(openAngleIndex+1, fullClassName.lastIndexOf('>'));
				
				// split multivalue generic e.g. Map<X,Y>				
				multiValueGeneric = unwrappedClassName.split(",");
				for (String className : multiValueGeneric) {
					result.addAll(getImport(className.trim()));
				}
			}
			// if fully qualified 
			else if (fullClassName.contains(".")) {
				result.add(fullClassName);
			}
		}
		
		return result;
	}

	/**
	 * Returns a literal value that is assignable to a class matching the
	 * supplied class name.
	 * 
	 * @param className
	 * @return
	 */
	private static String getTestValue(String className) {
		String testValue = (String) TEST_DATA.get(className);
		if (testValue == null) {
			testValue = "null";
		}
		return testValue;
	}

	/**
	 * Converts a full class name into just the class name.
	 * 
	 * @param fullClassName
	 *            an objects fully qualified class name.
	 * @return The class name of the object with the package portion removed.
	 */
	private static String getClassName(String fullClassName) {
		
		if(!StringUtil.isSet(fullClassName)) {
			return null;
		}
		
		fullClassName += " ";
		char[] classNameArray = fullClassName.toCharArray();
		StringBuilder buffer = new StringBuilder();
		List<String> tokens = new ArrayList<String>();		
		String className;
		int dotIndex;
		for(int i = 0; i < fullClassName.length(); ++i) {
			if("<>, ".indexOf(classNameArray[i]) > -1) {
				if(buffer.length() > 0) {
					className = buffer.toString();
					dotIndex = className.lastIndexOf('.');
					if(dotIndex > -1) {
						className = className.substring(dotIndex+1);
					}
					tokens.add(className.trim());
				}
				buffer = new StringBuilder();
				tokens.add(classNameArray[i] + "");
			}
			else {
				buffer.append(classNameArray[i]);
			}			
		}

		StringBuilder result = new StringBuilder();
		for (String string : tokens) {
			result.append(string);
		}
		
		return result.toString().trim();
	}

	/**
	 * Breaks the passed description into standard width lines.
	 * <p>
	 * A break can be forced using a newline '\n' character.
	 * 
	 * @param charWidth
	 *            the desired line width.
	 * @param description
	 *            the text to split up.
	 * @return The description text split into lines stored in a String[].
	 */
	private static List splitDescription(int charWidth, String description) {

		List lines = new ArrayList();
		char[] chars = description.toCharArray();
		char c;

		StringBuffer wordBuffer = new StringBuffer();
		StringBuffer lineBuffer = new StringBuffer();

		for (int i = 0; i < chars.length; ++i) {
			c = chars[i];

			if (!Character.isWhitespace(c)) {
				wordBuffer.append(c);
			} else if (c == '\n') {
				// forced new line so flush all buffers and create a new line.
				lineBuffer.append(wordBuffer);
				lines.add(lineBuffer.toString());
				lines.add("<p>");
				lineBuffer = new StringBuffer();
				wordBuffer = new StringBuffer();
			} else {
				if (lineBuffer.length() + wordBuffer.length() < charWidth) {
					// new word so flush the word buffer
					lineBuffer.append(wordBuffer);
					lineBuffer.append(' ');
					wordBuffer = new StringBuffer();
				} else {
					// auto new line so flush all buffers and create a new line.
					lines.add(lineBuffer.toString());
					lineBuffer = wordBuffer;
					lineBuffer.append(' ');
					wordBuffer = new StringBuffer();
				}
			}
		}
		// flush all remaining characters
		if (lineBuffer.length() + wordBuffer.length() < charWidth) {
			lineBuffer.append(wordBuffer);
			lines.add(lineBuffer.toString());
		} else {
			lines.add(lineBuffer.toString());
			lines.add(wordBuffer.toString());
		}
		return lines;
	}

	/**
	 * Uncapitalizes the first letter of the passed string.
	 * 
	 * @param string
	 *            The string to uncapitalize.
	 * @return the uncapitalized String.
	 */
	private static String uncapitalize(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

	/**
	 * Capitalizes the first letter of the passed string.
	 * 
	 * @param string
	 *            The string to Capitalize.
	 * @return the capitalized String.
	 */
	private static String capitalize(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	/**
	 * Converts a fullClassName into a file name.
	 * <p>
	 * full description
	 * 
	 * @param fullClassName
	 * @return FileName
	 */
	private static String getFileNameFromClassName(String fullClassName) {
		return fullClassName.replace('.', '/') + ".java";
	}

	/**
	 * short summary of the method
	 * <p>
	 * full description
	 * 
	 * @param name
	 * @return String
	 */
	private static String getDisplayName(String name) {

		String caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		StringBuffer sb = new StringBuffer();

		int start = 0;
		int end = StringUtil.indexOfAny(name, caps);

		while (end != -1) {
			sb.append(capitalize(name.substring(start, end)) + " ");
			start = end;
			end = StringUtil.indexOfAny(name, caps, start + 1);
		}
		sb.append(capitalize(name.substring(start)));

		return sb.toString();
	}

	/**
	 * Provides the Velocity Logger with initilization data as defined by the
	 * LogSystem interface.
	 * 
	 * @param runtimeServices
	 * @throws Exception
	 * @see org.apache.velocity.runtime.log.LogSystem#init(org.apache.velocity.runtime.RuntimeServices)
	 */
	public void init(RuntimeServices runtimeServices) throws Exception {
		return;
	}

	/**
	 * Logs a message at the specified level.
	 * <p>
	 * Used by Velocity for logging as defined by the LogSystem interface.
	 * 
	 * @param level
	 * @param message
	 * @see org.apache.velocity.runtime.log.LogSystem#logVelocityMessage(int,
	 *      java.lang.String)
	 */
	public void logVelocityMessage(int level, String message) {
		LOGGER.log((Level) LOG_LEVEL_MAP.get(new Integer(level)), message);
	}
}