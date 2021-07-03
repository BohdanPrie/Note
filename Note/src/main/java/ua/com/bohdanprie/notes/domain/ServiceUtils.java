package ua.com.bohdanprie.notes.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.domain.entity.AbstractTextContainer;
import ua.com.bohdanprie.notes.domain.entity.ToDo;
import ua.com.bohdanprie.notes.domain.entity.ToDoLine;

public final class ServiceUtils {
	private static final Logger LOG = LogManager.getLogger(ServiceUtils.class.getName());
	
	@SuppressWarnings("unchecked")
	public static <T> T sortByTimeCreation(List<? extends AbstractTextContainer> elements) {
		LOG.trace("Sorting data by time creation");
		elements = elements.stream().sorted(Comparator.comparing(note -> note.getTimeCreation().getTime()))
				.collect(Collectors.toList());
		LOG.info("Returning sorted by time creation data");
		return (T) elements;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T sortByLastChange(List<? extends AbstractTextContainer> elements) {
		LOG.trace("Sorting data by last change");
		elements = elements.stream().sorted(Comparator.comparing(note -> note.getTimeChange().getTime()))
				.collect(Collectors.toList());
		LOG.info("Returning sorted by last change data");
		return (T) elements;
	}
	
	public static String toJSON(List<? extends AbstractTextContainer> elements) {
		LOG.trace("Converting data to JSON");
		String JSON = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JSON = mapper.writeValueAsString(elements);
		} catch (JsonProcessingException e) {
			LOG.warn("Fail to convert data to JSON");
		}
		LOG.info("Returning JSON of data");
		return JSON;
	}
	
	public static void combineResult(List<ToDoLine> values, Map<Integer, ArrayList<ToDo>> arrays){
		LOG.trace("Combining user's data");
		for(int i = 0; i < values.size(); i++) {
			int id = values.get(i).getId();
			values.get(i).setToDo(arrays.get(id));
		}
		LOG.info("Combining complited");
	}
}
