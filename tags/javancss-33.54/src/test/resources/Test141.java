public class TestJavadocNew {

	TestJavadocMethod test = new TestJavadocMethod();
	public void testMethod(final String text) {
		
	test.testMethod( new TestJavadocAnnotation(){
		
		/**
		 * 
		 */
		public void testMethod(){
			
		}
		});
	
	}
}
