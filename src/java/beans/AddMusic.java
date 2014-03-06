package beans;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tom.Cocozzello
 */
public class AddMusic extends Application
{
    static int firstRunNumber = 0;
    static File folder = null;
    static File[] listOfNames = null;
    static int namesLength = 0;
    List<File> mediaQueueNames = new ArrayList<File>();
    static MediaPlayer currentPlayer;
    static List<MediaPlayer> mediaQueue = new ArrayList<MediaPlayer>();
    
    public static void main(String[] args) throws Exception { launch(args); }

        //when it runs the first time the start methond is ran and everything is populated and current player is set to first song
    @Override
        public void start(Stage stage) throws MalformedURLException 
        {
             buildEveryTime();
            Thread t1 = new Thread(new Runnable() {
            public void run()
            {
                playQueue();
            }});  t1.start();
        }
        
        public void playQueue()
       {  
           //update arrays 
           buildEveryTime(); 
           final Label status = new Label("Init");     
           PrintWriter out;
           while(true)
           {     
               buildEveryTime();
               if(mediaQueue.size() > 0)
               {
                    int mediaQueueLenght = mediaQueueNames.size();     
                    setMediaPlayer(mediaQueue.get(0));
                    //wait here till song is done buffering
                    while(!getMediaPlayer().getStatus().toString().equals("READY"))
                    {
                        try 
                        {
                            Thread.sleep(1000);
                            System.out.println("Song is buffering");
                        } 
                        catch (InterruptedException ex) 
                        {
                            System.out.println("Failed to Thread.sleep() in song buffer");
                        }
                    }
                    //play first song in queue
                    getMediaPlayer().play();
                    //take the first name from the queue
                    try
                    {
                        out = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\Tom.Cocozzello\\Documents\\School\\Pi Documents\\AddMusicToQueue\\src\\java\\beans\\Queue.txt")));
                        //out.write("");
                        if(mediaQueueLenght > 1)
                        { 
                            String tempQueueMusicList = "";
                            for(int k = 1; k < mediaQueueLenght; k++)
                            {
                                String temp = mediaQueueNames.get(k).getName().replace(".mp3", "");
                                if(temp != "")
                                {
                                    if(k == (mediaQueueLenght - 1))
                                    {
                                        tempQueueMusicList += temp;
                                    }
                                    else
                                    {
                                        tempQueueMusicList += temp  + "\n";
                                    }
                                }
                            }   
                            out.print(tempQueueMusicList);
                        }
                        else
                        {
                            out.print("");
                        }
                        out.close();
                    }
                   catch(IOException a)
                   {} 
                    

                    int durationOfSong = (int) getMediaPlayer().getTotalDuration().toSeconds();
                    System.out.println("D= " + durationOfSong);
                    System.out.println("C= " + getMediaPlayer().getCurrentTime().toSeconds());
                    while(durationOfSong != (int) getMediaPlayer().getCurrentTime().toSeconds())                    
                    {
                        try 
                        {
                            Thread.sleep(1000);
                            System.out.println("IN THREAD PAUSE IN WHILE LOOP");
                        } 
                        catch (InterruptedException ex) 
                        {
                            Logger.getLogger(AddMusic.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("Duration Of Song = " + durationOfSong);
                        System.out.println("Current Time = " + (int) getMediaPlayer().getCurrentTime().toSeconds());
                    }    
                    try
                    {
                        getMediaPlayer().dispose();
                    }
                    catch(Exception a)
                    {
                        System.out.println("Error in Music player Dispose");
                    }
                    try 
                    {
                        Thread.sleep(1000);
                    } 
                    catch (InterruptedException ex) 
                    {
                        Logger.getLogger(AddMusic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
               }
               else
               {
                    try 
                    {
                        Thread.sleep(10000);
                        System.out.println("IN THREAD PAUSE IN WHILE LOOP");
                    } 
                    catch (InterruptedException ex) 
                    {
                        Logger.getLogger(AddMusic.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
           }
           
        }  
        
         //used so main class is run once
        public void  setfirstRunNumber(int i)
        {
            firstRunNumber = i;
        }       
        public int getfirstRunNumber()
        {
            return firstRunNumber;
        }    
        
        public List<String> getNames()
        {
            buildEveryTime();
            List<String> temp = new ArrayList<String>();
            for(int i = 0; i < folder.list().length; i++)
            {
                 temp.add(folder.list()[i].replace(".mp3", ""));
            }
            if(temp.size() > 0)
                return temp;
            else
            {
                temp.add("Music Play Is Empty");
                return temp;
            }
        }
        
         //used to know what media player is currently playing
       public void setMediaPlayer(MediaPlayer a)
       {           
           currentPlayer = a;
       }
       //used to know what media player is currently playing
       public MediaPlayer getMediaPlayer()
       {
           return currentPlayer;
       }
        
        public void buildEveryTime()
    {
        folder = new File("C:\\Users\\Tom.Cocozzello\\Desktop\\PiMusic");
        listOfNames = folder.listFiles();        
        namesLength = listOfNames.length;
        //create a list of mediaplayers for music to play
        //check if there is music in the music queue file
        Scanner reader = null;
        try
           {
               // clear mediaQueue and mediaQueueNames to repopulate song to keep queue up to date
               mediaQueue.clear();
               mediaQueueNames.clear();
               /*
               
               
               Change this file directory to your music folder.
               Make sure your folder is all .mp3 songs
               
               */
               File file = new File("C:\\Users\\Tom.Cocozzello\\Documents\\School\\Pi Documents\\AddMusicToQueue\\src\\java\\beans\\Queue.txt");
                reader = new Scanner(file);
                mediaQueueNames = new ArrayList<File>();
                //find songs in queue and add them to the mediaQueueNames which is the name of the songs
                while(reader.hasNextLine())
                {                  
                    File nextSong = findTrackReturnPath(reader.nextLine());
                    if(nextSong != null)
                    {
                        mediaQueueNames.add(nextSong);    
                        final Label status = new Label("Init");
                        mediaQueue.add(createMediaPlayer(nextSong.toString(), status));  
                    }
                }              
               reader.close();
           }
            //catch fill not found exception
           catch(FileNotFoundException a)
           {
               System.out.println("File was not found");
           }
    }
        
        
    public File findTrackReturnPath(String a)
    {
       File pathToSong = null;
       int listOfNamesLength = listOfNames.length;
       for(int i = 0; i < listOfNamesLength; i++)
       {
           if(listOfNames[i].getName().equals(a))
               return listOfNames[i];
           else if(listOfNames[i].getName().equals(a + ".mp3"))
               return listOfNames[i];
       }
       return pathToSong;
    }
    //add songs to the queue so they can be played
    public void addToQueue(String songName)
      {  
          try
          {
              Scanner scan = new Scanner(new FileInputStream("C:\\Users\\Tom.Cocozzello\\Documents\\School\\Pi Documents\\AddMusicToQueue\\src\\java\\beans\\Queue.txt"));
              int queueLength = 0;
              while(scan.hasNextLine())
              {
                  scan.next();
                  queueLength++;
              }
              scan.close();
              
              PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\Tom.Cocozzello\\Documents\\School\\Pi Documents\\AddMusicToQueue\\src\\java\\beans\\Queue.txt", true)));
              if(queueLength > 0)
              {
                  out.append("\n" + songName);
              }
              else
              {
                  out.append(songName);
              }
              out.close();     
          }
          catch (IOException e) {}
          
          
      }
    
    private static MediaPlayer createMediaPlayer(String filename, final Label status) 
        {
            URL mediaLocation = null;
            File file = new File(filename);
            if (!file.exists()) 
            {
                status.setText("File does not exist: " + filename);
            }
            try
            {
                mediaLocation = file.toURI().toURL();
            }
            catch(MalformedURLException a)
            {}  
            Media media = new Media(mediaLocation.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            return mediaPlayer;
            
        } 
    
    //return the queue so it can be displayed on the website
    public List getQueue()
    {
        List<String> QueueNames = new ArrayList<String>();
        try
        {
        Scanner scan = new Scanner(new FileInputStream("C:\\Users\\Tom.Cocozzello\\Documents\\School\\Pi Documents\\AddMusicToQueue\\src\\java\\beans\\Queue.txt"));
              while(scan.hasNextLine())
              {
                  String tempQueueName = scan.nextLine();
                  if(!"".equals(tempQueueName))
                      QueueNames.add(tempQueueName);
              }
              scan.close();
        }
        catch(FileNotFoundException a){}
        return QueueNames;
    }
}
