public class DigestResult {
  private final String fileName;
  private final DigestType digestType;
  private final String digestHex;
  private final DigestStatus status;

  public DigestResult(String fileName, DigestType digestType, String digestHex, DigestStatus status) {
    this.fileName = fileName;
    this.digestType = digestType;
    this.digestHex = digestHex;
    this.status = status;
  }

  public String getFileName() {
    return fileName;
  }

  public DigestType getDigestType() {
    return digestType;
  }

  public String getDigestHex() {
    return digestHex;
  }

  public DigestStatus getStatus() {
    return status;
  }
}