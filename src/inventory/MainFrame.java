package inventory;

import static java.lang.System.out;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;



/**
 * @class MainFrame
 * This class is used to control the main interface of our application.  It
 * is what forms our basic GUI and it's handler (a subclass) controls much of
 * the functionality of this app.
 *
 * There is no parameters to this.
 *
 * @author Chris O'Neil
 * @date 11/11/12
 */
public class MainFrame
{
    
    private JFrame frame;
    
    private File output_file;
    private File input_file;
    
    private Scanner read_file;
    
    private JComboBox<String> sort_by_combo;
    private JComboBox<String> show_items_combo;
    
    private JMenuBar drop_down;
    
    private JMenu file_menu;
    private JMenu edit_menu;
    
    private JMenuItem load_inventory;
    private JMenuItem save_inventory;
    private JMenuItem exit_program;
    private JMenuItem add_item;
    private JMenuItem modify_item;
    private JMenuItem delete_item;
    
    private JLabel sort_by_label;
    private JLabel show_items_label;
    private JLabel item_value_label;
    private JLabel about_item_value_label;
    
    private JPanel top_pan;
    private JPanel center_pan;
    private JPanel bottom_pan;
    
    private Item inventory_item;
    
    private String inventory_item_string;
    private String inventory_item_string_IO;
    private String load_error_type;
    
    private JList<String> inventory_list;
    
    private DefaultListModel<String> list_model;
    private DefaultListModel<String> list_model_IO;
    private DefaultListModel<String> current_model;
    private DefaultListModel<String> current_model_IO;

    
    private PrintStream inventory_out;
    
    private mainFrameHandler action_handler;
    
    private Item[] data_list;
    private final String[] item_types = {"All", "Bedroom", "Dining Room", 
            "Garage", "Kitchen", "Living Room", "Outdoors"};
    private final String[] sort_types = {"Item Name", "Location", 
            "Value", "Purchase Date"};
    private final String add_tip = "Add an item to your Inventory List.";
    private final String modify_tip = "Modify a single selected Item in the"
            + " Inventory List.";
    private final String load_tip = "Load a .txt file of your inventory";
    private final String save_tip = "Save your inventory as a .txt file";
    private final String sort_tip = "Select a characteristic in which to sort"
            + " your inventory.";
    private final String filter_tip = "Filter your inventory to only show items"
            + " of this type.";
    private final String no_items = "No Items Displayed";

    /**
     * This is the constructor.  It is what actually builds the interface.  It
     * is done using Swing, particularly FlowLayouts, GridLayouts, and
     * BorderLayouts.
     */
    
    public MainFrame()
    {
     // make new window
        frame = new JFrame("Home Inventory System");
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);

        // add mainFrameHandler
        action_handler = new mainFrameHandler();
        
        // set layout
        top_pan = new JPanel(new FlowLayout());
        center_pan = new JPanel(new BorderLayout());
        bottom_pan = new JPanel(new FlowLayout());
        frame.setLayout(new BorderLayout());
        
        // set file menu and add items
        file_menu = new JMenu("File");
        load_inventory = new JMenuItem("Load Inventory");
        load_inventory.setToolTipText(load_tip);
        save_inventory = new JMenuItem("Save Inventory");
        save_inventory.setToolTipText(save_tip);
        exit_program =  new JMenuItem("Exit Program");
        file_menu.add(load_inventory);
        file_menu.add(save_inventory);
        file_menu.add(exit_program);

        // set edit menu and add items
        edit_menu = new JMenu("Edit");
        add_item = new JMenuItem("Add Item");
        add_item.setToolTipText(add_tip);
        modify_item = new JMenuItem("Modify Item");
        modify_item.setToolTipText(modify_tip);
        delete_item = new JMenuItem("Delete Item");
        edit_menu.add(add_item);
        edit_menu.add(modify_item);
        edit_menu.add(delete_item);
        
        // add to drop down and put in our layout
        drop_down = new JMenuBar();
        drop_down.add(file_menu);
        drop_down.add(edit_menu);
        frame.setJMenuBar(drop_down);
        
        // add the action listener
        add_item.addActionListener(action_handler);
        modify_item.addActionListener(action_handler);
        delete_item.addActionListener(action_handler);
        load_inventory.addActionListener(action_handler);
        save_inventory.addActionListener(action_handler);
        exit_program.addActionListener(action_handler);
        
        // create labels
        sort_by_label = new JLabel("Sort Items By: ");
        show_items_label = new JLabel("Show Only Items In: ");
        
        // create combo_boxes and add items for the top panel
        sort_by_combo = new JComboBox<String>(sort_types);
        sort_by_combo.setToolTipText(sort_tip);
        sort_by_combo.setSelectedIndex(-1);
        show_items_combo = new JComboBox<String>(item_types);
        show_items_combo.setToolTipText(filter_tip);
        
        // create action handler for combo boxes
        sort_by_combo.addActionListener(action_handler);
        show_items_combo.addActionListener(action_handler);

        // LIST MODELS
        list_model = new DefaultListModel<String>();
        list_model_IO = new DefaultListModel<String>();
        current_model = new DefaultListModel<String>();
        current_model_IO = new DefaultListModel<String>();
        inventory_list = new JList<String>(list_model);
        inventory_list.setLayoutOrientation(0); // 0 corresponds to VERTICAL
        
        // add to top pan
        top_pan.add(sort_by_label);
        top_pan.add(sort_by_combo);
        top_pan.add(show_items_label);
        top_pan.add(show_items_combo);

        about_item_value_label = new JLabel("Total Value of Displayed Items: ",
                                                          SwingConstants.RIGHT);
        item_value_label = new JLabel(no_items, 
                SwingConstants.LEFT);
        // add to middle pan
        center_pan.add(inventory_list);
        
        // add to bottom pan
        bottom_pan.add(about_item_value_label);
        bottom_pan.add(item_value_label);
        
        frame.setVisible(true);
        frame.add(top_pan, BorderLayout.NORTH);
        frame.add(center_pan);
        frame.add(bottom_pan, BorderLayout.SOUTH);
        
        // pack the frame
        frame.pack();
        
        // close our operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    /**
     * This class i0 a supplementary class to our MainFrame interface.  It is
     * the action listener for all of the events that happen in our main
     * interface and it has many methods that help organize requests and
     * data.
     * 
     * @author Chris O'Neil
     * @date 
     */
    
    public class mainFrameHandler implements ActionListener
    {
       // attributes
        MainFrame user_interface;
        JFileChooser file_chooser;
        FileNameExtensionFilter filter_txt;
        AddModifyFrame add_frame;
        AddModifyFrame modify_frame;
        
        mainFrameHandler()
        {
            // does nothing but allocate memory.
        }
            
        /**
         * This method is what actually listens to our user.  It acts as a
         * traffic controller, listening to what the user is doing and then
         * calling upon other functions and methods to try to meet the user's
         * requests.  It doesn't do much more than listen and boss other
         * methods around.
         */
        public void actionPerformed(ActionEvent e)
        {
            out.println(e.getSource());
            if (e.getSource() == load_inventory)
            {
                loadInventory();
                updateAllData();
            } else if (e.getSource() == add_item)
            {
                addItem();
                updateAllData();
            } else if (e.getSource() == save_inventory)
            {
                saveFile(); 
            } else if (e.getSource() == delete_item)
            {
                if (list_model.size() != 1)
                {
                    deleteItem();
                    updateAllData();
                } else {
                    list_model.removeAllElements();
                    list_model_IO.removeAllElements();
                    inventory_list.setModel(list_model);
                    inventory_list.revalidate();
                    inventory_list.repaint(); 
                }
            } else if (e.getSource() == exit_program)
            {
                System.exit(0);
            } else if (e.getSource() == show_items_combo || 
                    e.getSource() == sort_by_combo) 
            {
                updateAllData();
            } else if (e.getSource() == modify_item)
            {
                modifyItem();
                updateAllData();
            } else 
            {
                // nothing happens...
            }
            frame.pack();
            
        } // end method
        
        /**
         * This method updates the list shown to the user for any given sort
         * or filter that the user does.
         */
        public void updateAllData()
        {
            data_list = getItemArray();
            if (data_list.length == 0)
            {
                String title = "Empty Data Error";
                String description = "Please enter data to sort or filter.";
                emptyError(title, description);
            } else
            {
                if (sort_by_combo.getSelectedIndex() != -1)
                {
                    data_list = sortItems(data_list);
                }
                data_list = filterItems(data_list);
                current_model = listToModel(data_list);
                current_model_IO = listToModelIO(data_list);
                
                updateTotalValue(data_list);
                
                inventory_list.setModel(current_model);
                inventory_list.revalidate();
                inventory_list.repaint(); 
            }
        }
        
        /**
         * This function inputs an array of items and outputs a list model
         * formatted for the user to read.
         * @param data
         * @return DefaultListModel
         */
        public DefaultListModel<String> listToModel(Item[] data)
        {
            DefaultListModel<String> model_out = new DefaultListModel<String>();
            int size = data.length;
            
            for (int n = 0; n < size; n++)
            {
                model_out.addElement(data[n].getFormattedItem());
            }
            
            return model_out;
        }
        
        /**
         * This function takes in an array of items and ouptuts a list model
         * which the user cannot read, but is used for data purposes.
         * @param data_IO
         * @return DefaultListModel
         */
        public DefaultListModel<String> listToModelIO(Item[] data_IO)
        {
            DefaultListModel<String> model_out_IO = 
                    new DefaultListModel<String>();
            int size = data_IO.length;
            
            for (int n = 0; n < size; n++)
            {
                model_out_IO.addElement(data_IO[n].getOutputFormattedItem());
            }
            
            return model_out_IO;
        }
        
        
        /**
         * This method takes in a string (of a number) and reformats it to 
         * have two decimal places after it.  It requires that the number is a
         * valid input.
         * @param value
         * @return String
         */
        public String formatToOutput(String value)
        {
            if (value.matches("\\d+\\."))
            {
                value = value + "00";
            } else if (value.matches("([0-9]+)"))
            {
                value = value + ".00";
            } else if (value.matches("\\d+\\.\\d")){
                value = value + "0";
            }
            return value;
        }
        
        /**
         * This method is used to update the total value of the items in the
         * list in the main frame.  This function will update the actual label
         * in the JFrame.
         */
        public void updateTotalValue(Item[] data_list)
        {
            String out;

            
            double total = 0;
            if (data_list.length !=0)
            { 
                for (int i = 0; i < data_list.length; i++)
                {
                    total += data_list[i].getDoubleValue();
                }
                out = "" + total;
                out = "$" + formatToOutput(out);
            } else {
                out = no_items;
            }

            item_value_label.setText(out);
            item_value_label.repaint();
            item_value_label.revalidate();    
        }
        
       
        /**
         * This function displays an error message with input title and input
         * message
         * @param title
         * @param message
         */
        public void emptyError(String title, String message)
        {
            String alert_message = message;
            JOptionPane.showMessageDialog(null, alert_message, 
                    title, JOptionPane.ERROR_MESSAGE);
        }
        
        /**
         * This function takes in an array of items and sorts them based on 
         * the sort function that is currently selected.  This should never
         * be called when the index of the sort_by_combox is -1
         * @param sort_items
         * @return Item[]
         */
        public Item[] sortItems(Item[] sort_items)
        {
            Item[] sorted_items;

            if (sort_by_combo.getSelectedItem() == "Item Name")
            {
                sorted_items = sortByItemName(sort_items);
            } else if (sort_by_combo.getSelectedItem() == "Location")
            {
                sorted_items = sortByItemLocation(sort_items);
            } else if (sort_by_combo.getSelectedItem() == "Value")
            {
                sorted_items = sortByValue(sort_items);
            } else if (sort_by_combo.getSelectedItem() == "Purchase Date")
            {
                sorted_items = sortByPurchaseDate(sort_items);
            } else 
            {
                // this should not happen.
                sorted_items = null;
            }

            return sorted_items;
        }
        
        /**
         * This function returns an array of all items that are in inventory,
         * whether they are being shown to the user or not.
         * @return
         */
        public Item[] getItemArray()
        {

            int size = list_model.getSize();

            Item[] sort_array = new Item[size];
            for (int s = 0; s < sort_array.length; s++)
            {   
                Item temp_item = IOtoItem(list_model_IO.elementAt(s));
                sort_array[s] = temp_item;
            }
            
            return sort_array;
        }
       
        /**
         * This is a bubble sort method that sorts the items by value.
         * @param sort_array
         * @return Item[]
         */
        public Item[] sortByValue(Item[] sort_array)
        {
            int i = 0;
            int j = 0;
            Item temp_sort;
            int size = sort_array.length;
            
            // BUBBLE SORT
            for(i = 0; i < size; i++)
            {
                for(j = 1; j < (size-i); j++)
                {
                    if(sort_array[j-1].getDoubleValue() >
                            sort_array[j].getDoubleValue())
                    {
                        temp_sort = sort_array[j-1];
                        sort_array[j-1]=sort_array[j];
                        sort_array[j]=temp_sort;
                    }
                }
            } // end bubble sort.
     
            return sort_array;
        }

        /**
         * This is a bubble sort method that sorts the items by purchase date.
         * @param sort_array
         * @return Item[]
         */
        public Item[] sortByPurchaseDate(Item[] sort_array)
        {
            int i = 0;
            int j = 0;
            Item temp_sort;
            int size = sort_array.length;
            
            // BUBBLE SORT
            for(i = 0; i < size; i++)
            {
                for(j = 1; j < (size-i); j++)
                {
                    if(sort_array[j-1].getFormattedDate().compareTo(
                            sort_array[j].getFormattedDate()) > 0)
                    {
                        temp_sort = sort_array[j-1];
                        sort_array[j-1]=sort_array[j];
                        sort_array[j]=temp_sort;
                    }
                }
            } // end bubble sort.
     
            return sort_array;
        }
        
        /**
         * This is a bubble sort method that sorts items by item name
         * (description)
         * @param sort_array
         * @return Item[]
         */
        public Item[] sortByItemName(Item[] sort_array)
        {
            int i = 0;
            int j = 0;
            Item temp_sort;
            int size = sort_array.length;
            
            // BUBBLE SORT
            for(i = 0; i < size; i++)
            {
                for(j = 1; j < (size-i); j++)
                {
                    if(sort_array[j-1].getItemDescription().compareTo(
                            sort_array[j].getItemDescription()) > 0)
                    {
                        temp_sort = sort_array[j-1];
                        sort_array[j-1]=sort_array[j];
                        sort_array[j]=temp_sort;
                    }
                }
            } // end bubble sort.
     
            return sort_array;
        }
        
        /**
         * This method is a bubble sort that sorts the items by their location.
         * @param sort_array
         * @return Item[]
         */
        public Item[] sortByItemLocation(Item[] sort_array)
        {
            int i = 0;
            int j = 0;
            Item temp_sort;
            int size = sort_array.length;
            
            // BUBBLE SORT
            for(i = 0; i < size; i++)
            {
                for(j = 1; j < (size-i); j++)
                {
                    if(sort_array[j-1].getItemLocation().compareTo(
                            sort_array[j].getItemLocation()) > 0)
                    {
                        temp_sort = sort_array[j-1];
                        sort_array[j-1]=sort_array[j];
                        sort_array[j]=temp_sort;
                    }
                }
            } // end bubble sort.
     
            return sort_array;
        }

        
        /**
         * This function modifies an item that is at the selected index at the
         * time of the event.
         */
        public void modifyItem()
        {
            String match_data;
            String modify_title = "Modify Inventory Item";
            Item item_for_modify;
 
            int  index = inventory_list.getSelectedIndex();
            int inventory_index;
            if (inventory_list.getModel() != list_model)
            {
                try {
                    inventory_list.getModel().getElementAt(index);
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    String error_title = "No Item Selected";
                    String error_message = "Please select an item to modify";
                    emptyError(error_title, error_message); 
                    return;
                }
                match_data = inventory_list.getModel().getElementAt(index);
                inventory_index = findInventoryListIndex(match_data);
            } else {
                inventory_index = index;
            }
            
            try {
                IOtoItem(list_model_IO.elementAt(inventory_index));
            } catch (java.lang.ArrayIndexOutOfBoundsException e2) {
                String error_title = "No Item Selected";
                String error_message = "Please select an item to modify";
                emptyError(error_title, error_message);
                return;
            }
            item_for_modify = IOtoItem(
                    list_model_IO.elementAt(inventory_index));
                      
            modify_frame = new AddModifyFrame(
                    frame, modify_title, item_for_modify);
            modify_frame.pack();
            modify_frame.setVisible(true);
            
            Item change_item = modify_frame.getItem();
            if (change_item != null)
            {
                list_model.setElementAt(
                        change_item.getFormattedItem(), inventory_index);
                list_model_IO.setElementAt(
                        change_item.getOutputFormattedItem(), inventory_index);
            }
            modify_frame.dispose();
    
        }
        
        
        /**
         * This method finds the Inventory List index of any given string that
         * is formatted properly (not IO formatted)
         * @param match
         * @return
         */
        public int findInventoryListIndex(String match)
        {
            for (int i = 0; i < list_model.size(); i++)
            {
                if (match.equals(list_model.getElementAt(i)))
                {
                    return i;
                }
            }
            return -1; // this should never happen
        }
        

        /**
         * This method takes in an input array of items and filters them and
         * outputs another array of items. This array of items output will only 
         * include those of which are within the location (type) of filtered.
         * @param data_in
         * @return Item[]
         */
        public Item[] filterItems(Item[] data_in)
        {
            int index = show_items_combo.getSelectedIndex();
            if (index == -1)
            {
                // this should never happen
                return data_in;
            }
            
            String compare_type = item_types[index];

            int size = data_in.length;
            
            ArrayList<Item> filtered_data = new ArrayList<Item>();
            if (compare_type != "All")
            {
                for (int i = 0; i < size; i++)
                {
                    if (compare_type.equals(data_in[i].getItemType()))
                    { 
                        filtered_data.add(data_in[i]);
                    }
                }
            } else {
                return data_in;
            }
            int filter_size = filtered_data.size();
            return filtered_data.toArray(new Item[filter_size]);

        }
        
        
        /**
         * This function takes in parameter of an input string that MUST BE
         * FORMATTED in the way that our output IO is formatted.  It takes in 
         * a string and creates an Item (data structure).
         * @param IO
         * @return Item
         */
        public Item IOtoItem(String IO)
        {
            Scanner scan = new Scanner(IO);
            scan.useDelimiter("[{^}]");

                String temp_image;
                String temp_day;
                String temp_month;
                String temp_year;
                String temp_price;
                String temp_description;
                String temp_type;

                temp_description = scan.next();
                temp_type = scan.next();
                temp_price = scan.next();
                temp_month = scan.next();
                temp_day = scan.next();
                temp_year = scan.next();
                temp_image = scan.next();
        
                Item item = new Item(temp_image,
                                     temp_day,
                                     temp_month,
                                     temp_year,
                                     temp_price,
                                     temp_description,
                                     temp_type);

            return item;
            
        }
        
        
        /**
         * This function calls a filechooser and allows the user to select a 
         * .txt file (formatted in the correct way) to input into the program.
         */
        public void loadInventory()
        {
            // we need to get our loading menu.
            file_chooser = new JFileChooser();
            filter_txt = new FileNameExtensionFilter("Text Files", "txt");
            file_chooser.setFileFilter(filter_txt); // set our filter
            int returnVal = file_chooser.showOpenDialog(add_item);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                input_file = file_chooser.getSelectedFile();
                try {
                    read_file = new Scanner(input_file);
                    read_file.useDelimiter("[{^}]"); // change the delimiter
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
                if (read_file.hasNext() == false)
                {
                    String title = "Error: Empty File";
                    String message = "The file you have selected is empty.";
                    emptyError(title, message);
                    return;
                }
                addFile();
            }
        }
        
        /**
         * This method deletes the selected Item.  It can only delete one item
         * at any given time.
         */
        public void deleteItem()
        {
            String match_data;
                
            int index = inventory_list.getSelectedIndex();
            int inventory_index;
            if (inventory_list.getModel() != list_model)
            {
                try {
                    inventory_list.getModel().getElementAt(index);
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    String error_title = "No Item Selected";
                    String error_message = "Please select an item to delete";
                    emptyError(error_title, error_message); 
                    return;
                }
                
                match_data = inventory_list.getModel().getElementAt(index);
                inventory_index = findInventoryListIndex(match_data);
            } else {
                inventory_index = index;
            }
                
            list_model.remove(inventory_index);
            list_model_IO.remove(inventory_index);
        }
        
        /**
         * This method checks to see if there is a valid location
         * @param check
         * @return boolean
         */
        public boolean checkLocation(String check)
        {
            for (int i = 0; i < item_types.length; i++)
            {
                if (check.equals(item_types[i]))
                {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * This method checks to see if there is a valid Value
         * @param check
         * @return boolean
         */
        public boolean checkValue(String check)
        {
            return check.matches("\\d+\\.") ||
                   check.matches("\\d+\\.\\d\\d") ||
                   check.matches("([0-9]+)");
        }
        
        /**
         * This method checks to see if there is a valid Month
         * @param check
         * @return boolean
         */
        public boolean checkMonth(String check)
        {
            int month = Integer.parseInt(check);
            return (month <= 12 && month >= 1);
        }
        
        /**
         * This function checks to see if there is a valid Day.  It depends on
         * there being a valid month
         * @param day_string
         * @param month
         * @return boolean
         */
        public boolean checkDay(String day_string, String month)
        {
            int day = Integer.parseInt(day_string);
            if (month == "3" || month == "5" || month == "8" ||
                    month == "10")
            {
                return (day >= 1 && day <= 30);
            } else if (month == "1")
            {
                return (day >= 1 && day <= 29);
            } else
            {
                return (day >= 1 && day <= 31);
            }
        }
        
        /**
         * This method checks to see if the year input is valid.  It will not
         * change dates less than (such as 1985) to <1990
         * @param year_string
         * @return boolean
         */
        public boolean checkYear(String year_string)
        {
            if (year_string == "<1990")
            {
                return true;
            }
            int year = Integer.parseInt(year_string);
            return (year >= 1990 && year <= 2013);
            
        }
        
        /**
         * This method checks to see if there is a valid image ending.
         * @param image
         * @return boolean
         */
        public boolean checkImage(String image)
        { 
            if (image.equals("null"))
            {
                return true;
            } else if (image.contains(".jpeg") ||
                    image.contains(".bmp") ||
                    image.contains(".png") ||
                    image.contains(".gif"))
            {
                return true;
            } else {
                out.println("The image is not being processed.");
                return false;
                
            }
            
        }
        
        /**
         * This function calls all of the mini-methods to make sure that the
         * entire input is valid.
         * @param IO
         * @return boolean
         */
        public boolean checkValidInput(String IO)
        {
            Scanner check = new Scanner(IO);
            check.useDelimiter("[{^}]");
            String month;
            // check the description
            if (!check.hasNext()) {
                check.close();
                load_error_type = "Input Size";
                return false; }
            if (check.next().isEmpty()) {
                check.close();
                load_error_type = "Description";
                return false; }
            if (!check.hasNext()) {
                check.close();
                load_error_type = "Input Size";
                return false; }
            if (!checkLocation(check.next())) {
                check.close();
                load_error_type = "Location";
                return false; }
            if (!check.hasNext()) {
                check.close();
                load_error_type = "Input Size";
                return false; }
            if (!checkValue(check.next())) {
                check.close();
                load_error_type = "Value";
                return false; }
            if (!check.hasNext()) {
                check.close();
                load_error_type = "Input Size";
                return false; }
            month = check.next();
            if (!checkMonth(month)) {
                check.close();
                load_error_type = "Month";
                return false; }
            if (!check.hasNext()) {
                check.close();
                load_error_type = "Input Size";
                return false; }
            if (!checkDay(check.next(), month)) {
                check.close();
                load_error_type = "Month";
                return false; }
            if (!check.hasNext()) {
                check.close();
                load_error_type = "Input Size";
                return false; }
            if (!checkYear(check.next())) {
                check.close();
                load_error_type = "Year";
                return false; }
            if (!check.hasNext()) {
                check.close();
                load_error_type = "Input Size";
                return false; }
            if (!checkImage(check.next())) {
                check.close();
                load_error_type = "Image";
                return false; }
            
            check.close();
            return true;
              
        }
        
        /**
         * This method adds a file to our application. It is heavily relied on
         * by our LoadFile() method, which takes a file from the filechooser
         * and hands it off to this function to actually read and add to our
         * application.
         */
        public void addFile()
        {
            ArrayList<String> error_types = new ArrayList<String>();
            ArrayList<Integer> error_lines = new ArrayList<Integer>();
            String error_message = "";
            String error_title;
            int count = 0;
            while (read_file.hasNextLine())
            {
                String current_line = read_file.nextLine();
                if (checkValidInput(current_line))
                {
                    Item new_item = IOtoItem(current_line);
                    updateDataModels(new_item);
                    //out.println(new_item.getFormattedItem());
                } else {
                    error_types.add(load_error_type);
                    error_lines.add(count);
                    continue;
                }
                count++;
            } // end while loop

            if (error_lines.size() != 0)
            {
                error_title = "Load Error";
                for (int c = 0; c < error_lines.size(); c++)
                {
                    error_message += "There was an error in line " +
                            error_lines.get(c) + " of your input document." +
                            " This error was an incorrect " + error_types.get(c)
                            + ". \n";
                }
                
                emptyError(error_title, error_message);
            }
            
        } // end method
        
        /**
         * This method calls another window and class to add a data point to
         * our application.
         */
        public void addItem()
        {
            String add_title = "Input Inventory Item";
            Item null_item = null;
            
            add_frame = new AddModifyFrame(frame, add_title, null_item);
            add_frame.pack();
            add_frame.setVisible(true);
            
            inventory_item = add_frame.getItem();
            if (inventory_item.checkValidData())
            {
                updateDataModels(inventory_item);
            }
            add_frame.dispose();
        }
        
        /**
         * This function can be called to update our data structure.  Any method
         * can hand off and item (formatted correctly) to this method, and it
         * will successfully add it to the data structure.
         * @param update
         */
        public void updateDataModels(Item update)
        {
            inventory_item_string = update.getFormattedItem();
            inventory_item_string_IO = 
                    update.getOutputFormattedItem();
            list_model.addElement(inventory_item_string);
            list_model_IO.addElement(inventory_item_string_IO);
            inventory_list.repaint();
            inventory_list.revalidate();
            frame.pack();
        }
        
        
        /**
         * This function calls upon the file chooser to save our current data
         * to a text file in the directory of the user's choice.
         */
        public void saveFile()
        {
            //JMenuItem save_inventory = new JMenuItem("Save Inventory");
            file_chooser = new JFileChooser();
            filter_txt = new FileNameExtensionFilter(
                    "Text Files", "txt");
            file_chooser.setFileFilter(filter_txt); // set our filter
            int return_val = file_chooser.showSaveDialog(file_chooser);
            if(return_val == JFileChooser.APPROVE_OPTION)
            {
                output_file = file_chooser.getSelectedFile();
            }
            
            try {
                inventory_out = new PrintStream(output_file);
                for (int i = 0; i < list_model_IO.getSize(); i++)
                {
                    inventory_out.println(list_model_IO.get(i));
                }
                inventory_out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        
    } // end helper class
    
  
} // end class  



