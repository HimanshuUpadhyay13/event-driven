package com.himanshu.machine;

import com.himanshu.machine.component.Machine;
import com.himanshu.machine.model.Event;
import com.himanshu.machine.model.State;
import com.himanshu.machine.model.Subscriber;
import com.himanshu.machine.model.enums.AllStates;
import com.himanshu.machine.model.enums.Events;
import com.himanshu.machine.model.enums.StateType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class MachineApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MachineApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        State initState = new State(StateType.INIT.name(), AllStates.INIT.name());
        State createdState = new State(AllStates.CREATED.name(), StateType.INTERMEDIATE.name());
        State activateState = new State(AllStates.ACTIVATED.name(), StateType.INTERMEDIATE.name());
        State inprogressState = new State(AllStates.INPROGRESS.name(), StateType.INTERMEDIATE.name());
        State cancelledState = new State(AllStates.CANCELLED.name(), StateType.FINAL.name());
        State completedState = new State(AllStates.COMPLETED.name(), StateType.FINAL.name());

        Event fullFillOrderEvent = new Event(Events.FULLFILLORDER.name());
        Event activateEvent = new Event(Events.ACTIVATE.name());
        Event cancelEvent = new Event(Events.CANCEL.name());
        Event makeProgressEvent = new Event(Events.MAKEPROGRESS.name());
        Event completeEvent = new Event(Events.COMPLETE.name());

        List<State> acceptableStates = Arrays.asList(initState, createdState, activateState, inprogressState, cancelledState, completedState, activateState);

        List<Event> acceptableEvents = Arrays.asList(fullFillOrderEvent, activateEvent, cancelEvent, makeProgressEvent, completeEvent);

        Map<Event, List<Pair<State, State>>> eventMap = new HashMap<>();
        eventMap.put(fullFillOrderEvent, Arrays.asList(new ImmutablePair<>(initState, createdState)));
        eventMap.put(activateEvent, Arrays.asList(new ImmutablePair<>(createdState, activateState)));
        eventMap.put(makeProgressEvent, Arrays.asList(new ImmutablePair<>(activateState, inprogressState)));
        eventMap.put(completeEvent, Arrays.asList(new ImmutablePair<>(inprogressState, completedState)));
        eventMap.put(cancelEvent, Arrays.asList(new ImmutablePair<>(inprogressState, cancelledState),
                new ImmutablePair<>(activateState, cancelledState),
                new ImmutablePair<>(createdState, cancelledState)));

        Map<AllStates, List<Subscriber>> globalSubsribers = new HashMap<>();

        globalSubsribers.put(AllStates.ALL, Arrays.asList(new Subscriber(UUID.randomUUID().toString()),
                new Subscriber(UUID.randomUUID().toString())));
        globalSubsribers.put(AllStates.CREATED, Arrays.asList(new Subscriber(UUID.randomUUID().toString()),
                new Subscriber(UUID.randomUUID().toString())));
        globalSubsribers.put(AllStates.INPROGRESS, Arrays.asList(new Subscriber(UUID.randomUUID().toString()),
                new Subscriber(UUID.randomUUID().toString())));


        Machine machine = new Machine(
                initState,
                acceptableStates,
                acceptableEvents,
                eventMap,
                globalSubsribers
        );
        Machine machine1 = new Machine(
                initState,
                acceptableStates,
                acceptableEvents,
                eventMap,
                globalSubsribers
        );

        machine.changeState(fullFillOrderEvent);
        machine.changeState(activateEvent);
        //machine.changeState(cancelEvent);
        //machine.changeState(makeProgressEvent);
        machine1.changeState(fullFillOrderEvent);


    }
}






