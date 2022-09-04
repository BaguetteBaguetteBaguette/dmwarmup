import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.*;
import java.util.*;

public class ReadWebPage 
{
    public static void main(String[] args) throws IOException, InterruptedException 
    {

        //1. Generate main file
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.surlatable.com/recipes/?srule=best-matches&start=0&sz=24")) //starting URL…
                .GET() // GET is default
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

         
//output to a file so its easy to mess with (you won’t be for your finished program)
         FileOutputStream fs = new FileOutputStream("mainFile.txt");
         PrintWriter pw = new PrintWriter(fs);
        pw.println(response.body()); //response.body() is the html source code in a string format. It outputs to a file so you can see it easier right now, but you will ultimately want to just manipulate the strings a lot
        pw.close();
        
        
        
        //new code
        
        //2. search main file for links to recipes 
        String link = "placeholder";
        
        
        File toScan = new File("mainFile.txt");
        Scanner mainReader = new Scanner(toScan);
        
        for(int i=0; i < 1514; i++)
        {
            mainReader.nextLine();
        }
        while (mainReader.hasNextLine())
        {
            String line = mainReader.nextLine();
            if(line.contains("thumb-link\" href=\"https://"))
            {
               //System.out.println(line);
               //System.out.println("success");
               //System.out.println(n);
               //n++;
               
               String shortLine = line.substring(28, line.length());
               link = shortLine.substring(0, shortLine.indexOf('"'));
               //System.out.println(index);
               System.out.println(link);
               
               
               //3. generate new file based on link
               HttpClient client1 = HttpClient.newHttpClient();
               HttpRequest request1 = HttpRequest.newBuilder()
                  .uri(URI.create(link)) //recipie webpage
                  .GET() // GET is default
                  .build();

                  HttpResponse<String> response1 = client1.send(request1,
                  HttpResponse.BodyHandlers.ofString());


               //output to a file so its easy to mess with (you won’t be for your finished program)
                  FileOutputStream fs1 = new FileOutputStream("recipe.txt");
                  PrintWriter pw1 = new PrintWriter(fs1);
                  pw1.println(response1.body()); //response.body() is the html source code in a string format. It outputs to a file so you can see it easier right now, but you will ultimately want to just manipulate the strings a lot
                  pw1.close();
                  
            //Sleep between requests
                Thread.sleep(10000);
            }  
        }
    }
}
