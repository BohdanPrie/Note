package ua.com.bohdanprie.notes.domain;

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
/**
 * Class provide methods to work with elements (change, sort, combine)
 * @author bohda
 *
 */
public final class ServiceUtils {
	private static final Logger LOG = LogManager.getLogger(ServiceUtils.class.getName());
	/**
	 * Method sort elements by time creation
	 * Note: parameter elements must be subclass of {@link AbstractTextContainer}
	 * @param <T>
	 * @param elements
	 * @return Collection of elements, sorted by time creation
	 */
	@SuppressWarnings("unchecked")
	public static <T> T sortByTimeCreation(List<? extends AbstractTextContainer> elements) {
		LOG.trace("Sorting data by time creation");
		elements = elements.stream().sorted(Comparator.comparing(note -> note.getTimeCreation().getTime()))
				.collect(Collectors.toList());
		LOG.info("Returning sorted by time creation data");
		return (T) elements;
	}
	/**
	 * Method sort elements by time of last change
	 * Note: parameter elements must be subclass of {@link AbstractTextContainer}
	 * @param <T>
	 * @param elements
	 * @return Collection of elements, sorted by time of last change
	 */
	@SuppressWarnings("unchecked")
	public static <T> T sortByLastChange(List<? extends AbstractTextContainer> elements) {
		LOG.trace("Sorting data by last change");
		elements = elements.stream().sorted(Comparator.comparing(note -> note.getTimeChange().getTime()))
				.collect(Collectors.toList());
		LOG.info("Returning sorted by last change data");
		return (T) elements;
	}
	/**
	 * Converts given Collection of elements to JSON 
	 * @param <T>
	 * @param elements
	 * @return {@link String} representation of given Collection of elements
	 */
	public static <T> String toJSON(List<T> elements) {
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
	/**
	 * Method takes returned from DB {@link ToDoLine}'s and and combine it with returned from DB 
	 * <br>Map of Integer value and List of {@link ToDo}. Where Integer value is {@link ToDoLine}'s id
	 * <br>Note: this method is a prototype, 
	 * <br>in future it will be updated to work with other types of data
	 * @param values
	 * @param arrays
	 */
	public static void combineResult(List<ToDoLine> values, Map<Integer, List<ToDo>> arrays){
		LOG.trace("Combining user's data");
		if(values == null || arrays == null) {
			LOG.trace("Fail to combine user's data, data are null");
			return;
		}
		for(int i = 0; i < values.size(); i++) {
			int id = values.get(i).getId();
			values.get(i).setToDo(sortById(arrays.get(id)));
		}
		LOG.info("Combining complited");
	}
	/**
	 * Method takes Collection of {@link ToDo} and sort it by id
	 * <br>Note: this method is a prototype, 
	 * <br>in future it will be updated to work with other types of data
	 * @param elements
	 * @return Collection of sorted by id elements
	 */
	public static List<ToDo> sortById(List<ToDo> elements) {
		LOG.trace("Sorting elements by id");
		if(elements == null) {
			LOG.debug("Elements are null");
			return elements;
		}
		elements = elements.stream().sorted(Comparator.comparing(element -> element.getId())).collect(Collectors.toList());
		LOG.info("Returning sorted by id elements");
		return elements;
	}
}