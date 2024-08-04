import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

class Logger {
    private final Map<String, Integer> msgtime;
    private final Queue<String> messageQueue;

    private final int boundtime = 10;
    private final int max = 100;

    public Logger() {
        msgtime = new HashMap<>();
        messageQueue = new LinkedList<>();
    }

    public boolean shouldPrintMessage(int timestamp, String message) {
        if (msgtime.containsKey(message)) {
            int last = msgtime.get(message);
            if (timestamp < last + boundtime) {
                return false;
            }
        }

        if (messageQueue.size() >= max) {
            String oldestMessage = messageQueue.poll();
            if (oldestMessage != null) {
                msgtime.remove(oldestMessage);
            }
        }

        msgtime.put(message, timestamp);
        messageQueue.offer(message);
        return true;
    }

    public boolean clean(int timestamp) {
        if (!messageQueue.isEmpty()) {
            return false;
        }
        
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.peek();
            if (msgtime.get(message) < timestamp - boundtime) {
                messageQueue.poll();
                msgtime.remove(message);
            } else {
                break;
            }
        }

        return true;
    }

    public int loggerSize() {
        return messageQueue.size();
    }

    public static void main(String[] args) {
        Logger logger = new Logger();

        System.out.println(logger.shouldPrintMessage(1, "foo")); 
        System.out.println(logger.shouldPrintMessage(2, "bar"));  
        System.out.println(logger.shouldPrintMessage(3, "foo"));  
        System.out.println(logger.shouldPrintMessage(8, "bar"));  
        System.out.println(logger.shouldPrintMessage(10, "foo")); 
        System.out.println(logger.shouldPrintMessage(11, "foo")); 
        System.out.println(logger.loggerSize()); 
        System.out.println(logger.clean(11)); 
        System.out.println(logger.clean(12)); 
    }
}
