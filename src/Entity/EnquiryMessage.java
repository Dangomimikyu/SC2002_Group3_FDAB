package Entity;

import Users.*;

public class EnquiryMessage {
    public String message;
    public final System_User sender;

    EnquiryMessage(String m, System_User s) {
        this.message = m;
        this.sender = s;
    }
}
