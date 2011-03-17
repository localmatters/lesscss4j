/*
   Copyright 2010-present Local Matters, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.localmatters.lesscss4j.parser;

import java.util.List;

import org.antlr.runtime.tree.Tree;

public class StyleSheetTree implements Tree {
    private Tree _delegate;
    private StyleSheetResource _resource;

    public StyleSheetTree(Tree delegate, StyleSheetResource resource) {
        _delegate = delegate;
        _resource = resource;
    }

    public Tree getDelegate() {
        return _delegate;
    }

    public StyleSheetResource getResource() {
        return _resource;
    }

    public Tree getChild(int i) {
        return getDelegate().getChild(i);
    }

    public int getChildCount() {
        return getDelegate().getChildCount();
    }

    public Tree getParent() {
        return getDelegate().getParent();
    }

    public void setParent(Tree t) {
        getDelegate().setParent(t);
    }

    public boolean hasAncestor(int ttype) {
        return getDelegate().hasAncestor(ttype);
    }

    public Tree getAncestor(int ttype) {
        return getDelegate().getAncestor(ttype);
    }

    public List getAncestors() {
        return getDelegate().getAncestors();
    }

    public int getChildIndex() {
        return getDelegate().getChildIndex();
    }

    public void setChildIndex(int index) {
        getDelegate().setChildIndex(index);
    }

    public void freshenParentAndChildIndexes() {
        getDelegate().freshenParentAndChildIndexes();
    }

    public void addChild(Tree t) {
        getDelegate().addChild(t);
    }

    public void setChild(int i, Tree t) {
        getDelegate().setChild(i, t);
    }

    public Object deleteChild(int i) {
        return getDelegate().deleteChild(i);
    }

    public void replaceChildren(int startChildIndex, int stopChildIndex, Object t) {
        getDelegate().replaceChildren(startChildIndex, stopChildIndex, t);
    }

    public boolean isNil() {
        return getDelegate().isNil();
    }

    public int getTokenStartIndex() {
        return getDelegate().getTokenStartIndex();
    }

    public void setTokenStartIndex(int index) {
        getDelegate().setTokenStartIndex(index);
    }

    public int getTokenStopIndex() {
        return getDelegate().getTokenStopIndex();
    }

    public void setTokenStopIndex(int index) {
        getDelegate().setTokenStopIndex(index);
    }

    public Tree dupNode() {
        return getDelegate().dupNode();
    }

    public int getType() {
        return getDelegate().getType();
    }

    public String getText() {
        return getDelegate().getText();
    }

    public int getLine() {
        return getDelegate().getLine();
    }

    public int getCharPositionInLine() {
        return getDelegate().getCharPositionInLine();
    }

    public String toStringTree() {
        return getDelegate().toStringTree();
    }

    @Override
    public String toString() {
        return getDelegate().toString();
    }
}
