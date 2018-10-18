package com.alevel.deliverit.logistics.postal.network;

import com.alevel.deliverit.customers.Parcel;
import com.alevel.deliverit.entity.Entity;
import com.alevel.deliverit.logistics.clock.generator.ClockSignal;
import com.alevel.deliverit.logistics.fsm.State;
import com.alevel.deliverit.logistics.fsm.StateMachine;
import com.google.common.collect.ImmutableSet;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A post office.
 *
 * @author Sergey Bogovesov
 * @author Vadym Mitin
 */
public class PostOffice extends Entity<PostOfficeId> {
    private final String postalCode;
    private Parcel currentParcel;
    private Route currentRoute;
    private Set<Connection> inputs = new HashSet<>();
    private Set<Connection> outputs = new HashSet<>();
    private final Queue<Parcel> sendingTray = new PriorityQueue<>();
    private final Queue<Parcel> receivingTray = new PriorityQueue<>();
    private StateMachine stateMachine;

    private PostOffice(PostOfficeId id, String postalCode) {
        super(id);
        this.postalCode = postalCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void receiveParcel(Parcel parcel) {
        receivingTray.add(parcel);
    }

    public void sendingParcel(Parcel parcel) {
        sendingTray.add(parcel);
    }

    public void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public void addInputConnection(Connection connection) {
        inputs.add(connection);
    }

    public void addOutputConnection(Connection connection) {
        outputs.add(connection);
    }

    public String getPostalCode() {
        return postalCode;
    }

    public ImmutableSet<Connection> getInputs() {
        return ImmutableSet.copyOf(inputs);
    }

    public ImmutableSet<Connection> getOutputs() {
        return ImmutableSet.copyOf(outputs);
    }

    private String getConnectionsAsString(Set<Connection> connections) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Connection connection : connections) {
            stringBuilder.append(connection).append(" ");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostOffice)) return false;
        PostOffice that = (PostOffice) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getPostalCode(), that.getPostalCode());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getPostalCode());
    }

    @Override
    public String toString() {
        return "PostOffice{" +
                /*"id='" + id + '\'' +*/
                "  postalCode='" + postalCode + '\'' +
                ", inputs=" + getConnectionsAsString(inputs) +
                ", outputs=" + getConnectionsAsString(outputs) +
                '}';
    }

    public void activate(ClockSignal signal) {

    }

    public static class Builder {
        private PostOfficeId id;
        private String name;
        private Set<State> states;

        public Builder setId(PostOfficeId id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setStates(Set<State> states) {
            this.states = states;
            return this;
        }

        public PostOffice build() {
            checkNotNull(id);
            checkNotNull(name);

            return new PostOffice(id, name);
        }
    }
}
