package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import provided.GivenNotFoundException;
import provided.StoryTestException;
import provided.ThenNotFoundException;
import provided.WhenNotFoundException;
import solution.StoryTesterImpl;
import solution.Given;
import solution.When;
import solution.Then;

public class PersonStoryUnitTest {
	
	public static class PersonStoryDDerivedTest extends PersonStoryDerivedTest {
		@Then("a person does something private &param")
		private void doSomethingPrivate(String param) {
			Assert.assertEquals("this", "that");
		}

		@Then("a person does something protected &param")
		protected void doSomethingProtected(String param) {
			Assert.assertEquals("this", "that");
		}
		
		@When("a person does something with an unexpected &type")
		public void doSomethingUnexpected(Exception param) {
			Assert.assertEquals("this", "that");
		}
		
		public static class Static {
			public static class _Static extends PersonStoryDDerivedTest {
				@Given("static static of &age")
				public void SS(Integer age) {
					this.aPerson(age);
				}
			}
			
			public class _NonStatic extends PersonStoryDDerivedTest {
				@Given("static non-static of &age")
				public void SnS(Integer age) {
					this.aPerson(age);
				}
			}
		}
		
		public class NonStatic {
			public class _NonStatic extends PersonStoryDDerivedTest {
				@Given("non-static non-static of &age")
				public void nSnS(Integer age) {
					this.aPerson(age);
				}
			}
		}

		@SuppressWarnings("unused")
		private class PrivateNonStatic {
			public class _NonStatic extends PersonStoryDDerivedTest {
				@Given("private non-static non-static of &age")
				private void pnSnS(Integer age) {
					this.aPerson(age);
				}
			}
		}
		
		@SuppressWarnings("unused")
		private static class PrivateStatic {
			public class _NonStatic extends PersonStoryDDerivedTest {
				@Given("private static static of &age")
				private void pSS(Integer age) {
					this.aPerson(age);
				}
			}
		}
	}

	public static final String given10 = "Given a person of age 10";
	public static final String given30 = "Given a person of age 30";
	public static final String celebrates2 = "When the person celebrates, and the number of hours is 2";
	public static final String celebrates10 = "When the person celebrates, and the number of hours is 10";
	public static final String fine = "Then the person feels Fine";
	public static final String tired = "Then the person feels Tired";
	public static final String smthPrivate = "Then a person does something private P";
	public static final String smthProtected = "Then a person does something protected P";
	
	@Before
	public void setUp() throws Exception {
	}
	
	private String join(String... arr) {
		String result = "";
		for (String str : arr) {
			result += str;
			result += "\n";
		}
		return result.substring(0, result.length() - 1);
	}

	@Test
	public void testGood1() throws Exception {
			String story = this.join(given10, celebrates10, fine);
			new StoryTesterImpl().testOnHirarchy(story, PersonStoryTest.class);
	}
	
	@Test
	public void testGood2() throws Exception {
			String story = this.join(given30, celebrates2, fine, celebrates10, tired);
			new StoryTesterImpl().testOnHirarchy(story, PersonStoryTest.class);
	}
	
	@Test(expected=StoryTestException.class)
	public void testBad() throws Exception {
			String story = this.join(given30, celebrates2, tired);
			new StoryTesterImpl().testOnHirarchy(story, PersonStoryTest.class);
	}

	@Test(expected=StoryTestException.class)
	public void testBadException() throws Exception {
			String story = this.join(given30, celebrates2, tired);
			try {
				new StoryTesterImpl().testOnHirarchy(story, PersonStoryTest.class);
			} catch (StoryTestException e) {
				Assert.assertEquals(e.getStep(), tired);
				Assert.assertEquals(e.getStoryExpected(), "Tired");
				Assert.assertEquals(e.getTestResult(), "Fine");
				throw e;
			}
	}

	@Test(expected=GivenNotFoundException.class)
	public void testGivenNotFound() throws Exception {
		String story = this.join("Given this does not exist", celebrates10, fine);
		new StoryTesterImpl().testOnHirarchy(story, PersonStoryTest.class);
	}

	@Test(expected=WhenNotFoundException.class)
	public void testWhenNotFound() throws Exception {
		String story = this.join(given10, "When some made up stuff like this", fine);
		new StoryTesterImpl().testOnHirarchy(story, PersonStoryTest.class);
	}
	
	@Test(expected=ThenNotFoundException.class)
	public void testThenNotFound() throws Exception {
		String story = this.join(given10, celebrates10, "Then this or that");
		new StoryTesterImpl().testOnHirarchy(story, PersonStoryTest.class);
	}

	@Test(expected=StoryTestException.class)
	public void testPrivateMethod() throws Exception {
		String story = this.join(given10, celebrates10, smthPrivate);
		try {
			new StoryTesterImpl().testOnHirarchy(story, PersonStoryDDerivedTest.class);
		} catch (StoryTestException e) {
			Assert.assertEquals(e.getStep(), smthPrivate);
			throw e;
		}
	}
	
	@Test(expected=StoryTestException.class)
	public void testProtectedMethod() throws Exception {
		String story = this.join(given10, celebrates10, smthProtected);
		try {
			new StoryTesterImpl().testOnHirarchy(story, PersonStoryDDerivedTest.class);
		} catch (StoryTestException e) {
			Assert.assertEquals(e.getStep(), smthProtected);
			throw e;
		}
	}
	
	@Test(expected=WhenNotFoundException.class)
	public void testUnexpectedType() throws Exception {
		String story = this.join(given10, "When a person does something with an unexpected type", fine);
		new StoryTesterImpl().testOnHirarchy(story, PersonStoryDDerivedTest.class);
	}
	
	@Test
	public void testNestedFirstLevel() throws Exception {
		new StoryTesterImpl().testOnNested(given10, PersonStoryDDerivedTest.class);
	}
	
	@Test(expected=GivenNotFoundException.class)
	public void testNestedNotFound() throws Exception {
		new StoryTesterImpl().testOnNested("Given foobar 123", PersonStoryDDerivedTest.class);
	}
	
	@Test
	public void testNestedNonStaticNonStatic() throws Exception {
		new StoryTesterImpl().testOnNested("Given non-static non-static of 12", PersonStoryDDerivedTest.class);
	}

	@Test
	public void testNestedStaticNonStatic() throws Exception {
		new StoryTesterImpl().testOnNested("Given static non-static of 12", PersonStoryDDerivedTest.class);
	}

	@Test
	public void testNestedStaticStatic() throws Exception {
		new StoryTesterImpl().testOnNested("Given static static of 12", PersonStoryDDerivedTest.class);
	}

	@Test
	public void testNestedPrivateNonStaticNonStatic() throws Exception {
		new StoryTesterImpl().testOnNested("Given private non-static non-static of 12", PersonStoryDDerivedTest.class);
	}
	
	@Test
	public void testNestedPrivateStaticStatic() throws Exception {
		new StoryTesterImpl().testOnNested("Given private static static of 12", PersonStoryDDerivedTest.class);
	}
	
}
