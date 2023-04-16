# Local Chatik

Local Chatik is a console network chat.

## Usage
------

Firstly you have

In order to launch server, run ChatServer.java.

```bash
java ChatServer
```

Ofc dont forget to complite it.

```bash
javac ChatServer.java
```

Then for each new user run Client.java.

```bash
java Client.java
```

If you want to chat from different devices change in **_settings.txt_** in resources folder **_SERVER_HOST_** from *"localhost"* to your IP address.

*Also you can change port in this file.*

Every message will be logged to **_file.log_**.

## How it works
------

### Chat Server

Initialy ChatServer sets logger, port. Then creating Server Socket. And finally it starts accepts clients.

```java
private static void acceptClients() {
    while (true) {
        try {
            Socket client = serverSocket.accept();
            new ServerClient(client).start();
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }
}
```

As server accepts new client it's creating object of **ServerClient** class which inherits **Thread**. This allows us hadle each client separately in parallel.

But before user will be added to chat he have to enter his name. So after we get out and in streams from client we send him *Enter user name: * after we get his name we write it into ServerClient field **_userName_**. Then calls enterChat function which adds user to chat.

ChatServer has private constructor, so for interaction with server exists public static function.

So in order to add user to the chat there is function  **_addToChat_**.

```java
public static void addToChat(ServerClient client) {
    clients.add(client);
}
```
*Clients is ChatServer field (`ArrayList<ServerClients>`) wich contains all users.*


Then calls **_reveiveMessages_** ServerClient function. There start "while" cycle which wait for message from client until he enter *"/exit"*. Otherwise then calls **_sendInChat_** ChatServer function which receives message it self and who sent it.

```java
    public static void sendInChat(String msg, ServerClient sender) {
        String messageLine = "[" + dtf.format(LocalDateTime.now()) + "]  " + sender.getUserName() + ": " + msg;
        logger.log(messageLine);
        sendEveryoneExcept(messageLine, sender);
    }
```

```java
    private static void sendEveryoneExcept(String msg, ServerClient exclude) {
        new Thread(() -> {
            for (ServerClient client : clients) {
                if (client.equals(exclude)) continue;
                try {
                    client.sendMsg(msg);
                } catch (IOException ignored) {
                }
            }
        }).start();
    }
```

Since there is no need to send message to the user who sended it if ingoring him. Sending message performs in other thread therfore user dont have to wait until message will sended for each client to send another message.

Since function sendMsg need client OutputStream and performs in other thread this stream can be closed add that will cause Exception we synchronizing by clientSocket and checks if it closed.

```java
    ...
    if(!clientSocket.isClosed()){
        synchronized (clientSocket) {
            if(!clientSocket.isClosed()){
                clientWriter.write((msg + "\n").getBytes());
                clientWriter.flush();
            }
        }
    }   
    ...
```

If client enter *"/exit"* calls **_leaveChat_** ServerClient function which calls ChatServer function **_removeFromChat_**.

```java
public static void removeFromChat(ServerClient client) {
    clients.remove(client);
}
```

Then it enters synchronized by **_clientSocket_** block add closing clients streams and socket.

### Overall

Each client creates ServerClient and represents a separate thread and every message sending creating a new Thread.

## Client
------

In **_main_** function firstly creates server Socket using *host* and *port* from **settings.txt**. Than retrives inputstream and outputstream from server using **_setIO_** function.

```java
public class Client {
    private static OutputStream out;
    private static BufferedReader in;

    ...
    public static void setIO() throws IOException {
        out = server.getOutputStream();
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }
    ...
}
```

Client has two inner classes **MessageSender** and **MessageReceiver** responsible for sending user messages to the chat and receiving messages from server respectively. Each class inherits Thread so they could perform in parallel.

If user input *"/exit"* MessageSender stops execution and closing streams, thereby stops execution of MessageReceiver.

### Overall
Client creates two additional thread to use chat. As he connects to the server in order to enter the chat he will have to enter his name. And to leave the chat he have to enter *"/exit"*.