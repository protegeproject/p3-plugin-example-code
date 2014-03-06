package examples.backend;

import java.io.*;
import java.util.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;

/**
 * Fake backend that shows the common structure and procedures.
 */

public class ExampleBackend implements KnowledgeBaseFactory {
    private static final String CLASS_FILE_NAME_PROPERTY = "example_class_file_name";
    private static final String INSTANCE_FILE_NAME_PROPERTY = "example_instance_file_name";

    /**
     * Create a new knowledge base.  This implementation just uses DefaultKnowledgeBase.  In addition all
     * kb's of this factory have a class called "Example_Root".
     */
    public KnowledgeBase createKnowledgeBase(Collection errors) {
        KnowledgeBase kb = new DefaultKnowledgeBase(this);
        kb.createCls("Example_Root", kb.getRootClses());
        return kb;
    }

    public void includeKnowledgeBase(KnowledgeBase kb, PropertyList sources, Collection errors) {
        load(kb, sources, errors, true);
    }

    public void loadKnowledgeBase(KnowledgeBase kb, PropertyList sources, Collection errors) {
        load(kb, sources, errors, false);
    }

    private void load(KnowledgeBase kb, PropertyList sources, Collection errors, boolean isIncluded) {
        loadClses(getReader(CLASS_FILE_NAME_PROPERTY, sources), kb, errors, isIncluded);
        loadInstances(getReader(INSTANCE_FILE_NAME_PROPERTY, sources), kb, errors, isIncluded);
    }

    public void saveKnowledgeBase(KnowledgeBase kb, PropertyList sources, Collection errors) {
        saveClsesAndSlots(getWriter(CLASS_FILE_NAME_PROPERTY, sources), kb, errors);
        saveSimpleInstances(getWriter(INSTANCE_FILE_NAME_PROPERTY, sources), kb, errors);
    }

    /**
     * We aren't really going to load anything from the files.  We just populate the kb with
     * a random set of classes.  In a real system these classes would be the result of parsing
     * the files.
     */
    private void loadClses(Reader reader, KnowledgeBase kb, Collection errors, boolean isIncluded) {
        Cls a = createCls("Class_A", null, kb, isIncluded);
        createCls("Class_B", a, kb, isIncluded);
        Slot s = createSlot("slot_s", kb, isIncluded);
        a.addDirectTemplateSlot(s);
    }

    /**
     * We aren't really going to load anything from the files.  We just populate the kb with
     * a random set of instances.  In a real system these instances would be the result of parsing
     * the files.
     */
    private void loadInstances(Reader reader, KnowledgeBase kb, Collection errors, boolean isIncluded) {
        Cls a = kb.getCls("Class_A");
        Slot s = kb.getSlot("slot_s");

        Instance i = createInstance("Instance_A", a, kb, isIncluded);
        i.setOwnSlotValue(s, "example slot value");
    }

    public KnowledgeBaseSourcesEditor createKnowledgeBaseSourcesEditor(String projectName, PropertyList sources) {
        return new ExampleSourcesEditor(projectName, sources);
    }

    /**
     * return true if the sources property list is complete so that a save can be successfully performed
     */
    public boolean isComplete(PropertyList sources) {
        return sources.getString(CLASS_FILE_NAME_PROPERTY) != null &&
            sources.getString(INSTANCE_FILE_NAME_PROPERTY) != null;
    }


    /**
     * returns the description of this backend for display to users
     */
    public String getDescription() {
        return "Example Backend";
    }

    /**
     * returns the path to any project file containing customized forms which are needed by this backend.  The
     * file must be in the jar and the path must be relative to this class file location in the jar.  Return
     * null if no customized forms are needed.
     */
    public String getProjectFilePath() {
        return null;
    }

    /**
     * extract a relative file name from the sources property list and turn it into a reader.  The FileUtilities
     * class handles turning the relative file name into an absolute name using the project file location.
     */
    private Reader getReader(String propertyName, PropertyList sources) {
        Reader reader = null;
        String filename = sources.getString(propertyName);
        if (filename != null) {
            reader = FileUtilities.getReader(filename);
        }
        return reader;
    }

    /**
     * extract a relative file name from the sources property list and turn it into a writer.  The FileUtilities
     * class handles turning the relative file name into an absolute name using the project file location.
     */
    private Writer getWriter(String propertyName, PropertyList sources) {
        Writer writer = null;
        String filename = sources.getString(propertyName);
        if (filename != null) {
            writer = FileUtilities.getWriter(filename);
        }
        return writer;
    }

    private void saveClsesAndSlots(Writer writer, KnowledgeBase kb, Collection errors) {
        Iterator i = kb.getClses().iterator();
        while (i.hasNext()) {
            Cls cls = (Cls) i.next();
            saveCls(cls);
        }
        Iterator j = kb.getSlots().iterator();
        while (j.hasNext()) {
            Slot slot = (Slot) j.next();
            saveSlot(slot);
        }
    }
    
    private void saveCls(Cls cls) {
        // write out the cls
    }

    private void saveSlot(Slot slot) {
        // write out the slot
    }

    private void saveSimpleInstances(Writer writer, KnowledgeBase kb, Collection errors) {
        Iterator i = kb.getInstances().iterator();
        while (i.hasNext()) {
            Instance instance = (Instance) i.next();
            if (instance instanceof SimpleInstance) {
                // save instance
            }
        }
    }
    private Cls createCls(String name, Cls parent, KnowledgeBase kb, boolean isIncluded) {
        if (parent == null) {
            parent = kb.getRootCls();
        }
        Cls cls = kb.createCls(name, CollectionUtilities.createCollection(parent));
        cls.setIncluded(isIncluded);
        return cls;
    }

    private Slot createSlot(String name, KnowledgeBase kb, boolean isIncluded) {
        Slot slot = kb.createSlot(name);
        slot.setIncluded(isIncluded);
        return slot;
    }

    private Instance createInstance(String name, Cls type, KnowledgeBase kb, boolean isIncluded) {
        Instance instance = kb.createInstance(name, type);
        instance.setIncluded(isIncluded);
        return instance;
    }

    /**
     * a helper method to extract the name of the file for storing classes from the property list
     */
    public static String getClsesFileName(PropertyList sources) {
        return sources.getString(CLASS_FILE_NAME_PROPERTY);
    }

    /**
     * a helper method to extract the name of the file for storing instances from the property list
     */
    public static String getInstancesFileName(PropertyList sources) {
        return sources.getString(INSTANCE_FILE_NAME_PROPERTY);
    }

    /**
     * helper method to stuff the class and instances file names into the sources property list
     */
    public static void setSourceFiles(PropertyList sources, String classesFileName, String instancesFileName) {
        sources.setString(CLASS_FILE_NAME_PROPERTY, classesFileName);
        sources.setString(INSTANCE_FILE_NAME_PROPERTY, instancesFileName);
    }

    /**
     * method to make this class easy to debug
     */
    public static void main(String[] args) {
        edu.stanford.smi.protege.Application.main(args);
    }

}
