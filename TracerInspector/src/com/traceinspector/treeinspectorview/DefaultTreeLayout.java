/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.traceinspector.treeinspectorview;

import static org.abego.treelayout.internal.util.Contract.checkArg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;

/**
 * Provides a generic implementation for the {@link TreeForTreeLayout}
 * interface, applicable to any type of tree node.
 * <p>
 * It allows you to create a tree "from scratch", without creating any new
 * class.
 * <p>
 * To create a tree you must provide the root of the tree (see
 * {@link #DefaultTreeForTreeLayout(Object)}. Then you can incrementally
 * construct the tree by adding children to the root or other nodes of the tree
 * (see {@link #addChild(Object, Object)} and
 * {@link #addChildren(Object, Object...)}).
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * @param <TreeNode>
 */
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

}
