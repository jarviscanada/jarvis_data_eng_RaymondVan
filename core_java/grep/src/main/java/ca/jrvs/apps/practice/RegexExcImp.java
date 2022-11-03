package ca.jrvs.apps.practice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Clean up and implement proper unit testing
public class RegexExcImp implements RegexExc {

  private Pattern jpegPattern = Pattern.compile("^.+\\.(jpg|jpeg)$");
  private Pattern ipPattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
  private Pattern wsPattern = Pattern.compile("^\\s*$");

  public static void main(String[] args) {
    String jpegTC1 = "abcdef";      // F
    String jpegTC2 = "abcdef.jpg";  // T
    String jpegTC3 = "jgaasd.jpeg";  // T
    String ipTC1 = "hi";            // F
    String ipTC2 = "0.0.0.0";       // T
    String ipTC3 = "999.9.0.999";   // T
    String ipTC4 = "999.0.999";     // F
    String ipTC5 = "999.0.9999";    // F
    String wsTC1 = "       ";       // T
    String wsTC2 = "";              // T
    String wsTC3 = "  .    ";       // F

    RegexExcImp regex = new RegexExcImp();

    boolean a = regex.matchJpeg(jpegTC1);
    boolean b = regex.matchJpeg(jpegTC2);
    boolean c = regex.matchJpeg(jpegTC3);
    System.out.println(jpegTC1 + ": " + a);
    System.out.println(jpegTC2 + ": " + b);
    System.out.println(jpegTC3 + ": " + c);

    boolean d = regex.matchIp(ipTC1);
    boolean e = regex.matchIp(ipTC2);
    boolean f = regex.matchIp(ipTC3);
    boolean g = regex.matchIp(ipTC4);
    boolean h = regex.matchIp(ipTC5);
    System.out.println(ipTC1 + ": " + d);
    System.out.println(ipTC2 + ": " + e);
    System.out.println(ipTC3 + ": " + f);
    System.out.println(ipTC4 + ": " + g);
    System.out.println(ipTC5 + ": " + h);

    boolean i = regex.isEmptyLine(wsTC1);
    boolean j = regex.isEmptyLine(wsTC2);
    boolean k = regex.isEmptyLine(wsTC3);
    System.out.println(wsTC1 + ": " + i);
    System.out.println(wsTC2 + ": " + j);
    System.out.println(wsTC3 + ": " + k);

    /*
    OUTPUT:
    abcdef: false
    abcdef.jpg: true
    jgaasd.jpeg: true
    hi: false
    0.0.0.0: true
    999.9.0.999: true
    999.0.999: false
    999.0.9999: false
           : true
    : true
      .    : false
     */
  }

  @Override
  public boolean matchJpeg(String filename) {
    Matcher matcher = jpegPattern.matcher(filename);
    return matcher.find();
  }

  @Override
  public boolean matchIp(String ip) {
    Matcher matcher = ipPattern.matcher(ip);
    return matcher.find();
  }

  @Override
  public boolean isEmptyLine(String line) {
    Matcher matcher = wsPattern.matcher(line);
    return matcher.find();
  }
}
