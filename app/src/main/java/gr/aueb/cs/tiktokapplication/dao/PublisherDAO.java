package gr.aueb.cs.tiktokapplication.dao;

import gr.aueb.cs.tiktokapplication.appnode.Publisher;

public class PublisherDAO {

    private static Publisher publisher;

    public PublisherDAO(Publisher p) {
        publisher = p;
    }

    public static Publisher getPublisher() {
        return publisher;
    }

}
