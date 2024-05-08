package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.supos.app.entity.WmsStorageLocation;
import com.supos.app.entity.WmsThreedWarehouse;
import com.supos.app.service.WmsStorageLocationService;
import com.supos.app.mapper.WmsStorageLocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.GZIPOutputStream;

/**
* @author Wenhao
* @description 针对表【wms_storage_location】的数据库操作Service实现
* @createDate 2024-03-15 23:19:11
*/
@Slf4j
@Service
public class WmsStorageLocationServiceImpl extends ServiceImpl<WmsStorageLocationMapper, WmsStorageLocation>
    implements WmsStorageLocationService, MqttCallbackExtended{

    @Autowired
    private WmsStorageLocationMapper wmsStorageLocationMapper;

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

    @Value("${mqtt.enable}")
    private boolean mqttEnable;

    //@Autowired
    //WmsThreedWarehouseMapper wmsThreedWarehouseMapper;

    MqttClient mqttClient;

    //private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    // MQTT json string to Unity, inbound: {"material": "SR20VET", "location": "A-01-A2"}, outbound: {"location": "A-01-A2"}
    public int updateSelectiveByLocationId(WmsStorageLocation wmsStorageLocation) {
        String locationName = "";
        List<WmsStorageLocation> storageLocations = wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation);
        if (!storageLocations.isEmpty()) {
            locationName = storageLocations.get(0).getName();
        }
        Gson gson = new Gson();
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("material", wmsStorageLocation.getMaterial_name());
        jsonData.put("location", locationName);
        // Wrap your map inside a list to form a single-element array
        String content = gson.toJson(Collections.singletonList(jsonData));
        sendMqttToUnity(content, 2, false);
        return wmsStorageLocationMapper.updateStorageLocationById(wmsStorageLocation);
    }

    @PostConstruct
    public void init() {
        if (!mqttEnable)
            return;
        try {
            mqttClient = new MqttClient(mqttBroker, mqttClientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setAutomaticReconnect(true);
            connOpts.setKeepAliveInterval(30);
            connOpts.setCleanSession(false);
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
        // This method is called when a message arrives from the server.
        System.out.println("Received request: " + new String(message.getPayload()));
        log.info("Received request from topic: {}, content: {}", topic, new String(message.getPayload()));

        // Return all material name and storage location name mapping list as response
        List<WmsStorageLocation> listAll = wmsStorageLocationMapper.selectAllStocked();
        List<Map<String, Object>> listOfMaps = new ArrayList<>();

        //int count = 0; // Counter to track the number of added elements
        for (WmsStorageLocation wmsStorageLocation : listAll) {
            //if (count >= 2) break; // Stop after adding two elements

            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("material", wmsStorageLocation.getMaterial_name());
            jsonData.put("location", wmsStorageLocation.getName());
            listOfMaps.add(jsonData);

            //count++; // Increment the counter
        }

        Gson gson = new Gson();
        String content = gson.toJson(listOfMaps);
        sendMqttToUnityByZip(content, 2, false);
        // Publish the response to the response topic
        //MqttMessage responseMessage = new MqttMessage(content.getBytes());
        //responseMessage.setQos(2); // Matching QoS for example
        //mqttClient.publish(mqttTopicFullResponse, responseMessage);

        System.out.println("Response published to topic: " + mqttTopicFullResponse);
        if (content.length() > 30) {
            System.out.println("Publishing message of length: " + content.length() + " characters");
            log.info("Response published to topic: {}, content json array size: {}", mqttTopicFullResponse, listOfMaps.size());
        } else {
            log.info("Response published to topic: {}, content: {}", mqttTopicFullResponse, content);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("Delivery Complete for token: {}", token.isComplete());
    }

    public void sendMqttToUnity(String content, int qos, boolean retained) {
        if (!mqttEnable)
            return;
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
            mqttClient.publish(mqttTopicIncrement, message);
            System.out.println("Message published");
        } catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void sendMqttToUnityByZip(String content, int qos, boolean retained) {
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
            mqttClient.publish(mqttTopicFullResponse, message);
            System.out.println("Compressed message published");
        } catch (Exception e) {
            System.out.println("Compression and publishing failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int insertSelective(WmsStorageLocation wmsStorageLocation) {
        return wmsStorageLocationMapper.insertSelective(wmsStorageLocation);
    }

    public int updateStorageLocationById(WmsStorageLocation wmsStorageLocation) {
        return wmsStorageLocationMapper.updateStorageLocationById(wmsStorageLocation);
    }

    public int deleteStorageLocationById(WmsStorageLocation wmsStorageLocation) {
        return wmsStorageLocationMapper.deleteStorageLocationById(wmsStorageLocation);
    }

    public List<WmsStorageLocation> selectAll(WmsStorageLocation wmsStorageLocation) {
        return wmsStorageLocationMapper.selectAll(wmsStorageLocation);
    }
}




