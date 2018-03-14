package hr.fer.zemris.java.tecaj.hw5.db;

import static hr.fer.zemris.java.tecaj.hw5.db.StudentDB.runCommand;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class DatabaseTests {

	private StudentDatabase db;
	private List<StudentRecord> result;
	
	@Before
	public void setUp() throws IOException{
		db = StudentDB.loadDatabase("database.txt");
	}
	
	@Test
	public void testIndexQuery1(){
		result = runCommand("indexquery jmbag =\"0000000029\"", db);
		assertEquals(1, result.size());
		assertEquals("0000000029", result.get(0).getJmbag());
	}
	
	@Test
	public void testIndexQuery2(){
		result = runCommand("indexquery  jmbag  = \t \"abc\"", db);
		assertEquals(0, result.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIndexQueryNoJmbag(){
		result = runCommand("indexquery jmbag = \"\"", db);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIndexQueryInvalidField(){
		result = runCommand("indexquery firstName = \"Marin\"", db);
	}
	
	@Test
	public void testQuerySingleExpr(){
		result = runCommand("query jmbag <= \"0000000002\"", db);
		assertEquals(2, result.size());
		
		result = runCommand("query jmbag>\"0000000061\"", db);
		assertEquals(2, result.size());
		
		result = runCommand("query jmbag != \"0000000040\"", db);
		assertEquals(62, result.size());
		
		result = runCommand("query lastName =      \"Gašić\"", db);
		assertEquals(1, result.size());
		
		result = runCommand("query lastName>=\"Vukojević\"", db);
		assertEquals(3, result.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryInvalidSingleExpr1(){
		result = runCommand("query jmbag > \"000*1\"", db);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryInvalidSingleExpr2(){
		result = runCommand("query JMBAG > \"0\"", db);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryInvalidSingleExpr3(){
		result = runCommand("query firstName !> \"0\"", db);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryInvalidSingleExpr4(){
		result = runCommand("query finalGrade = \"5\"", db);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryInvalidSingleExpr5(){
		result = runCommand("query firstName=lastName", db);
	}
	
	@Test
	public void testQueryLike(){
		result = runCommand("query lastName LIKE \"Hibner\"", db);
		assertEquals(1, result.size());
		
		result = runCommand("query firstName LIKE \"*Irena\"", db);
		assertEquals(1, result.size());
		
		result = runCommand("query firstName LIKE \"Irena*\"", db);
		assertEquals(1, result.size());
		
		result = runCommand("query firstName LIKE \"I*na\"", db);
		assertEquals(1, result.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryInvalidLike1(){
		result = runCommand("query firstName LIKE \"*Ire*na\"", db);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryInvalidLike2(){
		result = runCommand("query firstName LIKE \"\"", db);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryInvalidLike3(){
		result = runCommand("query firstName LIKE\"A*\"", db);
	}
	
	@Test
	public void testQueryMultipleExpr(){
		result = runCommand("query jmbag >= \"0000000060\" and lastName LIKE \"V*\"", db);
		assertEquals(2, result.size());
		
		result = runCommand("query jmbag > \"0\" and firstName = \"Marin\"", db);
		assertEquals(3, result.size());
		
		result = runCommand("query lastName<\"D\" and lastName>=\"C\"", db);
		assertEquals(3, result.size());
		
		result = runCommand("query jmbag > \"0000000060\" and jmbag < \"0000000010\"", db);
		assertEquals(0, result.size());
	}
}
