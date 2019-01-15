package project250.cse250.fhb369.ashepashe;

public class AdminItem {

    String Name, Message, Key;

    public AdminItem(String name, String message, String key) {
        Name = name;
        Message = message;
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
