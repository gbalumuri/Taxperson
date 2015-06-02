package test.gb.interview.taxperson;

import static org.junit.Assert.*;

import javax.ws.rs.core.Application;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ItemTaxServiceTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(ItemTaxService.class);
	}
	
	@Test
	public void getEmptyTaxTest() {
		final String errorMessage = target("item").request().get(String.class);
		assertTrue(JSONUtils.mayBeJSON(errorMessage));
		assertTrue(errorMessage.contains("error\":"));
		assertTrue(errorMessage.contains("Missing Item for tax calculation"));
	}
	
	@Test
	public void getEvenTax() {
		final String itemId = "jersey";
		final String evenItem = target("item/" + itemId).request().get(String.class);
		final JSONObject evenItemJson = JSONObject.fromObject(evenItem);
		assertEquals(evenItemJson.getInt("price"), itemId.length() * 100);
		assertEquals(evenItemJson.getString("name"), itemId);
		assertEquals(evenItemJson.getString("type"), "Necessity");
		assertEquals(evenItemJson.getInt("tax"), 6);
		assertEquals(evenItemJson.getInt("total"), itemId.length() * 100 + 6);
	}
	
	@Test
	public void getOddTax() {
		final String itemId = "perfume";
		final String oddItem = target("item/" + itemId).request().get(String.class);
		final JSONObject oddItemJson = JSONObject.fromObject(oddItem);
		assertEquals(oddItemJson.getInt("price"), itemId.length() * 400);
		assertEquals(oddItemJson.getString("name"), itemId);
		assertEquals(oddItemJson.getString("type"), "Luxury");
		assertEquals(oddItemJson.getInt("tax"), 252);
		assertEquals(oddItemJson.getInt("total"), itemId.length() * 400 + 252);
	}
}
