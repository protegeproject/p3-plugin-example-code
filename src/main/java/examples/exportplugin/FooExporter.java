package examples.exportplugin;

import java.io.*;
import java.util.*;
import javax.swing.*;
import edu.stanford.smi.protege.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.plugin.*;
import edu.stanford.smi.protege.util.*;

public class FooExporter implements ExportPlugin {
    private static final String EXTENSION = ".foo";

    public String getName() {
        return "FooFile";
    }

    public void handleExportRequest(Project project) {
        File file = promptForFooFile(project);
        if (file != null) {
            saveToFile(project, file);
        }
    }

    public void dispose() {
        // do nothing
    }
    
    public static void main(String[] args) {
        Application.main(args);
    }
    
    private File promptForFooFile(Project project) {
        String name = project.getName();
        String proposedName = new File(name + EXTENSION).getPath();
        JFileChooser chooser = ComponentFactory.createFileChooser(proposedName, EXTENSION);
        File file = null;
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        }
        return file;
     }
    
    private void saveToFile(Project project, File file) {
        PrintWriter writer = FileUtilities.createPrintWriter(file, false);
        saveProject(project, writer);
        writer.close();
    }
    
    // Write out each class, one per line.
    private void saveProject(Project project, PrintWriter writer) {
        Iterator i = project.getKnowledgeBase().getClses().iterator();
        while (i.hasNext()) {
            Cls cls = (Cls) i.next();
            if (!cls.isIncluded()) {
                writer.println(cls.getName());
        	}
        }
    }
}
