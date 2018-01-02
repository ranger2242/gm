package com.quadx.asteroids.tools;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.states.LobbyState;
import com.quadx.asteroids.states.MultiplayerState;
import com.quadx.asteroids.states.RoomState;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.quadx.asteroids.tools.Game.*;

/**
 * Created by Chris Cavazos on 12/10/2017.
 */
public class Com {
    ArrayList<String> roomPlayers = new ArrayList<>();
    ArrayList<String> lobbiedPlayers = new ArrayList<>();
    ArrayList<String> rooms = new ArrayList<>();

    private String ip = getIp();
    private String port = "2242";
    private String dir = "http://" + ip + ":" + port;
    private Socket socket;
    private final Gson gson;
    private int index = 0;
    public String serverID = "";
    private String numInLobby = "";
    public String room = "";
    public int color = 0;

    public Com() {
        gson = new Gson();

    }

    public void connect(String ip) {
        try {
            dir = "http://" + ip + ":" + port;
            connectSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void setIP(String IP) {
        ip = IP;
        dir = "http://" + ip + ":" + port;

        //outc("IP set to " + ip);
    }

    void log(String msg) {
        //outc(msg);
        socket.emit("log", msg);
    }

    private void connectSocket() throws URISyntaxException {
        socket = IO.socket(dir);

        socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {//on connect
            @Override
            public void call(Object... args) {
                String msg = "-Connection Established-";
                //outc(msg);
                socket.emit("getSocketID", msg);//ask server for socketID
            }

        });
        socket.on("start", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                RoomState.loadGame();
            }

        });
        socket.on("storeID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                serverID = args[0].toString();
                numInLobby = args[1].toString();
                System.out.println(serverID + " " + numInLobby);
            }

        });
        socket.on("sendPlayerList", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String s = args[0].toString();
                String[] arr = s.split("~");
                lobbiedPlayers = new ArrayList<>(Arrays.asList(arr));
                for (String s1 : lobbiedPlayers) {
                    System.out.println(s1);
                }
            }

        });
        socket.on("receiveData", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String s = args[0].toString();
                String[] arr = s.split("~");
                int n = arr.length;
                Triangle[] ts = new Triangle[n];
                float r= AsteroidState.player.getR()*sclf;
                for (int i = 0; i < n; i++) {
                    String[] arr2 = arr[i].split(" ");
                    float x = Float.parseFloat(arr2[0])*res.x;
                    float y = Float.parseFloat(arr2[1])*res.y;
                    float a = Float.parseFloat(arr2[2])*1000;
                    ts[i]=new Triangle(new Vector2(x,y),a,r);
                }
                MultiplayerState.setOtherPlayers(ts);
            }

        });
        socket.on("roomNames", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String s = args[0].toString();
                String[] arr = s.split("~");
                roomPlayers = new ArrayList<>(Arrays.asList(arr));
            }

        });
        socket.on("sendRoomList", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String s = args[0].toString();
                String[] arr = s.split("~");
                rooms = new ArrayList<>(Arrays.asList(arr));
                LobbyState.setRooms = true;
            }

        });
        socket.on("joinSuccess", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                room = args[0].toString();
                color= Integer.parseInt(args[1].toString());
                RoomState state = new RoomState(gsm, room);
                gsm.pop();
                gsm.push(state);
            }

        });
        socket.on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        });
        socket.connect();
    }


    private String getIp() {

        return "localhost";
        //return "127.0.0.1";
        // return "0.0.0.0";
        /*
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (whatismyip != null) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));
                String ip = in.readLine();
               //return InetAddress.getLocalHost().getHostAddress();
                 //return ip;
                return "localhost";
               //return "127.0.0.1";
               // return "0.0.0.0";
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "null IP";
        */
    }

    public ArrayList<String> getLobbied() {
        return lobbiedPlayers;
    }

    public ArrayList<String> getRoom() {
        return roomPlayers;
    }

    public void emit(String msg, String... n) {
        socket.emit(msg, n);
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }

    public void dispose() {
        if (!room.equals("")) {
            socket.emit("dropPlayer", room, serverID);
        }
    }
}
