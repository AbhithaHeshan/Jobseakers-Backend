package lk.creativelabs.jobseekers.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileServer {


    @Value("${localhost.url}")
    private  String host;

    @Value("${server.port}")
    private  String port;

    @Value("${server.servlet.context-path}")
    private  String contextPath;


    @Value("${file.prefix}")
    private  String prefix;


    public static void createDrictory(String folderPath){
        File assetsFolder  = new File(folderPath);

        if(!assetsFolder.exists()){
            boolean mkdir = assetsFolder.mkdir();
            if(!mkdir){
                //new Exeption();
            }
        }
    }


    public static String createDrictoryAndSaveFile(String folderPath,MultipartFile file) throws Exception {
        File assetsFolder  = new File(folderPath);

        try {
            if(!assetsFolder.exists()){
                boolean mkdir = assetsFolder.mkdir();
                if(!mkdir){
                    //new Exeption();
                }
            }
            return saveFile(file,folderPath);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }


    public static String saveFile(MultipartFile file,String destinationDirPath) throws Exception {

        try {
          //  byte[] fileBytes = file.getBytes();
            String randomId = UUID.randomUUID().toString();
            Path filePath = Paths.get(destinationDirPath ,randomId+"-"+file.getOriginalFilename());
         //    Files.write(filePath, fileBytes);

//           //  String filename = randomId + file.getOriginalFilename();
//            System.out.println(destinationDirPath + "dfefefe ");
//            File targetFile = new File(filePath.toUri());
//            file.transferTo(targetFile);



            try (InputStream inputStream = file.getInputStream();
                 BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
                 byte[] buffer = new byte[1024];
                 int bytesRead;
                 while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                 }
            }
            return  filePath.toUri().toString(); //toUri().toString()
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }

    }


    public  String uriToFileName(String filePath) {

        try {
            URI uri = new URI(filePath);
            String path = uri.getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);

             System.out.println("File Name: " + fileName);
             return fileName;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;

    }


    public  String createFileLink(String fileName, String directory){

       return "http://"+host+":"+port+contextPath+"/"+prefix+"/"+directory+"/"+fileName;

    }


}
