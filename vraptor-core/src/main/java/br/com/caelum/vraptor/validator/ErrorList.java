package br.com.caelum.vraptor.validator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Vetoed;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingList;

/**
 * Class that represents an error list.
 * 
 * @author Otávio Scherer Garcia
 */
@Vetoed
public class ErrorList extends ForwardingList<Message> {

	private static Function<Message, String> byCategory = new Function<Message, String>() {
		@Override
		public String apply(Message input) {
			return input.getCategory();
		}
	};

	private final List<Message> delegate;
	private Map<String, Collection<Message>> grouped;

	public ErrorList(List<Message> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Return messages grouped by category. This method can useful if you want to get messages for a specific
	 * category.
	 */
	public Map<String, Collection<Message>> getGrouped() {
		if (grouped == null) {
			grouped = FluentIterable.from(delegate).index(byCategory).asMap();
		}
		return grouped;
	}

	/**
	 * Return all messages by category. This method can useful if you want to get messages for a specific
	 * category.
	 */
	public Collection<Message> from(String category) {
		return getGrouped().get(category);
	}

	@Override
	protected List<Message> delegate() {
		return delegate;
	}
}
