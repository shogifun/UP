import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatWorking {
    private FileWriter log;
    private History history=new History();
    public ChatWorking()
    {
        try
        {
            log = new FileWriter("log.txt");
            log.write("Program start" + "\n");

        Scanner in=new Scanner(System.in);
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        String choice=null;
        showVariants();
        choice=in.next();
        try {


            while (!choice.equals("8")) {
                switch (choice) {
                    case "1":
                        loadHistory("history.json");
                        break;
                    case "2":
                        saveHistory();
                        break;
                    case "3":
                        addMessage(reader);
                        break;
                    case "4":
                        deleteById(reader);
                        break;
                    case "5":
                        searchByAuthor(reader);
                        break;
                    case "6":
                        searchByKeyword(reader);
                        break;
                    case "7":
                        searchByTimePeriod(reader);
                        break;
                    case "8":
                        break;
                    default:
                        System.out.println("Invalid input");
                }
                showVariants();
                choice = in.next();
            }
        }catch (IOException e)
        {
            log.write("IOException "+e.getMessage());
        }
        in.close();
        System.out.println("End working");
        log.write("End working");
        reader.close();
        log.close();
        }catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    private void showVariants()
    {
        System.out.println("Choose,what you want to do.");
        System.out.println("1.Load history from file");
        System.out.println("2.Save history to file");
        System.out.println("3.Add new message");
        System.out.println("4.Deleting message by id");
        System.out.println("5.Searching messages by author");
        System.out.println("6.Searching messages by keyword");
        System.out.println("7.Searching messages in timeperiod");
        System.out.println("8.End working");
    }
    private void loadHistory(String path) throws IOException {
        log.write("Start loading history"+"\n");
        try {
            String JSONData = Files.readAllLines(Paths.get(path)).toString();
            JsonReader readerTmp = Json.createReader(new StringReader(JSONData));
            JsonArray jsonValues = readerTmp.readArray();
            if (jsonValues.size() == 0) {
                System.out.println("Your history is empty");
                return;
            }
            JsonArray array = jsonValues.getJsonArray(0);
            readerTmp.close();
            history.loadFromFile(array);
            System.out.println("Loading complete");
            log.write("Loading complete"+"\n");
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e.getMessage());
            log.write("File not found " + e.getMessage()+"\n");
        }
        log.flush();
    }
    private void saveHistory() throws IOException
    {
        if(!history.isHistoryEmpty()){
            System.out.println("Start saving");
            log.write("Start saving"+"\n");
            FileWriter out = new FileWriter("history.json");
            JsonWriter writer = Json.createWriter(out);
            writer.writeArray( history.arrayForWriting());
            out.close();
            writer.close();
            System.out.println("Saving complete");
            this.log.write("Saving complete"+"\n");
        }else{
            System.out.println("History is empty");
            log.write("History is empty"+"\n");
        }
        log.flush();
    }


    private void addMessage(BufferedReader reader) throws  IOException{
        System.out.println("Enter author");
        String author=reader.readLine();

        System.out.println("Enter message");
        String message=reader.readLine();
        Random r=new Random();
        Timestamp currentTime=new Timestamp(System.currentTimeMillis());
        Message tmp=new Message(currentTime,r.nextInt()+"e",message,author);
        history.addMessage(tmp);
        log.write("Message added to history"+"\n");
        log.flush();
    }
    private void deleteById(BufferedReader reader) throws IOException
    {
        log.write("Trying to delete message by id");
        if(history.isHistoryEmpty()){
            System.out.println("History is empty");
            log.write("Deleting impossible - history is empty");
            return;
        }
        System.out.println("Enter id for deleting");
        String key=reader.readLine();
        boolean result=history.deleteById(key);
        if(result){
            System.out.println("Deleted sucsesfully"+"\n");
            log.write("Deleted sucsesfully"+"\n");
        }
        else{
            System.out.println("Messages is not found");
            log.write("Messages is not found"+"\n");
        }
        log.flush();
    }
    private void searchByAuthor(BufferedReader reader) throws IOException{
        log.write("Start searching by author"+"\n");
        if(history.isHistoryEmpty()){
            System.out.println("History is empty");
            log.write("Searcing complete. History is empty"+"\n");
        }
        else {
            System.out.println("Enter author for searching");
            String key = reader.readLine();
            ArrayList<Message> result = history.searchByAutor(key);
            if (result.isEmpty()) {
                System.out.println("No message from this author find");
                log.write("Searcing complete.No message from this author find\n");
            } else {
                System.out.println(result.size() + " messages find:");
                log.write("Searching complete: " + result.size() + " messages found");
                for (Message message : result) {
                    System.out.println(message);
                }

            }
        }
        log.flush();
    }
    private void searchByKeyword(BufferedReader reader)throws  IOException{
        log.write("Searching messages by keyword\n");
        if(history.isHistoryEmpty()){
            System.out.println("History is empty");
            log.write("Searching complete. History is empty "+"\n");
            return;
        }
        System.out.println("Enter keyword for searching");
        String key=reader.readLine();
        ArrayList<Message> result=history.searchByKeyword(key);


        if(result.isEmpty()){
            System.out.println("No message with this keyword found");
            log.write("Searching complete. No message with this keyword found"+"\n");
        }
        else
        {
            System.out.println(result.size()+" messages found:");
            log.write("Searching complete. "+result.size()+" messages found:"+"\n");
            for(Message message:result)
            {
                System.out.println(message);
            }
        }
        log.flush();


    }
    private void searchByTimePeriod(BufferedReader reader)throws IOException{
        ;
        if(history.isHistoryEmpty()){
            System.out.println("History is empty");
        }
        else{
            System.out.println("Enter the beginning of searching period in format: yyyy-MM-dd HH:mm:ss");
            String beginingDate=reader.readLine();
            System.out.println("Enter the end of searching period format: yyyy-MM-dd HH:mm:ss");
            String endDate=reader.readLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date dateBegin=dateFormat.parse(beginingDate);
                Date dateEnd=dateFormat.parse(endDate);
                log.write("Start searching in timeperiod from "+dateBegin.toString()+"to "+dateEnd.toString()+"\n");
                ArrayList<Message>result=history.searchByTimeperiod(dateBegin,dateEnd);
                if(result.size()==0) {
                    System.out.println("No messages found");
                    log.write("Searcing complete. No messages in this time period found"+"\n");
                }
                else{
                    System.out.println(result.size()+" message(s) found:"+"\n");
                    log.write("Searcing complete "+result.size()+" message(s) found:"+"\n");
                    for(Message message:result){
                        System.out.println(message);
                    }
                }
            }catch (ParseException e){
                System.out.println("Invalid date input");
                log.write("ParseException "+e.getMessage()+"\n");
            }
        }
        log.flush();
    }

}
