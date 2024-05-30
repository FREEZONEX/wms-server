package com.supos.app.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonParseException;
import com.supos.app.domain.entity.WmsPrediction;
import com.supos.app.domain.entity.WmsStorageLocation;
import com.supos.app.mapper.WmsPredictionMapper;
import com.supos.app.service.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class MqttServiceImpl extends MqttService implements MqttCallbackExtended {

    @Value("${mqtt.broker}")
    private String mqttBroker;

    @Value("${mqtt.clientId}")
    private String mqttClientId;
    @Value("${mqtt.topic_full_request}")
    private String mqttTopicFullRequest;

    @Value("${mqtt.topic_full_response}")
    private String mqttTopicFullResponse;

    @Value("${mqtt.topic_increment}")
    private String mqttTopicIncrement;

    @Value("${mqtt.topic_increment_full}")
    private String mqttTopicIncrementFull;

    @Value("${mqtt.enable}")
    private boolean mqttEnable;

    MqttClient mqttClient;

    //private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    @Autowired
    WmsPredictionMapper wmsPredictionMapper;

    public void sendIncrementToUnity(List<WmsStorageLocation> wmsStorageLocations, List<String> resources, boolean isInbound) {

        // 1. put increment data
        List<Map<String, Object>> incrementList = new ArrayList<>();
        for (WmsStorageLocation wmsStorageLocation : wmsStorageLocations) {
            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("warehouse_id", wmsStorageLocation.getWarehouse_id());
            jsonData.put("location", wmsStorageLocation.getName());
            if(isInbound)
                jsonData.put("material", wmsStorageLocation.getMaterial_name());
            /*int quantity = wmsStorageLocation.getQuantity();
            if(!isInbound)
                quantity = -quantity;           //outbound will be negative
            jsonData.put("quantity", quantity);*/
            incrementList.add(jsonData);
        }

        // 2. combine then send to mqtt
        Map<String, Object> allData = new HashMap<>();
        allData.put("increment", incrementList);
        allData.put("resource", resources);

        Gson gson = new Gson();
        String content = gson.toJson(allData);

        sendMqttToUnity(mqttTopicIncrement, content, 2, false);

        System.out.println("Increment published to topic: " + mqttTopicIncrement);
        if (content.length() > 30) {
            System.out.println("Publishing message of length: " + content.length() + " characters");
            log.info("Increment published to topic: {}, content json array size: {}", mqttTopicIncrement, allData.size());
        } else {
            log.info("Increment published to topic: {}, content: {}", mqttTopicIncrement, content);
        }
    }

    public void sendIncrementFullToAI(Long warehouse_id) {

        // 1. put increment data
        List<Map<String, Integer>> fullList= wmsStorageLocationServiceImpl.groupMaterialQuantity(warehouse_id);

        Gson gson = new Gson();
        String content = gson.toJson(fullList);

        sendMqttToUnity(mqttTopicIncrementFull, content, 2, false);

        System.out.println("Increment full data published to topic: " + mqttTopicIncrementFull);
        if (content.length() > 30) {
            System.out.println("Publishing message of length: " + content.length() + " characters");
            log.info("Increment full data published to topic: {}, content json array size: {}", mqttTopicIncrementFull, fullList.size());
        } else {
            log.info("Increment full data published to topic: {}, content: {}", mqttTopicIncrementFull, content);
        }
    }

    @PostConstruct
    public void init() {
        if (!mqttEnable)
            return;
        try {
            mqttClient = new MqttClient(mqttBroker, mqttClientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setAutomaticReconnect(true);
            //connOpts.setKeepAliveInterval(30);
            connOpts.setCleanSession(false);
            connOpts.setConnectionTimeout(10);
            mqttClient.setCallback(this);
            mqttClient.connect(connOpts);
        } catch (MqttException me) {
            log.error("Initialization Error: ", me);
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if (reconnect) {
            log.info("Reconnected to : {}, Resubscribing to topics...", serverURI);
        } else {
            log.info("Connected to : {}", serverURI);
        }
        try {
            mqttClient.subscribe(mqttTopicFullRequest, 2);
            log.info("Subscribed to: {}", mqttTopicFullRequest);
        } catch (MqttException me) {
            log.error("Subscription Error: ", me);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // Handle connection lost
        log.error("Connected lost: {}", mqttBroker);
        // Schedule a reconnection attempt
        //scheduleReconnect();
    }

    /* MQTT json string to Unity
        [{"material": "SR20VET", "location": "A-01-A2"}, {"material": "VQ35DE", "location": "A-01-A3"}]
    */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String contentReceived = new String(message.getPayload());
        System.out.println("Received request: " + contentReceived);
        log.info("Received request from topic: {}, content: {}", topic, contentReceived);

        try {
            JsonObject jsonObject = JsonParser.parseString(contentReceived).getAsJsonObject();
            int warehouseId = jsonObject.get("warehouse_id").getAsInt();
            boolean useZip = jsonObject.get("use_zip").getAsBoolean();
            log.info("warehouse_id: " + warehouseId);
            log.info("use_zip: " + useZip);

            // 1. get current data
            List<Map<String, Object>> currentList = new ArrayList<>();
            List<WmsStorageLocation> listStock = wmsStorageLocationServiceImpl.selectAllStocked(warehouseId);
            for (WmsStorageLocation wmsStorageLocation : listStock) {
                Map<String, Object> jsonData = new HashMap<>();
                jsonData.put("warehouse_id", wmsStorageLocation.getWarehouse_id());
                jsonData.put("location", wmsStorageLocation.getName());
                jsonData.put("material", wmsStorageLocation.getMaterial_name());
                jsonData.put("quantity", wmsStorageLocation.getQuantity());
                currentList.add(jsonData);
            }

            // 2. put history data
            List<Map<String, Object>> historyList = new ArrayList<>();
            List<WmsPrediction> wmsPredictions = wmsPredictionMapper.CalculatePrediction();
            for (WmsPrediction wmsPrediction : wmsPredictions) {
                Map<String, Object> jsonData = new HashMap<>();
                jsonData.put("date", wmsPrediction.getDate());
                jsonData.put("count", wmsPrediction.getCount());
                historyList.add(jsonData);
            }

            //3. combine then send to mqtt
            Map<String, Object> allStock = new HashMap<>();
            allStock.put("current", currentList);
            allStock.put("history", historyList);

            Gson gson = new Gson();
            String content = gson.toJson(allStock);

            if (useZip)
                sendMqttToUnityByZip(mqttTopicFullResponse, content, 2, false);
            else
                sendMqttToUnity(mqttTopicFullResponse, content, 2, false);

            System.out.println("Full data published to topic: " + mqttTopicFullResponse);
            if (content.length() > 30) {
                System.out.println("Publishing message of length: " + content.length() + " characters");
                log.info("Full data published to topic: {}, content json array size: {}", mqttTopicFullResponse, allStock.size());
            } else {
                log.info("Full data published to topic: {}, content: {}", mqttTopicFullResponse, content);
            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            String content = "Failed to parse JSON: " + e.getMessage();
            log.error(content);
            sendMqttToUnity(mqttTopicFullResponse, content, 2, false);
        } catch (NullPointerException e) {
            String content = "Missing required JSON fields: " + e.getMessage();
            log.error(content);
            sendMqttToUnity(mqttTopicFullResponse, content, 2, false);
        } catch (JsonParseException e) {
            String content = "General JSON parsing error: " + e.getMessage();
            log.error(content);
            sendMqttToUnity(mqttTopicFullResponse, content, 2, false);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("Delivery Complete for token: {}", token.isComplete());
    }

    public boolean sendMqttToUnity(String topic, String content, int qos, boolean retained) {
        if (!mqttEnable)
            return false;
        try {
            if (content.length() > 30) {
                System.out.println("Publishing message of length: " + content.length() + " characters");
                log.info("Publishing message of length: " + content.length() + " characters");
            } else {
                System.out.println("Publishing message: " + content);
                log.info("Publishing message: " + content);
            }

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            message.setRetained(retained);
            mqttClient.publish(topic, message);
            System.out.println("Message published");
        } catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
        return true;
    }

    public void sendMqttToUnityByZip(String topic, String content, int qos, boolean retained) {
        if (!mqttEnable)
            return;
        try {
            // Compress the message content using GZIP
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(content.getBytes());
            }
            byte[] compressedContent = byteArrayOutputStream.toByteArray();

            System.out.println("Publishing compressed message");
            log.info("Publishing compressed message");
            MqttMessage message = new MqttMessage(compressedContent);
            message.setQos(qos);
            message.setRetained(retained);
            mqttClient.publish(topic, message);
            System.out.println("Compressed message published");
        } catch (Exception e) {
            System.out.println("Compression and publishing failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
