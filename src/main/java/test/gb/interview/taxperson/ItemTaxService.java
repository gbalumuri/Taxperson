package test.gb.interview.taxperson;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Main Taxable item (exposed at "item" path)
 * Needs item as a parameter for calculating tax
 */
@Path("item")
public class ItemTaxService {
	/**
	 * Can create two enumerations for these constants.
	 */
	private static final float LUXURY_TAX_RATE = 0.09f;
	private static final float NECESSITY_TAX_RATE = 0.01f;
	private static final String NECESSITY_TYPE = "Necessity";
	private static final String LUXURY_TYPE = "Luxury";
	private ObjectMapper mapper = new ObjectMapper();

    /**
     * Method handling HTTP GET requests for given item. 
     * The response to the client willbe "JSON" media type.
     *
     * @return String that will be returned as a JSON response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public String getTax(@PathParam("id") String id) {
    	String json = "{}";
    	try {
    		json= mapper.writeValueAsString(getItem(id));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return json;
    }
    
    /**
     * Method handling HTTP GET requests for without item details. 
     * The response to the client willbe "JSON" media type with boiler plate
     * error message.
     *
     * @return String that will be returned as a JSON response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTax() {
        return "{\"error\": \"Missing Item for tax calculation\"}";
    }
    
    /**
     * Returns the Item object for given item id. Uses dummy logic to
     * figure out if the item is a luxury item and the item price. Real logic can
     * be implemented but using dummy logic for testing only.
     * 
     * @param id
     * @return Item.
     */
    private Item getItem(String id) {
		Item item;
		final int decide = id.length() % 2;
		if (decide != 0) {
			/*
			 * If item id length is odd, it is considered a luxury item and 
			 * price is four times the length just to bump up the price.
			 * And hundred for cents.
			 */
			item = createLuxuryItem(id, id.length() * 400);
		} else {
			/*
			 * If item id length is even, it is considered a necessary item and 
			 * price is just the length. And hundred for cents.
			 */
			item = createNecessityItem(id, id.length() * 100);
		}
		return item;		
	}

	private Item createNecessityItem(String id, int price) {
		Item i = new Item();
		i.name = id;
		i.price = price;
		i.tax = (int) (price * NECESSITY_TAX_RATE);
		i.type = NECESSITY_TYPE;
		i.total = i.price + i.tax;
		return i;
	}

	private Item createLuxuryItem(String id, int price) {
		Item i = new Item();
		i.name = id;
		i.price = price;
		i.tax = (int) (price * LUXURY_TAX_RATE);
		i.type = LUXURY_TYPE;
		i.total = i.price + i.tax;
		return i;
	}

}
