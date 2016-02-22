import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatWorking {
    private LinkedList <Message> messageList;
    private FileWriter writer;
    public ChatWorking() throws IOException
    {
        writer=new FileWriter("log.txt");
        writer.write("Program start"+"\n");
        messageList = new LinkedList<>();
        Scanner in=new Scanner(System.in);

        String choice=null;
        showVariants();
        choice=in.next();
        while (!choice.equals("8"))
        {
            switch (choice) {
                case "1":
                    loadHistory();
                    break;
                case "2":
                    saveHistory();
                    break;
                case "3":
                    addMessage();
                    break;
                case "4":
                    deleteById();
                    break;
                case "5":
                    searchByAuthor();
                    break;
                case "6":
                    searchByKeyword();
                    break;
                case "7":
                    searchByTimePeriod();
                    break;
                case "8":
                    break;
                default:
                    System.out.println("Invalid input");
            }
            showVariants();
            choice=in.next();
        }
        System.out.println("End working");
        writer.write("End working");
        writer.close();
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
    private void loadHistory() throws IOException {
        messageList.clear();
        writer.write("Start loading history"+"\n");
        try {
            String JSONData = Files.readAllLines(Paths.get("history.json")).toString();
            JsonReader reader = Json.createReader(new StringReader(JSONData));
            JsonArray jsonValues = reader.readArray();
            if (jsonValues.size() == 0) {
                System.out.println("Your history is empty");
                return;
            }
            JsonArray array = jsonValues.getJsonArray(0);
            reader.close();
            for (int i = 0; i < array.size(); i++) {
                JsonObject tmpObj = array.getJsonObject(i);
                Message tmp = new Message(new Timestamp(tmpObj.getJsonNumber("timestamp").longValue()), tmpObj.getString("id"), tmpObj.getString("message"),
                        tmpObj.getString("author"));
                messageList.add(tmp);
            }
            System.out.println("Loading complete");
            writer.write("Loading complete"+"\n");
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e.getMessage());
            writer.write("File not found " + e.getMessage()+"\n");
        }
        writer.flush();
    }
    private void saveHistory() throws IOException
    {
        if(!messageList.isEmpty())
        {
            System.out.println("Start saving");
            writer.write("Start saving"+"\n");
            FileWriter out = new FileWriter("history.json");
            JsonWriter writer = Json.createWriter(out);
            JsonArrayBuilder writeArray = Json.createArrayBuilder();
            for(Message message: messageList)
            {
                JsonObject tmp=Json.createObjectBuilder().add("timestamp",message.getTimestamp().getTime())
                        .add("id",message.getId())
                        .add("message",message.getMessage())
                        .add("author",message.getAuthor())
                        .build();
                writeArray.add(tmp);
            }
            JsonArray array = writeArray.build();
            writer.writeArray(array);
            out.close();
            writer.close();
            System.out.println("Saving complete");
            this.writer.write("Saving complete"+"\n");
        }
        else{
            System.out.println("History is empty");
            writer.write("History is empty"+"\n");
        }
        writer.flush();
    }
    private void addMessage() throws  IOException{
        Scanner in=new Scanner(System.in);

        System.out.println("Enter author");
        String author=in.next();

        System.out.println("Enter message");
        String message=in.next();
        Random r=new Random();
        Timestamp currentTime=new Timestamp(System.currentTimeMillis());
        Message tmp=new Message(currentTime,r.nextInt()+"e",message,author);
        messageList.add(tmp);
        writer.write("Message added to history"+"\n");
        writer.flush();
    }
    private void deleteById() throws IOException
    {
        writer.write("Trying to delete message by id");
        if(messageList.isEmpty()){
            System.out.println("History is empty");
            writer.write("Deleting impossible - history is empty");
            return;
        }
        System.out.println("Enter id for deleting");
        Scanner in=new Scanner(System.in);
        String key=in.next();
        int result=-1;
        for(int i=0;i<messageList.size();i++)
        {
            if(messageList.get(i).getId().equals(key)){
                result=i;
                break;
            }
        }
        if(result!=-1){
            messageList.remove(result);
            System.out.println("Deleted sucsesfully"+"\n");
            writer.write("Deleted sucsesfully"+"\n");
        }
        else{
            System.out.println("Messages is not found");
            writer.write("Messages is not found"+"\n");
        }
        writer.flush();
    }
    private void searchByAuthor() throws IOException{
        writer.write("Start searching by author"+"\n");
        if(messageList.isEmpty()){
            System.out.println("History is empty");
            writer.write("Searcing complete. History is empty"+"\n");
            return;
        }
        System.out.println("Enter author for searching");
        Scanner in=new Scanner(System.in);
        String key=in.next();
        ArrayList<Message> result=new ArrayList<>();
        for (Message message:messageList) {
            if(message.getAuthor().equals(key)){
                result.add(message);
            }
        }
        if(result.isEmpty()){
            System.out.println("No message from this author find");
            writer.write("Searcing complete.No message from this author find\n");
        }
        else
        {
            System.out.println(result.size()+" messages find:");
            writer.write("Searching complete: "+result.size()+" messages found");
            for(Message message:result)
            {
                System.out.println(message);
            }

        }
        writer.flush();
    }
    private void searchByKeyword()throws  IOException{
        writer.write("Searching messages by keyword\n");
        if(messageList.isEmpty()){
            System.out.println("History is empty");
            writer.write("Searching complete. History is empty "+"\n");
            return;
        }
        System.out.println("Enter keyword for searching");
        Scanner in=new Scanner(System.in);
        String key=in.next();
        ArrayList<Message> result=new ArrayList<>();
        for(int i=0;i<messageList.size();i++)
        {
            if(messageList.get(i).getMessage().contains(key)){
                result.add(messageList.get(i));
            }
        }
        if(result.isEmpty()){
            System.out.println("No message with this keyword found");
            writer.write("Searching complete. No message with this keyword found"+"\n");
        }
        else
        {
            System.out.println(result.size()+" messages found:");
            writer.write("Searching complete. "+result.size()+" messages found:"+"\n");
            for(Message message:result)
            {
                System.out.println(message);
            }
        }
    }
    private void searchByTimePeriod()throws IOException{
        ;
        if(messageList.isEmpty()){
            System.out.println("History is empty");
        }
        else{
            System.out.println("Enter the beginning of searching period in format: yyyy-MM-dd HH:mm:ss");
            BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
            String beginingDate=input.readLine();
            System.out.println("Enter the end of searching period format: yyyy-MM-dd HH:mm:ss");
            String endDate=input.readLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date dateBegin=dateFormat.parse(beginingDate);
                Date dateEnd=dateFormat.parse(endDate);
                writer.write("Start searching in timeperiod from "+dateBegin.toString()+"to "+dateEnd.toString()+"\n");
                ArrayList<Message>result=new ArrayList<>();
                for(Message message: messageList){
                    if((message.getTimestamp().compareTo(dateBegin)>=0)&&(message.getTimestamp().compareTo(dateEnd)<=0)){
                        result.add(message);
                    }
                }
                if(result.size()==0) {
                    System.out.println("No messages found");
                    writer.write("Searcing complete. No messages in this time period found"+"\n");
                }
                else{
                    System.out.println(result.size()+" message(s) found:"+"\n");
                    writer.write("Searcing complete "+result.size()+" message(s) found:"+"\n");
                    for(Message message:result){
                        System.out.println(message);
                    }
                }
            }catch (ParseException e){
                System.out.println("Invalid date input");
                writer.write("ParseException "+e.getMessage()+"\n");
            }
        }
        writer.flush();
    }

}
