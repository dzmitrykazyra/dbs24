/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class EmailEntry {
//    public List<Message> getMessages() {
//        return messages;
//    }

    @JsonProperty("Messages")
    private List<Message> Messages;

    @JsonProperty("Error")
    private String error;

}
