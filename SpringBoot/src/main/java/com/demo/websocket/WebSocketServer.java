package com.demo.websocket;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * webSocket服务类
 */
@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {

    static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, List<WebSocketServer>> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        logger.info("调用执行onOpen()......");
        this.session = session;
        try {
            addOnlineCount();
            sendMessage("websocket connection successfully !");
            logger.info("当前在线人数：" + onlineCount);
        } catch (IOException e) {
            logger.error("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        logger.info("调用执行onClose()......");
        // 在线人数减1
        subOnlineCount();
        logger.info("websocket用户退出，当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("开始执行onMessage()......");
        if (StringUtils.isNotEmpty(message) && session.isOpen()) {
            logger.info("websocket接收到的消息：" + message);
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("调用执行onError()，异常信息：" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        if (this.session.isOpen()) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount <= 0 ? 0 : onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


}
