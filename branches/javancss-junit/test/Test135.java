public interface Test135 {
  @Encoding(value = EncodingClass.EXTERNAL) byte[] baseContent = fileUploadHandler.getBaseContent();
  @Encoding byte[] fileContent = null;
}
