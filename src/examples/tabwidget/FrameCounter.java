package examples.tabwidget;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.resource.*;

// an example tab
public class FrameCounter extends AbstractTabWidget {
    private static final int MAX_FRAMES = 200;
    private JTextField field;

    // startup code
    public void initialize() {
        // initialize the tab label
        setLabel("Frame Counter");
        setIcon(Icons.getInstanceIcon());

        // create the button
        JButton button = new JButton("Update Frame Counter");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // update text field
                int count = getKnowledgeBase().getFrameCount();
                field.setText(String.valueOf(count));
            }
        });

        // create the output text field
        field = new JTextField(10);
        field.setEnabled(false);
        field.setHorizontalAlignment(SwingConstants.RIGHT);

        // add the components to the tab widget
        setLayout(new FlowLayout());
        add(button);
        add(field);
    }
    
    public static boolean isSuitable(Project project, Collection errors) {
    	boolean isSuitable;
    	if (project.getKnowledgeBase().getFrameCount() > MAX_FRAMES) {
    		isSuitable = false;
    		String text = "Project too big, max=" + MAX_FRAMES + " frames";
    		errors.add(text);
    	} else {
    	    isSuitable = true;
    	}
    	return isSuitable;
    }
    
    // this method is useful for debugging
    public static void main(String[] args) {
        edu.stanford.smi.protege.Application.main(args);
    }
}
