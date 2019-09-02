package in.fabits.fabits.api;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;

public class Socket {

    public static Channel myChannel;
    public static Channel fabitsChannel;
    //    public static Channel publicChannel = pusher.subscribe("fabitsApp");
    static Pusher pusher;

    public Socket() {
        pusher = new Pusher("9dbdbd3b88e6b4a2c2f0");
        myChannel = pusher.subscribe("fabits" + ApiUtil.getUserId());
        fabitsChannel = pusher.subscribe("fabits");
        pusher.connect();
    }
}
