package com.himanshu.machine.component;

import com.himanshu.machine.exceptions.InvalidEventException;
import com.himanshu.machine.model.Event;
import com.himanshu.machine.model.State;
import com.himanshu.machine.model.Subscriber;
import com.himanshu.machine.model.enums.AllStates;
import com.himanshu.machine.model.enums.StateType;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode
public class Machine {

    private State currentState;
    Map<Event, List<Pair<State, State>>> eventMap;
    private final List<State> acceptableStates;
    private final List<Event> acceptableEvents;
    Map<AllStates, List<Subscriber>> globalSubsribers;

    public Machine(State currentState, List<State> acceptableStates, List<Event> acceptableEvents, Map<Event, List<Pair<State, State>>> eventMap, Map<AllStates, List<Subscriber>> globalSubsribers) {
        this.currentState = currentState;
        this.eventMap = eventMap;
        this.acceptableStates = acceptableStates;
        this.acceptableEvents = acceptableEvents;
        this.globalSubsribers = globalSubsribers;
        System.out.println("Machine Initialized");
    }

    //This method would be triggered in case producer produces any event
    public void changeState(Event event) throws InvalidEventException {
        String previousState = currentState.getName();
        if (!acceptableEvents.contains(event) || eventMap == null || !eventMap.containsKey(event) || currentState.getType().equalsIgnoreCase(StateType.FINAL.name())) {
            System.out.println("Invalid Event");
            throw new InvalidEventException("Invalid Event, please check... or current state is already final");
        }
        List<Pair<State, State>> states = eventMap.get(event);
        states.forEach(stateStatePair -> {
            if (stateStatePair.getLeft().getName().equals(currentState.getName())) {
                currentState = stateStatePair.getRight();

            }
        });
        if (previousState.equalsIgnoreCase(currentState.getName())) {
            System.out.println("Error while changing the current state....");
        } else {
            publishTheChange(currentState, this.globalSubsribers);
            System.out.println("State changed from " + previousState + " to " + currentState.getName());

        }
    }

    private void publishTheChange(State currentState, Map<AllStates, List<Subscriber>> globalSubscribers) {
        globalSubscribers.forEach((K, V) -> {
            if (K.name().equalsIgnoreCase(AllStates.ALL.name()) || K.name().equalsIgnoreCase(currentState.getName())) {
                V.forEach(s -> {
                    s.message(currentState.getName());
                });
            }

        });
    }

    public void addState(State state) {
        acceptableStates.add(state);
    }

    public void addEvent(Event state) {
        acceptableEvents.add(state);
    }

    public void addSubscription(AllStates state, List<Subscriber> subscribers) {
        if (globalSubsribers.containsKey(state))
            globalSubsribers.get(state).addAll(subscribers);
        else
            globalSubsribers.put(state, subscribers);

    }

}
