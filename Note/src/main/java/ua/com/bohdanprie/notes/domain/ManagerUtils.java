package ua.com.bohdanprie.notes.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.domain.entities.AbstractText;

public final class ManagerUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> T sortByTimeCreation(List<? extends AbstractText> elements) {
		elements = elements.stream().sorted(Comparator.comparing(note -> note.getTimeCreation().getTime()))
				.collect(Collectors.toList());
		return (T) elements;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T sortByLastChange(List<? extends AbstractText> elements) {
		elements = elements.stream().sorted(Comparator.comparing(note -> note.getTimeChange().getTime()))
				.collect(Collectors.toList());
		return (T) elements;
	}
	
	public static String toJSON(List<? extends AbstractText> elements) {
		String JSON = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JSON = mapper.writeValueAsString(elements);
		} catch (JsonProcessingException e) {
			
		}
		return JSON;
	}
}
