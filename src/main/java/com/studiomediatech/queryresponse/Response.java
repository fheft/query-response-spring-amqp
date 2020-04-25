package com.studiomediatech.queryresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.studiomediatech.queryresponse.util.Logging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;


/**
 * Represents a declared and registered response to some {@link Query query}, an active message listener.
 *
 * @param  <T>  type of the provided response elements.
 */
class Response<T> implements MessageListener, Logging {

    private static final ObjectWriter writer = new ObjectMapper().writer();

    private final String queueName;
    private final String routingKey;

    private RabbitFacade facade;

    private Supplier<Iterator<T>> elements;
    private Supplier<Integer> total;

    // Size of response batches, 0=no batches
    private int batchSize;

    // Visible to tests
    protected Response(String routingKey) {

        this.queueName = UUID.randomUUID().toString();
        this.routingKey = routingKey;
    }

    @Override
    public void onMessage(Message message) {

        try {
            log().info("|--> Consumed query: " + message.getMessageProperties().getReceivedRoutingKey());

            List<Response<T>.PublishedResponseEnvelope<T>> responses = new ArrayList<>();

            if (batchSize == 0) {
                responses.add(buildResponse());
            } else {
                responses.addAll(buildResponses());
            }

            log().debug("Prepared response(s) {}", responses);

            for (Response<T>.PublishedResponseEnvelope<T> response : responses) {
                byte[] body = writer.writeValueAsBytes(response);

                var responseMessage = MessageBuilder.withBody(body)
                        .setContentEncoding(StandardCharsets.UTF_8.name())
                        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                        .build();

                var replyToAddress = message.getMessageProperties().getReplyToAddress();
                var exchangeName = replyToAddress.getExchangeName();
                var routingKey = replyToAddress.getRoutingKey();

                this.facade.publishResponse(exchangeName, routingKey, responseMessage);
            }
        } catch (RuntimeException | JsonProcessingException e) {
            log().error("Failed to publish response message", e);
        }
    }


    private List<Response<T>.PublishedResponseEnvelope<T>> buildResponses() {

        List<Response<T>.PublishedResponseEnvelope<T>> responses = new ArrayList<>();

        int count = 0;
        Iterator<T> it = this.elements.get();

        PublishedResponseEnvelope<T> response = new PublishedResponseEnvelope<T>();

        while (it.hasNext()) {
            response.elements.add(it.next());
            count++;

            if (count == this.batchSize) {
                response.count = response.elements.size();
                response.total = this.total != null ? this.total.get() : response.elements.size();
                responses.add(response);

                response = new PublishedResponseEnvelope<T>();
                count = 0;
            }
        }

        if (count != 0) {
            response.count = response.elements.size();
            response.total = this.total != null ? this.total.get() : response.elements.size();
            responses.add(response);
        }

        return responses;
    }


    private Response<T>.PublishedResponseEnvelope<T> buildResponse() {

        var response = new PublishedResponseEnvelope<T>();

        Iterator<T> it = this.elements.get();

        while (it.hasNext()) {
            response.elements.add(it.next());
        }

        response.count = response.elements.size();
        response.total = this.total != null ? this.total.get() : response.elements.size();

        return response;
    }


    static <T> Response<T> from(ResponseBuilder<T> responses) {

        Response<T> response = new Response<>(responses.getRespondToTerm());

        response.elements = responses.elements();

        if (responses.total() != null) {
            response.total = responses.total();
        }

        response.batchSize = responses.getBatchSize();

        return response;
    }


    public void accept(RabbitFacade facade) {

        this.facade = facade;
    }


    public String getQueueName() {

        return this.queueName;
    }


    public String getRoutingKey() {

        return this.routingKey;
    }

    class PublishedResponseEnvelope<R extends T> {

        @JsonProperty
        public int count;
        @JsonProperty
        public int total;
        @JsonProperty
        public Collection<R> elements = new ArrayList<>();

        PublishedResponseEnvelope() {

            // OK
        }

        @Override
        public String toString() {

            return "PublishedResponseEnvelope [count=" + count + ", total=" + total + "]";
        }
    }
}
