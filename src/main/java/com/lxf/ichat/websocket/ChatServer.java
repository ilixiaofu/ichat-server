package com.lxf.ichat.websocket;


import com.alibaba.fastjson.JSONObject;
import com.lxf.ichat.base.BaseObservable;
import com.lxf.ichat.po.UserPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/dochat/{user}")
public class ChatServer implements Serializable {

    private static final long serialVersionUID = -602733845022657881L;

    private static final String SYSTEM_MSG_TYPE = "SYS";
    private static final String P2P_MSG_TYPE = "P2P";
    private static final String P2M_MSG_TYPE = "P2M";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final Set<ChatServer> connections = new CopyOnWriteArraySet<>();
    protected static Logger logger = LoggerFactory.getLogger(ChatServer.class);
    private Session session = null;
    private UserPO currentUser = null;



    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user) {
        try {
            user = "{" + user + "}";
            logger.info("请求参数{}", user);
            this.session = session;
            connections.add(this);
            currentUser = JSONObject.parseObject(user, UserPO.class);
            JSONObject msg = new JSONObject(true);
            msg.put("uID", currentUser.getUID());
            msg.put("nickname", currentUser.getNickname());
            msg.put("extra", "当前在线人数(" + connections.size() + ")");

            JSONObject json = new JSONObject(true);
            json.put("msgType", SYSTEM_MSG_TYPE);
            json.put("from", "SYSTEM");
            json.put("time", dateFormat.format(new Date()));
            json.put("msg", msg);
            systemBroadcast(json.toJSONString(), SYSTEM_MSG_TYPE);
        } catch (NullPointerException e) {
            logger.error("", e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info("请求参数{}", currentUser);
        JSONObject msg = new JSONObject(true);
        msg.put("uID", currentUser.getUID());
        msg.put("nickname", currentUser.getNickname());
        msg.put("extra", currentUser.getNickname() + "下线");

        JSONObject json = new JSONObject(true);
        json.put("msgType", SYSTEM_MSG_TYPE);
        json.put("from", "SYSTEM");
        json.put("time", dateFormat.format(new Date()));
        json.put("msg", msg);
        systemBroadcast(json.toJSONString(), SYSTEM_MSG_TYPE);

        BaseObservable observable = BaseObservable.getBaseObservable();
        observable.notifyObservers(this.currentUser.getUID());
        connections.remove(this);
    }

    @OnError
    public void onError(Throwable t) {
        logger.error("{}", t.getMessage());

        JSONObject msg = new JSONObject(true);
        msg.put("uID", currentUser.getUID());
        msg.put("nickname", currentUser.getNickname());
        msg.put("extra", currentUser.getNickname() + "下线");

        JSONObject json = new JSONObject(true);
        json.put("msgType", SYSTEM_MSG_TYPE);
        json.put("from", "SYSTEM");
        json.put("time", dateFormat.format(new Date()));
        json.put("msg", msg);
        systemBroadcast(json.toJSONString(), SYSTEM_MSG_TYPE);

        BaseObservable observable = BaseObservable.getBaseObservable();
        observable.notifyObservers(this.currentUser.getUID());

        connections.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("{}", message);
        JSONObject msg = JSONObject.parseObject(message);
        JSONObject to = msg.getJSONObject("to");

        JSONObject data = new JSONObject(true);
        data.put("msg", msg.getString("msg"));
        data.put("from", msg.getJSONObject("from"));
        data.put("time", dateFormat.format(new Date()));

        try {
            data.put("msgType", P2P_MSG_TYPE);
            broadcastOneToOne(data.toJSONString(), to.getString("uID"));
        } catch (NullPointerException e) {
            data.put("msgType", P2M_MSG_TYPE);
            systemBroadcast(data.toJSONString(), P2M_MSG_TYPE);
        }
    }

    private void systemBroadcast(String msg, String msgType) {
        logger.info("{}", msg);
        if (SYSTEM_MSG_TYPE.equals(msgType)) {
            for (ChatServer client : connections) {
                try {
                    client.session.getAsyncRemote().sendText(msg);
                } catch (Exception e) {
                    connections.remove(client);
                    logger.debug("", e.getMessage());
                }
            }
            return;
        }
        if (P2M_MSG_TYPE.equals(msgType)) {
            for (ChatServer client : connections) {
                if (!client.currentUser.getUID().equals(this.currentUser.getUID())) {
                    try {
                        client.session.getAsyncRemote().sendText(msg);
                    } catch (Exception e) {
                        connections.remove(client);
                        logger.debug("", e.getMessage());
                    }
                }
            }
        }
    }

    private void broadcastOneToOne(String msg, String target) {
        logger.info("{}, {}", msg, target);
        for (ChatServer client : connections) {
            if (client.currentUser.getUID().equals(target)) {
                try {
                    client.session.getAsyncRemote().sendText(msg);
                } catch (Exception e) {
                    connections.remove(client);
                    logger.debug("", e.getMessage());
                }
                break;
            }
        }
    }
}
