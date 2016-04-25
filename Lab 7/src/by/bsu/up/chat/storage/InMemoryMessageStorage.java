package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;

import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public void addMessage(Message message) {
        try{
            saveMessages();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        messages.add(message);

    }

    @Override
    public boolean updateMessage(Message message) {
        //throw new UnsupportedOperationException("Update for messages is not supported yet");
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(message.getId())) {
                messages.get(i).setText(message.getText()+"was edited");
                messages.get(i).setIsEdit("was edited");
                try {
                    saveMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

        }
        return  false;
    }
    @Override
    public synchronized boolean removeMessage(String messageId) {
        //throw new UnsupportedOperationException("Removing of messages is not supported yet");
        for (int i = 0; i<messages.size(); i++){
            if (messages.get(i).getId().equals(messageId)){
                //messages.remove(i);
                messages.get(i).setText("message was deleted");
                try {
                    saveMessages();
                }catch (IOException e){
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return messages.size();
    }

    public InMemoryMessageStorage(){
        try{
            loadHistoryFromFile("history.json");
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }


    }

    private JsonObject toJson(Message message) {
        return Json.createObjectBuilder().add("timestamp",message.getTimestamp())
                .add("id",message.getId())
                .add("message",message.getText())
                .add("author",message.getAuthor())
                .add("isEdit",message.getIsEdit())
                .build();
    }
    public void saveMessages() throws  IOException{
        if (!messages.isEmpty()) {
            FileWriter out = new FileWriter("history.json");
            JsonWriter writeHistory = Json.createWriter(out);
            JsonArrayBuilder jsonHistory = Json.createArrayBuilder();
            for (Message i : messages) {
                jsonHistory.add(toJson(i));
            }
            JsonArray arr = jsonHistory.build();
            writeHistory.writeArray(arr);
            out.close();
            writeHistory.close();
        }
    }
    public void loadHistoryFromFile(String path) throws IOException {
        String JSONData = Files.readAllLines(Paths.get(path)).toString();
        JsonReader readerTmp = Json.createReader(new StringReader(JSONData));
        JsonArray jsonValues = readerTmp.readArray();
        JsonArray array = jsonValues.getJsonArray(0);
        readerTmp.close();

        for (int i = 0; i < array.size(); i++) {
            JsonObject tmpObj = array.getJsonObject(i);
            Message tmp = new Message(tmpObj);
            //System.out.println(tmp);
            messages.add(tmp);
        }

    }
}


