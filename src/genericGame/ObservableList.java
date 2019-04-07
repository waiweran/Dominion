package genericGame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observer;
import java.util.Set;

/**
 * List that implements Observable
 * @author Nathaniel Brooke
 * @version 04-01-2017
 * @param <T>
 */
public class ObservableList<T> implements List<T>, Serializable {

	private static final long serialVersionUID = 1050107616843372859L;
	
	private List<T> list;
	private transient Set<Observer> observers;
	
	public ObservableList() {
		list = new ArrayList<>();
		observers = new HashSet<>();
	}
	
	public ObservableList(List<T> input) {
		this();
		list.addAll(input);
	}

	@Override
	public boolean add(T e) {
		if(list.add(e)) {
			notifyObservers();
			return true;
		}
		return false;
	}

	@Override
	public void add(int index, T element) {
		list.add(index, element);
		notifyObservers();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		if(list.addAll(c)) {
			notifyObservers();
			return true;
		}
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if(list.addAll(index, c)) {
			notifyObservers();
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		list.clear();
		notifyObservers();		
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return list.listIterator();
	}

	@Override
	public boolean remove(Object o) {
		if(list.remove(o)) {
			notifyObservers();
			return true;
		}
		return false;
	}

	@Override
	public T remove(int index) {
		T val = list.remove(index);
		notifyObservers();	
		return val;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if(list.removeAll(c)) {
			notifyObservers();
			return true;
		}
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if(list.retainAll(c)) {
			notifyObservers();
			return true;
		}
		return false;
	}

	@Override
	public T set(int index, T element) {
		T val = list.set(index, element);
		notifyObservers();
		return val;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		ObservableList<T> newList = new ObservableList<>();
		newList.addAll(list.subList(fromIndex, toIndex));
		return newList;
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <F> F[] toArray(F[] a) {
		return list.toArray(a);
	}
	
	@Override
	public String toString() {
		return list.toString();
	}
	
	/**
	 * Adds an Observer to the observable list.
	 * @param o the Observer to add.
	 */
	public void addObserver(Observer o) {
		if(observers == null) observers = new HashSet<>();
		observers.add(o);
	}
	
	/**
	 * Clears the list of observers observing this.
	 */
	public void clearObservers() {
		observers.clear();
	}
	
	/**
	 * Notifies all observers.
	 */
	private void notifyObservers() {
		for(Observer o : observers) {
			o.update(null, null);
		}
	}

}
