package com.quadx.asteroids.tools;

import com.google.gson.Gson;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.states.AsteroidState;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Created by Chris Cavazos on 12/10/2017.
 */
public class Com {
    private String ip = getIp();
    private String port = "2242";
    private String dir = "http://" + ip + ":" + port;
    private Socket socket;
    private final Gson gson;
    private int index = 0;
    private String id = "";

    public Com(){
        gson = new Gson();

    }

    public void connect() {
        connectionHandler();
    }
    private void connectionHandler() {
        //outc("Attemping to connect");

        try {
            connectSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //outc("Socket opened on " + dir);


    }

    public boolean validIP(String ip) {
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

            }

        });
        socket.on("syncID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String[] s = gson.fromJson(args[0].toString(), String[].class);
                index = Arrays.asList(s).indexOf(id);
            }

        });
        socket.on("allDead", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
             /*   gameover = gson.fromJson(args[0].toString(), Boolean.class);
                if (gameover) {
                    socket.emit("stop");
                    started = false;
                    player.setHost(false);
                    rocks.clear();
                    Sounds.gameOver.play();

                }*/
            }

        });
        socket.on("storeIndex", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                index = Integer.parseInt(args[0].toString());
                id = args[1].toString();
            }

        });
        socket.on("receiveCount", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
               // MenuState.numPlayers = gson.fromJson(args[0].toString(), Integer.class);
            }

        });

        socket.on("receiveRocks", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                AsteroidState.setRocks(gson.fromJson(args[0].toString(), Asteroid[].class));
            }

        });
        socket.on("removeRock", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
               /* int x = rocks.size();
                int z = gson.fromJson(args[0].toString(), Integer.class);

                try {
                    rocks.remove(z);
                } catch (IndexOutOfBoundsException | ConcurrentModificationException e) {
                }
                int y = rocks.size();
                System.out.println(x + " " + z + " " + y);*/
            }

        });
        socket.on("addRocks", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                /*rocks.add(0, gson.fromJson(args[1].toString(), Asteroid.class));
                rocks.add(0, gson.fromJson(args[0].toString(), Asteroid.class));*/
            }

        });
        socket.on("addRock", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
              //  rocks.add(0, gson.fromJson(args[0].toString(), Asteroid.class));
            }

        });
        socket.on("receiveShips", new Emitter.Listener() {//on connect
            @Override
            public void call(Object... args) {

          /*      otherPlayers = gson.fromJson(args[0].toString(), Triangle[].class);
                ArrayList<Triangle> a = new ArrayList<>(Arrays.asList(otherPlayers));
                a.remove(index);
                otherPlayers = Arrays.copyOf(a.toArray(), a.size(), Triangle[].class);
*/
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

}
