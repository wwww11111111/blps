package com.example.xddd.jms;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class JmsSender {

    MqttAsyncClient aClient = new MqttAsyncClient("tcp://localhost:1883",
            "testClientLmao2");
    MqttConnectOptions options = new MqttConnectOptions();


    public JmsSender() throws MqttException {
        aClient.connect(options, new IMqttActionListener() {
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Connected");
            }

            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Connection failed: " + exception);
            }
        });
    }

    public static void main(String[] args) throws MqttException, InterruptedException {
        MqttAsyncClient aClient = new MqttAsyncClient("tcp://localhost:1883",
                "testClientLmao2");
        MqttConnectOptions options = new MqttConnectOptions();
        Scanner scanner = new Scanner(System.in);

        aClient.connect(options, new IMqttActionListener() {
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Connected");
            }

            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Connection failed: " + exception);
            }
        });
        while (true) {
            scanner.nextLine();
            MqttMessage msg = new MqttMessage("test".getBytes());
            msg.setRetained(true);
            aClient.publish("fillUp", msg);

        }
    }

    public void send(String message, String topic) throws MqttException {
        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setRetained(true);
        aClient.publish(topic, msg);
    }
}
