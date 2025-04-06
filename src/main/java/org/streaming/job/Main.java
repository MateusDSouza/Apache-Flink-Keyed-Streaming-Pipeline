package org.streaming.job;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * Here it is the main part of our application. There is the boilerplate of a flink application, the source of the
 * FlashSaleEvent that are generated in our mocked streaming of data, a keyed stream used in the group by clientId
 * and the result of the aggregation, by summing the total spend and the total units bought.
 * For the sync we print the result to the console, but ideally this needs to be stored in a Datalake, Iceberg or other
 * Analytics landzone.
 *
 */
public class Main {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<FlashSaleEvent> FlashSaleEventStream = env.addSource(new FlashSaleEventSource());

        SingleOutputStreamOperator<FlashSaleEvent> aggregatedEvents = FlashSaleEventStream
                .keyBy(FlashSaleEvent::getClientId)
                .reduce((ReduceFunction<FlashSaleEvent>) (value1, value2) -> {
                    double newTotalPrice = value1.getTotalPrice() + value2.getTotalPrice();
                    int newUnits = value1.getUnits() + value2.getUnits();

                    return new FlashSaleEvent(
                            value1.getEventId(), value1.getEventTimestamp(), value1.getApiTimestamp(),
                            value1.getOperationId(), value1.getOrderNumber(), value1.isCancelled(),
                            value1.getClientId(), newUnits, newTotalPrice, value1.getTotalVatAmount(),
                            value1.getFlashSaleId(), value1.getFlashSaleGoalId()
                    );
                });

        DataStream<AggregatedFlashSaleEvent> finalResults = aggregatedEvents
                .map(event -> new AggregatedFlashSaleEvent(
                        event.getClientId(),
                        event.getTotalPrice(),
                        event.getUnits()
                ));

        finalResults.print();

        env.execute("FlashSaleEvents - Total Bought per client every hour");
    }
}
