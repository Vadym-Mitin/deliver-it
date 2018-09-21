package com.alevel.deliverit;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vadym Mitin
 */
@DisplayName("Module Api should")
public class ModuleAPITest {

    public static final String METHOD_ASSERTION_RETURN = "Method return TEST";

    @Test
    @DisplayName("find subscribed methods without parameters")
    void checkMethodContainer() throws InvocationTargetException,
            IllegalAccessException, InstantiationException {

        ModuleAPIGiven.TestClass1 testClass1 = new ModuleAPIGiven.TestClass1();
        ModuleAPI api = ModuleAPI.getInstance();

        api.registerConsumers(testClass1);

        Map<String, ServiceMethod> methodsContainer = api.getConsumersContainer();

        for (Map.Entry<String, ServiceMethod> e : methodsContainer.entrySet()) {
            ServiceMethod value = e.getValue();
            String address = e.getKey();
            Optional actual = value.invokeConsumer();
            if (address.equals("value 1")) {
                assertEquals(METHOD_ASSERTION_RETURN, actual.get());
            }
            if (address.equals("value 2")) {
                assertEquals(10, actual.get());
            }
            if (address.equals("value 3")) {
                assertNull(actual.get());
            }
            if (address.equals("value 4")) {
                actual.get();
            }
        }
    }

    @Test
    @DisplayName("find subscribed methods with parameters")
    void checkFunctionContainer() throws InvocationTargetException,
            IllegalAccessException {

        ModuleAPIGiven.TestClass2 testClass2 = new ModuleAPIGiven.TestClass2();
        ModuleAPI api = ModuleAPI.getInstance();

        api.registerConsumers(testClass2);

        Map<String, ServiceMethod> methodsContainer = api.getConsumersContainer();

        for (Map.Entry<String, ServiceMethod> e : methodsContainer.entrySet()) {
            String address = e.getKey();
            ServiceMethod value = e.getValue();

            if (address.equals("Str")) {
                Optional optional = value.invokeConsumer(METHOD_ASSERTION_RETURN);
                assertEquals(METHOD_ASSERTION_RETURN, optional.get());
                System.out.println(optional.get());
            }
            if (address.equals("Int")) {
                int i = 10;
                Optional optional = value.invokeConsumer(10);
                assertEquals(i, optional.get());
                System.out.println(optional.get());
            }
            if (address.equals("Bool")) {
                boolean b = true;
                Optional optional = value.invokeConsumer(b);
                if (optional.isPresent()) {
                    assertTrue((Boolean) optional.get());
                    System.out.println(optional.get());
                } else System.out.println(" Something wrong " + optional);
            }
        }
    }

    @Test
    @DisplayName("run vertx witch ServiceMethod")
    void checkVertx() {

        Vertx vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();
        ModuleAPIGiven.TestClass4 testClass4 = new ModuleAPIGiven.TestClass4();
        ModuleAPI api = ModuleAPI.getInstance();

        api.registerConsumers(testClass4);

        Map<String, ServiceMethod> methodsStorage = api.getConsumersContainer();

        for (Map.Entry<String, ServiceMethod> e : methodsStorage.entrySet()) {
            String address = e.getKey();
            ServiceMethod value = e.getValue();
            eb.consumer(address, message -> {
                try {
                    value.invokeConsumer(message.body());
                } catch (InvocationTargetException | IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            });
        }

        eb.send("address.1", "Hello address.1");
        eb.send("address.2", new Double(10));
        eb.send("address.1", null);
        eb.send("address.2", null);
    }

    @Test
    @DisplayName("run vertx witch function")
    void checkVertx2() {

        Vertx vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();
        ModuleAPIGiven.TestClass5 testClass5 = new ModuleAPIGiven.TestClass5();
        ModuleAPI api = ModuleAPI.getInstance();

        api.registerConsumers(testClass5);

        Map<String, ServiceMethod> methodsContainer = api.getConsumersContainer();

        for (Map.Entry<String, ServiceMethod> e : methodsContainer.entrySet()) {
            String address = e.getKey();
            ServiceMethod value = e.getValue();
            eb.consumer(address, message -> {
                try {
                    value.invokeConsumer(message.body());
                } catch (InvocationTargetException | IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            });
        }

        eb.send("address.3", "Hello address.3");
        eb.send("address.4", new Double(10));
        eb.send("address.3", null);
        eb.send("address.4", null);
    }
}
