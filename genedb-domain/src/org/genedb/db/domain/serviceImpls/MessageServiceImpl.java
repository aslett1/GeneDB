package org.genedb.db.domain.serviceImpls;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.genedb.db.domain.misc.Message;
import org.genedb.db.domain.services.MessageService;
import org.gmod.schema.utils.CollectionUtils;

public class MessageServiceImpl implements MessageService {
	
	private Map<String, Deque<Message>> messageMap = new HashMap<String, Deque<Message>>();

	public synchronized void addNotification(String clientName, String string, String string2) {
		if (!messageMap.containsKey(clientName)) {
			messageMap.put(clientName, new ArrayDeque<Message>());
		}
		Deque<Message> messages = messageMap.get(clientName);
		messages.addLast(new Message());
	}

	public synchronized Deque<Message> checkMessages(String clientName) {
		return messageMap.get(clientName);
	}

	
	
}
