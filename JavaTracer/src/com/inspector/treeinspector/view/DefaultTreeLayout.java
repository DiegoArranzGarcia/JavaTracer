
package com.inspector.treeinspector.view;

import static org.abego.treelayout.internal.util.Contract.checkArg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.abego.treelayout.util.AbstractTreeForTreeLayout;

public class DefaultTreeLayout<E> extends
			AbstractTreeForTreeLayout<E> {

	private List<E> emptyList;

	private List<E> getEmptyList() {
		if (emptyList == null) {
			emptyList = new ArrayList<E>();
		}
		return emptyList;
	}

	private Map<E, List<E>> childrenMap = new HashMap<E, List<E>>();
	private Map<E, E> parents = new HashMap<E, E>();

	/**
	 * Creates a new instance with a given node as the root
	 * 
	 * @param root
	 *            the node to be used as the root.
	 */
	public DefaultTreeLayout(E root) {
		super(root);
	}

	@Override
	public E getParent(E node) {
		return parents.get(node);
	}

	@Override
	public List<E> getChildrenList(E node) {
		List<E> result = childrenMap.get(node);
		return result == null ? getEmptyList() : result;
	}

	/**
	 * 
	 * @param node
	 * @return true iff the node is in the tree
	 */
	public boolean hasNode(E node) {
		return node == getRoot() || parents.containsKey(node);
	}

	/**
	 * @param parentNode
	 *            [hasNode(parentNode)]
	 * @param node
	 *            [!hasNode(node)]
	 */
	public void addChild(E parentNode, E node) {
		checkArg(hasNode(parentNode), "parentNode is not in the tree");
		checkArg(!hasNode(node), "node is already in the tree");
				
		List<E> list = childrenMap.get(parentNode);
		if (list == null) {
			list = new ArrayList<E>();
			childrenMap.put(parentNode, list);
		}
		list.add(node);
		parents.put(node, parentNode);
				
		
	}
	
	public void removeChilds(E node){
		checkArg(hasNode(node), "node is not in the tree");
		List<E> childs = childrenMap.get(node);
		if (childs !=null){
			for (int i=0;i<childs.size();i++){
				parents.remove(childs.get(i));
			}
			childs.clear();
		}
	}

	@SuppressWarnings("unchecked")
	public void addChildren(E parentNode, E... nodes) {
		for (E node : nodes) {
			addChild(parentNode, node);
		}
		
	}
	
	public List<E> getNodesAtLevel(int level){
		E rootNode = getRoot();
		List<E> nodes = getNodesAtLevelRec(rootNode,level);
		return nodes;
	}

	private List<E> getNodesAtLevelRec(E node, int level) {
		
		List<E> list = new ArrayList<E>();
		if (level == 0){
			list.add(node);
		} else {
			
			Iterator<E> children = getChildren(node).iterator();
			
			while (children.hasNext()){
				List<E> childrenList = getNodesAtLevelRec(children.next(),level-1);
				list.addAll(childrenList);
			}
			
		}
		return list;
	}

}
