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
        int counter = 24; //used to advance the url to the next page
        String start = "https://www.surlatable.com/recipes/?srule=best-matches&start=0&sz=24";
        
    while(counter <= 96)
    {
        //1. Generate main file
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(start)) //starting URL…
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
                  
                  //String builders to use later
                  StringBuilder breadcrumbs = new StringBuilder("");
                  StringBuilder ingredients = new StringBuilder("");
                  StringBuilder procedure = new StringBuilder("");
                  
                  //boolean to tell certain loops to stop
                  boolean stop = false;
                  
                  for(int i=0; i < 1000; i++) //skip all the lines that dont really matter
                  {
                     recipeReader.nextLine();
                  }
                 
                  
                  while (recipeReader.hasNextLine())
                  {
                     String lineRecipe = recipeReader.nextLine();
                     
                     //Get the author of the recipie, and add it to the array for the csv file
                     if(lineRecipe.contains("recipe-author"))
                     { 
                        lineRecipe = recipeReader.nextLine();
                        System.out.println(lineRecipe);
                        arrayHolder[0] = lineRecipe; //add author to the array
                     }
         
                     //Get the breadcrumb trail (path) for the recipe, and make them into one string that can be added to the array for the csv file
                     if(lineRecipe.contains("breadcrumb-element"))
                     {
                        String crumb = lineRecipe.substring(lineRecipe.indexOf('>'), lineRecipe.length());
                        String shortCrumb = crumb.substring(1, crumb.indexOf('<'));
                        breadcrumbs.append(shortCrumb + "/"); 
                        arrayHolder[1] = breadcrumbs.toString(); //add the path to the array
                     }
         
                     //Get the recipe name, and add it to the array for the csv file
                     if(lineRecipe.contains("recipe-col-2 hide-mobile")) //I know this looks strange,
                     {                                                     //but the recipe tite is on the same line as "recipe-title",
                        lineRecipe = recipeReader.nextLine();              //so in order to find it I have to look on the line above it
                        System.out.println(lineRecipe);
                        arrayHolder[2] = lineRecipe; //add recipe name to the array
                     }
         
                     //Get the number of servings for the recipe, and add it to the array for the csv file
                     if(lineRecipe.contains("recipe-details-serves"))
                     {
                        lineRecipe = recipeReader.nextLine();
                        System.out.println(lineRecipe);
                        arrayHolder[3] = lineRecipe; //add servings to the array
                     }

                     //Get the ingredients for the recipe, and make them into one string that can be added to the array for the csv file
                     stop = false;
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
                              arrayHolder[4] = ingredients.toString(); //add ingredients to the array
                              //end ingredients reading
                           }
                        }
                     }
                     
                     //Get the procedure (instructions) for the recipe, and make them into one string that can be added to the array for the csv file
                     stop = false;
                     
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
                              arrayHolder[5] = procedure.toString(); //add instructions to the array
                              //end instructions reading
                           }
                        }
                     }
                  }
                  
                  //5. write information to excel(csv) file
                  CSVPrinter.write(arrayHolder); //send the array to CSVPrinter
                  
                  //Sleep between requests (10 seconds)
                  Thread.sleep(10000);
            }  
        }
       counter+=24; 
       start = "https://www.surlatable.com/recipes/?srule=best-matches&start="+counter+"&sz=24";
       System.out.println(start);
       System.out.println(counter);
     }   
        
    }
}
