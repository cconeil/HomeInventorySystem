package inventory;


/**
 * This class is the data structure that will eventually be in our list on the
 * MainFrame of this application.
 * @param image NOTE: if there is no image, it is required that "" is input.
 * @param day
 * @param month
 * @param year
 * @param price
 * @param description
 * @param type
 * 
 * @author Chris O'Neil
 */

public class Item
{
    // set attributes
    String image;
    String day;
    String month;
    String year;
    String price;
    String description;
    String type;
    String check_decimal;
    
    String[] check_decimal_array;
    int index;
    
     
    public Item(String image_in,
                String day_in,
                String month_in,
                String year_in,
                String price_in,
                String description_in,
                String type_in)
    {
        
        image = image_in; // should be "null" if there is no image
        day = day_in;
        month = month_in;
        year = year_in;
        price = price_in;
        description = description_in;
        type = type_in;
    }
    
    /**
     * This function checks if the data in our structure is valid.  It is used
     * within our AddModifyFrame to see if we should use the object or just
     * simply delete it. Also, It is responsible for throwing the exception
     * telling the user that their input is incorrect.
     * 
     * @return boolean
     */
    public boolean checkValidData()
    {
        // the three things that we need to check are valid are the image,
        // the price, and the description,
        Double price_double;
        try {
            price_double = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
    
    /**
     * This method will take an item and turn it into a formatted string which
     * we can safely display to our user. It does no error checking.
     * @return String
     */
    
    public String getFormattedItem()
    {
        String show_item = new String("Item: ");
        String show_location = new String("Location: ");
        String show_value = new String("Value: ");
        String show_purchased = new String("Purchased: ");
        String space = new String(" ");
        String dollar = new String("$");
        String slash = new String("/");
        
        // return the string formatted correctly
        return show_item + description + space + show_location + type + space +
                show_value + dollar + price + space + show_purchased + month +
                slash + day + slash + year;

    }
    
    /**
     * This function gives us a version of the string that can be output to a
     * txt file.  This is a very easy version of the string to manipulate, so
     * we can use this often.  Basically, it uses ^ as a delimiter.
     * @return String
     */
    public String getOutputFormattedItem()
    {
        String delimiter = "^";
        
        return description + delimiter + type + delimiter + price + delimiter +
                month + delimiter + day + delimiter + year + delimiter+ image;
    }
    
    
    /**
     * This method will simply give the type of the item that we are dealing
     * with.
     * @return String
     */
    public String getItemType()
    {
        return type;
    }
    

    /**
     * Returns the item Description (or title)
     * @return String
     */
    public String getItemDescription()
    {
        return description;
    }
    
    /**
     * Returns the item's value
     * @return String
     */
    public String getItemValue()
    {
        return price;
    }
    
    /**
     * Returns the Item image (path)
     * @return String
     */
    public String getItemImage()
    {
        return image;
    }
    
    /**
     * return the Item's location (type)
     * @return String
     */
    public String getItemLocation()
    {
        return type;
    }
    
    /**
     * returns the day as an integer.
     * @return int
     */
    public int getItemDayInt()
    {
        return Integer.parseInt(day);
    }
    
    /**
     * returns the month as an integer.
     * @return
     */
    public int getItemMonthInt()
    {
        return Integer.parseInt(month);
    }
    
    /**
     * returns the item year.
     * @return String
     */
    public String getItemYear()
    {
        return year;
    }
    
    /**
     * This returns the formatted date that we use for sorting.  The formatted
     * date is a string of a 4 digit year, two didgit month, then two digit day.
     * @return String
     */
    public String getFormattedDate()
    {
        String out = "";
        if (year == "<1990")
        {
            out = out + "1989";
        } else {
            out = year;
        }
        
        if (month.length() != 2)
        {
            out = out + "0" + month;
        } else {
            out = out + month;
        }
        
        if (day.length() != 2)
        {
            out = out + "0" + day;
        } else {
            out = out + day;
        }
        
        //System.out.println(out);
        return out;
    }
    
    /**
     * This method returns the double value of the item.  This is used for any
     * math that might be done on the item's value.
     * @return Double
     */
    public Double getDoubleValue()
    {
        return Double.valueOf(price);
    }
    
 
    
} // end class
