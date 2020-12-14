/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component.api;

public class Event {

    private String name;

    private int count;

    public Event() {
    }

    public Event(String name, int count) {
        this.name = name;
        this.count = count;
    }
}
