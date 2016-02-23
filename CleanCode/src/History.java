import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;


public class History {
    private LinkedList<Message> messageList;
    public History(){
        messageList=new LinkedList<>();
    }
    public void loadFromFile(JsonArray array){
        messageList.clear();
        for (int i = 0; i < array.size(); i++) {
            JsonObject tmpObj = array.getJsonObject(i);
            Message tmp = new Message(tmpObj);
            messageList.add(tmp);
        }
    }
    private JsonObject toJson(Message message) {
        return Json.createObjectBuilder().add("timestamp",message.getTimestamp().getTime())
                .add("id",message.getId())
                .add("message",message.getMessage())
                .add("author",message.getAuthor())
                .build();
    }
    public JsonArray arrayForWriting(){
        JsonArrayBuilder writeArray = Json.createArrayBuilder();
        for(Message message: messageList)
        {
            JsonObject tmp = toJson(message);
            writeArray.add(tmp);
        }
        JsonArray result = writeArray.build();
        return result;
    }
    public void addMessage(Message message){
        messageList.add(message);
    }
    public boolean deleteById(String id){
        int result=-1;
        for(int i=0;i<messageList.size();i++)
        {
            if(messageList.get(i).getId().equals(id)){
                result=i;
                break;
            }
        }
        if(result!=-1){
            messageList.remove(result);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isHistoryEmpty(){
        return messageList.isEmpty();
    }
    public ArrayList<Message> searchByAutor(String key){
        ArrayList<Message> result=new ArrayList<>();
        for (Message message:messageList) {
            if(message.getAuthor().equals(key)){
                result.add(message);
            }
        }
        return result;
    }
    public ArrayList<Message> searchByKeyword(String key){
        ArrayList<Message> result=new ArrayList<>();
        for(int i=0;i<messageList.size();i++)
        {
            if(messageList.get(i).getMessage().contains(key)){
                result.add(messageList.get(i));
            }
        }
        return result;
    }
    public ArrayList<Message> searchByTimeperiod(Date dateBegin,Date dateEnd){
        ArrayList<Message>result=new ArrayList<>();
        for(Message message: messageList){
            if((message.getTimestamp().compareTo(dateBegin)>=0)&&(message.getTimestamp().compareTo(dateEnd)<=0)){
                result.add(message);
            }
        }
        return result;
    }
}
