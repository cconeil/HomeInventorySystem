package inventory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import static java.lang.System.out;

/**
 * This class is used to create a GUI for adding or modifying an item to the 
 * data in our MainFrame. 
 * 
 * @author Chris O'Neil
 * @date 11/11/12
 */

public class AddModifyFrame extends JDialog
{
    

    public JComboBox <String> show_items_combo;
    public JComboBox <String> month_combo;
    public JComboBox <String> day_combo;
    public JComboBox <String> year_combo;
    private DefaultComboBoxModel<String> day31_model;
    private DefaultComboBoxModel<String> day30_model;
    private DefaultComboBoxModel<String> day29_model;
    
    private addModifyHandler action;

    private Item item;
    
    private File file;
    
    public JPanel top_pan;
    private JPanel top_pan_image;
    private JPanel middle_pan;
    private JPanel top_pan_input;
    private JPanel bottom_pan;
    
    public JButton select_image_button;
    public JButton confirm_button;
    public JButton cancel_button;
    
    public JTextField description_field;
    public JTextField value_field;
    
    private final JLabel description_label;
    private final JLabel value_label;
    private static JLabel image_selected_label;
    private JLabel image_selected;
    
    private JFileChooser file_chooser;
    private FileNameExtensionFilter filter_img;
    
    private Item made_item;
    
    private String[] days_31;
    private String[] days_30;
    private String[] days_29;
    private String[] years;
    
    private final String[] item_types = {"Bedroom", "Dining Room", 
            "Garage", "Kitchen", "Living Room", "Outdoors"};
    private final String[] months = {"January", "February", "March", "April", 
            "May", "June", "July", "August", "November", "December"};
    // TOOL TIPS
    private final String description_tip = "Enter the name of your item.";
    private final String value_tip = "Enter the value of you item with zero or"
            + " two decimal places.";
    private final String type_tip = "Choose where you keep this item.";
    private final String date_tip = "Enter the date you purchased this item.";
    private final String image_tip = "Choose an image of the item.  Make sure "
            + "you are choosing a .jpeg or .png";

    
  
    /**
     * This is the constructor for our AddModifyFrame class. It takes in an
     * owner (which it stems from), a title (which it wil display), and an item
     * which could exist (if modyifying) or not exist (if adding). 
     * @param owner_in
     * @param title_in
     * @param made_item_in
     */
    public AddModifyFrame(JFrame owner_in, String title_in, Item made_item_in)
    {
        // add attribute from input
        super(owner_in, title_in, true);
        made_item = made_item_in; // null if we are adding not modifying.
        file = null; // initialize the file (image) to nothing

        setLayout(new FlowLayout());
        
        top_pan = new JPanel(new GridLayout(2,1));
        top_pan_input = new JPanel(new FlowLayout());
        top_pan_image = new JPanel(new FlowLayout());
        
        middle_pan = new JPanel(new FlowLayout());
        bottom_pan = new JPanel(new FlowLayout());
        
        setLayout(new BorderLayout());
        
        // create our addModifyHandler
        action = new addModifyHandler();
        
        // create buttons
        confirm_button = new JButton("Confirm");
        select_image_button = new JButton("Select Image");
        cancel_button = new JButton("Cancel");
        confirm_button.addActionListener(action);
        select_image_button.addActionListener(action);
        select_image_button.setToolTipText(image_tip);
        cancel_button.addActionListener(action);
        
        // create text fields
        if (made_item != null)
        {
            description_field = new JTextField(
                    made_item.getItemDescription(), 20);
            value_field = new JTextField(made_item.getItemValue(), 20);
        } else {
            description_field = new JTextField(20);
            value_field = new JTextField(20);
        }
        description_field.setToolTipText(description_tip);
        value_field.setToolTipText(value_tip);
        
        // create string descriptions
        value_label = new JLabel("Value");
        description_label = new JLabel("Description");
        image_selected_label = new JLabel("No Image Selected");
        
        // create combo_boxes and add items for the top panel
        show_items_combo = new JComboBox<String>(item_types); // items
        show_items_combo.setToolTipText(type_tip);
        show_items_combo.addActionListener(action);

        // years
        int i = 0;
        years = new String[25];
        for (int y = 2013; y >= 1990; y--)
        {
            years[i] = Integer.toString(y);
            i++;
        }
        years[i] = "<1990"; // add in less than 1990
        
        year_combo = new JComboBox<String>(years);
        year_combo.addActionListener(action);
        year_combo.setToolTipText(date_tip);
        
        // months
        month_combo = new JComboBox<String>(months); // months
        month_combo.addActionListener(action);
        month_combo.setToolTipText(date_tip);
        
        // days
        days_31 = new String[31];
        days_30 = new String[30];
        days_29 = new String[29];
        
        for (int d = 0; d < days_31.length; d++) // 31 day array
        {
            days_31[d] = Integer.toString(d+1);
        }
        
        for (int d = 0; d < days_30.length; d++ )
        {
            days_30[d] = Integer.toString(d+1);
        }
        
        for (int d = 0; d < days_29.length; d++ )
        {
            days_29[d] = Integer.toString(d+1);
        }
        
        day31_model = new DefaultComboBoxModel<String>(days_31);
        day30_model = new DefaultComboBoxModel<String>(days_30);
        day29_model = new DefaultComboBoxModel<String>(days_29);
        
        day_combo = new JComboBox<String>();
        setDays(0); // initialize the days at january.
        day_combo.addActionListener(action);
        day_combo.setToolTipText(date_tip);
        
        if (made_item != null)
        {
            updateComboBoxStarts();
        }
        
        // add elements to the pans
        top_pan_input.add(show_items_combo);
        top_pan_input.add(description_label);
        top_pan_input.add(description_field);
        top_pan_input.add(value_label);
        top_pan_input.add(value_field);
        top_pan_input.add(month_combo);
        top_pan_input.add(day_combo);
        top_pan_input.add(year_combo);
        
        top_pan_image.add(select_image_button);
        top_pan.add(top_pan_input);
        top_pan.add(top_pan_image);
        

        if (made_item != null && !made_item.getItemImage().equals("null"))
        {
            ImageIcon icon = new ImageIcon(made_item.getItemImage());
            image_selected = new JLabel(icon);
            middle_pan.add(image_selected);
        } else {
            middle_pan.add(image_selected_label);
        }
        bottom_pan.add(confirm_button);
        bottom_pan.add(cancel_button);
        
        // add the pans
        add(top_pan, BorderLayout.NORTH);
        add(middle_pan);
        add(bottom_pan, BorderLayout.SOUTH);
    }
    
    public void updateComboBoxStarts()
    {
        int type_index = getTypeIndex();
        int day_index = made_item.getItemDayInt() - 1;
        int month_index = made_item.getItemMonthInt() - 1;
        int year_index = getYearsIndex();
        
        show_items_combo.setSelectedIndex(type_index);
        day_combo.setSelectedIndex(day_index);
        month_combo.setSelectedIndex(month_index);
        year_combo.setSelectedIndex(year_index);
        
    }
    
    /**
     * This method returns the index of the item type chosen.  It will return
     * -1 if no item is chosen, but that will never happpen.
     * @return
     */
    public int getTypeIndex()
    {
        for (int i = 0; i < item_types.length; i++)
        {
            if (item_types[i].equals(made_item.getItemType()))
            {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * This method returns the index of the year chosen.  It will return -1 if
     * no item is chose, but that will never happen.
     * @return
     */
    public int getYearsIndex()
    {
        for (int i = 0; i < item_types.length; i++)
        {
            if (years[i].equals(made_item.getItemYear()))
            {
                return i;
            }
        }
        return -1;
    }
    
    
    /**
     * This method set's the days model that we will be using for the interface.
     * each days model corresponds to a different number of days for a different
     * number of months.  This helps to eliminate bad input from the user.
     * @param month
     */
    public void setDays(int month)
    {
        if (month == 3 || month == 5 || month == 9 || month == 10)
        {
            day_combo.setModel(day30_model);
            
        } else if (month == 1)
        {
            day_combo.setModel(day29_model);
        } else {
            day_combo.setModel(day31_model);
        }
        day_combo.revalidate();
        day_combo.repaint();
    }
    
    /**
     * This function is a way to access the item that that is given in the
     * constructor of this class.
     * @return
     */
    public Item getItem()
    {
        return item;
    }

    /**
     * This is a sub class that handles all requests and Listens to the user at
     * all times.  It has several methods to help it react to what the user is
     * telling it to do, and give the user the best experience possible.
     * @author christopher
     *
     */ 
public class addModifyHandler implements ActionListener
{
    String data_day;
    String data_month;
    String data_year;
    String data_price;
    String data_description;
    String data_type;
    String data_image;
    
    JMenuItem select_image;
    int return_val;
    
    
    addModifyHandler()
    {
        // empty constructer.
    }
    
    /**
     * This function uses regular expressions to check if the user's value
     * input is valid.
     * @return boolean
     */
    public boolean checkValidAddModifyInput()
    {
        return data_price.matches("\\d+\\.") ||
                data_price.matches("\\d+\\.\\d\\d") ||
                data_price.matches("([0-9]+)");
    }
    
    /**
     * This method constructs and error box with the given parameters.
     * @param title
     * @param message
     */
    public void invalidError(String title, String message)
    {
        String alert_message = message;
        JOptionPane.showMessageDialog(null, alert_message, 
                title, JOptionPane.ERROR_MESSAGE);
    }
    

    /**
     * This method changes the value output to match the format that is needed
     * in the project specs.
     */
    public void changeValueOutput()
    {
        if (data_price.matches("\\d+\\."))
        {
            data_price = data_price + "00";
        } else if (data_price.matches("([0-9]+)"))
        {
            data_price = data_price + ".00";
        } else {
            // do nothing!
        }
    }
    
    
    /**
     * This is the part of the class that actually does all of the listening.
     * After listening, it will implement changes on the user interface and
     * other aspects of the program. This is where the actual data is added or
     * modified.
     */
    public void actionPerformed(ActionEvent e)
    {
        
        if (e.getSource() == month_combo)
        {
            setDays(month_combo.getSelectedIndex());
        } else if (e.getSource() == confirm_button)
        {
            if (file != null)
            {
                data_image = file.getAbsolutePath();
            } else {
                data_image = "null"; // set the string to empty
            }
            

                data_day = Integer.toString(day_combo.getSelectedIndex() + 1); // index is 1 less
                data_month = Integer.toString(month_combo.getSelectedIndex()+1);
                data_year = years[year_combo.getSelectedIndex()];
                data_price = value_field.getText();
                data_description = description_field.getText();
                data_type = item_types[show_items_combo.getSelectedIndex()];
                
            
            if (!checkValidAddModifyInput())
            {
                item = null;
                String title = "Invalid Value";
                String message = "The value " + data_price + " could not be" +
                        " input.  Please enter a value with no decimal places" +
                        " or two decimal places.";
               
                invalidError(title, message);
            } else {      
                
                if (data_description.equals(""))
                {
                    out.println("It's empty!");
                    String message = "Please enter a description of your item.";
                    String title = "Invalid Description";
                    invalidError(title, message);
                    
                } else {
                
                    changeValueOutput();
                    item = new Item(data_image,
                            data_day,
                            data_month,
                            data_year,
                            data_price,
                            data_description,
                            data_type);
                    dispose();
                }
            }

        } else if (e.getSource() == select_image_button)
        {
            // we need to get our loading menu.
            JMenuItem select_image = new JMenuItem("Select Image");
            file_chooser = new JFileChooser();
            filter_img = new FileNameExtensionFilter(
                    "Images", "jpeg", "png", "bmp", "gif");
            file_chooser.setFileFilter(filter_img); // set our filter
            return_val = file_chooser.showOpenDialog(select_image);
            if(return_val == JFileChooser.APPROVE_OPTION)
            {

                file = file_chooser.getSelectedFile();
                ImageIcon icon = new ImageIcon(file.getPath());
                middle_pan.remove(image_selected_label);      
                image_selected = new JLabel(icon);
                middle_pan.add(image_selected);
                add(middle_pan);
                repaint();
                revalidate();
                pack();
            }
        } else if (e.getSource() == cancel_button)
        {
            dispose();
        }

    } // end method
 
} // end helper class

} // end main class