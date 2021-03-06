package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

/**
 * Min heap. Elements are key:weight pairs. Ops are:
 * 1. construct from set of key:value pairs (a hashmap), 0(n)
 * 2. enqueue a new or existing key with its current weight, 0(log n)
 * 3. dequeue the key with the minimum weight, 0(log n)
 * 4. empty tells if the heap is empty, 0(1)
 * 
 * @author ericnunn
 *
 */
public class Heap {
	Map<String, Integer> weights;
	Map<String, Integer> place;
	List<String> heap;
	
	/**
	 * Builds a min heap from an initial HashMap, and keeps track
	 * of the location of each item in the heap so that its weight
	 * can be changed (and the heap repaired in 0(log n) time.
	 * @param initial HashMap to heapify
	 */
	public Heap(HashMap<String, Integer> initial) {
		this.weights = new HashMap<>();
		this.place = new HashMap<>();
		this.heap = new ArrayList<>();
		
		Integer last = 0;
		for (String key : initial.keySet()) {
			this.heap.add(key);
			this.weights.put(key, initial.get(key));
			this.place.put(key, last);
			last++;
			this.heapConstruct();
		}
	} // end constructor
	
	/**
	 * Returns true if there are no more items in the heap.
	 * @return true if the heap is empty, otherwise false
	 */
	public boolean empty() {
		return this.heap.size() == 0;
	} // end empty
	
	/**
	 * Put key into heap with given weight. If key is already present,
	 * this will change the heap as necessary.
	 * @param key	key of new key:weight pair to add to the heap
	 * @para weight	weight of the key:weight pair to add to the heap
	 */
	public void enqueue(String key, Integer weight) {
		Integer old_w;
		Integer i;
		
		if (this.weights.containsKey(key)) {
			old_w = this.weights.get(key);
			i = this.place.get(key);
		} else {
			this.heap.add(key);
			old_w = null;
			i = this.last();
			this.place.put(key, i);
		}
		
		this.weights.put(key, weight);
		
		if (old_w != null || old_w > weight) {
			this.swapUp(i);
		} else if (old_w < weight) {
			this.swapDown(i);
		}
	} // end enqueue
	
	/**
	 * Remove the minimum element and return its key and weight.
	 * @return
	 */
	public Pair<String, Integer> dequeue() {
		Integer last = this.last();
		if (last < 0) {
			return null;
		}
		
		// retrieve the minimum element (at the root)
		String key = this.heap.get(0);
		Integer weight = this.weights.get(key);
		this.weights.remove(key);
		this.place.remove(key);
		
		// remove the last element and place it at the root, then fix the heap
		if (last > 0) {
			this.heap.set(0, this.heap.remove(this.heap.size() - 1));
			this.place.put(this.heap.get(0), 0);
			this.swapDown(0);
		} else {
			this.heap.remove(0);
		}
		
		return new Pair<String, Integer>(key, weight);
	} // end dequeue
	
	/**
	 * Turn this.heap into a heap.
	 */
	private void heapConstruct() {
		Integer last_parent = this.parent(this.last());
		for (int i = last_parent; i >= 0; i--) {
			this.swapDown(i);
		}
	} // end heapConstruct
	
	/**
	 * Returns the weight of the given integer. Null is returned if i is not in the heap.
	 * @param i	index of element
	 * @return the weight of the ith element, if it exists
	 */
	private Integer weight(Integer i) {
		if (i > this.last()) {
			return null;
		}
		return this.weights.get(this.heap.get(i));
	}
	
	/**
	 * Returns the last index of the heap
	 * @return last index of the heap
	 */
	private Integer last() {
		return this.heap.size() - 1;
	} // end last
	
	/**
	 * Returns the index of the parent node in the heap
	 * @param i	index of element
	 * @return index of the ith element's parent
	 */
	private Integer parent(Integer i) {
		return (i-1) >> 1;
	}
	
	/**
	 * Returns a Pair object of both children of the given element in the heap.
	 * @param p
	 * @return children elements pair
	 */
	private Pair<Integer, Integer> children(Integer p) {
		Integer left = (p<<1) + 1;
		Integer right = left + 1;
		return new Pair<Integer, Integer>(left, right);
	}
	
	/**
	 * Recursively swap elements up the heap to restore min-heap properties.
	 * @param i	element to swap up the heap
	 */
	private void swapUp(Integer i) {
		if (i > 0) {
			Integer p = this.parent(i);
			if (this.weight(i) < this.weight(p)) {
				String temp = this.heap.get(p);
				this.heap.add(p, this.heap.get(i));
				this.heap.add(i, temp);
				this.place.put(this.heap.get(i), i);
				this.place.put(this.heap.get(p), p);
				this.swapUp(p);
			}
		}
	} // end swapUp
	
	/**
	 * Recursively swap elements down the heap to restore min-heap properties.
	 * @param p	element to swap down the heap
	 */
	private void swapDown(Integer p) {
		Integer child = this.children(p).getKey();
		Integer right = this.children(p).getValue();
		
		Integer child_w = this.weight(child);
		if (child_w == null || this.weight(p) == null) {
			return;
		}
		
		Integer right_w = this.weight(right);
		if (right_w != null && right_w < child_w) {
			child = right;
			child_w = right_w;
		}
		
		if (this.weight(p) > child_w) {
			String temp = this.heap.get(child);
			this.heap.set(child, this.heap.get(p));
			this.heap.set(p, temp);
			this.place.put(this.heap.get(child), child);
			this.place.put(this.heap.get(p), p);
			this.swapDown(child);
		}
		
	} // end swapDown
} // end Heap
