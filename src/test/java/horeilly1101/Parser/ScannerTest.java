package horeilly1101.Parser;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static horeilly1101.Parser.Scanner.*;
import static horeilly1101.Parser.FlexScanner.*;

public class ScannerTest {

  @Test
  public void readTest() {
    System.out.println("hey");

    String str = "12 + 34 * sin(1)";
//    FlexScanner scan = new FlexScanner(new StringReader(str));
//    for(int i = 0; i < 10; i++) {
//      try {
//        System.out.println(scan.next_token().sym);
//      } catch (RuntimeException| IOException e) {
//        System.out.println(e);
//      }
//    }


    System.out.println(read(str));

    String str2 = "12 + 34 * sin(1) / csc(x) + a";
    System.out.println(read(str2));

    String str3 = "12 8 $";
    System.out.println(read(str3));
  }
}
