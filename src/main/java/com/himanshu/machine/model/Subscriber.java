package com.himanshu.machine.model;

import lombok.Data;

@Data
public class Subscriber {

    private String subscriberId;

    public Subscriber(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public void message(String type) {
        System.out.println("Hello ," + subscriberId + " there is a change in state for your subsribed event, new State : " + type);
    }
}
