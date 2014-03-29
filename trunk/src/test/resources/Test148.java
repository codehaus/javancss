public class ClassJava7 {
  public void foo() {
	try {
		// do nothing
	}
	catch (java.beans.PropertyVetoException | java.beans.IntrospectionException e) {
		// do nothing
	}
  }

  public void bar() {
	try {
		// do nothing
	}
	catch (java.beans.PropertyVetoException e) {
		// do nothing
	}
  }
  
  public void baz() {
	String zipFileName = "";
	String outputFileName = "";
	java.nio.charset.Charset charset = java.nio.charset.Charset.forName("US-ASCII");
    java.nio.file.Path outputFilePath = java.nio.file.Paths.get(outputFileName);
	
	try (
      java.util.zip.ZipFile zf = new java.util.zip.ZipFile(zipFileName);
      java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(outputFilePath, charset)
    ) {
		// do nothing
	}
	catch (java.beans.PropertyVetoException e) {
		// do nothing
	}
  }
  
}

