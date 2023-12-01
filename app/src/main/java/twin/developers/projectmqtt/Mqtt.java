package twin.developers.projectmqtt;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Mqtt {
    private static final String TAG = "MQTT";
    private static final String MQTT_SERVER = "tcp://broker.emqx.io:1883";
    private static final String CLIENT_ID = "AndroidSample12312312312312312";
    private static final String TOPIC = "iot/lab/test";
    private static String MESSAGE = "";
    private static final int QOS = 2;

    private MqttAndroidClient mqttClient;

    // Constructor de la clase Mqtt
    public Mqtt(Context context) {
        String clientId = CLIENT_ID + System.currentTimeMillis();
        String serverUri = MQTT_SERVER;

        // Inicializa el cliente MQTT
        mqttClient = new MqttAndroidClient(context.getApplicationContext(), serverUri, clientId, new MemoryPersistence());

        // Establece el callback para manejar eventos de conexión, desconexión y recepción de mensajes
        mqttClient.setCallback(new MqttCallbackExtended() {
            // Método llamado cuando el cliente se conecta al broker
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.d(TAG, "Reconnected to: " + serverURI);
                } else {
                    Log.d(TAG, "Connected to: " + serverURI);
                }
                //publishMessage();
                subscribeToTopic();
            }

            // Método llamado cuando la conexión con el broker se pierde
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "Connection lost: " + cause.getMessage());
            }

            // Método llamado cuando el cliente recibe un mensaje del broker
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, "Message received: " + new String(message.getPayload()));
            }

            // Método llamado cuando el cliente ha entregado un mensaje al broker
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "Message delivered");
            }
        });
    }

    // Método para conectar al cliente al broker MQTT
    public void connectToMqttBroker() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        try {
            mqttClient.connect(options, null, new IMqttActionListener() {
                // Método llamado cuando el cliente se conecta al broker
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Connected to MQTT broker");
                }

                // Método llamado cuando la conexión al broker falla
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to connect to MQTT broker: " + exception.getMessage());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Método para publicar un mensaje en el tema especificado
    public void publishMessage(String mensaje) {
        try {
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(QOS);
            mqttClient.publish(TOPIC, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Método para suscribir al cliente al tema especificado
    public void subscribeToTopic() {
        try {
            mqttClient.subscribe(TOPIC, QOS, null, new IMqttActionListener() {
                // Método llamado cuando el cliente se suscribe al tema
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Subscribed to topic: " + TOPIC);
                }

                // Método llamado cuando la suscripción al tema falla
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to subscribe to topic: " + TOPIC);
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Método para desconectar al cliente del broker MQTT
    public void disconnect() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}