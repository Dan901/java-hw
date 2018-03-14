package hr.fer.zemris.java.webserver;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

@SuppressWarnings("javadoc")
public class RequestContextTest {

	private RequestContext rc;

	private RCCookie cookie;

	private ByteArrayOutputStream bos;

	@Before
	public void setUp() {
		bos = new ByteArrayOutputStream();
		rc = new RequestContext(bos, new HashMap<>(), new HashMap<>(), new ArrayList<>());
	}

	@Test
	public void testWrite1() throws IOException {
		rc.write(new byte[0]);
		byte[] result = ("HTTP/1.1 200 OK\r\n" + 
						"Server: DPS\r\n" + 
						"Content-Type: text/html;charset=UTF-8\r\n" + 
						"\r\n").getBytes(StandardCharsets.ISO_8859_1);
		assertArrayEquals(result, bos.toByteArray());
	}

	@Test
	public void testWrite2() throws IOException {
		String data = "Čevapčići";

		rc.setStatusCode(202);
		rc.setStatusText("Accepted");
		rc.setMimeType("text/plain");
		rc.setEncoding("ISO_8859_1");
		
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1", "/", "HttpOnly"));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, null, null));
		
		rc.write(data);
		
		byte[] result = ("HTTP/1.1 202 Accepted\r\n" + 
				"Server: DPS\r\n" + 
				"Content-Type: text/plain;charset=ISO_8859_1\r\n" +
				"Set-Cookie: korisnik=\"perica\"; Domain=127.0.0.1; Path=/; Max-Age=3600; HttpOnly\r\n" +
				"Set-Cookie: zgrada=\"B4\"\r\n" + 
				"\r\n" + 
				"Čevapčići").getBytes(StandardCharsets.ISO_8859_1);
		assertArrayEquals(result, bos.toByteArray());
	}
	
	@Test(expected=RuntimeException.class)
	public void testChangeAfterHeaderGenerated1() throws IOException{
		rc.write(new byte[0]);
		rc.setStatusCode(200);
	}
	
	@Test(expected=RuntimeException.class)
	public void testChangeAfterHeaderGenerated2() throws IOException{
		rc.write(new byte[0]);
		rc.setStatusText("OK");
	}
	
	@Test(expected=RuntimeException.class)
	public void testChangeAfterHeaderGenerated3() throws IOException{
		rc.write(new byte[0]);
		rc.setEncoding("UTF-8");
	}
	
	@Test(expected=RuntimeException.class)
	public void testChangeAfterHeaderGenerated4() throws IOException{
		rc.write(new byte[0]);
		rc.setMimeType("image/jpg");
	}
	
	@Test
	public void testConstructor(){
		new RequestContext(bos, null, null, null);
	}

	@Test(expected=NullPointerException.class)
	public void testInvalidConstructor(){
		new RequestContext(null, null, null, null);
	}
	
	@Test
	public void testParameters(){
		String name1 = "p1";
		String value1 = "v1";
		String name2 = "p2";
		String value2 = "v2";
		Map<String, String> map = new HashMap<>();
		map.put(name1, value1);
		map.put(name2, value2);
		RequestContext rc2 = new RequestContext(bos, map, null, null);
		
		assertEquals(value1, rc2.getParameter(name1));
		assertEquals(value2, rc2.getParameter(name2));
		assertEquals(2, rc2.getParameterNames().size());
	}
	
	@Test
	public void testPersistentParameters(){
		String name = "p1";
		String value = "v1";
		rc.setPersistentParameter(name, value);
		assertEquals(value, rc.getPersistentParameter(name));
		
		rc.removePersistentParameter(name);
		assertNull(rc.getPersistentParameter(name));
	}
	
	@Test
	public void testTempParameters(){
		String name = "p1";
		String value = "v1";
		rc.setTemporaryParameter(name, value);
		assertEquals(value, rc.getTemporaryParameter(name));
		
		rc.removeTemporaryParameter(name);
		assertNull(rc.getTemporaryParameter(name));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testReadOnlySet1(){
		Set<String> set = rc.getParameterNames();
		set.add("test");
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testReadOnlySet2(){
		Set<String> set = rc.getPersistentParameterNames();
		set.add("test");
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testReadOnlySet3(){
		rc.setTemporaryParameter("p1", "v1");
		Set<String> set = rc.getTemporaryParameterNames();
		set.add("test");
	}
}
