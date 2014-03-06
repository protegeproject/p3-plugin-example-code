package examples.slotwidget;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protege.widget.*;

public class StringInverter extends AbstractSlotWidget {
	private static final long serialVersionUID = -8183561387428676185L;
	private static final String TEXT_BOLD_PROPERTY = "text_is_bold";
    private static final String BACKGROUND_COLOR_PROPERTY = "background_color";
    private static final String COLOR_GREEN = "green";
    private static final String COLOR_PINK = "pink";
    private static final String DEFAULT_COLOR = COLOR_PINK;
    
    private JTextField field;
    private AbstractAction invertAction;

    // set up the widget
    public void initialize() {
        field = ComponentFactory.createTextField();
        field.getDocument().addDocumentListener(createDocumentListener());
        configureTextField();

        invertAction = new AbstractAction("Invert String", getInvertIcon()) {
			private static final long serialVersionUID = -2289385915311536270L;
			public void actionPerformed(ActionEvent event) {
                StringBuffer buf = new StringBuffer(field.getText());
                buf.reverse();
                field.setText(buf.toString());
            }
        };
        LabeledComponent c = new LabeledComponent(getLabel(), field);
        c.addHeaderButton(invertAction);
        add(c);

        // set the default size to be the same as the standard "TextFieldWidget".
        setPreferredColumns(2);
        setPreferredRows(1);
    }
    
    private void configureTextField() {
        if (isBold()) {
            Font font = field.getFont();
            font = font.deriveFont(Font.BOLD);
            field.setFont(font);
        }
        field.setBackground(getColor());
    }
        
    boolean isBold() {
        Boolean isBoldBoolean = getPropertyList().getBoolean(TEXT_BOLD_PROPERTY);
        return (isBoldBoolean == null) ? false : isBoldBoolean.booleanValue();
    }
    
    void setBold(boolean isBold) {
        getPropertyList().setBoolean(TEXT_BOLD_PROPERTY, Boolean.valueOf(isBold));
    }
    
    public Color getColor() {
        String colorText = getPropertyList().getString(BACKGROUND_COLOR_PROPERTY);
        return getColor(colorText);
    }
    
    public void setColor(Color color) {
        String text = getColorText(color);
        getPropertyList().setString(BACKGROUND_COLOR_PROPERTY, text);
    }
    
    private static Color getColor(String text) {
        Color color;
        if (text == null) {
            text = DEFAULT_COLOR;
        } 
        if (text.equals(COLOR_PINK)) {
            color = Color.PINK;
        } else if (text.equals(COLOR_GREEN)) {
            color = Color.GREEN;
        } else {
            color = getColor(DEFAULT_COLOR);
        }
        return color;
    }
    
    public Collection getAllowedColors() {
        return Arrays.asList(new Color[] {Color.PINK, Color.GREEN});
    }
    
    private static String getColorText(Color color) {
        String colorText;
        if (color == null) {
            colorText = null;
        } else if (color.equals(Color.PINK)) {
            colorText = COLOR_PINK;
        } else if (color.equals(Color.GREEN)) {
            colorText = COLOR_GREEN;
        } else {
            colorText = null;
        }
        return colorText;
    }

    // return the current value displayed by the widget
    public Collection getValues() {
        String s = field.getText();
        return CollectionUtilities.createCollection(s);
    }

    // initialize the display value
    public void setValues(Collection c) {
        String s = (String) CollectionUtilities.getFirstItem(c);
        field.setText(s);
    }

    // change whether or not the user can modify the displayed value
    public void setEditable(boolean editable) {
        field.setEnabled(editable);
        invertAction.setEnabled(editable);
    }

    /* indicate whether an instance of this class can handle the Class-Slot binding.
     * Note that "facet" is historical and is always null. This widget handles
     * cardinality-single string slots.
     */
    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        boolean isString = cls.getTemplateSlotValueType(slot) == ValueType.STRING;
        boolean isCardinalitySingle = !cls.getTemplateSlotAllowsMultipleValues(slot);
        return isString && isCardinalitySingle;
    }

    /* Access our icon in a way that works for both standalone class files and gif's in a
     * directory and with all files in a jar.  The image is in the same directory
     * as the specified class file.
     */
    private static Icon getInvertIcon() {
        return ComponentUtilities.loadImageIcon(StringInverter.class, "invert.gif");
    }

    // This listener updates the kb as characters are typed. This is not the usual
    // Protege convention.
    private DocumentListener createDocumentListener() {
        return new DocumentListener() {
            public void changedUpdate(DocumentEvent event) {
                // notify the system that the value has changed
                valueChanged();
            }
            public void removeUpdate(DocumentEvent event) {
                valueChanged();
            }
            public void insertUpdate(DocumentEvent event) {
                valueChanged();
            }
        };
    }
    
    // allows the widget to be configured
    public WidgetConfigurationPanel createWidgetConfigurationPanel() {
        WidgetConfigurationPanel panel = super.createWidgetConfigurationPanel();
        panel.addTab("Font and Color", new StringInverterConfigurationPanel(this));
        return panel;
    }

    // method to allow easy debuging
    public static void main(String[] args) {
        edu.stanford.smi.protege.Application.main(args);
    }
}

class StringInverterConfigurationPanel extends AbstractValidatableComponent {
	private static final long serialVersionUID = -8997892386994255019L;
	private StringInverter widget;
    private JCheckBox isBoldCheckBox;
    private JComboBox backgroundColorComboBox;
    
    StringInverterConfigurationPanel(StringInverter widget) {
        this.widget = widget;
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(createIsBoldComponent());
        panel.add(createBackgroundColorComponent());
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
    }
    
    private JComponent createIsBoldComponent() {
        isBoldCheckBox = ComponentFactory.createCheckBox("Bold Text");
        isBoldCheckBox.setSelected(widget.isBold());
        return isBoldCheckBox;
    }
    
    private JComponent createBackgroundColorComponent() {
        backgroundColorComboBox = ComponentFactory.createComboBox();
        // backgroundColorComboBox.setRenderer(new ColorRenderer());
        Collection colors = widget.getAllowedColors();
        backgroundColorComboBox.setModel(new DefaultComboBoxModel(colors.toArray()));
        backgroundColorComboBox.setSelectedItem(widget.getColor());
        LabeledComponent component = new LabeledComponent("Background Color", backgroundColorComboBox);
        return component;
    }

    public boolean validateContents() {
        return true;
    }
    
    public void saveContents() {
        widget.setBold(isBoldCheckBox.isSelected());
        widget.setColor((Color) backgroundColorComboBox.getSelectedItem());
    }
}

class ColorRenderer extends DefaultRenderer {
	private static final long serialVersionUID = 8038327633947785427L;
	public void load(Object o) {
        Color color = (Color) o;
        if (color.equals(Color.PINK)) {
            setMainText("Pink");
        } else if (color.equals(Color.GREEN)) {
            setMainText("Green");
        } else {
            setMainText("Unknown");
        }
    }
}