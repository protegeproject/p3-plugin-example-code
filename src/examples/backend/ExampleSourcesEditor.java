package examples.backend;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;

/**
 *  Editor for fields specific to this backend.
 */
public class ExampleSourcesEditor extends KnowledgeBaseSourcesEditor {
	private static final long serialVersionUID = 6644561615886047674L;
	private FileField clsesField;
    private FileField itsInstancesField;

    /**
     * create widgets to acquire everything necessary for a save to be performed successfully.  In
     * this example what is needed is two relative file names, one to store the classes and one
     * to store the instances.
     */
    public ExampleSourcesEditor(String projectPath, PropertyList sources) {
        super(projectPath, sources);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.add(createClsesField());
        panel.add(createInstancesField());
        add(panel);
    }

    /**
     * construct a file name from the base project name and an extension
     */
    private String constructName(String projectPath, String extension) {
        String name = null;
        if (projectPath != null) {
            int index = projectPath.indexOf(".");
            if (index != -1) {
                name = projectPath.substring(0, index) + extension;
            }
        }
        return name;
    }

    private JComponent createClsesField() {
        String name = ExampleBackend.getClsesFileName(getSources());
        if (name == null) {
            name = constructName(getProjectPath(), ".classes");
        }
        clsesField = new FileField("Classes file name", name, ".classes", "Ontology");
        return clsesField;
    }

    private JComponent createInstancesField() {
        String name = ExampleBackend.getInstancesFileName(getSources());
        if (name == null) {
            name = constructName(getProjectPath(), ".instances");
        }
        itsInstancesField = new FileField("Instances file name", name, ".instances", "Instances");
        return itsInstancesField;
    }

    private boolean hasValidValue(FileField field) {
        boolean hasValidValue;
        String value = field.getPath();
        if (value == null || value.length() == 0) {
            hasValidValue = false;
        } else {
            hasValidValue = true;
        }
        return hasValidValue;
    }

    /**
     * the system calls this method when the user changes the project name.  We
     * should update any files whose names may depend on the project name.
     */
    public void onProjectPathChange(String oldPath, String newPath) {
        if (newPath != null) {
            updatePath(clsesField, ".classes");
            updatePath(itsInstancesField, ".instances");
        }
    }

    /**
     * the system calls this method when the user presses the OK button and the contents
     * have been declared to be valid.  This method should stuff any
     * configuration information into the sources property list.
     */
    public void saveContents() {
        String clsesFileName = clsesField.getPath();
        String instancesFileName = itsInstancesField.getPath();
        ExampleBackend.setSourceFiles(getSources(), clsesFileName, instancesFileName);
    }

    private void updatePath(FileField field, String ext) {
        File projectFile = new File(getProjectPath());
        String projectName = projectFile.getName();
        int index = projectName.indexOf(".");
        if (index != -1) {
            projectName = projectName.substring(0, index);
        }
        String newPath = projectName + ext;
        field.setPath(newPath);
    }

    /**
     * called by the system when the user presses the OK button.  return true if the contents
     * of the fields are valid.  Otherwise pop up a dialog with a description of the problem and return false.
     */
    public boolean validateContents() {
        boolean isComplete = hasValidValue(clsesField) && hasValidValue(itsInstancesField);
        if (!isComplete) {
            // TODO: pop up a dialog to warn the user.
            isComplete = false;
        }
        return isComplete;
    }
}
