package examples.createprojectplugin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stanford.smi.protege.Application;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.KnowledgeBaseFactory;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.plugin.AbstractCreateProjectPlugin;
import edu.stanford.smi.protege.plugin.CreateProjectWizard;
import edu.stanford.smi.protege.storage.clips.ClipsKnowledgeBaseFactory;
import edu.stanford.smi.protege.util.FileField;
import edu.stanford.smi.protege.util.FileUtilities;
import edu.stanford.smi.protege.util.Wizard;
import edu.stanford.smi.protege.util.WizardPage;

public class FooCreator extends AbstractCreateProjectPlugin {
    private File fooFile;
    
    public FooCreator() {
        super("Foo Files");
    }

    public void setFile(File file) {
        fooFile = file;
    }

    public void dispose() {
        // do nothing
    }
    
    public static void main(String[] args) {
        Application.main(args);
    }
    
    public Project createProject() {
        Collection errors = new ArrayList();
        Project project = Project.createNewProject(null, errors);
        if (errors.isEmpty()) {
            loadFromFile(project, fooFile);
        }
        return project;
    }
    
    private void loadFromFile(Project project, File file) {
        try {
		    BufferedReader reader = FileUtilities.createBufferedReader(file);
		    loadProject(project, reader);
		    reader.close();
        } catch (IOException e) {
        	notifyUserOfError(e);
        }
    }

    private void loadProject(Project project, BufferedReader reader) throws IOException {
        KnowledgeBase kb = project.getKnowledgeBase();
        Collection rootClses = kb.getRootClses();
        String line;
        while ((line = reader.readLine()) != null) {
            String className = line.trim();
            kb.createCls(className, rootClses);
        }
    }
    
    private void notifyUserOfError(Exception e) {
    	System.out.println(e);
    }

    public boolean canCreateProject(KnowledgeBaseFactory factory, boolean useExistingSources) {
        return factory.getClass().equals(ClipsKnowledgeBaseFactory.class) && useExistingSources;
    }

    public WizardPage createCreateProjectWizardPage(CreateProjectWizard wizard, boolean useExistingSources) {
        WizardPage page = null;
        if (useExistingSources) {
            page = new FooFilesWizardPage(wizard, this);
        }
        return page;
    }
}

class FooFilesWizardPage extends WizardPage {
    private FooCreator plugin;
    private FileField fileField;
    
    FooFilesWizardPage(Wizard wizard, FooCreator plugin) {
        super("foo files", wizard);
        this.plugin = plugin;
        fileField = new FileField("Foo File", null, ".foo", "Foo Examples Files");
        JPanel panel = new JPanel(new GridLayout(1, 0));
        panel.add(fileField);
        setLayout(new BorderLayout());
        add(panel);
        setPageComplete(false);
        fileField.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                updatePageComplete();
            }
        });
    }
    
    private void updatePageComplete() {
        File file = fileField.getFilePath();
        setPageComplete(file != null && file.isFile());
    }
    
    public void onFinish() {
        plugin.setFile(fileField.getFilePath());
    }
}
