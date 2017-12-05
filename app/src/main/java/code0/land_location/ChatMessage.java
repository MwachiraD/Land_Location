package code0.land_location;

/**
 * Created by sikinijs on 8/5/17.
 */
public class ChatMessage {


    public String getMessage_text() {
        return Message_text;
    }

    public void setMessage_text(String message_text) {
        Message_text = message_text;
    }

    public String getMessage_sender() {
        return Message_sender;
    }

    public void setMessage_sender(String message_sender) {
        Message_sender = message_sender;
    }

    public String getMessageReceiver() {
        return MessageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        MessageReceiver = messageReceiver;
    }

    public String getChatType() {
        return ChatType;
    }

    public void setChatType(String chatType) {
        ChatType = chatType;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }



    public String getCurrent_user() {
        return current_user;
    }

    public void setCurrent_user(String current_user) {
        this.current_user = current_user;
    }

    private String current_user;

    public ChatMessage(String current_user,String messageReceiver, String chatType, long time, String message_text, String message_sender, String land_id)
    {
        MessageReceiver = messageReceiver;
        ChatType = chatType;
        Time = time;
        this.land_id = land_id;
        Message_text = message_text;
        Message_sender = message_sender;
        this.current_user = current_user;
    }

    private String Message_text;

    public String getLand_id() {
        return land_id;
    }

    public void setLand_id(String land_id) {
        this.land_id = land_id;
    }


    private String land_id;
    private String Message_sender;
    private String MessageReceiver;
    private String ChatType;
    private long Time;


    public ChatMessage (){ }







}