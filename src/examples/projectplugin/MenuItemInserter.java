package examples.projectplugin;

import javax.swing.*;

import edu.stanford.smi.protege.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.plugin.*;
import edu.stanford.smi.protege.ui.*;

public class MenuItemInserter extends AbstractProjectPlugin {

    public void afterCreate(Project p) {
        // do nothing
    }

    public void afterLoad(Project p) {
        // do nothing
    }

    public void afterShow(ProjectView view, ProjectToolBar toolBar, ProjectMenuBar menuBar) {
        JMenu editMenu = menuBar.getMenu(1);
        if (editMenu.getText().equals("Edit")) {
            editMenu.add(new JMenuItem("My New Edit Menu Item"));
        }
    }

    public void afterSave(Project p) {
        // do nothing
    }
    
    public void beforeSave(Project p) {
        // do nothing
    }
    
    public void beforeHide(ProjectView view, ProjectToolBar toolBar, ProjectMenuBar menuBar) {
        // do nothing
    }

    public void beforeClose(Project p) {
        // do nothing
    }

    public String getName() {
        return "Menu Item Inserter";
    }

    public void dispose() {
        // do nothing
    }
    
    public static void main(String[] args) {
        Application.main(args);
    }


}
