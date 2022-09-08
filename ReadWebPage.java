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
        String columnNames[] = {"Author","Path","Name","Servings","Ingredients","Instructions"};
        CSVPrinter.write(columnNames);
        
        
        //new code
        //Lhkjnkjn
        
        
        //2. search main file for links to recipes 
        String link = "placeholder";

        //Resourcing Arraylist
        String[] arrayHolder = new String[6]; //[author0, path1, name2, servings3, ingredients4, instructions5]
        //ArrayList<String[]> list = new ArrayList<String[]>(); //USE CSV array thingy
        
        File toScan = new File("mainFile.txt");
        Scanner mainReader = new Scanner(toScan);
        
        //skip the first 
        for(int i=0; i < 1514; i++)
        {
            mainReader.nextLine();
        }
        while (mainReader.hasNextLine())
        {
            String line = mainReader.nextLine();
            if(line.contains("thumb-link\" href=\"https://"))
            {  
               String shortLine = line.substring(28, line.length());
               link = shortLine.substring(0, shortLine.indexOf('"'));
               
               //check that it is getting the links with this print line
               System.out.println(link);
               
               
            //3. generate new file based on the link
               HttpClient client1 = HttpClient.newHttpClient();
               HttpRequest request1 = HttpRequest.newBuilder()
                  .uri(URI.create(link)) //recipe webpage
                  .GET() // GET is default
                  .build();

                  HttpResponse<String> response1 = client1.send(request1,
                  HttpResponse.BodyHandlers.ofString());


               //output to a file so its easy to mess with
                  FileOutputStream fs1 = new FileOutputStream("recipe.txt");
                  PrintWriter pw1 = new PrintWriter(fs1);
                  pw1.println(response1.body()); //response.body() is the html source code in a string format
                  pw1.close();
                  
                  
               //in here is where the stuff will be found and then written to the excel file
                  //4. Search file for new information
                  File toScanRecipe = new File("recipe.txt");
                  Scanner recipeReader = new Scanner(toScanRecipe);
                  
                  for(int i=0; i < 1000; i++) //skip all the lines that dont really matter
                  {
                     recipeReader.nextLine();
                  }
                  
                  while (recipeReader.hasNextLine())
                  {
                     String lineRecipe = recipeReader.nextLine();
                     
                     if(lineRecipe.contains("recipe-author"))
                     { 
                        lineRecipe = recipeReader.nextLine();
                        System.out.println(lineRecipe);
                        arrayHolder[0] = lineRecipe; //author
                        //write to file
                     }
         
                     boolean stop = false;
                     StringBuilder breadcrumbs = new StringBuilder("");
                     if(lineRecipe.contains("breadcrumb-element"))
                     {
                        String crumb = lineRecipe.substring(lineRecipe.indexOf('>'), lineRecipe.length());
                        String shortCrumb = crumb.substring(1, crumb.indexOf('<'));
/*problem*/             breadcrumbs.append(shortCrumb + "/");
                        arrayHolder[1] = breadcrumbs.toString(); //path
                     }
         
                     if(lineRecipe.contains("recipe-col-2 hide-mobile")) //I know this looks strange,
                     {                                                     //but the recipe tite is on the same line as "recipe-title",
                        lineRecipe = recipeReader.nextLine();              //so in order to find it I have to look on the line above it
                        System.out.println(lineRecipe);
                        arrayHolder[2] = lineRecipe; //recipe name
                     }
         
                     if(lineRecipe.contains("recipe-details-serves"))
                     {
                        lineRecipe = recipeReader.nextLine();
                        System.out.println(lineRecipe);
                        arrayHolder[3] = lineRecipe; //servings
                        //write to file
                     }

                     stop = false;
                     StringBuilder ingredients = new StringBuilder("");
                     if(lineRecipe.contains("recipe-details-ingredients"))
                     {
                        while(recipeReader.hasNextLine() && !stop)
                        {
                           lineRecipe = recipeReader.nextLine();
                           System.out.println(lineRecipe);
                           ingredients.append(lineRecipe + " ");

                           if(lineRecipe.contains("</div>"))
                           {
                              stop = true;
                              String arrayIngr = ingredients.toString();
                              arrayHolder[4] = arrayIngr; //ingredients
                              //end ingredients reading
                           }
                        }
                     }
                     
                     stop = false;
                     StringBuilder procedure = new StringBuilder("");
                     if(lineRecipe.contains("recipe-details-procedure"))
                     {
                        while(recipeReader.hasNextLine() && !stop)
                        {
                           lineRecipe = recipeReader.nextLine();
                           System.out.println(lineRecipe);
                           procedure.append(lineRecipe + " ");
                           
                           if(lineRecipe.contains("</div>"))
                           {
                              stop = true;
                              String arrayProc = procedure.toString();
                              arrayHolder[5] = arrayProc; //instructions
                              //end instructions reading
                           }
                        }
                     }
                  }
                  //5. write information to excel file (do this in each if statement)
                  CSVPrinter.write(arrayHolder); //send the array to CSVPrinter
                  
               //Sleep between requests
                  Thread.sleep(10000);
            }  
        }
    }
}
