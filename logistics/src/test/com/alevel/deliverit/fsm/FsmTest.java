package com.alevel.deliverit.fsm;

import com.alevel.deliverit.logistics.fsm.FiniteStateMachine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.alevel.deliverit.fsm.State.START;

/**
 * Unit test for finite state machine.
 * <p>
 * Each post office have to  process, check for address, sorting, notify and sending.
 * Some office can make radiation control, check for legality and check for dangerous.
 * Last state is sending for all office.
 */
@DisplayName("Finite state machine should:")
public class FsmTest {
    @Test
    @DisplayName("Switch states and perform commands")
    void testSwitchState() {
        LogisticCommandFactory logisticCommandFactory = new LogisticCommandFactory();
        Map routeMap = RouteMap.routeMap;
        LogisticContext context = new LogisticContext();
        FiniteStateMachine<State, LogisticContext> finiteStateMachine
                = new FiniteStateMachine<State, LogisticContext>(logisticCommandFactory, routeMap, START);
        finiteStateMachine.start(context);
    }
}
