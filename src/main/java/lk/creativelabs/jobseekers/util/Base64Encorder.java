package lk.creativelabs.jobseekers.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64Encorder {

      public static  String encode(String file){
          String filePath = file.replaceFirst("file:///", "");

          try {
              // Decode the URL-encoded string
              String decodedPath = java.net.URLDecoder.decode(filePath, "UTF-8");

              // Create a Path object
              Path path = Paths.get(decodedPath);

               // Read all bytes from the file into a byte array
               byte[] fileBytes = Files.readAllBytes(path);

               String base64String = Base64.getEncoder().encodeToString(fileBytes);
               return base64String;

          } catch (IOException e) {
              e.printStackTrace();
          }

           return "";
      }
}
