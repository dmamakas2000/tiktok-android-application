package gr.aueb.cs.tiktokapplication.dao;

import gr.aueb.cs.tiktokapplication.appnode.Consumer;

public class ConsumerDAO {

    private static Consumer consumer;

    public ConsumerDAO(Consumer c) {
        consumer = c;
    }

    public static Consumer getConsumer() {
        return consumer;
    }

}
